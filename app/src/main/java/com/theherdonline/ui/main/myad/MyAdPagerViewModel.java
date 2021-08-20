package com.theherdonline.ui.main.myad;

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
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.ResLivestock;
import com.theherdonline.di.MyApplication;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UserType;
import retrofit2.Call;



public class MyAdPagerViewModel extends PagerViewModel<EntityLivestock, ResLivestock> {

    final Integer mUserId;
    final UserType mUserType;
    final Boolean mIsPublicPage;        // true for public search, false for logged user
    final Boolean mIsAuctionPlus;
    AppExecutors mAppExecutors;

    public MyAdPagerViewModel(@NonNull Application application,SearchFilter searchFilter,Boolean isPublic, Boolean isAuctionPlus) {
        super(application);
        AppUtil appUtil = ((MyApplication)application).getmAppUtil();
        mUserId = appUtil.getUserId();
        mUserType = appUtil.getUserType();
        mIsPublicPage = isPublic;
        mIsAuctionPlus = isAuctionPlus;

        mAppExecutors = ((MyApplication)application).getmAppExecutors();

        /*SearchFilter filter = new SearchFilter();
        if (!isPublic) {
            if (mUserType == UserType.AGENT) {
                filter.setAgent_id(mUserId);
            } else {
                filter.setUser_id(mUserId);
            }
            filter.setAdvertisement_status_id(DataConverter.ADType2Int(adType));
        }*/
        mRepository.setFilter(searchFilter);
        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_media,AppConstants.TAG_images,AppConstants.TAG_agent,AppConstants.TAG_chats);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<EntityLivestock> convertResponse2ItemList(ResLivestock resLivestock) {
        return resLivestock .getData();
    }

    @Override
    public Call<ResLivestock> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        if (mIsPublicPage)
        {
            return mRetrofitClient. getPublicLivestock(filter,currentPage,mIsAuctionPlus);
        }
        else
        {
            return mRetrofitClient.getLivestock(currentPage,filter,requestInclude);
        }
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
                return mRetrofitClient.deleteLivestock(id);
            }
        }.asLiveData();
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final Boolean mIsForPublic;
        private final SearchFilter mSearchFilter;
        private final Boolean mIsAuctionPlus;


        public Factory(@NonNull Application application,SearchFilter searchFilter,Boolean isPublic,Boolean isAuctionPlus) {
            mApplication = application;
            mIsForPublic = isPublic;
            mSearchFilter = searchFilter;
            mIsAuctionPlus = isAuctionPlus;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MyAdPagerViewModel(mApplication, mSearchFilter,mIsForPublic, mIsAuctionPlus);
        }
    }

}
