package com.dynamicmedicine.mdme;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/11/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.loopj.android.http.*;

public class MdmeRestClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(String url, RequestParams params, String token, AsyncHttpResponseHandler responseHandler) {
        client.addHeader("Authorization", "token: " + token);
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return WebserverUrl.ROOT_URL + relativeUrl;
    }
}
