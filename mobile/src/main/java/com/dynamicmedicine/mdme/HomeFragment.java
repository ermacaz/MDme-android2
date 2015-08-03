package com.dynamicmedicine.mdme;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
    private Patient    mPatient;


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
        profileApiEndpoint = "/patients/" + userId + ".json";
        attachViewWidgets();
        setButtonListeners();
        mPatient = Patient.getInstance(getActivity());
        applyProfileToViews();
        super.onViewCreated(view, savedInstanceState);
    }

    private void applyProfileToViews() {
        mProfileName.setText(mPatient.getFullName());
        mProfileDob.setText("DOB: " + mPatient.getmBirthday());
        mProfileSex.setText(mPatient.getmSex());
        mProfileLocation.setText(mPatient.getLocation());
        getActivity().setTitle(mPatient.getFullName());
        //download image
        new DownloadImageTask(mProfileImage, TAG).execute(mPatient.getmAvatarMediumUrl());
    }



//    private void getProfileInfo() {
//        GetProfileTask getProfileTask = new GetProfileTask(getActivity(), TAG);
//        getProfileTask.setMessageLoading("Loading profile...");
//        getProfileTask.execute(profileApiEndpoint);
//    }

    private void setButtonListeners() {
        mButtonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new AppointmentsHomeFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left, R.animator.slide_out_right,
                        R.animator.slide_in_to_right, R.animator.slide_out_to_left);
                transaction.replace(R.id.contentFragment, fragment).addToBackStack(TAG);
                transaction.commit();
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



}
