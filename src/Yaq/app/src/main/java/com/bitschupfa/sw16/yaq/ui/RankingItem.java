package com.bitschupfa.sw16.yaq.ui;

import java.io.Serializable;

public class RankingItem implements Serializable {

    private String name;

    private int score;

    public RankingItem(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
