package com.theherdonline.ui.marketReport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentMarketReportDetailBinding;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Media;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import javax.inject.Inject;

import dagger.Lazy;

public class MarketReportDetailFragment extends HerdFragment {

    static public String ARG_data = "market-data";
    private final Handler mHandler = new Handler();
    FragmentMarketReportDetailBinding mBinding;
    BackPressInterface mListener;
    SaleyardInterface mSaleyardInterface;
    InterfaceContactCard mInterfaceContactCard;
    MarketReport mReport;
    String mAudioFileName;
    MediaPlayer mMediaPlayer;
    int mDuration;
    private final Runnable mRunnable = new Runnable() {

        @Override
        public void run() {
            if (mMediaPlayer != null) {

                int mDuration = mMediaPlayer.getDuration();
                int mCurrentPosition = mMediaPlayer.getCurrentPosition();
                String duration = getTotalDuration(mDuration);
                mBinding.tvStatus.setText(printPlayingStatus(mCurrentPosition, duration));

                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.pause();
                        updateUI(PlayerStatus.PAUSE);
                        Log.e("Data", "Stop When complete Audio..");
                    }
                });
            }
            //repeat above code every second
            mHandler.postDelayed(this, 10);
        }
    };
    EntitySaleyard mReportEntitySaleyard = null;
    boolean isPlay = true;
    String attachmeentURL, videoURL;
    View.OnClickListener mShareListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, AppConstants.MARKET_REPORT_SHARE + mReport.getId());
            startActivity(Intent.createChooser(shareIntent, "The Herd Online | Market Reports"));
        }
    };

    @Inject
    public MarketReportDetailFragment() {
    }

    public static MarketReportDetailFragment newInstance(Lazy<MarketReportDetailFragment> lazy, MarketReport marketReport) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_data, marketReport);
        MarketReportDetailFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(null,
                mExitListener, null, null, R.drawable.ic_button_share, mShareListener);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_report_detail, container, false);
        mMediaPlayer = new MediaPlayer();

        if (getArguments() != null) {

            mReport = getArguments().getParcelable(ARG_data);

            mBinding.tvTitleReport.setText(ActivityUtils.trimText(getContext(), mReport.getTitle()));
            mBinding.tvDescription.setText(ActivityUtils.trimText(getContext(), mReport.getDescription()));

            // sale at
            if (mReport.getSale_at() != null) {
                mBinding.linearSaleDate.setVisibility(View.VISIBLE);
                mBinding.tvSaleDate.setText(getContext().getString(R.string.txt_sale_date_s, ActivityUtils.trimText(getContext(), TimeUtils.BackendUTC2Local(mReport.getSale_at()))));
            } else {
                mBinding.linearSaleDate.setVisibility(View.GONE);
            }

            // address
            if (mReport.getEntitySaleyard() != null) {
                mBinding.linearSaleAddress.setVisibility(View.VISIBLE);
                mBinding.tvSaleAddress.setText(ActivityUtils.trimText(getContext(), mReport.getEntitySaleyard().getAddress()));
            } else {
                mBinding.linearSaleAddress.setVisibility(View.GONE);
            }
            // report date
            mBinding.tvVoiceReport.setText(getContext().getString(R.string.txt_report_date, ActivityUtils.trimText(getContext(), TimeUtils.BackendUTC2Local(mReport.getUpdated_at()))));


            mAudioFileName = mReport.getFull_path();

            //mBinding.cardViewPlayer.setVisibility(TextUtils.isEmpty(mAudioFileName) ? View.GONE : View.VISIBLE);

            if (!TextUtils.isEmpty(mAudioFileName)) {
                mBinding.cardViewPlayer.setVisibility(View.VISIBLE);
                mBinding.tvPlayerStatus.setText(R.string.txt_play_recording);
                loadingAudio(mAudioFileName);
            } else {
                mBinding.cardViewPlayer.setVisibility(View.GONE);
            }
            mReportEntitySaleyard = mReport.getEntitySaleyard();

            if (mReportEntitySaleyard == null) {
                mBinding.cardViewViewSaleyard.setVisibility(View.GONE);
            } else {
                mBinding.cardViewViewSaleyard.setVisibility(View.VISIBLE);
                mBinding.buttonViewSaleyard.setOnClickListener(l -> {
                    mSaleyardInterface.OnClickSaleyard(mReportEntitySaleyard);
                });
            }


            if (mReport.getUser() != null) {
                mBinding.includeContactCard.getRoot().setVisibility(View.VISIBLE);
                if (mInterfaceContactCard != null) {
                    UIUtils.showContactCard(getContext(), mBinding.includeContactCard, mReport.getUser(), mInterfaceContactCard);
                }
            }

            if (mReport.getReport_description() != null) {
                mBinding.cardViewReportNoteViews.setVisibility(View.VISIBLE);
                mBinding.tvNotesText.setText(mReport.getReport_description());
            } else {
                mBinding.cardViewReportNoteViews.setVisibility(View.GONE);
            }

            mBinding.cardViewViewAttachment.setVisibility(View.GONE);
            mBinding.cardViewReportVideo.setVisibility(View.GONE);

            if (ListUtils.emptyIfNull(mReport.getMediaList()).size() != 0) {

                for (int i = 0; i < mReport.getMediaList().size(); i++) {
                    Media media = mReport.getMediaList().get(i);

                    if (media.getCollection_name().equalsIgnoreCase(AppConstants.TAG_market_report_attachment)) {
                        media.setUrl_attchment(media.getUrl());
                        mBinding.cardViewViewAttachment.setVisibility(View.VISIBLE);
                        attachmeentURL = media.getUrl();

                    }
                    if (media.getCollection_name().equalsIgnoreCase(AppConstants.TAG_videos)) {
                        ActivityUtils.loadImage(getContext(), mBinding.imageViewVideo, media.getUrl(), R.drawable.logo);
                        mBinding.cardViewReportVideo.setVisibility(View.VISIBLE);
                        videoURL = media.getUrl();
                        mBinding.imageViewPlayVideoButton.setOnClickListener(l -> ActivityUtils.playVideo(getActivity(), media));
                    }

                }

            }

        }

        mBinding.cardViewViewAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.defaultString(attachmeentURL))));
            }
        });
        mBinding.buttonPlayAudio.setOnClickListener(l -> {
            if (isPlay) {
                isPlay = false;
                updateUI(PlayerStatus.PLAYING);
            } else {
                isPlay = true;
                updateUI(PlayerStatus.PAUSE);
            }


        });
        return mBinding.getRoot();

    }

    private void loadingAudio(String audioFileName) {

        try {
            // give data to mediaPlayer
            mMediaPlayer.setDataSource(audioFileName);
            // media player asynchronous preparation
            mMediaPlayer.prepareAsync();

            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                public void onPrepared(final MediaPlayer mp) {
                    mDuration = mMediaPlayer.getDuration();
                    mBinding.tvStatus.setText(getTotalDuration(mDuration));

                }
            });


        } catch (IOException e) {
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackPressInterface) {
            mListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (context instanceof InterfaceContactCard) {
            mInterfaceContactCard = (InterfaceContactCard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfaceContactCard");
        }

        if (context instanceof SaleyardInterface) {
            mSaleyardInterface = (SaleyardInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SaleyardInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onStop() {
        super.onStop();
        cleanWorkingField();
    }
    private void cleanWorkingField() {
        if (mMediaPlayer != null) {
            mHandler.removeCallbacks(mRunnable);
            mMediaPlayer.pause();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void updateUI(PlayerStatus playerStatus) {

        if (playerStatus == PlayerStatus.PLAYING) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_stopplayback);
            mBinding.buttonPlayAudio.setImageDrawable(drawable);
            mBinding.tvPlayerStatus.setText(R.string.txt_stop_playback);

            if (mAudioFileName != null) {
                mMediaPlayer.start();
                mRunnable.run();
            }

        } else if (playerStatus == PlayerStatus.PAUSE) {

            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_playrecording);
            mBinding.buttonPlayAudio.setImageDrawable(drawable);
            mBinding.tvPlayerStatus.setText(R.string.txt_play_recording);

            if (mAudioFileName != null) {
                mMediaPlayer.pause();
                mHandler.removeCallbacks(mRunnable);
            }

            mBinding.tvStatus.setText(getTotalDuration(mDuration));


        }
    }

    private String getTotalDuration(long millis) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        buf
                /*.append(String.format("%02d", hours))
                .append(":")*/
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    private String printPlayingStatus(long millis, String duration) {
        StringBuffer buf = new StringBuffer();

        long hours = millis / (1000 * 60 * 60);
        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000;

        buf
                /*.append(String.format("%02d", hours))
                .append(":")*/
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString() + "/ " + duration;
    }

    public enum PlayerStatus {
        PLAYING,
        PAUSE
    }
}



