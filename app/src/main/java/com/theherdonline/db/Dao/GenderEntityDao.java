package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.GenderEntity;

@Dao
public interface GenderEntityDao {
    @Query("SELECT * FROM tableGender")
    LiveData<List<GenderEntity>> loadAllLive();

    @Query("SELECT * FROM tableGender ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<GenderEntity> loadAll();

    @Query("SELECT * FROM tableGender WHERE id <> 0 ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<GenderEntity> loadAllExceptAll();

    @Query("SELECT * FROM tableGender WHERE id == 0 OR livestock_category_id = :cateID ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<GenderEntity> loadAllByCategory(Integer cateID);

    @Query("SELECT * FROM tableGender WHERE id <> 0 AND livestock_category_id = :cateID ORDER BY CASE WHEN id == 0 THEN 0 ELSE 1 END, name ASC")
    List<GenderEntity> loadAllByCategoryExceptAll(Integer cateID);


    @Query("SELECT 1 FROM tablePregnancy WHERE livestock_category_id = :cateID LIMIT 1")
    Integer isExisted(Integer cateID);


    @Query("SELECT * FROM tableGender WHERE id = :id")
    GenderEntity getItemById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<GenderEntity> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(GenderEntity entity);
    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableGender")
    void deleteAll();

}
