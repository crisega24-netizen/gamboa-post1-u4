# Post-contenido — Unidad 4: Patrones de Comportamiento en ComprasUDES

## Descripción
Repositorio del post-contenido de la Unidad 4 de Patrones de Diseño
de Software. Un único proyecto Spring Boot (compras-comportamiento)
que resuelve cuatro necesidades reales del backend de ComprasUDES,
el sistema interno de solicitudes de compra corporativas: aprobación
por niveles jerárquicos, ejecución reversible de solicitudes
aprobadas, notificaciones ante cambios de estado y reglas de
transición según el estado actual de la solicitud.

## Cómo ejecutar

```
$ mvn clean package
$ mvn spring-boot:run
$ mvn test
```

## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos
Se aplicó **Chain of Responsibility**. El problema central es que una
solicitud debe recorrer una secuencia de decisores (Supervisor de Área,
Gerente de Área, Director Financiero, y opcionalmente el Revisor de
Cumplimiento Normativo) donde cada uno evalúa si tiene autoridad para
resolverla o debe delegarla al siguiente, sin que `ControladorSolicitudes`
conozca cuántos niveles existen ni en qué orden se consultan. La clase
abstracta `NivelAprobacion` mantiene una referencia al siguiente nivel y
decide, mediante `aplica()`, si resuelve la solicitud o la delega. El
`RevisorCumplimientoNormativo` se ubica siempre al inicio de la cadena,
pero solo "aplica" cuando la categoría es `INTERNACIONAL`; para cualquier
otra categoría delega de inmediato, sin que el ensamblador de la cadena
necesite lógica condicional por categoría.

Se descartó **Command** porque este problema no consiste en encapsular
una acción reversible que alguien ejecuta y puede deshacer más tarde —
consiste en encontrar, entre varios decisores independientes, cuál tiene
la autoridad para resolver la solicitud en este momento. Ese "avanzar por
una cadena hasta que alguien resuelve" es exactamente lo que Command no
modela: Command encapsula una operación, no una búsqueda de responsable.

### Necesidad 2 — Ejecución reversible de solicitudes
Se aplicó **Command**. Reservar presupuesto y generar la orden de compra
son operaciones discretas que el equipo de Compras decide ejecutar y,
eventualmente, deshacer de forma independiente. Cada operación se
encapsuló como un objeto (`ReservarPresupuestoCommand`,
`GenerarOrdenCompraCommand`) que implementa `Operacion` con `ejecutar()` y
`deshacer()`, envolviendo a `PresupuestoService` y `OrdenCompraService`
sin modificarlos. El `EjecutorSolicitud` mantiene un historial completo
(`List<Operacion>`) — no solo la última operación — y una pila separada
para deshacer siempre la más reciente sin afectar a las anteriores.

Se descartó **Chain of Responsibility** porque aquí no hay ningún decisor
evaluando condiciones para decidir si resuelve o delega una petición
entrante: hay operaciones que un mismo actor ejecuta directamente y que
deben quedar registradas en orden para poder inspeccionarse o revertirse
después. No existe la noción de "pasar la solicitud al siguiente" — cada
comando se ejecuta cuando se le pide, sin buscar un responsable.

### Necesidad 3 — Notificaciones ante cambio de estado
Se aplicó **Observer**. Cada vez que una solicitud cambia de estado —ya
sea por la evaluación de niveles (Necesidad 1) o por la ejecución de
operaciones (Necesidad 2)— tres módulos completamente ajenos a la
`Solicitud` (correo, dashboard de contabilidad, auditoría) deben
enterarse y reaccionar. `PublicadorEstado` mantiene la lista de
suscriptores (`SuscriptorNotificacion`) y notifica a todos cuando se le
invoca `notificarCambioEstado(solicitud)`. Ni `ServicioAprobacionImpl` ni
`EjecutorSolicitud` conocen los tres módulos concretos: solo conocen la
interfaz `SuscriptorNotificacion`. Un test agrega un cuarto suscriptor
(un colector de prueba) sin modificar `PublicadorEstado` ni ningún
suscriptor existente, demostrando que el mecanismo es abierto a
extensión sin modificación.

Se descartó **State** (Necesidad 4) porque el comportamiento que cambia
aquí no es el de la propia `Solicitud` según su estado —qué operaciones
se le permiten—, sino el de terceros completamente externos que deben
reaccionar una vez que el estado ya cambió. La solicitud no delega su
comportamiento a nadie en este caso; simplemente cambia, y otros se
enteran.

