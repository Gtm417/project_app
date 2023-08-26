package org.example.projectapp.service.notification;

import org.example.projectapp.service.notification.message.MessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EventNotificationService {
    private static final Logger logger = LoggerFactory.getLogger(EventNotificationService.class);

    private final RabbitTemplate rabbitTemplate;

    private final Exchange exchange;

    public EventNotificationService(RabbitTemplate rabbitTemplate, Exchange exchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
    }

    public void sendNotification(MessageDto message, String routingKey) {
        message.setCreateDate(LocalDateTime.now());
        logger.info("Send message: {}", message);
        rabbitTemplate.convertAndSend(exchange.getName(), routingKey, message);
    }


}
