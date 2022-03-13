package com.example.notification;

import com.example.notification.dto.MessageDto;
import com.example.notification.dto.VacancySubscriptionMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.stereotype.Component;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = {"${spring.rabbitmq.queue}", "vacancyQueue"})
    public void receivedMessage(VacancySubscriptionMessageDto message) {
        logger.info("Message Received is {} ", message);
    }
}
