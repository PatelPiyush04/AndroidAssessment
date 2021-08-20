package com.theherdonline.ui.herdLive;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.DexterBuilder;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentGoLiveLoadingBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.home.PermissionStatus;
import com.theherdonline.ui.home.ReadStoragePermissionDialogFragment;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;


public class GoLiveLoadingFragment extends HerdFragment {

    @Inject
    Lazy<GoLiveStreamingFragment> mGoLiveStreamingFragmentProvider;

    public static String AGR_DATA = "awsMediaLiveID";
    final Handler handler = new Handler();
    final int delay = 10000;
    protected BackPressInterface mGobackListener;
    Runnable runnable;
    NetworkInterface mNetworkListener;
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mMainActivityViewModel;
    FragmentGoLiveLoadingBinding mBinding;
    String mAwsMediaLiveId;

    @Inject
    public GoLiveLoadingFragment() {
        // Required empty public constructor
    }

    public static GoLiveLoadingFragment newInstance(Lazy<GoLiveLoadingFragment> mGoLiveLoadingFragmentProvider, String awsMediaLiveId) {

        Bundle args = new Bundle();

        GoLiveLoadingFragment fragment = new GoLiveLoadingFragment();
        args.putString(AGR_DATA, awsMediaLiveId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainActivityViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

        if (getArguments() != null) {
            mAwsMediaLiveId = getArguments().getString(AGR_DATA);
            Log.e("Data", "AWS MediaLive ID: " + mAwsMediaLiveId);
            startSalesYardStream(mAwsMediaLiveId);
        } else {
            Log.e("Data", "AWS MediaLive ID is null..");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_go_live_loading, container, false);


        mBinding.tvCancelLive.setOnClickListener(l -> {

            ActivityUtils.showWarningDialog(requireActivity(), "Cancel Live?", "\nAre you sure you want to cancel this live session?", "Yes", "No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //yes Click listner
                    stopSalesyardStream(mAwsMediaLiveId);

                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // no click lister
                    dialog.dismiss();
                }
            });

            //ActivityUtils.showToast(requireContext(), "Cancel Live..");
        });

        return mBinding.getRoot();
    }

    private void startSalesYardStream(String awsMediaLiveId) {

        mMainActivityViewModel.startSaleyardStream(awsMediaLiveId).observe(requireActivity(), new DataObserver<ResAWSMedialiveChannel>() {

            @Override
            public void onSuccess(ResAWSMedialiveChannel data) {
                mNetworkListener.onLoading(false);
                getMediaLiveChannel(mAwsMediaLiveId);
                handler.postDelayed(runnable = new Runnable() {
                    public void run() {
                        getMediaLiveChannel(mAwsMediaLiveId);
                    }
                }, delay);
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

    private void getMediaLiveChannel(String awsMedialiveId) {
        Log.e("Data", "Api Called after 10 secods");
        mMainActivityViewModel.getAwsMedialiveChannel(awsMedialiveId).observe(requireActivity(), new DataObserver<ResAWSMedialiveChannel>() {

            @Override
            public void onSuccess(ResAWSMedialiveChannel data) {
                mNetworkListener.onLoading(false);
                Log.e("Data", "State=>" + data.getState());

                if (data.getState().toLowerCase().equalsIgnoreCase(AppConstants.TAG_STATE_RUNNING)) {
                    handler.removeCallbacks(runnable);
                    getMedialiveInput(data.getInputAttachments().get(0).getInputId());
                } else {
                    handler.postDelayed(runnable, delay);
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


    private void requestStoragePermission(ResAWSMedialiveChannel data) {

        try {
            Dexter.withActivity(requireActivity())
                    .withPermissions(
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {

                                String mStreamingURL = data.getDestinations().get(0).getUrl();
                                Log.e("Data", "Streaming URL=>" + mStreamingURL);
                                //ActivityUtils.addFragmentToActivity(requireActivity().getSupportFragmentManager(), GoLiveStreamingFragment.newInstance(mGoLiveStreamingFragmentProvider, mStreamingURL), R.id.frameLayout_container);
                                Intent intent = new Intent(requireActivity(), GoLiveStreamActivity.class);
                                intent.putExtra("StreamURL", mStreamingURL);
                                intent.putExtra("mMediaLiveId", mAwsMediaLiveId);
                                requireActivity().startActivity(intent);
                                mGobackListener.OnClickGoBackButton();
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // show alert dialog navigating to Settings
                                mGobackListener.OnClickGoBackButton();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getMedialiveInput(String inputId) {
        mMainActivityViewModel.getMediaLiveInputId(inputId).observe(requireActivity(), new DataObserver<ResAWSMedialiveChannel>() {

            @Override
            public void onSuccess(ResAWSMedialiveChannel data) {
                //  mNetworkListener.onLoading(false);
                //getMediaLiveChannel(mAwsMediaLiveId);
                requestStoragePermission(data);
               /* String mStreamingURL = data.getDestinations().get(0).getUrl();
                Log.e("Data", "Streaming URL=>" + mStreamingURL);
                //ActivityUtils.addFragmentToActivity(requireActivity().getSupportFragmentManager(), GoLiveStreamingFragment.newInstance(mGoLiveStreamingFragmentProvider, mStreamingURL), R.id.frameLayout_container);
                Intent intent = new Intent(requireActivity(), GoLiveStreamActivity.class);
                intent.putExtra("StreamURL", mStreamingURL);
                intent.putExtra("mMediaLiveId", mAwsMediaLiveId);
                requireActivity().startActivity(intent);*/

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

    private void stopSalesyardStream(String awsMediaLiveId) {
        mMainActivityViewModel.stopSaleyardStream(awsMediaLiveId).observe(requireActivity(), new DataObserver<ResAWSMedialiveChannel>() {

            @Override
            public void onSuccess(ResAWSMedialiveChannel data) {
                mNetworkListener.onLoading(false);
                //getMediaLiveChannel(mAwsMediaLiveId);
                handler.removeCallbacks(runnable);
                mGobackListener.OnClickGoBackButton();
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

    public Lazy<ViewModelFactory> getmLazyFactory() {
        return mLazyFactory;
    }

    public void setmLazyFactory(Lazy<ViewModelFactory> mLazyFactory) {
        this.mLazyFactory = mLazyFactory;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        handler.removeCallbacks(runnable);
        mNetworkListener = null;
    }

    @Override
    public Boolean getmShowNavigationBar() {
        return false;
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
        if (context instanceof BackPressInterface) {
            mGobackListener = (BackPressInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement BackPressInterface");
        }

    }

}