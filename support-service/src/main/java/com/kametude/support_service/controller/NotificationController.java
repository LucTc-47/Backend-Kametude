package com.kametude.support_service.controller;

import com.kametude.support_service.entity.Notification;
import com.kametude.support_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import com.kametude.support_service.dto.CreateNotificationRequest;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Notification>> getAll(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getAllForUser(userId));
    }

    @GetMapping("/users/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnread(@PathVariable UUID userId) {
        return ResponseEntity.ok(notificationService.getUnreadForUser(userId));
    }

    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markAsRead(@PathVariable UUID notificationId) {
        return ResponseEntity.ok(notificationService.markAsRead(notificationId));
    }

    @PostMapping
public ResponseEntity<Void> create(@RequestBody CreateNotificationRequest request) {
    notificationService.create(request.getUserId(), request.getMessage(), request.getType());
    return ResponseEntity.ok().build();
}
}


