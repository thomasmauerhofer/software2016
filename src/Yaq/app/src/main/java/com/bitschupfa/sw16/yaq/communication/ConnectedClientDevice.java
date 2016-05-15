package com.bitschupfa.sw16.yaq.communication;


import android.bluetooth.BluetoothSocket;

import com.bitschupfa.sw16.yaq.communication.messages.Message;

import java.io.IOException;
import java.net.Socket;

public class ConnectedClientDevice extends ConnectedDevice {
    private final ClientMessageHandler messageHandler;

    public ConnectedClientDevice(String id) {
        super(id);
        messageHandler = null;
    }

    public ConnectedClientDevice(String address, BluetoothSocket socket, ClientMessageHandler handler) throws IOException {
        super(address, socket);
        messageHandler = handler;
    }

    public ConnectedClientDevice(String address, Socket socket, ClientMessageHandler handler) throws IOException {
        super(address, socket);
        messageHandler = handler;
    }

    @Override
    protected void onMessage(Message message) {
        message.action(messageHandler);
    }
}
