package com.bitschupfa.sw16.yaq.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;

import com.bitschupfa.sw16.yaq.R;

/**
 * Created by Andrej on 04.06.2016.
 */
public class ThemeStorage {
    private final SharedPreferences preferences;
    public static final String PREF_FILE_NAME = "com.bitschupfa.sw16.yaq.PLAYER_THEME_PREF";
    public static final String PREF_THEME_KEY = "com.bitschupfa.sw16.yaq.THEME_ID_PREF";
    public static final String PREF_PRIMARY_COLOR_KEY = "com.bitschupfa.sw16.yaq.PRIMARY_THEME_COLOR_PREF";
    public static final String PREF_PRIMARY_COLOR_DARK_KEY = "com.bitschupfa.sw16.yaq.PRIMARY_THEME_COLOR_DARK_PREF";
    public static final String PREF_PRIMARY_COLOR_600_KEY = "com.bitschupfa.sw16.yaq.PRIMARY_THEME_COLOR_600_PREF";
    public static final String PREF_BACKGROUND_IMAGE_KEY = "com.bitschupfa.sw16.yaq.PRIMARY_BACKGROUND_IMAGE_PREF";
    public static final String PREF_LOGO_IMAGE_KEY = "com.bitschupfa.sw16.yaq.PRIMARY_LOGO_IMAGE_PREF";
    public static final String PREF_NAVIGATION_DRAWER_IMG_KEY = "com.bitschupfa.sw16.yaq.NAVIGATION_DRAWER_IMG_1_PREF";

    private static ThemeStorage instance;
    private Context context;


    private ThemeStorage(Context context) {
        preferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        this.context = context;

        setThemeId(ThemeChooser.THEME_BLUE);
        setPrimaryColor(ContextCompat.getColor(context, R.color.colorPrimary));
        setPrimaryColorDark(ContextCompat.getColor(context,R.color.colorPrimaryDark));
        setPrimaryColor600(ContextCompat.getColor(context,R.color.colorPrimary600));
        setBackgroundImageName(ThemeChooser.BACKGROUND_BLUE);
        setLogoImageName(ThemeChooser.LOGO_BLUE);
        setNavigationDrawerImageName(ThemeChooser.SIDEBAR_BACKGROUND_BLUE);
    }

    public static ThemeStorage getInstance(Context context) {
        if (instance == null) {
            instance = new ThemeStorage(context);
        }
        return instance;
    }

    public void setTheme(Theme theme){
        setThemeId(theme.getId());
        setPrimaryColor(ContextCompat.getColor(context,theme.getPrimaryColorId()));
        setPrimaryColorDark(ContextCompat.getColor(context,theme.getPrimaryColorDarkId()));
        setPrimaryColor600(ContextCompat.getColor(context,theme.getPrimaryColor600Id()));
        setBackgroundImageName(theme.getBackgroundImageName());
        setLogoImageName(theme.getLogoImageName());
        setNavigationDrawerImageName(theme.getNavigationDrawerImage1());
    }

    public boolean setPrimaryColor(int color) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_PRIMARY_COLOR_KEY, color);
        return editor.commit();
    }

    public int getPrimaryColor() {
        int color = preferences.getInt(PREF_PRIMARY_COLOR_KEY, 0);
        return color;
    }

    public boolean setPrimaryColorDark(int color) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_PRIMARY_COLOR_DARK_KEY, color);
        return editor.commit();
    }

    public int getPrimaryColorDark() {
        int color = preferences.getInt(PREF_PRIMARY_COLOR_DARK_KEY, 0);
        return color;
    }

    public boolean setPrimaryColor600(int color) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_PRIMARY_COLOR_600_KEY, color);
        return editor.commit();
    }

    public int getPrimaryColor600() {
        int color = preferences.getInt(PREF_PRIMARY_COLOR_600_KEY, 0);
        return color;
    }

    public boolean setBackgroundImageName(String name) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_BACKGROUND_IMAGE_KEY, name);
        return editor.commit();
    }

    public String getBackgroundImageName() {
        String name = preferences.getString(PREF_BACKGROUND_IMAGE_KEY, null);
        return name;
    }

    public boolean setLogoImageName(String name) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_LOGO_IMAGE_KEY, name);
        return editor.commit();
    }

    public String getLogoImageName() {
        String name = preferences.getString(PREF_LOGO_IMAGE_KEY, null);
        return name;
    }

    public boolean setNavigationDrawerImageName(String name) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_NAVIGATION_DRAWER_IMG_KEY, name);
        return editor.commit();
    }

    public String getNavigationDrawerImageName() {
        String name = preferences.getString(PREF_NAVIGATION_DRAWER_IMG_KEY, null);
        return name;
    }

    public boolean setThemeId(int id) throws IllegalArgumentException {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_THEME_KEY, id);
        return editor.commit();
    }

    public int getThemeId() {
        int id = preferences.getInt(PREF_THEME_KEY, 0);
        return id;
    }
}
