package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query(value = "SELECT roomid, room_title, room_type_id, created_by, created_at, deleted_at, deleted_by, updated_at FROM chat_rooms WHERE roomid = ?1;",nativeQuery = true)
    ChatRoom getRoomByRoomID(String roomID);

}
