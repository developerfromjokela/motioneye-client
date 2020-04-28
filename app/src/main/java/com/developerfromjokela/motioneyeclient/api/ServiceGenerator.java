/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder().create();
    private static OkHttpClient.Builder client = new OkHttpClient.Builder();


    public static <T> T createService(Class<T> serviceClass, String BASE_URL) {
        if (retrofit == null) {


            retrofit = new Retrofit.Builder()
                    .client(createOkHttpClient(false))
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit.create(serviceClass);
    }

    private static OkHttpClient createOkHttpClient(boolean canCache) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60 / 2, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(null)
                .build();
    }

}