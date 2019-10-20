package com.developerfromjokela.motioneyeclient.ui.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.developerfromjokela.motioneyeclient.R;
import com.developerfromjokela.motioneyeclient.api.MotionEyeHelper;
import com.developerfromjokela.motioneyeclient.classes.Camera;
import com.developerfromjokela.motioneyeclient.classes.CameraUser;
import com.developerfromjokela.motioneyeclient.classes.Device;
import com.developerfromjokela.motioneyeclient.classes.Media;
import com.developerfromjokela.motioneyeclient.other.Utils;
import com.squareup.picasso.Picasso;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static android.app.DownloadManager.Request.NETWORK_MOBILE;
import static com.developerfromjokela.motioneyeclient.other.Utils.removeSlash;

public class MovieView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                VideoView videoView = findViewById(R.id.videoView);
                TextView loadText = findViewById(R.id.videoLoadText);
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        Log.e("MovView", "Load done!");
                        findViewById(R.id.loadingLayout).setVisibility(View.GONE);
                    }
                });
                MediaController mc = new MediaController(this);
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

}
