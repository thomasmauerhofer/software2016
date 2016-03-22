package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.Activities.Host;
import com.bitschupfa.sw16.yaq.Activities.Join;
import com.bitschupfa.sw16.yaq.Activities.MainMenue;
import com.bitschupfa.sw16.yaq.Activities.QuesionsAsked;
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
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testHost() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));
        assertTrue("Wrong Activity!", solo.waitForActivity(QuesionsAsked.class));
    }
}
