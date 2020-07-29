/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Media implements Serializable {
    @SerializedName("mimeType")
    private String mimeType;
    @SerializedName("sizeStr")
    private String size;
    @SerializedName("momentStrShort")
    private String shortMonent;
    @SerializedName("timestamp")
    private double timestamp;
    @SerializedName("momentStr")
    private String moment;
    @SerializedName("path")
    private String path;

    private RecordingDevice device;

    public Media(String mimeType, String size, String shortMonent, double timestamp, String moment, String path, RecordingDevice device) {
        this.mimeType = mimeType;
        this.size = size;
        this.shortMonent = shortMonent;
        this.timestamp = timestamp;
        this.moment = moment;
        this.path = path;
        this.device = device;
    }

    public RecordingDevice getDevice() {
        return device;
    }

    public void setDevice(RecordingDevice device) {
        this.device = device;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getShortMonent() {
        return shortMonent;
    }

    public void setShortMonent(String shortMonent) {
        this.shortMonent = shortMonent;
    }

    public double getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(double timestamp) {
        this.timestamp = timestamp;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
