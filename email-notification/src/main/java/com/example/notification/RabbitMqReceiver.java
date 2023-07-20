package com.example.notification;

import com.example.notification.domain.EventStatus;
import com.example.notification.domain.NotificationEvent;
import com.example.notification.dto.VacancySubscriptionMessageDto;
import com.example.notification.service.NotificationEventService;
import com.example.notification.service.builder.EmailMessageBuilder;
import com.example.notification.service.builder.EmailMessageBuilderProvider;
import com.example.notification.service.mail.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class RabbitMqReceiver implements RabbitListenerConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqReceiver.class);
    private static final String VACANCY_QUEUE = "vacancyQueue";
    private final NotificationEventService service;
    private final EmailService emailService;

    public RabbitMqReceiver(NotificationEventService service, EmailService emailService) {
        this.service = service;
        this.emailService = emailService;
    }

    @Override
    public void configureRabbitListeners(RabbitListenerEndpointRegistrar rabbitListenerEndpointRegistrar) {
    }

    @RabbitListener(queues = {VACANCY_QUEUE})
    @Transactional
    public void receivedMessage(VacancySubscriptionMessageDto message) {
        LocalDateTime receiveDate = LocalDateTime.now();
        logger.info("Message Received from \"vacancyQueue\" {} at {}", message, receiveDate);
        NotificationEvent notificationEvent = service.saveNewEvent(message, VACANCY_QUEUE, receiveDate);
        try {
            EmailMessageBuilder emailMessageBuilder = EmailMessageBuilderProvider.provideBuilderByEvent(message.getName());
            SimpleMailMessage simpleMailMessage = emailMessageBuilder.buildMailMessage(message);
            emailService.sendSimpleMessage(simpleMailMessage);
            notificationEvent.setProcessedDate(LocalDateTime.now());
            notificationEvent.setStatus(EventStatus.OK);
        } catch (Exception e) {
            logger.error("Exception during process message {}", e.getMessage());
            notificationEvent.setProcessedDate(LocalDateTime.now());
            notificationEvent.setStatus(EventStatus.ERROR);
        }
    }
}
