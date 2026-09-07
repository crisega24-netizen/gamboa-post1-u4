package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

/**
 * Invoker de Command: ejecuta operaciones, mantiene un historial completo
 * y consultable (no solo la última) y permite deshacer la última operación
 * ejecutada sin afectar a las anteriores.
 */
public class EjecutorSolicitud {

    private final Solicitud solicitud;
    private final List<Operacion> historial = new ArrayList<>();
    private final Deque<Operacion> pilaDeshacer = new ArrayDeque<>();

    public EjecutorSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public void ejecutar(Operacion operacion) {
        operacion.ejecutar();
        historial.add(operacion);
        pilaDeshacer.push(operacion);
        solicitud.setEstado("EJECUTADA");
    }

    public void deshacerUltima() {
        if (!pilaDeshacer.isEmpty()) {
            Operacion ultima = pilaDeshacer.pop();
            ultima.deshacer();
        }
    }

    public List<Operacion> getHistorial() {
        return List.copyOf(historial);
    }
}