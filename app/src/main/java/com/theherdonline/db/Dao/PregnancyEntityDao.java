package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.PregnancyEntity;

@Dao
public interface PregnancyEntityDao {
    @Query("SELECT * FROM tablePregnancy")
    LiveData<List<PregnancyEntity>> loadAllLive();

    @Query("SELECT * FROM tablePregnancy ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<PregnancyEntity> loadAll();

    @Query("SELECT * FROM tablePregnancy WHERE id <> 0 ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<PregnancyEntity> loadAllExceptAll();

    @Query("SELECT * FROM tablePregnancy WHERE id == 0 OR livestock_category_id = :cateID ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<PregnancyEntity> loadAllByCategory(Integer cateID);

    @Query("SELECT * FROM tablePregnancy WHERE id <> 0 AND livestock_category_id = :cateID ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<PregnancyEntity> loadAllByCategoryExceptAll(Integer cateID);


    @Query("SELECT 1 FROM tablePregnancy WHERE livestock_category_id = :cateID LIMIT 1")
    Integer isExisted(Integer cateID);


    @Query("SELECT * FROM tablePregnancy WHERE id = :id")
    PregnancyEntity getItemById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PregnancyEntity> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PregnancyEntity entity);
    /**
     * Delete all users.
     */
    @Query("DELETE FROM tablePregnancy")
    void deleteAll();

}
