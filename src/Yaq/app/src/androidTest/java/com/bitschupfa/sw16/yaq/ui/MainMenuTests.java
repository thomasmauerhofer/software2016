package com.bitschupfa.sw16.yaq.ui;


import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.MainMenu;
import com.robotium.solo.Solo;


public class MainMenuTests extends ActivityInstrumentationTestCase2<MainMenu> {

    private Solo solo;

    public MainMenuTests() {
        super(MainMenu.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
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
