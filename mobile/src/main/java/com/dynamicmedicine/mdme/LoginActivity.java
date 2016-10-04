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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
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
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            Window w = getWindow();
//            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
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

    public void login(View button) {
        String mUserEmail = mEmailEditText.getText().toString();
        String mUserPassword = mPasswordEditText.getText().toString();
        if (mUserEmail.length() == 0 || mUserPassword.length() == 0) {
            Toast.makeText(this, "Fields cannot be blank", Toast.LENGTH_LONG).show();
        }

        String latestTimestamp = null;
        try {
            ClinicDatabaseHandler clinicDb = new ClinicDatabaseHandler(LoginActivity.this);
            DateTime latestClinicUpdate = clinicDb.getLatestUpdateTime();
            clinicDb.close();
            ProcedureDatabaseHandler pdb = new ProcedureDatabaseHandler(LoginActivity.this);
            DateTime latestProcedureUpdate = pdb.getLatestUpdateTime();
            pdb.close();
            DateTime latestUpdate = null;
            if (latestClinicUpdate != null && latestProcedureUpdate != null) {
                latestUpdate = (latestProcedureUpdate.isAfter(latestClinicUpdate) ? latestProcedureUpdate : latestClinicUpdate);
            } else if (latestClinicUpdate != null) {
                latestUpdate = latestClinicUpdate;
            } else if (latestProcedureUpdate != null) {
                latestUpdate = latestProcedureUpdate;
            }
            DateTimeFormatter isoFormat = ISODateTimeFormat.dateTime();
            if (latestUpdate != null) {
                latestTimestamp = latestUpdate.toString(isoFormat);
            }
        } catch (Exception updateTimeE) {
           Log.e(TAG, updateTimeE.getMessage());
        }

        RequestParams params = new RequestParams();
        params.put("email", mUserEmail);
        params.put("password", mUserPassword);
        if (latestTimestamp != null) {
            params.put("clinic_update_time", latestTimestamp);
        }
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

                        //save clinics
                        JSONArray clinics = response.getJSONObject("data").getJSONArray("clinics");
                        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("h:mm aa");
                        if (clinics.length() > 0) { //only returns clinics if they have updated
                            ClinicDatabaseHandler clinicDb = new ClinicDatabaseHandler(LoginActivity.this);
                            ProcedureDatabaseHandler procedureDb = new ProcedureDatabaseHandler(LoginActivity.this);
                            for (int i = 0; i < clinics.length(); i++) {
                                Clinic clinic = new Clinic();
                                JSONObject jsonClinic = clinics.getJSONObject(i);
                                if (!jsonClinic.isNull("id"))
                                    clinic.setId(jsonClinic.getInt("id"));
                                if (!jsonClinic.isNull("name"))
                                    clinic.setName(jsonClinic.getString("name"));
                                if (!jsonClinic.isNull("address"))
                                    clinic.setAddress(jsonClinic.getString("address"));
                                if (!jsonClinic.isNull("city"))
                                    clinic.setCity(jsonClinic.getString("city"));
                                if (!jsonClinic.isNull("state"))
                                    clinic.setState(jsonClinic.getString("state"));
                                if (!jsonClinic.isNull("country"))
                                    clinic.setCountry(jsonClinic.getString("country"));
                                if (!jsonClinic.isNull("zipcode"))
                                    clinic.setZipcode(jsonClinic.getString("zipcode"));
                                if (!jsonClinic.isNull("phone_number"))
                                    clinic.setPhoneNumber(jsonClinic.getString("phone_number"));
                                if (!jsonClinic.isNull("fax_number"))
                                    clinic.setFaxNumber(jsonClinic.getString("fax_number"));
                                if (!jsonClinic.isNull("ne_latitude"))
                                    clinic.setNeLatitude(jsonClinic.getDouble("ne_latitude"));
                                if (!jsonClinic.isNull("latitude"))
                                    clinic.setLatitude(jsonClinic.getDouble("latitude"));
                                if (!jsonClinic.isNull("sw_latitude"))
                                    clinic.setSwLatitude(jsonClinic.getDouble("sw_latitude"));
                                if (!jsonClinic.isNull("longitude"))
                                    clinic.setLatitude(jsonClinic.getDouble("longitude"));
                                if (!jsonClinic.isNull("ne_longitude"))
                                    clinic.setNeLongitude(jsonClinic.getDouble("ne_longitude"));
                                if (!jsonClinic.isNull("sw_longitude"))
                                    clinic.setSwLongitude(jsonClinic.getDouble("sw_longitude"));
                                if (!jsonClinic.isNull("updated_at"))
                                    clinic.setUpdatedAt(DateTime.parse(jsonClinic.getString("updated_at")));
                                if (!jsonClinic.isNull("is_open_sunday"))
                                    clinic.setOpenSunday(jsonClinic.getBoolean("is_open_sunday"));
                                if (!jsonClinic.isNull("sunday_open_time"))
                                    clinic.setSundayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("sunday_open_time")));
                                if (!jsonClinic.isNull("sunday_close_time"))
                                    clinic.setSundayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("sunday_close_time")));
                                if (!jsonClinic.isNull("is_open_monday"))
                                    clinic.setOpenMonday(jsonClinic.getBoolean("is_open_monday"));
                                if (!jsonClinic.isNull("monday_open_time"))
                                    clinic.setMondayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("monday_open_time")));
                                if (!jsonClinic.isNull("monday_close_time"))
                                    clinic.setMondayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("monday_close_time")));
                                if (!jsonClinic.isNull("is_open_tuesday"))
                                    clinic.setOpenTuesday(jsonClinic.getBoolean("is_open_tuesday"));
                                if (!jsonClinic.isNull("tuesday_open_time"))
                                    clinic.setTuesdayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("tuesday_open_time")));
                                if (!jsonClinic.isNull("tuesday_close_time"))
                                    clinic.setTuesdayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("tuesday_close_time")));
                                if (!jsonClinic.isNull("is_open_wednesday"))
                                    clinic.setOpenWednesday(jsonClinic.getBoolean("is_open_wednesday"));
                                if (!jsonClinic.isNull("wednesday_open_time"))
                                    clinic.setWednesdayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("wednesday_open_time")));
                                if (!jsonClinic.isNull("wednesday_close_time"))
                                    clinic.setWednesdayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("wednesday_close_time")));
                                if (!jsonClinic.isNull("is_open_thursday"))
                                    clinic.setOpenThursday(jsonClinic.getBoolean("is_open_thursday"));
                                if (!jsonClinic.isNull("thursday_open_time"))
                                    clinic.setThursdayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("thursday_open_time")));
                                if (!jsonClinic.isNull("thursday_close_time"))
                                    clinic.setThursdayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("thursday_close_time")));
                                if (!jsonClinic.isNull("is_open_friday"))
                                    clinic.setOpenFriday(jsonClinic.getBoolean("is_open_friday"));
                                if (!jsonClinic.isNull("friday_open_time"))
                                    clinic.setFridayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("friday_open_time")));
                                if (!jsonClinic.isNull("friday_close_time"))
                                    clinic.setFridayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("friday_close_time")));
                                if (!jsonClinic.isNull("is_open_saturday"))
                                    clinic.setOpenSaturday(jsonClinic.getBoolean("is_open_saturday"));
                                if (!jsonClinic.isNull("saturday_open_time"))
                                    clinic.setSaturdayOpenTime(timeFormatter.parseLocalTime(jsonClinic.getString("saturday_open_time")));
                                if (!jsonClinic.isNull("saturday_close_time"))
                                    clinic.setSaturdayCloseTime(timeFormatter.parseLocalTime(jsonClinic.getString("saturday_close_time")));

                                if (clinicDb.clinicExists(clinic.getId())) {
                                    clinicDb.updateClinic(clinic);
                                } else {
                                    clinicDb.addClinic(clinic);
                                }
                                //get procedures
                                JSONArray jsonProcedures = jsonClinic.getJSONArray("clinic_procedures");
                                if (jsonProcedures.length() > 0) {
                                    for (int j = 0; j < jsonProcedures.length(); j++) {
                                        Procedure procedure = new Procedure();
                                        JSONObject jsonProcedure = jsonProcedures.getJSONObject(j);
                                        if (!jsonProcedure.isNull("id"))
                                            procedure.setId(jsonProcedure.getInt("id"));
                                        if (!jsonProcedure.isNull("name"))
                                            procedure.setName(jsonProcedure.getString("name"));
                                        if (!jsonProcedure.isNull("description"))
                                            procedure.setDescription(jsonProcedure.getString("description"));
                                        if (!jsonProcedure.isNull("duration"))
                                            procedure.setDuration(jsonProcedure.getInt("duration"));
                                        if (!jsonProcedure.isNull("updated_at"))
                                            procedure.setUpdatedAt(DateTime.parse(jsonClinic.getString("updated_at")));
                                        procedure.setClinicId(clinic.getId());
                                        if (procedureDb.procedureExists(procedure.getId())) {
                                            procedureDb.updateProcedure(procedure);
                                        } else {
                                            procedureDb.addProcedure(procedure);
                                        }
                                    }
                                }
                            }
                            clinicDb.close();
                        }

                        //launch activity here
                        Intent intent = new Intent(getApplicationContext(), HomeNavActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                try {
                    Toast.makeText(LoginActivity.this, response.getString("message"), Toast.LENGTH_LONG).show();
                } catch (Exception je) {
                    Toast.makeText(LoginActivity.this, "Unable to connect", Toast.LENGTH_LONG).show();
                    Log.e(TAG, je.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(LoginActivity.this, "Unable to connect", Toast.LENGTH_LONG).show();
                Log.d("Failed: ", ""+statusCode);
                Log.d("Error : ", "" + throwable);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
