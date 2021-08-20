package com.theherdonline.ui.herdLive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.takusemba.rtmppublisher.Publisher;
import com.takusemba.rtmppublisher.PublisherListener;
import com.theherdonline.R;
import com.theherdonline.databinding.ActivityGoLiveStreamBinding;
import com.theherdonline.databinding.FragmentGoLiveStreamingBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerAppCompatActivity;

public class GoLiveStreamActivity extends DaggerAppCompatActivity implements PublisherListener {


    private String mStramUrl = "", mAwsMediaLiveId = "";
    ActivityGoLiveStreamBinding mBinding;

    NetworkInterface mNetworkListener;
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;

    private Publisher publisher;

    CountDownTimer countDownTimer;
    int second = -1, minute, hour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_go_live_stream);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        mStramUrl = intent.getStringExtra("StreamURL");
        mAwsMediaLiveId = intent.getStringExtra("mMediaLiveId");

        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

        mBinding.liveLabel.setVisibility(View.GONE);
        if (mStramUrl.isEmpty()) {
            ActivityUtils.showToast(this, "url is empty");
        } else {
            initPublisher();
            mBinding.togglePublish.setOnClickListener(l -> {
                if (publisher.isPublishing()) {
                    publisher.stopPublishing();
                } else {
                    publisher.startPublishing();
                }
            });

            mBinding.toggleCamera.setOnClickListener(l -> {
                publisher.switchCamera();
            });

        }

        mBinding.imgCloseStream.setOnClickListener(l -> {
            ActivityUtils.showWarningDialog(GoLiveStreamActivity.this, "Stop Live?", "Would you like to end the live session?.", "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    stopSalesyardStream(mAwsMediaLiveId);
                }
            }, "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

        });
    }

    private void initPublisher() {

        publisher = new Publisher.Builder(this)
                .setGlView(mBinding.surfaceView)
                .setUrl(mStramUrl)
                .setSize(Publisher.Builder.DEFAULT_WIDTH, Publisher.Builder.DEFAULT_HEIGHT)
                .setAudioBitrate(Publisher.Builder.DEFAULT_AUDIO_BITRATE)
                .setVideoBitrate(Publisher.Builder.DEFAULT_VIDEO_BITRATE)
                .setCameraMode(Publisher.Builder.DEFAULT_MODE)
                .setListener(this)
                .build();
    }

    private void stopSalesyardStream(String awsMediaLiveId) {
        mMainActivityViewModel.stopSaleyardStream(awsMediaLiveId).observe(this, new DataObserver<ResAWSMedialiveChannel>() {

            @Override
            public void onSuccess(ResAWSMedialiveChannel data) {
                // mNetworkListener.onLoading(false);
                GoLiveStreamActivity.this.finish();
            }

            @Override
            public void onError(Integer code, String msg) {
                ActivityUtils.showWarningDialog(GoLiveStreamActivity.this, null, "\n" + msg);

            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onDirty() {

            }
        });
    }

    @Override
    public void onBackPressed() {
        ActivityUtils.showWarningDialog(GoLiveStreamActivity.this, "Stop Live?", "Would you like to end the live session?.", "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopSalesyardStream(mAwsMediaLiveId);
                GoLiveStreamActivity.super.onBackPressed();
            }
        }, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mStramUrl.isEmpty()) {
            updateControls();
        }
    }

    @Override
    public void onStarted() {
        ActivityUtils.showToast(this, getString(R.string.start_publishing));
        updateControls();
        startCounting();

    }


    @Override
    public void onStopped() {
        ActivityUtils.showToast(this, getString(R.string.stopped_publishing));
        updateControls();
        stopCounting();
    }


    @Override
    public void onDisconnected() {
        ActivityUtils.showToast(this, getString(R.string.disconnected_publishing));
        initPublisher();
        updateControls();
        stopCounting();

    }

    @Override
    public void onFailedToConnect() {
        ActivityUtils.showToast(this, getString(R.string.failed_publishing));
        updateControls();
        stopCounting();

    }

    private void updateControls() {
        mBinding.togglePublish.setImageResource(publisher.isPublishing() ? R.drawable.stream_stop : R.drawable.stream_record);
    }

    private void startCounting() {

        mBinding.liveLabel.setVisibility(View.VISIBLE);
        mBinding.imgCloseStream.setVisibility(View.GONE);
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                second++;
                mBinding.liveLabel.setText(recorderTime());
                Log.e("Data", "Recroding Time=> " + recorderTime());
            }

            public void onFinish() {

            }
        };
        countDownTimer.start();

    }

    //recorder time
    public String recorderTime() {
        if (second == 60) {
            minute++;
            second = 0;
        }
        if (minute == 60) {
            hour++;
            minute = 0;
        }
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }


    private void stopCounting() {

        mBinding.liveLabel.setText("");
        mBinding.liveLabel.setVisibility(View.GONE);
        mBinding.imgCloseStream.setVisibility(View.VISIBLE);
        //thread.interrupt();
        second = -1;
        minute = 0;
        hour = 0;
        countDownTimer.cancel();


    }

}