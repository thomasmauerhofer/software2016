package com.bitschupfa.sw16.yaq.Profile;

import android.content.Context;

import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ProfileStorageTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private PlayerProfileStorage profileStorage;

    @Before
    public void setup() {
        Context fakeContext = RuntimeEnvironment.application.getApplicationContext();
        profileStorage = PlayerProfileStorage.getInstance(fakeContext);
    }

    @Test
    public void testGetPlayerName() {
        // ensure that the shared preferences are empty at the beginning
        RuntimeEnvironment.application.getApplicationContext()
                .getSharedPreferences(PlayerProfileStorage.PREF_FILE_NAME, Context.MODE_PRIVATE)
                .edit().clear().commit();

        String playerName = profileStorage.getPlayerName();

        assertNotNull("The player name must be a String object and not NULL.", playerName);
        assertTrue("The first call should yield to a default player name.",
                Arrays.asList(profileStorage.getRandomPlayerNames()).contains(playerName));
        assertEquals("Once the player name was set it should not change anymore.",
                playerName, profileStorage.getPlayerName());
    }

    @Test
    public void testSetPlayerName() {
        String playerName = "Heine";
        boolean result = profileStorage.setPlayerName(playerName);

        assertTrue("A successful setting of the player name should return true.", result);
        assertEquals("The player name must now be in the shared preference storage.",
                playerName, profileStorage.getPlayerName());
        assertEquals("The player name must also be in the PlayerProfile object.",
                playerName, profileStorage.getPlayerProfile().getPlayerName());
    }

    @Test
    public void testSetInvalidPlayerName1() {
        expectedException.expect(IllegalArgumentException.class);
        profileStorage.setPlayerName("");
    }

    @Test
    public void testSetInvalidPlayerName2() {
        expectedException.expect(IllegalArgumentException.class);
        profileStorage.setPlayerName(null);
    }

    @Test
    public void testRandomPlayerNames() {
        assertTrue("There must be some default player names.",
                profileStorage.getRandomPlayerNames().length > 0);
    }
}