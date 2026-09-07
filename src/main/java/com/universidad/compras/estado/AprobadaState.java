package com.universidad.compras.estado;

public class AprobadaState implements EstadoSolicitud {

    @Override
    public String aprobar(ContextoSolicitud contexto) {
        return "Error: la solicitud ya fue aprobada";
    }

    @Override
    public String rechazar(ContextoSolicitud contexto) {
        return "Error: no se puede rechazar una solicitud ya aprobada";
    }

    @Override
    public String ejecutar(ContextoSolicitud contexto) {
        contexto.transicionarA(new EjecutadaState());
        return "Ejecutada";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        contexto.transicionarA(new CanceladaState());
        return "Cancelada";
    }

    @Override
    public String nombre() {
        return "APROBADA";
    }
}