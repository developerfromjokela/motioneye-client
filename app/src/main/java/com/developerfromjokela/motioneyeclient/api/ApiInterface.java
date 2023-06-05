/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.api;


import com.developerfromjokela.motioneyeclient.classes.ActionStatus;
import com.developerfromjokela.motioneyeclient.classes.Cameras;
import com.developerfromjokela.motioneyeclient.classes.MainConfig;
import com.developerfromjokela.motioneyeclient.classes.Media;
import com.developerfromjokela.motioneyeclient.classes.MediaList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface ApiInterface {


    @GET()
    Call<MediaList> getMedia(@Url String url);

    @GET()
    Call<Cameras> getCameras(@Url String url);

    @POST()
    Call<okhttp3.ResponseBody> powerOff(@Url String url);

    @GET()
    Call<MainConfig> getMainConfig(@Url String url);

    @POST()
    Call<ActionStatus> peformAction(@Url String url);

    @GET()
    Call<okhttp3.ResponseBody> getMotionDetails(@Url String url);

    @FormUrlEncoded
    @POST()
    Call<okhttp3.ResponseBody> login(@Url String url, @Field("username") String username, @Field("password") String password);

    @POST()
    Call<okhttp3.ResponseBody> changeMainConfig(@Url String url, @Body RequestBody body);

    @GET()
    Call<okhttp3.ResponseBody> loginResult(@Url String url);
}