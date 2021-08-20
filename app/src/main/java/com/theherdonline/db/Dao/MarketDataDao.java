package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.MarketData;

@Dao
public interface MarketDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUsers(MarketData... marketData);

    @Query("SELECT * FROM tableMarket")
    LiveData<List<MarketData>> loadAllLive();

    @Query("SELECT * FROM tableMarket")
    List<MarketData> loadAll();


}
