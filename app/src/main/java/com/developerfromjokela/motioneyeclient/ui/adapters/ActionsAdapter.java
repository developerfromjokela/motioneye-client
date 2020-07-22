/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;

import java.util.List;

public class ActionsAdapter extends RecyclerView.Adapter<ActionsAdapter.ActionsViewHolder> {

    private Context mContext;
    private List<String> actionsList;
    private ActionsAdapterListener listener;

    public class ActionsViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        CardView action;
        AppCompatImageView icon;

        ActionsViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            action = itemView.findViewById(R.id.action);
            icon = itemView.findViewById(R.id.actionicon);
        }
    }

    public ActionsAdapter(Context mContext, List<String> actionsList, ActionsAdapterListener listener) {
        this.mContext = mContext;
        this.listener = listener;


        this.actionsList = actionsList;
    }

    @Override
    public ActionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.action, parent, false);
        return new ActionsViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final ActionsAdapter.ActionsViewHolder holder, final int position) {
        String actionString = actionsList.get(position);
        if (actionString.contains("preset")) {
            holder.icon.setVisibility(View.GONE);
            holder.number.setText(actionString.split("preset")[1]);
            holder.number.setVisibility(View.VISIBLE);
        } else if (actionString.contains("lock")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_lock);
        } else if (actionString.contains("unlock")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_unlock);
        } else if (actionString.contains("light_on")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_light_on);
        } else if (actionString.contains("light_off")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_light_off);
        } else if (actionString.contains("alarm_on")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_alarm_on);
        } else if (actionString.contains("alarm_off")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_alarm_off);
        } else if (actionString.contains("up")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_up);
        } else if (actionString.contains("right")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_right);
        } else if (actionString.contains("down")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_down);
        } else if (actionString.contains("left")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_back);
        } else if (actionString.contains("zoom_in")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_zoom_in);
        } else if (actionString.contains("zoom_out")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_zoom_out);
        } else if (actionString.contains("snapshot")) {
            holder.number.setVisibility(View.GONE);
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(R.drawable.ic_snapshot);
        }


        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("AA", "Action clicked");
                listener.onActionClicked(actionString, v);
            }
        });

    }

    public interface ActionsAdapterListener {

        void onActionClicked(String action, View button);
    }

    @Override
    public int getItemCount() {
        return actionsList.size();
    }
}