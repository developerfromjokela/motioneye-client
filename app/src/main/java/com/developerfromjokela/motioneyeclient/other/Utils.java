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

package com.developerfromjokela.motioneyeclient.other;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import org.jsoup.helper.StringUtil;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static android.app.DownloadManager.Request.NETWORK_WIFI;

public class Utils {

    public static final boolean DEBUG = true;

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
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getBSSID())) {
                bSSID = connectionInfo.getBSSID();
            }
        }
        return bSSID;
    }

    public static int getCurrentWifiNetworkId(Context context) {
        int networkId = -1;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null) {
                networkId = connectionInfo.getNetworkId();
            }
        }
        return networkId;
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
