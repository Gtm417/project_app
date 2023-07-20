package com.example.notification.service.builder;

import com.example.notification.dto.MessageDto;
import org.springframework.mail.SimpleMailMessage;

public interface EmailMessageBuilder {

    SimpleMailMessage buildMailMessage(MessageDto messageDto);
}
