package com.bitschupfa.sw16.yaq.ui;


import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.Join;
import com.bitschupfa.sw16.yaq.activities.MainMenu;
import com.bitschupfa.sw16.yaq.activities.QuizMaker;
import com.robotium.solo.Solo;


public class QuizMakerTest extends ActivityInstrumentationTestCase2<QuizMaker> {

    private Solo solo;

    public QuizMakerTest() {
        super(QuizMaker.class);
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

    public void testFileUpload() {
        // No functionality implemented yet
        // Only GUI available
    }
}
