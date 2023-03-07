/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.services;

import cl.ucn.fondef.sata.Timer;
import cl.ucn.fondef.sata.backend.repositories.EquipoRepository;
import cl.ucn.fondef.sata.backend.exceptions.IntegrityException;
import cl.ucn.fondef.sata.backend.model.device.Equipo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The Equipo Service.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Service
@Scope("singleton")
public class EquipoService {

    /**
     * The Equipo repo.
     */
    private final EquipoRepository equipoRepository;

    /**
     * The Constructor.
     *
     * @param equipoRepository the repo.
     */
    @Autowired
    public EquipoService(
            @NonNull final EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
    }

    /**
     * Insert a Equipo into the backend.
     *
     * @param equipo to insert.
     * @return the Equipo created.
     */
    @Transactional
    public Equipo saveEquipo(Equipo equipo) {
        try {
            return this.equipoRepository.saveAndFlush(equipo);
        } catch (DataIntegrityViolationException ex) {
            throw new IntegrityException("Can't add Equipo", ex);
        }
    }

    /**
     * Retrieve a {@link Equipo}.
     *
     * @param id to use.
     * @return the {@link Equipo}.
     */
    @Transactional(readOnly = true)
    public Optional<Equipo> retrieveEquipo(@NonNull Long id) {
        log.debug("retrieveEquipo for id<{}> ..", id);
        Timer timer = Timer.start();
        Optional<Equipo> oEquipo = this.equipoRepository.retrieveById(id);
        log.debug("retrieveEquipo for id [{}] .. done in {}ms.", id, timer.millis());
        return oEquipo;
    }

    /**
     * Retrieve all the {@link Equipo}.
     *
     * @return the list of Equipo.
     */
    @Transactional(readOnly = true)
    public List<Equipo> retrieveEquipos() {
        Timer timer = Timer.start();
        List<Equipo> equipos = this.equipoRepository.retrieveAll();
        log.debug("retrieveEquipos: {} equipos .. done in {}ms.", equipos.size(), timer.millis());
        return equipos;
    }
}
