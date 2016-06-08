package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.ui.BuildQuizAdapter;
import com.bitschupfa.sw16.yaq.ui.QuestionCatalogItem;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuildQuiz extends YaqActivity {

    private static final int MIN_NUMBER_OF_QUESTIONS = 1;
    private static final int MAX_NUMBER_OF_QUESTIONS = 100;

    private ListView listView;
    private QuestionQuerier questionQuerier;
    private Button btnBuildQuiz;
    private HashMap<String, Integer> questionCatalogMap = new HashMap<>();
    private List<QuestionCatalog> questionCatalogList;
    private ArrayList<QuestionCatalogItem> catalogs = new ArrayList<>();
    private BuildQuizAdapter dataAdapter = null;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnBuildQuiz = (Button) findViewById(R.id.ButtonBuildQuiz);

        initSearchView();
        initNumberPicker();
        displayListView();
        handleTheme();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void displayListView() {
        questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();


        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            catalogs.add(new QuestionCatalogItem(catalog, false, this));
        }

        dataAdapter = new BuildQuizAdapter(this, R.layout.list_build_quiz, catalogs, true, true, true);
        listView = (ListView) findViewById(R.id.ListViewBuildQuiz);
        listView.setAdapter(dataAdapter);
    }

    public void initSearchView() {
        searchText = (EditText) findViewById(R.id.EditTextBuildQuiz);

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                dataAdapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }

    public void initNumberPicker() {
        TextView numberPicker = (TextView) findViewById(R.id.numberPicker);
        numberPicker.setText(String.valueOf(QuizFactory.instance().getNumberOfQuestions()));
    }

    @SuppressWarnings("UnusedParameters")
    public void numberPickerMinusClicked(View view) {
        TextView numberPicker = (TextView) findViewById(R.id.numberPicker);
        int numberOfQuestions = QuizFactory.instance().getNumberOfQuestions() - 1;

        if (numberOfQuestions >= MIN_NUMBER_OF_QUESTIONS) {
            numberPicker.setText(String.valueOf(numberOfQuestions));
            QuizFactory.instance().setNumberOfQuestions(numberOfQuestions);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void numberPickerPlusClicked(View view) {
        TextView numberPicker = (TextView) findViewById(R.id.numberPicker);
        int numberOfQuestions = QuizFactory.instance().getNumberOfQuestions() + 1;

        if (numberOfQuestions <= MAX_NUMBER_OF_QUESTIONS) {
            numberPicker.setText(String.valueOf(numberOfQuestions));
            QuizFactory.instance().setNumberOfQuestions(numberOfQuestions);
        }
    }

    @SuppressWarnings("UnusedParameters")
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkEasy:
                setCheckedEasy(checked);
                break;
            case R.id.checkMedium:
                setCheckedMedium(checked);
                break;
            case R.id.checkHard:
                setCheckedHard(checked);
                break;
            default:
                break;
        }
        dataAdapter.getFilter().filter(searchText.getText());
    }

    @SuppressWarnings("UnusedParameters")
    public void submitButtonClick(View view) {
        QuizFactory.instance().clearQuiz();

        for (QuestionCatalogItem catalog : dataAdapter.getCatalogItems()) {
            if (catalog.isChecked()) {
                QuizFactory.instance().addQuestions(catalog.getCatalog().getName(), catalog.getCatalog().getTextQuestionList());
            }
        }
        finish();
    }

    public void setCheckedEasy(boolean checked) {
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxEasy(checked);
        }
    }

    public void setCheckedMedium(boolean checked) {
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxMedium(checked);
        }
    }

    public void setCheckedHard(boolean checked) {
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxHard(checked);
        }
    }
}
