/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.simulation.views.MedidaView;

import java.util.List;

/**
 * The Custom Medida Repository.
 *
 * @author Diego Urrutia-Astorga.
 */
public interface CustomMedidaRepository {

    /**
     * Retrieve the List of Medidas.
     *
     * @param idSimulacion to use.
     * @param idEjecucion  to use.
     * @param idComponente to use.
     * @param size         to use.
     * @return the List of Medida.
     */
    MedidaView retrieveMedidas(Long idSimulacion, Long idEjecucion, Long idComponente, Integer size);

    /**
     * Retrieve the List of Medidas.
     *
     * @param idSimulacion to use.
     * @param idEjecucion to use.
     * @param size        to use.
     * @return the List.
     */
    List<MedidaView> retrieveMedidas(Long idSimulacion, Long idEjecucion, Integer size);

}
