/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.ui.fragments.DevicesFragment;
import com.developerfromjokela.motioneyeclient.ui.fragments.RecordingsFragment;

import javax.net.ssl.HttpsURLConnection;

import static com.developerfromjokela.motioneyeclient.api.ServiceGenerator.motionEyeVerifier;

public class MainActivity extends AppCompatActivity implements DevicesFragment.startupExecListener {

    private boolean startupExec = false;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_devices:
                    Bundle a = new Bundle();
                    a.putBoolean("startupExec", startupExec);
                    DevicesFragment devicesFragment = new DevicesFragment();
                    devicesFragment.setArguments(a);
                    loadFragment(devicesFragment);
                    return true;
                case R.id.navigation_recordings:
                    loadFragment(new RecordingsFragment());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HttpsURLConnection.setDefaultHostnameVerifier(motionEyeVerifier);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(new DevicesFragment());
        navigation.setSelectedItemId(R.id.navigation_devices);
    }

    private void loadFragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void paramChanged(boolean newParam) {
        startupExec = newParam;
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
