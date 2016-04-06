package com.bitschupfa.sw16.yaq.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;

public class ConnectionListener implements Runnable {
    private final BluetoothServerSocket serverSocket;

    public ConnectionListener(BluetoothAdapter btAdapter) {
        BluetoothServerSocket tmp = null;

        try {
            tmp = btAdapter.listenUsingRfcommWithServiceRecord(BTService.SERVICE_NAME, BTService.SERVICE_UUID);
        } catch (IOException e) {
            e.printStackTrace();
        }

        serverSocket = tmp;
    }

    @Override
    public void run() {
        Log.d("BT", "waiting for new connections on the server socket.");

        BluetoothSocket socket = null;
        while (true) {
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (socket != null) {
                // TODO: register socket somewhere
            }
        }
    }

    public void close() {
        Log.d("BT", "closing server socket.");

        try {
            serverSocket.close();
        } catch (IOException e) { }
    }
}
