package com.theherdonline.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.Media;
import com.theherdonline.db.entity.ResError;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.util.UIUtils;

import org.apache.commons.collections4.ListUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class CreateSaleyardNew {
    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final EntitySaleyard mEntitySaleyard;
    private final MediatorLiveData<Resource<EntitySaleyard>> result = new MediatorLiveData<>();
    private Integer mSaleyardId;
    private EntitySaleyard mNewEntitySaleyard;
    public CreateSaleyardNew(AppExecutors appExecutors, RetrofitClient retrofitClient, EntitySaleyard entitySaleyard) {
        mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mEntitySaleyard = entitySaleyard;
        mAppExecutors.networkIO().execute(() -> {
            result.postValue(Resource.loading(null,"uploading"));
            if (mEntitySaleyard != null) {
                Call<EntitySaleyard> advertisementCall = mRetrofitClient.storeSaleyard(mEntitySaleyard, mSaleyardId);
                try {
                    Response<EntitySaleyard> response = advertisementCall.execute();
                    if (response.isSuccessful()) {
                        mNewEntitySaleyard = response.body();
                    }
                    else
                    {
                        ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                        result.postValue(Resource.error(response.code(), message.getMsg(), null));
                        return;
                    }
                } catch (IOException e) {
                    result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to update livestock", null));
                    return;
                }
            }
            mSaleyardId = mNewEntitySaleyard.getId();
            if (mEntitySaleyard.getMedia() != null && mEntitySaleyard.getMedia().size() > 0)
            {


                HashMap<String, List<Media>> hashMap = UIUtils.classifyMediasFullTypes(mEntitySaleyard.getMedia());
                List<Media> listPhoto = ListUtils.emptyIfNull(hashMap.get(Media.CoolectionName.images.name()));
                List<Media> listVideo = ListUtils.emptyIfNull(hashMap.get(Media.CoolectionName.videos.name()));
                List<Media> listPdf = ListUtils.emptyIfNull(hashMap.get(Media.CoolectionName.pdfs.name()));
                if (listPhoto.size() > 0) {
                    Call<List<Media>> mediaCall = mRetrofitClient.postSaleyardMediaImage(mSaleyardId, listPhoto);
                    try {
                        Response<List<Media>> response = mediaCall.execute();
                        result.postValue(Resource.loading(null, "uploading image"));
                        if (response.isSuccessful()) {
                        } else {
                            ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                            result.postValue(Resource.error(response.code(), message.getMsg(), null));
                            return;
                        }
                    } catch (IOException e) {
                        result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to upload image for new entitySaleyard", null));
                        return;
                    }
                }

                if (listVideo.size() > 0) {
                    Call<List<Media>> mediaCall = mRetrofitClient.postSaleyardkMediaVideo(mSaleyardId, listVideo);
                    try {
                        Response<List<Media>> response = mediaCall.execute();
                        result.postValue(Resource.loading(null, "uploading image"));
                        if (response.isSuccessful()) {

                        } else {
                            ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                            result.postValue(Resource.error(response.code(), message.getMsg(), null));
                            return;
                        }
                    } catch (IOException e) {
                        result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to upload video for new entitySaleyard", null));
                        return;
                    }
                }


                if (listPdf.size() > 0) {
                    Call<List<Media>> mediaCall = mRetrofitClient.postSaleyardkMediaPdf(mSaleyardId, listPdf);
                    try {
                        Response<List<Media>> response = mediaCall.execute();
                        result.postValue(Resource.loading(null, "uploading image"));
                        if (response.isSuccessful()) {

                        } else {
                            ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                            result.postValue(Resource.error(response.code(), message.getMsg(), null));
                            return;
                        }
                    } catch (IOException e) {
                        result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to upload entitySaleyard video", null));
                        return;
                    }
                }


            }
            result.postValue(Resource.success(mNewEntitySaleyard));
        });
    }

    public LiveData<Resource<EntitySaleyard>> asLiveData() {
        return result;
    }
}
