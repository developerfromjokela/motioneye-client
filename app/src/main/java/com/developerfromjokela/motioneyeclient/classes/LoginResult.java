/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

public class LoginResult {
    @SerializedName("prompt")

    private boolean prompt;

    @SerializedName("error")

    private String error = "no";

    public LoginResult(boolean prompt) {
        this.prompt = prompt;
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
