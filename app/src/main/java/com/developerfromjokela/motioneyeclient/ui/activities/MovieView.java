/*
 * Copyright (c) 2020 MotionEye Client by Developer From Jokela, All Rights Reserved.
 * Licensed with MIT
 */

package com.developerfromjokela.motioneyeclient.ui.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.CameraUser;
import com.developerfromjokela.motioneyeclient.classes.Media;
import com.developerfromjokela.motioneyeclient.ui.utils.MotionEyeMediaPlayer;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class MovieView extends MotionEyeActivity {

    private String TAG = this.getClass().getSimpleName();
    private Toolbar toolbar;
    private MotionEyeMediaPlayer videoView;
    private MediaController mc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            String baseurl = intent.getStringExtra("baseurl");
            CameraUser user = (CameraUser) intent.getSerializableExtra("user");

            Camera camera = (Camera) intent.getSerializableExtra("camera");
            Media media = (Media) intent.getSerializableExtra("media");

            setTitle(media.getPath().split("/")[media.getPath().split("/").length-1]);
            try {
                String url = baseurl + "/movie/"+camera.getId()+"/playback"+media.getPath()+"?_=" + new Date().getTime();
                MotionEyeHelper helper = new MotionEyeHelper();
                helper.setUsername(user.getUsername());
                helper.setPasswordHash(user.getPassword());
                url = helper.addAuthParams("GET", url, "");
                Log.e("RA", url);
                videoView = findViewById(R.id.videoView);
                TextView loadText = findViewById(R.id.videoLoadText);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.e("MovView", "Load done!");
                        findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                    }
                });
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                        snack(getString(R.string.video_playback_error, String.valueOf(i)));
                        findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                        return true;
                    }
                });
                mc = new MediaController(this);
                mc.show(500);
                mc.setAnchorView(videoView);
                mc.setMediaPlayer(videoView);
                Uri video = Uri.parse(url);
                videoView.setMediaController(mc);

                videoView.getBufferPercentage();
                videoView.setVideoURI(video);
                videoView.requestFocus();
                videoView.start();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

        } else
            finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movie_view, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.fullscreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                toggleHideyBar();
            }
        }
        return true;
    }

    /**
     * Detects and toggles immersive mode (also known as "hidey bar" mode).
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void toggleHideyBar() {

        // BEGIN_INCLUDE (get_current_ui_flags)
        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        // END_INCLUDE (get_current_ui_flags)
        // BEGIN_INCLUDE (toggle_ui_flags)
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            toolbar.setVisibility(View.GONE);
            Log.i(TAG, "Turning immersive mode mode on.");
            mc.hide();
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toolbar.setVisibility(View.VISIBLE);
                }
            });
        }

        newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
        //END_INCLUDE (set_ui_flags)
    }
}
