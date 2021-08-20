package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.EntityLivestock;

@Dao
public interface LivestockDao {

    @Query("SELECT * FROM EntityLivestock")
    List<EntityLivestock> loadAll();

    @Query("SELECT * FROM EntityLivestock WHERE agentId = :nId")
    LiveData<List<EntityLivestock>> loadLiveDataAllAgent(Integer nId);

    @Query("SELECT * FROM EntityLivestock WHERE producerId = :nId")
    LiveData<List<EntityLivestock>> loadLiveDataAllProducer(Integer nId);

    @Query("SELECT COUNT(*) FROM EntityLivestock")
    Integer count();

    @Query("SELECT * FROM EntityLivestock WHERE id = :nId LIMIT 1")
    EntityLivestock getItemById(Integer nId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<EntityLivestock> advertisements);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(EntityLivestock advertisement);

    @Query("DELETE FROM EntityLivestock")
    void deleteAll();
}
