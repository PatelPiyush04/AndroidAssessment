package com.theherdonline.ui.notification.backup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.theherdonline.api.RequestCommaValue;
import com.theherdonline.app.AppConstants;
import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.ResNotification;
import com.theherdonline.ui.general.SearchFilter;
import com.theherdonline.ui.profile.PagerViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class NotificationPagerViewBackupModel extends PagerViewModel<HerdNotification, ResNotification> {

    public NotificationPagerViewBackupModel(@NonNull Application application) {
        super(application);


        RequestCommaValue requestInclude = new RequestCommaValue(AppConstants.TAG_streamable,
                AppConstants.TAG_user,
                AppConstants.TAG_streamable_media,
                AppConstants.TAG_streamable_images);
        mRepository.setRequestInclude(requestInclude);
    }


    @Override
    public List<HerdNotification> convertResponse2ItemList(ResNotification resStream) {
        List<HerdNotification> herdNotificationList = new ArrayList<>();
        for (int i = 0; i < 100 ; i++)
        {
            HerdNotification notification = new HerdNotification();
            notification.setTitle("title " + i);
            notification.setMessage("At Sprint Digital we create scalable custom software that grows with your business. From delivering enterprise-grade technical solutions to executing result-driven digital marketing strategies, we ensure that we deliver innovative solutions that will grow your business. " + i);
            notification.setCreateTime("create time: " );
            notification.setUrl("https://sprintdigital.com.au/");
            herdNotificationList.add(notification);
        }

        //return resStream.getData();
        return herdNotificationList;
    }

    @Override
    public Call<ResNotification> requestCall(Integer currentPage, SearchFilter filter, RequestCommaValue requestInclude) {
        return mRetrofitClient.getNotifications(currentPage,null);
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
            return (T) new NotificationPagerViewBackupModel(mApplication);
        }
    }

}
