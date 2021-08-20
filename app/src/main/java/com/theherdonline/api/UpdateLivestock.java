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

public class UpdateLivestock {
    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final EntityLivestock mLivestock;
    private final MediatorLiveData<Resource<EntityLivestock>> result = new MediatorLiveData<>();
    private final Integer mLivestockId;
    private List<Media> mImageListToUpload = new ArrayList<>();
    private final List<Media> mImageListToDelete;
    private EntityLivestock mNewLivestock;
    private final  Boolean mNeedUpdate;
    public UpdateLivestock(AppExecutors appExecutors, RetrofitClient retrofitClient, Integer livestockId, EntityLivestock livestock, List<Media> mediaListToDelete,
                           Boolean bNeedUpdate) {
        mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mLivestock = livestock;
        mLivestockId = livestockId;
        mNeedUpdate = bNeedUpdate;

        if (livestock.getMedia() != null && livestock.getMedia().size() >= 0)
        {
            for (Media media : livestock.getMedia())
            {
                if (new File(media.getUrl()).exists())
                {
                    mImageListToUpload.add(media);
                }
            }
        }

        mImageListToDelete = mediaListToDelete;
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                int responseCode = 200;
                result.postValue(Resource.loading(null,"uploading"));
                // upload photo
                if (mImageListToUpload != null &&  mImageListToUpload.size() != 0)
                {

                    List<Media> listPhoto = new ArrayList<>();
                    List<Media> listVideo = new ArrayList<>();
                    for (Media m : mImageListToUpload)
                    {
                        if (AppUtil.isVideo(m))
                        {
                            listVideo.add(m);
                        }
                        else
                        {
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

                // delete photo
                if (mImageListToDelete !=  null && mImageListToDelete.size() != 0)
                {
                    for (Media m : mediaListToDelete)
                    {
                        if (m.getId() != null) {
                            Call<Void> deleteCall = mRetrofitClient.deleteLivestockMedia(mLivestockId, m.getId());
                            try {
                                Response<Void> response = deleteCall.execute();
                                result.postValue(Resource.loading(null, "deleting image"));
                                responseCode = response.code();
                                if (responseCode >= 200 && responseCode <= 300) {

                                } else {
                                    ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                                    result.postValue(Resource.error(responseCode, message.getMsg(), null));
                                    return;
                                }
                            } catch (IOException e) {
                                result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to delete media", null));
                                return;
                            }
                        }
                    }
                }
                if (mLivestock != null && mNeedUpdate) {
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



/*                else if (mediaListToDelete != null  || mediaListToDelete != null)
                {
                    // refresh latest data
                    Call<EntityLivestock> advertisementCall = mRetrofitClient.getLivestockById(mLivestockId);
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
                }*/
                result.postValue(Resource.success(mNewLivestock));
            }
        });
    }

    public LiveData<Resource<EntityLivestock>> asLiveData() {
        return result;
    }
}
