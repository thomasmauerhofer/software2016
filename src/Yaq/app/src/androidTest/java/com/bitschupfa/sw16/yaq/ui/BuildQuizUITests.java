package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.BuildQuiz;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.robotium.solo.Solo;

/**
 * Created by manu on 09.05.16.
 */
public class BuildQuizUITests extends ActivityInstrumentationTestCase2<BuildQuiz> {
    private Solo solo;

    public BuildQuizUITests() {
        super(BuildQuiz.class);
    }

    @Override
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), getActivity());
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }

    public void testBuildQuizFinished() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.submit_quiz));
        solo.sleep(500);
        assertTrue(solo.getCurrentActivity().isFinishing());
    }
}
