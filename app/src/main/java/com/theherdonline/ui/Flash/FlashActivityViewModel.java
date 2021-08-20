package com.theherdonline.ui.Flash;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import com.theherdonline.db.entity.ResVersion;
import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.entity.User;
import com.theherdonline.api.Resource;

public class FlashActivityViewModel extends ViewModel {

    final private DataRepository mDataRepository;
    //final private LiveData<Resource<List<LiveStock>>> mLiveDataLiveStock;

    final private LiveData<Resource<User>> mLiveDataIntiApplication;
    private MutableLiveData<Void> mLiveDataIntiApplicationTrigger = new MutableLiveData<>();


    final private LiveData<Resource<ResVersion>> mLvResVersion;
    private MutableLiveData<Void> mLvResVersionTrigger = new MutableLiveData();


    @Inject
    public FlashActivityViewModel(DataRepository dataRepository) {
        mDataRepository = dataRepository;
        //mLiveDataLiveStock = mDataRepository.refreshLiveStock();
        mLvResVersion = Transformations.switchMap(mLvResVersionTrigger,v-> mDataRepository.getVersion());
        mLiveDataIntiApplication = Transformations.switchMap(mLiveDataIntiApplicationTrigger, v-> mDataRepository.initApplication());
    }

    public void refreshVersion()
    {
        mLvResVersionTrigger.setValue(null);
    }

    public void refreshApplication()
    {
        mLiveDataIntiApplicationTrigger.setValue(null);
    }


    public LiveData<Resource<ResVersion>> getmLvResVersion() {
        return mLvResVersion;
    }

    public LiveData<Resource<User>> getmLiveDataIntiApplication() {
        return mLiveDataIntiApplication;
    }
}
