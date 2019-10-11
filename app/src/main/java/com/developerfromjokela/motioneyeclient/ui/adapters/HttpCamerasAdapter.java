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

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.CameraImage;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.other.Utils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;

public class HttpCamerasAdapter extends RecyclerView.Adapter<HttpCamerasAdapter.CamerasViewHolder> {

    private Context mContext;
    private CamerasAdapterListener listener;
    private Device device;

    private boolean attached = true;
    private Handler timerHandler = new Handler();


    public class CamerasViewHolder extends RecyclerView.ViewHolder {

        ImageView cameraImage;
        LinearLayout loadingBar;
        TextView fps, status;
        ProgressBar progressBar;
        Button tryagain;
        CardView itemCard;

        CamerasViewHolder(View itemView) {
            super(itemView);
            cameraImage = itemView.findViewById(R.id.cameraImage);
            loadingBar = itemView.findViewById(R.id.cameraBar);
            itemCard = itemView.findViewById(R.id.itemCard);
            fps = itemView.findViewById(R.id.fps);
            status = itemView.findViewById(R.id.status);
            progressBar = itemView.findViewById(R.id.progressar);
            tryagain = itemView.findViewById(R.id.tryagain);
        }
    }

    public HttpCamerasAdapter(Context mContext, CamerasAdapterListener listener, Device device) {
        this.mContext = mContext;
        this.listener = listener;

        this.device = device;

    }

    @Override
    public CamerasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera, parent, false);
        return new CamerasViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final HttpCamerasAdapter.CamerasViewHolder holder, final int position) {
        boolean loaded = false;
        final Camera camera = device.getCameras().get(position);
        holder.cameraImage.setId(Integer.valueOf(camera.getId()));
        holder.loadingBar.setId(Integer.valueOf(camera.getId()) + 4495);

        int framerate = Integer.valueOf(camera.getFramerate());
        List<Long> time = new ArrayList<>();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                // Here you can update your adapter data
                MotionEyeHelper helper = new MotionEyeHelper();
                helper.setUsername(device.getUser().getUsername());
                try {
                    helper.setPasswordHash(device.getUser().getPassword());
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                String cameraId = camera.getId();
                String serverurl;
                if (device.getDdnsURL().length() > 5) {
                    if ((Utils.getNetworkType(mContext)) == NETWORK_MOBILE) {
                        serverurl = device.getDDNSUrlCombo();
                    } else if (device.getWlan().networkId == Utils.getCurrentWifiNetworkId(mContext)) {
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

                new DownloadImageFromInternet(holder, camera, this, time, loaded).execute(finalUrl);


            }
        };

        holder.tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerHandler.postDelayed(timerRunnable, Utils.imageRefreshInterval); //Start timer after 1 sec

            }
        });
        timerHandler.postDelayed(timerRunnable, Utils.imageRefreshInterval); //Start timer after 1 sec

        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClick(position, camera);
            }
        });
    }

    public interface CamerasAdapterListener {

        void onImageClick(int position, Camera camera);
    }

    @Override
    public int getItemCount() {
        return device.getCameras().size();
    }

    private static String removeSlash(String url) {
        if (!url.endsWith("/"))
            return url;
        String[] parts = url.split("/");

        return parts[0];
    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, CameraImage> {
        ImageView imageView;
        LinearLayout progressBar;
        ProgressBar loading;
        TextView fps, status;
        Camera camera;
        boolean loaded;
        Runnable timerRunnable;
        Button tryagain;
        List<Long> time;

        public DownloadImageFromInternet(CamerasViewHolder viewholder, Camera camera, Runnable timerRunnable, List<Long> time, boolean loaded) {
            this.imageView = viewholder.cameraImage;
            this.progressBar = viewholder.loadingBar;
            this.loaded = loaded;
            this.fps = viewholder.fps;
            this.loading = viewholder.progressBar;
            this.status = viewholder.status;
            this.camera = camera;
            this.timerRunnable = timerRunnable;
            this.tryagain = viewholder.tryagain;
            this.time = time;

        }

        protected void onPreExecute() {

            status.setText(R.string.loading);
            loading.setVisibility(View.VISIBLE);
            tryagain.setVisibility(View.GONE);
        }

        protected CameraImage doInBackground(String... urls) {
            String imageURL = urls[0];


            try {
                URL url = new URL(imageURL);
                URLConnection connection = url.openConnection();
                Map<String, List<String>> fps = connection.getHeaderFields();
                String humanReadableFPS = "0";
                InputStream in = url.openStream();
                final Bitmap decoded = BitmapFactory.decodeStream(in);
                in.close();
                for (Map.Entry<String, List<String>> key : fps.entrySet()) {
                    for (String string : key.getValue()) {
                        if (string.contains("capture_fps")) {
                            int ii = 0;

                            double d = Double.parseDouble(string.split("capture_fps_" + camera.getId() + "=")[1].split(";")[0].trim());
                            ii = (int) d;
                            humanReadableFPS = String.valueOf(Math.round(ii));
                            return new CameraImage(humanReadableFPS, decoded, true);

                        }

                    }
                }




            } catch (Exception e) {
                e.printStackTrace();
                return new CameraImage(false, e.getMessage());
            }
            return null;

        }

        protected void onPostExecute(CameraImage result) {
            if (result.isSuccessful()) {
                if (!loaded) {
                    progressBar.setVisibility(View.GONE);

                    imageView.setVisibility(View.VISIBLE);
                    loaded = true;

                }
                imageView.setImageBitmap(result.getBitmap());
                cameraViewVisible(loaded, progressBar, imageView);

                if (time.size() == Utils.fpsLen) {

                    long streamingFps = time.size() * 1000 / (time.get(time.size()-1) - time.get(0));
                    int fpsDeliv = Math.round(streamingFps);
                    fps.setText(result.getFps() + "/"+fpsDeliv+" fps");

                }

                long timeNow = new Date().getTime();
                time.add(timeNow);
                if (time.size() > Utils.fpsLen) {
                    time.remove(0);
                }

                if (attached) {
                    timerHandler.postDelayed(timerRunnable, Utils.imageRefreshInterval); //Start timer after 1 sec

                }

            } else {
                loaded = false;
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                tryagain.setVisibility(View.VISIBLE);
                status.setText(result.getErrorString());
            }


        }
    }


    public void onPause() {
        attached = false;
    }

    public void onResume() {
        attached = true;
        notifyDataSetChanged();
    }

    public void onDestroy() {
        attached = false;
    }


    private void cameraViewVisible(boolean loaded, LinearLayout progressBar, ImageView imageView) {
        if (!loaded) {
            progressBar.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            loaded = true;


        }
    }


}
