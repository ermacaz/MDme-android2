package com.dyamicmedicine.mdme.asyncJson;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 7/16/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class AsyncJsonHelper {

    public static JSONArray getJsonArrayFromResource(Context context, int resourceId) throws JSONException, IOException {
      InputStream data = context.getResources().openRawResource(resourceId);
      return new JSONArray(getStringFromInputStream(data));
    }

    public static JSONObject getJsonObjectFromResource(Context context, int resourceId) throws JSONException, IOException {
       InputStream data = context.getResources().openRawResource(resourceId);
        return new JSONObject(getStringFromInputStream(data));
    }

    public static JSONArray getJsonArrayFromUrl(String url) throws MalformedURLException, JSONException, IOException {
        return getJsonArrayFromUrl(url, 0, 0);
    }

    public static JSONArray getJsonArrayFromUrl(String url, int connectTimeout, int readTimeout) throws MalformedURLException, JSONException, IOException {
        return new JSONArray(getStringFromUrl(url, connectTimeout, readTimeout));
    }

    public static JSONObject getJsonObjectFromUrl(String url) throws MalformedURLException, JSONException, IOException {
        return getJsonObjectFromUrl(url, 0, 0);
    }

    public static JSONObject getJsonObjectFromUrl(String url, int connectTimeout, int readTimeout) throws MalformedURLException, JSONException, IOException {
        return new JSONObject(getStringFromUrl(url, connectTimeout, readTimeout));
    }

    //convert resource data into string
    private static String getStringFromInputStream(InputStream data) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(data));
        StringBuilder sBuilder = new StringBuilder();
        String line = "";
        while((line = br.readLine()) != null) {
            sBuilder.append(line);
        }
        return sBuilder.toString();
    }

    private static String getStringFromUrl(String url, int connectTimeout, int readTimeout) throws
            MalformedURLException, JSONException, IOException {
        URL urlObj = new URL(url);
        HttpURLConnection con = (HttpURLConnection)urlObj.openConnection();
        String jsonStr = "";
        if (connectTimeout != 0) {
            con.setConnectTimeout(connectTimeout);
        }
        if (readTimeout != 0) {
            con.setReadTimeout(readTimeout);
        }
        try {
            jsonStr = getStringFromInputStream(con.getInputStream());
        } finally {
            con.disconnect();
        }
        return jsonStr;
    }



}
