/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.services;

import cl.ucn.fondef.sata.backend.repositories.MedidaRepository;
import cl.ucn.fondef.sata.backend.model.simulation.Medida;
import cl.ucn.fondef.sata.backend.model.simulation.views.MedidaView;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The Medida Service.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Service
@Scope("singleton")
public class MedidaService {

    /**
     * The Medida Repository.
     */
    private final MedidaRepository medidaRepository;

    /**
     * The Constructor.
     */
    @Autowired
    public MedidaService(
            final MedidaRepository medidaRepository
    ) {
        this.medidaRepository = medidaRepository;
    }

    public Medida saveMedida(Medida medida) {
        return this.medidaRepository.save(medida);
    }

    /**
     * Retrieve the list of medidas.
     *
     * @return the list of Medidas.
     */
    public List<Medida> retrieveMedidas() {
        return this.medidaRepository.findAll();
    }

    /**
     * Retrieve the List of Medidas.
     *
     * @param idSimulacion to use.
     * @param idEjecucion  to use.
     * @param idComponente to use.
     * @param size         to use.
     * @return the List of Medida.
     */
    public MedidaView retrieveMedidas(
            @NonNull final Long idSimulacion,
            @NonNull final Long idEjecucion,
            @NonNull final Long idComponente,
            @NonNull final Integer size) {
        return this.medidaRepository.retrieveMedidas(idSimulacion, idEjecucion, idComponente, size);
    }

    /**
     * Retrieve the List of Medidas.
     *
     * @param idSimulacion to use.
     * @param idEjecucion  to use.
     * @param size         to use.
     * @return the List.
     */
    public List<MedidaView> retrieveMedidas(
            @NonNull final Long idSimulacion,
            @NonNull final Long idEjecucion,
            @NonNull final Integer size) {
        return this.medidaRepository.retrieveMedidas(idSimulacion, idEjecucion, size);
    }

}
