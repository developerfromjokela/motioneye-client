/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import android.net.wifi.WifiConfiguration;

public class WifiNetwork {

    private WifiConfiguration configuration;
    private boolean selected;

    public WifiNetwork(WifiConfiguration configuration, boolean selected) {
        this.configuration = configuration;
        this.selected = selected;
    }


    public WifiConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(WifiConfiguration configuration) {
        this.configuration = configuration;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
