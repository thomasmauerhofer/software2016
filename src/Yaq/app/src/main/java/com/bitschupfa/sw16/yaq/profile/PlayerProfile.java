package com.bitschupfa.sw16.yaq.profile;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class PlayerProfile implements Serializable {
    private final String playerName;
    private final String avatar;

    public PlayerProfile(@NonNull String name, @NonNull String encodedAvatar) {
        if (name == null) {
            throw new IllegalArgumentException("Player name may not be null");
        }
        if (encodedAvatar == null) {
            throw new IllegalArgumentException("Encoded avatar may not be null");
        }

        playerName = name;
        avatar = encodedAvatar;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getEncodedAvatar() {
        return avatar;
    }
}
