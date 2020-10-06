package net.tacs.game.controller;

import net.tacs.game.model.websocket.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    /**
     * Mapea mensajes que llegan desde el cliente (frontend) a la ruta
     * '/app/send' donde '/app' es el prefijo definido en WebSocketConfig.java
     * @param message ChatMessage enviado por el cliente Stomp
     * @return
     */
    @MessageMapping("/send")
    @SendTo("/topic/turn_end")
    public ChatMessage sendMessage(ChatMessage message) {
        return message;
    }
}
