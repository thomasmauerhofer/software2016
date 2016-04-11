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

import com.bitschupfa.sw16.yaq.Bluetooth.ConnectionListener;
import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.PlayerList;
import com.bitschupfa.sw16.yaq.Utils.Quiz;

public class Host extends AppCompatActivity {
    private static final int REQUEST_ENABLE_DISCOVERABLE_BT = 42;

    private final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
    private final ConnectionListener btConnectionListener = new ConnectionListener();
    private PlayerList playerList;
    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupBluetooth();
        new Thread(btConnectionListener, "BT Connection Listener Thread").start();

        TextView hostnameLabel = (TextView) findViewById(R.id.lbl_hostname);
        hostnameLabel.append(btAdapter.getName());

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        btConnectionListener.close();
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

        if (btAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 0);
            startActivityForResult(discoverableIntent, REQUEST_ENABLE_DISCOVERABLE_BT);
        } else {
            btConnectionListener.setDiscoverable(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_DISCOVERABLE_BT:
                Log.d("Host:onActivityResult", "requestCode: REQUEST_ENABLE_DISCOVERABLE_BT");
                if (resultCode != RESULT_CANCELED) {
                    btConnectionListener.setDiscoverable(true);
                } else {
                    Log.d("BT", "Could not enable discoverability.");
                    finish();
                }
                break;
            default:
                Log.d("Host:onActivityResult", "unknown requestCode: " + resultCode);
        }
    }
}
