package com.example.notification.service.builder;

import com.example.notification.dto.MessageDto;
import com.example.notification.dto.VacancySubscriptionMessageDto;
import com.example.notification.service.TemplateConfigurer;

import java.util.HashMap;

import static com.example.notification.service.Util.readFileToString;

public class VacancySubBuilder extends AbstractEmailMessageBuilder {
    private static final String VACANCY_SUB_TEMPLATE = readFileToString("groovy.templates/VacancySubTemplate.txt");
    private static final String SUBSCRIBE_SUBJECT = "Someone subscribed on vacancy";
    private static final String UNSUBSCRIBED_SUBJECT = "Someone unsubscribed from vacancy";
    private static final String USER_MAIL_PLACEHOLDER = "userEmail";
    private static final String SUBSCRIBED_PLACEHOLDER = "subscribed";
    private static final String VACANCY_ID_PLACEHOLDER = "vacancyId";

    @Override
    public String buildText(MessageDto messageDto) {
        VacancySubscriptionMessageDto vacancySubMessage = (VacancySubscriptionMessageDto) messageDto;
        HashMap<String, Object> binding = new HashMap<>();
        binding.put(USER_MAIL_PLACEHOLDER, vacancySubMessage.getUserEmail());
        binding.put(SUBSCRIBED_PLACEHOLDER, isSubscribe(messageDto) ? "subscribed" : "unsubscribed");
        binding.put(VACANCY_ID_PLACEHOLDER, vacancySubMessage.getVacancyId());
        return TemplateConfigurer.processTemplate(VACANCY_SUB_TEMPLATE, binding);
    }

    @Override
    public String getSubject(MessageDto messageDto) {
        return isSubscribe(messageDto)
                ? SUBSCRIBE_SUBJECT
                : UNSUBSCRIBED_SUBJECT;
    }

    private boolean isSubscribe(MessageDto messageDto) {
        return messageDto.getName().equals("vacancySub");
    }
}
