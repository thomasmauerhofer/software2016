package com.bitschupfa.sw16.yaq.communication.messages;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private String senderAddress;

    public Message() {
        senderAddress = null; // must be set by the receiver of the message
    }

    public Message(String address) {
        senderAddress = address;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String address) {
        senderAddress = address;
    }

    public abstract void action(HostMessageHandler handler);
    public abstract void action(ClientMessageHandler handler);
}
