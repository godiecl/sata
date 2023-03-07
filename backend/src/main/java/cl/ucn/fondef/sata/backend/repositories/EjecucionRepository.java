/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.simulation.Ejecucion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Ejecucion Repository.s
 *
 * @author Diego Urrutia-Astorga.
 */
@Repository
public interface EjecucionRepository extends JpaRepository<Ejecucion, Long> {

}
