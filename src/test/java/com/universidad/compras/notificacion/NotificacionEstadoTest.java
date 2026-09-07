package com.universidad.compras.notificacion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NotificacionEstadoTest {

    @Test
    void cambiarEstadoDisparaLasTresReaccionesSinLanzarExcepcion() {
        Solicitud s = new Solicitud("S-020", "ana@udes.edu.co", 2500000, "SOFTWARE", "CC-100");

        PublicadorEstado publicador = new PublicadorEstado();
        publicador.registrar(new NotificadorCorreo());
        publicador.registrar(new NotificadorDashboard());
        publicador.registrar(new NotificadorAuditoria());

        assertDoesNotThrow(() -> {
            s.setEstado("APROBADA");
            publicador.notificarCambioEstado(s);
        });
    }

    @Test
    void agregarUnCuartoSuscriptorDePruebaNoRequiereModificarElMecanismo() {
        Solicitud s = new Solicitud("S-021", "luis@udes.edu.co", 1800000, "MATERIAL_OFICINA", "CC-200");

        PublicadorEstado publicador = new PublicadorEstado();
        publicador.registrar(new NotificadorCorreo());

        // Colector de prueba: un cuarto suscriptor definido aquí mismo, sin
        // tocar PublicadorEstado ni ningún suscriptor existente.
        List<String> eventosCapturados = new ArrayList<>();
        SuscriptorNotificacion colectorDePrueba = solicitud ->
                eventosCapturados.add(solicitud.getId() + " -> " + solicitud.getEstado());

        publicador.registrar(colectorDePrueba);

        s.setEstado("EJECUTADA");
        publicador.notificarCambioEstado(s);

        assertEquals(1, eventosCapturados.size());
        assertEquals("S-021 -> EJECUTADA", eventosCapturados.get(0));
    }
}