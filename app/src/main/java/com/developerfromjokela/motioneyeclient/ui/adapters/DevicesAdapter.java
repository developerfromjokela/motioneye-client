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
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    public interface DevicesAdapterListener {

        void onDeviceClicked(int position, Device device);
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }
}