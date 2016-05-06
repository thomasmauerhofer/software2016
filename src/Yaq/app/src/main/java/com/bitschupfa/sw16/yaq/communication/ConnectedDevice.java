package com.bitschupfa.sw16.yaq.communication;


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
        // note: the ObjectOutputStream must be constructed first on both sides since the
        // ObjectInputStream tries to read the object stream header first and this blocks until
        // the ObjectOutputStream is constructed which leads to starvation if the ObjectInputStream
        // is constructed first
        outputStream = new ObjectOutputStream(s.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(s.getInputStream());
    }

    public ConnectedDevice(InputStream in, OutputStream out) throws IOException {
        outputStream = new ObjectOutputStream(out);
        out.flush();
        inputStream = new ObjectInputStream(in);
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting Thread.");

        while (true) {
            try {
                Message msg = (Message) inputStream.readObject();
                msg.action(); // TODO: inject action target into message (e.g. the game object on the host side)
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Unable to parse received object: " + e.getMessage());
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
