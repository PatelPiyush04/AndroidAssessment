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
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.repository.DataRepository;

import java.util.List;

import javax.inject.Inject;

public class NotificationPagerViewModel extends ViewModel  {

    private DataRepository repository;

    private MutableLiveData<Void> liveDataRefresh = new MutableLiveData();
    final private LiveData<Resource<List<HerdNotification>>> liveDataHerdNotificationList;


    @Inject
    public NotificationPagerViewModel(DataRepository repository) {
        this.repository = repository;
        liveDataHerdNotificationList = Transformations.switchMap(liveDataRefresh,liveDataRefresh->{
           return this.repository.getNotificationV2();
        });
    }


    void refresh()
    {
        liveDataRefresh.setValue(null);
    }

    public LiveData<Resource<List<HerdNotification>>> getLiveDataHerdNotificationList() {
        return liveDataHerdNotificationList;
    }
}
