/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Cameras {
    @SerializedName("cameras")

    private ArrayList<Camera> cameras;

    public Cameras(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }

    public ArrayList<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }

}
