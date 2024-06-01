package com.itec3506.summer24.loms.models;

import jakarta.persistence.*;
import lombok.NonNull;

import java.util.UUID;

@Entity
@Table(
        name="roomParticipants"
)
public class RoomParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    private String roomId;

    @NonNull
    private String userId;

    public RoomParticipant(Integer id, String roomId, String userId) {
        this.id = id;
        this.roomId = roomId;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NonNull String getRoomId() {
        return roomId;
    }

    public void setRoomId(@NonNull String roomId) {
        this.roomId = roomId;
    }

    public @NonNull String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }
}
