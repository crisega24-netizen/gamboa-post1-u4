package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificadorAuditoria implements SuscriptorNotificacion {
    @Override
    public void notificar(Solicitud solicitud) {
        ClientesNotificacion.registrarAuditoria(
                solicitud.getId(), solicitud.getEstado(),
                "Cambio registrado por " + solicitud.getNivelResolutor());
    }
}