package com.theherdonline.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.PagerData;
import com.theherdonline.api.NetworkBoundRemoteData;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.api.Resource;
import com.theherdonline.api.Status;
import com.theherdonline.ui.general.SearchFilter;
import retrofit2.Call;


public abstract class PagerListRepository<ItemType, ResponseType> {

    public  Integer INIT_CURRENT_PAGE = 1;
    private AppExecutors mAppExecutors;
    private final List<ItemType> mDataList = new ArrayList<>();
    private final LiveData<Resource<List<ItemType>>> mResultThisCall;
    private final MutableLiveData<String> mResultThisCallTrigger = new MutableLiveData<>();
    private final LiveData<Resource<List<ItemType>>> mResult;
    private SearchFilter mFilter;
    private RequestCommaValue mRequestInclude;
    private Integer mCurrentPage;
    private Integer mTotalPageNumber;
    private Integer mTotalItemNumber;


    public abstract List<ItemType> response2Data(ResponseType response);
    public abstract Call<ResponseType> grabDataFromRemote(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude);

    public PagerListRepository(AppExecutors appExecutors) {
        reset();
        mAppExecutors = appExecutors;
        mResultThisCall = Transformations.switchMap(mResultThisCallTrigger,(String trigger)->{
            return refreshData();
        });

        mResult = Transformations.switchMap(mResultThisCall,(Resource<List<ItemType>> data)->{
            MutableLiveData<Resource<List<ItemType>>> ret = new MutableLiveData<>();
            if (data.status == Status.SUCCESS)
            {
                if (data.data != null) {
                    if (mCurrentPage == INIT_CURRENT_PAGE + 1)
                    {
                        mDataList.clear();
                    }
                    mDataList.addAll(data.data);

                }
                ret.postValue(Resource.success(mDataList));
            }
            else if (data.status == Status.LOADING)
            {
                if (mCurrentPage == INIT_CURRENT_PAGE)
                {
                    ret.postValue(Resource.loading(null));
                }
                else
                {
                    ret.postValue(Resource.loadingMore(null));
                }
            } else if (data.status == Status.ERROR)
            {
                ret.postValue(Resource.error(data.errorCode,data.message,null));
            }
            else
            {
                ret.postValue(Resource.stale(null));
            }
            return ret;
        });
    }

    public SearchFilter getFilter() {
        return mFilter;
    }

    public void setFilter(SearchFilter mFilter) {
        this.mFilter = mFilter;
    }

    public RequestCommaValue getRequestInclude() {
        return mRequestInclude;
    }

    public void setRequestInclude(RequestCommaValue mRequestInclude) {
        this.mRequestInclude = mRequestInclude;
    }

    public LiveData<Resource<List<ItemType>>> asLiveData() {
        return mResult;
    }

    public List<ItemType> getDataList() {
        return mDataList;
    }

    public void refresh()
    {
        mResultThisCallTrigger.setValue("start");
    }

    public LiveData<Resource<List<ItemType>>> refreshData()
    {
        if (mTotalPageNumber != null && mTotalPageNumber < mCurrentPage)
        {
            //This is the last page
            MutableLiveData<Resource<List<ItemType>>> ret = new MutableLiveData<>();
            ret.postValue(Resource.error(AppConstants.DATA_ERROR_END,AppConstants.DATA_ERROR_END_INOF,null));
            return ret;
        }
        else {
            return new NetworkBoundRemoteData<List<ItemType>, ResponseType>(mAppExecutors) {
                @Override
                protected List<ItemType> convertResponse2Report(ResponseType response) {
                    return response2Data(response);
                }

                @Override
                protected void processReport(@NonNull List<ItemType> item) {

                }

                @Override
                protected void processResponse(@NonNull ResponseType item) {
                    if (item instanceof PagerData) {
                        mTotalPageNumber = ((PagerData)item).getTotalPageNumber();
                        mTotalItemNumber = ((PagerData)item).getTotal();
                        if (mCurrentPage <= mTotalPageNumber) {
                            mCurrentPage++;
                        }
                    }
                    else
                    {
                        throw new RuntimeException("must implement ListPageData");
                    }
                }

                @Override
                protected Call<ResponseType> createRequest() {
                    //return mRetrofitClient.searchsaleyards(mFilter,mCurrentPage);
                    return grabDataFromRemote(mCurrentPage,mFilter,mRequestInclude);
                }
            }.asLiveData();
        }
    }

    public void reset(){
        mCurrentPage = INIT_CURRENT_PAGE;
        mTotalPageNumber = null;
        //mDataList.clear();
    }

    public Integer getTotalPage()
    {
        return mTotalPageNumber;
    }

    public Integer getTotalItemNumber()
    {
        return mTotalItemNumber;
    }


    public Integer getCurrentPageIndex()
    {
        return mCurrentPage;
    }

}
