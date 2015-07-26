package com.dynamicmedicine.mdme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.dynamicmedicine.mdme.asyncJson.AsyncPostJson;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * MDme Android application
 * Author:: Matt Hamada (maito:ermacaz@gmail.com)
 * Created on:: 7/23/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class HomeActivity extends AppCompatActivity {

    //for gcm reg
    private final String TAG = "HomeActivity";

    private GoogleCloudMessaging gcm;
    private AtomicInteger msgID = new AtomicInteger();
    private Context context;
    private String SENDER_ID = "570465868788";
    private String regid;
    private SharedPreferences mPreferences;
    private String mAppVersion;
    private String deviceRegistrationUrl;
    private static final String PROPERTY_REG_ID = "registration_id";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private String patientId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mPreferences = getSharedPreferences("CurrentUser", MODE_PRIVATE);
        patientId = mPreferences.getString("patient_id", "-1");
        deviceRegistrationUrl = WebserverUrl.ROOT_URL +
                "/patients/" + patientId + "/devices";
        context = getApplicationContext();

        //get app version for gcm
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            mAppVersion = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.getMessage());
        }

        setupGooglePlayServices();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupGooglePlayServices();
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

    private void setupGooglePlayServices() {
        if (isGooglePlayAvailable()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);

            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                Log.w(TAG, "No valid google play services apk found");
            }
        }
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration id not found");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(mAppVersion, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "Application changed versions.");
            return "";
        }
        return registrationId;
    }

    /**
     * Will store gcm registration id in shared preferences
     * @param context application's context
     * @param regId registration id for gcm
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regID on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(mAppVersion, appVersion);
        editor.commit();
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    //get applications shared preferences for gcm key
    private SharedPreferences getGCMPreferences(Context context) {
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    private boolean isGooglePlayAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Log.i(TAG, "Device does not support google play services.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p>
     * Stores the registration ID and app versionCode in the application's
     * shared preferences.
     * Must be done with async as register() / unregister() are blocking
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device regsistered, registration ID=" + regid;

                    // You should send the registration ID to your server over HTTP,
                    // so it can use GCM/HTTP or CCS to send messages to your app.
                    // The request to your server should be authenticated if your app
                    // is using accounts.


                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error : " + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                sendRegistrationIdToBackend();
            }

        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        JSONObject params = new JSONObject();
        JSONObject device = new JSONObject();

        try {
            device.put("patient_id", patientId);
            device.put("platform", "android");
            device.put("token", regid);
            params.put("device", device);
            DeviceRegisterTask deviceTask = new DeviceRegisterTask(HomeActivity.this, TAG, params);
            deviceTask.setMessageLoading("Registering device...");
            deviceTask.execute(deviceRegistrationUrl);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(this, "Error registering device", Toast.LENGTH_SHORT).show();
        }

    }

    private class DeviceRegisterTask extends AsyncPostJson {
        public DeviceRegisterTask(Context context, String tag, JSONObject params) {
            super(context, tag, params);
        }

    }

}
