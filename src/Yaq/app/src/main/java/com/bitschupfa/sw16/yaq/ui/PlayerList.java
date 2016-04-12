package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thomas on 18.03.16.
 */
public class PlayerList {

    private final Activity activity;

    private final List<PlayerEntry> players;

    public PlayerList(Activity activity) {
        this.activity = activity;
        players = new ArrayList<>();
    }

    public void addPlayer(String playerName) {
        PlayerEntry entry = new PlayerEntry(activity, players.size() + 1);
        entry.setPlayer(playerName);
        players.add(entry);
    }

    public void removePlayer(int id) {

        if(id == players.size()) {
            players.get(id - 1).removePlayer();
            return;
        } else {
            players.remove(id - 1);
        }

        for(int i = id - 1; i < players.size(); i++) {
            PlayerEntry entry = players.get(i);
            entry.setId(activity, entry.getId() - 1);
        }
    }

    public void removePlayerWithName(String name) {
        int id = getPlayerIdWithName(name);
        removePlayer(id);
    }

    public int getPlayerIdWithName(String name) {
        for(PlayerEntry entry : players) {
            if(entry.getName().equals(name)) {
                return entry.getId();
            }
        }
        return 0;
    }
}
