package com.example.notification;

import com.example.notification.dto.MessageDto;
import com.example.notification.dto.VacancySubscriptionMessageDto;
import com.example.notification.service.NotificationEventService;
import com.rabbitmq.client.BlockedCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private static final String VACANCY_QUEUE = "vacancyQueue";
    private final NotificationEventService service;

    public RabbitMqReceiver(NotificationEventService service) {
        this.service = service;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = {VACANCY_QUEUE})
    @Transactional
    public void receivedMessage(VacancySubscriptionMessageDto message) {
        LocalDateTime receiveDate = LocalDateTime.now();
        logger.info("Message Received from \"vacancyQueue\" {} at {}", message, receiveDate);
        service.saveNewEvent(message, VACANCY_QUEUE, receiveDate);
    }
}
