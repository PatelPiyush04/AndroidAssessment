package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.AnimalCategory;

@Dao
public interface AnimalDao {
    @Query("SELECT * FROM tableAnimalCategory")
    List<AnimalCategory> loadAll();

    @Query("SELECT * FROM tableAnimalCategory")
    LiveData<List<AnimalCategory>> loadLiveDataAll();

    @Query("SELECT id FROM tableAnimalCategory WHERE name = :name")
    Integer getAnimalIndex(String name);

    @Query("SELECT COUNT(*) FROM tableAnimalCategory")
    Integer count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AnimalCategory> breeds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(AnimalCategory breed);

    @Query("DELETE FROM tableAnimalCategory")
    void deleteAll();
}
