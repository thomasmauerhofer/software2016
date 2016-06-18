package com.bitschupfa.sw16.yaq.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

import javax.xml.transform.TransformerFactoryConfigurationError;

public class EditQuestions extends YaqActivity {

    private static final int MIN_ANSWER_NUMBER = 0;
    private static final int MAX_ANSWER_NUMBER = 10;

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
        // TODO add or edit questions + get answer value
        String question = textQuestions.getText().toString();
        Answer answer1 = new Answer(textAnswer1.getText().toString(), 10);
        Answer answer2 = new Answer(textAnswer2.getText().toString(), 0);
        Answer answer3 = new Answer(textAnswer3.getText().toString(), 0);
        Answer answer4 = new Answer(textAnswer4.getText().toString(), 0);

        if(textQuestion != null) {
            textQuestion.setQuestion(question);
            textQuestion.setAnswers(answer1, answer2, answer3, answer4);
            TextQuestionDAO editQuestion = new TextQuestionDAO(textQuestion);
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

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit1(View view) {
        final TextView numberPickerLabel = (TextView) findViewById(R.id.numberPickerEdit1);
        showNumberPickerDialogEdit(numberPickerLabel);
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit2(View view) {
        final TextView numberPickerLabel = (TextView) findViewById(R.id.numberPickerEdit2);
        showNumberPickerDialogEdit(numberPickerLabel);
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit3(View view) {
        final TextView numberPickerLabel = (TextView) findViewById(R.id.numberPickerEdit3);
        showNumberPickerDialogEdit(numberPickerLabel);
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit4(View view) {
        final TextView numberPickerLabel = (TextView) findViewById(R.id.numberPickerEdit4);
        showNumberPickerDialogEdit(numberPickerLabel);
    }

    public void showNumberPickerDialogEdit(final TextView numberPickerLabel) {
        Dialog numberQuestionsDialog = new Dialog(this);
        numberQuestionsDialog.setContentView(R.layout.dialog_number_picker);

        NumberPicker picker = (NumberPicker) numberQuestionsDialog.findViewById(R.id.np_dialog);
        picker.setMinValue(MIN_ANSWER_NUMBER);
        picker.setMaxValue(MAX_ANSWER_NUMBER);
        // TODO get Value from Question
        picker.setValue(0);

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                numberPickerLabel.setText(String.valueOf(newVal));
                // TODO set Value from Question
            }
        });

        numberQuestionsDialog.show();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}

