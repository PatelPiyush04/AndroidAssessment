package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.LivestockSubCategory;

@Dao
public interface LivestockSubCategoryDao {


    @Query("SELECT * FROM tableSubCategory")
    LiveData<List<LivestockSubCategory>> loadAllLive();

    @Query("SELECT * FROM tableSubCategory ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<LivestockSubCategory> loadAll();

    @Query("SELECT * FROM tableSubCategory WHERE id <> 0 ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<LivestockSubCategory> loadAllExceptAll();

    @Query("SELECT * FROM tableSubCategory WHERE livestock_category_id = :id ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC ")
    List<LivestockSubCategory> loadSpecificArray(Integer id);

    @Query("SELECT * FROM tableSubCategory WHERE livestock_category_id = :id OR id == 0  ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<LivestockSubCategory> loadSpecificArrayExtend(Integer id);

    @Query("SELECT * FROM tableSubCategory WHERE id = :id")
    LivestockSubCategory getItemById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LivestockSubCategory> livestockCategoryList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(LivestockSubCategory livestockCategory);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableSubCategory")
    void deleteAll();

}
