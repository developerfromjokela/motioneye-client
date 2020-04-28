/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import java.io.Serializable;

public class CameraUser implements Serializable {
    private String Username = "";
    private String Password = "";

    public CameraUser(String username, String password) {
        this.Username = username;
        this.Password = password;
    }

    public String getPassword() {
        return Password;
    }

    public String getUsername() {
        return Username;
    }

    public void setPassword(String password) {
        if (password == null)
            Password = "";
        else
            Password = password;
    }

    public void setUsername(String username) {
        if (username == null)
            Username = "";
        else
            Username = username;
    }
}
