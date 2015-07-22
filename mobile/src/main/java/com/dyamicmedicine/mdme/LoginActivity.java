package com.dyamicmedicine.mdme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dyamicmedicine.mdme.asyncJson.AsyncJsonTask;
import com.dyamicmedicine.mdme.asyncJson.AsyncPostJson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";
    private final static String LOGIN_API_ENDPOINT = WebserverUrl.ROOT_URL + "/sessions.json";
    private SharedPreferences mPreferences;
    private String mUserEmail;
    private String mUserPassword;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);

        //colors text field line - no way to do via xml in api < 21
        mEmailEditText = (EditText)findViewById(R.id.userEmail);
        mPasswordEditText = (EditText)findViewById(R.id.userPassword);
        mEmailEditText.getBackground().setColorFilter(getResources().getColor(R.color.MDme_primary), PorterDuff.Mode.SRC_ATOP);
        mPasswordEditText.getBackground().setColorFilter(getResources().getColor(R.color.MDme_primary), PorterDuff.Mode.SRC_ATOP);
    }

    public void login(View button){
        mUserEmail = mEmailEditText.getText().toString();
        mUserPassword = mPasswordEditText.getText().toString();
        if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_LONG).show();
        }
        else {
            JSONObject params = new JSONObject();
            try {
                params.put("email", mUserEmail);
                params.put("password", mUserPassword);
            }
            catch (JSONException e) {
                Log.e(TAG, e.getMessage());
            }
            LoginTask loginTask = new LoginTask(LoginActivity.this, TAG, params);
            loginTask.setMessageLoading("Logging in...");
            loginTask.execute(LOGIN_API_ENDPOINT);
        }
    }

    private class LoginTask extends AsyncPostJson {
        public LoginTask(Context context, String tag, JSONObject params) {
            super(context, tag, params);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getBoolean("success")) {
                    //login successfull
                    SharedPreferences.Editor editor = mPreferences.edit();
                    editor.putString("ApiToken", json.getJSONObject("api_token").getString("token"));
                    editor.putString("patient_id", json.getString("user_id"));
                    editor.apply();

                    //launch activity here
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
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
