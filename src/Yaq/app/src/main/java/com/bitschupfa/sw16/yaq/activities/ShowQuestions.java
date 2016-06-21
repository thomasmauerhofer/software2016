package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.Answer;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.ui.ShowQuestionsAdapter;

import java.util.ArrayList;

import io.realm.Realm;

public class ShowQuestions extends YaqActivity {

    private ListView listView;
    private ArrayAdapter dataAdapter = null;
    private TextView textCatalogue;
    private TextQuestion actualTextQuestion;
    private QuestionCatalog catalog;
    private int catalogId;
    private ArrayList<TextQuestion> questionList = new ArrayList<>();
    private Realm realm;
    private QuestionQuerier questionQuerier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        handleTheme();

        realm = Realm.getDefaultInstance();
        questionQuerier = new QuestionQuerier(realm);
        catalogId = getIntent().getIntExtra("QuestionCatalog", 0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowQuestions.this, EditQuestions.class);
                intent.putExtra("QuestionCatalog", catalog.getCatalogID());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateListView();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        this.getMenuInflater().inflate(R.menu.menu_manage_questions, menu);
        menu.findItem(R.id.edit).setVisible(true);
        menu.findItem(R.id.delete).setVisible(true);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(ShowQuestions.this, EditQuestions.class);
                intent.putExtra("Question", actualTextQuestion.getQuestionID());
                startActivity(intent);
                return true;
            case R.id.delete:
                realm.beginTransaction();
                for (Answer answer : actualTextQuestion.getAnswers()) {
                    answer.deleteFromRealm();
                }
                actualTextQuestion.deleteFromRealm();
                realm.commitTransaction();
                actualTextQuestion = null;
                updateListView();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void updateListView() {
        if(listView != null) {
            listView.setAdapter(null);
            dataAdapter.clear();
        }
        displayListView();
    }

    public void displayListView() {
        catalog = questionQuerier.getQuestionCatalogById(catalogId);

        textCatalogue = (TextView) findViewById(R.id.textCatalogue);
        textCatalogue.setText(catalog.getName());

        questionList.addAll(questionQuerier.getAllQuestionsFromCatalog(catalog.getCatalogID()));

        dataAdapter = new ShowQuestionsAdapter(this, R.layout.list_show_questions, questionList);
        listView = (ListView) findViewById(R.id.ListViewShowQuestions);
        listView.setAdapter(dataAdapter);

        this.registerForContextMenu(listView);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                TextQuestion item = (TextQuestion) listView.getItemAtPosition(position);

                Intent intent = new Intent(ShowQuestions.this, EditQuestions.class);
                intent.putExtra("Question", item.getQuestionID());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                TextQuestion item = (TextQuestion) listView.getItemAtPosition(pos);
                actualTextQuestion = item;
                openContextMenu(listView);
                return true;
            }
        });
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }
}
