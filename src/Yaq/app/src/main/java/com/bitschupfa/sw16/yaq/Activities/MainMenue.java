package com.bitschupfa.sw16.yaq.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bitschupfa.sw16.yaq.R;

public class MainMenue extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menue);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menue, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.menu_settings) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_profile) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.menu_manage) {
            Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hostButtonClicked(View view)
    {
        Intent intent = new Intent(MainMenue.this, Host.class);
        startActivity(intent);
    }

    public void joinButtonClicked(View view) {
        Intent intent = new Intent(MainMenue.this, Join.class);
        startActivity(intent);
    }
}
