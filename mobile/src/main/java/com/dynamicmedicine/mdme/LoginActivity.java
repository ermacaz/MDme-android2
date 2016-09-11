package com.dynamicmedicine.mdme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dynamicmedicine.mdme.asyncJson.AsyncPostJson;
import com.loopj.android.http.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import cz.msebera.android.httpclient.Header;


public class LoginActivity extends Activity {

    private final static String TAG = "LoginActivity";
    private final static String LOGIN_API_ENDPOINT = "/logins";
    private SharedPreferences mPreferences;
    private EditText mEmailEditText;
    private EditText mPasswordEditText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_login);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setTitle("Login");
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        //colors text field line - no way to do via xml in api < 21
        mEmailEditText = (EditText)findViewById(R.id.userEmail);
        mPasswordEditText = (EditText)findViewById(R.id.userPassword);
        mEmailEditText.getBackground().setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.MDme_loginInputBackground), PorterDuff.Mode.SRC_ATOP);
        mPasswordEditText.getBackground().setColorFilter(ContextCompat.getColor(LoginActivity.this, R.color.MDme_loginInputBackground), PorterDuff.Mode.SRC_ATOP);
    }

    public void login2(View button) {
        String mUserEmail = mEmailEditText.getText().toString();
        String mUserPassword = mPasswordEditText.getText().toString();
        if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_LONG).show();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("email", mUserEmail);
        params.put("password", mUserPassword);

        MdmeRestClient.post(LOGIN_API_ENDPOINT, params, new JsonHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.getBoolean("success")) {
                        //login successfull
                        SharedPreferences.Editor editor = mPreferences.edit();
                        editor.putString("ApiToken", response.getJSONObject("data").getJSONObject("api_token").getString("token"));
                        editor.putString("patient_id", response.getJSONObject("data").getString("user_id"));
                        editor.apply();

                        //launch activity here
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                try {
                    Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (JSONException je) {
                    Log.e(TAG, je.getMessage());
                }
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }



    public void login(View button){
        String mUserEmail = mEmailEditText.getText().toString();
        String mUserPassword = mPasswordEditText.getText().toString();
        if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_LONG).show();
        }
        else {
            JSONObject params = new JSONObject();
            try {
                params.put("email", mUserEmail);
                params.put("password", mUserPassword);
                LoginTask loginTask = new LoginTask(LoginActivity.this, TAG, params);
                loginTask.setMessageLoading("Logging in...");
                loginTask.execute(LOGIN_API_ENDPOINT);
            }
            catch (JSONException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this, "Error, please try again.", Toast.LENGTH_LONG).show();
            }
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
                    editor.putString("ApiToken", json.getJSONObject("data").getJSONObject("api_token").getString("token"));
                    editor.putString("patient_id", json.getJSONObject("data").getString("user_id"));
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
