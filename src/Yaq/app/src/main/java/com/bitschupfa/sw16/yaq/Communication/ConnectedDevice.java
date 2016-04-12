package com.bitschupfa.sw16.yaq.Communication;


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;


public class ConnectedDevice implements Runnable {
    private final static String TAG = "BTConnectedDevice";
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

    @Override
    public void run() {
        Log.d(TAG, "Starting Thread.");

        while (true) {
            try {
                Message msg = (Message) inputStream.readObject();
                // TODO: handle message
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Unable to parse received object: " + e.getMessage());
                continue;
            } catch (IOException e) {
                Log.e(TAG, "I/O Error: " + e.getMessage());
                Log.e(TAG, "Killing Thread.");
                break;
            }
        }
    }

    public void sendMessage(Message msg) throws IOException {
        outputStream.writeObject(msg);
        outputStream.flush();
    }
}
