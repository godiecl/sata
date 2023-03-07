/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.simulation;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The Ejecucion.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ejecuciones")
public final class Ejecucion extends BaseEntity {

    /**
     * The Fecha de Inicio.
     */
    @Getter
    @Column
    private Instant fechaInicio;

    /**
     * The Fecha de Termino.
     */
    @Getter
    @Column
    private Instant fechaTermino;

    /**
     * The Descripcion.
     */
    @Getter
    @Column
    private String descripcion;

    /**
     * Resumen.
     */
    @Getter
    @Column
    private String resumen;

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
     * The Estado.
     */
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private EstadoEjecucion estadoEjecucion;

    /**
     * The Medidas.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @Builder.Default
    @OneToMany(
            mappedBy = "ejecucion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Medida> medidas = new ArrayList<>();

    /**
     * Set the Simulacion.
     *
     * @param simulacion to set.
     * @return the simulacion.
     */
    public Ejecucion setSimulacion(@NonNull final Simulacion simulacion) {
        this.simulacion = simulacion;
        this.idSimulacion = simulacion.getId();
        return this;
    }

    /**
     * The Estado enum.
     */
    public enum EstadoEjecucion {
        ESTADO_UNSPECIFIED,
        ESTADO_INICIALIZADA,
        ESTADO_EN_EJECUCION,
        ESTADO_DETENIDA,
        ESTADO_TERMINADA,
        ESTADO_ERROR,
    }
}
