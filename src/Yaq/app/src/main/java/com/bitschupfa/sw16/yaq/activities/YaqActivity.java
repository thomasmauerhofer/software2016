package com.bitschupfa.sw16.yaq.activities;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.ui.ThemeChooser;

public abstract class YaqActivity extends AppCompatActivity {

    public ThemeChooser themeChooser;
    public int currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        themeChooser = new ThemeChooser(this);
        currentTheme = themeChooser.getThemeStorage().getThemeId();
        switch (currentTheme) {
            case ThemeChooser.THEME_BLUE:
                setTheme(R.style.AppTheme_Blue);
                break;
            case ThemeChooser.THEME_GREEN:
                setTheme(R.style.AppTheme_Green);
                break;
            case ThemeChooser.THEME_HELLOKITTY:
                setTheme(R.style.AppTheme_Pink);
                break;
            case ThemeChooser.THEME_TEAL:
                setTheme(R.style.AppTheme_Teal);
                break;
        }
    }

    protected void handleTheme() {
        setBackgroundImage();
    }

    protected Drawable getDrawableByName(String name) {
        int logoImageId = getResources().getIdentifier(name, "drawable",
                getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(logoImageId, getTheme());
        } else {
            return getResources().getDrawable(logoImageId);
        }
    }

    protected void setBackgroundImage() {
        ImageView backgroundImage = (ImageView) findViewById(R.id.backgroundImageView);
        if (backgroundImage != null)
            backgroundImage.setImageDrawable(getDrawableByName((themeChooser.getThemeStorage().getBackgroundImageName())));
    }
}
