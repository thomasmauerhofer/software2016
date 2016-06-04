package com.bitschupfa.sw16.yaq.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.utils.ThemeListAdapter;

import java.util.ArrayList;
import java.util.List;

public class Themes extends YaqActivity {

    private List<String> themes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView themesListView = (ListView) findViewById(R.id.themesListView);

        initThemes();
        ThemeListAdapter themeListAdapter = new ThemeListAdapter(this,getThemeList());

        assert themesListView != null;
        themesListView.setAdapter(themeListAdapter);
        handleTheme();
    }

    @Override
    protected void handleTheme() {
        setBackgroundImage();
    }

    private void initThemes(){
        themes = new ArrayList<>();
        themes.add("Blue");
        themes.add("Green");
        themes.add("Hello Kitty Special");
        themes.add("Teal");
    }

    public List<String> getThemeList(){
        return themes;
    }

    public String getThemeNameLC(int index){
        return themes.get(index).toLowerCase().replace(" ","");
    }

}
