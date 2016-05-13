package com.bitschupfa.sw16.yaq.game;

import android.support.annotation.NonNull;

import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;


public class Player implements Comparable<Player>{
    private final ConnectedDevice device;
    private PlayerProfile profile;
    private int score;

    public Player(@NonNull ConnectedDevice device) {
        this.device = device;
        this.score = 0;
    }

    public void setProfile(PlayerProfile profile) {
        this.profile = profile;
    }

    public void addScore(int score) {
        this.score += score;
    }

    public ConnectedDevice getDevice() {
        return device;
    }

    public PlayerProfile getProfile() {
        return profile;
    }

    public int getScore() {
        return score;
    }

    public void resetScore() {
        this.score = 0;
    }

    @Override
    public int compareTo(@NonNull Player another) {
        return this.getScore() - another.getScore();
    }
}
