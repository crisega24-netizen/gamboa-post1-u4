package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Handler abstracto de Chain of Responsibility. Cada nivel decide si
 * resuelve la solicitud o la delega al siguiente nivel de la cadena.
 * Agregar, quitar o reordenar un nivel solo implica cambiar cómo se
 * ensambla la cadena en ServicioAprobacionImpl — nunca a quien la dispara.
 */
public abstract class NivelAprobacion {

    private NivelAprobacion siguiente;

    public NivelAprobacion setSiguiente(NivelAprobacion siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public final ResultadoAprobacion evaluar(Solicitud solicitud) {
        if (aplica(solicitud)) {
            return resolver(solicitud);
        }
        if (siguiente != null) {
            return siguiente.evaluar(solicitud);
        }
        return new ResultadoAprobacion(false, "Ninguno", "No hay nivel que resuelva esta solicitud");
    }

    /** ¿Este nivel tiene autoridad/competencia para resolver esta solicitud? */
    protected abstract boolean aplica(Solicitud solicitud);

    /** Resuelve la solicitud (aprueba o rechaza) y registra su nombre como resolutor. */
    protected abstract ResultadoAprobacion resolver(Solicitud solicitud);
}