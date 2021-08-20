package com.theherdonline.ui.marketReport;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import com.theherdonline.api.NetworkBoundRemoteData;
import com.theherdonline.api.Resource;
import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.ResMarketReport;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.di.MyApplication;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;
import retrofit2.Call;

public class MarketReportPagerViewModel extends PagerViewModel<MarketReport, ResMarketReport> {

    final Integer mUserId;
    AppExecutors mAppExecutors;

    public MarketReportPagerViewModel(@NonNull Application application, Integer mUserId) {
        super(application);
        mAppExecutors = ((MyApplication)application).getmAppExecutors();
        this.mUserId = mUserId;
        /*this.mOfUserId = mOfUser;
        this.mStreamType = streamType;*/
        SearchFilter filter = new SearchFilter();
        filter.setUser_id(mUserId);
        mRepository.setFilter(filter);
        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_user, AppConstants.TAG_user_dot_organisation,AppConstants.TAG_saleyard,AppConstants.TAG_media);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<MarketReport> convertResponse2ItemList(ResMarketReport resMarketReport) {
        return resMarketReport.getData();
    }

    @Override
    public Call<ResMarketReport> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        return mRetrofitClient.getMarketReport(currentPage,filter,requestInclude);
    }

    @Override
    public LiveData<Resource<Void>> deleteItem(int id) {
        return new NetworkBoundRemoteData<Void,Void>(mAppExecutors){
            @Override
            protected Void convertResponse2Report(Void response) {
                return response;
            }

            @Override
            protected void processReport(@NonNull Void item) {

            }

            @Override
            protected void processResponse(@NonNull Void item) {

            }

            @Override
            protected Call<Void> createRequest() {
                return mRetrofitClient.deleteMarketReport(id);
            }
        }.asLiveData();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mUserId;

        public Factory(@NonNull Application application, Integer userId) {
            mApplication = application;
            mUserId = userId;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MarketReportPagerViewModel(mApplication, mUserId);
        }
    }

}
