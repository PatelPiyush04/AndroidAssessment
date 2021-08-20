package com.theherdonline.ui.main.myad;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.di.MyApplication;

public class MyAdFragmentViewModel extends AndroidViewModel {

    final private DataRepository mRepository;
    /*final private LiveData<Resource<User>> mLiveDataUser;
    final private MutableLiveData<Integer> mLiveDataUserId = new MutableLiveData<>();
*/


    public MyAdFragmentViewModel(@NonNull Application application, DataRepository repository) {
        super(application);
        mRepository = repository;
        /*mLiveDataUser = Transformations.switchMap(mLiveDataUserId, (Integer id)->{
           return repository.getUserById(id);
        });*/
    }


    public void refreshUserProfile(Integer userId)
    {
        //mLiveDataUserId.setValue(userId);
    }

    //public LiveData<Resource<User>> getmLiveDataUser() {
    //    return mLiveDataUser;
   // }

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
            return (T) new MyAdFragmentViewModel(mApplication, mDataRepository);
        }
    }



}
