package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.BuildQuiz;
import com.robotium.solo.Solo;

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
        super.tearDown();
    }

    public void testBuildQuizFinished() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.submit_quiz));
        assertTrue("Wrong Activity!", solo.waitForActivity(BuildQuiz.class));
    }

    public void testNumberOfQuestions() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.plus));
        solo.clickOnButton(getActivity().getResources().getString(R.string.plus));
        solo.clickOnButton(getActivity().getResources().getString(R.string.plus));
        solo.clickOnButton(getActivity().getResources().getString(R.string.minus));

        solo.sleep(100);

        TextView textView = (TextView) solo.getView(R.id.numberPicker);

        String text = textView.getText().toString();
        assertNotNull(text);

        assertEquals("12", text);
    }
}
