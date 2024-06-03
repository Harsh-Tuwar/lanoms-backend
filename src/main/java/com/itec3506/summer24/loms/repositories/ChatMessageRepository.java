package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {

    @Query(value = "SELECT message_id, room_id, timestamp, sender_user_id, content FROM chat_messages WHERE room_id=?1;", nativeQuery = true)
    ArrayList<ChatMessage> getMessagesByRoomId(String roomId);

//    @Modifying
//    @Query(value = "INSERT INTO chat_messages (content, room_id, sender_user_id, timestamp) VALUES(:content, :room_id, :sender_user_id, :timestamp);")
//    void sendMessage(@Param("content") String content,@Param("room_id") String room_id,@Param("sender_user_id") String sender_user_id,@Param("timestamp") LocalDateTime timestamp);
}
