/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.classes.WifiNetwork;

import java.util.List;

public class WifisAdapter extends ArrayAdapter {


    private List<WifiNetwork> networks;

    public WifisAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<WifiNetwork> objects) {
        super(context, resource, textViewResourceId, objects);
        networks = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") final View view = View.inflate(getContext(), android.R.layout.simple_list_item_1, null);

        if (networks.get(position).isSelected()) {
            // set your color
            view.setBackgroundColor(Color.parseColor("#1d7031"));

        }

        TextView textView = view.findViewById(android.R.id.text1);
        if (networks.get(position).getConfiguration().status == 0)
            textView.setText(networks.get(position).getConfiguration().SSID + " (" + getContext().getString(R.string.connected) + ")");
        else
            textView.setText(networks.get(position).getConfiguration().SSID);

        return view;
    }


}
