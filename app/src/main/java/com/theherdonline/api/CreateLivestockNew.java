package com.theherdonline.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.ResError;
import com.theherdonline.util.AppUtil;

import retrofit2.Call;
import retrofit2.Response;

public class CreateLivestockNew {
    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final EntityLivestock mLivestock;
    private final MediatorLiveData<Resource<EntityLivestock>> result = new MediatorLiveData<>();
    private Integer mLivestockId;
    private EntityLivestock mNewLivestock;
    private final File mCacheDir;
    public CreateLivestockNew(AppExecutors appExecutors, RetrofitClient retrofitClient, EntityLivestock livestock, File  cacheDir ) {
        mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mLivestock = livestock;
        mCacheDir = cacheDir;
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                int responseCode = 200;
                result.postValue(Resource.loading(null,"uploading"));
                if (mLivestock != null) {
                    Call<EntityLivestock> advertisementCall = mRetrofitClient.storeLivestock(mLivestock, mLivestockId);
                    try {
                        Response<EntityLivestock> response = advertisementCall.execute();
                        responseCode = response.code();
                        if (responseCode >= 200 && responseCode <= 300) {
                            mNewLivestock = response.body();

                        }
                        else
                        {
                            ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                            result.postValue(Resource.error(responseCode, message.getMsg(), null));
                            return;
                        }
                    } catch (IOException e) {
                        result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to update livestock", null));
                    }
                }
                mLivestockId = mNewLivestock.getId();

                if (mLivestock.getMedia() != null && mLivestock.getMedia().size() > 0)
                {

                    List<Media> listPhoto = new ArrayList<>();
                    List<Media> listVideo = new ArrayList<>();
                    for (Media m : mLivestock.getMedia())
                    {
                        if (AppUtil.isVideo(m))
                        {
                            listVideo.add(m);
                        }
                        else
                        {
                          //  m.setUrl(AppUtil.getCompressImageName(mCacheDir, m.getUrl()));
                            listPhoto.add(m);
                        }
                    }

                    if (listPhoto.size() > 0) {
                        Call<List<Media>> mediaCall = mRetrofitClient.postLivestockMediaImage(mLivestockId, listPhoto);
                        try {
                            Response<List<Media>> response = mediaCall.execute();
                            result.postValue(Resource.loading(null, "uploading image"));
                            responseCode = response.code();
                            if (responseCode >= 200 && responseCode <= 300) {
                               // System.out.println("Time consuming: " + (DateTime.now().getMillis() - now.getMillis()));

                            } else {
                                ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                                result.postValue(Resource.error(responseCode, message.getMsg(), null));
                                return;
                            }
                        } catch (IOException e) {
                            result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to create livestock", null));
                            return;
                        }
                    }

                    if (listVideo.size() > 0) {
                        Call<List<Media>> mediaCall = mRetrofitClient.postLivestockMediaVideo(mLivestockId, listVideo);
                        try {
                            Response<List<Media>> response = mediaCall.execute();
                            result.postValue(Resource.loading(null, "uploading image"));
                            responseCode = response.code();
                            if (responseCode >= 200 && responseCode <= 300) {

                            } else {
                                ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                                result.postValue(Resource.error(responseCode, message.getMsg(), null));
                                return;
                            }
                        } catch (IOException e) {
                            result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to create livestock", null));
                            return;
                        }
                    }


                }

                result.postValue(Resource.success(mNewLivestock));
            }
        });
    }

    public LiveData<Resource<EntityLivestock>> asLiveData() {
        return result;
    }
}
