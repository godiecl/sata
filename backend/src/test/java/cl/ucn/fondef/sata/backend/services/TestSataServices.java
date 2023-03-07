/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.services;

import cl.ucn.fondef.sata.Timer;
import cl.ucn.fondef.sata.backend.exceptions.IntegrityException;
import cl.ucn.fondef.sata.backend.exceptions.PreRequisitesException;
import cl.ucn.fondef.sata.backend.model.registry.Usuario;
import cl.ucn.fondef.sata.backend.model.device.Archivo;
import cl.ucn.fondef.sata.backend.model.device.Equipo;
import cl.ucn.fondef.sata.backend.model.simulation.Simulacion;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * The test of Sata services.
 *
 * @author Diego Urrutia-Astorga.
 */
@SpringBootTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Slf4j
public class TestSataServices {

    /**
     * The Usuario Service.
     */
    @Autowired
    private UsuarioService us;

    /**
     * The Equipo Service.
     */
    @Autowired
    private EquipoService es;

    /**
     * The Simulacion Service.
     */
    @Autowired
    private SimulacionService ss;

    /**
     * The Test of Usuarios.
     */
    @SneakyThrows
    @Test
    public void testUsuarios() {

        log.debug("Starting the Test of Usuarios ..");

        // retrieve the DatabaseSeeder Usuario
        {
            // by id ..
            Assertions.assertTrue(us.retrieveUsuario(0L)
                                    .isEmpty());
            Assertions.assertTrue(us.retrieveUsuario(1L)
                                    .isPresent());

            // by rut, email
            Assertions.assertTrue(us.retrieveUsuario("12345678-5")
                                    .isPresent());
            Assertions.assertTrue(us.retrieveUsuario("admin@ucn.cl")
                                    .isPresent());

            // not valid!
            Assertions.assertTrue(us.retrieveUsuario("")
                                    .isEmpty());
            Assertions.assertTrue(us.retrieveUsuario("null")
                                    .isEmpty());
            Assertions.assertTrue(us.retrieveUsuario("11-9")
                                    .isEmpty());
            Assertions.assertTrue(us.retrieveUsuario("i+d@ucn.cl")
                                    .isEmpty());
            Assertions.assertTrue(us.retrieveUsuario("12345678-5@ucn.cl")
                                    .isEmpty());
        }

        // insert a second Usuario
        {
            Usuario usuario = Usuario.builder()
                                     .rut("13014491-8")
                                     .email("durrutia@ucn.cl")
                                     .nombre("Diego")
                                     .apellido("Urrutia")
                                     .password("durrutia123")
                                     .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                     .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                     .build();
            log.debug("Usuario to save: {}", usuario);

            Usuario usuarioBd = this.us.addUsuario(usuario);
            log.debug("Usuario saved: {}", usuarioBd);
        }

        // save usuario
        {
            // nulls
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   .id(0L)
                                   // .rut("12345678-5")
                                   // .email("admin@ucn.cl")
                                   // .nombre("Universidad Catolica")
                                   // .apellido("del Norte")
                                   // .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut null
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   // .rut("81518400-9")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut empty
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut empty spaces
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("          ")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut already exists
            Assertions.assertThrows(IntegrityException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("12345678-5")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut invalid: letter
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-X")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut invalid: need -
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("815184009")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut invalid: letter L
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("815L8400-9")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // rut invalid: bad valid digit
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-0")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // password null
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   // .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // password empty
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // password empty
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("               ")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // email null
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   // .email("info@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // email empty
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   .email("")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // email empty
            Assertions.assertThrows(PreRequisitesException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   .email("            @ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // email already exists
            Assertions.assertThrows(IntegrityException.class,
                    () -> this.us.addUsuario(
                            Usuario.builder()
                                   // .id(0L)
                                   .rut("81518400-9")
                                   .email("durrutia@ucn.cl")
                                   .nombre("Universidad Catolica")
                                   .apellido("del Norte")
                                   .password("ucn123")
                                   .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                                   .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                                   .build()));

            // all ok
            Assertions.assertDoesNotThrow(() -> this.us.addUsuario(
                    Usuario.builder()
                           // .id(0L)
                           .rut("81518400-9")
                           .email("info@ucn.cl")
                           .nombre("Universidad Catolica")
                           .apellido("del Norte")
                           .password("ucn123")
                           .estadoUsuario(Usuario.EstadoUsuario.ESTADO_ACTIVO)
                           .rolUsuario(Usuario.RolUsuario.ROL_ADMINISTRADOR)
                           .build()));

        }

        // Retrieve a Usuario
        {
            Assertions.assertTrue(this.us.retrieveUsuario("13014491-8")
                                         .isPresent());
            Assertions.assertTrue(this.us.retrieveUsuario("durrutia@ucn.cl")
                                         .isPresent());
            Assertions.assertTrue(this.us.retrieveUsuario("81518400-9")
                                         .isPresent());
            Assertions.assertTrue(this.us.retrieveUsuario("info@ucn.cl")
                                         .isPresent());

        }

        // Authenticate
        {
            // Ok
            {
                Optional<Usuario> oUsuario = this.us.authenticate("13014491-8", "durrutia123");
                Assertions.assertTrue(oUsuario.isPresent());
            }
            {
                Optional<Usuario> oUsuario = this.us.authenticate("durrutia@ucn.cl", "durrutia123");
                Assertions.assertTrue(oUsuario.isPresent());
            }
            // Wrong
            {
                Optional<Usuario> oUsuario = this.us.authenticate("", "");
                Assertions.assertFalse(oUsuario.isPresent());
            }
            {
                Optional<Usuario> oUsuario = this.us.authenticate("13014491-8", "");
                Assertions.assertFalse(oUsuario.isPresent());
            }
            {
                Optional<Usuario> oUsuario = this.us.authenticate("13014491-8", "durrutia");
                Assertions.assertFalse(oUsuario.isPresent());
            }
            {
                Optional<Usuario> oUsuario = this.us.authenticate("13014491-0", "durrutia123");
                Assertions.assertFalse(oUsuario.isPresent());
            }
        }

        // Retrieve all the Usuarios
        {
            List<Usuario> usuarios = this.us.getUsuarios();
            Assertions.assertNotNull(usuarios);
            Assertions.assertEquals(3, usuarios.size());
            for (Usuario usuario : usuarios) {
                log.debug("Usuario: {}", usuario);
            }
        }

        log.debug("Done.");

    }


    /**
     * The Test of Equipos.
     */
    @SneakyThrows
    @Test
    public void testEquipos() {
        log.debug("Starting the Test of Equipos ..");

        // insert a Equipo
        {
            Equipo equipo = Equipo.builder()
                                  .nombre("Simulador de Lluvia")
                                  .descripcion("Mix de Hardware y Software")
                                  .estadoEquipo(Equipo.EstadoEquipo.ESTADO_PROTOTIPO)
                                  .urlRepositorio("https://sata.disc.cl")
                                  .build();
            log.debug("saveEquipo ..");
            Timer t1 = Timer.start();
            equipo = this.es.saveEquipo(equipo);
            log.debug("saveEquipo in {}ms. -> {}", t1, equipo);
        }

        // retrieve Equipo
        {
            Timer t1 = Timer.start();
            Optional<Equipo> oEquipo = this.es.retrieveEquipo(2L);
            log.debug("retrieveEquipo in {}ms. -> {}", t1, oEquipo);
            Assertions.assertTrue(oEquipo.isPresent());

            log.debug("oEquipo.get().getArchivos()");
            Assertions.assertNotNull(oEquipo.get()
                                            .getArchivos());

            log.debug("oEquipo.get().getArchivos().size()");
            Assertions.assertEquals(1, oEquipo.get()
                                              .getArchivos()
                                              .size());

            // include archivos
            {
                Equipo equipo = oEquipo.get();

                log.debug("equipo.addArchivo(..)");
                equipo.addArchivo(Archivo.builder()
                                         .nombre("One")
                                         .size(1L)
                                         .tipoArchivo(Archivo.TipoArchivo.TIPO_JPG)
                                         .build());

                log.debug("equipo.addArchivo(..)");
                equipo.addArchivo(Archivo.builder()
                                         .nombre("Two")
                                         .size(2L)
                                         .tipoArchivo(Archivo.TipoArchivo.TIPO_PDF)
                                         .build());

                log.debug("equipo.addArchivo(..)");
                equipo.addArchivo(Archivo.builder()
                                         .nombre("Three")
                                         .size(3L)
                                         .tipoArchivo(Archivo.TipoArchivo.TIPO_PNG)
                                         .build());

                log.debug("saveEquipo ..");
                Timer t2 = Timer.start();
                equipo = this.es.saveEquipo(equipo);
                log.debug("saveEquipo with associations in {}ms. -> {}", t2, equipo);
            }

        }

        // retrieve Equipo
        {
            Timer t1 = Timer.start();
            Optional<Equipo> oEquipo = this.es.retrieveEquipo(2L);
            log.debug("retrieveEquipo in {}ms. -> {}", t1, oEquipo);
            Assertions.assertTrue(oEquipo.isPresent());

            Equipo equipo = oEquipo.get();

            Assertions.assertNotNull(equipo.getArchivos());
            Assertions.assertEquals(4, oEquipo.get()
                                              .getArchivos()
                                              .size());

            equipo.addArchivo(Archivo.builder()
                                     .nombre("Four")
                                     .size(4L)
                                     .tipoArchivo(Archivo.TipoArchivo.TIPO_PDF)
                                     .build());

            // save the Equipo
            Timer t2 = Timer.start();
            equipo = this.es.saveEquipo(equipo);
            log.debug("saveEquipo in {}ms. -> {}", t2, equipo);

        }

        // retrieve Equipo (check the previous insert)
        {
            Timer t1 = Timer.start();
            Optional<Equipo> oEquipo = this.es.retrieveEquipo(2L);
            log.debug("retrieveEquipo in {}ms. -> {}", t1, oEquipo);
            Assertions.assertTrue(oEquipo.isPresent());

            Equipo equipo = oEquipo.get();

            Assertions.assertNotNull(equipo.getArchivos());
            Assertions.assertEquals(5, equipo.getArchivos()
                                             .size());
            for (Archivo archivo : equipo.getArchivos()) {
                log.debug("Archivo: {}", archivo);
            }

        }

        log.debug("Test Equipos: Done.");

    }

    /**
     * The Test of Simulacion.
     */
    @SneakyThrows
    @Test
    public void testSimulacionAndEquipos() {
        log.debug("Starting the Test of Simulacion+Equipo ..");

        // Insert a Equipo
        log.debug("Add a Equipo ..");
        {
            Equipo equipo = Equipo.builder()
                                  .nombre("Simulador de Lluvia")
                                  .descripcion("Mix de Hardware y Software")
                                  .estadoEquipo(Equipo.EstadoEquipo.ESTADO_PROTOTIPO)
                                  .urlRepositorio("https://sata.disc.cl")
                                  .build();
            log.debug("Equipo to Save: {}", equipo);
            this.es.saveEquipo(equipo);
        }

        // Insert a Simulacion
        {
            Optional<Equipo> oEquipo = this.es.retrieveEquipo(2L);
            Assertions.assertTrue(oEquipo.isPresent());

            Simulacion simulacion = Simulacion.builder()
                                              .nombre("Simulatron v1.0")
                                              .descripcion("This is Simulatron v1.0")
                                              .equipo(oEquipo.get())
                                              .build();
            log.debug("Simulacion to Save: {}", simulacion);
            this.ss.saveSimulacion(simulacion);
        }

        // Retrieve the Simulations
        Assertions.assertTrue(this.ss.retrieveSimulacion(1L)
                                     .isEmpty());

        List<Simulacion> simulaciones = this.ss.retrieveSimulaciones();
        Assertions.assertEquals(2, simulaciones.size());
        for (Simulacion simulacion : simulaciones) {
            log.debug("Simulacion: {}", simulacion);
        }

    }

}
