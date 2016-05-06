package com.bitschupfa.sw16.yaq.communication.messages;

import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;


public class NEWPLAYERMessage extends Message {
    private static final String TAG = "NEWPLAYERMessage";
    private String[] players;

    public NEWPLAYERMessage(@NonNull String[] playerNames) {
        super();
        players = playerNames;
    }

    public NEWPLAYERMessage(@NonNull String address, @NonNull String[] playerNames) {
        super(address);
        players = playerNames;
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.updatePlayerList(players);
    }

    @Override
    public void action(ClientMessageHandler handler) {
        // noting to do here
    }

    @Override
    public String toString() {
        return String.format("New player message from host (%s)", getSenderAddress());
    }
}
