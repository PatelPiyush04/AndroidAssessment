package com.theherdonline.ui.main.myad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.EntitySaleyard;
import com.theherdonline.api.Resource;
import com.theherdonline.api.StaleData;
import com.theherdonline.util.AppUtil;

public class CreateLivestockViewModel extends ViewModel {

    DataRepository mDataRepository;
    AppUtil mAppUtil;
    AppDatabase mDatabase;



    final private LiveData<Resource<MarketReport>> mLiveDataPostMarketReport;
    final private MutableLiveData<MarketReport> mNewMarketReport = new MutableLiveData<>();

    final private LiveData<Resource<List<EntitySaleyard>>> mLiveDataSaleyard;
    final private MutableLiveData<Integer> mLiveDataUserId = new MutableLiveData<>();



    @Inject
    public CreateLivestockViewModel(DataRepository dataRepository, AppUtil appUtil, AppDatabase appDatabase) {
        mDataRepository = dataRepository;
        mAppUtil = appUtil;
        mDatabase = appDatabase;

        mLiveDataPostMarketReport = Transformations.switchMap(mNewMarketReport, (MarketReport marketReport)->{
            if (marketReport != null) {
                return mDataRepository.postMarketReport(marketReport);
            }
            else
            {
                return new StaleData<MarketReport>();
            }
        });

        mLiveDataSaleyard = Transformations.switchMap(mLiveDataUserId,(Integer userId) -> {
           if (userId == null)
           {
               return new StaleData<List<EntitySaleyard>>();
           }
           else
           {
               return mDataRepository.getSaleyard(userId,null,null);
           }
        });
    }

    public void refreshSaleyard(Integer userId)
    {
        mLiveDataUserId.setValue(userId);
    }

    public LiveData<Resource<List<EntitySaleyard>>> getmLiveDataSaleyard() {
        return mLiveDataSaleyard;
    }

    public void postMarketReport(MarketReport marketReport)
    {
        mNewMarketReport.setValue(marketReport);
    }

    public LiveData<Resource<MarketReport>> getmLiveDataPostMarketReport() {
        return mLiveDataPostMarketReport;
    }


}
