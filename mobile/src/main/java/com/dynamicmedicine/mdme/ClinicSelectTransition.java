package com.dynamicmedicine.mdme;

import android.transition.ChangeBounds;
import android.transition.ChangeImageTransform;
import android.transition.ChangeTransform;
import android.transition.TransitionSet;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/18/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class ClinicSelectTransition extends TransitionSet {
    public ClinicSelectTransition() {
        setOrdering(ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform()).
                addTransition(new ChangeImageTransform());
    }
}
