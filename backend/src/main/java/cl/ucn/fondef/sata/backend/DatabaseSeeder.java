/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend;

import cl.ucn.fondef.sata.Timer;
import cl.ucn.fondef.sata.backend.model.registry.Usuario;
import cl.ucn.fondef.sata.backend.model.device.Archivo;
import cl.ucn.fondef.sata.backend.model.device.Componente;
import cl.ucn.fondef.sata.backend.model.device.Equipo;
import cl.ucn.fondef.sata.backend.model.simulation.Ejecucion;
import cl.ucn.fondef.sata.backend.model.simulation.Evento;
import cl.ucn.fondef.sata.backend.model.simulation.Medida;
import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import cl.ucn.fondef.sata.backend.services.EquipoService;
import cl.ucn.fondef.sata.backend.services.MedidaService;
import cl.ucn.fondef.sata.backend.services.SimulacionService;
import cl.ucn.fondef.sata.backend.services.UsuarioService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/**
 * The Database loader.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Component
public class DatabaseSeeder implements CommandLineRunner {

    /**
     * The Usuario Service.
     */
    private final UsuarioService usuarioService;

    /**
     * The EquipoService.
     */
    private final EquipoService equipoService;

    /**
     * The SimulacionService.
     */
    private final SimulacionService simulacionService;

    /**
     * The MedidaService.
     */
    private final MedidaService medidaService;

    /**
     * The Constructor.
     *
     * @param usuarioService to use.
     */
    @Autowired
    public DatabaseSeeder(
            final UsuarioService usuarioService,
            final EquipoService equipoService,
            final SimulacionService simulacionService,
            final MedidaService medidaService
    ) {
        this.usuarioService = usuarioService;
        this.equipoService = equipoService;
        this.simulacionService = simulacionService;
        this.medidaService = medidaService;
    }

    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     */
    @Override
    public void run(String... args) {
        final Timer timer = Timer.start();
        log.info("Starting the database seeder ..");

        // Check Usuarios
        if (this.usuarioService.getUsuarioSize() != 0) {
            log.warn("Database already seeded, skipping!");
            return;
        }
        Usuario usuario = this.seedUsuarios();
        Equipo equipo = this.seedEquipos();
        Simulacion simulacion = this.seedSimulaciones(equipo);
        this.seedMedida(simulacion.getEjecuciones()
                                  .get(0), equipo.getComponentes()
                                                 .get(0));
        log.info("Seeder done in: {}ms.", timer.millis());
    }

    /**
     * Seed the Usuarios.
     */
    private Usuario seedUsuarios() {

        log.debug("Seeding Usuarios into the database ..");

        // The Admin Usuario
        Usuario usuario = Usuario.builder()
                                 .rut("12345678-5")
                                 .email("admin@ucn.cl")
                                 .nombre("Administrator")
                                 .apellido("Administrator")
                                 .password("admin123")
                                 .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                 .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                 .build();
        log.debug("Usuario to save: {}", usuario);

        usuario = usuarioService.addUsuario(usuario);
        log.debug("Usuario saved: {}", usuario);

        log.debug("SeedUsuarios Done.");
        return usuario;
    }

    /**
     * Seed the Equipos.
     */
    private Equipo seedEquipos() {
        log.debug("Seeding Equipos into the database ..");

        Equipo equipo = Equipo.builder()
                              .nombre("Quebrada de Guanaco")
                              .descripcion("Quebrada de Guanaco ubicada al noreste de Antofagasta")
                              .estadoEquipo(Equipo.EstadoEquipo.ESTADO_PROTOTIPO)
                              .urlRepositorio("https://www.alltrails.com/es/ruta/chile/antofagasta/ruta-quebrada-guanaco")
                              .build();
        log.debug("Equipo to save: {}", equipo);

        equipo = equipoService.saveEquipo(equipo);
        log.debug("Equipo saved: {}", equipo);

        // archivos
        equipo.addArchivo(Archivo
                .builder()
                .nombre("Quebrada de Guanaco")
                .size(1024L)
                .url("https://s1.wklcdn.com/image_122/3673342/36910715/23904415.jpg")
                .tipoArchivo(Archivo.TipoArchivo.TIPO_JPG)
                .build()
        );

        // componentes
        equipo.addComponente(Componente
                .builder()
                .nombre("Sensor de temperatura")
                .descripcion("Thermistor RTD v2.0")
                .unidad("Grados Celsius")
                .formula("T = 1 / (A + B * ln(R) + C * ln(R)^3)")
                .umbralMinimo(0.0)
                .umbralWarning(45.0)
                .umbralError(60.0)
                .tarjeta("RPI-001")
                .estadoComponente(Componente.EstadoComponente.ESTADO_ACTIVO)
                .tipoComponente(Componente.TipoComponente.TIPO_SENSOR)
                .build()
        );
        equipo.addComponente(Componente
                .builder()
                .nombre("Actuador de bomba")
                .descripcion("Pump version 3.18E MiniMed 670G")
                .unidad("Litros por minuto")
                .formula("L = 0.5 * (1 + tanh(0.1 * (x - 50)))")
                .umbralMinimo(0.0)
                .umbralWarning(10.0)
                .umbralError(20.0)
                .tarjeta("RPI-002")
                .estadoComponente(Componente.EstadoComponente.ESTADO_ACTIVO)
                .tipoComponente(Componente.TipoComponente.TIPO_ACTUADOR)
                .build()
        );
        // componentes
        equipo.addComponente(Componente
                .builder()
                .nombre("Sensor de Humedad")
                .descripcion("Sensor DHT11")
                .unidad("Porcentaje")
                .formula("H = 0.51 * x")
                .umbralMinimo(0.0)
                .umbralWarning(90.0)
                .umbralError(100.0)
                .tarjeta("RPI-001")
                .estadoComponente(Componente.EstadoComponente.ESTADO_ACTIVO)
                .tipoComponente(Componente.TipoComponente.TIPO_SENSOR)
                .build()
        );
        equipo = equipoService.saveEquipo(equipo);

        log.debug("Seed Equipos Done.");

        return equipo;
    }

    /**
     * Seed the Simulaciones.
     */
    private Simulacion seedSimulaciones(@NonNull final Equipo equipo) {
        log.debug("Seeding Simulaciones into the database ..");

        Simulacion simulacion = Simulacion.builder()
                                          .nombre("Simulacion Guanaco (demo)")
                                          .descripcion("Simulacion Base de la Quebrada de Guanaco")
                                          .build();
        simulacion.setEquipo(equipo);

        log.debug("Simulacion to save: {}", simulacion);
        simulacion = simulacionService.saveSimulacion(simulacion);
        log.debug("Simulacion saved: {}", simulacion);

        // evento
        Evento evento = Evento.builder()
                              .intensidad(100.0) // porcentaje
                              .duracion(10.0) // 10 segundos
                              .tiempo(15) // 15 segundos
                              .descripcion("Evento 1: apertura al 100% por 10 segundos a los 15 segundos")
                              .build();
        evento.setIdComponente(equipo.getComponentes()
                                     .get(1)
                                     .getId());
        simulacion.addEvento(evento);

        simulacion = simulacionService.saveSimulacion(simulacion);

        // ejecucion
        Ejecucion ejecucion = Ejecucion.builder()
                                       .fechaInicio(Instant.now())
                                       .descripcion("Ejecucion de " + simulacion.getNombre())
                                       .estadoEjecucion(Ejecucion.EstadoEjecucion.ESTADO_TERMINADA)
                                       .build();
        simulacion.addEjecucion(ejecucion);

        simulacion = simulacionService.saveSimulacion(simulacion);

        log.debug("Seed Simulaciones Done.");
        return simulacion;
    }

    /**
     * Seed the Medidas.
     */
    private void seedMedida(Ejecucion ejecucion, Componente componente) {

        log.debug("Seeding Medidas into the database ..");

        // crear la medida
        IntStream.range(0, 10)
                 .forEach(i -> {
                     Medida medida = Medida.builder()
                                           .senial(ThreadLocalRandom.current()
                                                                    .nextDouble(0.0, 100.0))
                                           .valor(ThreadLocalRandom.current()
                                                                   .nextDouble(0.0, 100.0))
                                           .fecha(Instant.now())
                                           .idComponente(componente.getId())
                                           .idEjecucion(ejecucion.getId())
                                           .build();

                     log.debug("Medida to save: {}", medida);
                     medida = medidaService.saveMedida(medida);
                     log.debug("Medida saved: {}", medida);
                 });

        log.debug("Seed Medidas Done.");

    }

}
