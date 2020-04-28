/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Range;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.ApiInterface;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.CameraImage;
import com.developerfromjokela.motioneyeclient.classes.CameraImageError;
import com.developerfromjokela.motioneyeclient.classes.CameraImageFrame;
import com.developerfromjokela.motioneyeclient.classes.Cameras;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.ErrorResponse;
import com.developerfromjokela.motioneyeclient.database.Source;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.developerfromjokela.motioneyeclient.ui.adapters.HttpCamerasAdapter;
import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static com.developerfromjokela.motioneyeclient.api.ServiceGenerator.motionEyeVerifier;

public class CameraViewer extends AppCompatActivity {

    private HttpCamerasAdapter adapter;
    private Device device;
    private String ID;
    private Source source;
    private List<CameraImageFrame> cameraImageFrames = new ArrayList<>();
    private boolean sleeping = false;
    private GridLayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraviewer);
        HttpsURLConnection.setDefaultHostnameVerifier(motionEyeVerifier);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        source = new Source(this);
        if (intent.getExtras() != null) {
            ID = intent.getStringExtra("DeviceId");
        } else {
            finish();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        sleeping = false;
        try {
            device = source.get(ID);

            setTitle(device.getDeviceName());
            Log.e(CameraViewer.class.getSimpleName(), new Gson().toJson(device));
            RecyclerView recyclerView = findViewById(R.id.cameras);
            manager = new GridLayoutManager(this, 1);
            recyclerView.setLayoutManager(manager);
            recyclerView.setItemAnimator(null);
            for (Camera camera : device.getCameras()) {
                CameraImageFrame frame = new CameraImageFrame(camera, device, null, false);
                cameraImageFrames.add(frame);
            }
            adapter = new HttpCamerasAdapter(new HttpCamerasAdapter.CamerasAdapterListener() {
                @Override
                public void onImageClick(int position, CameraImageFrame camera) {
                    Intent fullscreen = new Intent(CameraViewer.this, FullCameraViewer.class);
                    fullscreen.putExtra("DeviceId", ID);
                    fullscreen.putExtra("Camera", new Gson().toJson(camera.getCamera()));
                    startActivity(fullscreen);
                }

                @Override
                public void onRefreshRequest(int position, CameraImageFrame cameraImageFrame) {
                    getRunnableForCamera(position).run();
                }

            }, cameraImageFrames);
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
            String url = baseurl + "/config/list?_=" + new Date().getTime();
            MotionEyeHelper helper = new MotionEyeHelper();
            helper.setUsername(device.getUser().getUsername());
            helper.setPasswordHash(device.getUser().getPassword());
            url = helper.addAuthParams("GET", url, "");
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);

            apiInterface.getCameras(url).enqueue(new Callback<Cameras>() {
                @Override
                public void onResponse(Call<Cameras> call, Response<Cameras> response) {
                    Cameras cameras = response.body();
                    if (response.isSuccessful()) {
                        device.setCameras(cameras.getCameras());
                        apiInterface.getMotionDetails(baseurl + "/version").enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    try {
                                        final String stringResponse = response.body().string();
                                        Document html = Jsoup.parse(stringResponse);
                                        Elements elements = html.select("body");
                                        String[] lines = elements.html().replace("\"", "").replace("\n", "").split("<br>");
                                        for (String string : lines) {
                                            String[] paramParts = string.split("=");
                                            String paramName = paramParts[0].trim();
                                            String paramValue = paramParts[1];
                                            if (paramName.contains("hostname"))
                                                device.setDeviceName(paramValue);
                                            else if (paramName.contains("motion_version"))
                                                device.setMotionVersion(paramValue);
                                            else if (paramName.contains("os_version"))
                                                device.setOsVersion(paramValue);
                                            else if (paramName.equals("version"))
                                                device.setMotioneyeVersion(paramValue);

                                        }

                                        cameraImageFrames.clear();

                                        for (Camera camera : device.getCameras()) {
                                            CameraImageFrame frame = new CameraImageFrame(camera, device, null, false);
                                            cameraImageFrames.add(frame);
                                            getRunnableForCamera(cameraImageFrames.size() - 1).run();
                                        }

                                        adapter.notifyDataSetChanged();
                                        setTitle(device.getDeviceName());

                                        source.editEntry(device);


                                    } catch (IOException e) {
                                        for (CameraImageFrame cameraImageFrame : cameraImageFrames) {
                                            cameraImageFrame.setError(new CameraImageError("motioneye_error5", e.getMessage(), true));
                                        }
                                        adapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        for (CameraImageFrame cameraImageFrame : cameraImageFrames) {
                                            cameraImageFrame.setError(new CameraImageError("motioneye_error6", e.getMessage(), true));
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                            }


                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                for (CameraImageFrame cameraImageFrame : cameraImageFrames) {
                                    cameraImageFrame.setError(new CameraImageError("motioneye_error2", t.getMessage(), true));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                    } else {

                        ErrorResponse message = new Gson().fromJson(response.errorBody().charStream(), ErrorResponse.class);
                        for (CameraImageFrame cameraImageFrame : cameraImageFrames) {
                            cameraImageFrame.setError(new CameraImageError("motioneye_error4", message.getError(), true));
                        }
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<Cameras> call, Throwable t) {
                    for (CameraImageFrame cameraImageFrame : cameraImageFrames) {
                        cameraImageFrame.setError(new CameraImageError("motioneye_error3", t.getMessage(), true));
                    }
                    adapter.notifyDataSetChanged();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            for (CameraImageFrame cameraImageFrame : cameraImageFrames) {
                cameraImageFrame.setError(new CameraImageError("motioneye_error3", e.getMessage(), true));
            }
            adapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        sleeping = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sleeping = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.deviceSettings:
                startDeviceSettings(device.getID());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Runnable getRunnableForCamera(int position) {
        return new Runnable() {
            @Override
            public void run() {
                MotionEyeHelper helper = new MotionEyeHelper();
                helper.setUsername(device.getUser().getUsername());
                try {
                    helper.setPasswordHash(device.getUser().getPassword());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }


                CameraImageFrame frame = cameraImageFrames.get(position);
                String cameraId = frame.getCamera().getId();
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
                String baseurl;
                if (!serverurl.contains("://"))
                    baseurl = removeSlash("http://" + serverurl);
                else
                    baseurl = removeSlash(serverurl);

                String url = baseurl + "/picture/" + cameraId + "/current?_=" + new Date().getTime();
                url = helper.addAuthParams("GET", url, "");
                String finalUrl = url;
                boolean visible = position >= manager.findFirstVisibleItemPosition() && position <= manager.findLastVisibleItemPosition();
                if (!sleeping) {
                    if (visible)
                        new DownloadImageFromInternet(position, frame, this).execute(finalUrl);
                    if (!visible)
                        new Handler().postDelayed(this, Utils.imageRefreshInterval); //Start timer after 1 sec
                }

            }
        };
    }


    private class DownloadImageFromInternet extends AsyncTask<String, Void, CameraImage> {
        Runnable timerRunnable;
        CameraImageFrame camera;
        int position;
        Handler timeHandler;

        public DownloadImageFromInternet(int position, CameraImageFrame camera, Runnable timerRunnable) {
            this.camera = camera;
            this.timerRunnable = timerRunnable;
            this.position = position;
            timeHandler = new Handler();
        }

        protected void onPreExecute() {
        }

        protected CameraImage doInBackground(String... urls) {

            String imageURL = urls[0];


            try {
                URL url = new URL(imageURL);
                Map<String, List<String>> fps;
                if (imageURL.startsWith("https://")) {
                    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
                    connection.setHostnameVerifier(motionEyeVerifier);
                    fps = connection.getHeaderFields();
                } else {
                    URLConnection connection = url.openConnection();
                    fps = connection.getHeaderFields();
                }
                InputStream in = url.openStream();
                final Bitmap decoded = BitmapFactory.decodeStream(in);
                in.close();
                for (Map.Entry<String, List<String>> key : fps.entrySet()) {
                    for (String string : key.getValue()) {
                        if (string.contains("capture_fps")) {
                            double d = Double.parseDouble(string.split("capture_fps_" + camera.getCamera().getId() + "=")[1].split(";")[0].trim());
                            String humanReadableFPS = String.valueOf(Math.round((int) d));
                            return new CameraImage(humanReadableFPS, decoded, true);

                        }

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return new CameraImage(false, e.getMessage());
            }
            return new CameraImage(false, "Got no image from motionEye");

        }

        protected void onPostExecute(CameraImage result) {

            camera.setError(null);
            if (result.isSuccessful()) {
                if (!camera.isInitialLoadDone()) {
                    camera.setInitialLoadDone(true);
                }
                camera.setBitmap(result.getBitmap());
                List<Long> time = camera.getTimes();

                if (time.size() == Utils.fpsLen) {

                    long streamingFps = time.size() * 1000 / (time.get(time.size() - 1) - time.get(0));
                    int fpsDeliv = Math.round(streamingFps);
                    camera.setFrameRateText((fpsDeliv + "/" + result.getFps() + " fps"));

                }

                long timeNow = new Date().getTime();
                time.add(timeNow);
                if (time.size() > Utils.fpsLen) {
                    time.remove(0);
                }

                if (!isFinishing()) {
                    timeHandler.postDelayed(timerRunnable, Utils.imageRefreshInterval); //Start timer after 1 sec
                }

            } else {
                camera.setInitialLoadDone(false);
                camera.setError(new CameraImageError("motioneye_err1", result.getErrorString(), true));
            }

            adapter.notifyItemChanged(position);

        }
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

    private void startDeviceSettings(String deviceID) {
        Intent fullscreen = new Intent(CameraViewer.this, DeviceSettings.class);
        fullscreen.putExtra("DeviceId", deviceID);
        startActivityForResult(fullscreen, 5300);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5300)
            if (resultCode == RESULT_CANCELED)
                finish();
    }
}
