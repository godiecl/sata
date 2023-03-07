/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.services;

import cl.ucn.fondef.sata.backend.repositories.EjecucionRepository;
import cl.ucn.fondef.sata.backend.repositories.SimulacionRepository;
import cl.ucn.fondef.sata.backend.exceptions.IntegrityException;
import cl.ucn.fondef.sata.backend.model.simulation.Ejecucion;
import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * The Simulacion Service.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Service
@Scope("singleton")
public class SimulacionService {

    /**
     * The Simulacion Repository.
     */
    private final SimulacionRepository simulacionRepository;

    /**
     * The Ejecucion Repository.
     */
    private final EjecucionRepository ejecucionRepository;

    /**
     * The Constructor.
     */
    @Autowired
    public SimulacionService(
            @NonNull final SimulacionRepository simulacionRepository,
            @NonNull final EjecucionRepository ejecucionRepository
    ) {
        this.simulacionRepository = simulacionRepository;
        this.ejecucionRepository = ejecucionRepository;
    }

    /**
     * Save a simulation.
     *
     * @param simulacion to save.
     */
    @Transactional
    public Simulacion saveSimulacion(@NonNull final Simulacion simulacion) {

        // fix
        Simulacion simulacionFixed = Simulacion.fixAndValidate(simulacion);

        try {
            return this.simulacionRepository.saveAndFlush(simulacionFixed);
        } catch (DataIntegrityViolationException ex) {
            throw new IntegrityException("Can't add Simulacion", ex);
        }
    }

    /**
     * Retrieve a Simulacion.
     *
     * @param id of Simulacion.
     * @return the Simulacion.
     */
    @Transactional(readOnly = true)
    public Optional<Simulacion> retrieveSimulacion(@NonNull final Long id) {
        log.debug("retrieveSimulacion for id [{}].", id);
        return this.simulacionRepository.retrieveById(id);
    }

    /**
     * Retrieve all Simulaciones.
     *
     * @return the simulaciones. l
     */
    @Transactional(readOnly = true)
    public List<Simulacion> retrieveSimulaciones() {
        List<Simulacion> simulaciones = this.simulacionRepository.retrieveAll();
        log.debug("retrieveSimulaciones: {}", simulaciones.size());
        return simulaciones;
    }

    /**
     * Update the state of a simulation.
     *
     * @param estadoEjecucion to use.
     */
    @Transactional
    public Ejecucion updateEstadoSimulacion(
            @NonNull final Long idEstado,
            @NonNull final Ejecucion.EstadoEjecucion estadoEjecucion) {

        // find the ejecucion
        Optional<Ejecucion> oEjecucion = this.ejecucionRepository.findById(idEstado);

        Ejecucion ejecucion = oEjecucion.orElseThrow(() -> new NoSuchElementException("Can't find the Ejecucion!"));

        // change the state and save
        ejecucion.setEstadoEjecucion(estadoEjecucion);
        return this.ejecucionRepository.saveAndFlush(ejecucion);
    }

    /**
     * Add a new state to a simulation.
     * @param idSimulacion to use.
     * @return the Ejecucion.
     */
    @Transactional
    public Ejecucion addEstadoSimulacion(
            @NonNull final Long idSimulacion) {
        // retrieve the simulacion
        Optional<Simulacion> oSimulacion = this.simulacionRepository.retrieveById(idSimulacion);
        // check if exists
        Simulacion simulacion = oSimulacion.orElseThrow(() -> new NoSuchElementException("Can't find the Simulacion!"));

        // create the new state
        Ejecucion ejecucion = Ejecucion.builder()
                .fechaInicio(Instant.now())
                .descripcion("Ejecucion de " + simulacion.getNombre())
                .estadoEjecucion(Ejecucion.EstadoEjecucion.ESTADO_INICIALIZADA)
                .build();
        simulacion.addEjecucion(ejecucion);
        simulacion = this.saveSimulacion(simulacion);
        return ejecucion;
    }
}
