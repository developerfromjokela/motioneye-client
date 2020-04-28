/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

public class CheckIP {
    @SerializedName("prompt")

    private boolean prompt;

    public CheckIP(boolean prompt) {
        this.prompt = prompt;
    }

    public boolean isPrompt() {
        return prompt;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }
}
