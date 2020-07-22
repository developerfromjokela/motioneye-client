/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.other;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.View;

import org.jsoup.helper.StringUtil;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static android.app.DownloadManager.Request.NETWORK_WIFI;

public class Utils {

    public static final boolean DEBUG = true;
    public static int imageRefreshInterval = 15;
    public static int framerateFactor = 1;
    public static int fpsLen = 4;
    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    public static String removeSlash(String url) {
        if (!url.endsWith("/"))
            return url;
        String[] parts = url.split("/");

        return parts[0];
    }



    public static int dpToPx(Context c) {
        Resources r = c.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
    }


    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getCurrentWifiBSSID(Context context) {
        String bSSID = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getBSSID())) {
                bSSID = connectionInfo.getBSSID();
            }
        }
        return bSSID;
    }

    public static String getCurrentWifiNetworkId(Context context) {
        String networkId = "";
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                networkId = connectionInfo.getSSID();
            }
        }
        return networkId;
    }

    public static boolean validIP(String ip) {
        try {
            if (ip == null || ip.isEmpty()) {
                return false;
            }

            String[] parts = ip.split("\\.");
            if (parts.length != 4) {
                return false;
            }

            for (String s : parts) {
                int i = Integer.parseInt(s);
                if ((i < 0) || (i > 255)) {
                    return false;
                }
            }
            return !ip.endsWith(".");
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public static boolean isValidURL(String url) {

            boolean ret = true;

            if("".equals(url) || url==null)
            {
                ret = false;
            }else if(url.startsWith("-")||url.endsWith("-"))
            {
                ret = false;
            }else if(url.indexOf(".")==-1)
            {
                ret = false;
            }else
            {
                // Split domain into String array.
                String[] domainEle = url.split("\\.");
                int size = domainEle.length;
                // Loop in the domain String array.
                for(int i=0;i<size;i++)
                {
                    // If one domain part string is empty, then reutrn false.
                    String domainEleStr = domainEle[i];
                    if("".equals(domainEleStr.trim()))
                    {
                        return false;
                    }
                }

                // Get domain char array.
                char[] domainChar = url.toCharArray();
                size = domainChar.length;
                // Loop in the char array.
                for(int i=0;i<size;i++)
                {
                    // Get each char in the array.
                    char eleChar = domainChar[i];
                    String charStr = String.valueOf(eleChar);

                    // If char value is not a valid domain character then return false.
                    if(!".".equals(charStr) && !"-".equals(charStr) && !charStr.matches("[a-zA-Z]") && !charStr.matches("[0-9]"))
                    {
                        ret = false;
                        break;
                    }
                }
            }
            return ret;
    }

    public static int getNetworkType(Context context) {
        if (!isNetworkAvailable(context))
            return -1;
        ConnectivityManager conMan = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //mobile
        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
        //wifi
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();

        int result = 0;

        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            result |= NETWORK_MOBILE;
        }

        if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            result |= NETWORK_WIFI;
        }

        return result;
    }

    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
