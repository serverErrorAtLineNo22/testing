package com.example.springBoot.implis;


import com.example.springBoot.daos.ChatRoomDao;
import com.example.springBoot.models.ChatRoom;
import com.example.springBoot.services.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpls implements ChatRoomService {

    @Autowired
    private ChatRoomDao chatRoomDao;
    @Override
    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists) {
        return chatRoomDao.findBySenderIdAndRecipientId(senderId,recipientId)
                .map(ChatRoom::getId)
                .or(() -> {
                    if(createNewRoomIfNotExists){
                        var chatId=createChatId(senderId,recipientId);
                        return Optional.of(chatId);
                    }
    return Optional.empty();
                });
    }

    @Override
    public String createChatId(String senderId, String recipientId) {
        var chatId=String.format("%s_%s" ,senderId,recipientId);
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();
        chatRoomDao.save(senderRecipient);
        chatRoomDao.save(recipientSender);
        return chatId;
    }
}
