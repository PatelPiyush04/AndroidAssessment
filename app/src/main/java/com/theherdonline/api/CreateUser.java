package com.theherdonline.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.io.IOException;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.ResError;
import com.theherdonline.db.entity.ResLogin;
import com.theherdonline.db.entity.User;
import com.theherdonline.util.AppUtil;

import retrofit2.Call;
import retrofit2.Response;

public class CreateUser {
    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final User mUser;
    private final MediatorLiveData<Resource<User>> result = new MediatorLiveData<>();
    private Integer mUserId;
    private String  mPassword;
    private String  mAvatar;
    private Boolean mRegister = true;


    public CreateUser(AppExecutors appExecutors, RetrofitClient retrofitClient, User user, String password, String avatar, AppUtil appUtil) {
        mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mUser = user;
        mUserId = user.getId();
        mPassword = password;
        mAvatar = avatar;
        mRegister = user.getId() == null;
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                int responseCode = 200;
                result.postValue(Resource.loading(null,"loading"));
                // create a new user
                Call<User> advertisementCall;
                if (mUserId == null && password != null)
                {
                    advertisementCall = mRetrofitClient.createUser(user,mPassword);
                }
                else
                {
                    advertisementCall = mRetrofitClient.updateUser(user);

                }
                try {
                    Response<User> response = advertisementCall.execute();
                    responseCode = response.code();
                    if (response.isSuccessful()) {
                        mUserId = response.body().getId();
                        User newUser = response.body();

                        if (mRegister)
                        {
                                Call<ResLogin> loginCall =  mRetrofitClient.postLogin(user.getEmail(), password);
                            try {
                               Response<ResLogin> resLoginResponse = loginCall.execute();
                               if (resLoginResponse.isSuccessful())
                               {
                                   appUtil.updateAccessToken(resLoginResponse.body().getAccess_token());
                                   result.postValue(Resource.success(newUser));
                                   return;
                               }
                               else
                               {
                                   ResError message = NetworkUtil.parseErrorResponseV2(resLoginResponse.errorBody());
                                   result.postValue(Resource.error(resLoginResponse.code(), message, null));
                                   return;
                               }
                            }
                            catch (IOException e) {
                                result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, e.getMessage(), null));
                                return;
                            }

                        }


                        if (avatar != null) {

                            if (mRegister)
                            {
                                // new user, log in to get token, then

                            }
                            result.postValue(Resource.loading(null, "media"));
                            Call<Media> mediaCall = mRetrofitClient.postUserAvatar(mUserId, mAvatar);
                            try {
                                Response<Media> responseMeida = mediaCall.execute();
                                // result.postValue(Resource.loading(null, "uploading avatar"));
                                responseCode = responseMeida.code();
                                if (responseCode >= 200 && responseCode <= 300) {
                                    result.postValue(Resource.success(newUser));
                                } else {
                                    ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                                    result.postValue(Resource.error(responseCode, message, null));
                                    return;
                                }
                            } catch (IOException e) {
                                result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to create user", null));
                                return;
                            }
                        }
                        else
                        {
                            result.postValue(Resource.success(newUser));
                        }
                    }
                    else
                    {
                        ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                        result.postValue(Resource.error(responseCode, message, null));
                    }
                } catch (IOException e) {
                    result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, e.getMessage(), null));
                }
            }
        });
    }


    public LiveData<Resource<User>> asLiveData() {
        return result;
    }
}
