package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.ui.PlayerList;

public class Join extends AppCompatActivity {

    private PlayerList playerList;

    private TextView textView;
    private ProgressBar pBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        playerList = new PlayerList(this);

        playerList.addPlayer("Thomas");
        playerList.addPlayer("Manuel");
        playerList.addPlayer("Matthias");
        playerList.addPlayer("Max");
        playerList.addPlayer("Johannes");
        playerList.addPlayer("Patrik");

        playerList.removePlayerWithName("Max");

        pBar = (ProgressBar) findViewById(R.id.loadingBar);
        textView = (TextView) findViewById(R.id.textView);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pBar.setVisibility(View.INVISIBLE);
                textView.setTextSize(40);
                startTimer();
            }
        });
    }

    public void startTimer()
    {
        new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                textView.setText("" + millisUntilFinished / 1000);
            }

            public void onFinish() {
                Intent intent = new Intent(Join.this, QuestionsAsked.class);
                startActivity(intent);
            }
        }.start();
    }
}
