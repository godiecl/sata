/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.device.Equipo;
import lombok.NonNull;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * The Custom Equipo Repository.
 *
 * @author Diego Urrutia-Astorga.
 */
public interface CustomEquipoRepository {

    /**
     * Retrieve the equipo by id.
     *
     * @param id to use.
     * @return the Equipo.
     */
    Optional<Equipo> retrieveById(@Param("id") @NonNull Long id);

    /**
     * Retrieve all the Equipos.
     *
     * @return the List.
     */
    List<Equipo> retrieveAll();

}
