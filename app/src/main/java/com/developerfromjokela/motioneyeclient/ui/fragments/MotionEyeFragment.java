/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;

import android.view.View;

public class MotionEyeFragment extends Fragment {

    private View view;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MotionEyeFragment.this.view = view;
    }

    public void snack(String content) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), content, 3000).show();
    }

    public void snack(String content, int duration) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), content, duration).show();
    }
}
