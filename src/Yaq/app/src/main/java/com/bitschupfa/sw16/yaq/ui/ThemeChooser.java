package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;

import com.bitschupfa.sw16.yaq.R;

/**
 * Created by Andrej on 04.06.2016.
 */
public class ThemeChooser {

    public final static int THEMEBLUE = 0;
    public final static int THEMEGREEN = 1;
    public final static int THEMEHELLOKITTY = 2;
    public final static int THEMETEAL = 3;

    private Activity activity;
    public ThemeStorage themeStorage;
    public Theme theme;


    public ThemeChooser(Activity activity){
        this.activity = activity;
        themeStorage = ThemeStorage.getInstance(activity);
    }

    public void setTheme(int themeID){
        switch (themeID){
            case THEMEBLUE:
                theme = new Theme(THEMEBLUE,
                        R.color.blue500,
                        R.color.blue900,
                        R.color.blue600,
                        "background_blue_grass",
                        "background_blue",
                        "background_blue_blue");
                setTheme(theme);

                break;
            case THEMEGREEN:
                theme = new Theme(THEMEGREEN,
                        R.color.greenlight500,
                        R.color.greenlight900,
                        R.color.greenlight600,
                        "background_green_grass",
                        "background_green",
                        "sidebar_animation_green");
                setTheme(theme);
                break;
            case THEMEHELLOKITTY:
                theme = new Theme(THEMEHELLOKITTY,
                        R.color.pink500,
                        R.color.pink900,
                        R.color.pink600,
                        "background_pink_grass",
                        "background_pink",
                        "sidebar_animation_pink" );
                setTheme(theme);
                break;
            case THEMETEAL:
                theme = new Theme(THEMETEAL,
                        R.color.teal500,
                        R.color.teal900,
                        R.color.teal600,
                        "background_teal_grass",
                        "background_teal",
                        "sidebar_animation_teal");
                setTheme(theme);
                break;
        }
    }

    private void setTheme(Theme theme){
        themeStorage.setTheme(theme);
    }

    public Theme getTheme(){
        return theme;
    }

    public ThemeStorage getThemeStorage(){
        return themeStorage;
    }
}
