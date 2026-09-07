package com.universidad.compras.estado;

/**
 * State: cada estado concreto decide qué operaciones son válidas para sí
 * mismo y hacia qué estado transiciona el contexto tras cada una. Agregar
 * un estado nuevo (ej. EN_ESPERA_PROVEEDOR) solo implica una clase nueva
 * que implemente esta interfaz — no if/else dispersos en varios métodos.
 */
public interface EstadoSolicitud {
    String aprobar(ContextoSolicitud contexto);
    String rechazar(ContextoSolicitud contexto);
    String ejecutar(ContextoSolicitud contexto);
    String cancelar(ContextoSolicitud contexto);
    String nombre();
}