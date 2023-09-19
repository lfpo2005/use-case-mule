package dev.luisoliveira.mule.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JmsConsumer {

    @JmsListener(destination = "stg-mule")
    public void receiveMessage(String message) {
        log.debug("Received message: {}", message);
    }
}
