package com.example.springBoot.services;

import com.example.springBoot.models.ChatMessage;

import java.util.List;

public interface ChatMessageService {

    ChatMessage save(ChatMessage chatMessage);
    List<ChatMessage> findChatMessage(String senderId,String recipientId);
}
