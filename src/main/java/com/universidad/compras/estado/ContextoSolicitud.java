package com.universidad.compras.estado;

import com.universidad.compras.modelo.Solicitud;

public class ContextoSolicitud {

    private final Solicitud solicitud;
    private EstadoSolicitud estadoActual;

    public ContextoSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
        this.estadoActual = EstadoFactory.desde(solicitud.getEstado());
    }

    public String aprobar() {
        return estadoActual.aprobar(this);
    }

    public String rechazar() {
        return estadoActual.rechazar(this);
    }

    public String ejecutar() {
        return estadoActual.ejecutar(this);
    }

    public String cancelar() {
        return estadoActual.cancelar(this);
    }

    public void transicionarA(EstadoSolicitud nuevoEstado) {
        this.estadoActual = nuevoEstado;
        this.solicitud.setEstado(nuevoEstado.nombre());
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }
}