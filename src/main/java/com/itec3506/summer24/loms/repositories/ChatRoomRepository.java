package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.ChatRoom;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, UUID> {

    @Query(value = "SELECT roomid, room_title, room_type_id, created_by, created_at, deleted_at, deleted_by, updated_at FROM chat_rooms WHERE roomid = (:room_id) AND deleted_at IS NULL;",nativeQuery = true)
    ChatRoom getRoomByRoomID(@Param("room_id") String room_id);

    @Transactional
    @Modifying
    @Query(value = "UPDATE chat_rooms SET deleted_at = now(), deleted_by=(:user_id) WHERE roomid=(:room_id);", nativeQuery = true)
    void deleteRoom(@Param("room_id") String room_id, @Param("user_id") String user_id);
}
