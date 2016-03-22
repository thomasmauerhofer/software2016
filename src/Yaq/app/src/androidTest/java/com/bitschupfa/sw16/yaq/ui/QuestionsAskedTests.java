package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.Activities.QuesionsAsked;
import com.bitschupfa.sw16.yaq.Activities.Statistic;
import com.robotium.solo.Solo;

/**
 * Created by thomas on 22.03.16.
 */
public class QuestionsAskedTests extends ActivityInstrumentationTestCase2<QuesionsAsked> {

    private Solo solo;

    public QuestionsAskedTests() {
        super(QuesionsAsked.class);
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
        solo.clickOnButton("Answer 1");
        assertTrue("Wrong Activity!", solo.waitForActivity(Statistic.class));
    }
}
