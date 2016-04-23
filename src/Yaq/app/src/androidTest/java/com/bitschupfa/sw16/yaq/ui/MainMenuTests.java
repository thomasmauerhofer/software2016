package com.bitschupfa.sw16.yaq.ui;


import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.MainMenue;
import com.bitschupfa.sw16.yaq.R;
import com.robotium.solo.Solo;


public class MainMenuTests extends ActivityInstrumentationTestCase2<MainMenue> {

    private Solo solo;

    public MainMenuTests() {
        super(MainMenue.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testMainMenuHost() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.host));
        assertTrue("Wrong Activity!", solo.waitForActivity(Host.class));
    }

    public void testMainMenuJoin() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.join));
        assertTrue("Wrong Activity!", solo.waitForActivity(Join.class));
    }
}
