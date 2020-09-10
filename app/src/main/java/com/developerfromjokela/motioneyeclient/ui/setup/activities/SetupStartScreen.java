/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.setup.activities;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.ApiInterface;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.api.ServiceGenerator;
import com.developerfromjokela.motioneyeclient.classes.CameraUser;
import com.developerfromjokela.motioneyeclient.classes.Cameras;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.WifiNetwork;
import com.developerfromjokela.motioneyeclient.database.Source;
import com.developerfromjokela.motioneyeclient.ui.adapters.WifisAdapter;
import com.developerfromjokela.motioneyeclient.ui.utils.URLUtils;
import com.developerfromjokela.motioneyeclient.ui.utils.WifiInfoConverter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetupStartScreen extends AppCompatActivity {

    int currentView = 0;
    private Button continue_btn;
    private Button previous;
    private final int[] views = {R.layout.setup_main_screen, R.layout.setup_device_network, R.layout.setup_device_credentials, R.layout.setup_device_wifi, R.layout.setup_device_validate, R.layout.setup_device_finish};
    private Device.Builder device;
    private View.OnClickListener previousListener;
    private View.OnClickListener continueListener;
    private int locPermissionRequest = 54454;

    private View currentViewObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_screen_template);
        continue_btn = findViewById(R.id.continue_btn);
        previous = findViewById(R.id.back_btn);
        device = new Device.Builder();
        previousListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (views.length > currentView) {
                    currentView = currentView - 1;

                    setCurrentViewToViewStub(currentView);
                    if (currentView == 0)
                        previous.setEnabled(false);
                    else
                        previous.setEnabled(true);
                    if (currentView == views.length - 1)
                        continue_btn.setEnabled(false);
                    else
                        continue_btn.setEnabled(true);
                } else {
                    previous.setEnabled(false);
                }
            }
        };
        continueListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentView < views.length) {
                    currentView = currentView + 1;
                    setCurrentViewToViewStub(currentView);
                    if (currentView == views.length - 1)
                        continue_btn.setEnabled(false);
                    else
                        continue_btn.setEnabled(true);
                    previous.setEnabled(true);
                } else {
                    continue_btn.setEnabled(false);
                }
            }
        };

        initializeViewStub();


    }

    private void initializeViewStub() {
        final LinearLayout ll = findViewById(R.id.ll);

        final View inflated = LayoutInflater.from(SetupStartScreen.this).inflate(views[0], ll, false);
        ll.addView(inflated);
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_right_in);
        animation.setStartOffset(0);
        inflated.startAnimation(animation);
        previous.setEnabled(false);
        setListenersForSetupItems(inflated, 0);

    }

    private View setCurrentViewToViewStub(final int view) {

        final LinearLayout ll = findViewById(R.id.ll);
        final View[] inflated = {null};


        final View viewToDelete = ll.getChildAt(0);
        AlphaAnimation fadeOut = new AlphaAnimation(1f, 0f);
        fadeOut.setDuration(300);
        fadeOut.setFillAfter(true);
//Create move up animation
        TranslateAnimation moveUp = new TranslateAnimation(0, 0, 0, -viewToDelete.getHeight() / 6);
        moveUp.setDuration(300);
        moveUp.setFillAfter(true);
//Merge both animations into an animation set
        AnimationSet animations = new AnimationSet(false);
        animations.addAnimation(fadeOut);
        animations.addAnimation(moveUp);
        animations.setDuration(200);
        animations.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Hide the view after the animation is done to prevent it to show before it is removed from the parent view
                viewToDelete.setVisibility(View.GONE);
                //Create handler on the current thread (UI thread)
                Handler h = new Handler();
                //Run a runnable after 100ms (after that time it is safe to remove the view)
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ll.removeView(viewToDelete);
                        inflated[0] = LayoutInflater.from(SetupStartScreen.this).inflate(views[view], ll, false);
                        ll.addView(inflated[0]);
                        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.slide_right_in);
                        animation.setStartOffset(0);
                        animation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                setListenersForSetupItems(inflated[0], currentView);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        inflated[0].startAnimation(animation);
                        fillItems(inflated[0], currentView);
                        currentViewObject = inflated[0];


                    }
                }, 100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        viewToDelete.startAnimation(animations);
        //Create fade out animation

        return inflated[0];


    }

    private void fillItems(View view, final int position) {
        if (currentView == 1) {
            continue_btn.setEnabled(false);

            final EditText local_hostname = view.findViewById(R.id.local_hostname);
            final EditText ddns_hostname = view.findViewById(R.id.ddns_hostname);
            final EditText ddns_port = view.findViewById(R.id.ddns_port);
            final EditText local_port = view.findViewById(R.id.local_port);
            local_hostname.setText(device.getDeviceUrl());
            ddns_hostname.setText(device.getDdnsURL());
            ddns_port.setText(device.getDDNSPort());
            local_port.setText(device.getPort());
            if (URLUtils.validIP(device.getDeviceUrl())) {


                continue_btn.setEnabled(true);
            } else {

                continue_btn.setEnabled(false);
            }
            if (!device.getDdnsURL().contains("//") && device.getDdnsURL().length() > 7) {
                ddns_hostname.setText("http://" + device.getDdnsURL());
            }

        } else if (currentView == 2) {
            final EditText username = view.findViewById(R.id.login);
            final EditText password = view.findViewById(R.id.password);
            if (device.getUser() != null) {
                username.setText(device.getUser().getUsername());
                password.setText(device.getUser().getPassword());
                if (!device.getUser().getUsername().isEmpty()) {
                    continue_btn.setEnabled(true);
                } else
                    continue_btn.setEnabled(false);
            } else {
                continue_btn.setEnabled(false);
            }

        } else if (currentView == 3) {
            continue_btn.setEnabled(false);
            final ListView wifiNetwork = view.findViewById(R.id.wifiNetwork);
            final TextView emptyView = view.findViewById(R.id.emptyView);
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locPermissionRequest);
                wifiNetwork.setVisibility(View.GONE);
                CardView noLocationLayout = view.findViewById(R.id.permissionWarning);
                noLocationLayout.setVisibility(View.VISIBLE);
            } else {
                initWifiPicker(wifiNetwork, emptyView);
            }

        } else if (currentView == 4) {
            continue_btn.setEnabled(false);

        } else if (currentView == 5) {
            try {
                Source source = new Source(SetupStartScreen.this);
                source.createEntry(device.build());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Setup", "CurrentView 5 " + e.getMessage());
            }
            previous.setEnabled(false);
            continue_btn.setEnabled(true);
            continue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            continue_btn.setText(R.string.wizard_finish);
            TextView successtext = view.findViewById(R.id.successtext);
            successtext.setText(getString(R.string.wizard_completed, device.getDeviceName()));
        }
    }

    private void initWifiPicker(ListView wifiNetwork, TextView emptyText) {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        int netId = -1;
        final List<WifiNetwork> SSIDCONFIGS = new ArrayList<>();

        ConnectivityManager connManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        int connectedIndex = -1;
        WifiNetwork connectedNet = null;
        if (mWifi.isConnected())
            connectedNet = new WifiNetwork(WifiInfoConverter.infoToConfiguration(wifiManager.getConnectionInfo()), false);

        for (WifiConfiguration tmp : wifiManager.getConfiguredNetworks()) {
            SSIDCONFIGS.add(new WifiNetwork(tmp, ((connectedNet != null && connectedNet.getConfiguration().SSID.equals(tmp.SSID)) || tmp.status == WifiConfiguration.Status.CURRENT)));
            if ((connectedNet != null && connectedNet.getConfiguration().SSID.equals(tmp.SSID)) || tmp.status == WifiConfiguration.Status.CURRENT)
                connectedIndex = SSIDCONFIGS.size() - 1;
        }
        if (SSIDCONFIGS.isEmpty()) {
            if (mWifi.isConnected()) {
                SSIDCONFIGS.add(connectedNet);
            }
        }

        final int[] itemPosition = {connectedIndex};


        if (device.getWlan() != null) {


            for (int i = 0; i < wifiManager.getConfiguredNetworks().size(); i++) {
                WifiConfiguration tmp = wifiManager.getConfiguredNetworks().get(i);

                if (tmp.networkId == device.getWlan().networkId) {
                    SSIDCONFIGS.get(i).setSelected(true);
                    itemPosition[0] = i;
                    continue_btn.setEnabled(true);
                }
            }
        } else {
            continue_btn.setEnabled(false);

        }

        Collections.sort(SSIDCONFIGS, new Comparator<WifiNetwork>() {
            @Override
            public int compare(WifiNetwork o1, WifiNetwork o2) {
                return o1.getConfiguration().SSID.compareTo(o2.getConfiguration().SSID);
            }
        });

        WifisAdapter adapter = new WifisAdapter(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, SSIDCONFIGS);
        continue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                device.setWlan(SSIDCONFIGS.get(itemPosition[0]).getConfiguration());
                continueListener.onClick(v);
            }
        });
        // Assign adapter to ListView
        wifiNetwork.setAdapter(adapter);
        wifiNetwork.setEmptyView(emptyText);
        if (connectedIndex != -1) {
            wifiNetwork.smoothScrollToPosition(connectedIndex);
            continue_btn.setEnabled(true);
        }
        // ListView Item Click Listener
        wifiNetwork.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item index
                if (itemPosition[0] != -1) {
                    SSIDCONFIGS.get(itemPosition[0]).setSelected(false);
                }
                itemPosition[0] = position;

                SSIDCONFIGS.get(position).setSelected(true);

                adapter.notifyDataSetChanged();

                device.setWlan(SSIDCONFIGS.get(itemPosition[0]).getConfiguration());
                // Show Alert

                continue_btn.setEnabled(true);
            }
        });

        Button skip = findViewById(R.id.skip_wifi);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                device.setWlan(null);
                continue_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        continueListener.onClick(continue_btn);
                    }
                });
                continueListener.onClick(continue_btn);
            }
        });
    }


    private static String removeSlash(String url) {
        if (url.endsWith("/")) {
            return url.substring(0, url.length() - 1);
        } else
            return url;
    }

    private void setListenersForSetupItems(View view, final int position) {

        Log.e("Setup", String.valueOf(position));
        Log.e("Setup", String.valueOf(currentView));

        if (currentView == 0) {
            continue_btn.setOnClickListener(continueListener);
            previous.setOnClickListener(previousListener);
        } else if (currentView == 1) {

            final EditText local_hostname = view.findViewById(R.id.local_hostname);
            final EditText ddns_hostname = view.findViewById(R.id.ddns_hostname);
            final EditText ddns_port = view.findViewById(R.id.ddns_port);
            final EditText local_port = view.findViewById(R.id.local_port);
            continue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    device.setDeviceUrl(local_hostname.getText().toString());
                    device.setLocalPort(local_port.getText().toString());
                    device.setDdnsURL(ddns_hostname.getText().toString());
                    device.setDDNSPort(ddns_port.getText().toString());
                    continueListener.onClick(v);
                }
            });
            local_hostname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String url = s.toString();


                    if (!url.contains(":/") && url.contains(":")) {

                        final String[] portparts = url.split(":");
                        if (portparts.length == 2) {
                            String portString = portparts[portparts.length - 1];
                            try {
                                int port = Integer.parseInt(portString);
                                device.setDeviceUrl(portparts[0]);
                                local_port.setText(String.valueOf(port));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        local_hostname.setText(portparts[0]);
                                        local_hostname.setSelection(local_hostname.getText().length());

                                        Log.e("Setup", "Set Local Port " + portparts[1]);
                                    }
                                }, 1000);
                            } catch (Exception ignored) {
                            }

                        }


                    } else if (url.contains("://")) {
                        if (url.split("://").length >= 2) {
                            String nURL = url.split("://")[1];
                            final String[] portparts = nURL.split(":");
                            if (portparts.length == 2) {
                                if (portparts[1].equals("/"))
                                    return;
                                String portString = portparts[portparts.length - 1];
                                try {
                                    int port = Integer.parseInt(portString);
                                    device.setDeviceUrl(url.split("://")[0] + "://" + portparts[0]);
                                    local_port.setText(String.valueOf(port));
                                    String finalUrl = url;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            local_hostname.setText(finalUrl.split("://")[0] + "://" + portparts[0]);
                                            local_hostname.setSelection(local_hostname.getText().length());

                                            Log.e("Setup", "Set Local Port2 " + portparts[1]);
                                        }
                                    }, 1000);
                                } catch (Exception ignored) {
                                }

                            }
                            continue_btn.setEnabled(URLUtil.isValidUrl(url));

                        }
                    }

                    Log.e("SSS", url);
                    if (!URLUtil.isValidUrl(url)) {
                        url = "http://" + url;
                        if (URLUtil.isValidUrl(url))
                            device.setDeviceUrl(url);

                        continue_btn.setEnabled(URLUtil.isValidUrl(url));
                    } else
                        continue_btn.setEnabled(true);
                }
            });
            ddns_hostname.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String url = s.toString();

                    if (!url.contains(":/") && url.contains(":")) {


                        final String[] portparts = url.split(":");
                        if (portparts.length == 2) {
                            String portString = portparts[portparts.length - 1];
                            try {
                                int port = Integer.parseInt(portString);
                                device.setDdnsURL(portparts[0]);
                                ddns_port.setText(String.valueOf(port));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ddns_hostname.setText(portparts[0]);
                                        ddns_hostname.setSelection(ddns_hostname.getText().length());

                                        Log.e("Setup", "Set Local Port 1" + portparts[1]);
                                    }
                                }, 1000);
                            } catch (Exception ignored) {
                            }
                        }


                    } else if (url.contains("://")) {
                        if (url.split("://").length >= 2) {
                            String nURL = url.split("://")[1];
                            final String[] portparts = nURL.split(":");
                            if (portparts.length == 2) {
                                if (portparts[1].equals("/"))
                                    return;
                                String portString = portparts[portparts.length - 1];
                                try {
                                    int port = Integer.parseInt(portString);
                                    device.setDdnsURL(url.split("://")[0] + "://" + portparts[0]);
                                    ddns_port.setText(String.valueOf(port));
                                    String finalUrl = url;
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            ddns_hostname.setText(finalUrl.split("://")[0] + "://" + portparts[0]);
                                            ddns_hostname.setSelection(ddns_hostname.getText().length());

                                            Log.e("Setup", "Set Local Port 2" + portparts[1]);
                                        }
                                    }, 1000);
                                } catch (Exception ignored) {
                                }
                            }
                            continue_btn.setEnabled(URLUtil.isValidUrl(url));

                        }
                    }
                    if (!isValidURL(url) && url.length() > 2) {

                        if (isValidURL("http://" + url)) {
                            device.setDdnsURL("http://" + url);
                            continue_btn.setEnabled(true);

                        } else {
                            continue_btn.setEnabled(false);

                        }

                    } else {
                        device.setDdnsURL(url);
                        continue_btn.setEnabled(true);
                    }
                }
            });
            ddns_port.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (ddns_hostname.getText().toString().split(":").length > 2) {
                        String url_noports = ddns_hostname.getText().toString().split(":")[0] + ddns_hostname.getText().toString().split(":")[1];
                        device.setDdnsURL(url_noports + ":" + s.toString());
                    } else if (ddns_hostname.getText().toString().split(":").length > 1) {
                        device.setDdnsURL(ddns_hostname.getText().toString() + ":" + s.toString());

                    }
                    device.setDDNSPort(s.toString());

                }
            });
            local_port.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    device.setLocalPort(s.toString());

                }
            });
        } else if (currentView == 2) {
            final EditText username = view.findViewById(R.id.login);
            final EditText password = view.findViewById(R.id.password);
            continue_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    device.setUser(new CameraUser(username.getText().toString(), password.getText().toString()));
                    continueListener.onClick(v);
                }
            });
            username.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    device.setUser(new CameraUser(s.toString(), password.getText().toString()));
                    if (s.length() > 0)
                        continue_btn.setEnabled(true);
                    else
                        continue_btn.setEnabled(false);
                }
            });
            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    device.setUser(new CameraUser(username.getText().toString(), s.toString()));
                }
            });
        } else if (currentView == 3) {
            Button requestLocation = view.findViewById(R.id.loc_request);
            requestLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(SetupStartScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locPermissionRequest);
                }
            });
        } else if (currentView == 4) {
            // Result Textview
            TextView Errorstatus = view.findViewById(R.id.errorDetails);
            // Root Layouts
            LinearLayout rootLayout2 = view.findViewById(R.id.ddns_connection_test);
            // Progressbars
            ProgressBar progressBar1 = view.findViewById(R.id.progress1);
            ProgressBar progressBar2 = view.findViewById(R.id.progress2);
            ProgressBar progressBar3 = view.findViewById(R.id.progress3);
            ProgressBar progressBar4 = view.findViewById(R.id.progress4);

            // Icons
            ImageView status1 = view.findViewById(R.id.statusimage1);
            ImageView status2 = view.findViewById(R.id.statusimage2);
            ImageView status3 = view.findViewById(R.id.statusimage3);
            ImageView status4 = view.findViewById(R.id.statusimage4);


            if (device.getDdnsURL().length() > 0) {
                rootLayout2.setVisibility(View.VISIBLE);
            }


            Log.e("SETUP", device.getDeviceUrlCombo());
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
                                                    continue_btn.setEnabled(true);
                                                    status4.setVisibility(View.VISIBLE);
                                                    progressBar4.setVisibility(View.GONE);
                                                    status4.setImageResource(R.drawable.ic_check_green);

                                                }

                                                @Override
                                                public void TestFailed(String response, int status) {
                                                    continue_btn.setEnabled(false);

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
                                            continue_btn.setEnabled(false);
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
                                }
                            }

                            @Override
                            public void TestFailed(String response, int status) {
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
                                    continue_btn.setEnabled(true);
                                    progressBar3.setVisibility(View.GONE);
                                    status3.setVisibility(View.VISIBLE);
                                    status3.setImageResource(R.drawable.ic_check_green);
                                    status4.setVisibility(View.GONE);
                                    progressBar4.setVisibility(View.VISIBLE);
                                    getServerDetails(new TestInterface() {
                                        @Override
                                        public void TestSuccessful(String response, int status) {
                                            continue_btn.setEnabled(true);

                                            status4.setVisibility(View.VISIBLE);
                                            progressBar4.setVisibility(View.GONE);
                                            status4.setImageResource(R.drawable.ic_check_green);

                                        }

                                        @Override
                                        public void TestFailed(String response, int status) {
                                            continue_btn.setEnabled(false);

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
                                    continue_btn.setEnabled(false);
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
                        }
                    }

                }

                @Override
                public void TestFailed(String response, int status2) {
                    progressBar1.setVisibility(View.GONE);
                    status1.setVisibility(View.VISIBLE);
                    status1.setImageResource(R.drawable.ic_error_red);
                    Errorstatus.setVisibility(View.VISIBLE);
                    Errorstatus.setText(response);
                }
            }, device.getDeviceUrlCombo());

        }

    }

    private void getServerDetails(TestInterface testInterface, String serverurl) {
        Log.e("Setup", serverurl);
        String baseurl;

        Log.e("Setup", String.valueOf(serverurl.split("//").length));
        if (!serverurl.contains("://"))
            baseurl = removeSlash("http://" + serverurl);
        else
            baseurl = removeSlash(serverurl);

        Log.e("Setup", baseurl);
        ApiInterface apiInterface = null;
        try {
            apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            testInterface.TestFailed(e.getMessage(), 700);
            return;
        }
        Call<ResponseBody> call = apiInterface.getMotionDetails(baseurl + "/version");
        Log.e("Setup", baseurl);
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
                            String paramValue = paramParts[1].trim();
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
                                Log.e("Setup", cameras.getCameras().size() + " cameras found");
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

    private void validateServer(TestInterface testInterface, String serverurl) {
        Log.e("Setup1", serverurl);
        String baseurl;

        Log.e("Setup2", String.valueOf(serverurl.split("//").length));
        if (!serverurl.contains("://"))
            baseurl = removeSlash("http://" + serverurl);
        else
            baseurl = removeSlash(serverurl);

        Log.e("Setup3", baseurl);
        ApiInterface apiInterface = null;
        try {
            apiInterface = ServiceGenerator.createService(ApiInterface.class, baseurl);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            testInterface.TestFailed(e.getMessage(), 700);
            return;
        }
        Call<ResponseBody> call = apiInterface.login(baseurl + "/login", device.getUser().getUsername(), device.getUser().getPassword(), "login");
        Log.e("Setup", baseurl);
        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("Setup", call.request().body().contentType().toString());
                try {
                    if (response.isSuccessful())
                        testInterface.TestSuccessful(Jsoup.parse(response.body().string()).wholeText(), response.code());
                    else
                        testInterface.TestFailed(Jsoup.parse(response.errorBody().string()).wholeText(), response.code());
                } catch (Exception e) {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == locPermissionRequest) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (currentView == 3) {
                    currentViewObject.findViewById(R.id.permissionWarning).setVisibility(View.GONE);
                    currentViewObject.findViewById(R.id.wifiNetwork).setVisibility(View.VISIBLE);
                    initWifiPicker(currentViewObject.findViewById(R.id.wifiNetwork), currentViewObject.findViewById(R.id.emptyView));
                }
            }
        }
    }
}
