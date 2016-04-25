package com.bitschupfa.sw16.yaq.activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.utils.Quiz;

import java.util.List;

public class QuestionsAsked extends AppCompatActivity {

    private ProgressBar countdownTimerBar;
    private TextView countdownTimerText;
    private TextView questionView;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;

    private LinearLayout buttonLayout;
    private RelativeLayout askedView;

    private Button buttonPressed;

    private Quiz quiz;
    private long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_asked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        countdownTimerBar = (ProgressBar) findViewById(R.id.barTimer);
        countdownTimerText = (TextView) findViewById(R.id.time);
        questionView = (TextView) findViewById(R.id.question);
        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        answer4Button = (Button) findViewById(R.id.answer4);

        buttonLayout = (LinearLayout) findViewById(R.id.linearLayout);
        askedView = (RelativeLayout) findViewById(R.id.questionAskedView);

        endTime = 10000;

        if(getIntent().hasExtra("questions")) {
            quiz = (Quiz) getIntent().getExtras().get("questions");
        } else {
            quiz = new Quiz();
            quiz.createTmpQuiz(this);
        }

        showQuestions();

        askedView.setEnabled(false);

        askedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quiz.getCurrentQuestionCounter() < quiz.getQuestions().size()) {
                    resetButtons();
                    showQuestions();
                    startCountdown(endTime);
                } else {
                    Intent intent = new Intent(QuestionsAsked.this, Statistic.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        startCountdown(endTime);
    }

    private void startCountdown(final long endTime) {

        new CountDownTimer(endTime, 1000) {
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                countdownTimerText.setText(String.valueOf(time));
            }

            public void onFinish() {
                checkAnswer();
                countdownTimerText.setText(getString(R.string.time_up));
            }
        }.start();

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(countdownTimerBar, "progress", 100, 0);
        progressAnimator.setDuration(endTime);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    private void showQuestions() {
        TextQuestion question = quiz.getCurrentQuestion();
        List<Answer> answers = question.getShuffledAnswers();

        questionView.setText(question.getQuestion());
        answer1Button.setText(answers.get(0).getAnswerString());
        answer2Button.setText(answers.get(1).getAnswerString());
        answer3Button.setText(answers.get(2).getAnswerString());
        answer4Button.setText(answers.get(3).getAnswerString());
    }



    private void resetButtons() {
        answer1Button.setBackgroundResource(R.drawable.button_blue);
        answer2Button.setBackgroundResource(R.drawable.button_blue);
        answer3Button.setBackgroundResource(R.drawable.button_blue);
        answer4Button.setBackgroundResource(R.drawable.button_blue);

        askedView.setEnabled(false);

        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View child = buttonLayout.getChildAt(i);
            child.setClickable(true);
        }
    }

    public void answerButtonClicked(View view) {
        buttonPressed = (Button) view;
        deactivateButtons();
        buttonPressed.setBackgroundResource(R.drawable.button_grey);
    }

    private void checkAnswer() {
        TextQuestion question = quiz.getCurrentQuestion();

        if (buttonPressed != null) {
            String buttonText = buttonPressed.getText().toString();

            if (!(buttonText.equals(question.getAnswers().get(0).getAnswerString()))) {
                buttonPressed.setBackgroundResource(R.drawable.button_red);
                markRightButton();
            } else {
                markRightButton();
            }
        } else {
            markRightButton();
        }
        deactivateButtons();
        askedView.setEnabled(true);
        buttonPressed = null;
        quiz.incrementCurrentQuestionCounter();
    }

    private void markRightButton() {
        TextQuestion question = quiz.getCurrentQuestion();

        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            buttonPressed = (Button) buttonLayout.getChildAt(i);
            String buttonText = buttonPressed.getText().toString();

            if (buttonText.equals(question.getAnswers().get(0).getAnswerString())) {
                buttonPressed.setBackgroundResource(R.drawable.button_green);
            }
        }
    }

    private void deactivateButtons() {
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View child = buttonLayout.getChildAt(i);
            child.setClickable(false);
        }
    }
}
