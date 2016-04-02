package com.bitschupfa.sw16.yaq.Activities;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.Question;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionsAsked extends AppCompatActivity {

    private ProgressBar countdownTimerBar;
    private TextView countdownTimerText;
    private TextView questionView;
    private Button answer1Button;
    private Button answer2Button;
    private Button answer3Button;
    private Button answer4Button;

    private ArrayList<Question> dummyQuestions;
    private Question question;

    private ArrayList<String> q;
    private ArrayList<String> cA;
    private ArrayList<ArrayList<String>> wA;
    private ArrayList<String> dummyAA;
    private ArrayList<String> dummyAA1;
    private ArrayList<String> dummyAA2;
    private ArrayList<String> dummyAA3;

    private LinearLayout buttonLayout;
    private RelativeLayout askedView;

    private Button buttonPressed;

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

        q = new ArrayList<String>();
        cA = new ArrayList<String>();
        wA = new ArrayList<ArrayList<String>>();

        dummyAA = new ArrayList<String>();
        dummyAA.add("1992");
        dummyAA.add("1991");
        dummyAA.add("1990");
        dummyAA.add("1989");

        dummyAA1 = new ArrayList<String>();
        dummyAA1.add("Deutschland");
        dummyAA1.add("Spanien");
        dummyAA1.add("Brasilien");
        dummyAA1.add("Italien");

        dummyAA2 = new ArrayList<String>();
        dummyAA2.add("Hunden");
        dummyAA2.add("Spinnen");
        dummyAA2.add("Schlangen");
        dummyAA2.add("Katzen");

        dummyAA3 = new ArrayList<String>();
        dummyAA3.add("Europa");
        dummyAA3.add("Afrika");
        dummyAA3.add("Australien");
        dummyAA3.add("Asien");

        question = new Question();
        dummyQuestions = new ArrayList<Question>();

        endTime = 10000;

        loadQuestions();
        showQuestions();

        askedView.setEnabled(false);

        askedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dummyQuestions.size() > 1) {
                    dummyQuestions.remove(0);
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

    public void startCountdown(final long endTime) {

        new CountDownTimer(endTime, 1000) {
            public void onTick(long millisUntilFinished) {
                countdownTimerText.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                checkAnswer();
                countdownTimerText.setText("Time's up!");
            }
        }.start();

        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(countdownTimerBar, "progress", 100, 0);
        progressAnimator.setDuration(endTime);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    public void loadQuestions() {
        fillQuestions("Wann wurde die Mauer in Berlin niedergerissen?", "1989", dummyAA);
        fillQuestions("Wer wurde 2006 Fussball Weltmeister?", "Italien", dummyAA1);
        fillQuestions("Vor welchem Tieren fürchtete sich Napoleon?", "Katzen", dummyAA2);
        fillQuestions("Welcher Kontinent ist der Größte?", "Asien", dummyAA3);

        for (int counter = 0; counter < q.size(); counter++) {
            question = new Question();
            question.setQuestion(q.get(counter));
            question.setCorrectAnswer(cA.get(counter));
            question.setAllAnswers(wA.get(counter));
            dummyQuestions.add(question);
        }
    }

    public void showQuestions() {
        Collections.shuffle(dummyQuestions);

        questionView.setText(dummyQuestions.get(0).getQuestion());
        answer1Button.setText(dummyQuestions.get(0).getAllAnswers().get(0));
        answer2Button.setText(dummyQuestions.get(0).getAllAnswers().get(1));
        answer3Button.setText(dummyQuestions.get(0).getAllAnswers().get(2));
        answer4Button.setText(dummyQuestions.get(0).getAllAnswers().get(3));
    }

    // only for dummy questions
    public void fillQuestions(String questionText, String correctAnswer, ArrayList<String> allAnswers) {
        q.add(questionText);
        cA.add(correctAnswer);
        Collections.shuffle(allAnswers);
        wA.add(allAnswers);
    }

    public void resetButtons() {
        answer1Button.setBackground(getResources().getDrawable(R.drawable.button_blue));
        answer2Button.setBackground(getResources().getDrawable(R.drawable.button_blue));
        answer3Button.setBackground(getResources().getDrawable(R.drawable.button_blue));
        answer4Button.setBackground(getResources().getDrawable(R.drawable.button_blue));

        askedView.setEnabled(false);

        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View child = buttonLayout.getChildAt(i);
            child.setClickable(true);
        }
    }

    public void answerButtonClicked(View view) {
        buttonPressed = (Button) view;
        deactivateButtons();
        buttonPressed.setBackgroundColor(Color.GRAY);
    }

    public void checkAnswer() {
        if (buttonPressed != null) {
            String buttonText = buttonPressed.getText().toString();

            if (!(buttonText.equals(dummyQuestions.get(0).getCorrectAnswer()))) {
                buttonPressed.setBackgroundColor(Color.RED);
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
    }

    public void markRightButton() {
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            buttonPressed = (Button) buttonLayout.getChildAt(i);
            String buttonText = buttonPressed.getText().toString();

            if (buttonText.equals(dummyQuestions.get(0).getCorrectAnswer())) {
                buttonPressed.setBackgroundColor(Color.GREEN);
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
