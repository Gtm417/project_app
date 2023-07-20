package com.example.notification.service;

import com.example.notification.domain.NotificationEvent;
import com.example.notification.dto.MessageDto;

import java.time.LocalDateTime;

public interface NotificationEventService {
    NotificationEvent saveNewEvent(MessageDto message, String queue, LocalDateTime receiveDate);
}
