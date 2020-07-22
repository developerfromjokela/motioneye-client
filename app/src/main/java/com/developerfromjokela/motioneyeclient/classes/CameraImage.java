/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import android.graphics.Bitmap;

public class CameraImage {
    private int fps = 0;
    private Bitmap bitmap = null;
    private boolean successful;
    private String errorString;


    public int getFps() {
        return fps;
    }

    public CameraImage(int fps, Bitmap bitmap, boolean successful) {
        this.fps = fps;
        this.bitmap = bitmap;
        this.successful = successful;
    }

    public CameraImage(boolean successful, String errorString) {
        this.successful = successful;
        this.errorString = errorString;
    }

    public boolean isSuccessful() {
        return successful;
    }


    public String getErrorString() {
        return errorString;
    }


    public Bitmap getBitmap() {
        return bitmap;
    }
}
