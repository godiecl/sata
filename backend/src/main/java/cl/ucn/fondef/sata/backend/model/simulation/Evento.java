/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.simulation;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.backend.exceptions.PreRequisitesException;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.device.Componente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Evento.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "eventos")
public final class Evento extends BaseEntity {

    /**
     * The intensidad.
     */
    @Getter
    @Column(nullable = false)
    private Double intensidad;

    /**
     * The duracion.
     */
    @Getter
    @Column(nullable = false)
    private Double duracion;

    /**
     * The tiempo.
     */
    @Getter
    @Column(nullable = false)
    private int tiempo;

    /**
     * The descripcion.
     */
    @Getter
    @Column
    private String descripcion;

    /**
     * The Componente.
     */
    @Getter
    @JoinColumn(name = "componente_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Componente componente;

    /**
     * The Componente Id.
     */
    @Getter
    @Setter
    @Column(name = "componente_id", nullable = false)
    private Long idComponente;

    /**
     * The Simulacion.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @JoinColumn(name = "simulacion_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Simulacion simulacion;

    /**
     * The Simulacion id.
     */
    @Getter
    @Setter
    @Column(name = "simulacion_id", nullable = false)
    private Long idSimulacion;

    /**
     * Set the Simulacion.
     *
     * @param simulacion to set.
     * @return this.
     */
    public Evento setSimulacion(@NonNull final Simulacion simulacion) {
        if (this.simulacion != null) {
            log.warn("The Simulacion with id: {} was already set!", simulacion.getId());
        }
        this.simulacion = simulacion;
        this.idSimulacion = simulacion.getId();
        return this;
    }

    /**
     * Set the Componente.
     *
     * @param componente to set.
     * @return this.
     */
    public Evento setComponente(@NonNull final Componente componente) {
        this.componente = componente;
        this.idComponente = componente.getId();
        return this;
    }

    /**
     * Fix and validate the internal state.
     *
     * @param evento to fix.
     * @return the Evento fixed.
     */
    public static Evento fixAndValidate(@NonNull final Evento evento) {
        if (evento.intensidad < 0 || evento.intensidad > 100) {
            throw new PreRequisitesException("Intensidad debe estar entre 0 y 100");
        }
        return evento;
    }

}
