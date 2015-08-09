package com.dynamicmedicine.mdme;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.dynamicmedicine.mdme.asyncJson.AsyncGetJson;

import org.json.JSONObject;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 7/27/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */
public class Patient {

    private static final String TAG = "PatientSingleton";
    private static Patient mInstance = null;

    private String mId;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mHomePhone;
    private String mWorkPhone;
    private String mMobilePhone;
    private String mAvatarMediumUrl;
    private String mAvatarThumbUrl;
    private String mSocialLastFour;
    private String mBirthday;
    private String mSex;
    private String mAddress1;
    private String mAddress2;
    private String mCity;
    private String mState;
    private String mCountry;
    private String mZipcode;
    private static GetProfileTask getProfileTask;

    public static Patient getInstance(Context context) {
        JSONObject json= new JSONObject();
        if (mInstance == null) {
            getProfileTask = new GetProfileTask(context, TAG);
            getProfileTask.setMessageLoading("Loading profile...");
            try {
                json = getProfileTask.execute("/patients/1.json").get();
                if (json.getBoolean("success")) {
                    JSONObject patient = json.getJSONObject("patient");
                    mInstance = new Patient(
                            patient.getString("id"),
                            patient.getString("first_name"),
                            patient.getString("last_name"),
                            patient.getString("email"),
                            patient.getString("home_phone"),
                            patient.getString("work_phone"),
                            patient.getString("mobile_phone"),
                            patient.getString("avatar_medium_url"),
                            patient.getString("avatar_thumb_url"),
                            patient.getString("social_last_four"),
                            patient.getString("birthday_form_format"),
                            patient.getString("sex_humanize"),
                            patient.getString("address1"),
                            patient.getString("address2"),
                            patient.getString("city"),
                            patient.getString("state"),
                            patient.getString("country"),
                            patient.getString("zipcode"));

                }
                else {
                    Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e ) {
                Log.e(TAG, e.getMessage());
            }
        }
        return mInstance;
    }

    private Patient(String mId, String mFirstName, String mLastName, String mEmail, String mHomePhone, String mWorkPhone, String mMobilePhone, String mAvatarMediumUrl, String mAvatarThumbUrl, String mSocialLastFour, String mBirthday, String mSex, String mAddress1, String mAddress2, String mCity, String mState, String mCountry, String mZipcode) {
        this.mId = mId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mEmail = mEmail;
        this.mHomePhone = mHomePhone;
        this.mWorkPhone = mWorkPhone;
        this.mMobilePhone = mMobilePhone;
        this.mAvatarMediumUrl = mAvatarMediumUrl;
        this.mAvatarThumbUrl = mAvatarThumbUrl;
        this.mSocialLastFour = mSocialLastFour;
        this.mBirthday = mBirthday;
        this.mSex = mSex;
        this.mAddress1 = mAddress1;
        this.mAddress2 = mAddress2;
        this.mCity = mCity;
        this.mState = mState;
        this.mCountry = mCountry;
        this.mZipcode = mZipcode;
    }

    public String getLocation() {
        if (this.mState == null) {
            return mCity + ", " + mCountry;
        }
        else {
            return mCity + ", " + mState;
        }
    }

    public String getFullName() {
        return mFirstName + " " + mLastName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmHomePhone() {
        return mHomePhone;
    }

    public void setmHomePhone(String mHomePhone) {
        this.mHomePhone = mHomePhone;
    }

    public String getmWorkPhone() {
        return mWorkPhone;
    }

    public void setmWorkPhone(String mWorkPhone) {
        this.mWorkPhone = mWorkPhone;
    }

    public String getmMobilePhone() {
        return mMobilePhone;
    }

    public void setmMobilePhone(String mMobilePhone) {
        this.mMobilePhone = mMobilePhone;
    }

    public String getmAvatarMediumUrl() {
        return mAvatarMediumUrl;
    }

    public void setmAvatarMediumUrl(String mAvatarMediumUrl) {
        this.mAvatarMediumUrl = mAvatarMediumUrl;
    }

    public String getmAvatarThumbUrl() {
        return mAvatarThumbUrl;
    }

    public void setmAvatarThumbUrl(String mAvatarThumbUrl) {
        this.mAvatarThumbUrl = mAvatarThumbUrl;
    }

    public String getmSocialLastFour() {
        return mSocialLastFour;
    }

    public void setmSocialLastFour(String mSocialLastFour) {
        this.mSocialLastFour = mSocialLastFour;
    }

    public String getmBirthday() {
        return mBirthday;
    }

    public void setmBirthday(String mBirthday) {
        this.mBirthday = mBirthday;
    }

    public String getmSex() {
        return mSex;
    }

    public void setmSex(String mSex) {
        this.mSex = mSex;
    }

    public String getmAddress1() {
        return mAddress1;
    }

    public void setmAddress1(String mAddress1) {
        this.mAddress1 = mAddress1;
    }

    public String getmAddress2() {
        return mAddress2;
    }

    public void setmAddress2(String mAddress2) {
        this.mAddress2 = mAddress2;
    }

    public String getmCity() {
        return mCity;
    }

    public void setmCity(String mCity) {
        this.mCity = mCity;
    }

    public String getmState() {
        return mState;
    }

    public void setmState(String mState) {
        this.mState = mState;
    }

    public String getmCountry() {
        return mCountry;
    }

    public void setmCountry(String mCountry) {
        this.mCountry = mCountry;
    }

    public String getmZipcode() {
        return mZipcode;
    }

    public void setmZipcode(String mZipcode) {
        this.mZipcode = mZipcode;
    }



    private static class GetProfileTask extends AsyncGetJson {
        public GetProfileTask(Context context, String tag) {
            super(context, tag);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                //moved to above with .get() so not async
//                if (json.getBoolean("success")) {
//                    JSONObject patient = json.getJSONObject("patient");
//                    mInstance = new Patient(
//                            patient.getString("id"),
//                            patient.getString("first_name"),
//                            patient.getString("last_name"),
//                            patient.getString("email"),
//                            patient.getString("home_phone"),
//                            patient.getString("work_phone"),
//                            patient.getString("mobile_phone"),
//                            patient.getString("avatar_medium_url"),
//                            patient.getString("avatar_thumb_url"),
//                            patient.getString("social_last_four"),
//                            patient.getString("birthday_form_format"),
//                            patient.getString("sex_humanize"),
//                            patient.getString("address1"),
//                            patient.getString("address2"),
//                            patient.getString("city"),
//                            patient.getString("state"),
//                            patient.getString("country"),
//                            patient.getString("zipcode"));
//
//                }
//                else {
//                    Toast.makeText(this.context, json.getString("message"), Toast.LENGTH_LONG).show();
//                }
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
