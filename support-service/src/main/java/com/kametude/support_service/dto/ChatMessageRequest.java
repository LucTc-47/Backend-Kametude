package com.kametude.support_service.dto;

import java.util.UUID;

public class ChatMessageRequest {

    private UUID orderId;
    private String content;

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    private UUID senderId;

public UUID getSenderId() {
    return senderId;
}

public void setSenderId(UUID senderId) {
    this.senderId = senderId;
}
}