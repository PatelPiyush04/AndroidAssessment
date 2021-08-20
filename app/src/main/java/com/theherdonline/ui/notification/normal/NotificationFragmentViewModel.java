package com.theherdonline.ui.notification.normal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.api.Resource;
import com.theherdonline.db.entity.User;
import com.theherdonline.di.MyApplication;
import com.theherdonline.repository.DataRepository;

public class NotificationFragmentViewModel extends AndroidViewModel {

    final private DataRepository mRepository;
    final private LiveData<Resource<User>> mLiveDataUser;
    final private MutableLiveData<Integer> mLiveDataUserId = new MutableLiveData<>();



    public NotificationFragmentViewModel(@NonNull Application application, DataRepository repository) {
        super(application);
        mRepository = repository;
        mLiveDataUser = Transformations.switchMap(mLiveDataUserId, (Integer id)->{
           return repository.getUserById(id);
        });
    }


    public void refreshUserProfile(Integer userId)
    {
        mLiveDataUserId.setValue(userId);
    }

    public LiveData<Resource<User>> getmLiveDataUser() {
        return mLiveDataUser;
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
            return (T) new NotificationFragmentViewModel(mApplication, mDataRepository);
        }
    }



}
