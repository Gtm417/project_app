package org.example.projectapp.service.notification;

import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class EventNotificationService {
    private final RabbitTemplate rabbitTemplate;

    private final Exchange exchange;

    public EventNotificationService(RabbitTemplate rabbitTemplate, Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendNotification() {
        String message = "Notification";
        rabbitTemplate.convertAndSend(exchange.getName(), "generic", message);
    }


}
