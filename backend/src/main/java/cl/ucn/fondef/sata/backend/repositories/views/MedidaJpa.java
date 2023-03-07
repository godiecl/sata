/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories.views;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

/**
 * The Medida Jpa.
 *
 * @author Diego Urrutia-Astorga.
 */
@AllArgsConstructor
@Getter
public final class MedidaJpa {
    Long id;
    Double senial;
    Double valor;
    Instant fecha;
    Long idComponente;
    Long idEjecucion;
    Long idSimulacion;
}
