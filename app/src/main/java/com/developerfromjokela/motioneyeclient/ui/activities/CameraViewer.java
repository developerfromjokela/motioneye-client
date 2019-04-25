/*
 * Copyright (c) 2019. MotionEyeClient by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT;
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.ApiInterface;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.Cameras;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.database.Source;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.developerfromjokela.motioneyeclient.ui.adapters.HttpCamerasAdapter;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;

public class CameraViewer extends AppCompatActivity {

    private HttpCamerasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraviewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Source source = new Source(this);
        if (intent.getExtras() != null) {
            String ID = intent.getStringExtra("DeviceId");
            try {
                Device device = source.get(ID);
                setTitle(device.getDeviceName());
                Log.e(CameraViewer.class.getSimpleName(), new Gson().toJson(device));
                RecyclerView recyclerView = findViewById(R.id.cameras);
                recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
                adapter = new HttpCamerasAdapter(this, new HttpCamerasAdapter.CamerasAdapterListener() {
                    @Override
                    public void onImageClick(int position, Camera camera) {
                        Intent fullscreen = new Intent(CameraViewer.this, FullCameraViewer.class);
                        fullscreen.putExtra("DeviceId", ID);
                        fullscreen.putExtra("Camera", new Gson().toJson(camera));
                        startActivity(fullscreen);
                    }

                }, device);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                String baseurl;
                String serverurl;
                if (device.getDdnsURL().length() > 5) {
                    if ((Utils.getNetworkType(CameraViewer.this)) == NETWORK_MOBILE) {
                        serverurl = device.getDDNSUrlCombo();
                    } else if (device.getWlan().networkId == Utils.getCurrentWifiNetworkId(CameraViewer.this)) {
                        serverurl = device.getDeviceUrlCombo();

                    } else {
                        serverurl = device.getDDNSUrlCombo();

                    }
                } else {
                    serverurl = device.getDeviceUrlCombo();

                }
                Log.e("Setup", String.valueOf(serverurl.split("//").length));
                if (!serverurl.contains("://"))
                    baseurl = removeSlash("http://" + serverurl);
                else
                    baseurl = removeSlash(serverurl);
                String url = baseurl + "/config/list?_=" + String.valueOf(new Date().getTime());
                MotionEyeHelper helper = new MotionEyeHelper();
                helper.setUsername(device.getUser().getUsername());
                helper.setPasswordHash(device.getUser().getPassword());
                url = helper.addAuthParams("GET", url, "");
                ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);

                apiInterface.getCameras(url).enqueue(new Callback<Cameras>() {
                    @Override
                    public void onResponse(Call<Cameras> call, Response<Cameras> response) {
                        Cameras cameras = response.body();
                        device.setCameras(cameras.getCameras());
                        apiInterface.getMotionDetails(baseurl + "/version").enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.headers().get("Server").toLowerCase().contains("motioneye")) {
                                    try {
                                        final String stringResponse = response.body().string();
                                        Document html = Jsoup.parse(stringResponse);
                                        Elements elements = html.select("body");
                                        String lines[] = elements.html().replace("\"", "").replace("\n", "").split("<br>");
                                        for (String string : lines) {
                                            String[] paramParts = string.split("=");
                                            String paramName = paramParts[0].trim();
                                            String paramValue = paramParts[1];
                                            if (paramName.contains("hostname"))
                                                device.setDeviceName(paramValue);
                                            else if (paramName.contains("version"))
                                                device.setMotioneyeVersion(paramValue);
                                            else if (paramName.contains("motion_version"))
                                                device.setMotionVersion(paramValue);
                                            else if (paramName.contains("os_version"))
                                                device.setOsVersion(paramValue);
                                        }
                                        adapter.notifyDataSetChanged();
                                        setTitle(device.getDeviceName());

                                        source.editEntry(device);


                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Cameras> call, Throwable t) {

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null)
            adapter.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null)
            adapter.onDestroy();
    }

    private static String removeSlash(String url) {
        if (!url.endsWith("/"))
            return url;
        String[] parts = url.split("/");

        return parts[0];
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
