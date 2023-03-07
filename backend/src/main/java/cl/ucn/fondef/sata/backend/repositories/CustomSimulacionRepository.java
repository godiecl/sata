/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The Custom Simulacion Repository.
 *
 * @author Diego Urrutia-Astorga.
 */
public interface CustomSimulacionRepository {

    /**
     * Retrieve the simulacion by id..
     *
     * @param id to use.
     * @return the simulacion.
     */
    Optional<Simulacion> retrieveById(@Param("id") @NonNull Long id);

    /**
     * Retrieve all the simulaciones.
     *
     * @return the List.
     */
    List<Simulacion> retrieveAll();

}
