package com.bitschupfa.sw16.yaq.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.Cast.CastHelper;
import com.bitschupfa.sw16.yaq.Database.Object.Answer;
import com.bitschupfa.sw16.yaq.Database.Object.TextQuestion;
import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.Quiz;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class QuestionsAsked extends AppCompatActivity {
    private final static String TAG = QuestionsAsked.class.getCanonicalName();

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

    Quiz quiz;
    private long endTime;

    private CastHelper castHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_asked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        castHelper = CastHelper.getInstance(getApplicationContext());

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menue, menu);
        menu.findItem(R.id.menu_manage).setVisible(false);
        menu.findItem(R.id.menu_settings).setVisible(false);
        menu.findItem(R.id.menu_profile).setVisible(false);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(castHelper.mMediaRouteSelector);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        castHelper.addCallbacks();
    }

    public void startCountdown(final long endTime) {

        new CountDownTimer(endTime, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownTimerText.setText("" + millisUntilFinished / 1000);
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

    public void showQuestions() {
        TextQuestion question = quiz.getCurrentQuestion();
        List<Answer> answers = question.getShuffeledAnswers();

        questionView.setText(question.getQuestion());
        answer1Button.setText(answers.get(0).getAnswerString());
        answer2Button.setText(answers.get(1).getAnswerString());
        answer3Button.setText(answers.get(2).getAnswerString());
        answer4Button.setText(answers.get(3).getAnswerString());

        JSONObject json = new JSONObject();
        try {
            json.put("type", "question");
            json.put("question", question.getQuestion());
            json.put("answer_1", answers.get(0).getAnswerString());
            json.put("answer_2", answers.get(1).getAnswerString());
            json.put("answer_3", answers.get(2).getAnswerString());
            json.put("answer_4", answers.get(3).getAnswerString());
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        castHelper.sendMessage(json.toString());
    }



    public void resetButtons() {
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

    public void checkAnswer() {
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
        castHelper.sendMessage("Correct answer: " + question.getAnswers().get(0).getAnswerString());
    }

    public void markRightButton() {
        TextQuestion question = quiz.getCurrentQuestion();

        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            buttonPressed = (Button) buttonLayout.getChildAt(i);
            String buttonText = buttonPressed.getText().toString();

            if (buttonText.equals(question.getAnswers().get(0).getAnswerString())) {
                buttonPressed.setBackgroundResource(R.drawable.button_green);
            }
        }
    }

    public void deactivateButtons() {
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View child = buttonLayout.getChildAt(i);
            child.setClickable(false);
        }
    }
}
