package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.ResponseChatRoom;
import com.itec3506.summer24.loms.models.RoomParticipant;
import com.itec3506.summer24.loms.repositories.UserInfoRepository;
import com.itec3506.summer24.loms.requestBody.CreateRoomRequestBody;
import com.itec3506.summer24.loms.requestBody.DeleteRoomRequestBody;
import com.itec3506.summer24.loms.requestBody.UpdateRoomRequestBody;
import com.itec3506.summer24.loms.services.ChatRoomService;
import com.itec3506.summer24.loms.types.RoomTypesEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/room")
public class ChatRoomController {
    @Autowired
    private UserInfoRepository userRepo;

    @Autowired
    private ChatRoomService chatRoomService;

    @PreAuthorize("hasAuthority('ROLE_USER') || hasAuthority('ROLE_SUPER_USER') || hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    @ResponseBody
    public String createNewRoom(
            HttpServletRequest request,
            @RequestBody CreateRoomRequestBody body
    ) {
        try {
            String requesterId = (String) request.getAttribute("userId");
            UserInfoRepository.UserRolesByUserId userData = userRepo.getRolesByUserId(requesterId);

            if (body.getRoomTypeId() != RoomTypesEnum.DM && userData.getRoles().contains("ROLE_USER")) {
                throw new Exception("Insufficient Permission! You are only allowed to create a DM!");
            }

            chatRoomService.createChatRoom(
                    body.getRoomTitle(),
                    body.getRoomTypeId(),
                    body.getParticipants(),
                    requesterId
            );

            return "Room created successfully!";
        } catch (Exception error) {
            return error.getMessage();
        }
    }

    /*
    Desc:
        1. This method can only be triggered by the user with a role of "SUPER_USER" or "ADMIN".
        2. Update Chat_rooms table with title
        3. No one can change the room type
        4. Update Participants from room_participants table
        5. Make sure to add the requester as one of the participant
     */
    @PreAuthorize("hasAuthority('ROLE_SUPER_USER') || hasAuthority('ROLE_ADMIN')")
    @PutMapping("/modify")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> updateRoom(
            HttpServletRequest request,
            @RequestBody UpdateRoomRequestBody body
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        try {
            chatRoomService.updateRoom(requesterId, body);
            resp.put("message", "Room updated successfully");
            resp.put("status", HttpStatus.OK.value());
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());
        }

        return ResponseEntity.ok(resp);
    }

    /*
    Desc:
        1. This method can only be triggered by the user with a role of "SUPER_USER" or "ADMIN".
        2. Update deleted_at column of "chat_rooms" table with Date.now();
     */
    @PreAuthorize("hasAuthority('ROLE_SUPER_USER') || hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> deleteRoom(
            HttpServletRequest request,
            @RequestBody DeleteRoomRequestBody body
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        try {
            chatRoomService.deleteRoom(body.getRoomID(), requesterId);
            resp.put("message", "Room Deleted successfully");
            resp.put("status", HttpStatus.OK.value());
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());
        }

        return ResponseEntity.ok(resp);
    }

    /*
    Desc:
        1. This method can be triggered by everyone
        2. Using the userID from the request
            - get all roomIds where the requested userID is a part of from the room_participants table
            - using the roomIds, return all the rooms data
     */
    @GetMapping("/my")
    @ResponseBody
    public ResponseEntity<HashMap<String, Object>> getMyRooms(
            HttpServletRequest request
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        try {
            List<ResponseChatRoom> rooms = chatRoomService.getChatRoomsByUserId(requesterId);
            List<Object> roomList = new ArrayList<>();

            if (!rooms.isEmpty()) {
                for (ResponseChatRoom room: rooms) {
                    HashMap<String, Object> roomItem = new HashMap<>();
                    roomItem.put("title", room.getTitle());
                    roomItem.put("roomID", room.getRoomID());
                    roomItem.put("createdAt", room.getCreatedAt());
                    roomItem.put("createdBy", room.getCreatedBy());
                    roomItem.put("roomType", room.getRoomTypeId());

                    List<Object> participants = new ArrayList<>();

                    for (RoomParticipant member: room.getParticipants()) {
                        HashMap<String, Object> memberItem = new HashMap<>();
                        memberItem.put("userId", member.getUserId());
                        memberItem.put("internalId", member.getId());

                        participants.add(memberItem);
                    }

                    roomItem.put("participants", participants);

                    roomList.add(roomItem);
                }
            }

            resp.put("rooms", roomList);
            resp.put("status", HttpStatus.OK.value());
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());
        }

        return ResponseEntity.ok(resp);
    }
}
