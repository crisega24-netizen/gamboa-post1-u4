package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class GerenteArea extends NivelAprobacion {

    private static final double LIMITE = 10_000_000;

    @Override
    protected boolean aplica(Solicitud solicitud) {
        return solicitud.getMonto() <= LIMITE;
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor("Gerente de Área");
        return new ResultadoAprobacion(true, "Gerente de Área",
                "Aprobada dentro del límite de autoridad del Gerente de Área");
    }
}