package com.developerfromjokela.motioneyeclient;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class MotionEyeClient extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
