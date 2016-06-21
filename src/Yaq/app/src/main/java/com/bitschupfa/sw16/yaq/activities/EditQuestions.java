package com.bitschupfa.sw16.yaq.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import io.realm.Realm;

public class EditQuestions extends YaqActivity {

    private static final int MIN_ANSWER_NUMBER = 0;
    private static final int MAX_ANSWER_NUMBER = 20;

    private EditText textQuestionEdit;
    private EditText textAnswer1Edit;
    private EditText textAnswer2Edit;
    private EditText textAnswer3Edit;
    private EditText textAnswer4Edit;
    private TextView numberPickerLabel1;
    private TextView numberPickerLabel2;
    private TextView numberPickerLabel3;
    private TextView numberPickerLabel4;

    private TextQuestion textQuestion;
    private int catalogId;

    private Realm realm;
    private QuestionQuerier questionQuerier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        handleTheme();

        realm = Realm.getDefaultInstance();
        questionQuerier = new QuestionQuerier(realm);
        displayQuestion();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public void displayQuestion() {
        questionQuerier = new QuestionQuerier(realm);
        catalogId = getIntent().getIntExtra("QuestionCatalog", 0);
        textQuestion = questionQuerier.getQuestionById(getIntent().getIntExtra("Question", 0));

        textQuestionEdit = (EditText) findViewById(R.id.question);
        textAnswer1Edit = (EditText) findViewById(R.id.answer1);
        textAnswer2Edit = (EditText) findViewById(R.id.answer2);
        textAnswer3Edit = (EditText) findViewById(R.id.answer3);
        textAnswer4Edit = (EditText) findViewById(R.id.answer4);
        numberPickerLabel1 = (TextView) findViewById(R.id.numberPickerEdit1);
        numberPickerLabel2 = (TextView) findViewById(R.id.numberPickerEdit2);
        numberPickerLabel3 = (TextView) findViewById(R.id.numberPickerEdit3);
        numberPickerLabel4 = (TextView) findViewById(R.id.numberPickerEdit4);

        if(textQuestion != null) {
            textQuestionEdit.setText(textQuestion.getQuestion());
            textAnswer1Edit.setText(textQuestion.getAnswers().get(0).getAnswerString());
            textAnswer2Edit.setText(textQuestion.getAnswers().get(1).getAnswerString());
            textAnswer3Edit.setText(textQuestion.getAnswers().get(2).getAnswerString());
            textAnswer4Edit.setText(textQuestion.getAnswers().get(3).getAnswerString());
            numberPickerLabel1.setText(String.valueOf(textQuestion.getAnswers().get(0).getAnswerValue()));
            numberPickerLabel2.setText(String.valueOf(textQuestion.getAnswers().get(1).getAnswerValue()));
            numberPickerLabel3.setText(String.valueOf(textQuestion.getAnswers().get(2).getAnswerValue()));
            numberPickerLabel4.setText(String.valueOf(textQuestion.getAnswers().get(3).getAnswerValue()));
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void submitEditButtonClick(View view) {
        String question = textQuestionEdit.getText().toString();

        realm.beginTransaction();
        Answer answer1 = realm.createObject(Answer.class);
        answer1.setAnswerString(textAnswer1Edit.getText().toString());
        answer1.setAnswerValue(Integer.parseInt(numberPickerLabel1.getText().toString()));
        Answer answer2 = realm.createObject(Answer.class);
        answer2.setAnswerString(textAnswer2Edit.getText().toString());
        answer2.setAnswerValue(Integer.parseInt(numberPickerLabel2.getText().toString()));
        Answer answer3 = realm.createObject(Answer.class);
        answer3.setAnswerString(textAnswer3Edit.getText().toString());
        answer3.setAnswerValue(Integer.parseInt(numberPickerLabel3.getText().toString()));
        Answer answer4 = realm.createObject(Answer.class);
        answer4.setAnswerString(textAnswer4Edit.getText().toString());
        answer4.setAnswerValue(Integer.parseInt(numberPickerLabel4.getText().toString()));
        realm.commitTransaction();

        if(textQuestion != null) {
            realm.beginTransaction();
            for(Answer answer : textQuestion.getAnswers()) {
                answer.deleteFromRealm();
            }
            textQuestion.setQuestion(question);
            textQuestion.setAnswers(answer1, answer2, answer3, answer4);
            realm.copyToRealmOrUpdate(textQuestion);
            realm.commitTransaction();
            textQuestion = null;
        }
        else {
            QuestionCatalog catalog = questionQuerier.getQuestionCatalogById(catalogId);

            realm.beginTransaction();
            TextQuestion newTextQuestion = new TextQuestion(questionQuerier.getHighestQuestionId() + 1,
                    question, answer1, answer2, answer3, answer4, catalogId);
            catalog.getTextQuestionList().add(newTextQuestion);
            //realm.copyToRealm(newTextQuestion);
            realm.commitTransaction();
        }
        finish();
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit1(View view) {
        showNumberPickerDialogEdit(numberPickerLabel1);
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit2(View view) {
        showNumberPickerDialogEdit(numberPickerLabel2);
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit3(View view) {
        showNumberPickerDialogEdit(numberPickerLabel3);
    }

    @SuppressWarnings("UnusedParameters")
    public void showNumberPickerDialogEdit4(View view) {
        showNumberPickerDialogEdit(numberPickerLabel4);
    }

    public void showNumberPickerDialogEdit(final TextView numberPickerLabel) {
        Dialog numberQuestionsDialog = new Dialog(this);
        numberQuestionsDialog.setContentView(R.layout.dialog_number_picker);

        NumberPicker picker = (NumberPicker) numberQuestionsDialog.findViewById(R.id.np_dialog);
        picker.setMinValue(MIN_ANSWER_NUMBER);
        picker.setMaxValue(MAX_ANSWER_NUMBER);

        picker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                if (i > 10) {
                    i = 10 - i;
                }
                return String.valueOf(i);
            }
        });

        picker.setValue(Integer.parseInt(numberPickerLabel.getText().toString()));

        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal > 10) {
                    newVal -= 10;
                    newVal = -newVal;
                }
                numberPickerLabel.setText(String.valueOf(newVal));
            }
        });

        numberQuestionsDialog.show();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

