/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.Media;
import com.developerfromjokela.motioneyeclient.classes.RecordingDevice;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.developerfromjokela.motioneyeclient.ui.utils.DeviceURLUtils;
import com.developerfromjokela.motioneyeclient.ui.utils.MotionEyeSettings;
import com.squareup.picasso.Picasso;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static com.developerfromjokela.motioneyeclient.other.Utils.removeSlash;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.DevicesViewHolder> {

    private Context mContext;
    private List<Media> mediaList;
    private MediaAdapterListener listener;


    public class DevicesViewHolder extends RecyclerView.ViewHolder {

        TextView mediaName, itemSize, itemDate;
        ImageView preview;

        DevicesViewHolder(View itemView) {
            super(itemView);
            mediaName = itemView.findViewById(R.id.mediaName);
            itemSize = itemView.findViewById(R.id.itemSize);
            itemDate = itemView.findViewById(R.id.itemDate);
            preview = itemView.findViewById(R.id.preview);
        }

    }


    public RecordingsAdapter(Context mContext, List<Media> mediaList, MediaAdapterListener listener) {
        this.mContext = mContext;
        this.listener = listener;
        this.mediaList = mediaList;
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.media, parent, false);
        return new DevicesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final RecordingsAdapter.DevicesViewHolder holder, int position) {
        final Media media = mediaList.get(position);
        RecordingDevice device = media.getDevice();
        if (new MotionEyeSettings(holder.itemView.getContext()).isDeviceSelectionDisabled())
            holder.mediaName.setText(media.getDevice().getCamera().getName());
        else
            holder.mediaName.setText(media.getPath().split("/")[media.getPath().split("/").length - 1]);
        holder.itemDate.setText(media.getShortMonent());
        holder.itemSize.setText(media.getSize());
        DeviceURLUtils.getOptimalURL(mContext, device.getDevice(), new DeviceURLUtils.DeviceURLListener() {
            @Override
            public void onOptimalURL(String serverURL) {
                String baseurl;
                Log.e("Setup", String.valueOf(serverURL.split("//").length));
                if (!serverURL.contains("://"))
                    baseurl = removeSlash("http://" + serverURL);
                else
                    baseurl = removeSlash(serverURL);

                try {
                    String url = baseurl + "/movie/" + device.getCamera().getId() + "/preview" + media.getPath() + "?_=" + new Date().getTime();
                    MotionEyeHelper helper = new MotionEyeHelper();
                    helper.setUsername(device.getDevice().getUser().getUsername());
                    helper.setPasswordHash(device.getDevice().getUser().getPassword());
                    url = helper.addAuthParams("GET", url, "");
                    Log.e("RA", url);

                    Picasso picasso = ServiceGenerator.getPicasso(mContext);
                    picasso.load(url).into(holder.preview);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onMediaClicked(position, media, serverURL);
                    }
                });
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }

    public interface MediaAdapterListener {

        void onMediaClicked(int position, Media media, String cachedURL);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }



}