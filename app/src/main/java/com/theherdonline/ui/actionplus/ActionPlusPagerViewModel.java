package com.theherdonline.ui.actionplus;

import android.app.Application;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.db.entity.EntityLivestock;
import com.theherdonline.db.entity.ResLivestock;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;

import java.util.List;

import retrofit2.Call;


public class ActionPlusPagerViewModel extends PagerViewModel<EntityLivestock, ResLivestock> {



    public ActionPlusPagerViewModel(@NonNull Application application) {
        super(application);
    }


    @Override
    public List<EntityLivestock> convertResponse2ItemList(ResLivestock resLivestock) {
        return resLivestock .getData();
    }

    @Override
    public Call<ResLivestock> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        return null;  // mRetrofitClient.getActionPlus(currentPage);
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
            return (T) new ActionPlusPagerViewModel(mApplication);
        }
    }

}
