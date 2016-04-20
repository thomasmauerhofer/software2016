package com.bitschupfa.sw16.yaq.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.bitschupfa.sw16.yaq.R;

import java.io.ByteArrayOutputStream;
import java.util.Random;


public class PlayerProfile {
    private static final String TAG = "PlayerProfile";
    private static final String PREF_FILE_NAME = "com.bitschupfa.sw16.yaq.PLAYER_PROFILE_PREF";
    private static final String PREF_PLAYER_NAME_KEY = "com.bitschupfa.sw16.yaq.PLAYER_NAME";
    private static final String PREF_PLAYER_AVATAR_KEY = "com.bitschupfa.sw16.yaq.PLAYER_AVATAR";

    private static PlayerProfile instance;

    private final Bitmap defaultAvatar;
    private final String[] randomPlayerNames;
    private final SharedPreferences preferences;


    private PlayerProfile(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        randomPlayerNames = new String[]{"Yak", "Jak", "Bos mutus", "Bos grunniens", "Grunzochse"};
        defaultAvatar = BitmapFactory.decodeResource(context.getResources(), R.mipmap.default_avatar);
    }

    public static PlayerProfile getInstance(Context context) {
        if (instance == null) {
            instance = new PlayerProfile(context);
        }

        return instance;
    }

    public String getPlayerName() {
        String name = preferences.getString(PREF_PLAYER_NAME_KEY, null);

        if (name == null) {
            int randomIndex = new Random().nextInt(randomPlayerNames.length);
            name = randomPlayerNames[randomIndex];
            setPlayerName(name);
        }

        return name;
    }

    public boolean setPlayerName(String name) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_PLAYER_NAME_KEY, name);
        return editor.commit();
    }

    public Bitmap getPlayerAvatar() {
        String encodedAvatar = preferences.getString(PREF_PLAYER_AVATAR_KEY, null);
        if (encodedAvatar == null) {
            setPlayerAvatar(defaultAvatar);
            return defaultAvatar;
        }

        return decodeBitmap(encodedAvatar);
    }

    public boolean setPlayerAvatar(Bitmap avatar) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_PLAYER_AVATAR_KEY, encodeBitmap(avatar));
        return editor.commit();
    }

    private String encodeBitmap(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    private Bitmap decodeBitmap(String string) {
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
