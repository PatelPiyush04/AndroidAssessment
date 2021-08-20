package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.MarketReport;

@Dao
public interface MarketReportDao {


    @Query("SELECT * FROM tableMarketReport")
    LiveData<List<MarketReport>> loadAllLive();

    @Query("SELECT * FROM tableMarketReport")
    List<MarketReport> loadAll();


    @Query("SELECT * FROM tableMarketReport WHERE id = :id")
    MarketReport getMarketReportById(Integer id);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<MarketReport> marketReportList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(MarketReport marketReport);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableMarketReport")
    void deleteAll();

    /**
     * Delete other users.
     */
    @Query("DELETE FROM tableMarketReport WHERE id != :id")
    void deleteOthers(Integer id);
}
