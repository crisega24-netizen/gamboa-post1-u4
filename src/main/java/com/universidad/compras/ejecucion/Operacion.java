package com.universidad.compras.ejecucion;

/**
 * Command: encapsula una acción ejecutable y reversible de forma
 * independiente. Cada operación concreta sabe cómo deshacerse a sí misma
 * sin que el ejecutor conozca los detalles de PresupuestoService u
 * OrdenCompraService.
 */
public interface Operacion {
    void ejecutar();
    void deshacer();

    /** Descripción legible de la operación, para el historial consultable. */
    String getDescripcion();
}