### Necesidad 4 — Reglas de transición según el estado
Se aplicó **State**. Las reglas de qué operaciones son válidas
(aprobar, rechazar, ejecutar, cancelar) dependían de if/else dispersos
revisando `getEstado()` en varios métodos. Se reemplazaron por
`EstadoSolicitud`, una interfaz con un método por operación, implementada
por un objeto-estado concreto por cada valor de `Solicitud.getEstado()`
(`PendienteState`, `AprobadaState`, `EjecutadaState`, `RechazadaState`,
`CanceladaState`). `ContextoSolicitud` delega cada operación al estado
actual, y es el propio estado quien decide si la operación es válida y,
si lo es, hace que el contexto transicione a otro estado mediante
`transicionarA(...)`. Agregar un estado nuevo (por ejemplo,
`EN_ESPERA_PROVEEDOR`) solo implica una clase nueva que implemente
`EstadoSolicitud` y su entrada en `EstadoFactory` — no tocar los
if/else existentes.

Se descartó **Strategy**, visto en la guía con una estructura muy
parecida (una interfaz con varias implementaciones intercambiables), por
una diferencia de intención: en Strategy un cliente externo elige e
inyecta explícitamente el comportamiento activo (por ejemplo, un
carrito activando la estrategia de descuento que desea usar en ese
momento). Aquí no hay ningún cliente externo seleccionando un
comportamiento en cada llamada — es la propia solicitud quien, según en
qué estado se encuentre en ese instante de su historia, determina su
comportamiento válido, y además debe poder transicionar de un estado a
otro como parte de resolver la operación. Un conjunto de estrategias
independientes entre sí no modela esa transición por su cuenta; State sí,
porque cada estado conoce a qué otro estado se pasa el contexto.

### Reflexión — otros tres patrones (opcional)
1. Un reporte que recorre secuencialmente todas las solicitudes de un
   centro de costo sin exponer si están en una lista, un mapa o una
   estructura distinta encajaría con **Iterator**: expone una forma
   uniforme de recorrer una colección sin revelar su representación
   interna.
2. Tres tipos de comprobante que comparten el mismo esqueleto de
   impresión (encabezado, cuerpo, pie) pero difieren solo en cómo
   llenan el cuerpo encajarían con **Template Method**: el esqueleto
   del algoritmo se fija en una clase base, y las subclases solo
   sobrescriben el paso variable.
3. Guardar y restaurar instantáneas completas del estado de una
   solicitud sin que el código que las guarda conozca los detalles
   internos de `Solicitud` encajaría con **Memento**. Se diferencia de
   lo construido en la Necesidad 2 en que Command deshace una
   *operación* (reservar, generar orden) reproduciendo su lógica
   inversa, mientras que Memento captura y restaura un *estado
   completo* del objeto en un punto del tiempo, sin ejecutar ninguna
   lógica de negocio para revertirlo.

## Herramientas utilizadas
- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code o IntelliJ IDEA, Git, GitHub

## Conclusiones
Lo que más trabajo me costó de este laboratorio no fue implementar los
patrones en sí, sino decidir entre dos que se parecen mucho en su
estructura pero resuelven problemas distintos. El caso más difícil fue
distinguir State de Strategy en la Necesidad 4: las dos son básicamente
una interfaz con varias implementaciones intercambiables, y al principio
mi primer instinto fue pensar en Strategy porque ya lo había usado antes
en otro laboratorio. La diferencia terminó estando en quién decide el
comportamiento: en Strategy un cliente externo elige e inyecta la
implementación que quiere usar, mientras que en State es el propio
objeto (la solicitud) quien, según su estado actual, determina qué es
válido y además transiciona a otro estado por sí mismo. Algo parecido
me pasó entre Chain of Responsibility y Command en la Parte 1: ambos
aparecen en el mismo laboratorio a propósito para obligarme a pensar si
el problema era "encontrar quién resuelve" o "encapsular una acción
reversible". Al final, lo que más me sirvió fue no fijarme solo en la
forma del código (una interfaz con varias clases) sino preguntarme qué
pregunta responde cada patrón, porque por la forma sola varios patrones
de comportamiento se ven prácticamente iguales.