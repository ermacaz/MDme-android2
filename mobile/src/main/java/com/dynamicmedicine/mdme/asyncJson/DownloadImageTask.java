package com.dynamicmedicine.mdme.asyncJson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
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
    public DownloadImageTask(ImageView bmImage, String tag) {
        this.bmImage = bmImage;
        this.TAG = tag;
    }

    protected Bitmap doInBackground(String... urls) {
        String urlDisplay = urls[0];
        Bitmap picture = null;
        try {
            InputStream in = new URL(urlDisplay).openStream();
            picture = BitmapFactory.decodeStream(in);
            in.close();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return picture;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}

