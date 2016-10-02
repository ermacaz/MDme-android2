package com.dynamicmedicine.mdme;


/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/27/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class Procedure {
    private int id;
    private int clinicId;
    private String name;
    private String description;
    private int duration;

    public Procedure() {

    }

    public String toString() {
        return this.name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
