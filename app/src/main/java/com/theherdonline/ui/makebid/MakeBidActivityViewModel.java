package com.theherdonline.ui.makebid;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import com.theherdonline.repository.DataRepository;
import com.theherdonline.db.entity.EntityLivestock;


public class MakeBidActivityViewModel extends ViewModel {

    final private DataRepository mDataRepository;
    private MutableLiveData<EntityLivestock> mLiveDataLivestock = new MutableLiveData<>();
  //  final private LiveData<Resource<ResMakeBid>> mLvResMakeBid;
    final private MutableLiveData<String> mLvResMakeBidTrigger = new MutableLiveData<>();

    private MutableLiveData<Integer> mLvPrice = new MutableLiveData<>();

    @Inject
    public MakeBidActivityViewModel(DataRepository dataRepository) {
        mDataRepository = dataRepository;
        /*mLvResMakeBid = Transformations.switchMap(mLvResMakeBidTrigger,(String price) ->{
            Integer id = mLiveDataLivestock.getValue().getId();
            return mDataRepository.makeBid(id, mLvPrice.getValue());
        });*/
    }


    public void setLivestock(EntityLivestock livestock) {
        this.mLiveDataLivestock.setValue(livestock);
    }

    public MutableLiveData<EntityLivestock> getmLiveDataLivestock() {
        return mLiveDataLivestock;
    }

    public void setPrice(Integer price)
    {
        mLvPrice.setValue(price);

    }

    public void makeBid(Integer price)
    {
        setPrice(price);
       // mLvResMakeBidTrigger.setValue("start");
    }


   // public LiveData<Resource<ResMakeBid>> getmLvResMakeBit() {
    //    return mLvResMakeBid;
  //  }

    public MutableLiveData<Integer> getmLvPrice() {
        return mLvPrice;
    }
}