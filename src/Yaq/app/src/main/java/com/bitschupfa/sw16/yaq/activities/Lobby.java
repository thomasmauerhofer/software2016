package com.bitschupfa.sw16.yaq.activities;


import android.app.Activity;

import com.bitschupfa.sw16.yaq.profile.PlayerProfile;

public interface Lobby {
    PlayerProfile accessPlayerProfile();

    void updatePlayerList(String[] playerNames);

    void openGameActivity();

    void handleFullGame();

    void quit();

    Activity getActivity();
}
