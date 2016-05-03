package com.bitschupfa.sw16.yaq.communication;

abstract class Message {
    private final String senderAddress;

    public Message(String address) {
        senderAddress = address;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public abstract void action();
}
