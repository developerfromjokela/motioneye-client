/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.classes.CameraImageFrame;

import java.util.List;

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
