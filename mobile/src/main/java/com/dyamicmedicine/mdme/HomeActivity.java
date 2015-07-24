package com.dyamicmedicine.mdme;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dyamicmedicine.mdme.asyncJson.AsyncGetJson;
import com.dyamicmedicine.mdme.asyncJson.DownloadImageTask;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private final static String  TAG = "HomeActivity";
    private String profileApiEndpoint;
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
    private ProgressBar mProgressBar;
    private Handler progressBarHandler = new Handler();
    int timeleft = 100;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        String userId = mPreferences.getString("patient_id", "-1");
        profileApiEndpoint = WebserverUrl.ROOT_URL + "/patients/" + userId + ".json";
        setTitle("John David Smith");
        attachViewWidgets();
        setButtonListeners();
        getProfileInfo();
        setupProgressBar();

    }

    private void setupProgressBar() {
        new Thread(new Runnable() {
          public void run() {
              while (timeleft > 0) {
                  try {
                      Thread.sleep(60000);
                  }
                  catch (InterruptedException e) {
                      e.printStackTrace();;
                  }
                  //update progress bar

              }
          }
        });

    }

    private void getProfileInfo() {
        GetProfileTask getProfileTask = new GetProfileTask(HomeActivity.this, TAG);
        getProfileTask.setMessageLoading("Loading profile...");
        getProfileTask.execute(profileApiEndpoint);
    }

    private void setButtonListeners() {
        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonFifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonSixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void attachViewWidgets() {
        mProfileImage = (ImageView)findViewById(R.id.profile_avatar);
        mProfileSex = (TextView)findViewById(R.id.profile_sex);
        mProfileDob = (TextView)findViewById(R.id.profile_birthday);
        mProfileName = (TextView)findViewById(R.id.profile_full_name);
        mProfileLocation = (TextView)findViewById(R.id.profile_location);
        mButtonFirst = (Button)findViewById(R.id.profile_button_first);
        mButtonSecond = (Button)findViewById(R.id.profile_button_second);
        mButtonThird = (Button)findViewById(R.id.profile_button_third);
        mButtonFourth = (Button)findViewById(R.id.profile_button_fourth);
        mButtonFifth = (Button)findViewById(R.id.profile_button_fifth);
        mButtonSixth = (Button)findViewById(R.id.profile_button_sixth);
        mProgressBar = (ProgressBar)findViewById(R.id.profile_progress_bar);
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

    private class GetProfileTask extends AsyncGetJson {
        public GetProfileTask(Context context, String tag) {
            super(context, tag);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getBoolean("success")) {
                    JSONObject patient = json.getJSONObject("patient");
                    mProfileName.setText(patient.getString("full_name"));
                    mProfileDob.setText("DOB: " + patient.getString("birthday_form_format"));
                    mProfileSex.setText(patient.getString("sex_humanize"));
                    mProfileLocation.setText(patient.getString("location"));
                    setTitle(patient.getString("full_name"));
                    //download image
                    new DownloadImageTask(mProfileImage, TAG).execute(WebserverUrl.ROOT_URL + patient.getString("avatar_medium_url"));
                }
                else {
                    Toast.makeText(this.context, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            finally {
                super.onPostExecute(json);
            }
        }
    }
}
