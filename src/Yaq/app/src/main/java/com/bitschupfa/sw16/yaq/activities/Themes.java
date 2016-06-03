package com.bitschupfa.sw16.yaq.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;

import java.util.ArrayList;
import java.util.List;

public class Themes extends AppCompatActivity {

    private TextView themeName;
    private List<String> themes;
    private ListView themesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        themeName = (TextView) findViewById(R.id.themePreviewTextView);
        themesListView = (ListView) findViewById(R.id.themesListView);

        themes = new ArrayList<String>();
        themes.add("Blue");
        themes.add("Teal");
        themes.add("Green");
        themes.add("Hello Kitty Special");

        ArrayAdapter<String> themeListAdapter = new ArrayAdapter<String>(this,
                R.layout.theme_list_row, R.id.themePreviewTextView, themes);

        themesListView.setAdapter(themeListAdapter);
    }
}
