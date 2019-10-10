package com.developerfromjokela.motioneyeclient.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.developerfromjokela.motioneyeclient.ui.adapters.DevicesAdapter;
import com.developerfromjokela.motioneyeclient.ui.adapters.MediaDeviceAdapter;
import com.developerfromjokela.motioneyeclient.ui.adapters.RecordingsAdapter;
import com.google.gson.Gson;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
public class RecordingsFragment extends Fragment implements MediaDeviceAdapter.DevicesAdapterListener, RecordingsAdapter.MediaAdapterListener {

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
        try {
            String baseurl;
            String serverurl;
            if (selectedDevice.getDevice().getDdnsURL().length() > 5) {
                if ((Utils.getNetworkType(getContext())) == NETWORK_MOBILE) {
                    serverurl = selectedDevice.getDevice().getDDNSUrlCombo();
                } else if (selectedDevice.getDevice().getWlan().networkId == Utils.getCurrentWifiNetworkId(getContext())) {
                    serverurl = selectedDevice.getDevice().getDeviceUrlCombo();

                } else {
                    serverurl = selectedDevice.getDevice().getDDNSUrlCombo();

                }
            } else {
                serverurl = selectedDevice.getDevice().getDeviceUrlCombo();

            }
            Log.e("Setup", String.valueOf(serverurl.split("//").length));
            if (!serverurl.contains("://"))
                baseurl = removeSlash("http://" + serverurl);
            else
                baseurl = removeSlash(serverurl);
            String url = baseurl + "/movie/"+selectedDevice.getCamera().getId()+"/list?_=" + new Date().getTime();
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
                    mediaList.clear();
                    recordingsAdapter.notifyDataSetChanged();
                    recordingsAdapter.updateDetails(selectedDevice);
                    for (Media media : mediaListObj.getMedia()) {
                        mediaList.add(media);
                        recordingsAdapter.notifyItemInserted(mediaList.size()-1);
                    }
                }

                @Override
                public void onFailure(Call<MediaList> call, Throwable t) {
                    hideProgress();
                    t.printStackTrace();
                    t.fillInStackTrace();
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMediaClicked(int position, Media media) {
        String baseurl;
        String serverurl;
        if (selectedDevice.getDevice().getDdnsURL().length() > 5) {
            if ((Utils.getNetworkType(getContext())) == NETWORK_MOBILE) {
                serverurl = selectedDevice.getDevice().getDDNSUrlCombo();
            } else if (selectedDevice.getDevice().getWlan().networkId == Utils.getCurrentWifiNetworkId(getContext())) {
                serverurl = selectedDevice.getDevice().getDeviceUrlCombo();

            } else {
                serverurl = selectedDevice.getDevice().getDDNSUrlCombo();

            }
        } else {
            serverurl = selectedDevice.getDevice().getDeviceUrlCombo();

        }
        Log.e("Setup", String.valueOf(serverurl.split("//").length));
        if (!serverurl.contains("://"))
            baseurl = removeSlash("http://" + serverurl);
        else
            baseurl = removeSlash(serverurl);
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
    }
}
