/*
 * Copyright (c) 2019. MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licenced with MIT:
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 *  and associated documentation files (the "Software"), to deal in the Software without restriction,including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *   The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *    SOFTWARE.
 */

package com.developerfromjokela.motioneyeclient.ui.adapters;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
                listener.onActionClicked(position, actionString, v);
            }
        });
    }

    public interface ActionsAdapterListener {

        void onActionClicked(int position, String action, View button);
    }

    @Override
    public int getItemCount() {
        return actionsList.size();
    }
}