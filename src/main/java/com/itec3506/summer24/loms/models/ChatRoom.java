package com.itec3506.summer24.loms.models;

import com.itec3506.summer24.loms.types.RoomTypesEnum;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "chatRooms",
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "roomID")
        }
)
public class ChatRoom {
    @Id
    private String roomID;

    @Column(nullable = false)
    private String roomTitle;

    @Column(nullable = false)
    private RoomTypesEnum roomTypeId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false, updatable = false)
    private String createdBy;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @Column(nullable = true)
    private String deletedBy;

    // Constructors
    public ChatRoom() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public ChatRoom(String roomId, String roomTitle, RoomTypesEnum roomTypeId, String createdBy) {
        this.roomTitle = roomTitle;
        this.roomID = roomId;
        this.roomTypeId = roomTypeId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public String getRoomId() {
        return roomID;
    }

    public void setRoomId(String roomId) {
        this.roomID = roomId;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public RoomTypesEnum getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(RoomTypesEnum roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }
}
