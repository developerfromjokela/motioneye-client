/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

public class CameraImageError {
    private String errorCode;
    private String errorCause;
    private boolean displayRetry = true;

    public CameraImageError(String errorCode, String errorCause, boolean displayRetry) {
        this.errorCode = errorCode;
        this.errorCause = errorCause;
        this.displayRetry = displayRetry;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCause() {
        return errorCause;
    }

    public void setErrorCause(String errorCause) {
        this.errorCause = errorCause;
    }

    public boolean isDisplayRetry() {
        return displayRetry;
    }

    public void setDisplayRetry(boolean displayRetry) {
        this.displayRetry = displayRetry;
    }
}
