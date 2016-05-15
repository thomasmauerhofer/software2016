package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.StatisticsAtHost;
import com.robotium.solo.Solo;

public class StatisticAtHostTest extends ActivityInstrumentationTestCase2<StatisticsAtHost> {

    private Solo solo;

    public StatisticAtHostTest() {
        super(StatisticsAtHost.class);
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

    public void testPlayAgainButton() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.play_again));
        assertTrue("Wrong Activity!", solo.waitForActivity(Host.class));
    }

    public void testStatisticWithoutScoreList() {
        assertTrue(solo.searchText(getActivity().getResources().getString(R.string.error_cant_show_score)));
    }
}
