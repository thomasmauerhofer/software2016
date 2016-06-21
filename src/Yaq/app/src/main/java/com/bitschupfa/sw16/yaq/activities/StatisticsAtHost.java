package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.MediaRouteActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.communication.messages.PLAYAGAINMessage;
import com.bitschupfa.sw16.yaq.game.HostGameLogic;
import com.bitschupfa.sw16.yaq.ui.HostCloseConnectionDialog;
import com.bitschupfa.sw16.yaq.utils.CastHelper;
import com.bitschupfa.sw16.yaq.utils.QuizFactory;

public class StatisticsAtHost extends Statistic {
    private static final String TAG = "StatisticsAtHost";
    private CastHelper castHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        castHelper = CastHelper.getInstance(getApplicationContext(), CastHelper.GameState.END);

        Button playAgainButton = (Button) findViewById(R.id.playAgainButton);
        playAgainButton.setVisibility(View.VISIBLE);
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

    @SuppressWarnings("UnusedParameters")
    public void playAgainButtonClicked(View view) {
        HostGameLogic.getInstance().sendMessageToClients(new PLAYAGAINMessage());
    }

    @Override
    public void onBackPressed() {
        new HostCloseConnectionDialog(this, castHelper, true).show(getFragmentManager(), TAG);
    }
}
