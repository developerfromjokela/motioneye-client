/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.ApiInterface;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.Cameras;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.MainConfig;
import com.developerfromjokela.motioneyeclient.database.Source;
import com.developerfromjokela.motioneyeclient.other.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;

public class DeviceSettings extends MotionEyeActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devicesettings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        DevicePreferences preferencesFragment = new DevicePreferences();
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            preferencesFragment.setDeviceId(intent.getStringExtra("DeviceId"), findViewById(R.id.validateSettingsFab), this);
            getSupportFragmentManager().beginTransaction().add(R.id.devPreferencesFrame, preferencesFragment).commit();
        } else {
            finish();
        }


    }


    public static class DevicePreferences extends androidx.preference.PreferenceFragmentCompat {

        private MainConfig config;
        private Device device;
        public static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");

        private boolean networkChangesMade = false;
        private FloatingActionButton validateButton;


        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.devicepreferences);
            disableMEYESettings();
            initNewtorkSettings();

            {
                String autoOpenID = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("autoOpenID", null);
                if (autoOpenID != null)
                    if (autoOpenID.equals(device.getID()))
                        ((CheckBoxPreference) findPreference("open_by_default")).setChecked(true);
                findPreference("open_by_default").setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @Override
                    public boolean onPreferenceChange(Preference preference, Object o) {
                        boolean autoStart = (boolean) o;
                        if (autoStart)
                            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("autoOpenID", device.getID()).apply();
                        else
                            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString("autoOpenID", null).apply();
                        return true;
                    }
                });
            }
            {
                findPreference("admin_username").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);
                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(config.getAdmin_username(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (checkForDuplicate(config.getAdmin_username(), editText.getText().toString()))
                                            b.setEnabled(false);
                                        else
                                            b.setEnabled(true);

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        disableMEYESettings();

                                        try {
                                            if (device.getUser().getUsername().equals(preference.getSummary()))
                                                device.getUser().setUsername(editText.getText().toString());
                                            changeSettings(getAdminUsernameSettingsJSON(editText.getText().toString()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();
                        return false;
                    }
                });
            }
            {
                findPreference("delete_camera").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        dialogBuilder.setTitle(R.string.delete_camera);
                        dialogBuilder.setMessage(R.string.delete_camera_caution);
                        dialogBuilder.setNegativeButton(R.string.cancel, null);
                        dialogBuilder.setPositiveButton(R.string.delete_camera, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Source source = new Source(getContext());
                                try {
                                    source.delete_item(device);
                                    getActivity().setResult(RESULT_CANCELED);
                                    getActivity().finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(getActivity(), getString(R.string.failed_device_delete, e.getMessage()), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                        return false;
                    }
                });
            }
            {
                findPreference("admin_password").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);
                        editText.setText(preference.getSummary());
                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(config.getAdmin_password(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (checkForDuplicate(config.getAdmin_password(), editText.getText().toString()))
                                            b.setEnabled(false);
                                        else
                                            b.setEnabled(true);

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        disableMEYESettings();
                                        try {
                                            JSONObject settings = getBasicSettingsJSON();
                                            if (device.getUser().getUsername().equals(findPreference("admin_username").getSummary()))
                                                device.getUser().setPassword(editText.getText().toString());
                                            settings.getJSONObject("main").put("admin_password", editText.getText().toString());
                                            changeSettings(settings);
                                            dialog.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();
                        return false;
                    }
                });
            }

            {
                findPreference("surv_username").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);
                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(config.getNormal_username(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (checkForDuplicate(config.getNormal_username(), editText.getText().toString()))
                                            b.setEnabled(false);
                                        else
                                            b.setEnabled(true);

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        disableMEYESettings();


                                        try {
                                            if (device.getUser().getUsername().equals(preference.getSummary()))
                                                device.getUser().setUsername(editText.getText().toString());
                                            changeSettings(getNormalUsernameSettingsJSON(editText.getText().toString()));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();
                        return false;
                    }
                });
            }
            {
                findPreference("surv_password").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);
                        editText.setText(preference.getSummary());
                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(config.getNormal_password(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        if (checkForDuplicate(config.getNormal_password(), editText.getText().toString()))
                                            b.setEnabled(false);
                                        else
                                            b.setEnabled(true);

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        disableMEYESettings();

                                        try {
                                            JSONObject settings = getBasicSettingsJSON();
                                            if (device.getUser().getUsername().equals(findPreference("surv_username").getSummary()))
                                                device.getUser().setPassword(editText.getText().toString());

                                            settings.getJSONObject("main").put("normal_password", editText.getText().toString());
                                            changeSettings(settings);
                                            dialog.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                                        }
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                        return false;
                    }
                });
            }

            {
                findPreference("ip_addr").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);

                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);

                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(device.getDeviceUrl(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        String url = s.toString();

                                        if (!url.isEmpty()) {

                                            if (checkForDuplicate(device.getDeviceUrl(), url)) {
                                                b.setEnabled(false);
                                            } else
                                                b.setEnabled(true);


                                            if (url.split("://").length >= 2) {
                                                String nUrl = url.split("://")[1];
                                                if (nUrl.contains(":")) {
                                                    b.setEnabled(false);
                                                    return;
                                                } else
                                                    b.setEnabled(true);
                                            } else {
                                                if (url.contains(":")) {
                                                    b.setEnabled(false);
                                                    return;
                                                } else
                                                    b.setEnabled(true);
                                            }

                                            if (!URLUtil.isValidUrl(url)) {
                                                url = "http://" + url;
                                                b.setEnabled(URLUtil.isValidUrl(url));
                                            } else
                                                b.setEnabled(true);


                                        } else {
                                            b.setEnabled(false);

                                        }


                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        device.setDeviceUrl(editText.getText().toString());
                                        networkChangesMade = true;
                                        replaceDeviceInDB();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                        return false;
                    }
                });
            }
            {
                findPreference("ddns_addr").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);

                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);

                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(device.getDdnsURL(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        String url = editText.getText().toString();
                                        if (!url.isEmpty()) {

                                            if (checkForDuplicate(device.getDdnsURL(), url)) {
                                                b.setEnabled(false);
                                            } else
                                                b.setEnabled(true);

                                            if (!URLUtil.isValidUrl(url)) {
                                                url = "http://" + url;
                                                b.setEnabled(URLUtil.isValidUrl(url));
                                            } else
                                                b.setEnabled(true);

                                            if (url.contains("://") && url.split("://").length >= 2) {
                                                String nUrl = url.split("://")[1];
                                                if (nUrl.contains(":")) {
                                                    b.setEnabled(false);
                                                } else
                                                    b.setEnabled(true);
                                            } else {
                                                if (url.contains(":")) {
                                                    b.setEnabled(false);
                                                } else
                                                    b.setEnabled(true);

                                            }

                                        } else {
                                            b.setEnabled(true);

                                        }


                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        device.setDdnsURL(editText.getText().toString());
                                        networkChangesMade = true;

                                        replaceDeviceInDB();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                        return false;
                    }
                });
            }
            {
                findPreference("port").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);

                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(device.getLocalPort(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        String url = s.toString();


                                        if (checkForDuplicate(device.getDdnsURL(), url)) {
                                            b.setEnabled(false);
                                        } else
                                            b.setEnabled(true);


                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        device.setLocalPort(editText.getText().toString());
                                        networkChangesMade = true;

                                        replaceDeviceInDB();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                        return false;
                    }
                });
            }

            {
                findPreference("ddns_port").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = dialogView.findViewById(R.id.settingParam);

                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                        layout.setHint(preference.getTitle());
                        dialogBuilder.setPositiveButton(R.string.save, null);
                        AlertDialog alertDialog = dialogBuilder.create();

                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                            @Override
                            public void onShow(DialogInterface dialog) {

                                Button b = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                if (checkForDuplicate(device.getDDNSPort(), editText.getText().toString()))
                                    b.setEnabled(false);
                                else
                                    b.setEnabled(true);
                                editText.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {


                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        String url = s.toString();


                                        if (checkForDuplicate(device.getDDNSPort(), url)) {
                                            b.setEnabled(false);
                                        }
                                        else
                                            b.setEnabled(true);


                                    }
                                });
                                b.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View view) {

                                        device.setDDNSPort(editText.getText().toString());
                                        networkChangesMade = true;

                                        replaceDeviceInDB();
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });
                        alertDialog.show();

                        return false;
                    }
                });
            }


            loadConfig();


        }

        private void initNewtorkSettings() {
            findPreference("ip_addr").setSummary(device.getDeviceUrl());
            findPreference("ddns_addr").setSummary(device.getDdnsURL());
            findPreference("port").setSummary(device.getLocalPort());
            findPreference("ddns_port").setSummary(device.getDDNSPort());

        }

        public void loadConfig() {
            try {
                String url = getFullUrl() + "/config/main/get/?_=" + new Date().getTime();
                MotionEyeHelper helper = new MotionEyeHelper();
                helper.setUsername(device.getUser().getUsername());
                helper.setPasswordHash(device.getUser().getPassword());
                url = helper.addAuthParams("GET", url, "");
                ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, getFullUrl());
                apiInterface.getMainConfig(url).enqueue(new Callback<MainConfig>() {
                    @Override
                    public void onResponse(Call<MainConfig> call, Response<MainConfig> response) {
                        if (response.isSuccessful()) {
                            config = response.body();
                            getAdvancedDetails();

                        }

                    }

                    @Override
                    public void onFailure(Call<MainConfig> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }

        private void validateSettings() {
            MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View validateView = inflater.inflate(R.layout.validatedialog, null);
            dialogBuilder.setView(validateView);

            dialogBuilder.setPositiveButton(R.string.close, null);
            dialogBuilder.setCancelable(false);
            AlertDialog validateDialog = dialogBuilder.create();
            validateDialog.setOnShowListener(new DialogInterface.OnShowListener() {

                @Override
                public void onShow(DialogInterface dialog) {



                    TextView Errorstatus = validateView.findViewById(R.id.errorDetails);
                    // Root Layouts
                    LinearLayout rootLayout2 = validateView.findViewById(R.id.ddns_connection_test);
                    // Progressbars
                    ProgressBar progressBar1 = validateView.findViewById(R.id.progress1);
                    ProgressBar progressBar2 = validateView.findViewById(R.id.progress2);
                    ProgressBar progressBar3 = validateView.findViewById(R.id.progress3);
                    ProgressBar progressBar4 = validateView.findViewById(R.id.progress4);

                    // Icons
                    ImageView status1 = validateView.findViewById(R.id.statusimage1);
                    ImageView status2 = validateView.findViewById(R.id.statusimage2);
                    ImageView status3 = validateView.findViewById(R.id.statusimage3);
                    ImageView status4 = validateView.findViewById(R.id.statusimage4);
                    Button continue_btn = validateDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                    continue_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            validateDialog.dismiss();
                        }
                    });
                    continue_btn.setEnabled(false);

                    if (device.getDdnsURL().length() > 0) {
                        rootLayout2.setVisibility(View.VISIBLE);
                    }


                    validateServer(new TestInterface() {
                        @Override
                        public void TestSuccessful(String response, int status) {
                            progressBar1.setVisibility(View.GONE);
                            status1.setVisibility(View.VISIBLE);
                            status1.setImageResource(R.drawable.ic_check_green);
                            status2.setVisibility(View.GONE);
                            progressBar2.setVisibility(View.VISIBLE);
                            if (device.getDdnsURL().length() > 0) {
                                validateServer(new TestInterface() {
                                    @Override
                                    public void TestSuccessful(String response, int status) {

                                        progressBar2.setVisibility(View.GONE);
                                        status2.setVisibility(View.VISIBLE);
                                        status2.setImageResource(R.drawable.ic_check_green);
                                        try {
                                            checkLogin(device.getDeviceUrlCombo(), new TestInterface() {
                                                @Override
                                                public void TestSuccessful(String response, int status) {
                                                    progressBar3.setVisibility(View.GONE);
                                                    status3.setVisibility(View.VISIBLE);
                                                    status3.setImageResource(R.drawable.ic_check_green);
                                                    status4.setVisibility(View.GONE);
                                                    progressBar4.setVisibility(View.VISIBLE);
                                                    getServerDetails(new TestInterface() {
                                                        @Override
                                                        public void TestSuccessful(String response, int status) {
                                                            validateDialog.setCancelable(true);
                                                            continue_btn.setEnabled(true);
                                                            status4.setVisibility(View.VISIBLE);
                                                            progressBar4.setVisibility(View.GONE);
                                                            status4.setImageResource(R.drawable.ic_check_green);

                                                        }

                                                        @Override
                                                        public void TestFailed(String response, int status) {
                                                            validateDialog.setCancelable(true);
                                                            continue_btn.setEnabled(true);
                                                            status4.setVisibility(View.VISIBLE);
                                                            progressBar4.setVisibility(View.GONE);
                                                            status4.setImageResource(R.drawable.ic_error_red);
                                                            Errorstatus.setVisibility(View.VISIBLE);
                                                            Errorstatus.setText(response);
                                                        }
                                                    }, device.getDeviceUrlCombo());
                                                }

                                                @Override
                                                public void TestFailed(String response, int status) {
                                                    validateDialog.setCancelable(true);
                                                    continue_btn.setEnabled(true);
                                                    progressBar3.setVisibility(View.GONE);
                                                    status3.setVisibility(View.VISIBLE);
                                                    status3.setImageResource(R.drawable.ic_error_red);
                                                    Errorstatus.setVisibility(View.VISIBLE);
                                                    Errorstatus.setText(response);
                                                }
                                            });
                                        } catch (NoSuchAlgorithmException e) {
                                            e.printStackTrace();
                                            progressBar3.setVisibility(View.GONE);
                                            status3.setVisibility(View.VISIBLE);
                                            status3.setImageResource(R.drawable.ic_error_red);
                                            Errorstatus.setVisibility(View.VISIBLE);
                                            Errorstatus.setText(e.getMessage());
                                            validateDialog.setCancelable(true);
                                            continue_btn.setEnabled(true);
                                        }
                                    }

                                    @Override
                                    public void TestFailed(String response, int status) {
                                        validateDialog.setCancelable(true);
                                        continue_btn.setEnabled(true);
                                        progressBar2.setVisibility(View.GONE);
                                        status2.setVisibility(View.VISIBLE);
                                        status2.setImageResource(R.drawable.ic_error_red);
                                        Errorstatus.setVisibility(View.VISIBLE);
                                        Errorstatus.setText(response);
                                    }
                                }, device.getDDNSUrlCombo());
                            } else {

                                try {
                                    checkLogin(device.getDeviceUrlCombo(), new TestInterface() {
                                        @Override
                                        public void TestSuccessful(String response, int status) {
                                            progressBar3.setVisibility(View.GONE);
                                            status3.setVisibility(View.VISIBLE);
                                            status3.setImageResource(R.drawable.ic_check_green);
                                            status4.setVisibility(View.GONE);
                                            progressBar4.setVisibility(View.VISIBLE);
                                            getServerDetails(new TestInterface() {
                                                @Override
                                                public void TestSuccessful(String response, int status) {
                                                    validateDialog.setCancelable(true);
                                                    continue_btn.setEnabled(true);
                                                    status4.setVisibility(View.VISIBLE);
                                                    progressBar4.setVisibility(View.GONE);
                                                    status4.setImageResource(R.drawable.ic_check_green);


                                                }

                                                @Override
                                                public void TestFailed(String response, int status) {
                                                    validateDialog.setCancelable(true);
                                                    continue_btn.setEnabled(true);
                                                    status4.setVisibility(View.VISIBLE);
                                                    progressBar4.setVisibility(View.GONE);
                                                    status4.setImageResource(R.drawable.ic_error_red);
                                                    Errorstatus.setVisibility(View.VISIBLE);
                                                    Errorstatus.setText(response);
                                                }
                                            }, device.getDeviceUrlCombo());
                                        }

                                        @Override
                                        public void TestFailed(String response, int status) {
                                            validateDialog.setCancelable(true);
                                            continue_btn.setEnabled(true);
                                            progressBar3.setVisibility(View.GONE);
                                            status3.setVisibility(View.VISIBLE);
                                            status3.setImageResource(R.drawable.ic_error_red);
                                            Errorstatus.setVisibility(View.VISIBLE);
                                            Errorstatus.setText(response);
                                        }
                                    });
                                } catch (NoSuchAlgorithmException e) {
                                    e.printStackTrace();
                                    progressBar3.setVisibility(View.GONE);
                                    status3.setVisibility(View.VISIBLE);
                                    status3.setImageResource(R.drawable.ic_error_red);
                                    Errorstatus.setVisibility(View.VISIBLE);
                                    Errorstatus.setText(e.getMessage());
                                    validateDialog.setCancelable(true);
                                    continue_btn.setEnabled(true);

                                }
                            }

                        }

                        @Override
                        public void TestFailed(String response, int status2) {
                            progressBar1.setVisibility(View.GONE);
                            status1.setVisibility(View.VISIBLE);
                            status1.setImageResource(R.drawable.ic_error_red);
                            Errorstatus.setVisibility(View.VISIBLE);
                            validateDialog.setCancelable(true);
                            continue_btn.setEnabled(true);

                            Errorstatus.setText(response);
                        }
                    }, device.getDeviceUrlCombo());
                }
            });

            validateDialog.show();




        }

        private void setConfigValues() {
            SwitchPreferenceCompat advancedSettings = (SwitchPreferenceCompat) findPreference("advancedSettings") ;
            advancedSettings.setChecked(config.isShow_advanced());
            advancedSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    try {
                        changeSettings(getAdvancedSettingsJSON(advancedSettings.isChecked()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

                    }
                    return false;
                }
            });
            findPreference("admin_username").setSummary(config.getAdmin_username());
            findPreference("admin_password").setSummary(config.getAdmin_password());
            findPreference("surv_username").setSummary(config.getNormal_username());
            findPreference("surv_password").setSummary(config.getNormal_password());
            Preference motionEyeVersion = findPreference("motionEyeVersion");
            Preference motionVersion = findPreference("motionVersion");
            Preference OSVersion = findPreference("OSVersion");
            if (config.isShow_advanced()) {
                motionEyeVersion.setVisible(true);

                OSVersion.setVisible(true);
                motionVersion.setVisible(true);

                motionEyeVersion.setSummary(device.getMotioneyeVersion());
                motionVersion.setSummary(device.getMotionVersion());
                OSVersion.setSummary(device.getOsVersion());
            } else {
                motionEyeVersion.setVisible(false);

                OSVersion.setVisible(false);
                motionVersion.setVisible(false);
            }



        }

        private void enableMEYESettings() {
            findPreference("advancedSettings").setEnabled(true);
            findPreference("admin_username").setEnabled(true);
            findPreference("admin_password").setEnabled(true);
            findPreference("surv_username").setEnabled(true);
            findPreference("surv_password").setEnabled(true);

        }

        private void disableMEYESettings() {
            findPreference("advancedSettings").setEnabled(false);
            findPreference("admin_username").setEnabled(false);
            findPreference("admin_password").setEnabled(false);
            findPreference("surv_username").setEnabled(false);
            findPreference("surv_password").setEnabled(false);

        }

        private void validateServer(TestInterface testInterface, String serverurl) {
            String baseurl;

            if (!serverurl.contains("://"))
                baseurl = removeSlash("http://" + serverurl);
            else
                baseurl = removeSlash(serverurl);

            ApiInterface apiInterface = null;
            try {
                apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                testInterface.TestFailed(e.getMessage(), 700);
                return;
            }
            Call<ResponseBody> call = apiInterface.login(baseurl + "/login", device.getUser().getUsername(), device.getUser().getPassword());
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            testInterface.TestSuccessful(response.body().string(), response.code());
                        } catch (IOException e) {
                            e.printStackTrace();
                            testInterface.TestFailed(e.getMessage(), 700);

                        }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    t.fillInStackTrace();
                    testInterface.TestFailed(t.getMessage(), 700);

                }
            });
        }

        private void getServerDetails(TestInterface testInterface, String serverurl) {
            String baseurl;

            if (!serverurl.contains("://"))
                baseurl = removeSlash("http://" + serverurl);
            else
                baseurl = removeSlash(serverurl);

            ApiInterface apiInterface = null;
            try {
                apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                testInterface.TestFailed(e.getMessage(), 700);
                return;
            }
            Call<ResponseBody> call = apiInterface.getMotionDetails(baseurl + "/version");
            ApiInterface finalApiInterface = apiInterface;
            call.enqueue(new Callback<ResponseBody>() {

                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            final String stringResponse = response.body().string();
                            Document html = Jsoup.parse(stringResponse);
                            Elements elements = html.select("body");
                            String[] lines = elements.html().replace("\"", "").replace("\n", "").split("<br>");
                            for (String string : lines) {
                                String[] paramParts = string.split("=");
                                String paramName = paramParts[0].trim();
                                String paramValue = paramParts[1];
                                if (paramName.contains("hostname"))
                                    device.setDeviceName(paramValue);
                                else if (paramName.contains("motion_version"))
                                    device.setMotionVersion(paramValue);
                                else if (paramName.contains("os_version"))
                                    device.setOsVersion(paramValue);
                                else if (paramName.equals("version"))
                                    device.setMotioneyeVersion(paramValue);

                            }
                            String url = baseurl + "/config/list?_=" + new Date().getTime();
                            MotionEyeHelper helper = new MotionEyeHelper();
                            helper.setUsername(device.getUser().getUsername());
                            helper.setPasswordHash(device.getUser().getPassword());
                            url = helper.addAuthParams("GET", url, "");

                            Call<Cameras> call2 = finalApiInterface.getCameras(url);
                            call2.enqueue(new Callback<Cameras>() {
                                @Override
                                public void onResponse(Call<Cameras> call, Response<Cameras> response) {
                                    Cameras cameras = response.body();
                                    device.setCameras(cameras.getCameras());
                                    testInterface.TestSuccessful(stringResponse, response.code());

                                }

                                @Override
                                public void onFailure(Call<Cameras> call, Throwable t) {
                                    t.printStackTrace();
                                    t.fillInStackTrace();
                                    testInterface.TestFailed(t.getMessage(), 700);
                                }
                            });


                        } catch (IOException e) {
                            e.printStackTrace();
                            testInterface.TestFailed(e.getMessage(), 700);

                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            testInterface.TestFailed(e.getMessage(), 700);

                        }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.printStackTrace();
                    t.fillInStackTrace();
                    testInterface.TestFailed(t.getMessage(), 700);

                }
            });
        }

        public static boolean isValidURL(String url) {
            return URLUtil.isValidUrl(url);
        }

        private interface TestInterface {
            void TestSuccessful(String response, int status);

            void TestFailed(String response, int status);

        }

        private void checkLogin(String serverurl, TestInterface testInterface) throws NoSuchAlgorithmException {
            String baseurl;
            if (!serverurl.contains("://"))
                baseurl = removeSlash("http://" + serverurl);
            else
                baseurl = removeSlash(serverurl);
            ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);

            MotionEyeHelper helper = new MotionEyeHelper();
            helper.setUsername(device.getUser().getUsername());
            helper.setPasswordHash(device.getUser().getPassword());
            String url = baseurl;

            url += "/login?_=" + new Date().getTime();
            helper.setLoggedIn(true);
            url = helper.addAuthParams("GET", url, "");
            Call<okhttp3.ResponseBody> checkLoginCall = apiInterface.loginResult(url);
            checkLoginCall.enqueue(new Callback<okhttp3.ResponseBody>() {
                @Override
                public void onResponse(Call<okhttp3.ResponseBody> call2, Response<okhttp3.ResponseBody> response2) {
                    if (!response2.isSuccessful()) {
                        try (ResponseBody responseBody2 = response2.errorBody()) {
                            final String responseString = responseBody2.string();
                            if (response2.code() == 403)
                                testInterface.TestFailed(getString(R.string.wizard_wrong_credentials), response2.code());
                            else
                                testInterface.TestFailed(responseString, response2.code());


                        } catch (Exception e) {
                            e.fillInStackTrace();
                            e.printStackTrace();
                            testInterface.TestFailed(e.getMessage(), 700);
                        }
                    } else {
                        try (ResponseBody responseBody2 = response2.body()) {
                            final String stringResponse2 = responseBody2.string();
                            testInterface.TestSuccessful(stringResponse2, response2.code());


                        } catch (Exception e) {
                            e.fillInStackTrace();
                            e.printStackTrace();
                            testInterface.TestFailed(e.getMessage(), 700);
                        }
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    t.fillInStackTrace();

                    t.printStackTrace();
                    testInterface.TestFailed(t.getMessage(), 700);

                }
            });
        }

        private static String removeSlash(String url) {
            if (!url.endsWith("/"))
                return url;
            String[] parts = url.split("/");

            return parts[0];
        }


        private String getFullUrl() {
            String serverurl;
            if (device.getDdnsURL().length() > 5) {
                if ((Utils.getNetworkType(getActivity())) == NETWORK_MOBILE) {
                    serverurl = device.getDDNSUrlCombo();
                } else if (device.getWlan() != null && device.getWlan().SSID.equals(Utils.getCurrentWifiNetworkId(getActivity()))) {
                    serverurl = device.getDeviceUrlCombo();

                } else {
                    serverurl = device.getDDNSUrlCombo();

                }
            } else {
                serverurl = device.getDeviceUrlCombo();

            }
            String baseurl;
            if (!serverurl.contains("://"))
                baseurl = Utils.removeSlash("http://" + serverurl);
            else
                baseurl = Utils.removeSlash(serverurl);
            return baseurl;
        }

        public void setDeviceId(String ID, FloatingActionButton button,  Context context) {
            Source source = new Source(context);
            try {
                device = source.get(ID);
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().finish();
            }

            this.validateButton = button;

        }

        private JSONObject getBasicSettingsJSON() throws JSONException {

            JSONObject settings = new JSONObject();
            settings.put("admin_username", config.getAdmin_username());
            settings.put("normal_username", config.getNormal_username());
            settings.put("show_advanced", config.isShow_advanced());
            JSONObject finalObject = new JSONObject();
            finalObject.put("main", settings);
            return finalObject;
        }

        private JSONObject getAdminUsernameSettingsJSON(String username) throws JSONException {

            JSONObject settings = new JSONObject();
            settings.put("admin_username", username);
            settings.put("normal_username", config.getNormal_username());
            settings.put("show_advanced", config.isShow_advanced());
            JSONObject finalObject = new JSONObject();
            finalObject.put("main", settings);
            return finalObject;
        }

        private JSONObject getNormalUsernameSettingsJSON(String username) throws JSONException {

            JSONObject settings = new JSONObject();

            settings.put("admin_username", config.getAdmin_username());
            settings.put("normal_username", username);
            settings.put("show_advanced", config.isShow_advanced());
            JSONObject finalObject = new JSONObject();
            finalObject.put("main", settings);
            return finalObject;
        }

        private JSONObject getAdvancedSettingsJSON(boolean showAdvanced) throws JSONException {

            JSONObject settings = new JSONObject();

            settings.put("admin_username", config.getAdmin_username());
            settings.put("normal_username", config.getNormal_username());
            settings.put("show_advanced", showAdvanced);
            JSONObject finalObject = new JSONObject();
            finalObject.put("main", settings);
            return finalObject;
        }

        @Override
        public void onCreatePreferences(Bundle bundle, String s) {

        }

        private boolean checkForDuplicate(String original, String newValue) {
            return original.equals(newValue);
        }


        private void changeSettings(JSONObject changes) {
            try {
                String url = getFullUrl() + "/config/0/set/?_=" + new Date().getTime();
                MotionEyeHelper helper = new MotionEyeHelper();
                helper.setUsername(device.getUser().getUsername());
                helper.setPasswordHash(device.getUser().getPassword());
                RequestBody body = RequestBody.create(JSON, changes.toString());

                url = helper.addAuthParams("POST", url, changes.toString());
                ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, getFullUrl());
                disableMEYESettings();


                apiInterface.changeMainConfig(url, body).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try (ResponseBody body = response.body()) {
                            assert body != null;
                            String responseString = body.string();
                            JSONObject object = new JSONObject(responseString);
                            if (!object.getString("error").equals("null")) {
                                Toast.makeText(getActivity(), object.getString("error"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        replaceDeviceInDB();

                        loadConfig();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                        loadConfig();

                    }
                });

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        }

        private void getAdvancedDetails() {


            ApiInterface apiInterface = null;
            try {
                apiInterface = ServiceGenerator.createService(ApiInterface.class, getFullUrl());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                setConfigValues();
                enableMEYESettings();
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            disableMEYESettings();

            apiInterface.getMotionDetails(getFullUrl() + "/version").enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    setConfigValues();
                    enableMEYESettings();
                        try {
                            final String stringResponse = response.body().string();
                            Document html = Jsoup.parse(stringResponse);
                            Elements elements = html.select("body");
                            String[] lines = elements.html().replace("\"", "").replace("\n", "").split("<br>");
                            for (String string : lines) {
                                String[] paramParts = string.split("=");
                                String paramName = paramParts[0].trim();
                                String paramValue = paramParts[1];
                                if (paramName.contains("hostname"))
                                    device.setDeviceName(paramValue);
                                else if (paramName.contains("motion_version"))
                                    device.setMotionVersion(paramValue);
                                else if (paramName.contains("os_version"))
                                    device.setOsVersion(paramValue);
                                else if (paramName.equals("version"))
                                    device.setMotioneyeVersion(paramValue);

                            }
                            try {
                                if (Double.valueOf(device.getMotioneyeVersion()) > 0.4 || Double.valueOf(device.getMotioneyeVersion()) == 0.4) {
                                    hideAdvancedSettingsSwitch();
                                    getActivity().setTitle(device.getDeviceName()+" "+getString(R.string.settings));
                                    config.setShow_advanced(true);
                                    setConfigValues();
                                    enableMEYESettings();

                                } else {
                                    if (config.isShow_advanced()) {
                                        getActivity().setTitle(device.getDeviceName()+" "+getString(R.string.settings));
                                        setConfigValues();
                                        enableMEYESettings();
                                    }
                                }
                            } catch (Exception ignored) {
                                if (config.isShow_advanced()) {
                                    getActivity().setTitle(device.getDeviceName()+" "+getString(R.string.settings));
                                    setConfigValues();
                                    enableMEYESettings();
                                }
                            }


                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    setConfigValues();
                    enableMEYESettings();
                }
            });


        }


        private void hideAdvancedSettingsSwitch() {
            Preference advancedSettings = findPreference("advancedSettings");
            advancedSettings.setVisible(false);
        }




        @SuppressLint("RestrictedApi")
        private void replaceDeviceInDB() {
            Source source = new Source(getActivity());
            try {
                source.editEntry(device);
                loadConfig();
                initNewtorkSettings();

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            if (networkChangesMade) {
                validateButton.setVisibility(View.VISIBLE);
                validateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validateSettings();
                        validateButton.setVisibility(View.GONE);
                    }
                });
            }
        }
    }





}
