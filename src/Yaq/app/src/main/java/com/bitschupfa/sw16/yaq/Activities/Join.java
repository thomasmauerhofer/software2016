package com.bitschupfa.sw16.yaq.Activities;

import android.app.AlertDialog;
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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.PlayerList;

import java.util.HashSet;
import java.util.Set;

public class Join extends AppCompatActivity {
    private final static String TAG = "JoinGameActivity";
    private final static int REQUEST_ENABLE_BT = 42;

    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private final Set<BluetoothDevice> pairedDevices = new HashSet<>();
    private final Set<BluetoothDevice> discoveredDevices = new HashSet<>();
    private PlayerList playerList;

    private TextView textView;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupBluetooth();
        findOtherBtDevices();

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
        unregisterReceiver(broadcastReceiver);
    }

    private void setupBluetooth() {
        registerReceiver(broadcastReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));

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
        }

        if (!btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode != RESULT_OK) {
            Log.d(TAG, "Could not enable Bluetooth.");
            finish();
        }
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
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
                case BluetoothDevice.ACTION_FOUND:
                    BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    discoveredDevices.add(dev);
                    Log.d(TAG, "Discovered new device: " + dev.getName());
                    break;
            }
        }
    };

    private void findOtherBtDevices() {
        for (BluetoothDevice dev : btAdapter.getBondedDevices()) {
            pairedDevices.add(dev);
            Log.d(TAG, "Added already paired device: " + dev.getName());
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
}
