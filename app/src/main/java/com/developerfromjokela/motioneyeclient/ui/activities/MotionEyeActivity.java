/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

public class MotionEyeActivity extends AppCompatActivity {

    public void snack(String content) {
        Snackbar.make(findViewById(android.R.id.content), content, 2000).show();
    }

    public void snack(String content, int duration) {
        Snackbar.make(findViewById(android.R.id.content), content, duration).show();
    }
}
