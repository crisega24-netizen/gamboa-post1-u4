package com.universidad.compras.estado;

public class PendienteState implements EstadoSolicitud {

    @Override
    public String aprobar(ContextoSolicitud contexto) {
        contexto.transicionarA(new AprobadaState());
        return "Aprobada";
    }

    @Override
    public String rechazar(ContextoSolicitud contexto) {
        contexto.transicionarA(new RechazadaState());
        return "Rechazada";
    }

    @Override
    public String ejecutar(ContextoSolicitud contexto) {
        return "Error: debe estar aprobada antes de ejecutarse";
    }

    @Override
    public String cancelar(ContextoSolicitud contexto) {
        contexto.transicionarA(new CanceladaState());
        return "Cancelada";
    }

    @Override
    public String nombre() {
        return "PENDIENTE";
    }
}