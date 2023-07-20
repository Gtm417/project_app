package org.example.projectapp.controller;

import org.example.projectapp.service.notification.EventNotificationService;
import org.example.projectapp.service.notification.message.MessageDto;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("notification")
public class EmailNotificationController {
    private final EventNotificationService service;

    public EmailNotificationController(EventNotificationService service) {
        this.service = service;
    }

    @PostMapping("/{routingKey}")
    public void notification(@RequestBody MessageDto message, @PathVariable String routingKey) {
        service.sendNotification(message, routingKey);
    }

}
