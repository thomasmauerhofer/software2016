package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.bitschupfa.sw16.yaq.ui.RankingList;

import java.util.ArrayList;
import java.util.List;

public class Statistic extends YaqActivity {

    private ListView list;
    private RankingList ranking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView)findViewById(R.id.ranking);
        ranking = new RankingList(this, new ArrayList<RankingItem>());
        list.setAdapter(ranking);


        if(getIntent().hasExtra("scoreList")) {
            List<RankingItem> items = (List<RankingItem>) getIntent().getExtras().get("scoreList");
            ranking.addItems(items);
        } else {
            Toast.makeText(this, R.string.error_cant_show_score , Toast.LENGTH_LONG).show();
        }
        handleTheme();
    }

    @Override
    protected void handleTheme() {
        List<Button> buttons = new ArrayList<>();
        buttons.add((Button) findViewById(R.id.playAgainButton));
        styleButtons(buttons);
        setBackgroundImage();
    }

    @SuppressWarnings("UnusedParameters")
    public void playAgainButtonClicked(View view) {
        Intent intent = new Intent(Statistic.this, Join.class);
        startActivity(intent);
        finish();
    }
}
