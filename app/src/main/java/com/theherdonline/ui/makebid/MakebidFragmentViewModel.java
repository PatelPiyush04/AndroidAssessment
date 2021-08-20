package com.theherdonline.ui.makebid;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.di.MyApplication;

public class MakebidFragmentViewModel extends AndroidViewModel {

    final private DataRepository mRepository;

    public MakebidFragmentViewModel(@NonNull Application application, DataRepository repository) {
        super(application);
        mRepository = repository;
    }




    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final Integer mUserId;

        private final DataRepository mDataRepository;

        public Factory(@NonNull Application application, Integer userID) {
            mApplication = application;
            mUserId = userID;
            mDataRepository = ((MyApplication)application).getmDataRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MakebidFragmentViewModel(mApplication, mDataRepository);
        }
    }



}
