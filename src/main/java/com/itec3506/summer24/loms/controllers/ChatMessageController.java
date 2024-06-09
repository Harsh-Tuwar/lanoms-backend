package com.itec3506.summer24.loms.controllers;

import com.itec3506.summer24.loms.models.RoomParticipant;
import com.itec3506.summer24.loms.requestBody.SendMessageRequestBody;
import com.itec3506.summer24.loms.services.ChatMessageService;
import com.itec3506.summer24.loms.services.ChatRoomService;
import com.itec3506.summer24.loms.types.WebsocketTopicsEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatMessageController {
    @Autowired
    private ChatMessageService msgService;

    @Autowired
    private ChatRoomService chatRoomService;

    private SimpMessagingTemplate template;

    @Autowired
    public ChatMessageController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @GetMapping("")
    public ResponseEntity<HashMap<String, Object>> getMessagesByRoomId (
            HttpServletRequest request,
            @RequestParam(name = "roomId") String roomId
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        if (requesterId == null || roomId == null) {
            throw new IllegalArgumentException("Missing required data for the response!");
        }

        if (!haveAccessToTheRoomID(requesterId, roomId)) {
            throw new IllegalArgumentException("Doesn't have access to the room!");
        }

        try{
            resp.put("data", msgService.getMessagesByRoomId(roomId));
            resp.put("status", HttpStatus.OK.value());
            resp.put("message", "Messages fetched!");

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    @ResponseBody
    @PostMapping("/send")
    public ResponseEntity<HashMap<String, Object>> sendToRoomId(
        HttpServletRequest request,
        @RequestBody SendMessageRequestBody body
    ) {
        HashMap<String, Object> resp = new HashMap<>();
        String requesterId = (String) request.getAttribute("userId");

        if (requesterId == null) {
            throw new IllegalArgumentException("Missing required data for the response!");
        }

        if (!requesterId.equals(body.getSenderId())) {
            throw new IllegalArgumentException("Sender Id and payload data doesn't match!");
        }

        if (!haveAccessToTheRoomID(body.getSenderId(), body.getRoomId())) {
            throw new IllegalArgumentException("Doesn't have access to the room!");
        }

        try{
            // Record the message into DB
            msgService.sendToRoomId(body);

            // Send message to all chats channel (filtering will be done on the client side)
            this.template.convertAndSend(WebsocketTopicsEnum.CHAT.toString(), body);

            resp.put("status", HttpStatus.OK.value());
            resp.put("message", "Message Sent!");

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            resp.put("error", e.getMessage());
            resp.put("causedBy", e.getCause());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resp);
        }
    }

    protected boolean haveAccessToTheRoomID(String userId, String roomId) {
        List<RoomParticipant> roomsByUserId = chatRoomService.getRoomsByUserId(userId);

        if (roomsByUserId.isEmpty()) {
            return false;
        }

        return roomsByUserId.stream()
                .anyMatch(room -> room.getRoomId().equals(roomId));
    }
}
