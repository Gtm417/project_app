package com.example.notification.service.impl;

import com.example.notification.domain.EventStatus;
import com.example.notification.domain.NotificationEvent;
import com.example.notification.dto.MessageDto;
import com.example.notification.repository.NotificationEventRepository;
import com.example.notification.service.NotificationEventService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class NotificationEventServiceImpl implements NotificationEventService {

    private final NotificationEventRepository repository;

    public NotificationEventServiceImpl(NotificationEventRepository repository) {
        this.repository = repository;
    }

    public NotificationEvent saveNewEvent(MessageDto message, String queue, LocalDateTime receiveDate) {
        Objects.requireNonNull(message.getName(), "Event name must be not null");
        Objects.requireNonNull(message.getCreateDate(), "Message date must be not null");
        Objects.requireNonNull(queue, "Queue name must be not null");
        Objects.requireNonNull(receiveDate, "Receive date must be not null");

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .eventName(message.getName())
                .messageDate(message.getCreateDate())
                .queue(queue)
                .receiveDate(receiveDate)
                .status(EventStatus.NEW)
                .build();

        return repository.save(notificationEvent);
    }
}
