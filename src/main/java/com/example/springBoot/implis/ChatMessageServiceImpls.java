package com.example.springBoot.implis;

import com.example.springBoot.daos.ChatMessageDao;
import com.example.springBoot.models.ChatMessage;
import com.example.springBoot.services.ChatMessageService;
import com.example.springBoot.services.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpls implements ChatMessageService {
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatMessageDao chatMessageDao;
    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId =chatRoomService
                .getChatRoomId(chatMessage.getSenderId(),chatMessage.getRecipientId(),true)
                .orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessageDao.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessage> findChatMessage(String senderId, String recipientId) {
       var chatId =chatRoomService.getChatRoomId(senderId,recipientId,false);
       return chatId.map(chatMessageDao::findByChatId).orElse(new ArrayList<>());
    }
}
