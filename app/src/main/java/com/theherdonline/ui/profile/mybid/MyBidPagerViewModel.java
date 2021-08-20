package com.theherdonline.ui.profile.mybid;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import java.util.List;

import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.Bid;
import com.theherdonline.db.entity.ResBids;
import com.theherdonline.di.MyApplication;
import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;
import com.theherdonline.util.AppUtil;
import com.theherdonline.util.UserType;
import retrofit2.Call;


public class MyBidPagerViewModel extends PagerViewModel<Bid, ResBids> {

    final Integer mUserId;
    final UserType mUserType;

    public MyBidPagerViewModel(@NonNull Application application) {
        super(application);
        AppUtil appUtil = ((MyApplication)application).getmAppUtil();
        mUserId = appUtil.getUserId();
        mUserType = appUtil.getUserType();
        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_biddable);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<Bid> convertResponse2ItemList(ResBids resLivestock) {
        return resLivestock .getData();
    }

    @Override
    public Call<ResBids> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        return mRetrofitClient.getBidList(currentPage,mUserId);
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
            return (T) new MyBidPagerViewModel(mApplication);
        }
    }

}
