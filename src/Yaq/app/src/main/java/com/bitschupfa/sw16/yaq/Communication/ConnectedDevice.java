package com.bitschupfa.sw16.yaq.Communication;


import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class ConnectedDevice {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public ConnectedDevice(BluetoothSocket s) throws IOException {
        inputStream = new ObjectInputStream(s.getInputStream());
        outputStream = new ObjectOutputStream(s.getOutputStream());
    }

    public ConnectedDevice(InputStream in, OutputStream out) throws IOException {
        inputStream = new ObjectInputStream(in);
        outputStream = new ObjectOutputStream(out);
    }

    public void sendMessage(Message msg) throws IOException {
        outputStream.writeObject(msg);
        outputStream.flush();
    }
}
