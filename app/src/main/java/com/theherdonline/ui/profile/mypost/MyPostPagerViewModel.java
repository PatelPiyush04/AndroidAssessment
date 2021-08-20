package com.theherdonline.ui.profile.mypost;

import android.app.Application;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import com.theherdonline.app.AppConstants;
import com.theherdonline.app.AppExecutors;
import com.theherdonline.db.entity.Post;
import com.theherdonline.db.entity.ResPost;
import com.theherdonline.di.MyApplication;
import com.theherdonline.api.NetworkBoundRemoteData;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.api.Resource;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;
import com.theherdonline.util.AppUtil;
import retrofit2.Call;


public class MyPostPagerViewModel extends PagerViewModel<Post, ResPost> {

    final Integer mUserId;

    AppExecutors mAppExecutors;

    public MyPostPagerViewModel(@NonNull Application application) {
        super(application);
        AppUtil appUtil = ((MyApplication)application).getmAppUtil();
        mAppExecutors = ((MyApplication)application).getmAppExecutors();
        mUserId = appUtil.getUserId();
        SearchFilter filter = new SearchFilter();
        filter.setUser_id(mUserId);
        mRepository.setFilter(filter);
        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_user);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<Post> convertResponse2ItemList(ResPost resLivestock) {
        return resLivestock .getData();
    }

    @Override
    public Call<ResPost> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        return mRetrofitClient.getPost(currentPage,filter,requestInclude);
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        public Factory(@NonNull Application application) {
            mApplication = application;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MyPostPagerViewModel(mApplication);
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
                return mRetrofitClient.deletePost(id);
            }
        }.asLiveData();
    }
}
