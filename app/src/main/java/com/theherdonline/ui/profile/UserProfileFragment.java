package com.theherdonline.ui.profile;

import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentUserProfileBinding;
import com.theherdonline.db.entity.StreamType;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.general.InterfaceContactCard;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.herdinterface.NetworkInterface;
import com.theherdonline.ui.main.payment.PaymentFragment;
import com.theherdonline.ui.organisation.OrganisationFragment;
import com.theherdonline.ui.profile.mybid.MyBidFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UserType;
import dagger.Lazy;

import static com.theherdonline.app.AppConstants.SERVER_ERROR_401;


public class UserProfileFragment extends HerdFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_USER_ID = "param_user_id";

    private static final String ARG_PARAM_IS_APP_USER = "param_is_app_user";



    MainActivityViewModel mMainViewModel;
    UserProfileFragmentViewModel mProfileViewModel;
    @Inject
    Lazy<ViewModelFactory> mLazyFactory;

    @Inject
    Lazy<BidListFragment> mBidListFragmentProvider;

    @Inject
    Lazy<PaymentFragment> mPaymentFragmentProvider;


    @Inject
    Lazy<EditProfileFragment> mEditProfileFragmentProvider;



    @Inject
    Lazy<ProfileOptionsFragment> mProfileOptionsFragmentProvider;



    @Inject
    AppUtil mAppUtil;

    @Inject
    Lazy<MyBidFragment> mMyBidFragmentProvider;


    @Inject
    Lazy<AccountFragment> mAccountFragmentProvider;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    User mUser;

    // TODO: Rename and change types of parameters
    private Integer mCurrentUserId;
    private NetworkInterface mListener;
    private InterfaceContactCard mContactCardListener;

    private Boolean mNeedRefresh = true;

    private FragmentUserProfileBinding mBinding;
    private Boolean mIsAppUser = true;
    private View.OnClickListener mListenerLogout = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mMainViewModel.clearData();     // clear cached data in memory
            mAppUtil.logoutCleanUp();       // clear cached data in disk
            mListener.onFailed(AppConstants.SERVER_ERROR_401, "logout");
        }
    };


    private View.OnClickListener mOnClickOption =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                            mAccountFragmentProvider.get()
                            ,R.id.frameLayout_container);
                }
    };


    @Override
    public CustomerToolbar getmCustomerToolbar() {
            return new CustomerToolbar((mAppUtil.getUserId() != null && mAppUtil.getUserId() == mCurrentUserId) ? getString(R.string.title_tab_my_herd) : "",
                mIsAppUser ? null : mExitListener,
                mIsAppUser ?  R.drawable.ic_toolbar_option: null,
                mIsAppUser ?  mOnClickOption : null,
                null,
                null);
    }

    @Inject
    Lazy<OrganisationFragment> mOrganisationFragmentProvider;


    private void showProfile(User user)
    {
        mBinding.tvName.setText(ActivityUtils.trimText(getContext(), user.getFullName()));
        if (user.getOrganisation() !=  null)
        {
            mBinding.tvOrganization.setVisibility(View.VISIBLE);
            mBinding.tvOrganization.setText(ActivityUtils.trimText(getContext(), user.getOrganisation().getName()));
            mBinding.tvOrganization.setOnClickListener(l->{
                ((MainActivity)getActivity()).stackFragment(OrganisationFragment.newInstance(mOrganisationFragmentProvider.get(),user.getOrganisationId()));
            });

        }
        else
        {
            mBinding.tvOrganization.setVisibility(View.GONE);
        }

        mBinding.tvFollower.setText(getString(R.string.txt_num_followers,user.getFollowerCount()));
        ActivityUtils.loadCircleImage(getContext(), mBinding.avatar, ActivityUtils.getImageAbsoluteUrl(user.getAvatar_location_full_url()), R.drawable.avatar_place_holder);
        mBinding.tvFollowing.setText(user.getIs_authed_user_following() ? R.string.txt_following : R.string.txt_unfollowing );
        mBinding.imageViewFollow.setImageDrawable(ContextCompat.getDrawable(getContext(), user.getIs_authed_user_following() ? R.drawable.ic_button_follow : R.drawable.ic_button_unfollow));
        mBinding.imageViewEmail.setOnClickListener(l->{
            mContactCardListener.OnClickSendMessage(user);
        });
        mBinding.imageViewPhoneNumber.setOnClickListener(l->{
            mContactCardListener.OnClickPhoneCall(user);
        });
        mBinding.imageViewFollow.setOnClickListener(l->{
            if (mAppUtil.getAccessToken() == null)
            {
                mNetworkListener.onFailed(SERVER_ERROR_401,"");
                return;
            }
            Boolean b = mBinding.tvFollowing.getText().equals(getString(R.string.txt_following));
            mProfileViewModel.setFollow(b);
            mBinding.tvFollowing.setText(!b ? R.string.txt_following : R.string.txt_unfollowing);
            mBinding.imageViewFollow.setImageDrawable(ContextCompat.getDrawable(getContext(), !b ? R.drawable.ic_button_follow : R.drawable.ic_button_unfollow));

        });
    }

    private DataObserver<User> mUserObserver = new DataObserver<User>() {
        @Override
        public void onSuccess(User data) {
            mNetworkListener.onLoading(false);
            showProfile(data);
           // if (!mIsAppUser)
           // {
                //if (mSectionsPagerAdapter == null)
                //{
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager(), mCurrentUserId,mIsAppUser);
                //}
                mBinding.streamContainer.setAdapter(mSectionsPagerAdapter);
            //}
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
    };

    @Inject
    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(Lazy<UserProfileFragment> userProfileFragmentLazy, Integer userId, Boolean isAppUser) {
        Bundle args = new Bundle();
        UserProfileFragment fragment = userProfileFragmentLazy.get();
        if (userId != null) {
            args.putInt(ARG_PARAM_USER_ID, userId);
            args.putBoolean(ARG_PARAM_IS_APP_USER, isAppUser);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProfileViewModel.setRefreshDataFlag(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false);
        mCurrentUserId = getArguments().getInt(ARG_PARAM_USER_ID);
        Integer appUserId = mAppUtil.getUserId();
        //mIsAppUser = (mCurrentUserId == null) || (mCurrentUserId == 0) || (mCurrentUserId != null && mCurrentUserId.equals(appUserId));
        mIsAppUser = getArguments().getBoolean(ARG_PARAM_IS_APP_USER);
        if  (mIsAppUser) {
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
            mBinding.frameLayoutSavedAds.setOnClickListener(l -> {
                ActivityUtils.addFragmentToActivity(getActivity().getSupportFragmentManager(),
                        MyBidFragment.newInstance(mMyBidFragmentProvider.get(), MyBidFragment.ARG_MODE_save_ads), R.id.frameLayout_container);
            });
            mBinding.frameLayoutSavedAuction.setVisibility(mAppUtil.getUserType() == UserType.AGENT ? View.VISIBLE : View.GONE);
        }
        mBinding.linearLayoutContactCard.setVisibility(mIsAppUser ? View.GONE : View.VISIBLE);
        //mBinding.tvFollower.setVisibility(mIsAppUser ? View.VISIBLE : View.GONE);
        mBinding.tvFollower.setVisibility(View.GONE);

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

        if (context instanceof InterfaceContactCard) {
            mContactCardListener = (InterfaceContactCard) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement InterfaceContactCard");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mContactCardListener = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);
        mProfileViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(UserProfileFragmentViewModel.class);
        subscription();
        mProfileViewModel.refreshUserProfile(mCurrentUserId);
    }

    public void subscription() {
        mProfileViewModel.getmLiveDataUser().observe(this, mUserObserver);
        mProfileViewModel.getmLiveDataFollowResponse().observe(this, new DataObserver<Void>() {
            @Override
            public void onSuccess(Void data) {
               // mProfileViewModel.refreshUserProfile(mCurrentUserId);
              //  Toast.makeText(getContext(),getString(R.string.app_name),Toast.LENGTH_LONG).show();
               // mProfileViewModel.refreshUserProfile(mCurrentUserId);
            }
            @Override
            public void onError(Integer code, String msg) {
                mListener.onFailed(code,msg);
                // reset the following button text if the request is failed.
                if (mBinding.tvFollowing.getText().equals(getString(R.string.txt_following)))
                {
                    mBinding.tvFollowing.setText(R.string.txt_unfollowing);
                    mBinding.imageViewFollow.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.ic_button_unfollow));

                }
                else
                {
                    mBinding.tvFollowing.setText(R.string.txt_following);
                    mBinding.imageViewFollow.setImageDrawable(ContextCompat.getDrawable(getContext(),  R.drawable.ic_button_follow ));
                }
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
    public void onResume() {
        super.onResume();

    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public Integer mUserId;
        public Boolean mIsAppUser;

        public SectionsPagerAdapter(FragmentManager fm, Integer userId, Boolean isAppUser) {
            super(fm);
            mUserId = userId;
            mIsAppUser = isAppUser;
        }



        @Override
        public Fragment getItem(int position) {
                if (!mIsAppUser) {
                    return AdsTabFragment.newInstance(mUserId, null, StreamType.ALL);
                }
                else
                {
                    return mProfileOptionsFragmentProvider.get();
                }
                    }

        @Override
        public int getCount() {
            return 1;
        }
    }


}
