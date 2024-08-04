package com.example.restservice.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import static org.springframework.web.socket.CloseStatus.SERVER_ERROR;

public class CustomWebSocketHandler extends AbstractWebSocketHandler {

    private final Logger log = LoggerFactory.getLogger(CustomWebSocketHandler.class);

    private final Map<String, WebSocketSession> sessions;

    public CustomWebSocketHandler(Map<String, WebSocketSession> _sessions) {
        sessions = _sessions;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // When using KeyCloak, the principal name is take from the `sub` field in the JWT
        log.debug("Getting principal...");
        var principal = session.getPrincipal();

        if(principal == null || principal.getName() == null) {
            session.close(SERVER_ERROR.withReason("User must be authenticated"));
            return;
        }

        log.debug("Got principal {}", principal.getName());

        sessions.put(principal.getName(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var principal = session.getPrincipal();
        sessions.remove(principal.getName());
    }
}
