/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.classes;

import android.net.wifi.WifiConfiguration;

import com.developerfromjokela.motioneyeclient.ui.utils.URLUtils;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Device implements Serializable {
    private String ID;
    private String deviceName;
    private String deviceUrl;
    private String ddnsURL;
    private String localPort, DDNSPort;
    private CameraUser user;
    private String motioneyeVersion = "";
    private String motionVersion = "";
    private String osVersion = "";
    private ArrayList<Camera> cameras = new ArrayList<>();
    private WifiConfiguration wlan;


    public Device(String ID, String deviceName, String deviceUrl, String ddnsURL, String localPort, String DDNSPort, CameraUser user, String motioneyeVersion, String motionVersion, String osVersion, ArrayList<Camera> cameras, WifiConfiguration wlan) {
        this.ID = ID;
        this.deviceName = deviceName;
        this.deviceUrl = deviceUrl;
        this.ddnsURL = ddnsURL;
        this.localPort = localPort;
        this.DDNSPort = DDNSPort;
        this.user = user;
        this.motioneyeVersion = motioneyeVersion;
        this.motionVersion = motionVersion;
        this.osVersion = osVersion;
        this.cameras = cameras;
        this.wlan = wlan;
    }

    public String getDeviceUrlCombo() {

        if (localPort.length() > 0) {
            try {
                return URLUtils.setPort(deviceUrl, Integer.parseInt(localPort));
            } catch (Exception e) {
                e.printStackTrace();
                return deviceUrl + ":" + localPort;
            }
        }
        else
            return deviceUrl;
    }

    public String getDDNSUrlCombo() {
        if (DDNSPort.length() > 0) {
            try {
                return URLUtils.setPort(ddnsURL, Integer.parseInt(DDNSPort));
            } catch (Exception e) {
                e.printStackTrace();
                return ddnsURL + ":" + DDNSPort;
            }
        }
        else
            return ddnsURL;
    }

    public String getMotioneyeVersion() {
        return motioneyeVersion;
    }

    public void setMotioneyeVersion(String motioneyeVersion) {
        this.motioneyeVersion = motioneyeVersion;
    }

    public String getMotionVersion() {
        return motionVersion;
    }

    public void setMotionVersion(String motionVersion) {
        this.motionVersion = motionVersion;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getLocalPort() {
        return localPort;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }

    public String getDDNSPort() {
        return DDNSPort;
    }

    public void setDDNSPort(String DDNSPort) {
        this.DDNSPort = DDNSPort;
    }

    public void setUser(CameraUser user) {
        this.user = user;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public String getDdnsURL() {
        return ddnsURL;
    }

    public CameraUser getUser() {
        return user;
    }

    public String getID() {
        return ID;
    }

    public WifiConfiguration getWlan() {
        return wlan;
    }

    public String getPort() {
        return localPort;
    }

    public void setCameras(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }

    public void setPort(String port) {
        this.localPort = port;
    }

    public void setWlan(WifiConfiguration wlan) {
        this.wlan = wlan;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getDeviceUrl() {
        return deviceUrl;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setDeviceUrl(String deviceUrl) {
        this.deviceUrl = deviceUrl;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public List<Camera> getCamera() {
        return cameras;
    }

    public void setCamera(ArrayList<Camera> cameras) {
        this.cameras = cameras;
    }

    public void setDdnsURL(String ddnsURL) {
        this.ddnsURL = ddnsURL;
    }

    public static class Builder {
        private String ID = "";
        private String deviceName = "";
        private String deviceUrl = "";
        private CameraUser user;
        private String ddnsURL = "";
        private String localPort = "", DDNSPort = "";
        private String motioneyeVersion = "";
        private String motionVersion = "";
        private String osVersion = "";
        private WifiConfiguration wlan;
        private ArrayList<Camera> cameras = new ArrayList<>();


        public String getMotioneyeVersion() {
            return motioneyeVersion;
        }

        public void setMotioneyeVersion(String motioneyeVersion) {
            this.motioneyeVersion = motioneyeVersion;
        }

        public String getMotionVersion() {
            return motionVersion;
        }

        public void setMotionVersion(String motionVersion) {
            this.motionVersion = motionVersion;
        }

        public String getOsVersion() {
            return osVersion;
        }

        public void setOsVersion(String osVersion) {
            this.osVersion = osVersion;
        }

        public Builder setWlan(WifiConfiguration wlan) {
            this.wlan = wlan;
            return this;

        }

        public Builder setLocalPort(String port) {
            this.localPort = port;
            return this;

        }

        public Builder setDDNSPort(String DDNSPort) {
            this.DDNSPort = DDNSPort;
            return this;

        }

        public String getDeviceUrlCombo() {

            if (localPort.length() > 0) {
                try {
                    return URLUtils.setPort(deviceUrl, Integer.parseInt(localPort));
                } catch (Exception e) {
                    e.printStackTrace();
                    return deviceUrl + ":" + localPort;
                }
            }
            else
                return deviceUrl;
        }

        public String getDDNSUrlCombo() {
            if (DDNSPort.length() > 0) {
                try {
                    return URLUtils.setPort(ddnsURL, Integer.parseInt(DDNSPort));
                } catch (Exception e) {
                    e.printStackTrace();
                    return ddnsURL + ":" + DDNSPort;
                }
            }
            else
                return ddnsURL;
        }

        public void setCameras(ArrayList<Camera> cameras) {
            this.cameras = cameras;
        }

        public String getPort() {
            return localPort;
        }


        public Builder setDeviceName(String deviceName) {
            this.deviceName = deviceName;
            return this;
        }

        public Builder setDeviceUrl(String deviceUrl) {
            this.deviceUrl = deviceUrl;
            return this;
        }

        public Builder setID(String ID) {
            this.ID = ID;
            return this;
        }


        public Builder setCamera(ArrayList<Camera> cameras) {
            this.cameras = cameras;
            return this;
        }

        public Builder setUser(CameraUser user) {
            this.user = user;
            return this;
        }

        public Builder setDdnsURL(String ddnsURL) {
            this.ddnsURL = ddnsURL;
            return this;
        }

        public String getDdnsURL() {
            return ddnsURL;
        }

        public List<Camera> getCameras() {
            return cameras;
        }

        public String getDeviceUrl() {
            return deviceUrl;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public CameraUser getUser() {
            return user;
        }

        public String getID() {
            return ID;
        }

        public WifiConfiguration getWlan() {
            return wlan;
        }

        public String getDDNSPort() {
            return DDNSPort;
        }

        public String getLocalPort() {
            return localPort;
        }

        public Device build() {
            return new Device(ID, deviceName, deviceUrl, ddnsURL, localPort, DDNSPort, user, motioneyeVersion, motionVersion, osVersion, cameras, wlan);
        }
    }
}
