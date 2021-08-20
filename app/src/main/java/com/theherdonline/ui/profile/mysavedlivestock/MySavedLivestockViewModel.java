package com.theherdonline.ui.profile.mysavedlivestock;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.ResLivestock;
import com.theherdonline.di.MyApplication;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UserType;
import retrofit2.Call;


public class MySavedLivestockViewModel extends PagerViewModel<EntityLivestock, ResLivestock> {

    final Integer mUserId;
    final UserType mUserType;


    public MySavedLivestockViewModel(@NonNull Application application, SearchFilter searchFilter) {
        super(application);
        AppUtil appUtil = ((MyApplication)application).getmAppUtil();
        mUserId = appUtil.getUserId();
        mUserType = appUtil.getUserType();
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
        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_media,AppConstants.TAG_images);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<EntityLivestock> convertResponse2ItemList(ResLivestock resPagerLivestock) {
        return resPagerLivestock .getData();
    }

    @Override
    public Call<ResLivestock> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
            return mRetrofitClient.getLikedLivestock(currentPage,null,null);
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
            return (T) new MySavedLivestockViewModel(mApplication, mSearchFilter);
        }
    }

}
