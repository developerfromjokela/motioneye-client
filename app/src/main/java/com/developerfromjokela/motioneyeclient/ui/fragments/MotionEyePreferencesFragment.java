package com.developerfromjokela.motioneyeclient.ui.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.developerfromjokela.motioneyeclient.R;
import com.google.android.material.snackbar.Snackbar;

public class MotionEyePreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    public void snack(String content) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), content, 3000).show();
    }

    public void snack(String content, int duration) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), content, duration).show();
    }
}
