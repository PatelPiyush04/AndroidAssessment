package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.LivestockCategory;
import com.theherdonline.db.entity.SaleyardCategory;

@Dao
public interface SaleyardCategoryDao {


    @Query("SELECT * FROM tableSaleyardCategory")
    LiveData<List<SaleyardCategory>> loadAllLive();

    @Query("SELECT * FROM tableSaleyardCategory ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<SaleyardCategory> loadAll();


    @Query("SELECT * FROM tableSaleyardCategory WHERE id <> 0 ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<SaleyardCategory> loadAllExceptAll();



    @Query("SELECT * FROM tableSaleyardCategory WHERE id = :id")
    SaleyardCategory getItemById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<SaleyardCategory> saleyardCategoryList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(SaleyardCategory saleyardCategory);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableSaleyardCategory")
    void deleteAll();


}
