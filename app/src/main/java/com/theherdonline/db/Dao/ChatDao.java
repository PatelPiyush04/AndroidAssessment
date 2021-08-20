package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.Chat;

public interface ChatDao {

    @Query("SELECT * FROM tableChat")
    LiveData<List<Chat>> loadAllLive();

    @Query("SELECT * FROM tableChat ORDER BY id ASC")
    List<Chat> loadAll();

    @Query("SELECT * FROM tableChat WHERE id = :id")
    Chat getItemById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Chat> liveChatList);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(Chat liveChatList);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableChat")
    void deleteAll();
    
}
