package com.developerfromjokela.motioneyeclient.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.developerfromjokela.motioneyeclient.R;

public class DeviceSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicesettings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        getSupportFragmentManager().beginTransaction().add(R.id.devPreferencesFrame, new DevicePreferences()).commit();
    }


    public static class DevicePreferences extends android.support.v7.preference.PreferenceFragmentCompat
    {


        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.devicepreferences);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            // findPreference() uses android:key like in preferences.xml !


        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }
    }



}
