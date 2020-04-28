/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

public class ErrorResponse {
    @SerializedName("prompt")
    private boolean prompt;
    @SerializedName("error")
    private String error;

    public ErrorResponse(boolean prompt, String error) {
        this.prompt = prompt;
        this.error = error;
    }

    public boolean isPrompt() {
        return prompt;
    }

    public void setPrompt(boolean prompt) {
        this.prompt = prompt;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
