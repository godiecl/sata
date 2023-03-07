/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.simulation;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.device.Componente;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

/**
 * The Medida.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medidas")
public final class Medida extends BaseEntity {

    /**
     * The Senial.
     */
    @Getter
    @Column(nullable = false)
    private Double senial;

    /**
     * The Valor.
     */
    @Getter
    @Column(nullable = false)
    private Double valor;

    /**
     * The Fecha.
     */
    @Getter
    @Column(nullable = false)
    private Instant fecha;

    /**
     * The Componente.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @JoinColumn(name = "componente_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Componente componente;

    /**
     * The Componente id (to not fetch the component).
     */
    @Getter
    @Setter
    @Column(name = "componente_id")
    private Long idComponente;

    /**
     * The Ejecucion.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @JoinColumn(name = "ejecucion_id", insertable = false, updatable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Ejecucion ejecucion;

    /**
     * The Ejecucion id.
     */
    @Getter
    @Setter
    @Column(name = "ejecucion_id", nullable = false)
    private Long idEjecucion;

}
