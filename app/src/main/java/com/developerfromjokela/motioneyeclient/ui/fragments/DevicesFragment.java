/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.database.Source;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.developerfromjokela.motioneyeclient.ui.activities.CameraViewer;
import com.developerfromjokela.motioneyeclient.ui.adapters.DevicesAdapter;
import com.developerfromjokela.motioneyeclient.ui.setup.activities.SetupStartScreen;
import com.developerfromjokela.motioneyeclient.ui.utils.DevicesView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DevicesFragment extends android.support.v4.app.Fragment implements DevicesAdapter.DevicesAdapterListener {

    private DevicesView camerasRecyclerView;
    private DevicesAdapter adapter;
    private Source database;
    private FloatingActionButton addCamera;
    private LinearLayout emptyView;
    private List<Device> deviceList = new ArrayList<>();
    private boolean startupExec = false;
    private startupExecListener startupExecListener;

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        if (args != null) {
            if (args.containsKey("startupExec"))
                startupExec = args.getBoolean("startupExec");
        }


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof startupExecListener)
            startupExecListener = (startupExecListener) context;
        else
            throw new RuntimeException("Listener not found! Can't continue");
    }

    public interface startupExecListener {
        void paramChanged(boolean newParam);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cameras, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }

    @Override
    public void onDeviceClicked(int position, Device device) {
       startViewer(device);
    }

    private void startViewer(Device device) {
        Intent viewer = new Intent(getActivity(), CameraViewer.class);
        viewer.putExtra("DeviceId", device.getID());
        startActivity(viewer);
    }

    @Override
    public void onDeviceDeleteRequest(int position, Device device) {
        deleteDevice(device);
    }

    private void setListeners() {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {

            @Override
            public void onChanged() {
                super.onChanged();
                checkEmpty();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                checkEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                checkEmpty();
            }


        });
        addCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SetupStartScreen.class));
            }
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        // Inflate Menu from xml resource
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.device_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteDevice:
                DevicesView.RecyclerContextMenuInfo info = (DevicesView.RecyclerContextMenuInfo) item.getMenuInfo();
                deleteDevice(deviceList.get(info.position));
                break;
        }

        return false;
    }

    private void deleteDevice(Device device) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setTitle(R.string.delete_camera);
        dialogBuilder.setMessage(R.string.delete_camera_caution);
        dialogBuilder.setNegativeButton(R.string.cancel, null);
        dialogBuilder.setPositiveButton(R.string.delete_camera, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Source source = new Source(getContext());
                try {
                    source.delete_item(device);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.failed_device_delete, e.getMessage()), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void loadFromDatabase() {
        try {
            deviceList.clear();
            deviceList.addAll(database.getAll());
            for (Device device : deviceList) {
                adapter.notifyItemInserted(deviceList.indexOf(device));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        checkEmpty();
        String autoOpenID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("autoOpenID", null);
        if (autoOpenID !=  null && !startupExec) {
            startupExec = true;
            startupExecListener.paramChanged(startupExec);
            for (Device device : deviceList) {
                if (device.getID().equals(autoOpenID))
                    startViewer(device);

            }
        }

    }

    private void setupViews(View view) {
        emptyView = view.findViewById(R.id.emptyView);
        addCamera = view.findViewById(R.id.addItem);
        camerasRecyclerView = view.findViewById(R.id.camerasRecyclerView);
        camerasRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        camerasRecyclerView.addItemDecoration(new Utils.GridSpacingItemDecoration(1, Utils.dpToPx(getActivity()), true));

    }

    private void initializeObjects() {
        adapter = new DevicesAdapter(getActivity(), deviceList, this);
        camerasRecyclerView.setAdapter(adapter);
        database = new Source(getActivity());
    }

    private void checkEmpty() {
        emptyView.setVisibility(adapter.getItemCount() > 0 ? View.VISIBLE : View.GONE);

        emptyView.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeObjects();
        setListeners();
        loadFromDatabase();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startupExecListener = null;
    }
}
