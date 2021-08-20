package com.theherdonline.ui.marketReport;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentMarketReportCreateBinding;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.general.InterfacePermissionCheck;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.herdinterface.SaleyardInterface;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.view.DateTimePicker;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;
import com.theherdonline.util.UIUtils;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.Lazy;

public class CreateMarketReportFragment extends HerdFragment {

    static public String ARG_data = "market-data";
    final AnimationDrawable mAnimation = new AnimationDrawable();
    FragmentMarketReportCreateBinding mBinding;
    BackPressInterface mActivityListener;
    NetworkInterface mNetworkListener;
    InterfacePermissionCheck mInterfacePermissionCheck;
    InterfaceContactCard mInterfaceContactCard;
    SaleyardInterface mSaleyardInterface;
    MarketReport mMarketReport;
    MediaPlayer mMediaPlayer;
    MediaRecorder mAudioRecorder;
    String mAudioFileName;
    EntitySaleyard mSelectedEntitySaleyard;
    Boolean mIsNeedRefresh = false;

    EntitySaleyard mReportEntitySaleyard = null;
    int mAudioDuration;
    DateTime mAudioDurationDateTime;

    MarketReport mReport;
    @Inject
    Lazy<SelectSyleyardFragment> mSelectSyleyardFragmentLazy;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    MainActivityViewModel mMainViewModel;
    CreateMarketReportViewModel mCreateMarketReportViewModel;
    @Inject
    AppUtil mAppUtil;
    Timer mPlayerTimer = new Timer();
    Timer mRecoderTimer = new Timer();
    PlayerStatus mPlayStatus = PlayerStatus.IDLE;
    Boolean mIsCreateUI = false;
    Boolean mAudioReady = false;
    Boolean isActive = false, ispermissionGranted = false;
    private List<EntitySaleyard> mEntitySaleyardList = null;

    @Inject
    public CreateMarketReportFragment() {
    }

