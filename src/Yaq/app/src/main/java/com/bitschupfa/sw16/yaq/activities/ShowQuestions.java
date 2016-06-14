package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.dao.TextQuestionDAO;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.QuestionCatalogItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowQuestions extends YaqActivity {

    private ListView listView;
    private QuestionQuerier questionQuerier;
    private HashMap<String, Integer> questionCatalogMap = new HashMap<>();
    private List<QuestionCatalog> questionCatalogList;
    private ArrayList<String> questionsList = new ArrayList<>();
    private ArrayAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleTheme();

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

        QuestionCatalog catalog = (QuestionCatalog) getIntent().getSerializableExtra("QuestionCatalogue");

        for (TextQuestion question : catalog.getTextQuestionList()) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            questionsList.add(question.getQuestion());
        }

        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionsList);
        listView = (ListView) findViewById(R.id.ListViewShowQuestions);
        listView.setAdapter(dataAdapter);
        /*questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();

        QuestionCatalog catalog = new QuestionCatalog()
        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            catalogs.add(new QuestionCatalogItem(catalog, false, this));
        }

        dataAdapter = new ArrayAdapter<>(this, R.layout.list_show_questions, catalogs);
        listView = (ListView) findViewById(R.id.ListViewShowQuestions);
        listView.setAdapter(dataAdapter);

        /*listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                QuestionCatalogItem item = (QuestionCatalogItem) listView.getItemAtPosition(position);

                Intent intent = new Intent(ManageQuestions.this, ShowQuestions.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}
