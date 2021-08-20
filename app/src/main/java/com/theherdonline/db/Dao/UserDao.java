package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.User;

@Dao
public interface UserDao {


    @Query("SELECT * FROM tableUserInfo")
    LiveData<List<User>> loadAllLive();

    @Query("SELECT * FROM tableUserInfo")
    List<User> loadAll();


    @Query("SELECT * FROM tableUserInfo WHERE agentId = :id")
    List<User> getProducerUsers(Integer id);


    @Query("SELECT * FROM tableUserInfo WHERE id = :id")
    User getUserById(Integer id);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> products);

    /**
     * Insert a user in the database. If the user already exists, replace it.
     *
     * @param user the user to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tableUserInfo")
    void deleteAll();

    /**
     * Delete other users.
     */
    @Query("DELETE FROM tableUserInfo WHERE id != :id")
    void deleteOthers(Integer id);

}
