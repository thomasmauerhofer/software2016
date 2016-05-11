package com.bitschupfa.sw16.yaq.communication;


import android.bluetooth.BluetoothSocket;

import com.bitschupfa.sw16.yaq.communication.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedClientDevice extends ConnectedDevice {
    private final ClientMessageHandler messageHandler;

    public ConnectedClientDevice(String address, BluetoothSocket socket, ClientMessageHandler handler) throws IOException {
        super(address, socket);
        messageHandler = handler;
    }

    public ConnectedClientDevice(String address, InputStream in, OutputStream out, ClientMessageHandler handler) throws IOException {
        super(address, in, out);
        messageHandler = handler;
    }

    @Override
    protected void onMessage(Message message) {
        message.action(messageHandler);
    }
}
