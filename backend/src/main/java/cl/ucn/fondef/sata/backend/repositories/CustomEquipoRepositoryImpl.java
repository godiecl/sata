/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.device.Equipo;
import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * The CustomEquipoRepositoryImpl.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
public class CustomEquipoRepositoryImpl implements CustomEquipoRepository {

    /**
     * The EntityManager.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Equipo> retrieveById(@NonNull Long id) {

        try {
            // evento.componentes
            String sqlComponentes = """
                    SELECT DISTINCT e
                    FROM Equipo e
                    LEFT JOIN FETCH e.componentes
                    WHERE e.id = :id
                    """;
            Equipo equipoComponentes = this.em.createQuery(sqlComponentes, Equipo.class)
                                              .setParameter("id", id)
                                              .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                              .getSingleResult();
            // evento.archivos
            String sqlArchivos = """
                    SELECT DISTINCT e
                    FROM Equipo e
                    LEFT JOIN FETCH e.archivos
                    WHERE e.id = :id
                    """;
            Equipo equipoArchivos = this.em.createQuery(sqlArchivos, Equipo.class)
                                           .setParameter("id", id)
                                           .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                           .getSingleResult();

            // evento.simulaciones
            String sqlSimulaciones = """
                    SELECT DISTINCT e
                    FROM Equipo e
                    LEFT JOIN FETCH e.simulaciones s
                    WHERE e.id = :id
                    """;
            Equipo equipoSimulaciones = this.em.createQuery(sqlSimulaciones, Equipo.class)
                                               .setParameter("id", id)
                                               .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                               .getSingleResult();

            // evento.simulaciones.ejecuciones
            String sqlEjecuciones = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.ejecuciones
                    WHERE s IN :simulaciones
                    """;
            List<Simulacion> simulacionEjecuciones = this.em.createQuery(sqlEjecuciones, Simulacion.class)
                                                            .setParameter("simulaciones", equipoSimulaciones.getSimulaciones())
                                                            .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                                            .getResultList();
            // evento.simulaciones.eventos
            String sqlEventos = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.eventos
                    WHERE s IN :simulaciones
                    """;
            List<Simulacion> simulacionEventos = this.em.createQuery(sqlEventos, Simulacion.class)
                                                        .setParameter("simulaciones", equipoSimulaciones.getSimulaciones())
                                                        .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                                        .getResultList();

            return Optional.ofNullable(equipoComponentes);

        } catch (javax.persistence.NoResultException e) {
            log.warn("Warning: No Equipo found for id: {}.", id);
            return Optional.empty();
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Equipo> retrieveAll() {

        // evento.componentes
        String sqlComponentes = """
                SELECT DISTINCT e
                FROM Equipo e
                LEFT JOIN FETCH e.componentes
                """;
        List<Equipo> equipoComponentes = this.em
                .createQuery(sqlComponentes, Equipo.class)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        // evento.archivos
        String sqlArchivos = """
                SELECT DISTINCT e
                FROM Equipo e
                LEFT JOIN FETCH e.archivos a
                WHERE e IN :equipos
                """;
        List<Equipo> equipoArchivos = this.em
                .createQuery(sqlArchivos, Equipo.class)
                .setParameter("equipos", equipoComponentes)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        // evento.simulaciones
        String sqlSimulaciones = """
                SELECT DISTINCT e
                FROM Equipo e
                LEFT JOIN FETCH e.simulaciones s
                WHERE e IN :equipos
                """;
        List<Equipo> equipoSimulaciones = this.em
                .createQuery(sqlSimulaciones, Equipo.class)
                .setParameter("equipos", equipoArchivos)
                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                .getResultList();

        for (Equipo equipo : equipoSimulaciones) {
            // evento.simulaciones.ejecuciones
            String sqlEjecuciones = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.ejecuciones
                    WHERE s IN :simulaciones
                    """;
            List<Simulacion> simulacionEjecuciones = this.em
                    .createQuery(sqlEjecuciones, Simulacion.class)
                    .setParameter("simulaciones", equipo.getSimulaciones())
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();

            // evento.simulaciones.eventos
            String sqlEventos = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.eventos
                    WHERE s IN :simulaciones
                    """;
            List<Simulacion> simulacionEventos = this.em
                    .createQuery(sqlEventos, Simulacion.class)
                    .setParameter("simulaciones", equipo.getSimulaciones())
                    .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                    .getResultList();
        }


        return equipoComponentes;
    }
}
