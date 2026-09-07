package com.universidad.compras.estado;

public final class EstadoFactory {
    private EstadoFactory() {}

    public static EstadoSolicitud desde(String nombreEstado) {
        return switch (nombreEstado) {
            case "PENDIENTE" -> new PendienteState();
            case "APROBADA" -> new AprobadaState();
            case "RECHAZADA" -> new RechazadaState();
            case "EJECUTADA" -> new EjecutadaState();
            case "CANCELADA" -> new CanceladaState();
            default -> throw new IllegalArgumentException("Estado desconocido: " + nombreEstado);
        };
    }
}