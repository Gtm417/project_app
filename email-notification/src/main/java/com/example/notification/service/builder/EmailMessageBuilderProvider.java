package com.example.notification.service.builder;

public class EmailMessageBuilderProvider {
    public static EmailMessageBuilder provideBuilderByEvent(String eventName) {
        switch (eventName) {
            case "vacancySub":
            case "vacancyUnSub":
                return new VacancySubBuilder();
            default:
                throw new IllegalArgumentException("Illegal event name=" + eventName);
        }
    }
}
