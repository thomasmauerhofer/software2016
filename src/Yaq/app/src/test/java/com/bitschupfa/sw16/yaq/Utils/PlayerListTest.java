package com.bitschupfa.sw16.yaq.Utils;

import com.bitschupfa.sw16.yaq.communication.ConnectedClientDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.game.PlayerList;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.ui.RankingItem;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PlayerListTest {
    private PlayerList players = new PlayerList();

    @Before
    public void setUp() throws Exception{
        ConnectedDevice device = new ConnectedClientDevice("Tom");
        players.addPlayer("Tom", device);
        players.addPlayer("Matthias", device);
        players.addPlayer("Manuel", device);
        players.addPlayer("Patrice", device);
        players.addPlayer("Johannes", device);

        players.getPlayer("Tom").setProfile(new PlayerProfile("Tom", ""));
        players.getPlayer("Matthias").setProfile(new PlayerProfile("Matthias", ""));
        players.getPlayer("Manuel").setProfile(new PlayerProfile("Manuel", ""));
        players.getPlayer("Patrice").setProfile(new PlayerProfile("Patrice", ""));
        players.getPlayer("Johannes").setProfile(new PlayerProfile("Johannes", ""));

        players.getPlayer("Tom").addScore(10);
        players.getPlayer("Matthias").addScore(-10);
        players.getPlayer("Manuel").addScore(0);
        players.getPlayer("Patrice").addScore(1);
        players.getPlayer("Johannes").addScore(-1);
        Assert.assertEquals(players.getNumberOfPlayers(), 5);
    }

    @Test
    public void removePlayerTest() {
        players.removePlayer("Tom");
        Assert.assertEquals(players.getNumberOfPlayers(), 4);
    }

    @Test
    public void resetScoresTest() {
        players.resetScores();
        for(RankingItem item :players.getSortedScoreList()) {
            Assert.assertEquals(item.getScore(), 0);
        }
    }

    @Test
    public void getSortedScoreListTest() {
        int highestScore = 100;
        for(RankingItem item :players.getSortedScoreList()) {
            Assert.assertTrue(highestScore > item.getScore());
        }
    }

    @Test
    public void clearTest() {
        players.clear();
        Assert.assertEquals(players.getNumberOfPlayers(), 0);
    }
}
