package com.itec3506.summer24.loms.services;

import com.itec3506.summer24.loms.models.ChatRoom;
import com.itec3506.summer24.loms.models.RoomParticipant;
import com.itec3506.summer24.loms.repositories.ChatRoomRepository;
import com.itec3506.summer24.loms.repositories.RoomParticipantsRepository;
import com.itec3506.summer24.loms.types.RoomTypesEnum;
import com.itec3506.summer24.loms.utils.LomsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatRoomService {
    @Autowired
    private ChatRoomRepository chatRoomsRepository;

    @Autowired
    private RoomParticipantsRepository roomParticipantsRepository;

    public void createChatRoom(String roomTitle, RoomTypesEnum roomTypeId, List<String> participants, String createdBy) {
        LomsUtils utils = new LomsUtils();
        String roomId = utils.generateUuid();

        ChatRoom chatRoom = new ChatRoom(roomId, roomTitle, roomTypeId, createdBy);

        List<RoomParticipant> rpList = new ArrayList<>();

        for (String userId: participants) {
            RoomParticipant rp = new RoomParticipant(
                    null,
                    roomId,
                    userId
            );

            rpList.add(rp);
        }

        roomParticipantsRepository.saveAll(rpList);
        chatRoomsRepository.save(chatRoom);
    }
}
