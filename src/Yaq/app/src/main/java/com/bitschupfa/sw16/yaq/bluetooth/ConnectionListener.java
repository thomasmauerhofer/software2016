package com.bitschupfa.sw16.yaq.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.bitschupfa.sw16.yaq.communication.ConnectedClientDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;

import java.io.IOException;

public class ConnectionListener implements Runnable {
    private final static String TAG = "BTConnectionListener";

    private BluetoothServerSocket btServerSocket;
    private boolean isDiscoverable = false;


    public ConnectionListener() {

    }

    @Override
    public synchronized void run() {
        Log.d(TAG, "Starting thread.");

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Log.e(TAG, "Bluetooth not available on device. Killing thread.");
            return;
        }

        if (!isDiscoverable) {
            Log.d(TAG, "Device is not discoverable. Wait until status changes.");
            while (!isDiscoverable) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Log.e(TAG, "Interrupted while waiting for device to be discoverable. Message: "
                            + e.getMessage());
                    return;
                }
            }
            Log.d(TAG, "Device is now discoverable.");
        }

        try {
            btServerSocket = btAdapter.listenUsingRfcommWithServiceRecord(BTService.SERVICE_NAME, BTService.SERVICE_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Could not create Server Socket: " + e.getMessage());
            return;
        }

        Log.d(TAG, "Waiting for new connections on the Server Socket.");
        BluetoothSocket socket;
        while (true) {
            try {
                socket = btServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Server Socket was killed: " + e.getMessage());
                return;
            }

            if (socket != null) {
                try {
                    ConnectedDevice client =  new ConnectedClientDevice(socket, null); // TODO: inject here the correct client message handler (instead of null), i.e. the game object
                    registerClient(client);
                } catch (IOException e) {
                    Log.e(TAG, "Could not create new ConnectedDevice: " + e.getMessage());
                }
            }
        }
    }

    public void close() {
        if(btServerSocket == null) {
            return;
        }
        Log.d(TAG, "Killing thread.");

        try {
            btServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, "Error while closing the socket: " + e.getMessage());
        }
    }

    @SuppressWarnings("WeakerAccess")
    public void registerClient(ConnectedDevice client) {
        new Thread(client).start();
        // TODO: register ConnectedDevice thread in the game to send messages to the client
    }

    public synchronized void setDiscoverable() {
        isDiscoverable = true;
        notify();
    }
}
