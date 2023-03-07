/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.device;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.backend.exceptions.PreRequisitesException;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Componente.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "componentes")
public final class Componente extends BaseEntity {

    /**
     * The Nombre.
     */
    @Getter
    @Column(nullable = false)
    private String nombre;

    /**
     * Descripcion.
     */
    @Getter
    @Column
    private String descripcion;

    /**
     * Unidad.
     */
    @Getter
    @Column
    private String unidad;

    /**
     * Formula.
     */
    @Getter
    @Column
    private String formula;

    /**
     * Umbral Minimo.
     */
    @Getter
    @Column
    private Double umbralMinimo;

    /**
     * Umbral Warning.
     */
    @Getter
    @Column
    private Double umbralWarning;

    /**
     * Umbral Error.
     */
    @Getter
    @Column
    private Double umbralError;

    /**
     * Cliente (tarjeta).
     */
    @Getter
    @Column(nullable = false)
    private String tarjeta;

    /**
     * The Estado.
     */
    @Getter
    @Enumerated(EnumType.STRING)
    private EstadoComponente estadoComponente;

    /**
     * The Tipo.
     */
    @Getter
    @Enumerated(EnumType.STRING)
    private TipoComponente tipoComponente;

    /**
     * The Equipo.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @JoinColumn(name = "equipo_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Equipo equipo;

    /**
     * The Equipo id.
     */
    @Getter
    @Column(name = "equipo_id", nullable = false)
    private Long idEquipo;

    /**
     * Set the Equipo.
     *
     * @param equipo to set.
     * @return the Componente.
     */
    public Componente setEquipo(Equipo equipo) {
        if (this.equipo != null) {
            throw new PreRequisitesException("The Equipo was already set!");
        }
        this.equipo = equipo;
        this.idEquipo = equipo.getId();
        return this;
    }

    /**
     * The Estado enum.
     */
    public enum EstadoComponente {
        ESTADO_UNSPECIFIED, ESTADO_ACTIVO, ESTADO_INACTIVO, ESTADO_FALLA, ESTADO_REPARACION
    }

    /**
     * The Tipo enum.
     */
    public enum TipoComponente {
        TIPO_UNSPECIFIED, TIPO_SENSOR, TIPO_ACTUADOR, TIPO_SENSOR_ACTUADOR,
    }
}
