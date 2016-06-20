package com.bitschupfa.sw16.yaq.communication.messages;


import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;

public class PLAYAGAINMessage extends Message {
    public PLAYAGAINMessage() {
        super();
    }

    @Override
    public void action(HostMessageHandler handler) {
        handler.playAgain();
    }

    @Override
    public void action(ClientMessageHandler handler) {
        handler.playAgain();
    }
}
