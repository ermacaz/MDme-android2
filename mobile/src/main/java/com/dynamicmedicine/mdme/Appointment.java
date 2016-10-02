package com.dynamicmedicine.mdme;

import org.joda.time.DateTime;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 10/1/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class Appointment {
    private int id;
    private int clinicId;
    private int doctorId;
    private DateTime startTime;
    private DateTime endTime;
    private int duration;
    private String description;
    private String status;

    public Appointment () {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
