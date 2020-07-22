/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.developerfromjokela.motioneyeclient.ui.adapters.CompactRecordingsAdapter;
import com.developerfromjokela.motioneyeclient.ui.utils.DeviceURLUtils;
import com.developerfromjokela.motioneyeclient.ui.utils.MotionEyeSettings;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.ApiInterface;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.Media;
import com.developerfromjokela.motioneyeclient.classes.MediaList;
import com.developerfromjokela.motioneyeclient.classes.RecordingDevice;
import com.developerfromjokela.motioneyeclient.database.Source;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.developerfromjokela.motioneyeclient.ui.activities.MovieView;
import com.developerfromjokela.motioneyeclient.ui.adapters.MediaDeviceAdapter;
import com.developerfromjokela.motioneyeclient.ui.adapters.RecordingsAdapter;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static com.developerfromjokela.motioneyeclient.other.Utils.removeSlash;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecordingsFragment extends MotionEyeFragment implements MediaDeviceAdapter.DevicesAdapterListener, RecordingsAdapter.MediaAdapterListener {

    private BottomSheetBehavior sheetBehavior;
    private LinearLayout bottom_sheet;
    private RecyclerView devicesView;
    private MediaDeviceAdapter adapter;
    private Source source;
    private List<RecordingDevice> devices = new ArrayList<>();
    private RecyclerView recordings;
    private RecordingDevice selectedDevice;
    private String TAG = this.getClass().getSimpleName();
    private List<Media> mediaList = new ArrayList<>();

    private ProgressBar recordingsProgress;

    private RecordingsAdapter recordingsAdapter;

    private MotionEyeSettings settings;

    public RecordingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recordings, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);// click event for show-dismiss bottom sheet
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        devicesView = view.findViewById(R.id.devices);
        adapter = new MediaDeviceAdapter(getContext(), devices, this);
        source = new Source(getContext());
        settings = new MotionEyeSettings(getContext());
        recordingsProgress = view.findViewById(R.id.recordingsProgress);
        try {
            for (Device device : source.getAll()) {
                for (Camera camera : device.getCameras()) {
                    devices.add(new RecordingDevice(device, camera));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        devicesView.setAdapter(adapter);
        devicesView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        devicesView.addItemDecoration(new Utils.GridSpacingItemDecoration(1, Utils.dpToPx(getActivity()), true));
        adapter.notifyDataSetChanged();

        if (settings.getRecordingsUIMode().equals("compact"))
            recordingsAdapter = new CompactRecordingsAdapter(getContext(), mediaList, this, selectedDevice);
        else
            recordingsAdapter = new RecordingsAdapter(getContext(), mediaList, this, selectedDevice);

        recordings = view.findViewById(R.id.recordings);

        recordings.setAdapter(recordingsAdapter);
        recordings.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recordings.addItemDecoration(new Utils.GridSpacingItemDecoration(1, Utils.dpToPx(getActivity()), true));


        if (!devices.isEmpty()) {
            selectedDevice = devices.get(0);
            loadRecordings();
        } else {
            View view1 = view.findViewById(R.id.emptyView);
            view1.setVisibility(View.VISIBLE);
            devicesView.setVisibility(View.GONE);
            recordings.setVisibility(View.GONE);
            view.findViewById(R.id.bottom_sheet);

        }

    }

    private void displayProgress() {
        recordingsProgress.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        recordingsProgress.setVisibility(View.INVISIBLE);
    }


    private void loadRecordings() {
        displayProgress();
        DeviceURLUtils.getOptimalURL(getContext(), selectedDevice.getDevice(), new DeviceURLUtils.DeviceURLListener() {
            @Override
            public void onOptimalURL(String serverURL) {
                try {
                    String baseurl;
                    Log.e("Setup", String.valueOf(serverURL.split("//").length));
                    if (!serverURL.contains("://"))
                        baseurl = removeSlash("http://" + serverURL);
                    else
                        baseurl = removeSlash(serverURL);
                    String url = baseurl + "/movie/" + selectedDevice.getCamera().getId() + "/list?_=" + new Date().getTime();
                    MotionEyeHelper helper = new MotionEyeHelper();
                    helper.setUsername(selectedDevice.getDevice().getUser().getUsername());

                    helper.setPasswordHash(selectedDevice.getDevice().getUser().getPassword());

                    url = helper.addAuthParams("GET", url, "");
                    ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);

                    apiInterface.getMedia(url).enqueue(new Callback<MediaList>() {
                        @Override
                        public void onResponse(Call<MediaList> call, Response<MediaList> response) {
                            hideProgress();
                            MediaList mediaListObj = response.body();
                            recordingsAdapter.updateDetails(selectedDevice);
                            mediaList.clear();
                            recordingsAdapter.notifyDataSetChanged();
                            Collections.sort(mediaListObj.getMedia(), new Comparator<Media>() {
                                @Override
                                public int compare(Media o1, Media o2) {
                                    return (int) o2.getTimestamp() - (int) o1.getTimestamp();
                                }
                            });
                            for (Media media : mediaListObj.getMedia()) {
                                mediaList.add(media);
                                recordingsAdapter.notifyItemInserted(mediaList.size() - 1);
                            }
                        }

                        @Override
                        public void onFailure(Call<MediaList> call, Throwable t) {
                            hideProgress();
                            t.printStackTrace();
                            t.fillInStackTrace();
                            snack(t.getMessage());
                        }
                    });
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                    snack(e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {
                hideProgress();
                e.printStackTrace();
                e.fillInStackTrace();
                snack(e.getMessage());
            }
        });

    }

    @Override
    public void onMediaClicked(int position, Media media, String cachedURL) {
        String baseurl;
        Log.e("Setup", String.valueOf(cachedURL.split("//").length));
        if (!cachedURL.contains("://"))
            baseurl = removeSlash("http://" + cachedURL);
        else
            baseurl = removeSlash(cachedURL);
        Intent playbackIntent = new Intent(getContext(), MovieView.class);
        playbackIntent.putExtra("baseurl", baseurl);
        playbackIntent.putExtra("user", selectedDevice.getDevice().getUser());

        playbackIntent.putExtra("camera", selectedDevice.getCamera());
        playbackIntent.putExtra("media", media);
        startActivity(playbackIntent);
    }

    @Override
    public void onDeviceClicked(int position, RecordingDevice device) {
        selectedDevice = device;
        loadRecordings();
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
