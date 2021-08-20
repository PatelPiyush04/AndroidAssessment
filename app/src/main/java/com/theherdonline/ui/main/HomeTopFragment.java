package com.theherdonline.ui.main;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentSearchTopCategoryBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdLive.ResHerdLive;
import com.theherdonline.util.ActivityUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

public class HomeTopFragment extends HerdFragment {

    @Inject
    Lazy<ViewModelFactory> mLazyFactory;
    MainActivityViewModel mViewModel;
    FragmentSearchTopCategoryBinding mBinding;
    private OnFragmentInteractionListener mListener;

    @Inject
    public HomeTopFragment() {
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(false, getString(R.string.title_home), null, null, null, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_top_category, container, false);

        mBinding.viewSaleyard.setOnClickListener(l -> {
            mListener.OnClickSaleyardAuctions();
        });

        mBinding.viewPaddock.setOnClickListener(l -> {
            mListener.OnClickPaddockSales();
        });

        mBinding.viewStream.setOnClickListener(l -> {
            mListener.OnClickMarketReports();
        });

        mBinding.viewPrimeSaleyard.setOnClickListener(l -> mListener.OnClickPrimeSaleyards());

        mBinding.viewSaleyardPlus.setOnClickListener(l -> {

           // ActivityUtils.showWarningDialog(requireContext(), "Wanted Livestock", "\n Wanted Livestok is Coming Soon...");
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(StringUtils.defaultString(AppConstants.WantedLiveStockURL))));

        });
        mBinding.viewHerdLive.setOnClickListener(l -> {
            mListener.OnclickHerdLive();

        });

        mBinding.rlLiveNow.setOnClickListener(l -> {
            mListener.OnclickHerdLive();
        });
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

        subscription();
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void subscription() {
        //     mMarketViewModel.getmLiveDataUser().observe(this, mUserObserver);

        mViewModel.getHerdLiveNow1(1).observe(requireActivity(), new DataObserver<List<ResHerdLive>>() {

            @Override
            public void onSuccess(List<ResHerdLive> data) {
                mNetworkListener.onLoading(false);
                mBinding.rlLiveNow.setVisibility(data.size() == 0 ? View.GONE : View.VISIBLE);
            }

            @Override
            public void onError(Integer code, String msg) {
            }

            @Override
            public void onLoading() {
            }

            @Override
            public void onDirty() {
            }
        });

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void OnClickSaleyardAuctions();

        void OnClickPaddockSales();

        void OnClickPrimeSaleyards();

        void OnClickAuctionPlus();

        void OnClickMarketReports();

        void OnclickHerdLive();
    }

}
