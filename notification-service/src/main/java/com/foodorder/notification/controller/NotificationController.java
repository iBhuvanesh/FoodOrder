package com.foodorder.notification.controller;

import com.foodorder.notification.dto.NotificationRequest;
import com.foodorder.notification.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping
    public Map<String, Object> send(@Valid @RequestBody NotificationRequest request) {
        return notificationService.dispatch(request);
    }
}
