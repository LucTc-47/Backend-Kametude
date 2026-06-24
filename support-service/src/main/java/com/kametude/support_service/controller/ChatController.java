package com.kametude.support_service.controller;

import com.kametude.support_service.entity.ChatMessage;
import com.kametude.support_service.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/orders/{orderId}/messages")
    public ResponseEntity<List<ChatMessage>> getHistory(@PathVariable UUID orderId) {
        return ResponseEntity.ok(chatService.getHistory(orderId));
    }
}