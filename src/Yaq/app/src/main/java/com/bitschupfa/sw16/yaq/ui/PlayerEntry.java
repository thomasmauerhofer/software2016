package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 22.03.16.
 */
public class PlayerEntry {

    private LinearLayout layout = null;
    private TextView player_name = null;
    private ImageView avatar = null;
    private int id = 0;

    PlayerEntry(Activity activity, int id) {
        setId(activity, id);
        this.id = id;
    }

    public void setPlayer(String name) {
        layout.setBackgroundResource(R.drawable.boarder_table_entry_light);
        player_name.setText(name);
        player_name.setTextColor(Color.parseColor("#1F3255"));
        avatar.setImageResource(R.mipmap.icon_player);
    }

    public void removePlayer() {
        layout.setBackgroundResource(R.drawable.boarder_table_entry_dark);
        player_name.setText(R.string.player);
        player_name.setText(player_name.getText().toString()+ " " + String.valueOf(id));
        player_name.setTextColor(ContextCompat.getColor(layout.getContext(), R.color.player_table_inactive));
        avatar.setImageResource(R.mipmap.icon_player_black);
    }

    public String getName() {
        return player_name.getText().toString();
    }

    public int getId() {
        return id;
    }

    public void setId(Activity activity, int id) {

        String name = null;
        if(this.id != 0) {
            name = getName();
            removePlayer();
        }

        switch (id) {
            case 1:
                layout = (LinearLayout) activity.findViewById(R.id.player1_layer);
                player_name = (TextView) activity.findViewById(R.id.player1_name);
                avatar = (ImageView) activity.findViewById(R.id.player1_img);
                break;
            case 2:
                layout = (LinearLayout) activity.findViewById(R.id.player2_layer);
                player_name = (TextView) activity.findViewById(R.id.player2_name);
                avatar = (ImageView) activity.findViewById(R.id.player2_img);
                break;
            case 3:
                layout = (LinearLayout) activity.findViewById(R.id.player3_layer);
                player_name = (TextView) activity.findViewById(R.id.player3_name);
                avatar = (ImageView) activity.findViewById(R.id.player3_img);
                break;
            case 4:
                layout = (LinearLayout) activity.findViewById(R.id.player4_layer);
                player_name = (TextView) activity.findViewById(R.id.player4_name);
                avatar = (ImageView) activity.findViewById(R.id.player4_img);
                break;
            case 5:
                layout = (LinearLayout) activity.findViewById(R.id.player5_layer);
                player_name = (TextView) activity.findViewById(R.id.player5_name);
                avatar = (ImageView) activity.findViewById(R.id.player5_img);
                break;
            case 6:
                layout = (LinearLayout) activity.findViewById(R.id.player6_layer);
                player_name = (TextView) activity.findViewById(R.id.player6_name);
                avatar = (ImageView) activity.findViewById(R.id.player6_img);
                break;
            case 7:
                layout = (LinearLayout) activity.findViewById(R.id.player7_layer);
                player_name = (TextView) activity.findViewById(R.id.player7_name);
                avatar = (ImageView) activity.findViewById(R.id.player7_img);
                break;
            case 8:
                layout = (LinearLayout) activity.findViewById(R.id.player8_layer);
                player_name = (TextView) activity.findViewById(R.id.player8_name);
                avatar = (ImageView) activity.findViewById(R.id.player8_img);
                break;
        }

        if(id != 0) {
            setPlayer(name);
        }
    }
}
