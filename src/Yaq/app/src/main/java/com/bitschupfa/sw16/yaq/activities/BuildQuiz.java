package com.bitschupfa.sw16.yaq.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.utils.CustomAdapter;
import com.bitschupfa.sw16.yaq.utils.QuizMonitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.bitschupfa.sw16.yaq.activities.BuildQuiz.CheckBoxValue.*;

public class BuildQuiz extends AppCompatActivity {

    private ListView listView;
    private QuestionQuerier questionQuerier;
    private Button btnBuildQuiz;
    private HashMap<String, Integer> questionCatalogMap = new HashMap<>();
    private List<QuestionCatalog> questionCatalogList;
    private ArrayList<TextQuestion> questions = new ArrayList<TextQuestion>();
    private ArrayList<QuestionCatalogueItem> qCList = new ArrayList<>();
    private CustomAdapter dataAdapter = null;
    private EditText searchText;
    /*private CheckBox checkEasy;
    private CheckBox checkMedium;
    private CheckBox checkHard;

    private boolean isCheckedEasy = true;
    private boolean isCheckedMedium = true;
    private boolean isCheckedHard = true;*/

    public enum CheckBoxValue {
        UNCHECKED, EASY, MEDIUM, HARD
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initSearchView();
        //initFilterView();
        displayListView();
        checkButtonClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void displayListView() {
        questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();

        QuestionCatalogueItem qCatalogueItem;

        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            boolean hasEasyQuestions = (questionQuerier.getAllQuestionsFromCatalogByDifficulty(catalog.getCatalogID(), EASY.ordinal()).size() > 0);
            boolean hasMediumQuestions = (questionQuerier.getAllQuestionsFromCatalogByDifficulty(catalog.getCatalogID(), MEDIUM.ordinal()).size() > 0);
            boolean hasHardQuestions = (questionQuerier.getAllQuestionsFromCatalogByDifficulty(catalog.getCatalogID(), HARD.ordinal()).size() > 0);

            qCatalogueItem = new QuestionCatalogueItem(catalog.getName(), catalog.getCatalogID(), 1, false,
                    questionQuerier.getAllQuestionsFromCatalog(catalog.getCatalogID()).size(), hasEasyQuestions,
                    hasMediumQuestions, hasHardQuestions, this);

            qCList.add(qCatalogueItem);
        }

        dataAdapter = new CustomAdapter(this, R.layout.list_build_quiz, qCList, true, true, true);
        listView = (ListView) findViewById(R.id.ListViewBuildQuiz);
        listView.setAdapter(dataAdapter);
    }

    private void initSearchView() {
        searchText = (EditText) findViewById(R.id.EditTextBuildQuiz);

        searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                dataAdapter.getFilter().filter(cs.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    /*private void initFilterView() {
        checkEasy = (CheckBox) findViewById(R.id.checkEasy);
        checkMedium = (CheckBox) findViewById(R.id.checkMedium);
        checkHard = (CheckBox) findViewById(R.id.checkHard);
    }*/

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
    }

    private void checkButtonClick() {
        btnBuildQuiz = (Button) findViewById(R.id.ButtonBuildQuiz);

        btnBuildQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                /*ArrayList<QuestionCatalogueItem> questionCatalogueList = dataAdapter.questionCatalagoueItem;
                for (int i = 0; i < questionCatalogueList.size(); i++) {
                    QuestionCatalogueItem item = questionCatalogueList.get(i);
                    if (item.isChecked()) {
                        responseText.append("\n- " + item.getName());
                        questions.addAll(questionQuerier.getAllQuestionsFromCatalog(item.getId()));
                    }
                }*/

                for(List<TextQuestion> textQuestions : dataAdapter.getCheckedQuestions()) {
                    questions.addAll(textQuestions);
                }

                responseText.append("\n\nquestions: " + questions.size());
                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                QuizMonitor app = (QuizMonitor) getApplication();
                if (app.getQuizClass() != null) {
                    app.getQuizClass().addQuestions(questions);
                }

                finish();
            }
        });
    }

    /*public Integer getCheckedEasyValue() {
        Integer value;
        if(isCheckedEasy) {
            value = EASY.ordinal();
        }
        else {
            value = UNCHECKED.ordinal();
        }
        if(dataAdapter != null) {
            dataAdapter.setCheckBoxEasy(value);
        }
        return value;
    }

    public Integer getCheckedMediumValue() {
        Integer value;
        if(isCheckedMedium) {
            value = MEDIUM.ordinal();
        }
        else {
            value = UNCHECKED.ordinal();
        }
        if(dataAdapter != null) {
            dataAdapter.setCheckBoxMedium(value);
        }
        return value;
    }

    public Integer getCheckedHardValue() {
        Integer value;
        if(isCheckedHard) {
            value = HARD.ordinal();
        }
        else {
            value = UNCHECKED.ordinal();
        }
        if(dataAdapter != null) {
            dataAdapter.setCheckBoxHard(value);
        }
        return value;
    }*/

    public void setCheckedEasy(boolean checked) {
        /*int value;
        if (checked) {
            value = EASY.ordinal();
        } else {
            value = UNCHECKED.ordinal();
        }*/
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxEasy(checked);
        }
    }

    public void setCheckedMedium(boolean checked) {
        /*int value;
        if (checked) {
            value = MEDIUM.ordinal();
        } else {
            value = UNCHECKED.ordinal();
        }*/
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxMedium(checked);
        }
    }

    public void setCheckedHard(boolean checked) {
        /*int value;
        if (checked) {
            value = HARD.ordinal();
        } else {
            value = UNCHECKED.ordinal();
        }*/
        if (dataAdapter != null) {
            dataAdapter.setCheckBoxHard(checked);
        }
    }

    public static class QuestionCatalogueItem {
        private String name;
        private int id;
        private int difficulty;
        private boolean checked;
        private int questionsCounter;
        private boolean hasEasyQuestions;
        private boolean hasMediumQuestions;
        private boolean hasHardQuestions;
        private QuestionQuerier questionQuerier;

        public QuestionCatalogueItem(String name_, int id_, int difficulty_,
                                     boolean checked_, int questionsCounter_, boolean hasEasyQuestions_,
                                     boolean hasMediumQuestions_, boolean hasHardQuestions_, Context context) {
            name = name_;
            id = id_;
            difficulty = difficulty_;
            checked = checked_;
            questionsCounter = questionsCounter_;
            hasEasyQuestions = hasEasyQuestions_;
            hasMediumQuestions = hasMediumQuestions_;
            hasHardQuestions = hasHardQuestions_;
            questionQuerier = new QuestionQuerier(context);
        }

        public int getId() {
            return id;
        }

        public void setDifficulty(Integer difficulty) {
            this.difficulty = difficulty;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public int getQuestionsCounter() {
            return questionsCounter;
        }

        public void setQuestionsCounter(int questionsCounter) {
            this.questionsCounter = questionsCounter;
        }

        public boolean hasEasyQuestions() {
            return hasEasyQuestions;
        }

        public boolean hasMediumQuestions() {
            return hasMediumQuestions;
        }

        public boolean hasHardQuestions() {
            return hasHardQuestions;
        }

        public List<TextQuestion> getAllQuestionsByDiff(int id, int difficulty) {
            return questionQuerier.getAllQuestionsFromCatalogByDifficulty(id, difficulty);
        }
    }
}
