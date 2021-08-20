package com.theherdonline.ui.main.myad;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.tabs.TabLayout;
import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentMarketReportBinding;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.postad.PostLivestockFragment;
import com.theherdonline.ui.postad.PostPrimSaleyardFragment;
import com.theherdonline.ui.view.CustomerOnCheckedChangeListener;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;

import javax.inject.Inject;

import dagger.Lazy;


public class PrimeSaleyardsFragment extends HerdFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;


    @Inject
    Lazy<CreateLivestockFragment> mCreateLivestockFragment;


    @Inject
    Lazy<PostPrimSaleyardFragment> mPostLivestockFragmentProvider;


    Integer[] mRadioButtonsId = {R.id.radio_option_1, R.id.radio_option_3};
    MainActivityViewModel mMainViewModel;
    MyAdFragmentViewModel mMyAdFragmentViewModel;
    // TODO: Rename and change types of parameters
    private Integer mUserId;
    private NetworkInterface mListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FragmentMarketReportBinding mBinding;


    @Inject
    public PrimeSaleyardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public CustomerToolbar getmCustomerToolbar() {


        CustomerToolbar customerToolbar = new CustomerToolbar(true,
                getString(R.string.txt_prime_saleyards), R.drawable.ic_add_circle, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if (activity != null) {
                    mMainViewModel.updatePostLivestocks(new EntityLivestock());
                    Fragment newFragment = PostPrimSaleyardFragment.newInstance(mPostLivestockFragmentProvider, null);
                    ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(), newFragment, R.id.frameLayout_container, PostLivestockFragment.class.getName());

                }
            }
        },
                null, null);

        return customerToolbar;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_report, container, false);
        mUserId = mAppUtil.getUserId();


        mBinding.tabs.removeAllTabs();


        TabLayout.Tab tab1 = mBinding.tabs.newTab();
        tab1.setText(R.string.txt_current);
        TabLayout.Tab tab2 = mBinding.tabs.newTab();
        tab2.setText(R.string.txt_past);
        mBinding.tabs.addTab(tab1,0);
        mBinding.tabs.addTab(tab2,1);

        mBinding.linearRadioGroup.removeView(mBinding.radioOption2);
        mBinding.linearRadioGroup.removeView(mBinding.divide2);

        //mBinding.streamContainer.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tabs));
        //  mBinding.streamContainer.addOnPageChangeListener(new CustomerOnpageChangeListener(mBinding.tabs, mBinding.linearRadioGroup,mRadioButtonsId));


/*        mBinding.tabs.removeAllTabs();
        TabLayout.Tab tab1 = mBinding.tabs.newTab();
        tab1.setText("Requested");
        TabLayout.Tab tab2 = mBinding.tabs.newTab();
        tab2.setText("Complete");
        TabLayout.Tab tab3 = mBinding.tabs.newTab();
        tab3.setText("Post");

        mBinding.tabs.addTab(tab1,0);
        mBinding.tabs.addTab(tab2,1);
        mBinding.tabs.addTab(tab3,2);

        mBinding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.streamContainer));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mUserId);
        mBinding.streamContainer.setAdapter(mSectionsPagerAdapter);


        UIUtils.showToolbar(getContext(), mBinding.includeToolbar, getString(R.string.txt_my_ads), null,
                R.drawable.ic_add_circle, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Activity activity = getActivity();
                        if (activity != null)
                        {
                            Fragment newFragment = PostLivestockFragment.newInstance(mPostLivestockFragmentProvider,null);
                            ActivityUtils.addFragmentToActivity(((FragmentActivity) activity).getSupportFragmentManager(), newFragment, R.id.frameLayout_container, PostLivestockFragment.class.getName());

                        }
                    }
                },
                null, null);*/


        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mUserId);
        mBinding.streamContainer.setAdapter(mSectionsPagerAdapter);

        mBinding.linearRadioGroup.setOnCheckedChangeListener(new CustomerOnCheckedChangeListener(mBinding.streamContainer,
                mRadioButtonsId));
        return mBinding.getRoot();
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        MyAdFragmentViewModel.Factory factoryProfile = new MyAdFragmentViewModel.Factory(
                getActivity().getApplication(),
                mUserId);
        mMyAdFragmentViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(MyAdFragmentViewModel.class);
        subscription();
    }


    public void subscription() {
        //     mMyAdFragmentViewModel.getmLiveDataUser().observe(this, mUserObserver);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mUserId == null || mUserId == 0) {
            // might enter this screen from main screen without login
            mUserId = mAppUtil.getUserId();
        }
        //  mBinding.progressBar.setVisibility(View.INVISIBLE);
        //  mBinding.frameLayoutContainer.setVisibility(View.VISIBLE);
        mMyAdFragmentViewModel.refreshUserProfile(mUserId);
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

        public SectionsPagerAdapter(FragmentManager fm, Integer userId) {
            super(fm);
            mUserId = userId;
        }

        @Override
        public Fragment getItem(int position) {
            SearchFilterPublicSaleyard filterPublicSaleyard = new SearchFilterPublicSaleyard();
            filterPublicSaleyard.type = AppConstants.TAG_SALEYARD_TYPE_PRIME;
            if (position == 0) {
                filterPublicSaleyard.saleyard_status_id = 2;
            } else if (position == 1) {
                filterPublicSaleyard.saleyard_status_id = 3;
            }

            PrimeSaleyardPagerListFragment f = PrimeSaleyardPagerListFragment.newInstance(filterPublicSaleyard);
            f.setEnableSwipe(true);
            return f;

        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
