package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Nivel adicional que solo aplica a solicitudes INTERNACIONAL. Vive siempre
 * al inicio de la cadena; si la solicitud no es internacional, simplemente
 * no aplica y delega, sin que nadie fuera de este nivel necesite saber
 * que existe una regla especial para esa categoría.
 */
public class RevisorCumplimientoNormativo extends NivelAprobacion {

    @Override
    protected boolean aplica(Solicitud solicitud) {
        return "INTERNACIONAL".equals(solicitud.getCategoria());
    }

    @Override
    protected ResultadoAprobacion resolver(Solicitud solicitud) {
        solicitud.setEstado("APROBADA");
        solicitud.setNivelResolutor("Revisor de Cumplimiento Normativo");
        return new ResultadoAprobacion(true, "Revisor de Cumplimiento Normativo",
                "Solicitud internacional revisada y aprobada por cumplimiento normativo");
    }
}