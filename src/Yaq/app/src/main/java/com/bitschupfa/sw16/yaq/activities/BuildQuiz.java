package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by manu on 30.04.16.
 */
public class BuildQuiz extends AppCompatActivity {

    private ListView listView;
    private QuestionQuerier questionQuerier;
    private Button btnBuildQuiz;
    private HashMap<String, Integer> questionCatalogMap = new HashMap<>();
    private List<QuestionCatalog> questionCatalogList;
    private List<TextQuestion> questions = new ArrayList<>();
    private Queue<Integer> queue = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.ListViewBuildQuiz);
        listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listView.setTextFilterEnabled(true);

        btnBuildQuiz = (Button) findViewById(R.id.ButtonBuildQuiz);

        questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();

        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
        }

        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_checked, new ArrayList<>(questionCatalogMap.keySet())));

        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckedTextView item = (CheckedTextView) view;
                Integer questionId = 0;
                if (item.isChecked()) {
                    questionId = questionCatalogMap.get(parent.getItemAtPosition(position));
                    Log.d("BuildQuiz", "add id: " + questionId);
                    queue.add(questionId);
                } else {
                    questionId = questionCatalogMap.get(parent.getItemAtPosition(position));
                    Log.d("BuildQuiz", "remove id: " + questionId);
                    queue.remove(questionId);
                }
            }
        });

        btnBuildQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Iterator it = queue.iterator();

                while (it.hasNext()) {
                    questions.addAll(questionQuerier.getAllQuestionsFromCatalog((Integer) it.next()));
                }
                Toast.makeText(BuildQuiz.this, "questions: " + questions.size(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
