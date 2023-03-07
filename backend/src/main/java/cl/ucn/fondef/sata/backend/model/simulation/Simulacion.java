/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.simulation;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.device.Equipo;
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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * The Simulacion.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "simulaciones")
public final class Simulacion extends BaseEntity {

    /**
     * The Nombre.
     */
    @Getter
    @Column(nullable = false)
    private String nombre;

    /**
     * The Descripcion.
     */
    @Getter
    @Column
    private String descripcion;

    /**
     * The Fecha.
     */
    @Getter
    @Column(nullable = false)
    private Instant fecha;

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
    @Setter
    @Column(name = "equipo_id", nullable = false)
    private Long idEquipo;

    /**
     * The Ejecuciones.
     */
    @Getter
    @Builder.Default
    @OneToMany(
            mappedBy = "simulacion",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Ejecucion> ejecuciones = new ArrayList<>();

    /**
     * The Eventos.
     */
    @Getter
    @Builder.Default
    @OneToMany(
            mappedBy = "simulacion",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<Evento> eventos = new ArrayList<>();

    /**
     * Set the Equipo.
     *
     * @param equipo to set.
     */
    public void setEquipo(final Equipo equipo) {
        this.equipo = equipo;
        this.idEquipo = equipo.getId();
    }

    /**
     * Append a Ejecucion to this Simulacion.
     *
     * @param ejecucion to append.
     * @return the Simulacion.
     */
    public Simulacion addEjecucion(@NonNull final Ejecucion ejecucion) {
        this.ejecuciones.add(ejecucion);
        ejecucion.setSimulacion(this);
        return this;
    }

    /**
     * Append a Evento to this Simulacion.
     *
     * @param evento to append.
     * @return the Simulacion.
     */
    public Simulacion addEvento(@NonNull final Evento evento) {
        this.eventos.add(evento);
        evento.setSimulacion(this);
        return this;
    }

    /**
     * Fix and validate the internal state.
     *
     * @param simulacion to fix.
     * @return the Simulacion fixed.
     */
    public static Simulacion fixAndValidate(@NonNull final Simulacion simulacion) {
        if (simulacion.fecha == null) {
            simulacion.fecha = Instant.now();
        }
        return simulacion;
    }

}
