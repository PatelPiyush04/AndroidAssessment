package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.LivestockCategory;

@Dao
public interface LivestockCategoryDao {


    @Query("SELECT * FROM tableCategory")
    LiveData<List<LivestockCategory>> loadAllLive();

    @Query("SELECT * FROM tableCategory ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<LivestockCategory> loadAll();

    @Query("SELECT * FROM tableCategory WHERE id <> 0 ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<LivestockCategory> loadAllExceptAll();

    @Query("SELECT * FROM tableCategory WHERE id = :id")
    LivestockCategory getItemById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LivestockCategory> livestockCategoryList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(LivestockCategory livestockCategory);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableCategory")
    void deleteAll();


}
