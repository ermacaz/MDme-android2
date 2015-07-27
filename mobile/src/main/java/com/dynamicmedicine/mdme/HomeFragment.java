package com.dynamicmedicine.mdme;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.IconButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dynamicmedicine.mdme.asyncJson.AsyncGetJson;
import com.dynamicmedicine.mdme.asyncJson.DownloadImageTask;

import org.json.JSONObject;


/**
 * MDme Android application
 * Author:: Matt Hamada (maito:ermacaz@gmail.com)
 * Created on:: 7/23/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
// TODO  http://developer.android.com/intl/ko/training/basics/fragments/creating.html
public class HomeFragment extends Fragment {

    private final static String  TAG = "HomeFragment";
    private String profileApiEndpoint;
    private SharedPreferences mPreferences;
    private ImageView mProfileImage;
    private TextView mProfileName;
    private TextView  mProfileSex;
    private TextView  mProfileDob;
    private TextView  mProfileLocation;
    private IconButton mButtonFirst;
    private IconButton mButtonSecond;
    private IconButton mButtonThird;
    private IconButton mButtonFourth;
    private IconButton mButtonFifth;
    private IconButton mButtonSixth;


    public HomeFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPreferences = getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String userId = mPreferences.getString("patient_id", "-1");
        profileApiEndpoint = WebserverUrl.ROOT_URL + "/patients/" + userId + ".json";
        attachViewWidgets();
        setButtonListeners();
        getProfileInfo();
        super.onViewCreated(view, savedInstanceState);
    }



    private void getProfileInfo() {
        GetProfileTask getProfileTask = new GetProfileTask(getActivity(), TAG);
        getProfileTask.setMessageLoading("Loading profile...");
        getProfileTask.execute(profileApiEndpoint);
    }

    private void setButtonListeners() {
        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonFifth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mButtonSixth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void attachViewWidgets() {
        mProfileImage    = (ImageView) getView().findViewById(R.id.profile_avatar);
        mProfileSex      = (TextView)  getView().findViewById(R.id.profile_sex);
        mProfileDob      = (TextView)  getView().findViewById(R.id.profile_birthday);
        mProfileName     = (TextView)  getView().findViewById(R.id.profile_full_name);
        mProfileLocation = (TextView)  getView().findViewById(R.id.profile_location);
        mButtonFirst     = (IconButton)    getView().findViewById(R.id.profile_button_first);
        mButtonSecond    = (IconButton)    getView().findViewById(R.id.profile_button_second);
        mButtonThird     = (IconButton)    getView().findViewById(R.id.profile_button_third);
        mButtonFourth    = (IconButton)    getView().findViewById(R.id.profile_button_fourth);
        mButtonFifth     = (IconButton)    getView().findViewById(R.id.profile_button_fifth);
        mButtonSixth     = (IconButton)    getView().findViewById(R.id.profile_button_sixth);
    }

    private class GetProfileTask extends AsyncGetJson {
        public GetProfileTask(Context context, String tag) {
            super(context, tag);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getBoolean("success")) {
                    JSONObject patient = json.getJSONObject("patient");
                    mProfileName.setText(patient.getString("full_name"));
                    mProfileDob.setText("DOB: " + patient.getString("birthday_form_format"));
                    mProfileSex.setText(patient.getString("sex_humanize"));
                    mProfileLocation.setText(patient.getString("location"));
                    getActivity().setTitle(patient.getString("full_name"));
                    //download image
                    new DownloadImageTask(mProfileImage, TAG).execute(WebserverUrl.ROOT_URL + patient.getString("avatar_medium_url"));
                }
                else {
                    Toast.makeText(this.context, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            finally {
                super.onPostExecute(json);
            }
        }
    }


}