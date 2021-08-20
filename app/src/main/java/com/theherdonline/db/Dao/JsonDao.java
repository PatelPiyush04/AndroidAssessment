package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theherdonline.db.entity.HerdNotification;
import com.theherdonline.db.entity.JsonEntity;

import java.util.List;

@Dao
public interface JsonDao {
    @Query("SELECT * FROM tableJson WHERE id = :id")
    JsonEntity load(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(JsonEntity jsonEntity);

    @Query("DELETE FROM tableJson WHERE id = :id")
    void delete(Integer id);

    @Query("DELETE FROM tableJson")
    void deleteAll();

}
