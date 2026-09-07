package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

/**
 * Observer: reacciona a un cambio de estado de una Solicitud sin que la
 * Solicitud ni quien la modifica conozcan los detalles de esta reacción.
 */
public interface SuscriptorNotificacion {
    void notificar(Solicitud solicitud);
}