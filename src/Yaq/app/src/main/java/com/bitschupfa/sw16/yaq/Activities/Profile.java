package com.bitschupfa.sw16.yaq.Activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.PlayerProfile;

public class Profile extends AppCompatActivity {
    private final static String TAG = "ProfileActivity";

    private PlayerProfile profile;
    private EditText nameTextbox;
    private ImageView avatarImageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        nameTextbox = (EditText) findViewById(R.id.txt_playerName);
        avatarImageview = (ImageView) findViewById(R.id.imgView_avatar);
        profile = PlayerProfile.getInstance(this.getApplicationContext());

        nameTextbox.setText(profile.getPlayerName());
        avatarImageview.setImageBitmap(profile.getPlayerAvatar());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String playerName = nameTextbox.getText().toString();
        if (!playerName.equals(profile.getPlayerName())) {
            Log.d(TAG, "Player name changed. Update in preferences.");
            profile.setPlayerName(playerName);
        }
    }
}
