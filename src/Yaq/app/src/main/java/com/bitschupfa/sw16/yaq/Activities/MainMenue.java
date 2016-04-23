package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.MediaRouteActionProvider;
import android.support.v7.media.MediaRouter;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.Cast.CastHelper;
import com.bitschupfa.sw16.yaq.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMenue extends AppCompatActivity {

    private static final String TAG = MainMenue.class.getCanonicalName();
    private CastHelper castHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        castHelper = CastHelper.getInstance(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menue, menu);
        MenuItem mediaRouteMenuItem = menu.findItem(R.id.media_route_menu_item);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider) MenuItemCompat.getActionProvider(mediaRouteMenuItem);
        mediaRouteActionProvider.setRouteSelector(castHelper.mMediaRouteSelector);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        castHelper.addCallbacks();
    }

    @Override
    protected void onStop() {
        //castHelper.removeCallbacks();
        super.onStop();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            castHelper.sendMessage("Settings clicked");
            return true;
        } else if (id == R.id.menu_profile) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            castHelper.sendMessage("Profile clicked");
            return true;
        } else if (id == R.id.menu_manage) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            JSONObject json = new JSONObject();
            try {
                json.put("type", "question");
                json.put("question", "What is longer than the rest?");
                json.put("answer_1", "2000 metres");
                json.put("answer_2", "2 kilometres");
                json.put("answer_3", "1.24274 miles");
                json.put("answer_4", "1 light-year");
            } catch (JSONException e) {
                Log.d(TAG, e.getMessage());
            }
            castHelper.sendMessage(json.toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("UnusedParameters")
    public void hostButtonClicked(View view)
    {
        castHelper.sendMessage("Host");
        Intent intent = new Intent(MainMenue.this, Host.class);
        startActivity(intent);
    }

    @SuppressWarnings("UnusedParameters")
    public void joinButtonClicked(View view) {
        castHelper.sendMessage("Join");
        Intent intent = new Intent(MainMenue.this, Join.class);
        startActivity(intent);
    }
}

