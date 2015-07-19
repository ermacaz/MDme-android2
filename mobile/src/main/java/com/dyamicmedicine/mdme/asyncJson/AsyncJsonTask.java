package com.dyamicmedicine.mdme.asyncJson;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 7/16/15
 * Copyright:: Copyright (c) 2014 MDme
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class AsyncJsonTask extends AsyncTask<String, Void, JSONObject> {
    private static final String TAG = "AsyncJsonTask";
    protected Context context;
    private ProgressDialog progressDialog = null;
    private int requestRetries = 0;
    private int timeoutConnect = 0;
    private int timeoutRead    = 0;
    private String loadingTitle   = "";
    private String jsonSuccess    = "success";
    private String jsonInfo       = "info";
    private String messageLoading = "Loading...";
    private String messageBusy    = "Server error, please try again.";
    private String messageError   = "Unknown error occurred, please try again.";



    public AsyncJsonTask(Context context) {
        this.context = context;
    }

    protected JSONObject getJsonFromUrl(String url) {
        JSONObject json = new JSONObject();
        int retries = this.requestRetries;
        try {
            try {
                json.put(this.jsonSuccess, false);
                json = AsyncJsonHelper.getJsonObjectFromUrl(url, this.timeoutConnect, this.timeoutRead);
            }
            catch (SocketTimeoutException e) {
                if (requestRetries-- > 0) {
                    json = getJsonFromUrl(url);
                }
                else {
                    json.put(this.jsonInfo, this.messageBusy);
                }
            }
            catch (Exception e) {
                if (retries-- > 0) {
                    json = getJsonFromUrl(url);
                }
                else {
                    Log.e(TAG, e.getMessage());
                    json.put(this.jsonInfo, this.messageError);
                }
            }
        }
        catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return json;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        return this.getJsonFromUrl(params[0]);
    }

    @Override
    protected void onPreExecute() {
        progressDialog = ProgressDialog.show(
                this.context, this.loadingTitle, this.messageLoading,
                true, true, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        AsyncJsonTask.this.cancel(true);
                    }
                }
        );
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        progressDialog = null;
    }

    protected void validateJson(JSONObject json) throws JSONException, IOException {
        if (json != null) {
            //valid json responses will have {success: true}
            if (json.getBoolean("success")) {
                return;
            }
            else {
                throw new IOException(json.getString("info"));
            }
        }
        else {
            throw new IOException(this.messageError);
        }
    }

    public void setConnectionParams(int timeoutConnect, int timeoutRead, int requestRetries) {
        this.timeoutConnect = timeoutConnect;
        this.timeoutRead = timeoutRead;
        this.requestRetries = requestRetries;
    }

    public String getLoadingTitle() {
        return loadingTitle;
    }

    public void setLoadingTitle(String loadingTitle) {
        this.loadingTitle = loadingTitle;
    }

    public String getMessageLoading() {
        return messageLoading;
    }

    public void setMessageLoading(String messageLoading) {
        this.messageLoading = messageLoading;
    }

    public String getMessageBusy() {
        return messageBusy;
    }

    public void setMessageBusy(String messageBusy) {
        this.messageBusy = messageBusy;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public int getTimeoutConnect() {
        return timeoutConnect;
    }

    public void setTimeoutConnect(int timeoutConnect) {
        this.timeoutConnect = timeoutConnect;
    }

    public int getTimeoutRead() {
        return timeoutRead;
    }

    public void setTimeoutRead(int timeoutRead) {
        this.timeoutRead = timeoutRead;
    }

    public int getRetryCount() {
        return requestRetries;
    }

    public void setRetryCount(int requestRetries) {
        this.requestRetries = requestRetries;
    }

    public String getJsonSuccess() {
        return jsonSuccess;
    }

    public void setJsonSuccess(String jsonSuccess) {
        this.jsonSuccess = jsonSuccess;
    }

    public String getJsonInfo() {
        return jsonInfo;
    }

    public void setJsonInfo(String jsonInfo) {
        this.jsonInfo = jsonInfo;
    }


}
