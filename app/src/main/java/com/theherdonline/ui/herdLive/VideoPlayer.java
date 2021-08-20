package com.theherdonline.ui.herdLive;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.theherdonline.R;

public class VideoPlayer extends AppCompatActivity {

    VideoView mVideoView;
    MediaController mediaController;
    FrameLayout mclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();
        String url = intent.getStringExtra("path");


        mediaController = new MediaController(this);
        mVideoView = findViewById(R.id.mVideoView);
        mclose = findViewById(R.id.fm_closeVideo);
        mediaController.setAnchorView(mVideoView);
       /* Uri localUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.mvideo);

        String url = "https://hard-harvest-vod.s3-ap-southeast-2.amazonaws.com/vods/01FAHHNRHCEZ93KQTX3AFNVKAH/video_01FAHHNRHCEZ93KQTX3AFNVKAH.m3u8";
*/
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.setMediaController(mediaController);

        mVideoView.start();

        mclose.setOnClickListener(l -> {
            mVideoView.stopPlayback();
            finish();
        });

        // implement on completion listener on video view
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
              //  Toast.makeText(getApplicationContext(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

    }
}