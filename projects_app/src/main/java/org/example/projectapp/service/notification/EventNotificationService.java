package org.example.projectapp.service.notification;

import org.example.projectapp.controller.RegistrationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(EventNotificationService.class);

    private final RabbitTemplate rabbitTemplate;

    private final Exchange exchange;

    public EventNotificationService(RabbitTemplate rabbitTemplate, Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendNotification() {
        //todo normal messaging
        MessageDto message = new MessageDto();
        message.setEmails(List.of("email", "test", "toDelete"));
        logger.info("Send message: {}", message);
        rabbitTemplate.convertAndSend(exchange.getName(), "generic", message);
    }


}
