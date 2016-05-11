package com.bitschupfa.sw16.yaq.communication;


import android.bluetooth.BluetoothSocket;

import com.bitschupfa.sw16.yaq.communication.messages.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedHostDevice extends ConnectedDevice {
    private final HostMessageHandler messageHandler;

    public ConnectedHostDevice(String address, BluetoothSocket socket, HostMessageHandler handler) throws IOException {
        super(address, socket);
        messageHandler = handler;
    }

    public ConnectedHostDevice(String address, InputStream in, OutputStream out, HostMessageHandler handler) throws IOException {
        super(address, in, out);
        messageHandler = handler;
    }

    @Override
    protected void onMessage(Message message) {
        message.action(messageHandler);
    }
}
