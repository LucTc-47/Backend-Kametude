package com.kametude.support_service.controller;

import com.kametude.support_service.dto.ChatMessageRequest;
import com.kametude.support_service.service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;

    public ChatWebSocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request) {
        chatService.sendMessage(request.getOrderId(), request.getSenderId(), request.getContent());
    }
}