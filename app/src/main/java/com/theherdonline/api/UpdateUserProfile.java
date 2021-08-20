package com.theherdonline.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.io.File;
import java.io.IOException;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.ResError;
import com.theherdonline.db.entity.User;
import com.theherdonline.util.AppUtil;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateUserProfile {
    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final User mUser;
    private final MediatorLiveData<Resource<User>> result = new MediatorLiveData<>();
    private Integer mUserId;
    private String  mAvatar;


    public UpdateUserProfile(AppExecutors appExecutors, RetrofitClient retrofitClient, Integer id, User user, String avatar) {
        mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mUser = user;
        mUserId = id;
        mAvatar = avatar;
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                result.postValue(Resource.loading(null,"loading"));
                Boolean bNeedUpdateAvatar = avatar != null && new File(avatar).isFile();
                Boolean bNeedUpdateProfile = user != null;
                if (bNeedUpdateAvatar)
                {
                    // need update avatar
                    updateAvatar(id,avatar);
                }
                else if (!bNeedUpdateAvatar && bNeedUpdateProfile)
                {
                    // need to update user profile
                    if (bNeedUpdateProfile)
                    {
                        updateProfile(user);
                    }
                }
            }
        });
    }

    private void updateProfile(User user)
    {
        Call<User>  callUpdateUserProfile = mRetrofitClient.updateUser(user);
        try {
            Response<User> responseUpdateProfile = callUpdateUserProfile.execute();
            // result.postValue(Resource.loading(null, "uploading avatar"));
            int  responseCode = responseUpdateProfile.code();
            if (responseCode >= 200 && responseCode <= 300) {
                result.postValue(Resource.success(responseUpdateProfile.body()));
            } else {
                ResError message = NetworkUtil.parseErrorResponseV2(responseUpdateProfile.errorBody());
                result.postValue(Resource.error(responseCode, message, null));
                return;
            }
        } catch (IOException e) {
            result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to update user profile", null));
            return;
        }
    }


    private void updateAvatar(Integer id, String avatarUrl)
    {
        String compressedAvatar = AppUtil.compressAvatarImage(avatarUrl);
        Call<Media> callMedia = mRetrofitClient.postUserAvatar(id, avatarUrl);
        try {
            Response<Media> responseUpdateAvatar = callMedia.execute();
            // result.postValue(Resource.loading(null, "uploading avatar"));
            int  responseCode = responseUpdateAvatar.code();
            if (responseCode >= 200 && responseCode <= 300) {
                AppUtil.deleteFile(compressedAvatar);
                if (mUser != null)
                {
                    updateProfile(mUser);
                }
            } else {
                ResError message = NetworkUtil.parseErrorResponseV2(responseUpdateAvatar.errorBody());
                result.postValue(Resource.error(responseCode, message, null));
                return;
            }
        } catch (IOException e) {
            result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to update user avatar", null));
            return;
        }
    }

    public LiveData<Resource<User>> asLiveData() {
        return result;
    }
}
