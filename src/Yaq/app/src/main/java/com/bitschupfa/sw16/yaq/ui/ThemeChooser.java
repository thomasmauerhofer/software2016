package com.bitschupfa.sw16.yaq.ui;

import android.app.Activity;

import com.bitschupfa.sw16.yaq.R;

/**
 * Created by Andrej on 04.06.2016.
 */
public class ThemeChooser {

    public final static int THEME_BLUE = 0;
    public final static int THEME_GREEN = 1;
    public final static int THEME_HELLOKITTY = 2;
    public final static int THEME_TEAL = 3;

    public final static String BACKGROUND_GREEN = "background_green_grass";
    public final static String LOGO_GREEN = "background_green";
    public final static String SIDEBAR_BACKGROUND_GREEN = "sidebar_animation_green";

    public final static String BACKGROUND_BLUE = "background_blue_grass";
    public final static String LOGO_BLUE = "background_blue";
    public final static String SIDEBAR_BACKGROUND_BLUE = "sidebar_animation_blue";

    public final static String BACKGROUND_PINK = "background_pink_grass";
    public final static String LOGO_PINK = "background_pink";
    public final static String SIDEBAR_BACKGROUND_PINK = "sidebar_animation_pink";

    public final static String BACKGROUND_TEAL = "background_teal_grass";
    public final static String LOGO_TEAL = "background_teal";
    public final static String SIDEBAR_BACKGROUND_TEAL = "sidebar_animation_teal";

    public ThemeStorage themeStorage;
    public Theme theme;

    public ThemeChooser(Activity activity){
        themeStorage = ThemeStorage.getInstance(activity);
    }

    public void setTheme(int themeID){
        switch (themeID){
            case THEME_BLUE:
                theme = new Theme(THEME_BLUE,
                        R.color.blue500,
                        R.color.blue900,
                        R.color.blue600,
                        BACKGROUND_BLUE,
                        LOGO_BLUE,
                        SIDEBAR_BACKGROUND_BLUE);
                setTheme(theme);

                break;
            case THEME_GREEN:
                theme = new Theme(THEME_GREEN,
                        R.color.greenlight500,
                        R.color.greenlight900,
                        R.color.greenlight600,
                        BACKGROUND_GREEN,
                        LOGO_GREEN,
                        SIDEBAR_BACKGROUND_GREEN);
                setTheme(theme);
                break;
            case THEME_HELLOKITTY:
                theme = new Theme(THEME_HELLOKITTY,
                        R.color.pink500,
                        R.color.pink900,
                        R.color.pink600,
                        BACKGROUND_PINK,
                        LOGO_PINK,
                        SIDEBAR_BACKGROUND_PINK);
                setTheme(theme);
                break;
            case THEME_TEAL:
                theme = new Theme(THEME_TEAL,
                        R.color.teal500,
                        R.color.teal900,
                        R.color.teal600,
                        BACKGROUND_TEAL,
                        LOGO_TEAL,
                        SIDEBAR_BACKGROUND_TEAL);
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
