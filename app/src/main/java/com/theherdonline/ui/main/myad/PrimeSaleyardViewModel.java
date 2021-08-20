package com.theherdonline.ui.main.myad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.api.NetworkBoundRemoteData;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.api.Resource;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.di.MyApplication;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.general.SearchFilterPublicSaleyard;
import com.theherdonline.ui.main.saleyardsearch.SaleyardPagerViewModel;

import retrofit2.Call;

public class PrimeSaleyardViewModel extends SaleyardPagerViewModel {

    AppExecutors mAppExecutors;

    public PrimeSaleyardViewModel(@NonNull Application application, SearchFilter searchFilter) {
        super(application, searchFilter);
        mAppExecutors = ((MyApplication)application).getmAppExecutors();

    }

    @Override
    public Call<ResPagerSaleyard> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        //return super.requestCall(currentPage, filter, requestInclude);
        return mRetrofitClient.getPagerSaleyards(null, ((SearchFilterPublicSaleyard)filter).saleyard_status_id, ((SearchFilterPublicSaleyard)filter).type);

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
                return mRetrofitClient.deleteSaleyard(id);
            }
        }.asLiveData();
    }


    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final SearchFilter mSearchFilter;

        public Factory(@NonNull Application application,SearchFilter searchFilter) {
            mApplication = application;
            mSearchFilter = searchFilter;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new PrimeSaleyardViewModel(mApplication, mSearchFilter);
        }
    }
}
