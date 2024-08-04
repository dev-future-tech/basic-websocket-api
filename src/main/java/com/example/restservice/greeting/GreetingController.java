package com.example.restservice.greeting;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.example.restservice.model.BasicMessage;
import com.example.restservice.model.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@RestController
public class GreetingController {
    private final Logger log = LoggerFactory.getLogger(GreetingController.class);
    private final Map<String, WebSocketSession> sessions;

    public GreetingController(Map<String, WebSocketSession> _sessions) {
        this.sessions = _sessions;
    }

    @PostMapping("/notifications/{user}")
    public void createNotification(@PathVariable("user") String user,
                                   @RequestBody String notification) throws IOException {

        log.debug("I have a notification for user {}", user);

        if (notification == null) {
            throw new IllegalArgumentException("Notification cannot be null");
        }

        var session = sessions.get(user);
        if(session == null) {
            throw new IllegalArgumentException(user + " is not connected");
        }
        session.sendMessage(new TextMessage(notification));
    }

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public BasicResponse basicMessage(BasicMessage message) {
        log.debug("incoming message! {}", message.getMessage());
        BasicResponse response = new BasicResponse();
        response.setUsername(message.getUsername());
        response.setResponse("I got your message!");
        return response;
    }
}
