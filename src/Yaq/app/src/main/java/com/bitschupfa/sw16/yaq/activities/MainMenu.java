package com.bitschupfa.sw16.yaq.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;
import com.bitschupfa.sw16.yaq.database.helper.CatalogImporter;
import com.bitschupfa.sw16.yaq.database.object.QuestionCatalog;
import com.bitschupfa.sw16.yaq.profile.PlayerProfileStorage;

import java.io.File;
import java.io.IOException;
import io.realm.Realm;

public class MainMenu extends YaqActivity implements NavigationView.OnNavigationItemSelectedListener {

    public ImageView profilePicture;
    public TextView profileName;
    public RelativeLayout navigationDrawerHeader;
    public PlayerProfileStorage profileStorage;
    public NavigationView navigationView;
    public AnimationDrawable drawerBackgroundAnimation;
    private CatalogImporter importer;

    private static final int READ_REQUEST_CODE = 42;
    public static final int NAVIGATION_VIEW_HEADER_INDEX = 0;
    public static final int RESULT_FINISH = 99;

    private Realm realm;

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
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
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
        assert drawer != null;
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

        realm = Realm.getDefaultInstance();
        importer = new CatalogImporter(this, realm);

        importDemoFiles();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    protected void handleTheme() {
        super.handleTheme();

        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);
        if (logoImageView != null) {
            logoImageView.setBackground(getDrawableByName(themeChooser.getThemeStorage().getLogoImageName()));
        }

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

    @SuppressWarnings("UnusedParameters")
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
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_about) {
            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.about_dialog_title) + " V" + getResources().getString(R.string.versionName))
                    .setMessage(R.string.about_dialog_message)
                    .setIcon(R.drawable.ic_help_black_24dp)
                    .show();
        } else if (id == R.id.menu_themes) {
            Intent intent = new Intent(MainMenu.this, Themes.class);
            startActivityForResult(intent, RESULT_FINISH);
        } else if (id == R.id.menu_manage) {
            Intent intent = new Intent(MainMenu.this, ManageQuestions.class);
            startActivity(intent);
        } else if (id == R.id.menu_import){
            CatalogImporter importer = new CatalogImporter(this, realm);
            importer.importFile();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
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
        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();
                try {
                    Log.d("Import", "Uri ist: " + uri.toString());
                    importer.readFile(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(this, R.string.import_successul, Toast.LENGTH_SHORT).show();
        }
        else if ( requestCode == RESULT_FINISH){
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            importer.importFile();
        }
    }

    private void importDemoFiles() {
        if (realm.where(QuestionCatalog.class).findAll().size() == 0 ) {
            try {
                importer.readFile("Medizin.txt");
                importer.readFile("general.txt");
            } catch (IOException e) {
                Log.e(MainMenu.class.getCanonicalName(), "Error while importing file: " + e.getMessage());
            }
        }
    }
}