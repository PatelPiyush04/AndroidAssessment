package com.theherdonline.di;

import android.os.AsyncTask;

import javax.inject.Inject;

import com.theherdonline.app.AppExecutors;
import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.AnimalCategory;
import com.theherdonline.db.entity.BreedCategory;
import com.theherdonline.db.entity.LivestockStatus;
import com.theherdonline.api.RetrofitClient;
import com.theherdonline.util.AppUtil;
import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import io.branch.referral.Branch;

public class MyApplication extends DaggerApplication {

    @Inject
    AppDatabase mAppDatabase;

    @Inject
    AppExecutors mAppExecutors;

    @Inject
    RetrofitClient mRetrofitClient;

    @Inject
    AppUtil mAppUtil;

    @Inject
    DataRepository mDataRepository;

    public AppDatabase getmAppDatabase() {
        return mAppDatabase;
    }

    public void setmAppDatabase(AppDatabase mAppDatabase) {
        this.mAppDatabase = mAppDatabase;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerMyAppComponent.builder().application(this).build();
    }


    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the Branch object
        Branch.getAutoInstance(this);

        // for test
        /*if (mAppDatabase != null) {
            for (int i = 0; i < 10; i++) {
                BreedCategory b = new BreedCategory();
                b.setId(i + 1);
                b.setName("Breed_" + i);
                mAppDatabase.breedDao().insertItem(b);
            }
            for (int i = 0; i < 10; i++) {
                AnimalCategory animalCategory = new AnimalCategory();
                animalCategory.setId(i + 1);
                animalCategory.setName("Animal_" + i);
                mAppDatabase.animalDao().insertItem(animalCategory);
            }
            for (int i = 0; i < 10; i++) {
                LivestockStatus animalCategory = new LivestockStatus();
                animalCategory.setId(i + 1);
                animalCategory.setName("LivestockStatus_" + i);
                mAppDatabase.livestockStatusDao().insertItem(animalCategory);
            }
        }*/




}



    public AppExecutors getmAppExecutors() {
        return mAppExecutors;
    }

    public RetrofitClient getmRetrofitClient() {
        return mRetrofitClient;
    }

    public DataRepository getmDataRepository() {
        return mDataRepository;
    }

    public AppUtil getmAppUtil() {
        return mAppUtil;
    }


}
