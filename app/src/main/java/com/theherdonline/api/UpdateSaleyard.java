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

public class UpdateSaleyard {
    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final EntitySaleyard mEntitySaleyard;
    private final MediatorLiveData<Resource<EntitySaleyard>> result = new MediatorLiveData<>();
    private final Integer mLivestockId;
    final private List<Media> mImageListToUpload;
    final private List<Media> mImageListToDelete;
    private EntitySaleyard mNewLivestock;

    public UpdateSaleyard(AppExecutors appExecutors, RetrofitClient retrofitClient, Integer saleyardId, EntitySaleyard entitySaleyard, List<Media> mediaListToDelete,
                          List<Media> mediaListToUpload) {
        mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mEntitySaleyard = entitySaleyard;
        mLivestockId = saleyardId;
        mImageListToUpload = mediaListToUpload;
        mImageListToDelete = mediaListToDelete;
        mAppExecutors.networkIO().execute(() -> {
            result.postValue(Resource.loading(null, "uploading"));
            if (ListUtils.emptyIfNull(mImageListToUpload).size() > 0) {


                HashMap<String, List<Media>> hashMap = UIUtils.classifyMediasFullTypes(mImageListToUpload);
                List<Media> listPhoto = ListUtils.emptyIfNull(hashMap.get(Media.CoolectionName.images.name()));
                List<Media> listVideo = ListUtils.emptyIfNull(hashMap.get(Media.CoolectionName.videos.name()));
                List<Media> listPdf = ListUtils.emptyIfNull(hashMap.get(Media.CoolectionName.pdfs.name()));

/*                for (Media m : mImageListToUpload) {
                    if (AppUtil.isVideo(m)) {
                        listVideo.add(m);
                    } else if (AppUtil.isImage(m)) {
                        listPhoto.add(m);
                    } else {
                        listPdf.add(m);
                    }
                }*/
                if (listPhoto.size() > 0) {
                    Call<List<Media>> mediaCall = mRetrofitClient.postSaleyardMediaImage(mLivestockId, listPhoto);
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
                        result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to upload entitySaleyard image", null));
                        return;
                    }
                }

                if (listVideo.size() > 0) {
                    Call<List<Media>> mediaCall = mRetrofitClient.postSaleyardkMediaVideo(mLivestockId, listVideo);
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

                if (listPdf.size() > 0) {
                    Call<List<Media>> mediaCall = mRetrofitClient.postSaleyardkMediaPdf(mLivestockId, listPdf);
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

            // delete photo
            if (ListUtils.emptyIfNull(mImageListToDelete).size() > 0) {
                for (Media m : mediaListToDelete) {
                    if (m.getId() != null) {
                        Call<Void> deleteCall = mRetrofitClient.deleteSaleyardMedia(mLivestockId, m.getId());
                        try {
                            Response<Void> response = deleteCall.execute();
                            result.postValue(Resource.loading(null, "deleting image"));
                            if (response.isSuccessful()) {

                            } else {
                                ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                                result.postValue(Resource.error(response.code(), message.getMsg(), null));
                                return;
                            }
                        } catch (IOException e) {
                            result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to delete media", null));
                            return;
                        }
                    }
                }
            }
            if (mEntitySaleyard != null) {
                Call<EntitySaleyard> advertisementCall = mRetrofitClient.storeSaleyard(mEntitySaleyard, mLivestockId);
                try {
                    Response<EntitySaleyard> response = advertisementCall.execute();
                    if (response.isSuccessful()) {
                        mNewLivestock = response.body();
                    } else {
                        ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                        result.postValue(Resource.error(response.code(), message.getMsg(), null));
                        return;
                    }
                } catch (IOException e) {
                    result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to update entitySaleyard", null));
                    return;
                }
            }

            // get new entitySaleyard
            Call<EntitySaleyard> newSaleyard = mRetrofitClient.getSaleyardById(saleyardId);
            try {
                Response<EntitySaleyard> response = newSaleyard.execute();
                if (response.isSuccessful()) {
                    mNewLivestock = response.body();
                } else {
                    ResError message = NetworkUtil.parseErrorResponseV2(response.errorBody());
                    result.postValue(Resource.error(response.code(), message.getMsg(), null));
                    return;
                }
            } catch (IOException e) {
                result.postValue(Resource.error(AppConstants.DATA_ERROR_UNKNOW, "Failed to get new entitySaleyard data", null));
                return;
            }
            result.postValue(Resource.success(mNewLivestock));
        });
    }

    public LiveData<Resource<EntitySaleyard>> asLiveData() {
        return result;
    }
}
