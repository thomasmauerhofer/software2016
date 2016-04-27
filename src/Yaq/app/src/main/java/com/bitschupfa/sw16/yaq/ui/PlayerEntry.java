package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;



class PlayerEntry {

    private TextView playerEntry = null;
    private int id = 0;

    PlayerEntry(Activity activity, int id) {
        setId(activity, id);
        this.id = id;
    }

    public void setPlayer(String name) {
        playerEntry.setText(name);
        playerEntry.setTextColor(Color.parseColor("#1F3255"));
        playerEntry.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_player, 0, 0, 0);
    }

    public void removePlayer() {
        playerEntry.setText(R.string.player);
        String text = playerEntry.getText().toString()+ " " + String.valueOf(id);
        playerEntry.setText(text);
        playerEntry.setTextColor(ContextCompat.getColor(playerEntry.getContext(), R.color.player_table_inactive));
        playerEntry.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.icon_player_black, 0, 0, 0);
    }

    public String getName() {
        return playerEntry.getText().toString();
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
                playerEntry = (TextView) activity.findViewById(R.id.player1_name);
                break;
            case 2:
                playerEntry = (TextView) activity.findViewById(R.id.player2_name);
                break;
            case 3:
                playerEntry = (TextView) activity.findViewById(R.id.player3_name);
                break;
            case 4:
                playerEntry = (TextView) activity.findViewById(R.id.player4_name);
                break;
            case 5:
                playerEntry = (TextView) activity.findViewById(R.id.player5_name);
                break;
            case 6:
                playerEntry = (TextView) activity.findViewById(R.id.player6_name);
                break;
            case 7:
                playerEntry = (TextView) activity.findViewById(R.id.player7_name);
                break;
            case 8:
                playerEntry = (TextView) activity.findViewById(R.id.player8_name);
                break;
        }

        if(id != 0) {
            setPlayer(name);
        }
    }
}
