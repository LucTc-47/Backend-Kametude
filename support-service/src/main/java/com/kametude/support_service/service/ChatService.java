package com.kametude.support_service.service;

import com.kametude.support_service.entity.ChatMessage;
import com.kametude.support_service.repository.ChatMessageRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatMessageRepository chatMessageRepository, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public ChatMessage sendMessage(UUID orderId, UUID senderId, String content) {
        ChatMessage message = new ChatMessage();
        message.setOrderId(orderId);
        message.setSenderId(senderId);
        message.setContent(content);

        ChatMessage saved = chatMessageRepository.save(message);

        messagingTemplate.convertAndSend("/topic/order." + orderId, saved);

        return saved;
    }

    public List<ChatMessage> getHistory(UUID orderId) {
        return chatMessageRepository.findByOrderIdOrderByTimestampAsc(orderId);
    }
}