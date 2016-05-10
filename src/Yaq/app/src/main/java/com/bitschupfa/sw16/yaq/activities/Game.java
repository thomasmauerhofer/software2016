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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.game.ClientGameLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Game extends AppCompatActivity {
    private static final String TAG = "GameActivity";

    private ProgressBar countdownTimerBar;
    private TextView countdownTimerText;
    private TextView questionView;
    private List<Button> answerButtons;

    private Button answerButtonPressed;
    private Answer selectedAnswer;

    private Map<Button, Answer> answerMapping = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ClientGameLogic.getInstance().setGameActivity(this);

        countdownTimerBar = (ProgressBar) findViewById(R.id.barTimer);
        countdownTimerText = (TextView) findViewById(R.id.time);
        questionView = (TextView) findViewById(R.id.question);

        answerButtons = new ArrayList<>(
                Arrays.asList(
                        (Button) findViewById(R.id.answer1), (Button) findViewById(R.id.answer2),
                        (Button) findViewById(R.id.answer3), (Button) findViewById(R.id.answer4)
                )
        );
    }

    public void showStatisticActivity() {
        Intent intent = new Intent(Game.this, Statistic.class);
        startActivity(intent);
        finish();
    }

    public void showQuestion(final TextQuestion question, final int timeout) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAnswerButtonsClickable(true);
                for (Button answerButton : answerButtons) {
                    answerButton.setBackgroundResource(R.drawable.button_blue);
                }

                questionView.setText(question.getQuestion());

                List<Answer> answers = question.getShuffledAnswers();
                answerMapping.clear();
                for (int i = 0; i < 4; ++i) {
                    answerButtons.get(i).setText(answers.get(i).getAnswerString());
                    answerMapping.put(answerButtons.get(i), answers.get(i));
                }

                startCountdown(timeout);
            }
        });
    }

    public void showAnswer(final Answer correctAnswer) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (answerButtonPressed != null && selectedAnswer != null && !selectedAnswer.equals(correctAnswer)) {
                    answerButtonPressed.setBackgroundResource(R.drawable.button_red);
                }

                for (Map.Entry<Button, Answer> entry : answerMapping.entrySet()) {
                    if (entry.getValue().equals(correctAnswer)) {
                        entry.getKey().setBackgroundResource(R.drawable.button_green);
                        break;
                    }
                }

                answerButtonPressed = null;
                selectedAnswer = null;
            }
        });

    }

    public void answerButtonClicked(View view) {
        answerButtonPressed = (Button) view;
        answerButtonPressed.setBackgroundResource(R.drawable.button_grey);
        setAnswerButtonsClickable(false);

        selectedAnswer = answerMapping.get(answerButtonPressed);
        ClientGameLogic.getInstance().answerQuestion(selectedAnswer);

        // TODO: correctly handle the progress bar
    }

    private void startCountdown(final long timeout) {
        new CountDownTimer(timeout, 1000) {
            public void onTick(long millisUntilFinished) {
                long time = millisUntilFinished / 1000;
                countdownTimerText.setText(String.valueOf(time));
            }

            public void onFinish() {
                countdownTimerText.setText(getString(R.string.time_up));
                setAnswerButtonsClickable(false);
            }
        }.start();

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(countdownTimerBar, "progress", 100, 0);
        progressAnimator.setDuration(timeout);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    private void setAnswerButtonsClickable(boolean clickable) {
        for (Button answerButton : answerButtons) {
            answerButton.setClickable(clickable);
        }
    }
}
