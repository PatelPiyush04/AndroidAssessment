package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.MarketReport;

import java.util.List;

@Dao
public interface NotificationDao {

    @Query("SELECT * FROM tableNotification ORDER BY id DESC")
    LiveData<List<HerdNotification>> loadAllLive();

    @Query("SELECT * FROM tableNotification ORDER BY id DESC")
    List<HerdNotification> loadAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<HerdNotification> notificationList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(HerdNotification notificationList);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableNotification")
    void deleteAll();

}
