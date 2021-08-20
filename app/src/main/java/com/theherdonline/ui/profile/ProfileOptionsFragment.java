package com.theherdonline.ui.profile;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.databinding.FragmentProfileOptionBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.herdinterface.LivestockInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.marketReport.CreateMarketReportFragment;
import com.theherdonline.ui.postad.PostLivestockFragment;
import com.theherdonline.ui.postad.PostPrimSaleyardFragment;
import com.theherdonline.ui.profile.mybid.MyBidFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UserType;

import javax.inject.Inject;

import dagger.Lazy;
import dagger.android.support.DaggerFragment;

public class ProfileOptionsFragment extends DaggerFragment {


    LivestockInterface mLivestockListener;

    @Inject
    Lazy<BidListFragment> mBidListFragmentProvider;

    @Inject
    AppUtil mAppUtil;

    @Inject
    Lazy<MyBidFragment> mMyBidFragmentProvider;

    @Inject
    Lazy<PostLivestockFragment> mPostLivestockFragmentLazy;
    @Inject
    Lazy<CreateMarketReportFragment> mCreateMarketReportFragmentProvider;

    MainActivityViewModel mMainViewModel;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    Lazy<PostPrimSaleyardFragment> mPostPrimSaleyardFragmentProvider;
    FragmentProfileOptionBinding mBinding;

    @Inject
    public ProfileOptionsFragment() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile_option, container, false);

        Boolean isAgent = mAppUtil.getUserType() == UserType.AGENT;

        mBinding.frameLayoutMyPosts.setVisibility(isAgent ? View.VISIBLE : View.GONE);
        mBinding.frameLayoutLoadPaddockSale.setVisibility(isAgent ? View.VISIBLE : View.GONE);
       // mBinding.frameLayoutLoadPrimeSaleyard.setVisibility(isAgent ? View.VISIBLE : View.GONE);
        mBinding.frameLayoutLoadStoreSaleyard.setVisibility(isAgent ? View.VISIBLE : View.GONE);
        mBinding.frameLayoutLoadMarketReport.setVisibility(isAgent ? View.VISIBLE : View.GONE);
        mBinding.frameLayoutSavedAuction.setVisibility(View.VISIBLE);


        mBinding.frameLayoutMyBids.setOnClickListener(l -> {
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    MyBidFragment.newInstance(mMyBidFragmentProvider.get(), MyBidFragment.ARG_MODE_bid), R.id.frameLayout_container);
        });
        mBinding.frameLayoutMyPosts.setOnClickListener(l -> {
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    MyBidFragment.newInstance(mMyBidFragmentProvider.get(), MyBidFragment.ARG_MODE_post), R.id.frameLayout_container);
        });

        mBinding.frameLayoutSavedAuction.setOnClickListener(l -> {
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    MyBidFragment.newInstance(mMyBidFragmentProvider.get(), MyBidFragment.ARG_MODE_save_saleyard), R.id.frameLayout_container);

        });

       /* mBinding.frameLayoutSavedPrimeSaleyard.setOnClickListener(l -> {
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    MyBidFragment.newInstance(mMyBidFragmentProvider.get(), MyBidFragment.ARG_MODE_save_prime_saleyard), R.id.frameLayout_container);

        });*/
        mBinding.frameLayoutSavedAds.setOnClickListener(l -> {
            ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                    MyBidFragment.newInstance(mMyBidFragmentProvider.get(), MyBidFragment.ARG_MODE_save_ads), R.id.frameLayout_container);
        });

        mBinding.frameLayoutLoadPaddockSale.setOnClickListener(l -> {
            mLivestockListener.onUpdateLivestock(null);
        });

       /* mBinding.frameLayoutLoadPrimeSaleyard.setOnClickListener(l ->
                ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                        PostPrimSaleyardFragment.newInstance(mPostPrimSaleyardFragmentProvider, null),
                        R.id.frameLayout_container));*/


        mBinding.frameLayoutLoadStoreSaleyard.setOnClickListener(l -> {
            ActivityUtils.showWarningDialog(getContext(), R.string.txt_request_saleyard, R.string.txt_request_saleyard_msg,
                    R.string.txt_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mMainViewModel.postSaleyardRequest();
                        }
                    }, R.string.txt_no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    });
        });

        mBinding.frameLayoutLoadMarketReport.setOnClickListener(l -> {
            Activity activity = getActivity();
            if (activity != null) {
                //setRefreshDataMode(true);
                ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(), CreateMarketReportFragment.newInstance(mCreateMarketReportFragmentProvider, null), R.id.frameLayout_container);
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LivestockInterface) {
            mLivestockListener = (LivestockInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LivestockInterface");
        }


    }


    @Override
    public void onDetach() {
        super.onDetach();
        mLivestockListener = null;
    }

}
