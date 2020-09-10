/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.api;

import android.content.Context;
import android.util.Log;

import com.developerfromjokela.motioneyeclient.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Cache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder().create();
    private static OkHttpClient.Builder client = new OkHttpClient.Builder();
    public static HostnameVerifier motionEyeVerifier = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return hostname.equals(session.getPeerHost());
        }
    };


    public static <T> T createService(Class<T> serviceClass, String BASE_URL) throws NoSuchAlgorithmException {
        if (retrofit == null) {


            // example.com because we don't need baseurl, but it's required
            retrofit = new Retrofit.Builder()
                    .client(createOkHttpClientNoCache())
                    .baseUrl("http://example.com")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(serviceClass);
    }

    private static OkHttpClient createOkHttpClientNoCache() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (!BuildConfig.DEBUG)
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .hostnameVerifier(motionEyeVerifier)
                .build();
    }

    private static OkHttpClient createOkHttpClient(Context context, boolean enableCache) throws NoSuchAlgorithmException {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (!BuildConfig.DEBUG)
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        int cacheSize = 20 * 1024 * 1024; // 10 MiB
        okhttp3.Cache cache = new okhttp3.Cache(httpCacheDirectory, cacheSize);
        if (!enableCache)
            cache = null;
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(cache)
                .hostnameVerifier(motionEyeVerifier)
                .build();
    }

    public static Picasso getPicasso(Context context) throws NoSuchAlgorithmException {
        return new Picasso.Builder(context).downloader(new OkHttp3Downloader(ServiceGenerator.createOkHttpClient(context, true))).build();
    }

}