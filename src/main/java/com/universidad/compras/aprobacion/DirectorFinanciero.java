package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

public class DirectorFinanciero extends NivelAprobacion {

    @Override
    protected boolean aplica(Solicitud solicitud) {
        return true; // último nivel: sin límite superior, siempre resuelve
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor("Director Financiero");
        return new ResultadoAprobacion(true, "Director Financiero",
                "Aprobada por el Director Financiero (sin límite superior)");
    }
}