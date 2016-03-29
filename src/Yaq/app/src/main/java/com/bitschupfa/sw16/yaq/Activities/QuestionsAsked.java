package com.bitschupfa.sw16.yaq.Activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.Question;

import java.util.ArrayList;
import java.util.Collections;

public class QuestionsAsked extends AppCompatActivity {

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
    private ArrayList<String> dummyWA;

    private LinearLayout linearLayout;
    private RelativeLayout askedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_asked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        questionView = (TextView) findViewById(R.id.question);
        answer1Button = (Button) findViewById(R.id.answer1);
        answer2Button = (Button) findViewById(R.id.answer2);
        answer3Button = (Button) findViewById(R.id.answer3);
        answer4Button = (Button) findViewById(R.id.answer4);

        q = new ArrayList<String>();
        cA = new ArrayList<String>();
        wA = new ArrayList<ArrayList<String>>();
        dummyWA = new ArrayList<String>();

        dummyWA.add("Antwort 1");
        dummyWA.add("Antwort 2");
        dummyWA.add("Antwort 3");

        question = new Question();
        dummyQuestions = new ArrayList<Question>();

        createQuestions();
        showQuestions();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        askedView = (RelativeLayout) findViewById(R.id.questionAskedView);

        askedView.setClickable(false);
        askedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dummyQuestions.size() > 1) {
                    dummyQuestions.remove(0);
                    resetButtons();
                    showQuestions();
                } else {
                    Intent intent = new Intent(QuestionsAsked.this, Statistic.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void createQuestions()
    {
        fillQuestions("Wann wurde die Mauer in Berlin niedergerissen?", "1989", dummyWA);
        fillQuestions("Wer wurde 2006 Fussball Weltmeister?", "Italien", dummyWA);
        fillQuestions("Vor welchem Tieren fürchtete sich Napoleon?", "Katzen", dummyWA);
        fillQuestions("Welcher Kontinent ist der Größte?", "Asien", dummyWA);

        for(int counter = 0; counter < q.size(); counter++) {
            question = new Question();
            question.setQuestion(q.get(counter));
            question.setCorrectAnswer(cA.get(counter));
            question.setWrongAnswers(wA.get(counter));
            dummyQuestions.add(question);
            Log.d("blah","add: " + dummyQuestions.get(counter).getQuestion());
        }
    }

    public void showQuestions()
    {
        Collections.shuffle(dummyQuestions);

        for (int i = 0; i < dummyQuestions.size(); i++) {
            Log.d("blah","shuffle: " + dummyQuestions.get(i).getQuestion());
        }

        questionView.setText(dummyQuestions.get(0).getQuestion());
        answer1Button.setText(dummyQuestions.get(0).getWrongAnswers().get(0));
        answer2Button.setText(dummyQuestions.get(0).getWrongAnswers().get(1));
        answer3Button.setText(dummyQuestions.get(0).getWrongAnswers().get(2));
        answer4Button.setText(dummyQuestions.get(0).getCorrectAnswer());
    }

    public void fillQuestions(String questionText, String correctAnswer, ArrayList<String> falseAnswers)
    {
        q.add(questionText);
        cA.add(correctAnswer);
        wA.add(falseAnswers);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void resetButtons()
    {
        answer1Button.setBackground(getResources().getDrawable(R.drawable.button_blue));
        answer2Button.setBackground(getResources().getDrawable(R.drawable.button_blue));
        answer3Button.setBackground(getResources().getDrawable(R.drawable.button_blue));
        answer4Button.setBackground(getResources().getDrawable(R.drawable.button_blue));

        askedView.setClickable(false);

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setClickable(true);
        }
    }

    public void answerButtonClicked(View view) {
        Button button = (Button)view;
        String buttonText = button.getText().toString();

        if(buttonText.equals(dummyQuestions.get(0).getCorrectAnswer()))
        {
            button.setBackgroundColor(Color.GREEN);
        }
        else
        {
            button.setBackgroundColor(Color.RED);
        }

        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            View child = linearLayout.getChildAt(i);
            child.setClickable(false);
        }
        askedView.setClickable(true);
    }
}
