package com.theherdonline.ui.marketReport;

import android.app.Activity;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;

import androidx.databinding.DataBindingUtil;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentMarketReportBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.view.CustomerOnCheckedChangeListener;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UserType;

import dagger.Lazy;


public class MarketReportFragment extends HerdFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    Lazy<MarketReportDetailFragment> mMarketReportDetailFragmentLazy;

    @Inject
    Lazy<CreateMarketReportFragment> mCreateMarketReportFragmentLazy;

    Integer[] mRadioButtonsId = {R.id.radio_option_1, R.id.radio_option_3};


    // TODO: Rename and change types of parameters
    private Integer mUserId;
    private NetworkInterface mListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FragmentMarketReportBinding mBinding;
    private Boolean mIsNeedRefresh = false;
    MainActivityViewModel mMainViewModel;
    MarketReportFragmentViewModel mMarketViewModel;


    private View.OnClickListener mListenerLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAppUtil.logoutCleanUp();
            mListener.onFailed(AppConstants.SERVER_ERROR_401, "logout");
        }
    };

    @Inject
    public MarketReportFragment() {
        // Required empty public constructor
    }

    public void setmIsNeedRefresh(Boolean mIsNeedRefresh) {
        this.mIsNeedRefresh = mIsNeedRefresh;
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        if (mAppUtil.getUserId() == null || mAppUtil.getUserType() != UserType.AGENT) {
            return new CustomerToolbar(true, getString(R.string.txt_market_report), null, null, null, null);
        } else {
            return new CustomerToolbar(true, getString(R.string.txt_market_report),
                    R.drawable.ic_add_circle, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        //setRefreshDataMode(true);
                        ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(), CreateMarketReportFragment.newInstance(mCreateMarketReportFragmentLazy, null), R.id.frameLayout_container);
                    }
                }
            },
                    null, null);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_report, container, false);
        mBinding.linearRadioGroup.removeView(mBinding.radioOption2);
        mBinding.linearRadioGroup.removeView(mBinding.divide1);
        mBinding.linearRadioGroup.removeView(mBinding.divide2);
        mBinding.radioOption1.setText(getString(R.string.txt_all_reports));
        mBinding.radioOption3.setText(getString(R.string.txt_my_reports));


        mUserId = mAppUtil.getUserId();

        mBinding.cardViewTabs.setVisibility(mUserId == null || mAppUtil.getUserType() != UserType.AGENT ? View.GONE : View.VISIBLE);


//        mBinding.streamContainer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabs));
        // mBinding.streamContainer.addOnPageChangeListener(new CustomerOnpageChangeListener(mBinding.tabs, mBinding.linearRadioGroup,mRadioButtonsId));

/*        mBinding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.streamContainer));
        mBinding.tabs.getTabAt(0).setText(getString(R.string.txt_all_reports));
        mBinding.tabs.getTabAt(1).setText(getString(R.string.txt_my_reports));*/

        if (mSectionsPagerAdapter == null) {
            mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mUserId);
            mSectionsPagerAdapter.setRefreshMode(true);
        } else {
            mSectionsPagerAdapter.setRefreshMode(mIsNeedRefresh);

        }
        mBinding.streamContainer.setAdapter(mSectionsPagerAdapter);

      /* if (mIsNeedRefresh == true
            mSectionsPagerAdapter == null) {
         //   mIsNeedRefresh = false;
       // }
       // else
        mBinding.streamContainer.setAdapter(mSectionsPagerAdapter);*/
/*        if (mUserId == null || mAppUtil.getUserType() != UserType.AGENT)
        {
            mBinding.tabs.setVisibility(View.GONE);
        }*/
/*
        UIUtils.showToolbar(getContext(), mBinding.includeToolbar, getString(R.string.txt_market_report), null,
                R.drawable.ic_add_circle, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = getActivity();
                        if (activity != null)
                        {
                            setRefreshDataMode(true);
                            ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(),mCreateMarketReportFragmentLazy.get(),R.id.frameLayout_container);
                        }
                    }
                },
        null,null);
        mBinding.includeToolbar.imageRight1.setVisibility(mUserId == null || mAppUtil.getUserType() != UserType.AGENT ? View.GONE : View.VISIBLE);
       */
        mBinding.linearRadioGroup.setOnCheckedChangeListener(new CustomerOnCheckedChangeListener(mBinding.streamContainer,
                mRadioButtonsId));

        return mBinding.getRoot();
    }


    public void setRefreshDataMode(Boolean refreshDataMode) {
        List<Fragment> fragmentList = mSectionsPagerAdapter.getTabFragmentList();
        if (fragmentList != null && fragmentList.size() != 0) {
            for (Fragment f : fragmentList) {
                ((MarketReportListFragment) f).setmRfreshOnResume(refreshDataMode);
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NetworkInterface) {
            mListener = (NetworkInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement NetworkInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mSectionsPagerAdapter = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

        MarketReportFragmentViewModel.Factory factoryProfile = new MarketReportFragmentViewModel.Factory(
                getActivity().getApplication(),
                mUserId);
        mMarketViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MarketReportFragmentViewModel.class);
        subscription();
    }


    public void subscription() {
        //     mMarketViewModel.getmLiveDataUser().observe(this, mUserObserver);
    }

    @Override
    public void onStop() {
        super.onStop();
        setmIsNeedRefresh(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mUserId == null || mUserId == 0) {
            // might enter this screen from main screen without login
            mUserId = mAppUtil.getUserId();
        }
        // mBinding.progressBar.setVisibility(View.INVISIBLE);
        // mBinding.frameLayoutContainer.setVisibility(View.VISIBLE);
        mMarketViewModel.refreshUserProfile(mUserId);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onUserProfileFragmentInteraction(Uri uri);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public Integer mUserId;

        private List<Fragment> mFragmentList = new ArrayList<>();

        MarketReportListFragment mAllReportFragment = null; // MarketReportListFragment.newInstance(null);
        MarketReportListFragment mMyReportFragment = null; // MarketReportListFragment.newInstance(mUserId);


        public List<Fragment> getTabFragmentList() {
            return mFragmentList;
        }

        public SectionsPagerAdapter(FragmentManager fm, Integer userId) {
            super(fm);
            mUserId = userId;
            mAllReportFragment = MarketReportListFragment.newInstance(null);
            mAllReportFragment.setEnableSwipe(false);
            mMyReportFragment = MarketReportListFragment.newInstance(mUserId);
            mMyReportFragment.setEnableSwipe(true);
        }


        public void setRefreshMode(Boolean b) {
            mAllReportFragment.setmRfreshOnResume(b);
            mMyReportFragment.setmRfreshOnResume(b);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                // MarketReportListFragment f = MarketReportListFragment.newInstance(null);
                // f.setmRfreshOnResume(true);
                // mFragmentList.add(position, f);
                return mAllReportFragment;
            } else {
                // MarketReportListFragment f = MarketReportListFragment.newInstance(mUserId);
                // f.setmRfreshOnResume(true);
                // mFragmentList.add(position, f);
                return mMyReportFragment;
            }
        }

        @Override
        public int getCount() {
            // for anonymous user, just show the first tab, all report.
            return (mUserId == null) || (mAppUtil.getUserType() != UserType.AGENT) ? 1 : 2;
        }
    }


}
