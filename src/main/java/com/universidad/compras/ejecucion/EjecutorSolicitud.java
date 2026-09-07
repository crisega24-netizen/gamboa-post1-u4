package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.PublicadorEstado;

import java.util.ArrayList;
import java.util.List;
import java.util.Deque;
import java.util.ArrayDeque;

public class EjecutorSolicitud {

    private final Solicitud solicitud;
    private final List<Operacion> historial = new ArrayList<>();
    private final Deque<Operacion> pilaDeshacer = new ArrayDeque<>();
    private final PublicadorEstado publicadorEstado;

    public EjecutorSolicitud(Solicitud solicitud) {
        this(solicitud, new PublicadorEstado());
    }

    public EjecutorSolicitud(Solicitud solicitud, PublicadorEstado publicadorEstado) {
        this.solicitud = solicitud;
        this.publicadorEstado = publicadorEstado;
    }

    public void ejecutar(Operacion operacion) {
        operacion.ejecutar();
        historial.add(operacion);
        pilaDeshacer.push(operacion);
        solicitud.setEstado("EJECUTADA");
        publicadorEstado.notificarCambioEstado(solicitud);
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