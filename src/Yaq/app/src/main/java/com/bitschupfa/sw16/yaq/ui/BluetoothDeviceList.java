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


public class BluetoothDeviceList extends ArrayAdapter<BluetoothDevice> {

    private final Activity activity;

    private final List<BluetoothDevice> devices;

    private final TextView listText;

    public BluetoothDeviceList(Activity activity, List<BluetoothDevice> devices, TextView listText) {
        super(activity, R.layout.list_bluetooth_device, devices);
        this.activity = activity;
        this.devices = devices;
        this.listText = listText;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.list_bluetooth_device, parent, false);
        }

        if ((listText.getVisibility() == View.VISIBLE) && !devices.isEmpty()) {
            listText.setVisibility(View.INVISIBLE);
        } else if ((listText.getVisibility() == View.INVISIBLE) && devices.isEmpty()) {
            listText.setVisibility(View.VISIBLE);
        }

        String item = devices.get(position).getName();
        if (item != null) {
            TextView deviceName = (TextView) view.findViewById(R.id.bluetoothEntry);

            if (deviceName != null) {
                deviceName.setText(item);
            }
        }

        return view;
    }
}
