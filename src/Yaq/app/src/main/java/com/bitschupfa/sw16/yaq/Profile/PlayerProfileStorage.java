package com.bitschupfa.sw16.yaq.Profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.Utils.BitmapUtils;

import java.util.Random;


public class PlayerProfileStorage {
    private static final String TAG = "PlayerProfileStorage";
    private static final String PREF_FILE_NAME = "com.bitschupfa.sw16.yaq.PLAYER_PROFILE_PREF";
    private static final String PREF_PLAYER_NAME_KEY = "com.bitschupfa.sw16.yaq.PLAYER_NAME";
    private static final String PREF_PLAYER_AVATAR_KEY = "com.bitschupfa.sw16.yaq.PLAYER_AVATAR";

    private static PlayerProfileStorage instance;

    private final Bitmap defaultAvatar;
    private final String[] randomPlayerNames;
    private final SharedPreferences preferences;


    private PlayerProfileStorage(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        randomPlayerNames = new String[]{"Yak", "Jak", "Bos mutus", "Bos grunniens", "Grunzochse"};
        defaultAvatar = BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_avatar);
    }

    public static PlayerProfileStorage getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerProfileStorage(context.getApplicationContext());
        }

        return instance;
    }

    public String getPlayerName() {
        String name = preferences.getString(PREF_PLAYER_NAME_KEY, null);
        if (name == null) {
            int randomIndex = new Random().nextInt(randomPlayerNames.length);
            name = randomPlayerNames[randomIndex];
            setPlayerName(name);
            Log.d(TAG, "No player name was set yet. Random choice was: '" + name + "'");
        }

        return name;
    }

    public boolean setPlayerName(String name) {
        Log.d(TAG, "Update player name to: '" + name + "'");

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_PLAYER_NAME_KEY, name);
        return editor.commit();
    }

    public Bitmap getPlayerAvatar() {
        String encodedAvatar = preferences.getString(PREF_PLAYER_AVATAR_KEY, null);
        if (encodedAvatar == null) {
            Log.d(TAG, "No player avatar set was yet. Set to default avatar");
            setPlayerAvatar(defaultAvatar);
            return defaultAvatar;
        }

        return BitmapUtils.decodeBitmap(encodedAvatar);
    }

    public boolean setPlayerAvatar(Bitmap avatar) {
        Log.d(TAG, "Update player avatar");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_PLAYER_AVATAR_KEY, BitmapUtils.encodeBitmap(avatar));
        return editor.commit();
    }

    public PlayerProfile getPlayerProfile() {
        return new PlayerProfile(getPlayerName(), BitmapUtils.encodeBitmap(getPlayerAvatar()));
    }
}
