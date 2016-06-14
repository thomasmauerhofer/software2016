package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.ui.BuildQuizAdapter;
import com.bitschupfa.sw16.yaq.ui.ManageQuestionsAdapter;
import com.bitschupfa.sw16.yaq.ui.QuestionCatalogItem;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageQuestions extends YaqActivity {

    private ListView listView;
    private QuestionQuerier questionQuerier;
    private HashMap<String, Integer> questionCatalogMap = new HashMap<>();
    private List<QuestionCatalog> questionCatalogList;
    private ArrayList<QuestionCatalogItem> catalogs = new ArrayList<>();
    private ManageQuestionsAdapter dataAdapter = null;
    private EditText searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleTheme();

        initSearchView();
        displayListView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    public void displayListView() {
        questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();


        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            catalogs.add(new QuestionCatalogItem(catalog, false, this));
        }

        dataAdapter = new ManageQuestionsAdapter(this, R.layout.list_manage_questions, catalogs, true, true, true);
        listView = (ListView) findViewById(R.id.ListViewManageQuestions);
        listView.setAdapter(dataAdapter);
    }

    public void initSearchView() {
        searchText = (EditText) findViewById(R.id.EditTextBuildQuizM);

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

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}

