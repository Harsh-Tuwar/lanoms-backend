package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.requestBody.CreateRoomRequestBody;
import com.itec3506.summer24.loms.requestBody.UpdateRoomRequestBody;
import com.itec3506.summer24.loms.services.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/room")
public class ChatRoomController {
    @Autowired
    private ChatRoomService chatRoomService;

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @PostMapping("/create")
    @ResponseBody
    public String createNewRoom(
            HttpServletRequest request,
            @RequestBody CreateRoomRequestBody body
    ) {
        try {
            String requesterId = (String) request.getAttribute("userId");

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

    //TODO for Feni: Complete this
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

        try {
            resp.put("message", "Room updated successfully");
            resp.put("status", HttpStatus.OK.value());
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());
        }

        return ResponseEntity.ok(resp);
    }

    //TODO for Feni: Complete this
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
            @RequestBody String roomId
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        try {
            resp.put("message", "Room updated successfully");
            resp.put("status", HttpStatus.OK.value());
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());
        }

        return ResponseEntity.ok(resp);
    }
}