    public static CreateMarketReportFragment newInstance(Lazy<CreateMarketReportFragment> lazy, MarketReport marketReport) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_data, marketReport);
        CreateMarketReportFragment fragment = lazy.get();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(getString(mIsCreateUI ? R.string.txt_create_report : R.string.txt_null),
                mExitListener, null, null, null, null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_report_create, container, false);
        mMediaPlayer = new MediaPlayer();

        if (getArguments() != null && getArguments().getParcelable(ARG_data) != null) {
            // open from the market list
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

            mReportEntitySaleyard = mReport.getEntitySaleyard();

            if (mReportEntitySaleyard == null) {
                mBinding.cardViewViewSaleyard.setVisibility(View.GONE);
            } else {
                mBinding.cardViewViewSaleyard.setVisibility(View.VISIBLE);
                mBinding.buttonViewSaleyard.setOnClickListener(l -> {
                    mSaleyardInterface.OnClickSaleyard(mReportEntitySaleyard);
                });
            }

            mBinding.buttonPublish.setVisibility(View.GONE);
            if (mReport.getUser() != null) {
                mBinding.includeContactCard.getRoot().setVisibility(View.VISIBLE);
                if (mInterfaceContactCard != null) {
                    UIUtils.showContactCard(getContext(), mBinding.includeContactCard, mReport.getUser(), mInterfaceContactCard);
                }
            }

            //loadingAudio(mAudioFileName);

/*            Media media1 = new Media();
            media1.setUrl("https://herd-web-app.s3.amazonaws.com/livestock/media/1312/video.mp4");
            media1.setName("videos");
            mReport.getMediaList().add(media1);*/


            mBinding.linearDeleteVideo.setVisibility(View.INVISIBLE);
            mBinding.linearDeleteAttachment.setVisibility(View.INVISIBLE);
            mBinding.linearUploadVideo.setVisibility(View.GONE);
            mBinding.linearUploadAttachment.setVisibility(View.GONE);
            mBinding.cardViewReportAttachment.setVisibility(View.GONE);
            mBinding.cardViewReportNoteViews.setVisibility(TextUtils.isEmpty(mReport.getReport_description()) ? View.GONE : View.VISIBLE);


            mBinding.tvNotesText.setText(mReport.getReport_description());

            mBinding.cardViewAttachmentPreview.setVisibility(View.GONE);
            mBinding.cardViewReportVideo.setVisibility(View.GONE);
            if (ListUtils.emptyIfNull(mReport.getMediaList()).size() != 0) {

                for (int i = 0; i < mReport.getMediaList().size(); i++) {
                    Media media = mReport.getMediaList().get(i);

                    if (media.getCollection_name().equalsIgnoreCase(AppConstants.TAG_market_report_attachment)) {
                        media.setUrl_attchment(media.getUrl());
                        mBinding.cardViewAttachmentPreview.setVisibility(View.VISIBLE);
                    } else {
                        mBinding.cardViewAttachmentPreview.setVisibility(View.GONE);
                    }
                    if (media.getCollection_name().equalsIgnoreCase(AppConstants.TAG_videos)) {
                        ActivityUtils.loadImage(getContext(), mBinding.imageViewVideo, media.getUrl(), R.drawable.logo);
                        mBinding.cardViewReportVideo.setVisibility(View.VISIBLE);
                        mBinding.imageViewPlayVideoButton.setOnClickListener(l -> ActivityUtils.playVideo(getActivity(), media));
                    } else {
                        mBinding.cardViewReportVideo.setVisibility(View.GONE);
                    }

                }

            }
            mIsCreateUI = false;
        } else {
            // create

            mBinding.imageViewPlayVideoButton.setVisibility(View.GONE);
            mBinding.imageViewVideo.setVisibility(View.GONE);
            mBinding.imageViewAttachment.setVisibility(View.GONE);
            mBinding.linearDeleteVideo.setVisibility(View.INVISIBLE);
            mBinding.linearDeleteAttachment.setVisibility(View.INVISIBLE);
            mBinding.linearUploadVideo.setVisibility(View.GONE);
            mBinding.linearUploadAttachment.setVisibility(View.GONE);

            mBinding.includeContactCard.getRoot().setVisibility(View.GONE);
            mReport = new MarketReport();
            mAudioFileName = null;
            mIsCreateUI = true;
            mReportEntitySaleyard = null;

        }

        mBinding.linearDeleteVideo.setOnClickListener(l -> {

            ActivityUtils.showWarningDialog(getContext(), R.string.txt_delete_video, R.string.txt_delete_video_info
                    , R.string.txt_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMainViewModel.deleteMarketReportVideo();
                            dialog.dismiss();
                        }
                    },
                    R.string.txt_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );


        });
        mBinding.linearDeleteAttachment.setOnClickListener(l -> {

            ActivityUtils.showWarningDialog(getContext(), R.string.txt_delete_attachment, R.string.txt_delete_attachment_info
                    , R.string.txt_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMainViewModel.deleteMarketReportAttachment();
                            dialog.dismiss();
                        }
                    },
                    R.string.txt_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }
            );


        });

        mBinding.buttonUploadVideo.setOnClickListener(l -> {

            if (checkPermissionGranted()) {
                ((MainActivity) getActivity()).popupUploadVideoDialog(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO, R.array.pickup_video_report_options);
            } /*else {
                ActivityUtils.showToast(requireActivity(), getString(R.string.permission_msg));
            }*/
        });

        mBinding.buttonUploadAttachment.setOnClickListener(l -> {
            if (checkPermissionGranted()) {
                ((MainActivity) getActivity()).popupUploadAttachmentDialog(AppConstants.ACTIVITY_CODE_MARKET_REPORT_UPLOAD_TAKE_PHOTO, R.array.pickup_mediaAttachment_options);
                //((MainActivity)getActivity()).popupPhotoDialog(getString(R.string.txt_add_a_photo), AppConstants.ACTIVITY_CODE_POST_TAKE_PHOTO);
            }/*else {
                ActivityUtils.showToast(requireActivity(), getString(R.string.permission_msg));
            }*/
        });

        mBinding.imageViewPlayVideoButton.setOnClickListener(l -> {
            if (mPlayStatus != null && mPlayStatus != PlayerStatus.IDLE) {
                ActivityUtils.showToast(getContext(), getString(R.string.txt_please_stop_audio_playing));
                return;
            } else {
                Media media = mMainViewModel.getmLiveDataMarketReportVideo().getValue();
                if (media != null) {
                    ActivityUtils.playVideo(getContext(), media);

                    // log an event.
                    if (!mIsCreateUI) {
                        if (mMainActivity != null && mReport != null) {
                            mMainActivity.logFirebaseEventPlayMarketReport(mReport);
                        }
                    }
                }
            }
        });

        mBinding.includeContactCard.getRoot().setVisibility(mIsCreateUI ? View.GONE : View.VISIBLE);
        mBinding.linearCreate.setVisibility(!mIsCreateUI ? View.GONE : View.VISIBLE);
        mBinding.cardViewReport.setVisibility(mIsCreateUI ? View.GONE : View.VISIBLE);
        mBinding.cardViewViewSaleyard.setVisibility(mIsCreateUI || mReportEntitySaleyard == null ? View.GONE : View.VISIBLE);
        mBinding.lineLayoutPlayer.setVisibility(View.VISIBLE);
        mBinding.buttonPublish.setVisibility(!mIsCreateUI ? View.GONE : View.VISIBLE);
        Drawable drawable1 = ContextCompat.getDrawable(getContext(), R.drawable.button_startrecording);
        drawable1.setTint(ContextCompat.getColor(getContext(), R.color.colorButton));
        Drawable drawable2 = ContextCompat.getDrawable(getContext(), R.drawable.button_startrecording);
        drawable2.setTint(ContextCompat.getColor(getContext(), android.R.color.holo_red_dark));
        mAnimation.addFrame(drawable1, 500);
        mAnimation.addFrame(drawable2, 500);
        mBinding.tvSaleyardAuction.setOnClickListener(l -> {
            if (mPlayStatus != PlayerStatus.IDLE) {
                Toast.makeText(getContext(), getString(R.string.txt_stop_recording_then_try_again), Toast.LENGTH_SHORT).show();
                return;
            }

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
                ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(), f, R.id.frameLayout_container);
            }
        });


        mBinding.buttonPlayAudio.setOnClickListener(l -> {
            /*if (mAudioFileName == null) {
                Toast.makeText(getContext(), getString(R.string.txt_no_audio_file), Toast.LENGTH_SHORT).show();
                return;
            }*/

            if (mIsCreateUI) {
                if (!mInterfacePermissionCheck.AudioRecordPermission()) {
                    return;
                }
            }


            switch (mPlayStatus) {
                case LOADING:
                    //Toast.makeText(getContext(), "Loading Audio", Toast.LENGTH_LONG).show();
                    break;
                case PLAYING:
                    Log.e("Data", "Playing from button click");
                    if (mMediaPlayer != null) {
                        mMediaPlayer.stop();

                    }
                    loadingAudio(mAudioFileName);

                    // mPlayStatus = PlayerStatus.IDLE;
                    // updateMediaUI(mPlayStatus);
                    break;
                case IDLE:
                    Log.e("Data", "Ideal from button click");
                    if (mPlayStatus == PlayerStatus.ERROR) {
                        Toast.makeText(getContext(), getString(R.string.txt_audio_is_not_available), Toast.LENGTH_LONG).show();
                        return;
                    }
                    // log an event.
                    if (!mIsCreateUI) {
                        if (mMainActivity != null && mReport != null) {
                            mMainActivity.logFirebaseEventPlayMarketReport(mReport);
                        }
                    }
                    if (mAudioFileName != null) {

                        Log.e("Data", "Ideal from button click with filePath");

                        mAudioDuration = mMediaPlayer.getDuration();
                        mAudioDurationDateTime = new DateTime(mAudioDuration);
                        mMediaPlayer.start();
                        mPlayStatus = PlayerStatus.PLAYING;
                        updateMediaUI(mPlayStatus);
                        mPlayerTimer = new Timer();
                        mPlayerTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mPlayStatus == PlayerStatus.PLAYING) {
                                                int current = mMediaPlayer.getCurrentPosition();
                                                if (isActive) {
                                                    //mBinding.tvStatus.setText(getString(R.string.txt_number_second,current));
                                                    mBinding.tvStatus.setText(printPlayingStatus(current, mAudioDuration));
                                                }
                                            }
                                        }
                                    });
                                }
                            }
                        }, 1000, 500);




