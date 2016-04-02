package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.Database.QuestionCatalog;
import com.bitschupfa.sw16.yaq.Database.QuestionQuerier;
import com.bitschupfa.sw16.yaq.Database.TextQuestion;
import com.bitschupfa.sw16.yaq.R;

import org.w3c.dom.Text;

import java.util.List;

public class QuesionsAsked extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesions_asked);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        QuestionQuerier questionQuerier = QuestionQuerier.instance(this, 1, 2);
        QuestionCatalog questionCatalog = questionQuerier.getCurrentQuestionCatalog();
        TextQuestion textQuestion = questionCatalog.getNextQuestion();
        TextView question = (TextView) findViewById(R.id.question);

        if(textQuestion == null){
           question.setText("No more questions");
        }else{
            Button answer1 = (Button) findViewById(R.id.answer1);
            Button answer2 = (Button) findViewById(R.id.answer2);
            Button answer3 = (Button) findViewById(R.id.answer3);
            Button answer4 = (Button) findViewById(R.id.answer4);
            answer1.setText(textQuestion.getAnswer1());
            answer2.setText(textQuestion.getAnswer2());
            answer3.setText(textQuestion.getAnswer3());
            answer4.setText(textQuestion.getAnswer4());
            question.setText(textQuestion.getQuestion());
        }
    }

    public void answerButtonClicked(View view) {
        Intent intent = new Intent(QuesionsAsked.this, Statistic.class);
        startActivity(intent);
        finish();
    }

}
