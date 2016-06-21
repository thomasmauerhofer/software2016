package com.bitschupfa.sw16.yaq.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.bluetooth.BTService;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedHostDevice;
import com.bitschupfa.sw16.yaq.game.ClientGameLogic;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;
import com.bitschupfa.sw16.yaq.ui.BluetoothDeviceList;
import com.bitschupfa.sw16.yaq.ui.PlayerList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Join extends YaqActivity implements Lobby {
    private final static String TAG = "JoinGameActivity";
    private final static int REQUEST_ENABLE_BT = 42;
    private final static int REQUEST_COARSE_LOCATION_PERMISSIONS = 43;

    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private final List<BluetoothDevice> pairedDevices = new ArrayList<>();
    private final List<BluetoothDevice> discoveredDevices = new ArrayList<>();

    private PlayerList playerList;

    private AlertDialog findDeviceDialog;
    private BluetoothDeviceList discovered;
    private BluetoothDeviceList paired;
    private ProgressBar dialogBar;

    private final BroadcastReceiver btBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                    if (state == BluetoothAdapter.STATE_TURNING_OFF) {
                        Log.d(TAG, "Bluetooth was turned off!");
                        finish();
                    }
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    Log.d(TAG, "Bluetooth device discovery started.");
                    dialogBar.setVisibility(View.VISIBLE);
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d(TAG, "Bluetooth device discovery finished.");
                    dialogBar.setVisibility(View.INVISIBLE);
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    Log.d(TAG, "Discovered new Bluetooth device " + dev.getAddress());

                    if (dev.getName() == null) {
                        Log.d(TAG, "Could not determine the name of the discovered device. skip.");
                        return;
                    }

                    for (BluetoothDevice alreadyDiscoveredDevice : discoveredDevices) {
                        if (alreadyDiscoveredDevice.getAddress().equals(dev.getAddress())) {
                            Log.d(TAG, "There is already a device with this address in the list.");
                            return;
                        }
                    }

                    discoveredDevices.add(dev);
                    discovered.notifyDataSetChanged();
                    break;
                default:
                    Log.d(TAG, intent.getAction() + " intent was not handled in BT broadcast receiver.");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ClientGameLogic.getInstance().setLobbyActivity(this);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btBroadcastReceiver, filter);

        if (!ClientGameLogic.getInstance().isConnected() && setupBluetooth()) {
            findOtherBluetoothDevices();
        }

        playerList = new PlayerList(this);

        handleTheme();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(btBroadcastReceiver);

        if (findDeviceDialog != null && findDeviceDialog.isShowing()) {
            findDeviceDialog.cancel();
        }
    }

    private boolean setupBluetooth() {
        setBluetoothDeviceLists();

        if (btAdapter == null) {
            new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage("Bluetooth is not supported on this device.")
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
            return false;
        }

        if (!btAdapter.isEnabled()) {
            Log.d(TAG, "Bluetooth is disabled. Ask user to enable it.");
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }

        return true;
    }

    private void findOtherBluetoothDevices() {
        int hasPermission = ActivityCompat.checkSelfPermission(Join.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        if (hasPermission != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Ask user for permission to discover nearby devices.");
            ActivityCompat.requestPermissions(Join.this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_COARSE_LOCATION_PERMISSIONS);
            return;
        }

        for (BluetoothDevice dev : btAdapter.getBondedDevices()) {
            pairedDevices.add(dev);
            paired.notifyDataSetChanged();
            Log.d(TAG, "Added already paired device: " + dev.getName());
        }

        if (btAdapter.isDiscovering()) {
            Log.d(TAG, "Device is already discovering. Cancel and begin again.");
            btAdapter.cancelDiscovery();
        }
        if (!btAdapter.startDiscovery()) {
            Log.e(TAG, "Could not start Bluetooth device discovery.");
            Toast.makeText(Join.this, R.string.start_discovery_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Log.d(TAG, "Bluetooth was enabled. Starting device discovery.");
            findOtherBluetoothDevices();
        } else if (requestCode == REQUEST_ENABLE_BT) {
            Log.d(TAG, "Could not enable Bluetooth.");
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_COARSE_LOCATION_PERMISSIONS:
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission for device discovery was granted by the user.");
                    findOtherBluetoothDevices();
                } else {
                    Log.d(TAG, "Permission for device discovery was not granted by the user.");
                }
                break;
        }
    }

    private void setBluetoothDeviceLists() {
        ViewGroup parent = (ViewGroup) findViewById(android.R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_find_device, parent, false);
        findDeviceDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(R.string.dialog_find_device_title)
                .setPositiveButton(R.string.refresh, null)
                .create();
        findDeviceDialog.setCanceledOnTouchOutside(false);

        dialogBar = (ProgressBar) dialogView.findViewById(R.id.find_devices_bar);
        ListView pairedList = (ListView) dialogView.findViewById(R.id.paired_devices);
        ListView discoveredList = (ListView) dialogView.findViewById(R.id.unpaired_devices);
        TextView noPairedDevices = (TextView) dialogView.findViewById(R.id.no_paired_devices_found);
        TextView noUnpairedDevices = (TextView) dialogView.findViewById(R.id.no_unpaired_devices_found);

        paired = new BluetoothDeviceList(this, pairedDevices, noPairedDevices);
        discovered = new BluetoothDeviceList(this, discoveredDevices, noUnpairedDevices);

        pairedList.setAdapter(paired);
        discoveredList.setAdapter(discovered);

        AdapterView.OnItemClickListener onPairedDevClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                BluetoothDevice selectedDev = pairedDevices.get(position);
                Log.d(TAG, "Selected already paired device: " + selectedDev.getName());
                new ClientConnector().execute(selectedDev);
            }
        };
        AdapterView.OnItemClickListener onDiscoveredDevClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                BluetoothDevice selectedDev = discoveredDevices.get(position);
                Log.d(TAG, "Selected discovered device: " + selectedDev.getName());
                new ClientConnector().execute(selectedDev);
            }
        };

        pairedList.setOnItemClickListener(onPairedDevClickListener);
        discoveredList.setOnItemClickListener(onDiscoveredDevClickListener);

        findDeviceDialog.show();

        findDeviceDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pairedDevices.clear();
                discoveredDevices.clear();
                paired.notifyDataSetChanged();
                discovered.notifyDataSetChanged();
                findOtherBluetoothDevices();
            }
        });

        findDeviceDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Join.this.finish();
            }
        });
    }

    @Override
    public PlayerProfile accessPlayerProfile() {
        return PlayerProfileStorage.getInstance(this).getPlayerProfile();
    }

    @Override
    public void updatePlayerList(final String[] playerNames) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerList.clear();
                playerList.addAll(playerNames);
            }
        });
    }

    @Override
    public void openGameActivity() {
        Intent intent = new Intent(Join.this, Game.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void handleFullGame() {
        ClientGameLogic.getInstance().quit();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(Join.this, R.string.game_full_error, Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }

    @Override
    public void quit() {
        finish();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private final class ClientConnector extends AsyncTask<BluetoothDevice, Void, ConnectedDevice> {
        private final static String TAG = "BTClientConnector";

        @Override
        protected ConnectedDevice doInBackground(BluetoothDevice... params) {
            if (params.length < 1) {
                Log.e(TAG, "No Bluetooth device was submitted to the connection task.");
                return null;
            }
            BluetoothDevice dev = params[0];

            Log.d(TAG, "Create socket.");
            BluetoothSocket btSocket;
            try {
                btSocket = dev.createRfcommSocketToServiceRecord(BTService.SERVICE_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Could not create Bluetooth socket: " + e.getMessage());
                return null;
            }

            Log.d(TAG, "Starting task.");
            if (btSocket == null) {
                Log.e(TAG, "No Bluetooth socket available. Stop task.");
                return null;
            }

            BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            if (btAdapter.isDiscovering()) {
                Log.d(TAG, "Device is currently in discover mode. Cancel discovery to avoid connection issues.");
                btAdapter.cancelDiscovery();
            }

            try {
                btSocket.connect();
            } catch (IOException e) {
                Log.e(TAG, "Could not establish connection to other device: " + e.getMessage());
                return null;
            }

            ConnectedDevice server = null;
            try {
                server = new ConnectedHostDevice(btSocket.getRemoteDevice().getAddress(),
                        btSocket, ClientGameLogic.getInstance());
            } catch (IOException e) {
                Log.e(TAG, "Could not create new ConnectedDevice: " + e.getMessage());
            }

            return server;
        }

        @Override
        protected void onPostExecute(ConnectedDevice connectedDevice) {
            Log.d(TAG, "Task finished.");
            if (connectedDevice != null) {
                try {
                    ClientGameLogic.getInstance().setConnectedHostDevice(connectedDevice);
                    findDeviceDialog.dismiss();
                } catch (IOException e) {
                    Log.e(TAG, "Could not send HELLO message to host: " + e.getMessage());
                    Toast.makeText(Join.this, R.string.communication_error,
                            Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(Join.this, R.string.connection_error, Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onBackPressed() {
        ClientGameLogic.getInstance().quit();
        super.onBackPressed();
    }
}
