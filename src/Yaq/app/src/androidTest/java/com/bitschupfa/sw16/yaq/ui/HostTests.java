package com.bitschupfa.sw16.yaq.ui;

import android.bluetooth.BluetoothAdapter;
import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.Activities.Host;
import com.bitschupfa.sw16.yaq.Activities.QuestionsAsked;
import com.bitschupfa.sw16.yaq.R;
import com.robotium.solo.Solo;

/**
 * Created by thomas on 22.03.16.
 */
public class HostTests extends ActivityInstrumentationTestCase2<Host> {

    private Solo solo;

    public HostTests() {
        super(Host.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
    }

    public void testHost() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));
        assertTrue("Wrong Activity!", solo.waitForActivity(QuestionsAsked.class));
    }
}
