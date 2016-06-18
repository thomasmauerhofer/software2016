package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import javax.xml.transform.TransformerFactoryConfigurationError;

public class EditQuestions extends YaqActivity {

    private EditText textQuestions;
    private EditText textAnswer1;
    private EditText textAnswer2;
    private EditText textAnswer3;
    private EditText textAnswer4;

    private TextQuestion textQuestion;
    private QuestionCatalog catalog;

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

        catalog = (QuestionCatalog) getIntent().getSerializableExtra("QuestionCatalog");
        textQuestion = (TextQuestion) getIntent().getSerializableExtra("Question");

        textQuestions = (EditText) findViewById(R.id.question);
        textAnswer1 = (EditText) findViewById(R.id.answer1);
        textAnswer2 = (EditText) findViewById(R.id.answer2);
        textAnswer3 = (EditText) findViewById(R.id.answer3);
        textAnswer4 = (EditText) findViewById(R.id.answer4);

        if(textQuestion != null) {
            textQuestions.setText(textQuestion.getQuestion());
            textAnswer1.setText(textQuestion.getAnswers().get(0).getAnswerString());
            textAnswer2.setText(textQuestion.getAnswers().get(1).getAnswerString());
            textAnswer3.setText(textQuestion.getAnswers().get(2).getAnswerString());
            textAnswer4.setText(textQuestion.getAnswers().get(3).getAnswerString());
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void submitEditButtonClick(View view) {
        // TODO add or edit questions
        String question = textQuestions.getText().toString();
        Answer answer1 = new Answer(textAnswer1.getText().toString(), 10);
        Answer answer2 = new Answer(textAnswer2.getText().toString(), 0);
        Answer answer3 = new Answer(textAnswer3.getText().toString(), 0);
        Answer answer4 = new Answer(textAnswer4.getText().toString(), 0);

        if(textQuestion != null) {
            TextQuestion editTextQuestion = new TextQuestion(textQuestion.getQuestionID(), question,
                    answer1, answer2, answer3, answer4, textQuestion.getCatalogID());
            TextQuestionDAO editQuestion = new TextQuestionDAO(editTextQuestion);
            editQuestion.editEntry(EditQuestions.this);
        }
        else {
            TextQuestion newTextQuestion = new TextQuestion(0, question,
                    answer1, answer2, answer3, answer4, catalog.getCatalogID());
            TextQuestionDAO newQuestion = new TextQuestionDAO(newTextQuestion);
            newQuestion.insertIntoDatabase(EditQuestions.this);
        }
        finish();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}

