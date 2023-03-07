/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend;

import cl.ucn.fondef.sata.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * The Configuration.
 *
 * @author Diego Urrutia-Astorga.
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure the message converters.
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {

        // remove jackson
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        converters.removeIf(converter -> converter instanceof GsonHttpMessageConverter);

        // configure gson as serializer
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(JsonUtils.GSON);
        converters.add(gsonHttpMessageConverter);
    }

    /**
     * Configure the CORS.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        ;
    }


}
