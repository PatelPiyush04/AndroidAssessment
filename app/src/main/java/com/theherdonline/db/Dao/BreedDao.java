package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.BreedCategory;

@Dao
public interface BreedDao {
    @Query("SELECT * FROM tableBreed")
    List<BreedCategory> loadAll();

    @Query("SELECT * FROM tableBreed")
    LiveData<List<BreedCategory>> loadLiveDataAll();

    @Query("SELECT id FROM tableAnimalCategory WHERE name = :name")
    Integer getBreedIndex(String name);


    @Query("SELECT COUNT(*) FROM tableBreed")
    Integer count();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BreedCategory> breedCategories);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(BreedCategory breed);

    @Query("DELETE FROM tableBreed")
    void deleteAll();
}
