package com.bitschupfa.sw16.yaq.communication.messages;

import android.bluetooth.BluetoothAdapter;

import com.bitschupfa.sw16.yaq.communication.ClientMessageHandler;
import com.bitschupfa.sw16.yaq.communication.HostMessageHandler;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private final String senderAddress;

    public Message() {
        senderAddress = BluetoothAdapter.getDefaultAdapter() != null ?
                BluetoothAdapter.getDefaultAdapter().getAddress() : "n/a";
    }

    public Message(String address) {
        senderAddress = address;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public abstract void action(HostMessageHandler handler);
    public abstract void action(ClientMessageHandler handler);
}
