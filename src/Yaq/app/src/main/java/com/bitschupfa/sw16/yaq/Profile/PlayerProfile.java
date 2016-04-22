package com.bitschupfa.sw16.yaq.Profile;

import android.graphics.Bitmap;

import com.bitschupfa.sw16.yaq.Utils.BitmapUtils;

import java.io.Serializable;

public class PlayerProfile implements Serializable {
    private final String playerName;
    private final String avatar;

    public PlayerProfile(String name, String encodedAvatar) {
        playerName = name;
        avatar = encodedAvatar;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Bitmap getAvatar() {
        return BitmapUtils.decodeBitmap(avatar);
    }
}
