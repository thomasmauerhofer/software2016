package com.bitschupfa.sw16.yaq.ui;

import android.bluetooth.BluetoothAdapter;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.activities.GameAtHost;
import com.bitschupfa.sw16.yaq.activities.Host;
import com.bitschupfa.sw16.yaq.activities.StatisticsAtHost;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.utils.Quiz;
import com.robotium.solo.Solo;

import java.util.ArrayList;
import java.util.List;


public class HostTests extends ActivityInstrumentationTestCase2<Host> {

    private Solo solo;
    private Button correctAnswer;
    private Button wrongAnswer1;
    private Button wrongAnswer2;
    private Button wrongAnswer3;
    private Drawable.ConstantState wrongConstant1;
    private Drawable.ConstantState wrongConstant2;
    private Drawable.ConstantState wrongConstant3;
    private Drawable.ConstantState correctConstant;

    public HostTests() {
        super(Host.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new Exception("Bluetooth is not supported.. Please don't use an emulator!");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                throw new Exception("Bluetooth is not enabled..");
            }
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testStartGameButton() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));
        assertTrue("Wrong Activity!", solo.waitForActivity(GameAtHost.class));
    }

    public void testSinglePlayerCorrectAnswer() {
        initHostForGame();

        solo.clickOnButton(correctAnswer.getText().toString());
        solo.sleep(100);

        Drawable greenBackground = correctAnswer.getBackground();

        assertTrue(!correctConstant.equals(greenBackground.getConstantState()));
        assertTrue(wrongConstant1.equals(wrongAnswer1.getBackground().getConstantState()));
        assertTrue(wrongConstant2.equals(wrongAnswer2.getBackground().getConstantState()));
        assertTrue(wrongConstant3.equals(wrongAnswer3.getBackground().getConstantState()));

        checkStatistics();
    }

    public void testSinglePlayerWrongAnswer() {
        initHostForGame();

        solo.clickOnButton(wrongAnswer1.getText().toString());
        solo.sleep(100);

        Drawable.ConstantState greenBackground = correctAnswer.getBackground().getConstantState();
        Drawable.ConstantState redBackground = wrongAnswer1.getBackground().getConstantState();

        assertTrue(!correctConstant.equals(greenBackground));
        assertTrue(!wrongConstant2.equals(redBackground));
        assertTrue(wrongConstant2.equals(wrongAnswer2.getBackground().getConstantState()));
        assertTrue(wrongConstant3.equals(wrongAnswer3.getBackground().getConstantState()));

        checkStatistics();
    }

    private void initHostForGame() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.start_game));

        initHostGameLogic();
        assertTrue("Wrong Activity!", solo.waitForActivity(GameAtHost.class));

        solo.sleep(500);
        correctAnswer = solo.getButton("correct");
        wrongAnswer1 = solo.getButton("wrong1");
        wrongAnswer2 = solo.getButton("wrong2");
        wrongAnswer3 = solo.getButton("wrong3");

        wrongConstant1 = wrongAnswer1.getBackground().getConstantState();
        wrongConstant2 = wrongAnswer2.getBackground().getConstantState();
        wrongConstant3 = wrongAnswer3.getBackground().getConstantState();
        correctConstant = correctAnswer.getBackground().getConstantState();
    }

    private void initHostGameLogic() {
        Quiz quiz = new Quiz();
        Answer answer1 = new Answer("correct", 10);
        Answer answer2 = new Answer("wrong1", 0);
        Answer answer3 = new Answer("wrong2", 0);
        Answer answer4 = new Answer("wrong3", 0);
        List<TextQuestion> questions = new ArrayList<>();
        questions.add(new TextQuestion(42, "Question1", answer1, answer2, answer3, answer4, 1, 1));
        quiz.addQuestions(questions);
        HostGameLogic.getInstance().setQuiz(quiz);
    }

    private void checkStatistics() {
        solo.clickOnButton(getActivity().getResources().getString(R.string.next_question));
        assertTrue("Wrong Activity!", solo.waitForActivity(StatisticsAtHost.class));
    }
}
