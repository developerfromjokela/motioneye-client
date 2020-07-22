package com.developerfromjokela.motioneyeclient.ui.utils;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;

public class WifiInfoConverter {

    public static WifiConfiguration infoToConfiguration(WifiInfo wifiInfo) {
        WifiConfiguration configuration = new WifiConfiguration();
        configuration.SSID = wifiInfo.getSSID();
        configuration.hiddenSSID = wifiInfo.getHiddenSSID();
        return configuration;
    }
}
