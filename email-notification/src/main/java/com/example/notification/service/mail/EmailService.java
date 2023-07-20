package com.example.notification.service.mail;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {
    void sendSimpleMessage(SimpleMailMessage message);
}
