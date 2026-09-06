package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class SupervisorArea extends NivelAprobacion {

    private static final double LIMITE = 2_000_000;

    @Override
    protected boolean aplica(Solicitud solicitud) {
        return solicitud.getMonto() <= LIMITE;
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor("Supervisor de Área");
        return new ResultadoAprobacion(true, "Supervisor de Área",
                "Aprobada dentro del límite de autoridad del Supervisor de Área");
    }
}