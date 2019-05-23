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

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.SwitchPreferenceCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.ApiInterface;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.ActionStatus;
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

public class DeviceSettings extends AppCompatActivity {




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
            preferencesFragment.setDeviceId(intent.getStringExtra("DeviceId"), this);
            getSupportFragmentManager().beginTransaction().add(R.id.devPreferencesFrame, preferencesFragment).commit();
        } else {
            finish();
        }


    }


    public static class DevicePreferences extends android.support.v7.preference.PreferenceFragmentCompat
    {

        private MainConfig config;
        private Device device;
        public static final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");


        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.devicepreferences);
            disableMEYESettings();


            {
                findPreference("admin_username").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = (EditText) dialogView.findViewById(R.id.settingParam);
                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

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
                findPreference("admin_password").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.settingdialog, null);
                        dialogBuilder.setView(dialogView);

                        dialogBuilder.setNegativeButton(R.string.close, null);

                        TextInputLayout layout = dialogView.findViewById(R.id.settingHead);
                        EditText editText = (EditText) dialogView.findViewById(R.id.settingParam);
                        editText.setText(preference.getSummary());
                        editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

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
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
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


            loadConfig();


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
                        config = response.body();
                        if (config.isShow_advanced()) {
                            getAdvancedDetails();

                        } else {
                            setConfigValues();
                            enableMEYESettings();
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

        private String getFullUrl() {
            String serverurl;
            if (device.getDdnsURL().length() > 5) {
                if ((Utils.getNetworkType(getActivity())) == NETWORK_MOBILE) {
                    serverurl = device.getDDNSUrlCombo();
                } else if (device.getWlan().networkId == Utils.getCurrentWifiNetworkId(getActivity())) {
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

        public void setDeviceId(String ID, Context context) {
            Source source = new Source(context);
            try {
                device = source.get(ID);
            } catch (Exception e) {
                e.printStackTrace();
                getActivity().finish();
            }

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


                ApiInterface apiInterface = ServiceGenerator.createService(ApiInterface.class, getFullUrl());
                disableMEYESettings();

                apiInterface.getMotionDetails(getFullUrl() + "/version").enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.headers().get("Server").toLowerCase().contains("motioneye")) {
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

                                    Log.e("DeviceSettings", paramName+":"+paramValue);
                                }
                                getActivity().setTitle(device.getDeviceName()+" "+getString(R.string.settings));
                                setConfigValues();
                                enableMEYESettings();



                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });


        }
    }





}
