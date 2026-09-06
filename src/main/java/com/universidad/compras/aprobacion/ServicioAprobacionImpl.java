package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import org.springframework.stereotype.Service;

/**
 * Ensambla la cadena de niveles y delega la evaluación al primero.
 * Reordenar, agregar o quitar un nivel solo implica cambiar este
 * ensamblaje — ControladorSolicitudes nunca se entera.
 */
@Service
public class ServicioAprobacionImpl implements ServicioAprobacion {

    private final NivelAprobacion primerNivel;

    public ServicioAprobacionImpl() {
        NivelAprobacion cumplimiento = new RevisorCumplimientoNormativo();
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente = new GerenteArea();
        NivelAprobacion director = new DirectorFinanciero();

        cumplimiento.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(director);

        this.primerNivel = cumplimiento;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        return primerNivel.evaluar(solicitud);
    }
}