package com.bitschupfa.sw16.yaq.communication;


import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.bitschupfa.sw16.yaq.communication.messages.CLIENTQUITMessage;
import com.bitschupfa.sw16.yaq.communication.messages.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


public abstract class ConnectedDevice implements Runnable {
    private final static String TAG = "BTConnectedDevice";
    private final String id;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;

    public ConnectedDevice(String id, BluetoothSocket s) throws IOException {
        this.id = id;

        // note: the ObjectOutputStream must be constructed first on both sides since the
        // ObjectInputStream tries to read the object stream header first and this blocks until
        // the ObjectOutputStream is constructed which leads to starvation if the ObjectInputStream
        // is constructed first
        outputStream = new ObjectOutputStream(s.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(s.getInputStream());
    }

    public ConnectedDevice(String id, Socket socket) throws IOException {
        this.id = id;
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting Thread.");

        while (true) {
            try {
                Message msg = (Message) inputStream.readObject();
                msg.setSenderAddress(id);
                onMessage(msg);
            } catch (ClassNotFoundException e) {
                Log.e(TAG, "Unable to parse received object: " + e.getMessage());
            } catch (IOException e) {
                Log.e(TAG, "I/O Error: " + e.getMessage());
                Log.e(TAG, "Killing Thread.");

                onMessage(new CLIENTQUITMessage(id));
                break;
            }
        }
    }

    public void sendMessage(Message msg) throws IOException {
        outputStream.writeObject(msg);
        outputStream.flush();
    }

    protected abstract void onMessage(Message message);

    public String getAddress() {
        return id;
    }
}
