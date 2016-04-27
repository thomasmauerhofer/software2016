package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.activities.QuestionsAsked;
import com.bitschupfa.sw16.yaq.activities.Statistic;
import com.bitschupfa.sw16.yaq.R;
import com.robotium.solo.Solo;

public class StatisticTests extends ActivityInstrumentationTestCase2<Statistic> {

    private Solo solo;

    public StatisticTests() {
        super(Statistic.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testStatistic() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.play_again));
        assertTrue("Wrong Activity!", solo.waitForActivity(QuestionsAsked.class));
    }
}
