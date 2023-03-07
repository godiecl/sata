/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.controllers;

import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.device.Componente;
import cl.ucn.fondef.sata.backend.model.device.Equipo;
import cl.ucn.fondef.sata.backend.services.EquipoService;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * The Equipo API REST.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@RestController
@RequestMapping("/api/equipos")
public class EquipoRestController {

    /**
     * The Equipo Service.
     */
    private final EquipoService es;

    /**
     * The Constructor.
     *
     * @param equipoService to use.
     */
    @Autowired
    public EquipoRestController(
            @NonNull final EquipoService equipoService
    ) {
        this.es = equipoService;
    }

    /**
     * Get the list of equipos.
     *
     * @return the list of equipos.
     */
    @GetMapping("/")
    public List<Equipo> getEquipos() {
        return this.es.retrieveEquipos();
    }

    /**
     * Add a new Equipo.
     *
     * @param ef to use.
     * @return the Equipo created.
     */
    @PostMapping("/")
    public Equipo addEquipo(@RequestBody final EquipoForm ef) {
        log.debug("EquipoForm: {}", BaseEntity.toString(ef));

        // the equipo
        Equipo equipo = Equipo.builder()
                              .nombre(ef.nombre)
                              .descripcion(ef.descripcion)
                              // FIXME: Cambiar el estado al agregar una simulacion
                              .estadoEquipo(Equipo.EstadoEquipo.ESTADO_CONSTRUCCION)
                              .urlRepositorio(ef.urlRepositorio)
                              .build();
        // save the equipo
        equipo = this.es.saveEquipo(equipo);

        for (ComponenteForm cf : ef.componentes) {
            equipo.addComponente(Componente
                    .builder()
                    .nombre(cf.nombre)
                    .descripcion(cf.descripcion)
                    .unidad(cf.unidad)
                    .formula(cf.formula)
                    .umbralMinimo(cf.umbralMinimo)
                    .umbralWarning(cf.umbralWarning)
                    .umbralError(cf.umbralError)
                    .tarjeta(cf.tarjeta)
                    .estadoComponente(Componente.EstadoComponente.ESTADO_ACTIVO)
                    .tipoComponente(Componente.TipoComponente.valueOf(cf.tipoComponente))
                    .build()
            );
        }

        equipo = this.es.saveEquipo(equipo);
        log.debug("Equipo saved: {}", equipo);
        return equipo;

    }

    /**
     * Get a Equipo by id.
     *
     * @param id to use.
     * @return the Equipo.
     */
    @GetMapping("/{id}")
    public Equipo getEquipoById(@PathVariable Long id) {
        return this.es
                .retrieveEquipo(id)
                .orElseThrow(() -> new EquipoNotFoundException(id));
    }

    /**
     * The NotFoundException.
     */
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Equipo Not Found")
    public static class EquipoNotFoundException extends RuntimeException {
        public EquipoNotFoundException(Long id) {
            super("Could not find Equipo id: " + id + ".");
        }
    }

    /**
     * The EquipoForm.
     */
    @Data
    public static class EquipoForm {
        private String nombre;
        private String descripcion;
        private String urlRepositorio;
        private List<ComponenteForm> componentes;
    }

    /**
     * The ComponenteForm.
     */
    @Data
    public static class ComponenteForm {
        private String nombre;
        private String descripcion;
        private String unidad;
        private String formula;
        private Double umbralMinimo;
        private Double umbralWarning;
        private Double umbralError;
        private String tarjeta;
        private String tipoComponente;
    }

}
