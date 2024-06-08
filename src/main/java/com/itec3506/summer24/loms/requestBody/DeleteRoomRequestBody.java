package com.itec3506.summer24.loms.requestBody;

public class DeleteRoomRequestBody {
    private String roomID;

    public DeleteRoomRequestBody() {
    }

    public DeleteRoomRequestBody(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }
}
