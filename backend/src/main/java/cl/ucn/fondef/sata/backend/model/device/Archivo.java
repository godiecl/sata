/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.device;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Archivo.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "archivos")
public final class Archivo extends BaseEntity {

    /**
     * The Nombre.
     */
    @Getter
    @Column(nullable = false)
    private String nombre;

    /**
     * The Size.
     */
    @Getter
    @Column(nullable = false)
    private Long size;

    /**
     * The URL.
     */
    @Getter
    @Column
    private String url;

    /**
     * The Tipo.
     */
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoArchivo tipoArchivo;

    /**
     * The Equipo.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    private Equipo equipo;

    /**
     * Set the Equipo.
     *
     * @param equipo to set.
     * @return the Archivo.
     */
    public Archivo setEquipo(@NonNull Equipo equipo) {
        this.equipo = equipo;
        return this;
    }

    /**
     * The Tipo Enum.
     */
    public enum TipoArchivo {
        TIPO_UNSPECIFIED,
        TIPO_PNG,
        TIPO_JPG,
        TIPO_PDF
    }
}
