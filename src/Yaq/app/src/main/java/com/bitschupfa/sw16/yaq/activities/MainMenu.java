package com.bitschupfa.sw16.yaq.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends YaqActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ImageView profilePicture;
    public TextView profileName;
    public RelativeLayout navigationDrawerHeader;
    public PlayerProfileStorage profileStorage;
    public NavigationView navigationView;
    public AnimationDrawable drawerBackgroundAnimation;

    public static final int NAVIGATION_VIEW_HEADER_INDEX = 0;
    public static final int RESULT_FINISH = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        profileStorage = PlayerProfileStorage.getInstance(this);
        profilePicture = (ImageView) navigationView.getHeaderView(NAVIGATION_VIEW_HEADER_INDEX).findViewById(R.id.profileImageView);
        navigationDrawerHeader = (RelativeLayout) navigationView.getHeaderView(NAVIGATION_VIEW_HEADER_INDEX).findViewById(R.id.navigation_drawer_header);
        profileName = (TextView) navigationView.getHeaderView(NAVIGATION_VIEW_HEADER_INDEX).findViewById(R.id.profileNameTextView);
        drawerBackgroundAnimation = (AnimationDrawable) navigationDrawerHeader.getBackground();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                drawerBackgroundAnimation.start();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                drawerBackgroundAnimation.stop();

            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationDrawerHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileAreaClicked(v);
            }
        });

        refreshProfileInNavigationHeader();
        handleTheme();
    }

    @Override
    protected void handleTheme() {
        List<Button> buttons = new ArrayList<>();

        buttons.add((Button) findViewById(R.id.host_btn));
        buttons.add((Button) findViewById(R.id.join_btn));

        styleButtons(buttons);

        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);
        logoImageView.setBackground(getDrawableByName(themeChooser.getThemeStorage().getLogoImageName())) ;

        setBackgroundImage();
        navigationDrawerHeader.setBackground(getDrawableByName(themeChooser.getThemeStorage().getNavigationDrawerImageName()));
        drawerBackgroundAnimation = (AnimationDrawable) navigationDrawerHeader.getBackground();

    }

    public void refreshProfileInNavigationHeader() {
        profilePicture.setImageBitmap(profileStorage.getPlayerAvatar());
        profileName.setText(profileStorage.getPlayerName());
    }

    @SuppressWarnings("UnusedParameters")
    public void hostButtonClicked(View view) {
        Intent intent = new Intent(MainMenu.this, Host.class);
        startActivity(intent);
    }

    public void profileAreaClicked(View view) {
        Intent intent = new Intent(MainMenu.this, Profile.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void joinButtonClicked(View view) {
        Intent intent = new Intent(MainMenu.this, Join.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.menu_about) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.about_dialog_title) + " V" + getResources().getString(R.string.versionName))
                    .setMessage(R.string.about_dialog_message)
                    .setIcon(R.drawable.ic_help_black_24dp)
                    .show();
        } else if (id == R.id.menu_themes) {
            Intent intent = new Intent(MainMenu.this, Themes.class);
            startActivityForResult(intent, RESULT_FINISH); ;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshProfileInNavigationHeader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        finish();
    }
}
