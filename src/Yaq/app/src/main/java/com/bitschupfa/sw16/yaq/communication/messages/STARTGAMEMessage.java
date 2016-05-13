package com.bitschupfa.sw16.yaq.communication.messages;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;


public class STARTGAMEMessage extends Message {
    private static final String TAG = "STARTGAMEMessage";

    public STARTGAMEMessage() {
        super();
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.startGame();
    }

    @Override
    public void action(ClientMessageHandler handler) {
        // noting to do here
    }

    @Override
    public String toString() {
        return String.format("Start game message from host (%s)", getSenderAddress());
    }
}
