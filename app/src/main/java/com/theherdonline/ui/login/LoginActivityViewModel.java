package com.theherdonline.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.entity.ResLogin;
import com.theherdonline.db.entity.User;
import com.theherdonline.api.Resource;
import com.theherdonline.api.StaleData;


public class LoginActivityViewModel extends ViewModel {

    final static private String mDirtyTag = "dirty";
    final static private String mRefreshTag = "refresh";

    final private DataRepository mDataRepository;

    // Login
    final private LiveData<Resource<ResLogin>> mLiveDataLogin;
    final private MutableLiveData<String> mLiveDataLoginTrigger = new MutableLiveData<>();
    // Checkout user info
    final private LiveData<Resource<User>> mLiveDataUserInfo;
    final private MutableLiveData<String> mLiveDataUserInfoTrigger = new MutableLiveData<>();
    public String mUsername;
    public String mPassword;

    private Boolean mIsloginWithFacebook = false;
    private String  mFacebookToken;

    // Login
    private MutableLiveData<User> mLDUserData = new MutableLiveData<>();

    final private LiveData<Resource<User>> mLDResNewUser;

    final private MutableLiveData<String> mLiveDataRegisterTrigger = new MutableLiveData<>();


    @Inject
    public LoginActivityViewModel(DataRepository dataRepository) {
        mDataRepository = dataRepository;

        mLDResNewUser = Transformations.switchMap(mLDUserData, (User user) ->{
            return mDataRepository.createUser(user, mPassword,null);
        });


        mLiveDataLogin = Transformations.switchMap(mLiveDataLoginTrigger, (String trigger) -> {
            if (mDirtyTag.equals(trigger)) {
                return new StaleData<ResLogin>();
            } else {
                if (!mIsloginWithFacebook) {
                    if (mUsername != null && mPassword != null) {
                        return mDataRepository.login(mUsername, mPassword);
                    } else {
                        return new StaleData<ResLogin>();
                    }
                }
                else
                {
                    if (mFacebookToken != null)
                    {
                        return mDataRepository.login(null,mFacebookToken);
                    }
                    else
                    {
                        return new StaleData<ResLogin>();
                    }
                }
            }
        });

        // checkout user info
        mLiveDataUserInfo = Transformations.switchMap(mLiveDataUserInfoTrigger, (String trigger) -> {
            return mDataRepository.getUserById(null);
        });
    }

    public LiveData<Resource<ResLogin>> getmLiveDataLogin() {
        return mLiveDataLogin;
    }


    public LiveData<Resource<User>> getmLDResNewUser() {
        return mLDResNewUser;
    }

    public MutableLiveData<User> getmLDUserData() {
        return mLDUserData;
    }

    public MutableLiveData<String> getmLiveDataRegisterTrigger() {
        return mLiveDataRegisterTrigger;
    }

    public void login(final String username, final String password) {
        mUsername = username;
        mPassword = password;
        mIsloginWithFacebook = false;
        mLiveDataLoginTrigger.setValue(mRefreshTag);
    }


    public void loginWithFackbook(final String accessToken) {
        mIsloginWithFacebook = true;
        mFacebookToken = accessToken;
        mLiveDataLoginTrigger.setValue(mRefreshTag);
    }

    public LiveData<Resource<User>> getmLiveDataUserInfo() {
        return mLiveDataUserInfo;
    }

    public void getUserInf() {
        mLiveDataUserInfoTrigger.setValue(mRefreshTag);
    }

    public void createUer(User user, String password)
    {
        mPassword = password;
        mLDUserData.setValue(user);
    }


}