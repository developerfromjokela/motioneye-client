package com.developerfromjokela.motioneyeclient.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.developerfromjokela.motioneyeclient.other.Utils;

public class MotionEyeSettings {

    private SharedPreferences sharedPreferences;

    public MotionEyeSettings(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private static class Keys {
        public static String DISABLE_DEVICE_SELECTION = "no_selection";
        public static String FRAMERATE_FACTOR = "framerateFactor";
        public static String RECORDINGS_UI_MODE = "ui_mode";
        public static String SORT_OPTION = "recordings_sort";
    }

    public boolean isDeviceSelectionDisabled() {
        return sharedPreferences.getBoolean(Keys.DISABLE_DEVICE_SELECTION, false);
    }

    public String getRecordingsUIMode() {
        return sharedPreferences.getString(Keys.RECORDINGS_UI_MODE, "compact");
    }

    public int getFrameRateFactor() {
        return sharedPreferences.getInt(Keys.FRAMERATE_FACTOR, 100);
    }

    public String getSortOption() {
        return sharedPreferences.getString(Keys.RECORDINGS_UI_MODE, RecordingSortOptions.DESCENDING);
    }

    public void setSortOption(String option) {
        sharedPreferences.edit().putString(Keys.RECORDINGS_UI_MODE, option).apply();
    }

}
