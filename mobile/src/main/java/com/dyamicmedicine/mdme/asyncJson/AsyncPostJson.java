package com.dyamicmedicine.mdme.asyncJson;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 7/22/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class AsyncPostJson extends AsyncJsonTask {
    String TAG;
    JSONObject params;
    public AsyncPostJson(Context context, String tag, JSONObject params) {
        super(context);
        this.TAG =tag;
        this.params = params;

    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        JSONObject json = new JSONObject();
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
            //set auth header if logged in
            SharedPreferences preferences = context.getSharedPreferences("CurrentUser", context.MODE_PRIVATE);
            String auth = preferences.getString("ApiToken", "UNAUTHED");
            if ((auth != null) && !(auth.equals("UNAUTHED"))) {
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Authorization", auth);
            }
            conn.connect();

            //set params
            oStream = new BufferedOutputStream(conn.getOutputStream());
            oStream.write(this.params.toString().getBytes("UTF-8"));
            oStream.flush();
            oStream.close();

            int responseCode = conn.getResponseCode();
            //unauthed
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Invalid credentials: Error " + responseCode);
            }

            //get and parse json response
            input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = input.readLine()) != null) {
                sb.append(line).append('\n');
            }
            String jsonStr = sb.toString();
            json = new JSONObject(jsonStr);
            json.put("success", true);
        }
        catch (Exception e) {
            try {
                json.put("success", false);
                json.put("message", e.getMessage());
            }
            catch (JSONException ex) {
                Log.e(TAG, ex.getMessage());
            }
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return json;
    }
}
