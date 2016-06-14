package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

public class EditQuestions extends YaqActivity {

    EditText textQuestions;
    EditText textAnswer1;
    EditText textAnswer2;
    EditText textAnswer3;
    EditText textAnswer4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleTheme();

        displayQuestion();
    }

    public void displayQuestion() {

        TextQuestion textQuestion = (TextQuestion) getIntent().getSerializableExtra("Question");

        textQuestions = (EditText) findViewById(R.id.question);
        textAnswer1 = (EditText) findViewById(R.id.answer1);
        textAnswer2 = (EditText) findViewById(R.id.answer2);
        textAnswer3 = (EditText) findViewById(R.id.answer3);
        textAnswer4 = (EditText) findViewById(R.id.answer4);

        textQuestions.setText(textQuestion.getQuestion());
        textAnswer1.setText(textQuestion.getAnswers().get(0).getAnswerString());
        textAnswer2.setText(textQuestion.getAnswers().get(1).getAnswerString());
        textAnswer3.setText(textQuestion.getAnswers().get(2).getAnswerString());
        textAnswer4.setText(textQuestion.getAnswers().get(3).getAnswerString());
    }

    @SuppressWarnings("UnusedParameters")
    public void submitEditButtonClick(View view) {
        finish();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}

