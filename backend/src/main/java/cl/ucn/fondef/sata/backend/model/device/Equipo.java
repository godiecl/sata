/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.device;

import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * The Equipo.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "equipos")
public final class Equipo extends BaseEntity {

    /**
     * Nombre.
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
     * URL Repositorio.
     */
    @Getter
    @Column
    private String urlRepositorio;

    /**
     * Estado.
     */
    @Getter
    @Enumerated(EnumType.STRING)
    private EstadoEquipo estadoEquipo;

    /**
     * Archivos.
     */
    @Getter
    @Builder.Default
    @OneToMany(
            mappedBy = "equipo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Archivo> archivos = new ArrayList<>();

    /**
     * Componentes.
     */
    @Getter
    @Builder.Default
    @OneToMany(
            mappedBy = "equipo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Componente> componentes = new ArrayList<>();

    /**
     * Simulaciones.
     */
    @Getter
    @Builder.Default
    @OneToMany(
            mappedBy = "equipo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Simulacion> simulaciones = new ArrayList<>();

    /**
     * Append a Archivo to this Equipo.
     *
     * @param archivo to append.
     * @return The Equipo.
     */
    public Equipo addArchivo(@NonNull final Archivo archivo) {
        this.archivos.add(archivo);
        archivo.setEquipo(this);
        return this;
    }

    /**
     * Append a Componente to this Equipo.
     *
     * @param componente to append.
     * @return The Equipo.
     */
    public Equipo addComponente(@NonNull final Componente componente) {
        this.componentes.add(componente);
        componente.setEquipo(this);
        return this;
    }

    /**
     * Append a Simulacion to this Equipo.
     *
     * @param simulacion to append.
     * @return The Equipo.
     */
    public Equipo addSimulacion(@NonNull final Simulacion simulacion) {
        this.simulaciones.add(simulacion);
        simulacion.setEquipo(this);
        return this;
    }

    /**
     * The Estado enum.
     */
    public enum EstadoEquipo {
        ESTADO_UNSPECIFIED,
        ESTADO_PROTOTIPO,
        ESTADO_CONSTRUCCION
    }

}
