/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.controllers;

import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.registry.Usuario;
import cl.ucn.fondef.sata.backend.services.UsuarioService;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The Usuario API REST.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    /**
     * The Usuario Service.
     */
    private final UsuarioService us;

    /**
     * The Constructor.
     *
     * @param us to use.
     */
    public UsuarioRestController(
            @Autowired @NonNull final UsuarioService us
    ) {
        this.us = us;
    }

    /**
     * Get the list of Usuarios.
     *
     * @return the list of Usuarios.
     */
    @GetMapping("/")
    public List<Usuario> getUsuarios() {
        return this.us.getUsuarios();
    }

    /**
     * The Login.
     *
     * @return the Usuario.
     */
    @PostMapping("/login")
    public Usuario login(@RequestBody final LoginForm loginForm) {
        log.debug("LoginForm: {}.", BaseEntity.toString(loginForm));
        return this.us.authenticate(loginForm.rutEmail, loginForm.password)
                      .orElseThrow();
    }

    /**
     * The Login Form.
     */
    @Data
    public static class LoginForm {
        private String rutEmail;
        private String password;
    }

}
