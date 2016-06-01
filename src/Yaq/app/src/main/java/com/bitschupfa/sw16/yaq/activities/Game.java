package com.bitschupfa.sw16.yaq.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
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
import com.bitschupfa.sw16.yaq.ui.RankingItem;

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
    private CountDownTimer timer;

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

    public void showStatisticActivity(ArrayList<RankingItem> scoreList) {
        Intent intent = new Intent(Game.this, Statistic.class);
        intent.putExtra("scoreList", scoreList);
        startActivity(intent);
        finish();
    }

    public void showQuestion(final TextQuestion question, final int timeout) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setAnswerButtonsClickable(true);
                for (Button answerButton : answerButtons) {
                    answerButton.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary),
                            PorterDuff.Mode.MULTIPLY);
                }

                questionView.setText(question.getQuestion());

                List<Answer> answers = question.getShuffledAnswers();
                answerButtons.get(0).setText(answers.get(0).getAnswerString());
                answerButtons.get(1).setText(answers.get(1).getAnswerString());
                answerButtons.get(2).setText(answers.get(2).getAnswerString());
                answerButtons.get(3).setText(answers.get(3).getAnswerString());

                answerMapping.clear();
                answerMapping.put(answerButtons.get(0), answers.get(0));
                answerMapping.put(answerButtons.get(1), answers.get(1));
                answerMapping.put(answerButtons.get(2), answers.get(2));
                answerMapping.put(answerButtons.get(3), answers.get(3));

                startCountdown(timeout);
            }
        });
    }

    public void showAnswer(final Answer correctAnswer) {
        final Activity activity = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (answerButtonPressed != null && selectedAnswer != null && !selectedAnswer.equals(correctAnswer)) {
                    answerButtonPressed.getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.red500),
                            PorterDuff.Mode.MULTIPLY);
                }

                for (Map.Entry<Button, Answer> entry : answerMapping.entrySet()) {
                    if (entry.getValue().equals(correctAnswer)) {
                        entry.getKey().getBackground().setColorFilter(ContextCompat.getColor(activity, R.color.green500),
                                PorterDuff.Mode.MULTIPLY);
                        break;
                    }
                }

                answerButtonPressed = null;
                selectedAnswer = null;
            }
        });

    }

    public void answerButtonClicked(View view) {
        timer.cancel();
        answerButtonPressed = (Button) view;
        answerButtonPressed.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.grey500),
                PorterDuff.Mode.MULTIPLY);
        setAnswerButtonsClickable(false);

        selectedAnswer = answerMapping.get(answerButtonPressed);
        ClientGameLogic.getInstance().answerQuestion(selectedAnswer);

        countdownTimerBar.setVisibility(View.INVISIBLE);
        countdownTimerText.setVisibility(View.INVISIBLE);
    }

    private void startCountdown(final long timeout) {
        countdownTimerBar.setVisibility(View.VISIBLE);
        countdownTimerText.setVisibility(View.VISIBLE);

        timer = new CountDownTimer(timeout, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                countdownTimerText.setText(String.valueOf((countdownTimerBar.getProgress() + 9)/ 10));
            }

            @Override
            public void onFinish() {
                countdownTimerBar.setVisibility(View.INVISIBLE);
                countdownTimerText.setVisibility(View.INVISIBLE);
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
