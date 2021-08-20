package com.theherdonline.ui.profile;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.annotation.NonNull;

import java.util.List;

import com.theherdonline.repository.PagerListRepository;
import com.theherdonline.di.MyApplication;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.api.Resource;
import com.theherdonline.api.RetrofitClient;
import com.theherdonline.ui.general.SearchFilter;
import retrofit2.Call;

public abstract class PagerViewModel<ItemType, ResponseType> extends AndroidViewModel {
    public final Application mApplication;
    public final PagerListRepository mRepository;
    public final RetrofitClient mRetrofitClient;
    public final LiveData<Resource<List<ItemType>>> mLiveDataStreamList;
    public abstract List<ItemType> convertResponse2ItemList(ResponseType responseType);
    public abstract Call<ResponseType> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude);
    public PagerViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
        if (application instanceof MyApplication) {
            MyApplication myApplication = (MyApplication) application;
            mRetrofitClient = myApplication.getmRetrofitClient();
            mRepository = new PagerListRepository<ItemType, ResponseType>(myApplication.getmAppExecutors())
            {
                @Override
                public List<ItemType> response2Data(ResponseType response) {
                    return convertResponse2ItemList(response);
                }

                @Override
                public Call<ResponseType> grabDataFromRemote(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
                    return requestCall(currentPage,filter,requestInclude);
                }       
            };
        }
        else
        {
            mRepository = null;
            mRetrofitClient = null;
            throw new RuntimeException("Application is not" + MyApplication.class.getName());
        }
        mLiveDataStreamList = mRepository.asLiveData();
    }

    public LiveData<Resource<List<ItemType>>> getmLiveDataStreamList() {
        return mLiveDataStreamList;
    }

    public List<ItemType> getDataList()
    {
        return mRepository.getDataList();
    }

    public void refreshRepository(Boolean isScrolling)
    {
       // if (mRepository.getDataList().size() == 0 || isScrolling == true)
       // {
            mRepository.refresh();
       // }
    }

    public void resetRepository()
    {
        mRepository.reset();
    }


    public LiveData<Resource<Void>> deleteItem(int id)
    {
        return null;
    }

    public Integer totalItemNumber()
    {
        return mRepository.getTotalItemNumber();
    }

    public PagerListRepository getmRepository() {
        return mRepository;
    }
}
