/*
 * Copyright (c) 2019. MotionEyeClient by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT;
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
