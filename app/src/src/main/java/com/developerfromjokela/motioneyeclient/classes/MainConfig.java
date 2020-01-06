/*
 * Copyright (c) 2019. MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT:
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without restriction,including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *   The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *    SOFTWARE.
 */

package com.developerfromjokela.motioneyeclient.classes;

import com.google.gson.annotations.SerializedName;

public class MainConfig {

    @SerializedName("normal_password")
    private String normal_password;
    @SerializedName("normal_username")
    private String normal_username;
    @SerializedName("show_advanced")
    private boolean show_advanced;
    @SerializedName("admin_username")
    private String admin_username;
    @SerializedName("admin_password")
    private String admin_password;

    public MainConfig(String normal_password, String normal_username, boolean show_advanced, String admin_username, String admin_password) {
        this.normal_password = normal_password;
        this.normal_username = normal_username;
        this.show_advanced = show_advanced;
        this.admin_username = admin_username;
        this.admin_password = admin_password;
    }


    public String getNormal_password() {
        return normal_password;
    }

    public void setNormal_password(String normal_password) {
        this.normal_password = normal_password;
    }

    public String getNormal_username() {
        return normal_username;
    }

    public void setNormal_username(String normal_username) {
        this.normal_username = normal_username;
    }

    public boolean isShow_advanced() {
        return show_advanced;
    }

    public void setShow_advanced(boolean show_advanced) {
        this.show_advanced = show_advanced;
    }

    public String getAdmin_username() {
        return admin_username;
    }

    public void setAdmin_username(String admin_username) {
        this.admin_username = admin_username;
    }

    public String getAdmin_password() {
        return admin_password;
    }

    public void setAdmin_password(String admin_password) {
        this.admin_password = admin_password;
    }
}
