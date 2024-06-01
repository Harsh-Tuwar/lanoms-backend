package com.itec3506.summer24.loms.types;

import jakarta.persistence.Enumerated;

public enum UserStatusEnum {
    ONLINE,
    AWAY,
    BUSY,
    ON_THE_PHONE,
    LUNCH;

    public static UserStatusEnum fromShortName(Integer shortId) {
        return switch (shortId) {
            case 0 -> UserStatusEnum.ONLINE;
            case 1 -> UserStatusEnum.AWAY;
            case 2 -> UserStatusEnum.BUSY;
            case 3 -> UserStatusEnum.ON_THE_PHONE;
            case 4 -> UserStatusEnum.LUNCH;
            default -> throw new IllegalArgumentException("ShortName [" + shortId
                    + "] not supported.");
        };
    }
}
