/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories;

import cl.ucn.fondef.sata.backend.model.registry.Usuario;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * The Usuario repository.
 *
 * @author Diego Urrutia-Astorga.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // @Transactional(readOnly = true)
    Usuario findByEmail(@Param("email") @NonNull String email);

    // @Transactional(readOnly = true)
    Usuario findByRut(@Param("rut") String rut);

}
