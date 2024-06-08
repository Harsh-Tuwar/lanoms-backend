package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.RoomParticipant;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface RoomParticipantsRepository extends JpaRepository<RoomParticipant, Integer> {

    @Query(value = "SELECT DISTINCT id, room_id, user_id FROM room_participants WHERE user_id=(:user_id)", nativeQuery = true)
    ArrayList<RoomParticipant> getRoomsByUserId(@Param("user_id") String user_id);

    @Query(value = "SELECT DISTINCT id, room_id, user_id FROM room_participants WHERE room_id=(:room_id)", nativeQuery = true)
    ArrayList<RoomParticipant> getParticipantsByRoomId(@Param("room_id") String room_id);

    @Transactional
    @Modifying
    @Query(value = "DELETE from room_participants WHERE room_id = (:room_id);", nativeQuery = true)
    void deleteParticipantsByRoomID(@Param("room_id") String room_id);
}
