package com.itec3506.summer24.loms.utils;

import java.util.UUID;


public class LomsUtils {
    public String generateUuid() {
        UUID uuid = UUID.randomUUID();

        return uuid.toString();
    }
}
