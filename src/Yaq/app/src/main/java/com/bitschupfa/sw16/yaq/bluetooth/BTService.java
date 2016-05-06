package com.bitschupfa.sw16.yaq.bluetooth;

import android.bluetooth.BluetoothAdapter;

import java.util.UUID;

public class BTService {
    public static final String SERVICE_NAME= "YAQ_BT_SERVICE";
    public static final String MAC_ADDRESS = BluetoothAdapter.getDefaultAdapter() != null ?
            BluetoothAdapter.getDefaultAdapter().getAddress() : "n/a";
    public static final UUID SERVICE_UUID = UUID.fromString("fa4bf3ee-e9c3-422e-98f0-37d514be1988");
}
