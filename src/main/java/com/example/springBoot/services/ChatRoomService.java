package com.example.springBoot.services;

import java.util.Optional;

public interface ChatRoomService {

    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists);

    String createChatId(String senderId, String recipientId);
}
