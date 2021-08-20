package com.theherdonline.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.entity.User;
import com.theherdonline.api.Resource;
import com.theherdonline.api.StaleData;

public class UserProfileFragmentViewModel extends ViewModel {

    final private DataRepository mRepository;
    final private LiveData<Resource<User>> mLiveDataUser;
    final private MutableLiveData<Integer> mLiveDataUserId = new MutableLiveData<>();

    final private LiveData<Resource<Void>> mLiveDataFollowResponse;
    final private MutableLiveData<Boolean> mLiveDataFollowStatus = new MutableLiveData<>();

    final private MutableLiveData<String> mLiveDataUploadAvatarUrl = new MutableLiveData<>();
    final private MutableLiveData<String> mLiveDataLocalAvatarUrl = new MutableLiveData<>();


    final private LiveData<Resource<User>> mLiveDataNewUserResponse;
    final private MutableLiveData<Integer> mLiveDataNewUserID = new MutableLiveData<>();

    private String mCurrentAvatarUrl;
    private Integer mUserId;
    private User    mNewUser;


    private Boolean mNeedRefreshData = true;


    @Inject
    public UserProfileFragmentViewModel(DataRepository repository) {
        mRepository = repository;
        mLiveDataUser = Transformations.switchMap(mLiveDataUserId, (Integer id)->{
                return repository.getUserById(id);

        });

        mLiveDataFollowResponse = Transformations.switchMap(mLiveDataFollowStatus, (Boolean isFollowing) ->{
            return repository.setFollowing(mLiveDataUserId.getValue(),isFollowing);
        });

        mLiveDataNewUserResponse = Transformations.switchMap(mLiveDataNewUserID, (Integer userId) ->{
            if (userId == null)
            {
                return new StaleData<>();
            }
            else {
                return repository.updateUser(mUserId, mNewUser, mCurrentAvatarUrl);
            }
        });

    }


    public void setRefreshDataFlag(Boolean bFlag)
    {
        mNeedRefreshData = bFlag;
    }

    public Boolean getmNeedRefreshData() {
        return mNeedRefreshData;
    }

    public LiveData<Resource<User>> getmLiveDataNewUserResponse() {
        return mLiveDataNewUserResponse;
    }

    public void updateUserProfile(Integer userId, User user, String avatar)
    {
        mUserId = userId;
        mCurrentAvatarUrl = avatar;
        mNewUser = user;
        mLiveDataNewUserID.setValue(userId);
    }

    public void resetData() {
        mLiveDataNewUserID.setValue(null);
    }

    public MutableLiveData<String> getmLiveDataLocalAvatarUrl() {
        return mLiveDataLocalAvatarUrl;
    }

    public void setFollow(Boolean isFollow)
    {
        mLiveDataFollowStatus.setValue(isFollow);
    }

    public LiveData<Resource<Void>> getmLiveDataFollowResponse() {
        return mLiveDataFollowResponse;
    }

    public void refreshUserProfile(Integer userId)
    {
        mLiveDataUserId.setValue(userId);
    }

    public LiveData<Resource<User>> getmLiveDataUser() {
        return mLiveDataUser;
    }
}
