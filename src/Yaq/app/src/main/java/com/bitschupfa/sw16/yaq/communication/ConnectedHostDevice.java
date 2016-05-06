package com.bitschupfa.sw16.yaq.communication;


import android.bluetooth.BluetoothSocket;

import com.bitschupfa.sw16.yaq.communication.messages.Message;

import java.io.IOException;

public class ConnectedHostDevice extends ConnectedDevice {
    private final HostMessageHandler messageHandler;

    public ConnectedHostDevice(BluetoothSocket socket, HostMessageHandler handler) throws IOException {
        super(socket);
        messageHandler = handler;
    }

    @Override
    protected void onMessage(Message message) {
        message.action(messageHandler);
    }
}
