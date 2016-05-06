package com.bitschupfa.sw16.yaq.communication.messages;


import android.support.annotation.NonNull;
import android.util.Log;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;


public class HELLOMessage extends Message {
    private static final String TAG = "HELLOMessage";
    private PlayerProfile playerProfile;

    public HELLOMessage(@NonNull PlayerProfile profile) {
        super();
        setPlayerProfile(profile);
    }

    public HELLOMessage(@NonNull String address, @NonNull PlayerProfile profile) {
        super(address);
        setPlayerProfile(profile);
    }

    public void setPlayerProfile(PlayerProfile profile) {
        if (profile == null) {
            throw new IllegalArgumentException("PlayerProfile may not be null!");
        }
        playerProfile = profile;
    }

    @Override
    public void action(HostMessageHandler handler) {
        // noting to do here
    }

    @Override
    public void action(ClientMessageHandler handler) {
        Log.d(TAG, this.toString());
        // TODO: register client in game object: game.registerClient(playerProfile);
    }

    @Override
    public String toString() {
        return String.format("Hallo message from %s (%s)",
                playerProfile.getPlayerName(), getSenderAddress());
    }
}
