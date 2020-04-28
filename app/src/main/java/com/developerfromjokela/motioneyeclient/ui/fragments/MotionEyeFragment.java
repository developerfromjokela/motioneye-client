/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
