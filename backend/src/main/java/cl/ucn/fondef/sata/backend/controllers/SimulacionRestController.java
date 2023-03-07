/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.controllers;

import cl.ucn.fondef.sata.backend.model.BaseEntity;
import cl.ucn.fondef.sata.backend.model.simulation.Ejecucion;
import cl.ucn.fondef.sata.backend.model.simulation.Evento;
import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import cl.ucn.fondef.sata.backend.model.simulation.views.MedidaView;
import cl.ucn.fondef.sata.backend.services.MedidaService;
import cl.ucn.fondef.sata.backend.services.SimulacionService;
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

import java.time.Instant;
import java.util.List;

/**
 * The Simulacion API Rest.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@RestController
@RequestMapping("/api/simulaciones")
public final class SimulacionRestController {

    /**
     * The Simulacion Service.
     */
    private final SimulacionService ss;

    /**
     * The Medida Service.
     */
    private final MedidaService ms;

    /**
     * The Constructor.
     *
     * @param ss to use.
     */
    public SimulacionRestController(@Autowired @NonNull final SimulacionService ss, @Autowired @NonNull final MedidaService ms) {
        this.ss = ss;
        this.ms = ms;
    }

    /**
     * Get the list of simulaciones.
     *
     * @return the list of simulaciones.
     */
    @GetMapping("/")
    public List<Simulacion> getSimulaciones() {
        return this.ss.retrieveSimulaciones();
    }

    /**
     * Get a Simulacion by id.
     *
     * @param id to use.
     * @return the Simulacion.
     */
    @GetMapping("/{id}")
    public Simulacion getSimulacionById(@PathVariable("id") final Long id) {
        return this.ss.retrieveSimulacion(id)
                      .orElseThrow(() -> new SimulacionNotFoundException(id));
    }

    /**
     * Add a new Simulacion.
     *
     * @param simulacionForm to use.
     * @return the Simulation created.
     */
    @PostMapping("/")
    public Simulacion addSimulacion(@RequestBody final SimulacionForm simulacionForm) {
        log.debug("SimulacionForm: {}.", BaseEntity.toString(simulacionForm));

        // the simulacion
        Simulacion simulacion = Simulacion.builder()
                                          .nombre(simulacionForm.nombre)
                                          .descripcion(simulacionForm.descripcion)
                                          .build();
        simulacion.setIdEquipo(simulacionForm.getIdEquipo());

        log.debug("Simulacion to Save: {}", simulacion);
        simulacion = this.ss.saveSimulacion(simulacion);
        log.debug("Simulacion saved: {}", simulacion);

        // insert the eventos
        for (EventoForm ef : simulacionForm.eventos) {

            // the evento
            Evento evento = Evento.builder()
                                  .intensidad(ef.intensidad)
                                  .duracion(ef.duracion)
                                  // FIXME: use the tiempo from the form of the user
                                  .tiempo(ef.duracion.intValue())
                                  // FIXME: use the descripcion from the form of the user
                                  .descripcion(ef.descripcion)
                                  .build();
            evento.setIdComponente(ef.getIdComponente());
            simulacion.addEvento(evento);
        }

        // initial state
        Ejecucion ejecucion = Ejecucion.builder()
                                       .fechaInicio(Instant.now())
                                       .estadoEjecucion(Ejecucion.EstadoEjecucion.ESTADO_INICIALIZADA)
                                       .build();
        simulacion.addEjecucion(ejecucion);

        simulacion = this.ss.saveSimulacion(simulacion);
        log.debug("Simulacion saved: {}", simulacion);

        return simulacion;
    }

    /**
     * Start a Simulacion.
     *
     * @param idSimulacion of the Simulacion.
     * @return the Simulation.
     */
    @PostMapping("/{idSimulacion}/ejecuciones/{idEjecucion}/{estado}")
    public Ejecucion updateEstadoEjecucion(
            @PathVariable("idSimulacion") final Long idSimulacion,
            @PathVariable("idEjecucion") final Long idEjecucion,
            @PathVariable("estado") final String estado
    ) {
        Ejecucion.EstadoEjecucion estadoEjecucion = Ejecucion.EstadoEjecucion.valueOf(estado);
        Ejecucion ejecucion = this.ss.updateEstadoSimulacion(idEjecucion, estadoEjecucion);
        log.debug("Ejecucion: {}.", ejecucion);
        return ejecucion;
    }

    /**
     * Add a new Ejecucion.
     *
     * @param idSimulacion of the Simulacion.
     * @return the new Ejecucion.
     */
    @PostMapping({"/{idSimulacion}/ejecuciones", "/{idSimulacion}/ejecuciones/"})
    public Ejecucion addEjecucion(
            @PathVariable("idSimulacion") final Long idSimulacion
    ) {
        Ejecucion ejecucion = this.ss.addEstadoSimulacion(idSimulacion);
        log.debug("Ejecucion: {}.", ejecucion);
        return ejecucion;
    }

    /**
     * Retrieve the Medidas of one Ejecucion.
     *
     * @param idSimulacion to use.
     * @param idEjecucion  to use.
     * @return the list of Medidas.
     */
    @GetMapping("/{idSimulacion}/ejecuciones/{idEjecucion}/medidas")
    public List<MedidaView> getMedidas(
            @PathVariable("idSimulacion") final Long idSimulacion,
            @PathVariable("idEjecucion") final Long idEjecucion) {
        return this.ms.retrieveMedidas(idSimulacion, idEjecucion, 100);
    }

    /**
     * Retrieve the Medidas of one Ejecucion and Componente.
     *
     * @param idSimulacion to use.
     * @param idEjecucion  to use.
     * @param idComponente to use.
     * @return the list of Medidas.
     */
    @GetMapping("/{idSimulacion}/ejecuciones/{idEjecucion}/medidas/{idComponente}")
    public MedidaView getMedidas(
            @PathVariable("idSimulacion") final Long idSimulacion,
            @PathVariable("idEjecucion") final Long idEjecucion,
            @PathVariable("idComponente") final Long idComponente) {
        return this.ms.retrieveMedidas(idSimulacion, idEjecucion, idComponente, 100);
    }

    /**
     * The NotFoundException.
     */
    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Simulacion Not Found")
    public static class SimulacionNotFoundException extends RuntimeException {
        public SimulacionNotFoundException(Long id) {
            super("Could not find Simulacion id: " + id + ".");
        }
    }

    /**
     * The Simulacion Form.
     */
    @Data
    public static class SimulacionForm {
        private Long idEquipo;
        private String nombre;
        private String descripcion;
        private List<EventoForm> eventos;
    }

    /**
     * The Evento Form.
     */
    @Data
    public static class EventoForm {
        private Long idComponente;
        private Double intensidad;
        private Double duracion;
        private String descripcion;
    }

}
