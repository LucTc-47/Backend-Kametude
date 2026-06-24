package com.kametude.support_service.dto;

import com.kametude.support_service.enums.NotificationType;

import java.util.UUID;

public class CreateNotificationRequest {

    private UUID userId;
    private String message;
    private NotificationType type;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }
}