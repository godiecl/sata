/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.jpa.QueryHints;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * The Custom Simulacion Repository.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
public class CustomSimulacionRepositoryImpl implements CustomSimulacionRepository {

    /**
     * The EntityManager.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Simulacion> retrieveById(@NonNull Long id) {

        try {
            String sqlEjecuciones = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.ejecuciones
                    WHERE s.id = :id
                    """;
            Simulacion simuEjecuciones = this.em.createQuery(sqlEjecuciones, Simulacion.class)
                                                .setParameter("id", id)
                                                .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                                .getSingleResult();

            String sqlEventos = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.eventos
                    WHERE s.id = :id
                    """;
            Simulacion simuEventos = this.em.createQuery(sqlEventos, Simulacion.class)
                                            .setParameter("id", id)
                                            .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                            .getSingleResult();
            /*
            String sqlEquipo = """
                    SELECT DISTINCT s
                    FROM Simulacion s
                    LEFT JOIN FETCH s.equipo
                    WHERE s.id = :id
                    """;
            Simulacion simuEquipo = this.em.createQuery(sqlEquipo, Simulacion.class)
                                           .setParameter("id", id)
                                           .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                           .getSingleResult();
             */

            return Optional.of(simuEventos);
        } catch (NoResultException ex) {
            log.warn("Warning: No Simulacion found for id: {}.", id);
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Simulacion> retrieveAll() {
        String sqlEjecuciones = """
                SELECT DISTINCT s
                FROM Simulacion s
                LEFT JOIN FETCH s.ejecuciones
                """;

        List<Simulacion> simuEjecuciones = this.em.createQuery(sqlEjecuciones, Simulacion.class)
                                                  .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                                  .getResultList();

        String sqlEventos = """
                SELECT DISTINCT s
                FROM Simulacion s
                LEFT JOIN FETCH s.eventos
                WHERE s IN :simulaciones
                """;

        List<Simulacion> simuEventos = this.em.createQuery(sqlEventos, Simulacion.class)
                                              .setParameter("simulaciones", simuEjecuciones)
                                              .setHint(QueryHints.HINT_PASS_DISTINCT_THROUGH, false)
                                              .getResultList();
        return simuEventos;
    }

}
