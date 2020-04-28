/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MediaList {
    @SerializedName("mediaList")
    private List<Media> media;
    @SerializedName("cameraName")
    private String cameraName;

    public MediaList(List<Media> media, String cameraName) {
        this.media = media;
        this.cameraName = cameraName;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }
}
