/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.services;

import cl.ucn.fondef.sata.backend.repositories.UsuarioRepository;
import cl.ucn.fondef.sata.backend.exceptions.IntegrityException;
import cl.ucn.fondef.sata.backend.model.registry.Usuario;
import cl.ucn.fondef.sata.backend.model.device.Equipo;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * The Usuario Service.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Service
@Scope("singleton")
public class UsuarioService {

    /**
     * The Argon hasher.
     */
    private static final PasswordEncoder PASSWORD_ENCODER = new Argon2PasswordEncoder(
            16,
            32,
            Runtime.getRuntime().availableProcessors() * 2, // 1 cpu
            1 << 16, // 2^16 (64MB). Official: 12
            6 // Official: 3
    );

    /**
     * The Usuario Repo.
     */
    private final UsuarioRepository usuarioRepository;

    /**
     * The Constructor.
     *
     * @param usuarioRepository the repo.
     */
    @Autowired
    public UsuarioService(
            @NonNull final UsuarioRepository usuarioRepository
    ) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * The number of Usuario.
     *
     * @return the size of {@link Usuario} table.
     */
    public Long getUsuarioSize() {
        return this.usuarioRepository.count();
    }

    /**
     * Retrieve a {@link Usuario}.
     *
     * @param rutEmail to use.
     * @return the {@link Usuario}.
     */
    // @Transactional(readOnly = true)
    public Optional<Usuario> retrieveUsuario(@NonNull final String rutEmail) {
        log.debug("Retrieving Usuario for <{}> ..", rutEmail);

        // stripping
        String rutEmailFixed = StringUtils.strip(rutEmail);

        // empty
        if (StringUtils.isEmpty(rutEmailFixed)) {
            return Optional.empty();
        }

        // finding by email
        if (rutEmailFixed.contains("@")) {
            return Optional.ofNullable(this.usuarioRepository.findByEmail(rutEmailFixed));
        }

        // finding by rut
        return Optional.ofNullable(this.usuarioRepository.findByRut(rutEmailFixed));
    }

    /**
     * Retrieve a {@link Equipo}.
     *
     * @param id to use.
     * @return the {@link Equipo}.
     */
    public Optional<Usuario> retrieveUsuario(@NonNull final Long id) {
        log.debug("Retrieving Usuario for id:{} ..", id);

        try {
            return Optional.of(this.usuarioRepository.getReferenceById(id));
        } catch (EntityNotFoundException | JpaObjectRetrievalFailureException ex) {
            return Optional.empty();
        }
    }

    /**
     * Authenticate a Usuario.
     *
     * @param rutEmail to use.
     * @param password to use.
     * @return the Usuario.
     */
    public Optional<Usuario> authenticate(@NonNull final String rutEmail, @NonNull final String password) {

        // stripping
        String rutEmailStrip = StringUtils.strip(rutEmail);
        String passwordStrip = StringUtils.strip(password);

        // check empty
        if (StringUtils.isEmpty(rutEmailStrip) || StringUtils.isEmpty(passwordStrip)) {
            log.warn("RutEmail or Password empty, skipping!");
            return Optional.empty();
        }

        // find the Usuario
        Optional<Usuario> oUsuario = this.retrieveUsuario(rutEmailStrip);

        // not found
        if (oUsuario.isEmpty()) {
            return Optional.empty();
        }

        // wrong password
        if (!PASSWORD_ENCODER.matches(passwordStrip, oUsuario.get().getPassword())) {
            log.warn("Usuario with login=<{}> use a wrong password!", rutEmailStrip);
            return Optional.empty();
        }

        // all ok!
        return oUsuario;
    }

    /**
     * Create a Usuario in the backend.
     *
     * @param usuario to create.
     * @return the Usuario created.
     */
    @Transactional
    public Usuario addUsuario(@NonNull final Usuario usuario) {

        // fix
        Usuario usuarioFixed = Usuario.fixAndValidate(usuario);

        // encode the password using the Password Encoder
        usuarioFixed.setPassword(PASSWORD_ENCODER.encode(usuarioFixed.getPassword()));

        log.debug("Saving Usuario: {}", usuarioFixed);

        // save
        try {
            return this.usuarioRepository.saveAndFlush(usuarioFixed);
        } catch (DataIntegrityViolationException ex) {
            throw new IntegrityException("Can't add Usuario: Data Integrity Violation", ex);
        }
    }

    /**
     * The List of Usuario.
     *
     * @return the List of Usuarios.
     */
    public List<Usuario> getUsuarios() {
        return this.usuarioRepository.findAll();
    }

}
