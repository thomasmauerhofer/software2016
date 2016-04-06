package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.RankingItem;
import com.bitschupfa.sw16.yaq.Utils.RankingList;

import java.util.ArrayList;

public class Statistic extends AppCompatActivity {

    private ListView list;

    private RankingList ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        list = (ListView)findViewById(R.id.ranking);
        ranking = new RankingList(this, new ArrayList<RankingItem>());
        list.setAdapter(ranking);

        ranking.addItems();
    }

    public void playAgainButtonClicked(View view) {
        Intent intent = new Intent(Statistic.this, QuestionsAsked.class);
        startActivity(intent);
        finish();
    }
}
