package com.cj.video.activity;

import android.os.Bundle;
import android.widget.VideoView;

import com.cj.mvvmproject.R;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    private VideoView vvVideo;
    String geturl="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        vvVideo= (VideoView) findViewById(R.id.vvVideo);
        geturl=getIntent().getStringExtra("vvVideo");
        vvVideo.setVideoPath(geturl);
        vvVideo.start();
    }
}
