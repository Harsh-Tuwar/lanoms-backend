package com.itec3506.summer24.loms.services;

import com.itec3506.summer24.loms.models.ChatMessage;
import com.itec3506.summer24.loms.repositories.ChatMessageRepository;
import com.itec3506.summer24.loms.requestBody.SendMessageRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatMessageService {
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    public List<ChatMessage> getMessagesByRoomId(
            String roomId
    ) {
        List<ChatMessage> messages = new ArrayList<>();

        try {
            List<ChatMessage> msgResp = chatMessageRepository.getMessagesByRoomId(roomId);

            if (!msgResp.isEmpty()) {
                for (ChatMessage messageItem: msgResp) {
                    ChatMessage message = new ChatMessage();

                    message.setRoomId(messageItem.getRoomId());
                    message.setContent(messageItem.getContent());
                    message.setMessageId(messageItem.getMessageId());
                    message.setSenderUserId(messageItem.getSenderUserId());
                    message.setTimestamp(messageItem.getTimestamp().toString());

                    messages.add(message);
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return messages;
    }

    public void sendToRoomId(SendMessageRequestBody payload) {
        try {
            ChatMessage message = new ChatMessage(
                    null,
                    payload.getRoomId(),
                    payload.getTimestamp(),
                    payload.getSenderId(),
                    payload.getContent()
            );

            chatMessageRepository.save(message);

            // Send to Websockets as well
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
