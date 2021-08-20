package com.theherdonline.ui.herdLive;

import android.content.Context;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.takusemba.rtmppublisher.Publisher;
import com.takusemba.rtmppublisher.PublisherListener;
import com.theherdonline.R;
import com.theherdonline.databinding.FragmentGoLiveStreamingBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.PriceType;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import javax.inject.Inject;

import dagger.Lazy;


public class GoLiveStreamingFragment extends HerdFragment implements PublisherListener {

    public static String AGR_DATA = "StreamURL";

    private String mStramUrl;
    FragmentGoLiveStreamingBinding mBinding;

    NetworkInterface mNetworkListener;
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;

    private Publisher publisher;
    private Handler handler = new Handler();
    private Thread thread;
    private boolean isCounting = false;
    long updatedAt;

    @Inject
    public GoLiveStreamingFragment() {
        // Required empty public constructor
    }

    public static GoLiveStreamingFragment newInstance(Lazy<GoLiveStreamingFragment> mGoLiveStreamingFragmentProvider, String mStreamURL) {
        Bundle args = new Bundle();
        GoLiveStreamingFragment fragment = mGoLiveStreamingFragmentProvider.get();
        args.putString(AGR_DATA, mStreamURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        if (getArguments() != null) {
            mStramUrl = getArguments().getString(AGR_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_go_live_streaming, container, false);

        //mBinding.tvUrl.setText(mStramUrl);

        if (mStramUrl.isEmpty()) {
            ActivityUtils.showToast(requireActivity(), "url is empty");
        } else {
            publisher = new Publisher.Builder((AppCompatActivity) requireActivity())
                    .setGlView(mBinding.surfaceView)
                    .setUrl(mStramUrl)
                    .setSize(Publisher.Builder.DEFAULT_WIDTH, Publisher.Builder.DEFAULT_HEIGHT)
                    .setAudioBitrate(Publisher.Builder.DEFAULT_AUDIO_BITRATE)
                    .setVideoBitrate(Publisher.Builder.DEFAULT_VIDEO_BITRATE)
                    .setCameraMode(Publisher.Builder.DEFAULT_MODE)
                    .setListener(this)
                    .build();

            mBinding.togglePublish.setOnClickListener(l -> {
                if (publisher.isPublishing()) {
                    publisher.startPublishing();
                } else {
                    publisher.startPublishing();
                }
            });

            mBinding.toggleCamera.setOnClickListener(l -> {
                publisher.switchCamera();
            });


        }
        return mBinding.getRoot();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNetworkListener = null;
    }

    public Lazy<ViewModelFactory> getmLazyFactory() {
        return mLazyFactory;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }
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
        ActivityUtils.showToast(requireActivity(), getString(R.string.start_publishing));
        updateControls();
        startCounting();

    }


    @Override
    public void onStopped() {

        ActivityUtils.showToast(requireActivity(), getString(R.string.stopped_publishing));
        updateControls();
        stopCounting();
    }


    @Override
    public void onDisconnected() {
        ActivityUtils.showToast(requireActivity(), getString(R.string.disconnected_publishing));
        updateControls();
        stopCounting();
    }

    @Override
    public void onFailedToConnect() {
        ActivityUtils.showToast(requireActivity(), getString(R.string.failed_publishing));
        updateControls();
        stopCounting();

    }

    private void updateControls() {
        mBinding.togglePublish.setText(getString(publisher.isPublishing() ? R.string.start_publishing : R.string.start_publishing));
    }

    private void startCounting() {

        isCounting = true;
        mBinding.liveLabel.setText(getString(R.string.publishing_label, format(0l), format(0l)));
        //label.text = getString(R.string.publishing_label, 0L.format(), 0L.format())
        long startedAt = System.currentTimeMillis();
        updatedAt = System.currentTimeMillis();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (isCounting) {
                    if (System.currentTimeMillis() - updatedAt > 1000) {
                        updatedAt = System.currentTimeMillis();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                long diff = System.currentTimeMillis() - startedAt;
                                long second = diff / 1000 * 60;
                                long min = diff / 1000 / 60;
                                Log.e("Data", "Time => min: " + min + " : Second:=>" + second);
                                mBinding.liveLabel.setText(getString(R.string.publishing_label, format(min), format(second)));

                            }
                        });
                    }
                }
            }
        });
        thread.start();

    }

    private void stopCounting() {

        isCounting = false;
        mBinding.liveLabel.setText("");
        mBinding.liveLabel.setVisibility(View.GONE);
        thread.interrupt();

    }

    private String format(long mduration) {
        return String.format("%02d", this);
    }

}