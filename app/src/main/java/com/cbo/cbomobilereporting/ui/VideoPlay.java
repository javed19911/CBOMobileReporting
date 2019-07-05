package com.cbo.cbomobilereporting.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.VideoView;


import com.cbo.cbomobilereporting.R;

/**
 * Created by AKSHIT on 29/06/2016.
 */
public class VideoPlay extends AppCompatActivity {

   VideoView videoView;
    DisplayMetrics displayMetrics;
    SurfaceView surfaceView;
    android.widget.MediaController mediaController;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_play);

        context = VideoPlay.this;
        videoView =(VideoView) findViewById(R.id.play_video);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {

            textView.setText("Video Play");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }
        getInit();


    }
    public void getInit(){

        mediaController = new android.widget.MediaController(context);
        displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        videoView.setMinimumHeight(height);
        videoView.setMinimumWidth(width);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath("/storage/extSdCard/video.mp4");
        videoView.start();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {

            finish();
        }

        return super.onOptionsItemSelected(item);


    }
}
