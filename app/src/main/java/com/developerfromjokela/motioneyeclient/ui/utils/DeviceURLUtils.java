package com.developerfromjokela.motioneyeclient.ui.utils;

import android.content.Context;

import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.RecordingDevice;
import com.developerfromjokela.motioneyeclient.other.Utils;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;

public class DeviceURLUtils {

    public interface DeviceURLListener {
        void onOptimalURL(String serverURL);

        void onError(Exception e);
    }


    public static void getOptimalURLForRecordings(Context context, RecordingDevice device, DeviceRecordingsURLListener listener) {
        if (device.getDevice().getWlan() == null) {
            ConnectionUtils.isHostAvailable(device.getDevice().getDeviceUrl(), Integer.parseInt(device.getDevice().getLocalPort()), 2000, new ConnectionUtils.HostAvailabilityCheck() {
                @Override
                public void onResult(boolean available) {
                    try {
                        listener.onOptimalURL(basicURLLogic(context, device.getDevice(), available), device);
                    } catch (Exception e) {
                        listener.onError(e, device);
                    }
                }
            });
        } else {
            try {
                listener.onOptimalURL(basicURLLogic(context, device.getDevice(), false), device);
            } catch (Exception e) {
                listener.onError(e, device);
            }
        }

    }

    public static void getOptimalURL(Context context, Device device, DeviceURLListener listener) {
        if (device.getWlan() == null) {
            ConnectionUtils.isHostAvailable(device.getDeviceUrl(), Integer.parseInt(device.getLocalPort()), 2000, new ConnectionUtils.HostAvailabilityCheck() {
                @Override
                public void onResult(boolean available) {
                    try {
                        listener.onOptimalURL(basicURLLogic(context, device, available));
                    } catch (Exception e) {
                        listener.onError(e);
                    }
                }
            });
        } else {
            try {
                listener.onOptimalURL(basicURLLogic(context, device, false));
            } catch (Exception e) {
                listener.onError(e);
            }
        }

    }

    public interface DeviceRecordingsURLListener {
        void onOptimalURL(String serverURL, RecordingDevice device);

        void onError(Exception e, RecordingDevice device);
    }


    private static String basicURLLogic(Context context, Device device, boolean isLocalAvailable) {
        String serverUrl;
        if (isLocalAvailable)
            serverUrl = device.getDeviceUrlCombo();
        else {
            if (device.getDdnsURL().length() > 5) {
                if ((Utils.getNetworkType(context)) == NETWORK_MOBILE) {
                    serverUrl = device.getDDNSUrlCombo();
                } else if (device.getWlan() != null && device.getWlan().SSID.equals(Utils.getCurrentWifiNetworkId(context))) {
                    serverUrl = device.getDeviceUrlCombo();

                } else {
                    serverUrl = device.getDDNSUrlCombo();
                }
            } else {
                serverUrl = device.getDeviceUrlCombo();
            }
        }
        return serverUrl;
    }
}
