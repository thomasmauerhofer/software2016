package com.bitschupfa.sw16.yaq.communication;


import android.bluetooth.BluetoothSocket;

import com.bitschupfa.sw16.yaq.communication.messages.Message;

import java.io.IOException;

public class ConnectedClientDevice extends ConnectedDevice {
    private final ClientMessageHandler messageHandler;

    public ConnectedClientDevice(String address, BluetoothSocket socket, ClientMessageHandler handler) throws IOException {
        super(address, socket);
        messageHandler = handler;
    }

    @Override
    protected void onMessage(Message message) {
        message.action(messageHandler);
    }
}
