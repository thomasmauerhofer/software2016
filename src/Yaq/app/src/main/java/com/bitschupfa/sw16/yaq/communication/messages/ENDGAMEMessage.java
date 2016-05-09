package com.bitschupfa.sw16.yaq.communication.messages;


import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;

public class ENDGAMEMessage extends Message {
    private static final String TAG = "ENDGAMEMessage";

    public ENDGAMEMessage() {
        super();
    }

    public ENDGAMEMessage(@NonNull String address) {
        super(address);
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.endGame();
    }

    @Override
    public void action(ClientMessageHandler handler) {
        // noting to do here
    }

    @Override
    public String toString() {
        return String.format("End game message from host (%s)", getSenderAddress());
    }
}
