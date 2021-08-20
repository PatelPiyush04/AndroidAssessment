package com.theherdonline.ui.herdLive;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentHerdLiveDetailBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.TimeUtils;

import javax.inject.Inject;

import dagger.Lazy;


public class HerdLiveDetailFragment extends HerdFragment {

    public static String AGR_DATA = "list Data";
    @Inject
    Lazy<GoLiveLoadingFragment> mGoLiveLoadingFragmentProvider;

    @Inject
    Lazy<GoLiveStreamingFragment> mGoLiveStreamingFragmentProvider;

    NetworkInterface mNetworkListener;
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;

    FragmentHerdLiveDetailBinding mBinding;


    private ResSaleyardStreamDetails mResSaleyardStream;

    @Inject
    public HerdLiveDetailFragment() {
        // Required empty public constructor
    }

    public static HerdLiveDetailFragment newInstance(Lazy<HerdLiveDetailFragment> mHerdLiveDetailFragmentProvider, ResSaleyardStreamDetails data) {
        Bundle args = new Bundle();
        HerdLiveDetailFragment fragment = mHerdLiveDetailFragmentProvider.get();
        args.putParcelable(AGR_DATA, data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

        if (getArguments() != null) {
            mResSaleyardStream = getArguments().getParcelable(AGR_DATA);

        }
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        CustomerToolbar customerToolbar = null;

        if (mResSaleyardStream != null && mResSaleyardStream.getMedialiveChannel() != null) {
            if (!mAppUtil.isCreateLiveStream() || mAppUtil.getUserId() != mResSaleyardStream.getUserId() || mResSaleyardStream.getMedialiveChannel().getAwsMedialiveId() == null) {
                //Hide Go live Button...
                customerToolbar = new CustomerToolbar(true, null, null, null, null, null);
            } else {
                // show go Live Button...
                customerToolbar = new CustomerToolbar(true, null, getString(R.string.txt_goLive), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ActivityUtils.showWarningDialog(requireActivity(), "Start Streaming Session?", "It may take a few minutes to setup.", "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String awsMediaId = String.valueOf(mResSaleyardStream.getMedialiveChannel().getAwsMedialiveId());
                                ActivityUtils.addFragmentToActivity(requireActivity().getSupportFragmentManager(), GoLiveLoadingFragment.newInstance(mGoLiveLoadingFragmentProvider, awsMediaId), R.id.frameLayout_container);
                                //ActivityUtils.addFragmentToActivity(requireActivity().getSupportFragmentManager(), GoLiveStreamingFragment.newInstance(mGoLiveStreamingFragmentProvider, "Test URL"), R.id.frameLayout_container);
                                //startActivity(new Intent(requireActivity(), GoLiveStreamActivity.class));
                            }
                        }, "No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    }
                },
                        null, null,
                        null, null,
                        null, null);
            }
        } else {
            customerToolbar = new CustomerToolbar(true, null, null, null, null, null);
        }

        return customerToolbar;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_herd_live_detail, container, false);

        if (mResSaleyardStream != null) {
            mBinding.tvTitle.setText(mResSaleyardStream.getName());
            mBinding.tvDate.setText(TimeUtils.BackendUTC2LocalUTC(TimeUtils.UTC2Local(mResSaleyardStream.getStreamDate())));

            mBinding.llRecordingViews.setVisibility(mResSaleyardStream.getListVods().size() == 0 ? View.GONE : View.VISIBLE);
            mBinding.rvRecordingList.setLayoutManager(new LinearLayoutManager(getContext()));
            mBinding.rvRecordingList.addItemDecoration(ActivityUtils.provideDividerItemDecoration(requireActivity(), R.drawable.list_divider));

            if (mResSaleyardStream.getListVods().size() != 0)
                mBinding.rvRecordingList.setAdapter(new RecordingsListAdapter(requireActivity(), mResSaleyardStream.getListVods()));


            if (mResSaleyardStream.getMedialiveChannel() != null && mResSaleyardStream.getMedialiveChannel().getAwsMedialiveId() != null) {
                // ActivityUtils.showToast(requireActivity(), "MedialiveChannel Is not null");
                getMediaLiveChannel(String.valueOf(mResSaleyardStream.getMedialiveChannel().getAwsMedialiveId()));
            } else {
                mBinding.llLiveStream.setVisibility(View.GONE);
                // ActivityUtils.showToast(requireActivity(), "MedialiveChannel Is null");
            }
        } else {
            Log.e("Data", "No data ....");
        }

        mBinding.imgPlay.setOnClickListener(l -> {
            ActivityUtils.playVideo(requireActivity(), mResSaleyardStream.getMedialiveChannel().getAwsLivestreamViewingUrl());
        });

        return mBinding.getRoot();
    }

    private void getMediaLiveChannel(String awsMedialiveId) {

        mMainActivityViewModel.getAwsMedialiveChannel(awsMedialiveId).observe(requireActivity(), new DataObserver<ResAWSMedialiveChannel>() {

            @Override
            public void onSuccess(ResAWSMedialiveChannel data) {
                mNetworkListener.onLoading(false);
                mBinding.llLiveStream.setVisibility(View.VISIBLE);
                if (data.getState().toLowerCase().equalsIgnoreCase(AppConstants.TAG_STATE_RUNNING)) {
                    // show the play button and hide the ‘Live Stream is offline’ label
                    mBinding.tvLiveStreamOffline.setVisibility(View.GONE);
                    mBinding.rlLiveStreamPlay.setVisibility(View.VISIBLE);
                    //Glide.with(requireActivity()).load(mResSaleyardStream.getMedialiveChannel().getAwsLivestreamViewingUrl()).into(mBinding.imgLiveStream);
                } else {
                    // hide the play button and show the ‘Live Stream is offline’ label
                    mBinding.tvLiveStreamOffline.setVisibility(View.VISIBLE);
                    mBinding.rlLiveStreamPlay.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(Integer code, String msg) {
                ActivityUtils.showWarningDialog(requireActivity(), null, "\n" + msg);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public Lazy<ViewModelFactory> getmLazyFactory() {
        return mLazyFactory;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mNetworkListener = null;
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


}