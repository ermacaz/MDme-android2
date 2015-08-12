package com.dynamicmedicine.mdme;

import android.app.Activity;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dynamicmedicine.mdme.asyncJson.DownloadImageTask;

import org.w3c.dom.Text;

/**
 * MDme Android application
 * Author:: Matt Hamada (maito:ermacaz@gmail.com)
 * Created on:: 8/2/15
 * Copyright:: Copyright (c) 2015 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class CheckinActivity extends Activity {

    private final String TAG = "CheckinActivity";
    private Patient mPatient;
    private UpcomingAppointment mUpcomingAppointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
//        mPatient = Patient.getInstance(this);
//        mUpcomingAppointment = UpcomingAppointment.getInstance(this);
//        setPatientInfo();
//        setAppointmentInfo();
    }

    @Override
    protected  void onResume() {
        super.onResume();
        if (mPatient == null) {
            mPatient = Patient.getInstance(this);
            setPatientInfo();
        }
        if (mUpcomingAppointment == null) {
            mUpcomingAppointment = UpcomingAppointment.getInstance(this);
            setAppointmentInfo();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_checkin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setPatientInfo() {
        ImageView headerAvatarThumb = (ImageView)findViewById(R.id.header_avatar_thumb);
        new DownloadImageTask(headerAvatarThumb, TAG, this).execute(mPatient.getmAvatarThumbUrl());
        TextView headerFullName = (TextView)findViewById(R.id.header_full_name);
        headerFullName.setText(mPatient.getFullName());
        TextView headerBirthday = (TextView)findViewById(R.id.header_birthday);
        headerBirthday.setText(mPatient.getmBirthday());
    }

    private void setAppointmentInfo() {
        //gray area text
        TextView apptTimeTextView = (TextView)findViewById(R.id.checkin_appointment_time);
        SpannableString timeText = new SpannableString("Time: " + mUpcomingAppointment.getmTime());
        timeText.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 5, 0);
        timeText.setSpan(new ForegroundColorSpan(Color.GREEN), 6, 11, 0);
        apptTimeTextView.setText(timeText, TextView.BufferType.SPANNABLE);
        TextView apptLocationTextView = (TextView)findViewById(R.id.checkin_appointment_location);
        apptLocationTextView.setText(mUpcomingAppointment.getmClinicAddress());
        TextView apptDoctorName = (TextView)findViewById(R.id.checkin_doctor_name);
        apptDoctorName.setText(mUpcomingAppointment.getmDoctorName());
        //qr code stuff
        ImageView qrCodeImageView = (ImageView)findViewById(R.id.checkin_qr_code);
        new DownloadImageTask(qrCodeImageView, TAG, this).execute(mUpcomingAppointment.getQrCodeUrl());
    }
}
