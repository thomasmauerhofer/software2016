package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StatisticsAtHost extends Statistic {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressWarnings("UnusedParameters")
    @Override
    public void playAgainButtonClicked(View view) {
        Intent intent = new Intent(StatisticsAtHost.this, Host.class);
        startActivity(intent);
        finish();
    }
}
