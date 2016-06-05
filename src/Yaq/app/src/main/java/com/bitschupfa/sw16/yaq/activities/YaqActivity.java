package com.bitschupfa.sw16.yaq.activities;

import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.ui.ThemeChooser;

import java.util.List;

public abstract class YaqActivity extends AppCompatActivity {

    public ThemeChooser themeChooser;
    public int currentTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        themeChooser = new ThemeChooser(this);
        currentTheme = themeChooser.getThemeStorage().getThemeId();
        switch(currentTheme){
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

    protected abstract void handleTheme();

    protected void styleButtons(List<Button> buttons){
        switch(currentTheme){
            case ThemeChooser.THEME_BLUE:
                styleButtonLogic(buttons,R.drawable.button_blue);
                break;
            case ThemeChooser.THEME_GREEN:
                styleButtonLogic(buttons,R.drawable.button_green);
                break;
            case ThemeChooser.THEME_HELLOKITTY:
                styleButtonLogic(buttons,R.drawable.button_pink);
                break;
            case ThemeChooser.THEME_TEAL:
                styleButtonLogic(buttons,R.drawable.button_teal);
                break;
        }
    }

    private void styleButtonLogic(List<Button> buttons, int drawableId){
        for(int i = 0; i < buttons.size();i++){
            buttons.get(i).setBackgroundResource(drawableId);
            if (Build.VERSION.SDK_INT >= 23) {
                buttons.get(i).setTextAppearance( R.style.text_button);
            }else{
                buttons.get(i).setTextAppearance(this, R.style.text_button);
            }
        }
    }

    protected Drawable getDrawableByName(String name){
        int logoImageId = getResources().getIdentifier(name,"drawable",
                getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return getResources().getDrawable(logoImageId, getTheme());
        }else{
            return getResources().getDrawable(logoImageId);
        }
    }

    protected void setBackgroundImage(){
        ImageView backgroundImage = (ImageView) findViewById(R.id.backgroundImageView);
        backgroundImage.setImageDrawable(getDrawableByName((themeChooser.getThemeStorage().getBackgroundImageName())));
    }

}
