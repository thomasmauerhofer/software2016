package com.bitschupfa.sw16.yaq.ui;

import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.StatisticsAtHost;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;
import com.bitschupfa.sw16.yaq.utils.SoloWrapper;
import com.robotium.solo.Solo;

import io.realm.RealmList;

public class StatisticAtHostTest extends ActivityInstrumentationTestCase2<Host> {
    private static final String CORRECT_TEXT = "correct";
    private static final String WRONG1_TEXT = "wrong1";
    private static final String WRONG2_TEXT = "wrong2";
    private static final String WRONG3_TEXT = "wrong3";

    private SoloWrapper solo;

    public StatisticAtHostTest() {
        super(Host.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new SoloWrapper(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        super.tearDown();
    }

    public void testPlayAgainButton() {
        initHostForGame(true);
        solo.clickOnButton(getActivity().getResources().getString(R.string.play_again));
        assertTrue("Wrong Activity!", solo.waitForActivity(Host.class));
    }

    private void initHostForGame(boolean startGame) {
        if (startGame) {
            solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));
        }

        initHostGameLogic();
        assertTrue("Wrong Activity!", solo.waitForActivity(GameAtHost.class));

        solo.sleep(500);
        solo.addButton(solo.getText(CORRECT_TEXT));
        solo.addButton(solo.getText(WRONG1_TEXT));
        solo.addButton(solo.getText(WRONG1_TEXT));
        solo.addButton(solo.getText(WRONG1_TEXT));
        solo.sleep(500);

        solo.clickOnText(CORRECT_TEXT);
        solo.clickOnButton(getActivity().getResources().getString(R.string.next_question));
        assertTrue("Wrong Activity!", solo.waitForActivity(StatisticsAtHost.class));
    }

    private void initHostGameLogic() {
        Answer answer1 = new Answer(CORRECT_TEXT, 10);
        Answer answer2 = new Answer(WRONG1_TEXT, 0);
        Answer answer3 = new Answer(WRONG2_TEXT, 0);
        Answer answer4 = new Answer(WRONG3_TEXT, 0);
        RealmList<TextQuestion> questions = new RealmList<>();
        TextQuestion question = new TextQuestion(42, "Question1", answer1, answer2, answer3, answer4, 1);
        questions.add(question);
        QuestionCatalog catalog = new QuestionCatalog(99, 1, "test", questions);

        QuizFactory.instance().clearQuiz();
        QuizFactory.instance().addQuestions(catalog.getName(), questions);
        HostGameLogic.getInstance().setQuiz(QuizFactory.instance().createNewQuiz());
    }
}
