package com.bitschupfa.sw16.yaq.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

import com.bitschupfa.sw16.yaq.activities.QuestionsAsked;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.utils.Quiz;
import com.robotium.solo.Solo;

public class QuestionsAskedTests extends ActivityInstrumentationTestCase2<QuestionsAsked> {

    private Solo solo;

    private Quiz quiz;

    private Button correctAnswer;

    private Button wrongAnswer1;

    private Button wrongAnswer2;

    private Button wrongAnswer3;

    public QuestionsAskedTests() {
        super(QuestionsAsked.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        solo = new Solo(getInstrumentation(), getActivity());

        correctAnswer = solo.getButton("correct");
        wrongAnswer1 = solo.getButton("wrong1");
        wrongAnswer2 = solo.getButton("wrong2");
        wrongAnswer3 = solo.getButton("wrong3");
    }


    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    public QuestionsAsked getActivity() {
        quiz = new Quiz();
        Answer answer1 = new Answer("correct", 10);
        Answer answer2 = new Answer("wrong1", 10);
        Answer answer3 = new Answer("wrong2", 10);
        Answer answer4 = new Answer("wrong3", 10);
        quiz.addQuestion(new TextQuestion(1, "Question1", answer1, answer2, answer3, answer4, 1, 1));

        Intent intent = new Intent();
        intent.putExtra("questions", quiz);
        setActivityIntent(intent);

        return super.getActivity();
    }

    public void testCorrectAnswer() {
        Drawable.ConstantState WrongConstant1 = wrongAnswer1.getBackground().getConstantState();
        Drawable.ConstantState WrongConstant2 = wrongAnswer2.getBackground().getConstantState();
        Drawable.ConstantState WrongConstant3 = wrongAnswer3.getBackground().getConstantState();
        Drawable.ConstantState correctConstant = correctAnswer.getBackground().getConstantState();

        solo.clickOnButton(correctAnswer.getText().toString());
        solo.sleep(1000);
        Drawable greyBackground = correctAnswer.getBackground();
        assertTrue(!correctConstant.equals(greyBackground.getConstantState()) &&
                WrongConstant1.equals(wrongAnswer1.getBackground().getConstantState()) &&
                WrongConstant2.equals(wrongAnswer2.getBackground().getConstantState()) &&
                WrongConstant3.equals(wrongAnswer3.getBackground().getConstantState()));

        solo.waitForText(getActivity().getString(R.string.time_up));
        Drawable.ConstantState greenBackground = correctAnswer.getBackground().getConstantState();
        assertTrue(!greyBackground.getConstantState().equals(greenBackground) &&
                WrongConstant1.equals(wrongAnswer1.getBackground().getConstantState()) &&
                WrongConstant2.equals(wrongAnswer2.getBackground().getConstantState()) &&
                WrongConstant3.equals(wrongAnswer3.getBackground().getConstantState()));
    }

    public void testWrongAnswer() {
        Drawable.ConstantState wrongConstant1 = wrongAnswer1.getBackground().getConstantState();
        Drawable.ConstantState wrongConstant2 = wrongAnswer2.getBackground().getConstantState();
        Drawable.ConstantState wrongConstant3 = wrongAnswer3.getBackground().getConstantState();
        Drawable.ConstantState correctConstant = correctAnswer.getBackground().getConstantState();

        solo.clickOnButton(wrongAnswer1.getText().toString());
        solo.sleep(1000);
        Drawable.ConstantState greyBackground = wrongAnswer1.getBackground().getConstantState();
        assertTrue(correctConstant.equals(correctAnswer.getBackground().getConstantState()) &&
                !wrongConstant1.equals(greyBackground) &&
                wrongConstant2.equals(wrongAnswer2.getBackground().getConstantState()) &&
                wrongConstant3.equals(wrongAnswer3.getBackground().getConstantState()));

        solo.waitForText(getActivity().getString(R.string.time_up));
        Drawable.ConstantState greenBackground = correctAnswer.getBackground().getConstantState();
        Drawable.ConstantState redBackground = wrongAnswer1.getBackground().getConstantState();
        assertTrue(!correctConstant.equals(greenBackground) &&
                !greyBackground.equals(redBackground) &&
                wrongConstant2.equals(wrongAnswer2.getBackground().getConstantState()) &&
                wrongConstant3.equals(wrongAnswer3.getBackground().getConstantState()));
    }
}
