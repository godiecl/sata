/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The Sata Spring Boot application.
 *
 * @author Diego Urrutia-Astorga.
 */
@SpringBootApplication(exclude = { JacksonAutoConfiguration.class })
@EnableJpaAuditing
@EnableJpaRepositories
public class BackendApplication {

    /**
     * The Main application.
     *
     * @param args to use.
     */
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
