package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.LivestockStatus;

@Dao
public interface LivestockStatusDao {
    @Query("SELECT * FROM tableLivestockStatus")
    List<LivestockStatus> loadAll();

    @Query("SELECT * FROM tableLivestockStatus")
    LiveData<List<LivestockStatus>> loadLiveDataAll();

    @Query("SELECT COUNT(*) FROM tableLivestockStatus")
    Integer count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LivestockStatus> livestockStatuses);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(LivestockStatus livestockStatus);

    @Query("DELETE FROM tableLivestockStatus")
    void deleteAll();
}
