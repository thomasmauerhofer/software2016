package com.bitschupfa.sw16.yaq.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.Bluetooth.ClientConnector;
import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.ui.BluetoothDeviceList;
import com.bitschupfa.sw16.yaq.ui.PlayerList;

import java.util.ArrayList;
import java.util.List;

public class Join extends AppCompatActivity {
    private final static String TAG = "JoinGameActivity";
    private final static int REQUEST_ENABLE_BT = 42;

    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private final List<BluetoothDevice> pairedDevices = new ArrayList<>();
    private final List<BluetoothDevice> discoveredDevices = new ArrayList<>();
    private ClientConnector connector = null;

    private PlayerList playerList;

    private TextView textView;
    private ProgressBar pBar;

    private Dialog findDeviceDialog;

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
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    Log.d(TAG, "Bluetooth device discovery finished.");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    discoveredDevices.add(dev);
                    Toast.makeText(Join.this, "Discovered new device: " + dev.getName(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Discovered new device: " + dev.getName());
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

        if(setupBluetooth()) {
            findOtherBtDevices();
        }

        playerList = new PlayerList(this);
        playerList.addPlayer("Thomas");
        playerList.addPlayer("Manuel");
        playerList.addPlayer("Matthias");
        playerList.addPlayer("Max");
        playerList.addPlayer("Johannes");
        playerList.addPlayer("Patrik");

        playerList.removePlayerWithName("Max");

        pBar = (ProgressBar) findViewById(R.id.loadingBar);
        textView = (TextView) findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar.setVisibility(View.INVISIBLE);
                textView.setTextSize(40);
                startTimer();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(btBroadcastReceiver);
    }

    private boolean setupBluetooth() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(btBroadcastReceiver, filter);

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
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return false;
        }

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == RESULT_OK) {
            Log.d(TAG, "Bluetooth was enabled. Starting device discovery.");
            findOtherBtDevices();
        } else if (requestCode == REQUEST_ENABLE_BT) {
            Log.d(TAG, "Could not enable Bluetooth.");
            finish();
        }
    }

    private void findOtherBtDevices() {
        setBluetoothDeviceLists();

        for (BluetoothDevice dev : btAdapter.getBondedDevices()) {
            pairedDevices.add(dev);
            Log.d(TAG, "Added already paired device: " + dev.getName());
        }

        if (btAdapter.isDiscovering()) {
            Log.d(TAG, "Device is already discovering. Cancel and begin again.");
            btAdapter.cancelDiscovery();
        }
        if (!btAdapter.startDiscovery()) {
            Log.e(TAG, "Could not start Bluetooth device discovery.");
        }
    }

    public void startTimer()
    {
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent intent = new Intent(Join.this, QuestionsAsked.class);
                startActivity(intent);
            }
        }.start();
    }

    public void setBluetoothDeviceLists() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_find_device, null);
        findDeviceDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle(R.string.dialog_find_device_title)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Join.this.finish();
                    }
                }).create();
        findDeviceDialog.setCanceledOnTouchOutside(false);


        ListView pairedList = (ListView) dialogView.findViewById(R.id.paired_devices);
        ListView unpairedList = (ListView) dialogView.findViewById(R.id.unpaired_devices);
        TextView noPairedDevices = (TextView) dialogView.findViewById(R.id.no_paired_devices_found);
        TextView noUnpairedDevices = (TextView) dialogView.findViewById(R.id.no_unpaired_devices_found);

        BluetoothDeviceList paired = new BluetoothDeviceList(this,  pairedDevices, noPairedDevices);
        pairedList.setAdapter(paired);
        pairedList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                connector = new ClientConnector(pairedDevices.get(position));
                connector.run();

                if(connector.getError()) {
                    Toast.makeText(Join.this, getText(R.string.cant_connect_to)+ " " + pairedDevices.get(position).getName(), Toast.LENGTH_SHORT).show();
                    connector.cancel();
                } else {
                    Toast.makeText(Join.this, getText(R.string.connect_to) + " " + pairedDevices.get(position).getName(), Toast.LENGTH_SHORT).show();
                    findDeviceDialog.dismiss();
                }
            }
        });

        BluetoothDeviceList unpaired = new BluetoothDeviceList(this, discoveredDevices, noUnpairedDevices);
        unpairedList.setAdapter(unpaired);
        unpairedList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
            {
                connector = new ClientConnector(discoveredDevices.get(position));
                connector.run();

                if(connector.getError()) {
                    Toast.makeText(Join.this, getText(R.string.cant_connect_to)+ " " + discoveredDevices.get(position).getName(), Toast.LENGTH_SHORT).show();
                    connector.cancel();
                } else {
                    Toast.makeText(Join.this, getText(R.string.connect_to) + " " + discoveredDevices.get(position).getName(), Toast.LENGTH_SHORT).show();
                    findDeviceDialog.dismiss();
                }
            }
        });
        findDeviceDialog.show();
    }
}