/*                        try {
                            mReport.setPath(mAudioFileName);
                            if (mReport.getFull_path() != null) {
                                mMediaPlayer.reset();
                                if (mReport.getFull_path() != null) {
                                    mMediaPlayer.setDataSource(ActivityUtils.getAudioAbsoluteUrl(mReport.getFull_path()));
                                    mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_file_download));
                                    mPlayStatus = PlayerStatus.LOADING;
                                    mMediaPlayer.prepareAsync();
                                }
                            }
                        } catch (IOException e) {
                            mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_button_stoprecording));
                            mPlayStatus = PlayerStatus.IDLE;
                        }*/
                        updateMediaUI(mPlayStatus);
                    } else {
                        if (mIsCreateUI) {
                            //
                            if (mAudioFileName != null && new File(mAudioFileName).exists()) {
                                Toast.makeText(getContext(), R.string.txt_delete_audio_before_record, Toast.LENGTH_LONG).show();
                                return;
                            }
                            mAudioFileName = getActivity().getExternalCacheDir().getAbsolutePath();
                            mAudioFileName += "/market_report_audio.mp3";
                            mAudioRecorder = new MediaRecorder();
                            mAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                            mAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                            mAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                            mAudioRecorder.setOutputFile(mAudioFileName);
                            try {
                                mAudioRecorder.prepare();
                                mAudioRecorder.start();
                                mPlayStatus = PlayerStatus.RECORDING;
                                updateMediaUI(mPlayStatus);
                            } catch (IOException e) {
                                Log.e("record", "prepare() failed");
                                mPlayStatus = PlayerStatus.IDLE;
                            }
                            mRecoderTimer = new Timer();
                            DateTime startTime = new DateTime();
                            mRecoderTimer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    if (getActivity() != null) {
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                DateTime currentTime = new DateTime();
                                                Long second = (currentTime.getMillis() - startTime.getMillis()) / 1000;
                                                if (isActive) {
                                                    mBinding.tvStatus.setText(printRecordingStatus(currentTime.getMillis() - startTime.getMillis()));
                                                }
                                            }
                                        });
                                    }
                                }
                            }, 1000, 1000);
                        }
                    }
                    break;
                case PAUSE:
                    //mMediaPlayer.start();
                    //mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_pause));
                    //mPlayStatus = PlayerStatus.PLAYING;
                    break;
                case RECORDING:
                    //Toast.makeText(getContext(), getString(R.string.txt_play_after_finish_record), Toast.LENGTH_SHORT).show();
                    // stop recording
                    mAudioRecorder.stop();
                    mAudioRecorder.release();
                    mAudioRecorder = null;
                    mPlayStatus = PlayerStatus.IDLE;
                    updateMediaUI(mPlayStatus);
                    mReport.setPath(mAudioFileName);
                    //loadingAudio(mAudioFileName);
                    mMainViewModel.addAudioToNewMarketReport(mAudioFileName);

                    break;
                default:
                    break;
            }
            updateMediaUI(mPlayStatus);
        });

        mBinding.linearDelete.setOnClickListener(l -> {
            if (mIsCreateUI && mAudioFileName != null && new File(mAudioFileName).exists()) {
                ActivityUtils.showWarningDialog(getContext(), R.string.txt_delete_recording, R.string.txt_delete_recording_info
                        , R.string.txt_delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new File(mAudioFileName).delete();
                                mAudioFileName = null;
                                mPlayStatus = PlayerStatus.IDLE;
                                updateMediaUI(mPlayStatus);
                            }
                        },
                        R.string.txt_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }
                );
            }
        });

        mBinding.buttonPublish.setOnClickListener(l -> {

            if (mPlayStatus == PlayerStatus.IDLE) {
                if (!ActivityUtils.checkInput(getContext(), mBinding.etTitle.getText().toString(), getString(R.string.txt_title))) {
                    return;
                }
                mReport.setTitle(mBinding.etTitle.getText().toString());
                mReport.setReport_description(mBinding.etNotes.getText().toString());
                if (TextUtils.isEmpty(mReport.getTitle())) {
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_saleyard_title_is_required));
                    return;
                }


                Media media = mMainViewModel.getmLiveDataMarketReportVideo().getValue();
                if (media != null) {
                    String path = media.getUrl();
                    if (path != null && new File(path).isFile()) {
                        List<Media> list = new ArrayList<>();
                        list.add(media);
                        mReport.setMediaList(list);
                    }
                }
                Media media_attachment = mMainViewModel.getmLiveDataMarketReportAttachment().getValue();
                if (media_attachment != null) {
                    String path = media_attachment.getUrl_attchment();
                    if (path != null && new File(path).isFile()) {
                        List<Media> list = new ArrayList<>();
                        list.add(media_attachment);
                        mReport.setMediaList(list);
                    }
                }

                if ((mReport.getFull_path() == null || (!ActivityUtils.isAudioFile(getContext(), mReport.getFull_path())))
                        && ListUtils.emptyIfNull(mReport.getMediaList()).size() == 0) {
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.txt_recording_or_video_missing), getString(R.string.txt_recording_or_video_missing_info));
                    return;
                }

                /*if (mReport.getFull_path() == null || (!ActivityUtils.isAudioFile(getContext(), mReport.getFull_path()))) {
                    ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name), getString(R.string.txt_audio_is_required));
                    return;
                }*/
                mReport.setUser_id(mAppUtil.getUserId());
                if (!StringUtils.isEmpty(mBinding.etNote.getText().toString())) {
                    mReport.setDescription(mBinding.etNote.getText().toString());
                }
                mReport.setUser_id(mAppUtil.getUserId());


                String sale_at = (String) mBinding.tvSelectSaleDate.getTag();
                if (TextUtils.isEmpty(sale_at)) {
                    sale_at = null;
                }
                mReport.setSale_at(sale_at);


                mCreateMarketReportViewModel.postMarketReport(mReport);
            } else if (mPlayStatus == PlayerStatus.RECORDING) {
                Toast.makeText(getContext(), getString(R.string.txt_stop_recording), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.txt_stop_playing), Toast.LENGTH_SHORT).show();
            }
        });


        return mBinding.getRoot();
    }

    private boolean checkPermissionGranted() {


        Dexter.withActivity(requireActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {

                            ispermissionGranted = true;
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            ispermissionGranted = false;
                            ActivityUtils.showToast(requireActivity(), getString(R.string.permission_msg));
                            //mGobackListener.OnClickGoBackButton();
                            //showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(requireActivity(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();

        return ispermissionGranted;
    }


    //    loadingAudio(mAudioFileName);
    public Boolean loadingAudio(String audioPath) {
        mReport.setFull_path(audioPath);
        try {
            if (mReport.getFull_path() != null) {
                if (mMediaPlayer != null) {
                    mMediaPlayer.reset();
                    mMediaPlayer = null;
                }
                mMediaPlayer = new MediaPlayer();
                mBinding.tvPlayerStatus.setText(R.string.txt_loading_audio);
                mMediaPlayer.setDataSource(ActivityUtils.getAudioAbsoluteUrl(mReport.getFull_path()));
                mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_file_download));
                mPlayStatus = PlayerStatus.LOADING;
                mMediaPlayer.prepareAsync();
                updateMediaUI(mPlayStatus);
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mAudioDuration = mp.getDuration();
                        mBinding.tvStatus.setText(printAudioDuration(mAudioDuration));
                        mBinding.tvPlayerStatus.setText(R.string.txt_play_recording);
                        mPlayStatus = PlayerStatus.IDLE;
                        updateMediaUI(mPlayStatus);
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // mMediaPlayer.seekTo(0);
                        // mPlayStatus = PlayerStatus.IDLE;
                        // updateMediaUI(mPlayStatus);
                        //loadingAudio(mAudioFileName);
                        mMainViewModel.addAudioToNewMarketReport(mAudioFileName);
                    }
                });
                mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mAudioReady = false;
                        mPlayStatus = PlayerStatus.ERROR;
                        updateMediaUI(mPlayStatus);
                        return false;
                    }
                });
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            mBinding.buttonPlayAudio.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.button_startrecording));
            mPlayStatus = PlayerStatus.IDLE;
            updateMediaUI(mPlayStatus);
            return false;
        }
    }

    public String printPlayingStatus(int current, int duration) {
        DateTime curDateTime = new DateTime(current);
        DateTime durDateTime = new DateTime(duration);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("mm:ss");
        return fmt.print(curDateTime) + "/" + fmt.print(duration);
    }

    public String printAudioDuration(int duration) {
        DateTime durDateTime = new DateTime();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("mm:ss");
        return fmt.print(duration);
    }


    public String printRecordingStatus(long current) {
        DateTime curDateTime = new DateTime(current);
        DateTimeFormatter fmt = DateTimeFormat.forPattern("mm:ss");
        return fmt.print(curDateTime);
    }


    public void updateMediaUI(PlayerStatus playerStatus) {
        mBinding.tvPlayerStatus.setText(playerStatus == PlayerStatus.RECORDING ? R.string.txt_stop_recording_text : R.string.txt_record);
        if (mAudioFileName == null) {
            mBinding.tvStatus.setText("");
        }

        Drawable drawable = null;
        mBinding.buttonPlayAudio.setEnabled(playerStatus != PlayerStatus.LOADING);
        if (playerStatus == PlayerStatus.PLAYING) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_stopplayback);
            mBinding.tvPlayerStatus.setText(R.string.txt_stop_playback);
        } else if (playerStatus == PlayerStatus.LOADING) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_playrecording);
            mBinding.tvPlayerStatus.setText(R.string.txt_loading_audio);
        } else if (playerStatus == PlayerStatus.PAUSE) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_stopplayback);
            mBinding.tvPlayerStatus.setText(R.string.txt_pause);
        } else if (playerStatus == PlayerStatus.ERROR) {
            drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_playrecording);
            mBinding.tvPlayerStatus.setText(R.string.txt_audio_is_not_available);
        } else if (playerStatus == PlayerStatus.RECORDING) {
            mBinding.buttonPlayAudio.setImageDrawable(null);
            mBinding.buttonPlayAudio.setBackground(mAnimation);
            mAnimation.start();

        } else {
            if (mRecoderTimer != null) {

                mRecoderTimer.cancel();
                mRecoderTimer.purge();
                mRecoderTimer = null;
            }
            if (mPlayerTimer != null) {
                mPlayerTimer.cancel();
                mPlayerTimer.purge();
                mPlayerTimer = null;
            }
            if (mAudioFileName != null && new File(mAudioFileName).exists()) {
                drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_playrecording);

            } else {
                if (mIsCreateUI) {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_stoprecording);
                } else {
                    drawable = ContextCompat.getDrawable(getContext(), R.drawable.button_playrecording);
                }
            }

            //mBinding.tvStatus.setText("");
            mBinding.buttonPlayAudio.setBackground(null);
            mBinding.tvPlayerStatus.setText(R.string.txt_play_recording);
            mAnimation.stop();
        }

        if (playerStatus == PlayerStatus.IDLE && mIsCreateUI && mAudioFileName != null && new File(mAudioFileName).exists()) {
            mBinding.linearDelete.setVisibility(View.VISIBLE);
        } else {
            mBinding.linearDelete.setVisibility(View.INVISIBLE);

        }

        mBinding.buttonPlayAudio.setImageDrawable(drawable);
    }


    @Override
    public void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.tvUnlink.setVisibility(mSelectedEntitySaleyard == null ? View.GONE : View.VISIBLE);
        mBinding.tvSelectSaleDate.setOnClickListener(l -> {
            DateTime cur = mBinding.tvSelectSaleDate.getTag() == null ? new DateTime() : TimeUtils.apiString2Time((String) mBinding.tvSelectSaleDate.getTag());
            DateTimePicker newFragment = DateTimePicker.newInstance(new DateTimePicker.InterfaceTimeDate() {
                @Override
                public void setTimeDate(DateTime dateTime) {
                    mBinding.tvSelectSaleDate.setTag(TimeUtils.time2ApiString(dateTime));
                    mBinding.tvSelectSaleDate.setText(TimeUtils.time2String(dateTime));
                }
            }, cur);
            newFragment.show(getChildFragmentManager(), "dialog");
        });

        if (mSelectedEntitySaleyard != null) {
            mBinding.tvSaleyardAuction.setText(ActivityUtils.trimText(getContext(), mSelectedEntitySaleyard.getName()));
            mBinding.etTitle.setText(ActivityUtils.trimText(getContext(), mSelectedEntitySaleyard.getName()));
            //mBinding.etTitle.setText(ActivityUtils.trimText(getContext(), mSelectedEntitySaleyard.getReport_description()));
            if (mSelectedEntitySaleyard.getHeadcount() == null) {
                mBinding.etNote.setText("");

            } else {
                mBinding.etNote.setText(mSelectedEntitySaleyard.getHeadcount() + " " + mSelectedEntitySaleyard.getSaleyardCategory().getName());

            }
            mBinding.tvSelectSaleDate.setText(TimeUtils.BackendUTC2Local(mSelectedEntitySaleyard.getStartsAt()));
            mBinding.tvSelectSaleDate.setTag(TimeUtils.BackendUTC2LocalApiFormat(mSelectedEntitySaleyard.getStartsAt()));

            mBinding.tvUnlink.setOnClickListener(l -> {
                mBinding.tvSaleyardAuction.setText("");
                mBinding.etTitle.setText("");
                mBinding.etNotes.setText("");
                mBinding.etNote.setText("");
                mBinding.tvSelectSaleDate.setText("");
                mBinding.tvSelectSaleDate.setTag(null);
                mSelectedEntitySaleyard = null;
                mBinding.tvUnlink.setVisibility(mSelectedEntitySaleyard == null ? View.GONE : View.VISIBLE);

            });
        }
        if (mReport != null) {
            if (mReport.getTitle() != null) {
                mBinding.etTitle.setText(mReport.getTitle());
            }

            if (mReport.getDescription() != null) {
                mBinding.etNote.setText(mReport.getDescription());
            }
        }
        if (mAppUtil.getUserId() != null) {
            if (mEntitySaleyardList == null) {
                mCreateMarketReportViewModel.refreshSaleyard(mAppUtil.getUserId());
            }
        }
        isActive = true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setIsRefreashTag(mIsNeedRefresh);

    }


    public void cleanWorkingField() {
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
        }

        if (mPlayerTimer != null) {
            mPlayerTimer.cancel();
            mPlayerTimer.purge();
            mPlayerTimer = null;
        }


        if (mRecoderTimer != null) {
            mRecoderTimer.cancel();
            mRecoderTimer.purge();
            mRecoderTimer = null;
        }

        mPlayStatus = PlayerStatus.IDLE;
        /*if (mAudioFileName != null) {
            ActivityUtils.deleteFile(mAudioFileName);
            mAudioFileName = null;
        }*/
    }

    @Override
    public void onStop() {
        super.onStop();
        cleanWorkingField();
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
        mActivityListener = null;
        mNetworkListener = null;
        mInterfacePermissionCheck = null;
        mInterfaceContactCard = null;
        mSaleyardInterface = null;
        mMainViewModel.refreshSelectedSaleyardMarketReport(null);
        mMainViewModel.deleteMarketReportVideo();
        mMainViewModel.deleteMarketReportAudio();
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

        mAudioFileName = mMainViewModel.getmLiveDataMarketReportAudio().getValue();
        updateMediaUI(mPlayStatus);

        if (mReport != null) {
            if (ListUtils.emptyIfNull(mReport.getMediaList()).size() != 0) {
                mMainViewModel.addVideoToNewMarketReport(mReport.getMediaList().get(0).getUrl());
            }
        }

        if (mIsCreateUI) {
            if (mMainViewModel.getmLiveDataMarketReportAudio().getValue() == null) {
                mMainViewModel.getmLiveDataMarketReportAudio().setValue(null);
            }
        } else {
            if (mReport != null && (!StringUtils.isEmpty(mReport.getFull_path()))) {
                mAudioFileName = mReport.getFull_path();
                mMainViewModel.addAudioToNewMarketReport(mReport.getFull_path());
            } else {
                mMainViewModel.addAudioToNewMarketReport(null);
            }
        }


        if (mMainViewModel.getmLiveDataMarketReportVideo().getValue() == null) {
            mMainViewModel.addVideoToNewMarketReport(null);
        }
        if (mMainViewModel.getmLiveDataMarketReportAttachment().getValue() == null) {
            mMainViewModel.addAttachmentToNewMarketReport(null);
        }


        mMainViewModel.getmLiveDataMarketReportAudio().observe(requireActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (mIsCreateUI) {
                    if (s == null) {
                        //updateMediaUI(PlayerStatus.IDLE);
                        mBinding.tvPlayerStatus.setText(R.string.txt_start_recording);
                        mBinding.buttonPlayAudio.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_startrecording));
                        mBinding.linearDeleteVideo.setVisibility(View.INVISIBLE);
                        mBinding.linearDeleteAttachment.setVisibility(View.INVISIBLE);
                    } else {
                        mBinding.linearDelete.setVisibility(View.VISIBLE);
                        if (ActivityUtils.isAudioFile(getContext(), s)) {
                            loadingAudio(s);
                        }
                    }
                } else {
                    if (s == null) {
                        mBinding.cardViewPlayer.setVisibility(View.GONE);
                    } else {
                        mBinding.cardViewPlayer.setVisibility(View.VISIBLE);
                        updateMediaUI(PlayerStatus.IDLE);
                        loadingAudio(s);
                    }
                }
            }
        });


        mMainViewModel.getmLiveDataMarketReportVideo().observe(requireActivity(), new Observer<Media>() {
            @Override
            public void onChanged(@Nullable Media media) {
                if (mIsCreateUI) {
                    if (media == null) {
                        mBinding.imageViewVideo.setVisibility(View.GONE);
                        //  mBinding.imageViewAttachment.setVisibility(View.GONE);
                        mBinding.imageViewPlayVideoButton.setVisibility(View.GONE);
                        mBinding.linearUploadVideo.setVisibility(View.VISIBLE);
                        // mBinding.linearUploadAttachment.setVisibility(View.VISIBLE);
                        mBinding.linearDeleteVideo.setVisibility(View.INVISIBLE);
                        //mBinding.linearDeleteAttachment.setVisibility(View.INVISIBLE);
                    } else {
                        mBinding.imageViewVideo.setVisibility(View.VISIBLE);
                        //mBinding.imageViewAttachment.setVisibility(View.VISIBLE);
                        mBinding.imageViewPlayVideoButton.setVisibility(View.VISIBLE);
                        mBinding.linearUploadVideo.setVisibility(View.GONE);
                        //mBinding.linearUploadAttachment.setVisibility(View.GONE);
                        mBinding.imageViewPlayVideoButton.setTag(media);
                        mBinding.linearDeleteVideo.setVisibility(View.VISIBLE);
                        // mBinding.linearDeleteAttachment.setVisibility(View.VISIBLE);
                        ActivityUtils.loadImage(getContext(), mBinding.imageViewVideo, media.getUrl(), R.drawable.logo);
                        //ActivityUtils.loadImage(getContext(),mBinding.imageViewAttachment,media.getUrl_attchment(),R.drawable.logo);

                    }
                } else {
                    if (media == null) {
                        mBinding.cardViewReportVideo.setVisibility(View.GONE);
                        // mBinding.cardViewReportAttachment.setVisibility(View.GONE);

                    } else {
                        // mBinding.cardViewReportVideo.setVisibility(View.VISIBLE);
                        //mBinding.cardViewReportAttachment.setVisibility(View.VISIBLE);
                        mBinding.imageViewPlayVideoButton.setVisibility(View.VISIBLE);
                        mBinding.imageViewVideo.setVisibility(View.VISIBLE);
                        //mBinding.imageViewAttachment.setVisibility(View.VISIBLE);
                        mBinding.imageViewPlayVideoButton.setTag(media);
                        mBinding.linearDeleteVideo.setVisibility(View.GONE);
                        //mBinding.linearDeleteAttachment.setVisibility(View.GONE);
                        mBinding.linearUploadVideo.setVisibility(View.VISIBLE);
                        //mBinding.linearUploadAttachment.setVisibility(View.VISIBLE);
                        ActivityUtils.loadImage(getContext(), mBinding.imageViewVideo, media.getUrl(), R.drawable.logo);
                        //ActivityUtils.loadImage(getContext(),mBinding.imageViewAttachment,media.getUrl_attchment(),R.drawable.logo);
                    }
                }
            }
        });

        mMainViewModel.getmLiveDataMarketReportAttachment().observe(this, new Observer<Media>() {
            @Override
            public void onChanged(@Nullable Media media) {
                if (mIsCreateUI) {
                    if (media == null) {
                        //mBinding.imageViewVideo.setVisibility(View.GONE);
                        mBinding.imageViewAttachment.setVisibility(View.GONE);
                        //mBinding.imageViewPlayVideoButton.setVisibility(View.GONE);
                        //mBinding.linearUploadVideo.setVisibility(View.VISIBLE);
                        mBinding.linearUploadAttachment.setVisibility(View.VISIBLE);
                        //mBinding.linearDeleteVideo.setVisibility(View.INVISIBLE);
                        mBinding.linearDeleteAttachment.setVisibility(View.INVISIBLE);
                    } else {
                        //mBinding.imageViewVideo.setVisibility(View.VISIBLE);
                        mBinding.imageViewAttachment.setVisibility(View.VISIBLE);
                        // mBinding.imageViewPlayVideoButton.setVisibility(View.VISIBLE);
                        //  mBinding.linearUploadVideo.setVisibility(View.GONE);
                        mBinding.linearUploadAttachment.setVisibility(View.GONE);
                        // mBinding.imageViewPlayVideoButton.setTag(media);
                        //mBinding.linearDeleteVideo.setVisibility(View.VISIBLE);
                        mBinding.linearDeleteAttachment.setVisibility(View.VISIBLE);
                        //  ActivityUtils.loadImage(getContext(),mBinding.imageViewVideo,media.getUrl(),R.drawable.logo);
                        ActivityUtils.loadImage(getContext(), mBinding.imageViewAttachment, media.getUrl_attchment(), R.drawable.logo);

                    }
                } else {
                    if (media == null) {
                        //mBinding.cardViewReportVideo.setVisibility(View.GONE);
                        mBinding.cardViewReportAttachment.setVisibility(View.GONE);

                    } else {
                        //mBinding.cardViewReportVideo.setVisibility(View.VISIBLE);
                        mBinding.cardViewReportAttachment.setVisibility(View.VISIBLE);
                        //mBinding.imageViewPlayVideoButton.setVisibility(View.VISIBLE);
                        //    mBinding.imageViewVideo.setVisibility(View.VISIBLE);
                        mBinding.imageViewAttachment.setVisibility(View.VISIBLE);
                        //  mBinding.imageViewPlayVideoButton.setTag(media);
                        // mBinding.linearDeleteVideo.setVisibility(View.GONE);
                        mBinding.linearDeleteAttachment.setVisibility(View.GONE);
                        //mBinding.linearUploadVideo.setVisibility(View.VISIBLE);
                        mBinding.linearUploadAttachment.setVisibility(View.VISIBLE);
                        //ActivityUtils.loadImage(getContext(),mBinding.imageViewVideo,media.getUrl(),R.drawable.logo);
                        ActivityUtils.loadImage(getContext(), mBinding.imageViewAttachment, media.getUrl_attchment(), R.drawable.logo);
                    }
                }
            }
        });

        mMainViewModel.getmMarketReportSelectedSaleyard().observe(this, new Observer<EntitySaleyard>() {
            @Override
            public void onChanged(@Nullable EntitySaleyard entitySaleyard) {
                if (entitySaleyard != null) {

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
                        mIsNeedRefresh = true;
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


    }

    public void setIsRefreashTag(Boolean tag) {
        Fragment f = getActivity().getSupportFragmentManager().findFragmentByTag(MarketReportFragment.class.getName());
        if (f instanceof MarketReportFragment) {
            ((MarketReportFragment) f).setmIsNeedRefresh(tag);
        }
    }

    public void enableChooseSaleyardButton(Boolean bEnable) {
        mBinding.tvSaleyardAuction.setEnabled(bEnable);
    }

    public enum PlayerStatus {
        RECORDING,
        LOADING,
        PLAYING,
        PAUSE,
        IDLE,
        ERROR
    }

    public interface RecordAudioInterface {
        void requestRecord(Boolean bStart);

        Boolean checkoutPermission();
        //void stopRecord();
    }
}
