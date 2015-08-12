package com.dynamicmedicine.mdme;

import android.content.Context;
import android.util.Log;

import com.dynamicmedicine.mdme.asyncJson.AsyncGetJson;

import org.json.JSONObject;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 7/28/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class UpcomingAppointment {

    private static final String TAG = "UpcomingApptSingleton";
    private static UpcomingAppointment mInstance = null;
    private static GetUpcomingAppointmentTask getUpcomingAppointmentTask;

    private String mId;
    private String mClinicAddress;
    private String mDoctorName;
    private String mDate;
    private String mTime;
    private int    mMinutesLeft;
    private int    mPercent;

    public static UpcomingAppointment getInstance(Context context) {
        JSONObject json = new JSONObject();
        if (mInstance == null) {
            getUpcomingAppointmentTask = new GetUpcomingAppointmentTask(context, TAG);
            getUpcomingAppointmentTask.setMessageLoading("Loading appointment...");
            try {
                json = getUpcomingAppointmentTask.execute("/patients/get-upcoming-appointment.json").get();
                if (json.getBoolean("success")) {
                    if (json.has("date")) { //json will only have success when no appt
                      mInstance = new UpcomingAppointment(
                              json.getString("id"),
                              json.getString("clinic_address"),
                              json.getString("doctor"),
                              json.getString("date"),
                              json.getString("time"),
                              json.getInt("minutesLeft"),
                              json.getInt("percent")
                      );
                    }
                    else {
                        mInstance = null;
                    }
                }
            }
            catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return mInstance;
    }

    private UpcomingAppointment(String mId, String mClinicAddress, String mDoctorName, String mDate, String mTime, int mMinutesLeft, int mPercent) {
        this.mId = mId;
        this.mClinicAddress = mClinicAddress;
        this.mDoctorName = mDoctorName;
        this.mDate = mDate;
        this.mTime = mTime;
        this.mMinutesLeft = mMinutesLeft;
        this.mPercent = mPercent;
    }

    public static UpcomingAppointment getmInstance() {
        return mInstance;
    }

    public static void setmInstance(UpcomingAppointment mInstance) {
        UpcomingAppointment.mInstance = mInstance;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmClinicAddress() {
        return mClinicAddress;
    }

    public void setmClinicAddress(String mClinicAddress) {
        this.mClinicAddress = mClinicAddress;
    }

    public String getmDoctorName() {
        return mDoctorName;
    }

    public void setmDoctorName(String mDoctorName) {
        this.mDoctorName = mDoctorName;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public int getmMinutesLeft() {
        return mMinutesLeft;
    }

    public void setmMinutesLeft(int mMinutesLeft) {
        this.mMinutesLeft = mMinutesLeft;
    }

    public int getmPercent() {
        return mPercent;
    }

    public void setmPercent(int mPercent) {
        this.mPercent = mPercent;
    }

    public String getQrCodeUrl() {
        return "/api/mobile/patients/" + this.mId + "/upcoming-appointment-qrcode.png";
    }

    private static class GetUpcomingAppointmentTask extends AsyncGetJson {
        public GetUpcomingAppointmentTask(Context context, String tag) { super(context, tag);}


    }
}
