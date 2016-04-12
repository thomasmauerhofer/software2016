package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.Quiz;
import com.bitschupfa.sw16.yaq.ui.PlayerEntry;
import com.bitschupfa.sw16.yaq.ui.PlayerList;

public class Host extends AppCompatActivity {

    private PlayerList playerList;

    private Quiz quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        quiz = new Quiz();

        playerList = new PlayerList(this);

        playerList.addPlayer("Thomas");
        playerList.addPlayer("Manuel");
        playerList.addPlayer("Matthias");
        playerList.addPlayer("Max");
        playerList.addPlayer("Johannes");
        playerList.addPlayer("Patrik");


        playerList.removePlayerWithName("Max");
    }

    public void startButtonClicked(View view) {
        Intent intent = new Intent(Host.this, QuestionsAsked.class);
        intent.putExtra("questions", quiz.createTmpQuiz());
        startActivity(intent);
        finish();
    }

    public void buildQuizButtonClicked(View view) {
        Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

    public void advancedSettingsButtonClicked(View view) {
        Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
    }

}
