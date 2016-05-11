package com.bitschupfa.sw16.yaq.utils;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScoreUtil {
    private Map<String, Integer> scores = new HashMap<>();

    public void init(Collection<String> playerIds) {
        scores.clear();
        for(String id : playerIds) {
            scores.put(id, 0);
        }
    }

    public void addScoreForPlayer(String id, int score) {
        score += getCurrentPlayerScore(id);
        scores.put(id, score);

    }

    public int getCurrentPlayerScore(String id) {
        return scores.get(id);
    }

    public Map<String, Integer> getSortedScoreList() {
        return MapUtil.sortByValue(scores);
    }
}
