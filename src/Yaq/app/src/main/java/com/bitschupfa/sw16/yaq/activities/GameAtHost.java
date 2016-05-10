package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;

public class GameAtHost extends Game {

    private RelativeLayout askedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HostGameLogic.getInstance().setGameActivity(this);

        askedView = (RelativeLayout) findViewById(R.id.questionAskedView);
        enableShowNextQuestion(false);

        askedView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HostGameLogic.getInstance().askNextQuestion();
            }
        });
    }

    public void enableShowNextQuestion(boolean active) {
        askedView.setEnabled(active);
    }
}
