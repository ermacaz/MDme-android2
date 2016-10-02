package com.dynamicmedicine.mdme;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * MDme Android application
 * Author:: ermacaz (maito:mattahamada@gmail.com)
 * Created on:: 9/22/16
 * Copyright:: Copyright (c) 2016 Dynamic Medicine, LLC
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential.
 */

public class ResizeAnimation extends Animation {

    private int startHeight;
    private int deltaHeight; // distance between start and end height
    private View view;

    /**
     * constructor, do not forget to use the setParams(int, int) method before
     * starting the animation
     *
     * @param v
     */
    public ResizeAnimation(View v) {
        this.view = v;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {

        view.getLayoutParams().height = (int) (startHeight + deltaHeight * interpolatedTime);
        view.requestLayout();
    }
    /**
     * set the starting and ending height for the resize animation
     * starting height is usually the views current height, the end height is the height
     * we want to reach after the animation is completed
     * @param start height in pixels
     * @param end height in pixels
     */
    public void setParams(int start, int end) {

        this.startHeight = start;
        deltaHeight = end - startHeight;
    }

    /**
     * set the duration for the hideshowanimation
     */
    @Override
    public void setDuration(long durationMillis) {
        super.setDuration(durationMillis);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}