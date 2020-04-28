/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.RecordingDevice;

import java.util.List;

public class MediaDeviceAdapter extends RecyclerView.Adapter<MediaDeviceAdapter.DevicesViewHolder> {

    private Context mContext;
    private List<RecordingDevice> deviceList;
    private DevicesAdapterListener listener;

    public class DevicesViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName, deviceURL, cameras;
        CardView itemCard;

        DevicesViewHolder(View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            cameras = itemView.findViewById(R.id.camerasCount);
            deviceURL = itemView.findViewById(R.id.deviceURL);
            itemCard = itemView.findViewById(R.id.itemCard);

        }

    }



    public MediaDeviceAdapter(Context mContext, List<RecordingDevice> deviceList, DevicesAdapterListener listener) {
        this.mContext = mContext;
        this.listener = listener;


        this.deviceList = deviceList;
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device, parent, false);
        return new DevicesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MediaDeviceAdapter.DevicesViewHolder holder, final int position) {
        final RecordingDevice device = deviceList.get(position);
        holder.deviceName.setText(device.getCamera().getName());
        holder.deviceURL.setText(device.getDevice().getDeviceName());
        if (device.getDevice().getDdnsURL().length() > 5)
            holder.cameras.setText(device.getDevice().getDeviceUrl() + " (" + mContext.getString(R.string.ddns_available) + ")");
        else
            holder.cameras.setText(device.getDevice().getDeviceUrl());
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeviceClicked(position, device);
            }
        });


    }

    public interface DevicesAdapterListener {

        void onDeviceClicked(int position, RecordingDevice device);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }



}