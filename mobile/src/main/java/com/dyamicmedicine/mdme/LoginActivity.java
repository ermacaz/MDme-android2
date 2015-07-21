package com.dyamicmedicine.mdme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dyamicmedicine.mdme.asyncJson.AsyncJsonTask;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;


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
        mEmailEditText.getBackground().setColorFilter(getResources().getColor(R.color.MDme_lightblue), PorterDuff.Mode.SRC_ATOP);
        mPasswordEditText.getBackground().setColorFilter(getResources().getColor(R.color.MDme_lightblue), PorterDuff.Mode.SRC_ATOP);
    }

    public void login(View button){
        mUserEmail = mEmailEditText.getText().toString();
        mUserPassword = mPasswordEditText.getText().toString();
        if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_LONG).show();
            return;
        }
        else {
            LoginTask loginTask = new LoginTask(LoginActivity.this);
            loginTask.setMessageLoading("Logging in...");
            loginTask.execute(LOGIN_API_ENDPOINT);
        }
    }

    private class LoginTask extends AsyncJsonTask {
        public LoginTask(Context context) {
            super(context);
        }

        @Override
        protected JSONObject doInBackground(String... urls) {
            JSONObject json = null;
            HttpURLConnection conn = null;
            try {
                BufferedOutputStream oStream;
                BufferedReader input;
                URL url = new URL(urls[0]);
                //configure request type / headers
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.connect();
                //set params
                JSONObject params = new JSONObject();
                params.put("email", mUserEmail);
                params.put("password", mUserPassword);
                oStream = new BufferedOutputStream(conn.getOutputStream());
                oStream.write(params.toString().getBytes("UTF-8"));
                oStream.flush();
                oStream.close();

                input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = input.readLine()) != null) {
                    sb.append(line + '\n');
                }
                String jsonStr = sb.toString();
                json = new JSONObject(jsonStr);
            }
            //this gets called when login fails
            //TODO I DONT THINK I CAN TOST IN ASYNC
            catch (FileNotFoundException e) {
                Log.e(TAG, e.getMessage());
                Toast.makeText(this.context, "Invalid credentials.", Toast.LENGTH_SHORT).show();
            }
            catch (MalformedInputException e) {
                Log.e(TAG, e.getMessage());
            }
            catch (IOException e) {
                Toast.makeText(this.context, "Connection error.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
            }
            catch (JSONException e) {
                Toast.makeText(this.context, "Invalid characters entered.", Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.getMessage());
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            Toast.makeText(this.context, "Request complete", Toast.LENGTH_LONG).show();
            Log.e(TAG, json.toString());
        }

    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
