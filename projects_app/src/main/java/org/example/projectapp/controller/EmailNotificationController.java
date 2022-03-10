package org.example.projectapp.controller;

import org.example.projectapp.service.notification.EventNotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notification")
public class EmailNotificationController {
    private final EventNotificationService service;

    public EmailNotificationController(EventNotificationService service) {
        this.service = service;
    }

    @PostMapping
    public void notification() {
        service.sendNotification();
    }

}
