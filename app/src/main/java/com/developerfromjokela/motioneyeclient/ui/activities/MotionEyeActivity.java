/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;

public class MotionEyeActivity extends AppCompatActivity {

    public void snack(String content) {
        Snackbar.make(findViewById(android.R.id.content), content, 2000).show();
    }

    public void snack(String content, int duration) {
        Snackbar.make(findViewById(android.R.id.content), content, duration).show();
    }
}
