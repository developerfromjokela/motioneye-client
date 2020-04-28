/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.classes.Device;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DevicesViewHolder> {

    private Context mContext;
    private List<Device> deviceList;
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



    public DevicesAdapter(Context mContext, List<Device> deviceList, DevicesAdapterListener listener) {
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
    public void onBindViewHolder(final DevicesAdapter.DevicesViewHolder holder, final int position) {
        final Device device = deviceList.get(position);
        holder.deviceName.setText(device.getDeviceName());
        if (device.getDdnsURL().length() > 5)
            holder.deviceURL.setText(device.getDeviceUrl() + " (" + mContext.getString(R.string.ddns_available) + ")");
        else
            holder.deviceURL.setText(device.getDeviceUrl());
        String suffix = mContext.getString(R.string.cameras_count_morethan1);
        if (device.getCamera().size() == 1)
            suffix = "";
        holder.cameras.setText(mContext.getString(R.string.cameras_count, String.valueOf(device.getCamera().size()), suffix));
        holder.itemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeviceClicked(position, device);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.e("DA", "Longclick");
                PopupMenu popup = new PopupMenu(mContext, holder.itemView);
                //inflating menu from xml resource
                popup.inflate(R.menu.device_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        return false;
                    }
                });
                //displaying the popup
                popup.show();
                return false;
            }
        });

    }

    public interface DevicesAdapterListener {

        void onDeviceClicked(int position, Device device);
        void onDeviceDeleteRequest(int position, Device device);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }



}