package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;



class PlayerEntry {

    private TextView player_entry= null;
    private int id = 0;

    PlayerEntry(Activity activity, int id) {
        setId(activity, id);
        this.id = id;
    }

    public void setPlayer(String name) {
        player_entry.setText(name);
        player_entry.setTextColor(Color.parseColor("#1F3255"));
        player_entry.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_player, 0, 0, 0);
    }

    public void removePlayer() {
        player_entry.setText(R.string.player);
        String text = player_entry.getText().toString()+ " " + String.valueOf(id);
        player_entry.setText(text);
        player_entry.setTextColor(ContextCompat.getColor(player_entry.getContext(), R.color.player_table_inactive));
        player_entry.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_player_black, 0, 0, 0);
    }

    public String getName() {
        return player_entry.getText().toString();
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
                player_entry = (TextView) activity.findViewById(R.id.player1_name);
                break;
            case 2:
                player_entry = (TextView) activity.findViewById(R.id.player2_name);
                break;
            case 3:
                player_entry = (TextView) activity.findViewById(R.id.player3_name);
                break;
            case 4:
                player_entry = (TextView) activity.findViewById(R.id.player4_name);
                break;
            case 5:
                player_entry = (TextView) activity.findViewById(R.id.player5_name);
                break;
            case 6:
                player_entry = (TextView) activity.findViewById(R.id.player6_name);
                break;
            case 7:
                player_entry = (TextView) activity.findViewById(R.id.player7_name);
                break;
            case 8:
                player_entry = (TextView) activity.findViewById(R.id.player8_name);
                break;
        }

        if(id != 0) {
            setPlayer(name);
        }
    }
}
