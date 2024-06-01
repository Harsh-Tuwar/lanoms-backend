package com.itec3506.summer24.loms.requestBody;

import java.util.List;

public class UpdateRoomRequestBody {
    private List<String> participants;
    private String roomTitle;

    public UpdateRoomRequestBody(List<String> participants, String roomTitle) {
        this.participants = participants;
        this.roomTitle = roomTitle;
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
}
