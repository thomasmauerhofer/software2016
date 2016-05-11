package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.game.ClientGameLogic;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;

import java.util.Timer;
import java.util.TimerTask;

public class GameAtHost extends Game {

    private RelativeLayout askedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HostGameLogic.getInstance().setGameActivity(this);
        ClientGameLogic.getInstance().setGameActivity(this);

        askedView = (RelativeLayout) findViewById(R.id.questionAskedView);
        enableShowNextQuestion(false);

        askedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HostGameLogic.getInstance().askNextQuestion();
                enableShowNextQuestion(false);
            }
        });

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                HostGameLogic.getInstance().askNextQuestion();
            }
        }, 500);
    }

    public void enableShowNextQuestion(final boolean active) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                askedView.setEnabled(active);
            }
        });
    }
}
