package com.universidad.compras.ejecucion;

import com.universidad.compras.modelo.Solicitud;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EjecucionSolicitudTest {

    @Test
    void ejecutarReservaPresupuestoYGeneraOrden() {
        Solicitud s = new Solicitud("S-010", "ana@udes.edu.co", 3000000, "SOFTWARE", "CC-100");
        s.setEstado("APROBADA");

        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenCompraService = new OrdenCompraService();
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);

        ejecutor.ejecutar(new ReservarPresupuestoCommand(presupuestoService, s.getCentroCosto(), s.getMonto()));
        ejecutor.ejecutar(new GenerarOrdenCompraCommand(ordenCompraService, s.getId(), "Proveedor XYZ"));

        assertEquals("EJECUTADA", s.getEstado());
    }

    @Test
    void deshacerSoloLaUltimaOperacionNoAfectaLaAnterior() {
        Solicitud s = new Solicitud("S-011", "luis@udes.edu.co", 4000000, "MATERIAL_OFICINA", "CC-200");
        s.setEstado("APROBADA");

        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenCompraService = new OrdenCompraService();
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);

        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(new ReservarPresupuestoCommand(presupuestoService, s.getCentroCosto(), s.getMonto()));
            ejecutor.ejecutar(new GenerarOrdenCompraCommand(ordenCompraService, s.getId(), "Proveedor XYZ"));
            ejecutor.deshacerUltima();
        });

        // El historial conserva ambas operaciones aunque una se haya deshecho;
        // solo la generación de orden fue revertida, no la reserva de presupuesto.
        assertEquals(2, ejecutor.getHistorial().size());
    }

    @Test
    void elHistorialConservaTodasLasOperacionesNoSoloLaUltima() {
        Solicitud s = new Solicitud("S-012", "ana@udes.edu.co", 2000000, "SOFTWARE", "CC-300");
        s.setEstado("APROBADA");

        PresupuestoService presupuestoService = new PresupuestoService();
        OrdenCompraService ordenCompraService = new OrdenCompraService();
        EjecutorSolicitud ejecutor = new EjecutorSolicitud(s);

        assertDoesNotThrow(() -> {
            ejecutor.ejecutar(new ReservarPresupuestoCommand(presupuestoService, s.getCentroCosto(), s.getMonto()));
            ejecutor.ejecutar(new GenerarOrdenCompraCommand(ordenCompraService, s.getId(), "Proveedor XYZ"));
        });

        assertEquals(2, ejecutor.getHistorial().size());
    }
}