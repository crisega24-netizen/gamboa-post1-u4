package com.universidad.compras.estado;

public class CanceladaState implements EstadoSolicitud {

    @Override
    public String aprobar(ContextoSolicitud contexto) {
        return "Error: no se puede aprobar una solicitud cancelada";
    }

    @Override
    public String rechazar(ContextoSolicitud contexto) {
        return "Error: no se puede rechazar una solicitud cancelada";
    }

    @Override
    public String ejecutar(ContextoSolicitud contexto) {
        return "Error: no se puede ejecutar una solicitud CANCELADA";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        return "Error: la solicitud ya fue cancelada";
    }

    @Override
    public String nombre() {
        return "CANCELADA";
    }
}