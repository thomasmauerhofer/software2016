package com.bitschupfa.sw16.yaq.communication;


import android.support.annotation.NonNull;
import android.util.Log;

import com.bitschupfa.sw16.yaq.profile.PlayerProfile;

public class HELLOMessage extends Message {
    private static final String TAG = "HELLOMessage";
    private final PlayerProfile playerProfile;

    public HELLOMessage(@NonNull String address, @NonNull PlayerProfile profile) {
        super(address);
        if (profile == null) {
            throw new IllegalArgumentException("PlayerProfile may not be null!");
        }
        playerProfile = profile;
    }

    @Override
    public void action() {
        Log.d(TAG, playerProfile.toString());
        // TODO: register client in game object: game.registerClient(playerProfile);
    }

    @Override
    public String toString() {
        return String.format("Hallo message from %s (%s)",
                playerProfile.getPlayerName(), getSenderAddress());
    }
}
