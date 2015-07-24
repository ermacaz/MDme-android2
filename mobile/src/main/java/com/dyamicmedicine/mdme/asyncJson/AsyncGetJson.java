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
public class AsyncGetJson extends AsyncJsonTask {
    String TAG;
    public AsyncGetJson(Context context, String tag) {
        super(context);
        this.TAG =tag;
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        JSONObject json = new JSONObject();
        HttpURLConnection conn = null;
        try {
            BufferedReader input;
            SharedPreferences preferences = context.getSharedPreferences("CurrentUser", context.MODE_PRIVATE);
            String auth = "Bearer " + preferences.getString("ApiToken", "");
            URL url = new URL(urls[0]);
            //configure request type / headers
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestProperty("Authorization", auth);
            conn.connect();

            int responseCode = conn.getResponseCode();
            //unauthed
            if (responseCode >= 400 && responseCode <= 499) {
                throw new Exception("Invalid credentials: Error " + responseCode);            }

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

