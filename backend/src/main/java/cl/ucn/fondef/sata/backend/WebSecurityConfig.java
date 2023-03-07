/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The WebSecurityConfig Class.
 *
 * @author Diego Urrutia-Astorga.
 */
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * Configure the security filter chain.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                // .cors(AbstractHttpConfigurer::disable)
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .headers().frameOptions().sameOrigin()
        ;
        return httpSecurity.build();
    }
}
