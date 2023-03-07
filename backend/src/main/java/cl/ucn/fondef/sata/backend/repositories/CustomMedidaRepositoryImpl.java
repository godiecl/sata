/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.Mapper;
import cl.ucn.fondef.sata.backend.repositories.views.ComponenteJpa;
import cl.ucn.fondef.sata.backend.repositories.views.MedidaJpa;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.device.Componente;
import cl.ucn.fondef.sata.backend.model.simulation.views.MedidaView;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * The Custom Medida Repository.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
public class CustomMedidaRepositoryImpl implements CustomMedidaRepository {

    /**
     * The EntityManager.
     */
    @PersistenceContext
    private EntityManager em;

    /**
     * {@inheritDoc}
     */
    @Override
    public MedidaView retrieveMedidas(
            @NonNull Long idSimulacion,
            @NonNull Long idEjecucion,
            @NonNull Long idComponente,
            @NonNull final Integer size) {

        ComponenteJpa componenteJpa = this.retrieveComponente(idComponente);

        String sql = """
                SELECT new cl.ucn.fondef.sata.backend.repositories.views.MedidaJpa(
                    m.id, m.senial, m.valor, m.fecha, m.componente.id, m.ejecucion.id, m.ejecucion.simulacion.id
                )
                FROM Medida m
                WHERE m.ejecucion.id = :idEjecucion
                AND m.componente.id = :idComponente
                ORDER BY m.fecha DESC
                """;
        List<MedidaJpa> medidaJpas = this.em.createQuery(sql, MedidaJpa.class)
                                            .setParameter("idEjecucion", idEjecucion)
                                            .setParameter("idComponente", idComponente)
                                            .setMaxResults(size)
                                            .getResultList();

        return Mapper.toListMedidaView(medidaJpas, idEjecucion, idSimulacion, componenteJpa);
    }

    /**
     * {@inheritDoc}
     */
    public List<MedidaView> retrieveMedidas(
            @NonNull Long idSimulacion,
            @NonNull final Long idEjecucion,
            @NonNull final Integer size) {

        String sql = """
                SELECT new cl.ucn.fondef.sata.backend.repositories.views.MedidaJpa(
                    m.id,
                    m.senial,
                    m.valor,
                    m.fecha,
                    m.componente.id,
                    m.ejecucion.id,
                    m.ejecucion.simulacion.id
                )
                FROM Medida m
                WHERE m.ejecucion.id = :idEjecucion
                AND m.componente.id = :idComponente
                ORDER BY m.fecha DESC
                """;

        List<MedidaView> medidaViews = new ArrayList<>();
        for (ComponenteJpa componenteJpa : this.retrieveComponentes(idEjecucion)) {
            log.debug("Componente: {}.", BaseEntity.toString(componenteJpa));
            List<MedidaJpa> medidaJpas = this.em.createQuery(sql, MedidaJpa.class)
                                                .setParameter("idEjecucion", idEjecucion)
                                                .setParameter("idComponente", componenteJpa.getId())
                                                .setMaxResults(size)
                                                .getResultList();
            medidaViews.add(Mapper.toListMedidaView(medidaJpas, idEjecucion, idSimulacion, componenteJpa));
        }
        return medidaViews;
    }

    /**
     * Retrieve the Componente.
     *
     * @param idComponente to use.
     * @return the ComponenteJpa.
     */
    private ComponenteJpa retrieveComponente(@NonNull final Long idComponente) {

        String sql = """
                SELECT new cl.ucn.fondef.sata.backend.repositories.views.ComponenteJpa(
                    c.id,
                    c.nombre,
                    c.descripcion,
                    c.unidad,
                    c.formula,
                    c.umbralMinimo,
                    c.umbralWarning,
                    c.umbralError
                )
                FROM Componente c
                WHERE c.id = :idComponente
                """;

        return this.em.createQuery(sql, ComponenteJpa.class)
                      .setParameter("idComponente", idComponente)
                      .getSingleResult();
    }

    /**
     * Retrieve the Componentes.
     *
     * @param idEjecucion to use.
     * @return the List of ComponentesJpa.
     */
    private List<ComponenteJpa> retrieveComponentes(@NonNull final Long idEjecucion) {

        String sql = """
                SELECT new cl.ucn.fondef.sata.backend.repositories.views.ComponenteJpa(
                    c.id,
                    c.nombre,
                    c.descripcion,
                    c.unidad,
                    c.formula,
                    c.umbralMinimo,
                    c.umbralWarning,
                    c.umbralError
                )
                FROM Componente c, Simulacion s, Ejecucion ex, Equipo eq
                WHERE ex.id = :idEjecucion
                AND ex.idSimulacion = s.id
                AND s.idEquipo = eq.id
                AND c.idEquipo = eq.id
                AND c.tipoComponente IN :tiposComponente
                """;

        return this.em.createQuery(sql, ComponenteJpa.class)
                      .setParameter("idEjecucion", idEjecucion)
                      .setParameter("tiposComponente", EnumSet.of(
                              Componente.TipoComponente.TIPO_SENSOR,
                              Componente.TipoComponente.TIPO_SENSOR_ACTUADOR))
                      .getResultList();
    }

}
