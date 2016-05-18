package com.bitschupfa.sw16.yaq.game;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.bitschupfa.sw16.yaq.communication.ConnectedClientDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;
import com.bitschupfa.sw16.yaq.game.Player;
import com.bitschupfa.sw16.yaq.game.PlayerList;
import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.ui.RankingItem;

import junit.framework.Assert;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class PlayerListTest {
    private PlayerList players = new PlayerList();

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void testAddPlayer() {
        PlayerList players = new PlayerList();

        String playerId = "1";
        ConnectedDevice dummyDevice = new ConnectedClientDevice(playerId);

        players.addPlayer(playerId, dummyDevice);

        Player player = players.getPlayer(playerId);
        assertNotNull(player);
        assertEquals(dummyDevice, player.getDevice());
    }


    @Test
    public void testAddRemoveSinglePlayer() {
        PlayerList players = new PlayerList();

        String playerId = "1";
        ConnectedDevice dummyDevice = new ConnectedClientDevice(playerId);

        players.addPlayer(playerId, dummyDevice);
        assertEquals(1, players.getNumberOfPlayers());

        players.removePlayer(playerId);
        assertEquals(0, players.getNumberOfPlayers());
    }

    @Test
    public void testAddMultiplePlayers() {
        int numberOfPlayers = 5;
        PlayerList players = new PlayerList();

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.addPlayer(Integer.toString(i), new ConnectedClientDevice(Integer.toString(i)));
            assertEquals(i, players.getNumberOfPlayers());
        }

        players.removePlayer("not valid id");
        assertEquals(numberOfPlayers, players.getNumberOfPlayers());

        players.removePlayer("3");
        assertEquals(numberOfPlayers - 1, players.getNumberOfPlayers());
    }

    @Test
    public void testMaxNumberOfPlayers() {
        expectedException.expect(IllegalStateException.class);

        int numberOfPlayers = 9;
        PlayerList players = new PlayerList();

        for (int i = 1; i <= numberOfPlayers; i++) {
            players.addPlayer(Integer.toString(i), new ConnectedClientDevice(Integer.toString(i)));
            assertEquals(i, players.getNumberOfPlayers());
        }
    }
}
