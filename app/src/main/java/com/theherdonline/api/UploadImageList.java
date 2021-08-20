package com.theherdonline.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.ResUploadImage;

public class UploadImageList {

    private final AppExecutors mAppExecutors;
    private final RetrofitClient mRetrofitClient;
    private final Integer mLivestockId;
    private final List<String> mImageList;
    private final MediatorLiveData<Resource<ResUploadImage>> result = new MediatorLiveData<>();

    private int mFinished = 0;

    public UploadImageList(AppExecutors appExecutors, RetrofitClient retrofitClient, Integer livestockId, List<String> imageList) {
       mAppExecutors = appExecutors;
        mRetrofitClient = retrofitClient;
        mImageList = imageList;
        mLivestockId = livestockId;
       /* int total = mImageList.size();
        mAppExecutors.networkIO().execute(new Runnable() {
            @Override
            public void run() {
                int responseCode = 200;
                result.postValue(Resource.loading(new ResUploadImage(total,0)));
                for (int i = 0; i < mImageList.size(); i++)
                {
                    String imagePath = mImageList.get(i);
                    Call<Media> mediaCall =  mRetrofitClient.postLivestockMedia(mLivestockId,imagePath);
                    try {
                        Response<Media> response = mediaCall.execute();
                        if (response.code() >= 200 && response.code() <= 300)
                        {
                            mFinished++;
                            result.postValue(Resource.loading(new ResUploadImage(total,mFinished)));
                            System.out.println(String.format("Upload Image %d / %d", mFinished, total));
                        }
                        else
                        {
                            responseCode = response.code();
                            break;
                        }

                    }
                    catch (IOException e)
                    {
                        break;
                    }
                }
                if (mFinished == mImageList.size())
                {
                    //finish
                    result.postValue(Resource.success(new ResUploadImage(total,mFinished)));
                }
                else
                {
                    //not finish
                    String msg = String.format("Failed to upload Image. Total: %d, Finished: %d", total,mFinished);
                    result.postValue(Resource.error(responseCode,msg, new ResUploadImage(total,mFinished)));
                }
            }
        });*/
    }

    public LiveData<Resource<ResUploadImage>> asLiveData() {
        return result;
    }
}
