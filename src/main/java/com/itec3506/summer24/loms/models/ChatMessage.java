package com.itec3506.summer24.loms.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(
        name = "chat_messages"
)
public class ChatMessage {
    @Id
    @GeneratedValue(strategy =  GenerationType.UUID)
    private String messageId;

    @Column(nullable = false)
    private String roomId;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private String senderUserId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    public ChatMessage() {
    }

    public ChatMessage(
            String messageId,
            String roomId,
            String timestamp,
            String senderUserId,
            String content
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        this.messageId = messageId;
        this.roomId = roomId;
        this.timestamp = LocalDateTime.parse(timestamp, formatter);
        this.senderUserId = senderUserId;
        this.content = content;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        this.timestamp = LocalDateTime.parse(timestamp, formatter);
    }

    public String getSenderUserId() {
        return senderUserId;
    }

    public void setSenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
