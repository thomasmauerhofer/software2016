package com.bitschupfa.sw16.yaq.activities;


import com.bitschupfa.sw16.yaq.profile.PlayerProfile;

public interface Lobby {
    PlayerProfile accessPlayerProfile();
    void updatePlayerList(String[] playerNames);
    void openGameActivity();
}
