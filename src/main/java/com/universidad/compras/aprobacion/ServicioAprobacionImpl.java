package com.universidad.compras.aprobacion;

import com.universidad.compras.modelo.Solicitud;
import com.universidad.compras.notificacion.PublicadorEstado;
import org.springframework.stereotype.Service;

@Service
public class ServicioAprobacionImpl implements ServicioAprobacion {

    private final NivelAprobacion primerNivel;
    private final PublicadorEstado publicadorEstado;

    public ServicioAprobacionImpl() {
        this(new PublicadorEstado());
    }

    public ServicioAprobacionImpl(PublicadorEstado publicadorEstado) {
        NivelAprobacion cumplimiento = new RevisorCumplimientoNormativo();
        NivelAprobacion supervisor = new SupervisorArea();
        NivelAprobacion gerente = new GerenteArea();
        NivelAprobacion director = new DirectorFinanciero();

        cumplimiento.setSiguiente(supervisor);
        supervisor.setSiguiente(gerente);
        gerente.setSiguiente(director);

        this.primerNivel = cumplimiento;
        this.publicadorEstado = publicadorEstado;
    }

    @Override
    public ResultadoAprobacion evaluar(Solicitud solicitud) {
        ResultadoAprobacion resultado = primerNivel.evaluar(solicitud);
        publicadorEstado.notificarCambioEstado(solicitud);
        return resultado;
    }
}