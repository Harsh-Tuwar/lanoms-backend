package com.itec3506.summer24.loms.requestBody;

import com.itec3506.summer24.loms.types.RoomTypesEnum;

import java.util.List;

public class CreateRoomRequestBody {
    private List<String> participants;
    private String roomTitle;
    private RoomTypesEnum roomTypeId;

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

    public RoomTypesEnum getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(RoomTypesEnum roomTypeId) {
        this.roomTypeId = roomTypeId;
    }
}
