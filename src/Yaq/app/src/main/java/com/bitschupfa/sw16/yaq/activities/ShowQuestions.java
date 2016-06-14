package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.QuestionCatalogItem;
import com.bitschupfa.sw16.yaq.ui.ShowQuestionsAdapter;

public class ShowQuestions extends YaqActivity {

    private ListView listView;
    private ArrayAdapter dataAdapter = null;
    private TextView textCatalogue;

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

        textCatalogue = (TextView) findViewById(R.id.textCatalogue);
        textCatalogue.setText(catalog.getName());

        dataAdapter = new ShowQuestionsAdapter(this, R.layout.list_show_questions, catalog.getTextQuestionList());
        listView = (ListView) findViewById(R.id.ListViewShowQuestions);
        listView.setAdapter(dataAdapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                TextQuestion item = (TextQuestion) listView.getItemAtPosition(position);

                Intent intent = new Intent(ShowQuestions.this, EditQuestions.class);
                intent.putExtra("Question", item);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}
