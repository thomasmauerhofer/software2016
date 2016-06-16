package com.bitschupfa.sw16.yaq.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.Join;
import com.robotium.solo.Solo;

import java.util.Set;


public class JoinTests extends ActivityInstrumentationTestCase2<Join> {

    private Solo solo;
    private BluetoothAdapter mBluetoothAdapter;

    public JoinTests() {
        super(Join.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new Exception("Bluetooth is not supported.. Please don't use an emulator!");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                throw new Exception("Bluetooth is not enabled..");
            }
        }
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testDialog() {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();

        if(devices.isEmpty()) {
            assertTrue("Text not shown in Dialog",
                    solo.searchText(getActivity().getString(R.string.dialog_no_devices_found)));
        } else {
            assertTrue("Paired devices not shown in list!",
                    solo.searchText(devices.iterator().next().getName()));
        }
        assertTrue("Wrong Activity!", solo.waitForActivity(Join.class));
    }
}