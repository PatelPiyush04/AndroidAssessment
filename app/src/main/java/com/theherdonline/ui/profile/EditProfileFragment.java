package com.theherdonline.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;

import javax.inject.Inject;

import com.theherdonline.R;
import com.theherdonline.app.AppConstants;
import com.theherdonline.databinding.FragmentEditAccountBinding;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.ViewModelFactory;
import com.theherdonline.ui.general.CustomerToolbar;
import com.theherdonline.ui.general.DataObserver;
import com.theherdonline.ui.general.HerdFragment;
import com.theherdonline.ui.main.MainActivity;
import com.theherdonline.ui.main.MainActivityViewModel;
import com.theherdonline.ui.main.payment.PaymentFragment;
import com.theherdonline.util.ActivityUtils;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UIUtils;
import dagger.Lazy;


public class EditProfileFragment extends HerdFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_USER = "param_user";
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
    AppUtil mAppUtil;


    User mUser;
    User mOldUser;

    String mAvatarUrl;
    String mOriginalAvatarUrl;


    // TODO: Rename and change types of parameters
    private FragmentEditAccountBinding mBinding;


    private DataObserver<User> mNewUserProfileObserver = new DataObserver<User>() {
        @Override
        public void onSuccess(User data) {
            showProgressBar(false);
           // mNetworkListener.onLoading(false);
            mProfileViewModel.setRefreshDataFlag(true);
            ActivityUtils.showWarningDialog(getContext(), getString(R.string.app_name),
                    getString(R.string.txt_your_profile_is_updated),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mGobackListener.OnClickGoBackButton();

                        }
                    });
        }

        @Override
        public void onError(Integer code, String msg) {
           // mNetworkListener.onLoading(false);
            showProgressBar(false);

            mNetworkListener.onFailed(code,msg);
        }

        @Override
        public void onLoading() {
            showProgressBar(true);

          //  mNetworkListener.onLoading(true);
        }

        @Override
        public void onDirty() {
            showProgressBar(false);

          //  mNetworkListener.onLoading(false);
        }
    };

    private DataObserver<User> mUserObserver = new DataObserver<User>() {
        @Override
        public void onSuccess(User data) {
            if (data == null)
                return;
            mOldUser = data;
            mBinding.editFirstName.setText(ActivityUtils.trimTextEmpty(data.getFirstName()));
            mBinding.editLastName.setText(ActivityUtils.trimTextEmpty(data.getLastName()));
            mBinding.editPhoneNumber.setText(ActivityUtils.trimTextEmpty(data.getPhone()));
            String avatarUrl = data.getAvatar_location_full_url();
            mMainViewModel.updateAvatar(avatarUrl);
            mUser = getNewUser();

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
    };

    @Override
    public CustomerToolbar getmCustomerToolbar() {
        return new CustomerToolbar(true,getString(R.string.txt_edit_account_details),null,null,null,null);
    }

    @Inject
    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_account, container, false);
        UIUtils.showToolbar(mBinding.toolbar,getString(R.string.txt_edit_account_details),mGobackListener);




        mBinding.imageAvatar.setOnClickListener(l->{
            ((MainActivity)getActivity()).popupPhotoDialog(getString(R.string.txt_change_avatar), AppConstants.ACTIVITY_CODE_AVARTAR_TAKE_PHOTO);
        });
        mBinding.buttonAction.setOnClickListener(l->{
            String userFirstName = mBinding.editFirstName.getText().toString();
            String userLastName = mBinding.editLastName.getText().toString();
            if (!ActivityUtils.checkInput(getContext(),getString(R.string.txt_firstname), userFirstName))
            {
                return;
            }
            if (!ActivityUtils.checkInput(getContext(),getString(R.string.txt_lastname), userLastName))
            {
                return;
            }
            User currentUser = getNewUser();

            String currentAvatar =  currentUser.getAvatarLocation();
            String newAvatar = currentAvatar.equals(mUser.getAvatarLocation()) ? null : currentAvatar;
            currentUser = currentUser.equals(mUser) ? null : currentUser;

            if (newAvatar == null && currentUser == null)
            {
                mGobackListener.OnClickGoBackButton();
            }
            else
            {
                mProfileViewModel.updateUserProfile(mUser.getId(),currentUser,newAvatar);
            }
        });
        return mBinding.getRoot();
    }

    public User getNewUser()
    {
        User user = new User();
        user.setId(mOldUser.getId());
        String userFirstName = mBinding.editFirstName.getText().toString();
        String userLastName = mBinding.editLastName.getText().toString();
        String userPhoneNumber = mBinding.editPhoneNumber.getText().toString();
        user.setFirstName(userFirstName);
        user.setLastName(userLastName);
        user.setPhone(userPhoneNumber);
        String avatarUrl = mMainViewModel.getmAvatarPath().getValue();
        user.setAvatarLocation(avatarUrl);
        return user;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mProfileViewModel.resetData();
    }


    public void showProgressBar(Boolean bShow)
    {
        mBinding.progressBar.setVisibility(bShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMainViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(MainActivityViewModel.class);

       /* UserProfileFragmentViewModel.Factory factoryProfile = new UserProfileFragmentViewModel.Factory(
                getActivity().getApplication(),
                mCurrentUserId);*/
        mProfileViewModel = ViewModelProviders.of(this, mLazyFactory.get())
                .get(UserProfileFragmentViewModel.class);
        mProfileViewModel.refreshUserProfile(mAppUtil.getUserId());
        subscription();
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityUtils.deleteFile(mMainViewModel.getmAvatarPath().getValue());

    }

    public void subscription() {
        mProfileViewModel.getmLiveDataUser().observe(this, mUserObserver);
        mProfileViewModel.getmLiveDataNewUserResponse().observe(this,mNewUserProfileObserver);
        /*mProfileViewModel.getmLiveDataLocalAvatarUrl().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (new File(s).isFile())
                {
                    ActivityUtils.loadCircleImage(getContext(),mBinding.imageAvatar,s,R.drawable.ic_default_person);
                    mBinding.imageAvatar.setTag(s);
                }
            }
        });*/

        mMainViewModel.getmAvatarPath().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String avatarUrl) {
                if (avatarUrl != null)
                {
                    if (new File(avatarUrl).isFile())
                    {
                        ActivityUtils.loadCircleImage(getContext(),mBinding.imageAvatar,avatarUrl,R.drawable.avatar_place_holder);
                       // mBinding.imageAvatar.setTag(avatarUrl);
                    }
                    else
                    {
                        ActivityUtils.loadCircleImage(getContext(),mBinding.imageAvatar,
                                ActivityUtils.getImageAbsoluteUrl(avatarUrl),
                                R.drawable.avatar_place_holder);
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
