package com.bitschupfa.sw16.yaq.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.QuestionQuerier;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.database.object.TextQuestion;
import com.bitschupfa.sw16.yaq.utils.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private ArrayList<QuestionCatalogueItem> qCList = new ArrayList<>();
    private customAdapter dataAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayListView();
        checkButtonClick();
    }

    public void displayListView() {
        questionQuerier = new QuestionQuerier(this);
        questionCatalogList = questionQuerier.getAllQuestionCatalogs();

        QuestionCatalogueItem qCatalogueItem;

        for (QuestionCatalog catalog : questionCatalogList) {
            questionCatalogMap.put(catalog.getName(), catalog.getCatalogID());
            qCatalogueItem = new QuestionCatalogueItem(catalog.getName(), catalog.getCatalogID(), false);
            qCList.add(qCatalogueItem);
        }

        dataAdapter = new customAdapter(this, R.layout.list_build_quiz, qCList);
        listView = (ListView) findViewById(R.id.ListViewBuildQuiz);
        listView.setAdapter(dataAdapter);
    }

    private void checkButtonClick() {
        btnBuildQuiz = (Button) findViewById(R.id.ButtonBuildQuiz);

        btnBuildQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer responseText = new StringBuffer();
                responseText.append("The following were selected...\n");

                ArrayList<QuestionCatalogueItem> questionCatalogueList = dataAdapter.questionCatalagoueItem;
                for (int i = 0; i < questionCatalogueList.size(); i++) {
                    QuestionCatalogueItem item = questionCatalogueList.get(i);
                    if (item.isChecked()) {
                        responseText.append("\n- " + item.getName());
                        questions.addAll(questionQuerier.getAllQuestionsFromCatalog(item.getId()));
                    }
                }
                responseText.append("\n\nquestions: " + questions.size());
                Toast.makeText(getApplicationContext(), responseText, Toast.LENGTH_LONG).show();

                Quiz quiz;
                if (getIntent().hasExtra("questions")) {
                    quiz = (Quiz) getIntent().getExtras().get("questions");
                } else {
                    quiz = new Quiz();
                }
                quiz.setQuiz(questions);
                finish();
            }
        });
    }

    public static class QuestionCatalogueItem {
        String name;
        Integer id;
        Boolean checked;

        public QuestionCatalogueItem(String name_, Integer id_, Boolean checked_) {
            name = name_;
            id = id_;
            checked = checked_;
        }

        public Integer getId() {
            return id;
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
    }

    private class customAdapter extends ArrayAdapter<QuestionCatalogueItem> {

        private ArrayList<QuestionCatalogueItem> questionCatalagoueItem;

        public customAdapter(Context context, int textViewResourceId,
                             ArrayList<QuestionCatalogueItem> questionCatalogueList) {
            super(context, textViewResourceId, questionCatalogueList);
            this.questionCatalagoueItem = new ArrayList<>();
            this.questionCatalagoueItem.addAll(questionCatalogueList);
        }

        private class ViewHolder {
            TextView name;
            CheckBox checkBox;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = layoutInflater.inflate(R.layout.list_build_quiz, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);

                holder.checkBox.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        QuestionCatalogueItem item = (QuestionCatalogueItem) cb.getTag();
                        item.setChecked(cb.isChecked());
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            QuestionCatalogueItem item = questionCatalagoueItem.get(position);
            holder.name.setText(item.getName());
            holder.checkBox.setChecked(item.isChecked());
            holder.checkBox.setTag(item);

            return convertView;
        }
    }
}
