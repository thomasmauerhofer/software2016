package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.io.IOException;

public class Profile extends YaqActivity {
    private final static String TAG = "ProfileActivity";

    private PlayerProfileStorage profile;
    private EditText nameTextBox;
    private ImageView avatarImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.fab);
        nameTextBox = (EditText) findViewById(R.id.txt_playerName);
        avatarImageView = (ImageView) findViewById(R.id.imgView_avatar);
        profile = PlayerProfileStorage.getInstance(Profile.this);

        nameTextBox.setText(profile.getPlayerName());
        avatarImageView.setImageBitmap(profile.getPlayerAvatar());
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crop.pickImage(Profile.this);
            }
        });
        handleTheme();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        String playerName = nameTextBox.getText().toString();
        if (!playerName.equals(profile.getPlayerName())) {
            profile.setPlayerName(playerName);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Crop.REQUEST_PICK:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Image selected using image picker.");
                    Uri src = data.getData();
                    Uri dest = Uri.fromFile(new File(getCacheDir(), "yaq_avatar_cropped"));
                    Crop.of(src, dest).asSquare().withMaxSize(400, 400).start(Profile.this);
                }
                break;
            case Crop.REQUEST_CROP:
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Cropped image received.");
                    handleNewAvatar(Crop.getOutput(data));
                } else if (requestCode == Crop.RESULT_ERROR) {
                    String errorMsg = Crop.getError(data).getMessage();
                    Log.e(TAG, "Could not get cropped image: " + errorMsg);
                    Toast.makeText(Profile.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.d(TAG, "Unhandled request code in onActivityResult: " + requestCode);
                break;
        }
    }

    private void handleNewAvatar(Uri avatarUri) {
        try {
            Bitmap avatar = MediaStore.Images.Media.getBitmap(this.getContentResolver(), avatarUri);
            avatarImageView.setImageBitmap(avatar);
            profile.setPlayerAvatar(avatar);
        } catch (IOException e) {
            Log.e(TAG, "Could not get cropped image: " + e.getMessage());
        }
    }
}
