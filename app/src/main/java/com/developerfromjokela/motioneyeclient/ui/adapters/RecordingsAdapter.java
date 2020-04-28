/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.Media;
import com.developerfromjokela.motioneyeclient.classes.RecordingDevice;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.squareup.picasso.Picasso;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static com.developerfromjokela.motioneyeclient.other.Utils.removeSlash;

public class RecordingsAdapter extends RecyclerView.Adapter<RecordingsAdapter.DevicesViewHolder> {

    private Context mContext;
    private List<Media> mediaList;
    private MediaAdapterListener listener;
    private RecordingDevice device;

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



    public RecordingsAdapter(Context mContext, List<Media> mediaList, MediaAdapterListener listener, RecordingDevice device) {
        this.mContext = mContext;
        this.listener = listener;
        this.device = device;
        this.mediaList = mediaList;
    }

    @Override
    public DevicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.media, parent, false);
        return new DevicesViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final RecordingsAdapter.DevicesViewHolder holder, final int position) {
        final Media media = mediaList.get(position);
        holder.mediaName.setText(media.getPath().split("/")[media.getPath().split("/").length-1]);
        holder.itemDate.setText(media.getShortMonent());
        holder.itemSize.setText(media.getSize());
        String baseurl;
        String serverurl;
        if (device.getDevice().getDdnsURL().length() > 5) {
            if ((Utils.getNetworkType(mContext)) == NETWORK_MOBILE) {
                serverurl = device.getDevice().getDDNSUrlCombo();
            } else if (device.getDevice().getWlan().networkId == Utils.getCurrentWifiNetworkId(mContext)) {
                serverurl = device.getDevice().getDeviceUrlCombo();

            } else {
                serverurl = device.getDevice().getDDNSUrlCombo();

            }
        } else {
            serverurl = device.getDevice().getDeviceUrlCombo();

        }
        Log.e("Setup", String.valueOf(serverurl.split("//").length));
        if (!serverurl.contains("://"))
            baseurl = removeSlash("http://" + serverurl);
        else
            baseurl = removeSlash(serverurl);

        try {
            String url = baseurl + "/movie/"+device.getCamera().getId()+"/preview"+media.getPath()+"?_=" + new Date().getTime();
            MotionEyeHelper helper = new MotionEyeHelper();
            helper.setUsername(device.getDevice().getUser().getUsername());
            helper.setPasswordHash(device.getDevice().getUser().getPassword());
            url = helper.addAuthParams("GET", url, "");
            Log.e("RA", url);

            Picasso.get().load(url).into(holder.preview);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMediaClicked(position, media);
            }
        });


    }

    public interface MediaAdapterListener {

        void onMediaClicked(int position, Media media);
    }

    @Override
    public int getItemCount() {
        return mediaList.size();
    }


    public void updateDetails(RecordingDevice device) {
        this.device = device;
    }

}