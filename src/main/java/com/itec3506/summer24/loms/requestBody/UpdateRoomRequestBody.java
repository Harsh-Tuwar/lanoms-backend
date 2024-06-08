package com.itec3506.summer24.loms.requestBody;

import java.util.List;

public class UpdateRoomRequestBody {
    private List<String> participants;
    private String roomTitle;
    private String roomId;

    public UpdateRoomRequestBody(List<String> participants, String roomTitle, String roomId) {
        this.participants = participants;
        this.roomTitle = roomTitle;
        this.roomId = roomId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    public void setRoomTitle(String roomTitle) {
        this.roomTitle = roomTitle;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
