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
import androidx.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.developerfromjokela.motioneyeclient.classes.CameraImageFrame;
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
import java.util.Random;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;

public class HttpCamerasAdapter extends RecyclerView.Adapter<HttpCamerasAdapter.CamerasViewHolder> {

    private CamerasAdapterListener listener;
    private List<CameraImageFrame> cameraImageFrames;



    public class CamerasViewHolder extends RecyclerView.ViewHolder {

        ImageView cameraImage;
        LinearLayout loadingBar;
        TextView fps, status;
        ProgressBar progressBar;
        Button tryagain;
        CardView itemCard;
        boolean attached;

        CamerasViewHolder(View itemView) {
            super(itemView);

            cameraImage = itemView.findViewById(R.id.cameraImage);
            loadingBar = itemView.findViewById(R.id.cameraBar);
            itemCard = itemView.findViewById(R.id.itemCard);
            fps = itemView.findViewById(R.id.fps);
            status = itemView.findViewById(R.id.status);
            progressBar = itemView.findViewById(R.id.progressar);
            tryagain = itemView.findViewById(R.id.tryagain);
            attached = false;
        }


    }

    public HttpCamerasAdapter(CamerasAdapterListener listener, List<CameraImageFrame> cameraImageFrames) {
        this.listener = listener;
        this.cameraImageFrames = cameraImageFrames;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull CamerasViewHolder holder) {
        holder.attached = false;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull CamerasViewHolder holder) {
        holder.attached = true;
    }

    @Override
    public CamerasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.camera, parent, false);
        return new CamerasViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final HttpCamerasAdapter.CamerasViewHolder holder, int position) {
        final CameraImageFrame camera = cameraImageFrames.get(position);

        if (camera.getError() != null) {
            holder.cameraImage.setVisibility(View.GONE);
            holder.fps.setVisibility(View.GONE);
            holder.loadingBar.setVisibility(View.VISIBLE);
            holder.progressBar.setVisibility(View.GONE);
            holder.tryagain.setVisibility(camera.getError().isDisplayRetry() ? View.VISIBLE : View.GONE);
            holder.status.setText(camera.getError().getErrorCause());


        } else {
            if (camera.getBitmap() != null && camera.isInitialLoadDone()) {
                holder.loadingBar.setVisibility(View.GONE);
                holder.cameraImage.setVisibility(View.VISIBLE);
                holder.fps.setVisibility(View.VISIBLE);
                holder.cameraImage.setImageBitmap(camera.getBitmap());
                holder.fps.setText(camera.getFrameRateText());
            } else {
                holder.cameraImage.setVisibility(View.GONE);
                holder.fps.setVisibility(View.GONE);
                holder.loadingBar.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);
                holder.status.setText(R.string.loading);
            }
        }


        holder.tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onRefreshRequest(position, camera);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onImageClick(position, camera);
            }
        });


    }

    public interface CamerasAdapterListener {

        void onImageClick(int position, CameraImageFrame cameraImageFrame);

        void onRefreshRequest(int position, CameraImageFrame cameraImageFrame);
    }

    @Override
    public int getItemCount() {
        return cameraImageFrames.size();
    }



}
