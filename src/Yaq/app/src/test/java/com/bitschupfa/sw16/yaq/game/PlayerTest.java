package com.bitschupfa.sw16.yaq.game;

import com.bitschupfa.sw16.yaq.communication.ConnectedClientDevice;
import com.bitschupfa.sw16.yaq.communication.ConnectedDevice;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class PlayerTest {

    Player player = null;
    ConnectedDevice device = null;

    @Before
    public void setUp() throws Exception{
        device = new ConnectedClientDevice("Tom");
        player = new Player(device);
        Assert.assertEquals(player.getScore(), 0);
    }

    @Test
    public void scoreTest() {
        player.addScore(1);
        Assert.assertEquals(player.getScore(), 1);
        player.resetScore();
        Assert.assertEquals(player.getScore(), 0);
    }

    @Test
    public void compareToTest() {
        Player anotherPlayer = new Player(device);
        Assert.assertEquals(player.compareTo(anotherPlayer), 0);

        anotherPlayer.addScore(10);
        Assert.assertTrue(player.compareTo(anotherPlayer) < 0);

        player.addScore(20);
        Assert.assertTrue(player.compareTo(anotherPlayer) > 0);
    }
}
