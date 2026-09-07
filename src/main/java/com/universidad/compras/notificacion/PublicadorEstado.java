package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;

import java.util.ArrayList;
import java.util.List;

/**
 * Subject de Observer: mantiene la lista de suscriptores y les avisa
 * cuando una Solicitud cambia de estado. Agregar un cuarto suscriptor
 * (o quinto, o el que sea) solo implica llamar a registrar() — nunca
 * modificar esta clase ni el código que dispara el cambio de estado.
 */
public class PublicadorEstado {

    private final List<SuscriptorNotificacion> suscriptores = new ArrayList<>();

    public void registrar(SuscriptorNotificacion suscriptor) {
        suscriptores.add(suscriptor);
    }

    public void notificarCambioEstado(Solicitud solicitud) {
        for (SuscriptorNotificacion suscriptor : suscriptores) {
            suscriptor.notificar(solicitud);
        }
    }
}