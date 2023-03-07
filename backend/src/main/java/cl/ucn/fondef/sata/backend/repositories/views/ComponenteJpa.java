/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.repositories.views;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The Componente Jpa.
 *
 * @author Diego Urrutia-Astorga.
 */
@AllArgsConstructor
@Getter
public final class ComponenteJpa {
    Long id;
    String nombre;
    String descripcion;
    String unidad;
    String formula;
    Double minimo;
    Double warning;
    Double error;
}
