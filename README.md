# gamboa-post1-u4
Post-contenido — Patrones de Comportamiento aplicados al backend de ComprasUDES

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