package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;

import java.util.List;

/**
 * Created by thomas on 12.04.16.
 */
public class BluetoothDeviceList extends ArrayAdapter<BluetoothDevice> {

    private Activity activity;

    private List<BluetoothDevice> devices;

    TextView listtext;

    public BluetoothDeviceList(Activity activity, List<BluetoothDevice> devices, TextView listText) {
        super(activity, R.layout.list_bluetooth_device, devices);
        this.activity = activity;
        this.devices = devices;
        this.listtext = listText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if((listtext.getVisibility() == View.VISIBLE) && !devices.isEmpty()) {
            listtext.setVisibility(View.INVISIBLE);
        } else if((listtext.getVisibility() == View.INVISIBLE) && devices.isEmpty()) {
            listtext.setVisibility(View.VISIBLE);
        }

        View rowView = inflater.inflate(R.layout.list_bluetooth_device, parent, false);
        TextView deviceName = (TextView) rowView.findViewById(R.id.bluetoothEntry);

        deviceName.setText(devices.get(position).getName());
        return rowView;
    }
}
