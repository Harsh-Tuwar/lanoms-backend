package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

}
