package com.dynamicmedicine.mdme;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dynamicmedicine.mdme.asyncJson.AsyncGetJson;

import org.json.JSONObject;


/**
 * MDme Android application
 * Author:: Matt Hamada (maito:ermacaz@gmail.com)
 * Created on:: 7/23/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class UpcomingAppointmentFragment extends Fragment {

    private final static String  TAG = "AppointmentFragment";
    private String upcomingApptApiEndpoint;
    private int    mAppointmentPercent;
    private int    mMinutesLeft;
    private SharedPreferences mPreferences;
    private ProgressBar mProgressBar;
    private Handler mBarHandler = new Handler();
    private boolean isAppointment;
    private TextView  mProfileAppointmentTime;
    private TextView  mProfileAppointmentTimeleft;



    public UpcomingAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming_appointment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mPreferences = getActivity().getSharedPreferences("CurrentUser", Context.MODE_PRIVATE);
        String userId = mPreferences.getString("patient_id", "-1");
        upcomingApptApiEndpoint = "/patients/get-upcoming-appointment.json";
        attachViewWidgets();
        isAppointment = false;
        getUpcomingAppt();
        super.onViewCreated(view, savedInstanceState);
    }

    private void attachViewWidgets() {
        mProfileAppointmentTime      = (TextView)    getView().findViewById(R.id.profile_appointment_time);
        mProfileAppointmentTimeleft  = (TextView)    getView().findViewById(R.id.profile_time_left);
        mProgressBar                 = (ProgressBar) getView().findViewById(R.id.profile_progress_bar);
    }

    private void getUpcomingAppt() {
        GetUpcomingApptTask getUpcomingApptTask = new GetUpcomingApptTask(getActivity(), TAG);
        getUpcomingApptTask.setMessageLoading("Loading profile...");
        getUpcomingApptTask.execute(upcomingApptApiEndpoint);
    }

    private void setupProgressBar() {

        new Thread(new Runnable() {
            public void run() {
                while (mAppointmentPercent <= 100) {
                    //update progress bar
                    mBarHandler.post(new Runnable() {
                        public void run() {
                            mProgressBar.setProgress(mAppointmentPercent);
                            mProfileAppointmentTimeleft.setText(Integer.toString(mMinutesLeft) + " " + getResources().getString(R.string.minutes_until_appointment));
                            if (mAppointmentPercent < 50) {
                                mProgressBar.getProgressDrawable().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                            }
                            else if (mAppointmentPercent >= 50 && mAppointmentPercent < 80) {
                                mProgressBar.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);

                            }
                            else if (mAppointmentPercent >= 20) {
                                mProgressBar.getProgressDrawable().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    });
                    //increment percent
                    //TODO sync this with real time in future.
                    try {
                        Thread.sleep(60000);
                        ++mAppointmentPercent;
                        --mMinutesLeft;
                    }
                    catch (InterruptedException e) {
                        Log.e(TAG, e.getMessage());
                    }

                }
            }
        }).start();

    }

    private class GetUpcomingApptTask extends AsyncGetJson {
        public GetUpcomingApptTask(Context context, String tag) { super(context, tag); }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getBoolean("success")) {

                    if (json.has("date")) { //json will only have success when no appt
                        mProfileAppointmentTime.setText(json.getString("date") + " " + json.getString("time"));
                        mMinutesLeft = json.getInt("minutesLeft");
                        mProfileAppointmentTimeleft.setText(json.getString("minutesLeft") + " " + getResources().getString(R.string.minutes_until_appointment));
                        mAppointmentPercent = json.getInt("percent");
                        setupProgressBar();
                    }
                    else {
                        mProgressBar.setVisibility(View.INVISIBLE);
                    }
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
