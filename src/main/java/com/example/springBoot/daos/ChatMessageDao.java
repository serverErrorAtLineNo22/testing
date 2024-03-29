package com.example.springBoot.daos;

import com.example.springBoot.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageDao extends JpaRepository<ChatMessage,String> {
    List<ChatMessage> findByChatId(String chatId);
}
