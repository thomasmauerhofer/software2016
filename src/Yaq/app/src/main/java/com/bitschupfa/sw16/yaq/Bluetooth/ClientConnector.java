package com.bitschupfa.sw16.yaq.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

public class ClientConnector implements Runnable {
    private final static String TAG = "BTClientConnector";
    private final BluetoothSocket btSocket;

    public ClientConnector(BluetoothDevice dev) {
        BluetoothSocket tmp = null;
        try {
            tmp = dev.createRfcommSocketToServiceRecord(BTService.SERVICE_UUID);
        } catch (IOException e) {
            Log.e(TAG, "Could not create Bluetooth socket: " + e.getMessage());
        }

        btSocket = tmp;
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting thread.");

        if (btSocket == null) {
            Log.e(TAG, "No Bluetooth socket available. Killing thread.");
            return;
        }

        BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter.isDiscovering()) {
            Log.d(TAG, "Device is currently in discover mode. Cancel discovery to avoid connection issues.");
            btAdapter.cancelDiscovery();
        }

        try {
            btSocket.connect();
        } catch (IOException e) {
            Log.e(TAG, "Could not establish connection to other device.");
            return;
        }

        // TODO: do something with the connected socket
    }

    public void cancel() {
        if (btSocket == null) {
            return;
        }
        Log.d(TAG, "Killing thread.");

        try {
            btSocket.close();
        } catch (IOException e) { }
    }
}
