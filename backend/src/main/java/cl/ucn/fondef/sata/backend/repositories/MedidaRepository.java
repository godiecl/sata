/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.simulation.Medida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Medida Repository.
 *
 * @author Diego Urrutia-Astorga.
 */
@Repository
public interface MedidaRepository extends JpaRepository<Medida, Long>, CustomMedidaRepository {

}
