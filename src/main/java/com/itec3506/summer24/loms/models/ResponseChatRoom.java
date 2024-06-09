package com.itec3506.summer24.loms.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.itec3506.summer24.loms.types.RoomTypesEnum;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseChatRoom {
    private String title;
    private String roomID;
    List<RoomParticipant> participants;
    private LocalDateTime createdAt;
    private String createdBy;
    private RoomTypesEnum roomTypeId;

    public ResponseChatRoom() {}

    public ResponseChatRoom(String title, String roomID, List<RoomParticipant> participants, LocalDateTime createdAt, String createdBy, RoomTypesEnum roomTypeId) {
        this.title = title;
        this.roomID = roomID;
        this.participants = participants;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.roomTypeId = roomTypeId;
    }

    public String getTitle() {
        return title;
    }

    public String getRoomID() {
        return roomID;
    }

    public List<RoomParticipant> getParticipants() {
        return participants;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public RoomTypesEnum getRoomTypeId() {
        return roomTypeId;
    }

    @Override
    public String toString() {
        return "ResponseChatRoom{" +
                "title='" + title + '\'' +
                ", roomID='" + roomID + '\'' +
                ", participants=" + participants +
                ", createdAt=" + createdAt +
                ", createdBy='" + createdBy + '\'' +
                ", roomTypeId=" + roomTypeId +
                '}';
    }
}
