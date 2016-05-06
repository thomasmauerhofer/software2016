package com.bitschupfa.sw16.yaq.communication;

import android.bluetooth.BluetoothAdapter;

import java.io.Serializable;

abstract class Message implements Serializable {
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

    public abstract void action();
}
