package com.bitschupfa.sw16.yaq.Utils;

/**
 * Created by thomas on 22.03.16.
 */
public class RankingItem {

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
