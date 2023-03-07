/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.model.simulation.views;

import lombok.Builder;

/**
 * The Componente View.
 *
 * @author Diego Urrutia-Astorga.
 */
@Builder
public final class ComponenteView {
    Long id;
    String nombre;
    String descripcion;
    String unidad;
    String formula;
    Double minimo;
    Double warning;
    Double error;
}
