package com.dyamicmedicine.mdme;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class LoginActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //colors text field line - no way to do via xml in api < 21
        EditText emailEditText = (EditText)findViewById(R.id.userEmail);
        EditText passwordEditText = (EditText)findViewById(R.id.userPassword);
        emailEditText.getBackground().setColorFilter(getResources().getColor(R.color.MDme_lightblue), PorterDuff.Mode.SRC_ATOP);
        passwordEditText.getBackground().setColorFilter(getResources().getColor(R.color.MDme_lightblue), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
