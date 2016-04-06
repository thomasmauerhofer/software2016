package com.bitschupfa.sw16.yaq.Activities;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.PlayerList;
import com.bitschupfa.sw16.yaq.Utils.Quiz;

public class Host extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 42;
    private static final int REQUEST_ENABLE_DISCOVERABLE_BT = 43;

    private final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private PlayerList playerList;
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupBluetooth();

        TextView hostnameLabel = (TextView) findViewById(R.id.lbl_hostname);
        hostnameLabel.append(bluetoothAdapter.getName());

        quiz = new Quiz();

        playerList = new PlayerList(this);

        playerList.addPlayer("Thomas");
        playerList.addPlayer("Manuel");
        playerList.addPlayer("Matthias");
        playerList.addPlayer("Max");
        playerList.addPlayer("Johannes");
        playerList.addPlayer("Patrik");


        playerList.removePlayerWithName("Max");
    }

    public void startButtonClicked(View view) {
        Intent intent = new Intent(Host.this, QuestionsAsked.class);
        intent.putExtra("questions", quiz.createTmpQuiz());
        startActivity(intent);
        finish();
    }

    public void buildQuizButtonClicked(View view) {
        Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void advancedSettingsButtonClicked(View view) {
        Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    private void setupBluetooth() {
        if (bluetoothAdapter == null) {
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

        // this is not really required, since activating discoverability entails enabling BT
        if (false && !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivityForResult(discoverableIntent, REQUEST_ENABLE_DISCOVERABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT && resultCode != RESULT_OK) {
            Log.d("BT", "Could not enable bluetooth.");
            finish();
        } else if (requestCode == REQUEST_ENABLE_DISCOVERABLE_BT && resultCode == RESULT_CANCELED) {
            Log.d("BT", "Could not enable discoverability.");
            finish();
        }
    }
}
