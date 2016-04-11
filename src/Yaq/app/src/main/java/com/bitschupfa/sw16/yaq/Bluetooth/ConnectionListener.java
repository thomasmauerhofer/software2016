package com.bitschupfa.sw16.yaq.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

public class ConnectionListener implements Runnable {
    private final static String TAG = "BTConnectionListener";

    private BluetoothServerSocket btServerSocket;
    private volatile boolean isDiscoverable = false;

    public ConnectionListener() {

    }

    @Override
    public void run() {
        Log.d(TAG, "Starting thread.");

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Log.e(TAG, "Bluetooth not available on device. Killing thread.");
            return;
        }

        if (!isDiscoverable) {
            Log.d(TAG, "Device is not discoverable. Idle until status changes.");
            while (!isDiscoverable);
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
                // TODO: register socket somewhere
            }
        }
    }

    public void close() {
        Log.d(TAG, "Killing thread.");

        try {
            btServerSocket.close();
        } catch (IOException e) { }
    }

    public void setDiscoverable(boolean discoverable) {
        isDiscoverable = discoverable;
    }
}
