package com.example.restservice.greeting;

import java.util.concurrent.atomic.AtomicLong;

import com.example.restservice.model.BasicMessage;
import com.example.restservice.model.BasicResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {
    private final Logger log = LoggerFactory.getLogger(GreetingController.class);

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
