package com.bitschupfa.sw16.yaq.Bluetooth;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.bitschupfa.sw16.yaq.Communication.ConnectedDevice;

import java.io.IOException;

public class ClientConnector implements Runnable {
    private final static String TAG = "BTClientConnector";
    private final BluetoothSocket btSocket;
    private boolean error = false;

    public ClientConnector(BluetoothDevice dev) {
        BluetoothSocket tmp = null;
        try {
            tmp = dev.createRfcommSocketToServiceRecord(BTService.SERVICE_UUID);
        } catch (IOException e) {
            error = true;
            Log.e(TAG, "Could not create Bluetooth socket: " + e.getMessage());
        }

        btSocket = tmp;
    }

    @Override
    public void run() {
        Log.d(TAG, "Starting thread.");

        if (btSocket == null) {
            error = true;
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
            error = true;
            Log.e(TAG, "Could not establish connection to other device: " + e.getMessage());
            return;
        }

        try {
            ConnectedDevice server = new ConnectedDevice(btSocket);
            new Thread(server).start();
            // TODO: pass device object to activity and send hello message
        } catch (IOException e) {
            error = true;
            Log.e(TAG, "Could not create new ConnectedDevice: " + e.getMessage());
        }
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

    public boolean getError() {
        return error;
    }
}
