package com.itec3506.summer24.loms.types;

public enum WebsocketTopicsEnum {
    PUBLIC {
        public String toString() {
            return "/topic/public";
        }
    },

    CHAT {
        public String toString() {
            return "/topic/chat";
        }
    }
}
