package com.theherdonline.db.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.Agent;

@Dao
public interface AgentDao {
    @Query("SELECT * FROM tableAgent")
    List<Agent> loadAll();

    @Query("SELECT COUNT(*) FROM tableAgent")
    Integer count();

    @Query("SELECT * FROM tableAgent WHERE id = :nId LIMIT 1")
    Agent getItemById(Integer nId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Agent> agentList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertItem(Agent agent);

    @Query("DELETE FROM tableAgent")
    void deleteAll();
}
