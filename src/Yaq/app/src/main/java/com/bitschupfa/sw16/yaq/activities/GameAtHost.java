package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.game.ClientGameLogic;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.ui.HostCloseConnectionDialog;
import com.bitschupfa.sw16.yaq.ui.RankingItem;
import com.bitschupfa.sw16.yaq.utils.CastHelper;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class GameAtHost extends Game {
    private static final String TAG = "GameAtHost";
    private LinearLayout askedView;
    private Button nextQuestion;

    private CastHelper castHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HostGameLogic.getInstance().setGameActivity(this);
        ClientGameLogic.getInstance().setGameActivity(this);

        askedView = (LinearLayout) findViewById(R.id.questionAskedView);
        nextQuestion = (Button) findViewById(R.id.next_question);
        enableShowNextQuestion(false);

        nextQuestion.setOnClickListener(new View.OnClickListener() {
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

        castHelper = CastHelper.getInstance(getApplicationContext(), CastHelper.GameState.GAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(castHelper.mMediaRouteSelector);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        castHelper.addCallbacks();
    }

    public void enableShowNextQuestion(final boolean active) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                askedView.setEnabled(active);
                int visible = active ? View.VISIBLE : View.INVISIBLE;
                nextQuestion.setVisibility(visible);
                if (active)
                    questionAskLL.setAlpha(0.7f);
                else {
                    questionAskLL.setAlpha(1.0f);
                }
            }
        });
    }

    @Override
    public void showStatisticActivity(ArrayList<RankingItem> scoreList) {
        Intent intent = new Intent(GameAtHost.this, StatisticsAtHost.class);
        intent.putExtra("scoreList", scoreList);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        new HostCloseConnectionDialog(this, castHelper).show(getFragmentManager(), TAG);
        castHelper.teardown(false);
    }
}
