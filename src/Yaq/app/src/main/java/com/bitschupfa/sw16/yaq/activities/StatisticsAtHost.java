package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.view.View;

/**
 * Created by thomas on 11.05.16.
 */
public class StatisticsAtHost extends Statistic {

    @SuppressWarnings("UnusedParameters")
    @Override
    public void playAgainButtonClicked(View view) {
        Intent intent = new Intent(StatisticsAtHost.this, Host.class);
        startActivity(intent);
        finish();
    }

}
