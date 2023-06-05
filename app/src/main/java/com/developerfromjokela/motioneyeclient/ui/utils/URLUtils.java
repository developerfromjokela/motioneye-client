/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class URLUtils {

    public static String setPort(String originalURL,
                                 int port)
            throws Exception {

        URI uri = new URI(originalURL);
        String scheme = uri.getScheme();
        if (scheme != null) {
            scheme = scheme.toLowerCase(Locale.US);
        } else {
            scheme = "http";
        }
        uri = new URI(scheme, uri.getUserInfo(), uri.getHost(), port,
                uri.getPath(), uri.getQuery(), uri.getFragment());

        return uri.toString();
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
            try {
                getDomainName(ip);
                return true;
            } catch (URISyntaxException e) {
                return false;
            }
        }
    }

    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        return uri.getHost();
    }
}
