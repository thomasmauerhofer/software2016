package com.bitschupfa.sw16.yaq.Utils;

import com.bitschupfa.sw16.yaq.utils.ScoreUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreUtilTest {

    private ScoreUtil scoreUtil = new ScoreUtil();
    private List<String> player = new ArrayList<>();

    @Before
    public void setUp() throws Exception{
        player.add("Tom");
        player.add("Matthias");
        player.add("Patrice");
        player.add("Manuel");
        player.add("Johannes");
        scoreUtil.init(player);
        checkInitialisation();
    }

    @Test
    public void testAddScore() {
        scoreUtil.addScoreForPlayer("Tom", 1);
        scoreUtil.addScoreForPlayer("Matthias", -1);

        Assert.assertEquals(scoreUtil.getCurrentPlayerScore("Tom"), 1);
        Assert.assertEquals(scoreUtil.getCurrentPlayerScore("Matthias"), -1);
    }

    @Test
    public void testGetSortedScoreList() {
        scoreUtil.addScoreForPlayer("Tom", 1);
        scoreUtil.addScoreForPlayer("Matthias", -1);
        scoreUtil.addScoreForPlayer("Patrice", 0);
        scoreUtil.addScoreForPlayer("Manuel", -10);
        scoreUtil.addScoreForPlayer("Johannes", 10);

        int currentMax = 100;
        Map<String, Integer> scores = scoreUtil.getSortedScoreList();

        for(Map.Entry<String, Integer> entry : scores.entrySet()) {
            int score = entry.getValue();
            Assert.assertTrue(currentMax > score);
            currentMax = score;
        }
    }

    private void checkInitialisation() {
        for(Map.Entry<String, Integer> entry : scoreUtil.getSortedScoreList().entrySet()) {
            Assert.assertEquals(entry.getValue().intValue(), 0);
        }
    }
}
