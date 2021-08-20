package com.theherdonline.ui.notification.backup;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentStreamBinding;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.stream.CreatePostFragment;
import com.theherdonline.ui.stream.StreamFragmentViewModel;
import com.theherdonline.ui.view.CustomerOnCheckedChangeListener;
import com.theherdonline.ui.view.CustomerOnpageChangeListener;
import com.theherdonline.util.AppUtil;

import javax.inject.Inject;

import dagger.Lazy;


public class NotificationBackupFragment extends HerdFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    @Inject
    AppUtil mAppUtil;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;


    @Inject
    Lazy<CreatePostFragment> mProviderCreatePostFragment;


    Integer [] mRadioButtonsId = {R.id.radio_all,R.id.radio_following};

    // TODO: Rename and change types of parameters
    private Integer mUserId;
    private NetworkInterface mListener;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FragmentStreamBinding mBinding;
    MainActivityViewModel mMainViewModel;
    StreamFragmentViewModel mProfileViewModel;
    private View.OnClickListener mListenerLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mAppUtil.logoutCleanUp();
            mListener.onFailed(AppConstants.SERVER_ERROR_401, "logout");
        }
    };


    @Inject
    public NotificationBackupFragment() {
        // Required empty public constructor
    }

    @Override
    public CustomerToolbar getmCustomerToolbar() {
            return new CustomerToolbar(getString(R.string.txt_notifications), null,
                    null,null, null, null);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_stream, container, false);
        mUserId = mAppUtil.getUserId();
        mBinding.streamContainer.addOnPageChangeListener(new CustomerOnpageChangeListener(mBinding.tabs, mBinding.linearRadioGroup,mRadioButtonsId));
        mBinding.cardViewTabs.setVisibility(View.GONE);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mUserId);
        mBinding.streamContainer.setAdapter(mSectionsPagerAdapter);
        mBinding.linearRadioGroup.setOnCheckedChangeListener(new CustomerOnCheckedChangeListener(mBinding.streamContainer,
                mRadioButtonsId ));
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

        StreamFragmentViewModel.Factory factoryProfile = new StreamFragmentViewModel.Factory(
                getActivity().getApplication(),
                mUserId);
        mProfileViewModel = ViewModelProviders.of(this, factoryProfile)
                .get(StreamFragmentViewModel.class);
    }





    @Override
    public void onResume() {
        super.onResume();
        if (mUserId == null || mUserId == 0) {
            // might enter this screen from main screen without login
            mUserId = mAppUtil.getUserId();
        }
        mBinding.progressBar.setVisibility(View.INVISIBLE);
        mBinding.frameLayoutContainer.setVisibility(View.VISIBLE);
        mProfileViewModel.refreshUserProfile(mUserId);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm, Integer userId) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
                return NotificationListBackupFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
