package com.theherdonline.ui.marketReport;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.AppDatabase;
import com.theherdonline.db.entity.MarketReport;
import com.theherdonline.db.entity.ResPagerSaleyard;
import com.theherdonline.api.Resource;
import com.theherdonline.api.StaleData;
import com.theherdonline.ui.herdinterface.BackPressInterface;
import com.theherdonline.util.AppUtil;

public class CreateMarketReportViewModel extends ViewModel {

    DataRepository mDataRepository;
    AppUtil mAppUtil;
    AppDatabase mDatabase;



    final private LiveData<Resource<MarketReport>> mLiveDataPostMarketReport;
    final private MutableLiveData<MarketReport> mNewMarketReport = new MutableLiveData<>();

    final private LiveData<Resource<ResPagerSaleyard>> mLiveDataSaleyard;
    final private MutableLiveData<Integer> mLiveDataUserId = new MutableLiveData<>();



    @Inject
    public CreateMarketReportViewModel(DataRepository dataRepository, AppUtil appUtil, AppDatabase appDatabase) {
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
               return new StaleData<ResPagerSaleyard>();
           }
           else
           {
               return mDataRepository.getSaleyardForReport();
           }
        });
    }

    public void refreshSaleyard(Integer userId)
    {
        mLiveDataUserId.setValue(userId);
    }

    public LiveData<Resource<ResPagerSaleyard>> getmLiveDataSaleyard() {
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
