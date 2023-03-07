/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend;

import cl.ucn.fondef.sata.backend.controllers.WebSocketController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * The Main Config.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    /**
     * The WebSocketController.
     */
    @Autowired
    WebSocketController webSocketController;

    /**
     * Register the WebSocket handlers.
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        log.info("Registering WebSocket handlers ... {}", this.webSocketController);
        registry.addHandler(webSocketController, "/ws").setAllowedOriginPatterns("*");
        // registry.addHandler(webSocketController, "/ws").setAllowedOriginPatterns("*").withSockJS();
    }
}
