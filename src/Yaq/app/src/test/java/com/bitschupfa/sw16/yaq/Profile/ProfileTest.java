package com.bitschupfa.sw16.yaq.Profile;

import android.graphics.Bitmap;

import com.bitschupfa.sw16.yaq.profile.PlayerProfile;
import com.bitschupfa.sw16.yaq.utils.BitmapUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;


@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE)
public class ProfileTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testCreatePlayerProfile() {
        String playerName = "Heine";
        String avatar = BitmapUtils.encodeBitmap(Bitmap.createBitmap(10, 10, Bitmap.Config.RGB_565));
        PlayerProfile profile = new PlayerProfile(playerName, avatar);

        assertEquals("Player name should not be mutated.", playerName, profile.getPlayerName());
        assertEquals("Encoded avatar should not be mutated.", avatar, profile.getEncodedAvatar());
    }

    @Test
    public void testInvalidPlayerName() {
        expectedException.expect(IllegalArgumentException.class);
        PlayerProfile profile = new PlayerProfile(null, null);
    }
}