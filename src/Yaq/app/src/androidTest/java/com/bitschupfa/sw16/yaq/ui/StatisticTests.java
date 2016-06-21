package com.bitschupfa.sw16.yaq.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.Statistic;
import com.robotium.solo.Solo;

import java.util.ArrayList;

public class StatisticTests extends ActivityInstrumentationTestCase2<Statistic> {

    private Solo solo;

    public StatisticTests() {
        super(Statistic.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    @Override
    public Statistic getActivity() {
        ArrayList<RankingItem> scores = new ArrayList<>();
        scores.add(new RankingItem("Tom", 1000));
        scores.add(new RankingItem("Matthias", 100));

        Intent intent = new Intent();
        intent.putExtra("scoreList", scores);
        setActivityIntent(intent);

        return super.getActivity();
    }

    public void testStatisticWithScoreList() {
        assertTrue(solo.searchText("Tom"));
        assertTrue(solo.searchText("Matthias"));
    }
}
