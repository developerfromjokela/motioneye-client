/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import android.graphics.Bitmap;

public class CameraImage {
    private String fps = "0";
    private Bitmap bitmap = null;
    private boolean successful;
    private String errorString;


    public String getFps() {
        return fps;
    }

    public CameraImage(String fps, Bitmap bitmap, boolean successful) {
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
