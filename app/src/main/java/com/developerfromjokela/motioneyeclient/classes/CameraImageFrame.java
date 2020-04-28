/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import android.graphics.Bitmap;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


/**
 * This class is for video frame transmitting to the RecyclerView
 */
public class CameraImageFrame {
    private Camera camera;
    private Device device;
    private Bitmap bitmap;
    private boolean initialLoadDone = false;
    private String frameRateText = "";
    private CameraImageError error = null;
    private List<Long> times = new ArrayList<>();


    public CameraImageFrame(Camera camera, Device device, Bitmap bitmap, boolean initialLoadDone) {
        this.camera = camera;
        this.device = device;
        this.bitmap = bitmap;
        this.initialLoadDone = initialLoadDone;
    }

    public List<Long> getTimes() {
        return times;
    }

    public CameraImageError getError() {
        return error;
    }

    public void setError(CameraImageError error) {
        this.error = error;
    }

    public String getFrameRateText() {
        return frameRateText;
    }

    public void setFrameRateText(String frameRateText) {
        this.frameRateText = frameRateText;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isInitialLoadDone() {
        return initialLoadDone;
    }

    public void setInitialLoadDone(boolean initialLoadDone) {
        this.initialLoadDone = initialLoadDone;
    }
}
