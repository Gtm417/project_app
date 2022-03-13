package com.example.notification.service.builder;

import com.example.notification.dto.MessageDto;
import org.springframework.mail.SimpleMailMessage;

public abstract class AbstractEmailMessageBuilder implements EmailMessageBuilder {
    public static String MAIL_SENDER = "std.projects.official@gmail.com";

    @Override
    public SimpleMailMessage buildMailMessage(MessageDto messageDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_SENDER);
        message.setTo(messageDto.getReceiversEmail().toArray(String[]::new));
        message.setSubject(getSubject(messageDto));
        message.setText(buildText(messageDto));
        return message;
    }

    public abstract String buildText(MessageDto messageDto);

    public abstract String getSubject(MessageDto messageDto);
}
