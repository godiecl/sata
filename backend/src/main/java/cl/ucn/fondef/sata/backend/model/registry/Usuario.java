/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.registry;

import cl.ucn.fondef.sata.JsonUtils;
import cl.ucn.fondef.sata.ValidationUtils;
import cl.ucn.fondef.sata.backend.exceptions.PreRequisitesException;
import cl.ucn.fondef.sata.backend.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * The Usuario.
 *
 * @author Diego Urrutia-Astorga.
 */
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public final class Usuario extends BaseEntity {

    /**
     * The Rut.
     */
    @Getter
    @Column(nullable = false, unique = true)
    private String rut;

    /**
     * The Email.
     */
    @Getter
    // @Email
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * The Nombre.
     */
    @Getter
    @Column(nullable = false)
    private String nombre;

    /**
     * The Apellido.
     */
    @Getter
    @Column
    private String apellido;

    /**
     * The Password.
     */
    @JsonUtils.InvisibleJson
    @Getter
    @Setter
    @Column(nullable = false)
    private String password;

    /**
     * The Estado.
     */
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoUsuario estadoUsuario;

    /**
     * The Rol.
     */
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rolUsuario;

    /**
     * Estado Enum.
     */
    public enum EstadoUsuario {
        ESTADO_UNSPECIFIED,
        ESTADO_ACTIVO,
        ESTADO_INACTIVO
    }

    /**
     * Rol Enum.
     */
    public enum RolUsuario {
        ROL_UNSPECIFIED,
        ROL_ADMINISTRADOR,
        ROL_CONFIGURADOR,
        ROL_OPERADOR
    }

    /**
     * Fix and validate the internal state.
     *
     * @param usuario to fix.
     * @return the Usuario fixed.
     */
    public static Usuario fixAndValidate(@NonNull final Usuario usuario) {
        usuario.rut = StringUtils.strip(usuario.rut);
        usuario.email = StringUtils.strip(usuario.email);
        usuario.nombre = StringUtils.strip(usuario.nombre);
        usuario.apellido = StringUtils.strip(usuario.apellido);
        usuario.password = StringUtils.strip(usuario.password);

        // Check the RUT
        if (!ValidationUtils.isRutValid(usuario.rut)) {
            throw new PreRequisitesException("Invalid RUT");
        }

        // Check the empty email
        if (StringUtils.isEmpty(usuario.email)) {
            throw new PreRequisitesException("Empty Email");
        }

        // Check the email
        if (!ValidationUtils.isEmailValid(usuario.email)) {
            throw new PreRequisitesException("Invalid Email");
        }

        // Check the empty nombre
        if (StringUtils.isEmpty(usuario.nombre)) {
            throw new PreRequisitesException("Empty nombre");
        }

        // Check the empty password
        if (StringUtils.isEmpty(usuario.password)) {
            throw new PreRequisitesException("Empty Password");
        }

        return usuario;
    }

}
