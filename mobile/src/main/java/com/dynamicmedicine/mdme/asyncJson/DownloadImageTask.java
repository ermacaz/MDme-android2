package com.dynamicmedicine.mdme.asyncJson;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.dynamicmedicine.mdme.WebserverUrl;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 7/23/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>  {

    ImageView bmImage;
    private String TAG;
    private Context context;

    public DownloadImageTask(ImageView bmImage, String tag, Context context) {
        this.bmImage = bmImage;
        this.context = context;
        this.TAG = tag;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = WebserverUrl.DOMAIN + urls[0];
        Bitmap picture = null;
        try {
            SharedPreferences preferences = context.getSharedPreferences("CurrentUser", context.MODE_PRIVATE);
            String auth = "Bearer " + preferences.getString("ApiToken", "");
            HttpURLConnection connection = (HttpURLConnection) new URL(urlDisplay).openConnection();
            connection.setRequestProperty("Authorization", auth);
            connection.setRequestMethod("GET");
            picture = BitmapFactory.decodeStream(connection.getInputStream());
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return picture;
    }
//    protected Bitmap doInBackground(String... urls) {
//        String urlDisplay = WebserverUrl.DOMAIN + urls[0];
//        Bitmap picture = null;
//        try {
//            SharedPreferences preferences = context.getSharedPreferences("CurrentUser", context.MODE_PRIVATE);
//            String auth = "Bearer " + preferences.getString("ApiToken", "");
//            InputStream in = new URL(urlDisplay).openStream();
//            picture = BitmapFactory.decodeStream(in);
//            in.close();
//        }
//        catch (Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//        return picture;
//    }


    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

