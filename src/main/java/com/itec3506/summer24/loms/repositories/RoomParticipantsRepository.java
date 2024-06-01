package com.itec3506.summer24.loms.repositories;

import com.itec3506.summer24.loms.models.RoomParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomParticipantsRepository extends JpaRepository<RoomParticipant, Integer> {

}
