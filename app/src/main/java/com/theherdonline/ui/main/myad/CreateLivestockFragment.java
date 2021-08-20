package com.theherdonline.ui.main.myad;

import android.app.Activity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentMarketReportCreateBinding;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.InterfacePermissionCheck;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.marketReport.CreateMarketReportViewModel;
import com.theherdonline.ui.marketReport.SelectSyleyardFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UIUtils;
import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class CreateLivestockFragment extends DaggerFragment {

    static public String ARG_data = "market-data";

    FragmentMarketReportCreateBinding mBinding;
    BackPressInterface mActivityListener;
    NetworkInterface mNetworkListener;
    InterfacePermissionCheck mInterfacePermissionCheck;
    InterfaceContactCard mInterfaceContactCard;

    MarketReport mMarketReport;
    MediaPlayer mMediaPlayer;
    MediaRecorder mAudioRecorder;
    String mAudioFileName;

    EntitySaleyard mSelectedEntitySaleyard;

    int mAudioDuration;
    MarketReport mReport;

    private List<EntitySaleyard> mEntitySaleyardList = null;

    @Inject
    Lazy<SelectSyleyardFragment> mSelectSyleyardFragmentLazy;

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;


    MainActivityViewModel mMainViewModel;
    CreateMarketReportViewModel mCreateMarketReportViewModel;



    @Inject
    AppUtil mAppUtil;

    Timer mPlayerTimer = new Timer();

    PlayerStatus mPlayStatus = PlayerStatus.IDLE;

    @Inject
    public CreateLivestockFragment() {
    }

    public static CreateLivestockFragment newInstance(Lazy<CreateLivestockFragment> lazy, MarketReport marketReport) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_data, marketReport);
        CreateLivestockFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_report_create, container, false);
        if (getArguments() != null)
        {
            // open from the market list
            mReport = getArguments().getParcelable(ARG_data);
            mAudioFileName = mReport.getPath();
            mBinding.tvSelectSaleyard.setVisibility(View.GONE);
            mBinding.tvSaleyardAuction.setVisibility(View.GONE);
           // mBinding.tvOr.setVisibility(View.GONE);
          //  mBinding.etNote.setEnabled(false);
            mBinding.etTitle.setEnabled(false);
            mBinding.lineLayoutPlayer.removeViewAt(0);
            mBinding.buttonPublish.setVisibility(View.GONE);
            if (mReport.getUser() != null)
            {
                mBinding.includeContactCard.getRoot().setVisibility(View.VISIBLE);
                if (mInterfaceContactCard != null) {
                    UIUtils.showContactCard(getContext(), mBinding.includeContactCard, mReport.getUser(), mInterfaceContactCard);
                }
            }
        }



        if (mReport == null) {
            mReport = new MarketReport();
            mBinding.etTitle.setText("");
         //   mBinding.etNote.setText("");
        } else {
            if (mReport.getTitle() != null) {
                mBinding.etTitle.setText(mReport.getTitle());
            }

            if (mReport.getDescription() != null) {
             //   mBinding.etNote.setText(mReport.getDescription());
            }
        }

        mBinding.tvSaleyardAuction.setOnClickListener(l -> {
            Activity activity = getActivity();
            if (activity != null) {
                // open sale yard select
                SelectSyleyardFragment f = SelectSyleyardFragment.newInstance(mSelectSyleyardFragmentLazy,
                        new SaleyardInterface() {
                            @Override
                            public void OnClickSaleyard(EntitySaleyard saleyard) {

                            }

                            @Override
                            public void OnClickSaleyardList(List<EntitySaleyard> saleyard) {

                            }

                            @Override
                            public void onClickShareSaleyard(EntitySaleyard saleyard) {

                            }



                            @Override
                            public void onUpdateSaleyard(EntitySaleyard saleyard) {

                            }

                            @Override
                            public void onConfirmSaleyardV2(EntitySaleyard newSaleyard, EntitySaleyard oldSaleyard) {

                            }

                            @Override
                            public void OnSelectSaleyard(EntitySaleyard saleyard) {
                                mSelectedEntitySaleyard = saleyard;
                                mBinding.tvSaleyardAuction.setText(ActivityUtils.trimText(getContext(), mSelectedEntitySaleyard.getName()));
                                mActivityListener.OnClickGoBackButton();
                            }

                            @Override
                            public void onClickSavedSaleyard(EntitySaleyard saleyard, ImageView view) {

                            }
                        });
                ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(),f, R.id.frameLayout_container);
            }
        });

        /*mBinding.buttonRecordAudio.setmPermissionListener(mInterfacePermissionCheck);
        mBinding.buttonRecordAudio.setmAudioRecordListener(new InterfaceAudioRecorder() {
            @Override
            public void startRecord(Boolean bStart) {
                if (bStart) {
                    // Start recording
                    if (mPlayStatus == PlayerStatus.IDLE) {
                        mAudioFileName = getActivity().getExternalCacheDir().getAbsolutePath();
                        mAudioFileName += "/market_report_audio.mp3";
                        mAudioRecorder = new MediaRecorder();
                        mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                        mAudioRecorder.setOutputFile(mAudioFileName);
                        mAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        try {
                            mAudioRecorder.prepare();
                            mAudioRecorder.start();
                            mPlayStatus = PlayerStatus.RECORDING;
                        } catch (IOException e) {
                            Log.e("record", "prepare() failed");
                            mPlayStatus = PlayerStatus.IDLE;
                        }
                    } else {
                        Toast.makeText(getContext(), getString(R.string.txt_record_after_finish_play), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // stop recording
                    mAudioRecorder.stop();
                    mAudioRecorder.release();
                    mAudioRecorder = null;
                    mPlayStatus = PlayerStatus.IDLE;
                    mReport.setPath(mAudioFileName);
                }
                updateMediaUI(mPlayStatus);
            }
        });*/


        mMediaPlayer = new MediaPlayer();
        //mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mAudioDuration = mp.getDuration();
                mp.start();
                mPlayStatus = PlayerStatus.PLAYING;
                mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause));
                mPlayerTimer = new Timer();
                mPlayerTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (mPlayStatus == PlayerStatus.PLAYING) {
                            int i = mMediaPlayer.getCurrentPosition();
                            mBinding.tvStatus.setText(String.format("Playing status: %d / %d", i, mAudioDuration));
                        }
                    }
                }, 1000, 500);
                updateMediaUI(mPlayStatus);

            }
        });
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_headset_mic));
                mPlayStatus = PlayerStatus.IDLE;
                mMediaPlayer.stop();
                updateMediaUI(mPlayStatus);
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_audio_failed));
                mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_headset_mic));
                mPlayStatus = PlayerStatus.IDLE;
                updateMediaUI(mPlayStatus);
                return false;
            }
        });

        //    String url = "https://sample-videos.com/audio/mp3/crowd-cheering.mp3"; //ActivityUtils.getImageAbsoluteUrl(path);

        mBinding.buttonPlayAudio.setOnClickListener(l -> {
            if (mAudioFileName == null)
            {
                Toast.makeText(getContext(),getString(R.string.txt_no_audio_file),Toast.LENGTH_SHORT).show();
                return;
            }
            switch (mPlayStatus) {
                case LOADING:
                    Toast.makeText(getContext(), "Loading Audio", Toast.LENGTH_LONG).show();
                    break;
                case PLAYING:
                    mMediaPlayer.pause();
                    mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_play_arrow));
                    mPlayStatus = PlayerStatus.PAUSE;
                    break;
                case IDLE:
                    try {
                        mReport.setFull_path(mAudioFileName);
                        if (mReport.getPath() != null) {
                            mMediaPlayer.reset();
                            if (mReport.getPath() != null) {
                                mMediaPlayer.setDataSource(ActivityUtils.getAudioAbsoluteUrl(mReport.getPath()));
                                mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_file_download));
                                mPlayStatus = PlayerStatus.LOADING;
                                mMediaPlayer.prepareAsync();
                            }
                        }
                    } catch (IOException e) {
                        mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_headset_mic));
                        mPlayStatus = PlayerStatus.IDLE;
                    }
                    break;
                case PAUSE:
                    mMediaPlayer.start();
                    mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause));
                    mPlayStatus = PlayerStatus.PLAYING;
                    break;
                case RECORDING:
                    Toast.makeText(getContext(), getString(R.string.txt_play_after_finish_record), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            updateMediaUI(mPlayStatus);

        });


        mBinding.buttonPublish.setOnClickListener(l -> {

            if (mPlayStatus == PlayerStatus.IDLE) {
                if (!ActivityUtils.checkInput(getContext(), mBinding.tvTitle.getText().toString(), getString(R.string.txt_title))) {
                    return;
                }
                mReport.setTitle(mBinding.etTitle.getText().toString());
                if (mReport.getPath() == null || (!ActivityUtils.isAudioFile(getContext(), mReport.getPath()))) {
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_audio_is_required));
                    return;
                }
                mReport.setUser_id(mAppUtil.getUserId());
              //  if (!StringUtils.isEmpty(mBinding.etNote.getText().toString())) {
               //     mReport.setDescription(mBinding.etNote.getText().toString());
              //  }
                mReport.setUser_id(mAppUtil.getUserId());
                mCreateMarketReportViewModel.postMarketReport(mReport);
            }
            else if (mPlayStatus == PlayerStatus.RECORDING)
            {
                Toast.makeText(getContext(),getString(R.string.txt_stop_recording),Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getContext(),getString(R.string.txt_stop_playing),Toast.LENGTH_SHORT).show();
            }
        });

        return mBinding.getRoot();
    }


    public void updateMediaUI(PlayerStatus playerStatus)
    {
      //  mBinding.tvRecorderStatus.setText(playerStatus == PlayerStatus.RECORDING ? R.string.txt_recording : R.string.txt_record);
        if (playerStatus == PlayerStatus.PLAYING)
        {
            mBinding.tvPlayerStatus.setText(R.string.txt_playing);
        }
        else if (playerStatus == PlayerStatus.LOADING)
        {
            mBinding.tvPlayerStatus.setText(R.string.txt_loading);
        }
        else if (playerStatus == PlayerStatus.PAUSE)
        {
            mBinding.tvPlayerStatus.setText(R.string.txt_pause);
        }
        else
        {
            mBinding.tvPlayerStatus.setText(R.string.txt_listen);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mSelectedEntitySaleyard != null) {
            mBinding.tvSaleyardAuction.setText(ActivityUtils.trimText(getContext(), mSelectedEntitySaleyard.getName()));
        }
        if (mReport != null) {
            if (mReport.getTitle() != null) {
                mBinding.etTitle.setText(mReport.getTitle());
            }

         //   if (mReport.getDescription() != null) {
          //      mBinding.etNote.setText(mReport.getDescription());
        //    }
        }
        if (mAppUtil.getUserId() != null)
        {
            if (mEntitySaleyardList == null)
            {
                mCreateMarketReportViewModel.refreshSaleyard(mAppUtil.getUserId());
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMainViewModel.refreshSelectedSaleyardMarketReport(null);
    }


    @Override
    public void onStop() {
        super.onStop();

        if (mAudioRecorder != null) {
            // clean Audio Recorder
            mAudioRecorder.stop();
            mAudioRecorder.release();
            mAudioRecorder = null;
        }
        if (mMediaPlayer != null) {
            // clean up Audio player
            mMediaPlayer.release();
            mMediaPlayer = null;
            mPlayerTimer.cancel();
        }

        mPlayStatus = PlayerStatus.IDLE;

        if (mAudioFileName != null)
        {
            ActivityUtils.deleteFile(mAudioFileName);
            mAudioFileName = null;
        }


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BackPressInterface) {
            mActivityListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }


        if (context instanceof NetworkInterface) {
            mNetworkListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }

        if (context instanceof InterfacePermissionCheck) {
            mInterfacePermissionCheck = (InterfacePermissionCheck) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfacePermissionCheck");
        }

        if (context instanceof InterfaceContactCard) {
            mInterfaceContactCard = (InterfaceContactCard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfaceContactCard");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivityListener = null;
        mNetworkListener = null;
        mInterfacePermissionCheck = null;
        mInterfaceContactCard = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        mMainViewModel.getmMarketReportSelectedSaleyard().observe(this, new Observer<EntitySaleyard>() {
            @Override
            public void onChanged(@Nullable EntitySaleyard entitySaleyard) {
                if (entitySaleyard != null) {
                    mBinding.tvSaleyardAuction.setText(ActivityUtils.trimText(getContext(), entitySaleyard.getName()));
                    if (mReport != null) {
                        mReport.setSaleyard_id(entitySaleyard.getId());
                    }
                }
            }
        });
        mCreateMarketReportViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(CreateMarketReportViewModel.class);
        mCreateMarketReportViewModel.getmLiveDataPostMarketReport().observe(this, new DataObserver<MarketReport>() {
            @Override
            public void onSuccess(MarketReport data) {
                mNetworkListener.onLoading(false);
                mCreateMarketReportViewModel.postMarketReport(null);
                ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_create_a_new_market_report), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mActivityListener.OnClickGoBackButton();
                    }
                });
            }

            @Override
            public void onError(Integer code, String msg) {
                mNetworkListener.onLoading(false);
                mNetworkListener.onFailed(code, msg);
            }

            @Override
            public void onLoading() {
                mNetworkListener.onLoading(true);
            }

            @Override
            public void onDirty() {
                mNetworkListener.onLoading(false);
            }
        });

        mCreateMarketReportViewModel.getmLiveDataSaleyard().observe(this, new DataObserver<ResPagerSaleyard>() {
            @Override
            public void onSuccess(ResPagerSaleyard data) {
                mNetworkListener.onLoading(false);
               // mBinding.tvSaleyardAuction.setClickable(false);
                mEntitySaleyardList = data.getData();
            }

            @Override
            public void onError(Integer code, String msg) {
                mNetworkListener.onLoading(false);
                mNetworkListener.onFailed(code,msg);
            }

            @Override
            public void onLoading() {
                mNetworkListener.onLoading(true);
            }

            @Override
            public void onDirty() {
                mNetworkListener.onLoading(false);
            }
        });


    }


    public void enableChooseSaleyardButton(Boolean bEnable)
    {
        mBinding.tvSaleyardAuction.setEnabled(bEnable);
    }

    public enum PlayerStatus {
        RECORDING,
        LOADING,
        PLAYING,
        PAUSE,
        IDLE
    }

    public interface RecordAudioInterface {
        void requestRecord(Boolean bStart);

        Boolean checkoutPermission();
        //void stopRecord();
    }
}
