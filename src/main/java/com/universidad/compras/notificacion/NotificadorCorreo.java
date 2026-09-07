package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificadorCorreo implements SuscriptorNotificacion {
    @Override
    public void notificar(Solicitud solicitud) {
        ClientesNotificacion.enviarCorreo(
                solicitud.getSolicitanteEmail(),
                "Actualización de tu solicitud " + solicitud.getId(),
                "Tu solicitud cambió al estado " + solicitud.getEstado());
    }
}
