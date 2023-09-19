package dev.luisoliveira.mule.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String destination, String message) {
        log.debug("Sending message to destination: {}", destination);
        log.debug("Message content: {}", message);

        jmsTemplate.send(destination, session -> {
            javax.jms.TextMessage textMessage = session.createTextMessage();
            textMessage.setText(message);

            log.debug("Created text message: {}", textMessage);

            return textMessage;
        });
    }
}
