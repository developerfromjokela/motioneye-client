/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import java.io.Serializable;

public class RecordingDevice implements Serializable {
    private Device device;
    private Camera camera;

    public RecordingDevice(Device device, Camera camera) {
        this.device = device;
        this.camera = camera;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public String getRecordingDeviceID() {
        return getDevice().getID() + getCamera().getId();
    }

    public String getRecordingDeviceName() {
        return getDevice().getDeviceName() + getCamera().getName();
    }
}
