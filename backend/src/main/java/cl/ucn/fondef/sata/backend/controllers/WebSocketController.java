/*
 * Copyright (c) 2023. Fondef IDeA I+D. Universidad Catolica del Norte.
 */

package cl.ucn.fondef.sata.backend.controllers;

import cl.ucn.fondef.sata.backend.services.MedidaService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * The WebSocketController.
 *
 * @author Diego Urrutia-Astorga.
 */
@Slf4j
@Controller
public class WebSocketController extends AbstractWebSocketHandler {

    /**
     * The Medida Service.
     */
    private final MedidaService ms;

    /**
     * The Constructor.
     */
    @Autowired
    public WebSocketController(
            @NonNull final MedidaService ms
    ) {
        this.ms = ms;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.debug("Message: {}", message.getPayload());
        session.sendMessage(message);
    }

    /*
    @MessageMapping("/medidas")
    public MedidaDTO addMedida(@NonNull final MedidaDTO medidaDTO) {
        return medidaDTO;
    }

    @Data
    public static class MedidaDTO {
        private Double senial;
        private Double valor;
        private String fecha; // FIXME: What's the format of the date?
        private Long idComponente;
        private Long idEjecucion;
    }
    */
}
