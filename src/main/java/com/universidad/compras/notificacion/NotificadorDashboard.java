package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

public class NotificadorDashboard implements SuscriptorNotificacion {
    @Override
    public void notificar(Solicitud solicitud) {
        ClientesNotificacion.actualizarDashboardContabilidad(
                solicitud.getId(), solicitud.getEstado(), solicitud.getMonto());
    }
}