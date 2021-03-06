package com.bitschupfa.sw16.yaq.ui;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.test.ActivityInstrumentationTestCase2;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.BuildQuiz;
import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.StatisticsAtHost;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.utils.Quiz;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;
import com.bitschupfa.sw16.yaq.utils.SoloWrapper;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;


public class HostTests extends ActivityInstrumentationTestCase2<Host> {
    private static final String CORRECT_TEXT = "correct";
    private static final String WRONG1_TEXT = "wrong1";
    private static final String WRONG2_TEXT = "wrong2";
    private static final String WRONG3_TEXT = "wrong3";

    private Realm realm;
    private SoloWrapper solo;

    public HostTests() {
        super(Host.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new SoloWrapper(getInstrumentation(), getActivity());

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new Exception("Bluetooth is not supported.. Please don't use an emulator!");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                throw new Exception("Bluetooth is not enabled..");
            }
        }

        realm = Realm.getDefaultInstance();
    }

    @Override
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
        realm.close();
        super.tearDown();
    }

    public void testStartGameButtonNoQuestions() {
        QuizFactory.instance().clearQuiz();
        HostGameLogic.getInstance().setQuiz(new Quiz(new ArrayList<TextQuestion>()));
        solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));
        solo.sleep(500);
        assertTrue("Wrong Activity!", solo.waitForActivity(Host.class));
    }

    public void testStartGameButtonWithQuestions() {
        initHostForGame(true);
        assertTrue("Wrong Activity!", solo.waitForActivity(GameAtHost.class));
    }

    public void testBuildQuizButton() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.build));
        assertTrue("Wrong Activity!", solo.waitForActivity(BuildQuiz.class));
        solo.goBack();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void testSinglePlayerCorrectAnswer() throws Exception {
        initHostForGame(true);

        solo.clickOnText(CORRECT_TEXT);
        solo.sleep(100);

        assertTrue(solo.getButtonWrapper(CORRECT_TEXT).wasClicked());
        assertFalse(solo.getButtonWrapper(WRONG1_TEXT).wasClicked());
        assertFalse(solo.getButtonWrapper(WRONG2_TEXT).wasClicked());
        assertFalse(solo.getButtonWrapper(WRONG3_TEXT).wasClicked());
        solo.sleep(100);

        checkStatistics();
    }

    public void testSinglePlayerWrongAnswer() throws Exception {
        initHostForGame(true);
        solo.clickOnText(WRONG1_TEXT);
        solo.sleep(100);

        assertTrue(solo.getButtonWrapper(WRONG1_TEXT).wasClicked());
        assertFalse(solo.getButtonWrapper(CORRECT_TEXT).wasClicked());
        assertFalse(solo.getButtonWrapper(WRONG2_TEXT).wasClicked());
        assertFalse(solo.getButtonWrapper(WRONG3_TEXT).wasClicked());
        solo.sleep(100);

        checkStatistics();
    }

    private void initHostForGame(boolean startGame) {
        if (startGame) {
            solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));
        }

        initHostGameLogic();
        assertTrue("Wrong Activity!", solo.waitForActivity(GameAtHost.class));

        solo.sleep(500);
        solo.addButton(solo.getText("correct"));
        solo.addButton(solo.getText("wrong1"));
        solo.addButton(solo.getText("wrong2"));
        solo.addButton(solo.getText("wrong3"));
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

    private void checkStatistics() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.next_question));
        assertTrue("Wrong Activity!", solo.waitForActivity(StatisticsAtHost.class));
        solo.goBack();
    }
}
