/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.simulation.views;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

/**
 * The Medida View.
 *
 * @author Diego Urrutia-Astorga.
 */
@Builder
public final class MedidaView {
    Long idSimulacion;
    Long idEjecucion;
    ComponenteView componente;
    List<DatoView> datos;

    @Builder
    public static final class DatoView {
        Double senial;
        Double valor;
        Instant fecha;
    }
}
