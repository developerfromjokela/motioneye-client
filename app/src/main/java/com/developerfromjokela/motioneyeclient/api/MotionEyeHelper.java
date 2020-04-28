/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.api;


import android.os.Build;
import android.text.TextUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class MotionEyeHelper {


    private String username;
    private boolean loggedIn;
    private String passwordHash = "";
    private static String TAG = MotionEyeHelper.class.getSimpleName();

    public MotionEyeHelper() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) throws NoSuchAlgorithmException {
        this.passwordHash = sha1(passwordHash);
    }

    public String addAuthParams(String method, String url, String body) {
        if (url.indexOf('?') < 0) {
            url += '?';
        } else {
            url += '&';
        }

        url += "_username=" + username;
        if (loggedIn) {
            url += "&_login=true";
            loggedIn = false;
        }

        try {
            String signature = computeSignature(method, url, body);
            url += "&_signature=" + signature;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return url;
    }

    private String computeSignature(String method, String path, String body) throws NoSuchAlgorithmException {
        String basePath = removeAllAfterShash(qualifyPath(""));

        path = qualifyPath(path);

        TreeMap<String, String> query = new TreeMap<>(getQueryMap(path));
        path = removeAllAfterShash(qualifyPath(path));
        path = '/' + path.substring(getBaseUrl(basePath).length());

        // do what you have to do here
        // In your case, another loop.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            query.keySet().removeIf(key -> key.contains("_signature"));
        } else {
            Iterator<String> keyiterator = query.keySet().iterator();
            while (keyiterator.hasNext()) {
                String key = keyiterator.next();
                if (key.contains("_signature"))
                    keyiterator.remove();
            }
        }
        List<String> paramsStr = new ArrayList<>();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            StringBuilder builder = new StringBuilder();
            String key = entry.getKey();
            String value = entry.getValue();
            builder.append(key);
            builder.append("=");
            builder.append(value);
            paramsStr.add(builder.toString());
        }
        path = path + '?' + TextUtils.join("&", paramsStr.toArray(new String[]{}));
        path = path.replace("[^a-zA-Z0-9/?_.=&{}\\[\\]\":, -]", "-");

        body = body.replace("[^a-zA-Z0-9/?_.=&{}\\[\\]\":, -]", "-");
        if (body.isEmpty()) {
            body = "";
        }
        return sha1(method + ':' + path + ':' + body + ':' + passwordHash).toLowerCase();


    }

    private static String sha1(String input) throws NoSuchAlgorithmException {
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte aResult : result) {
            sb.append(Integer.toString((aResult & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    private static String qualifyPath(String path) {
        String url = qualifyUrl(path);
        int pos = url.indexOf("//");
        if (pos < 0) { // Not a Full URL
            return url;
        }

        url = url.substring(pos + 2);
        pos = url.indexOf('/');
        if (pos < 0) { /* root with no trailing slash */
            return "";
        }

        return url.substring(pos);
    }

    private static String qualifyUrl(String url) {
        String finalUrl;
        int start = url.indexOf("//");
        if (start < 0) {
            finalUrl = "http://" + url;
        } else {
            finalUrl = url.substring(start);
        }
        return finalUrl;
    }


    private static String removeAllAfterShash(String url) {
        String[] parts = url.split("\\?");

        return parts[0];
    }

    private static String getBaseUrl(String url) {
        String[] parts = url.split("\\?");

        String finalUrl = parts[0];

        if (!finalUrl.endsWith("/"))
            finalUrl = finalUrl + "/";

        return finalUrl;
    }


    private static Map<String, String> getQueryMap(String query) {
        List<String> params = new ArrayList<>(Arrays.asList(query.split("[&?]")));

        params.remove(0); // Removing Domain or something else which is not the Param
        Map<String, String> map = new HashMap<>();

        for (String param : params) {

            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }


}
