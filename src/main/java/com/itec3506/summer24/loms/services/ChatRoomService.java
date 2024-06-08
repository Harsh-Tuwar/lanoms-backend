package com.itec3506.summer24.loms.services;

import com.itec3506.summer24.loms.models.ChatRoom;
import com.itec3506.summer24.loms.models.ResponseChatRoom;
import com.itec3506.summer24.loms.models.RoomParticipant;
import com.itec3506.summer24.loms.repositories.ChatRoomRepository;
import com.itec3506.summer24.loms.repositories.RoomParticipantsRepository;
import com.itec3506.summer24.loms.requestBody.UpdateRoomRequestBody;
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

        // check if participants list has the creator as one of the participant
        if (!participants.contains(createdBy)){
            participants.add(createdBy);
        }

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

    public List<RoomParticipant> getRoomsByUserId(String userId) {
        return roomParticipantsRepository.getRoomsByUserId(userId);
    }

    public List<ResponseChatRoom> getChatRoomsByUserId(String userId) throws Exception {
        List<ResponseChatRoom> roomList = new ArrayList<>();

        try {
            List<RoomParticipant> userRooms = roomParticipantsRepository.getRoomsByUserId(userId);

            for (RoomParticipant rpItem: userRooms) {
                String roomId = rpItem.getRoomId();

                // Get room data
                ChatRoom roomData = chatRoomsRepository.getRoomByRoomID(roomId);

                if (roomData == null) {
                    continue;
                }

                // Get all participants of the current roomID
                List<RoomParticipant> rp = roomParticipantsRepository.getParticipantsByRoomId(roomId);

                ResponseChatRoom roomListItem = new ResponseChatRoom(
                        roomData.getRoomTitle(),
                        roomData.getRoomId(),
                        rp,
                        roomData.getCreatedAt(),
                        roomData.getCreatedBy(),
                        roomData.getRoomTypeId()
                );

                roomList.add(roomListItem);
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return roomList;
    }

    public void updateRoom(String requesterId, UpdateRoomRequestBody body) throws Exception {
        try {
            String roomId = body.getRoomId();
            String newRoomTitle = body.getRoomTitle();
            List<String> newParticipants = body.getParticipants();

            // add requester as one of the participants if they are not there already
            if (!newParticipants.contains(requesterId)) {
                newParticipants.add(requesterId);
            }

            // Get existing room Data
            ChatRoom existingRoomData = chatRoomsRepository.getRoomByRoomID(roomId);

            if (existingRoomData.getRoomTypeId() == RoomTypesEnum.DM) {
                throw new Exception("Can't update DM!");
            }

            // create new room data with existing roomID
            ChatRoom updatedRoom = new ChatRoom(
                    roomId,
                    existingRoomData.getRoomTitle(),
                    existingRoomData.getRoomTypeId()
            );

            // Update room title
            updatedRoom.setRoomTitle(newRoomTitle);

            // Delete all existing participants
            roomParticipantsRepository.deleteParticipantsByRoomID(roomId);

            // Create new participants list
            List<RoomParticipant> rpList = new ArrayList<>();

            for (String userId: newParticipants) {
                RoomParticipant rp = new RoomParticipant(
                        null,
                        roomId,
                        userId
                );

                rpList.add(rp);
            }

            roomParticipantsRepository.saveAll(rpList);
            chatRoomsRepository.save(updatedRoom);
        } catch (Exception e) {
            throw new Exception("Something went wrong: " + e.getMessage());
        }
    }

    public void deleteRoom(String roomId, String userId) throws Exception {
        try {
            chatRoomsRepository.deleteRoom(roomId, userId);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
