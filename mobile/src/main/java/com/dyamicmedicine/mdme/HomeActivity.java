package com.dyamicmedicine.mdme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private final static String  TAG = "HomeActivity";
    private SharedPreferences mPreferences;
    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView mProfileSex;
    private TextView mProfileDob;
    private TextView mProfileLocation;
    private Button mButtonFirst;
    private Button mButtonSecond;
    private Button mButtonThird;
    private Button mButtonFourth;
    private Button mButtonFifth;
    private Button mButtonSixth;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("John David Smith");
        attachViewWidgets();
    }

    private void attachViewWidgets() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
