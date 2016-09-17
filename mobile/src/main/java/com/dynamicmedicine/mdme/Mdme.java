package com.dynamicmedicine.mdme;

import android.app.Application;

import java.util.List;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/13/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

class Mdme extends Application {
    private List<Clinic> storedClinics;
    public List<Clinic> getStoredClinics(){
        return storedClinics;
    }
}