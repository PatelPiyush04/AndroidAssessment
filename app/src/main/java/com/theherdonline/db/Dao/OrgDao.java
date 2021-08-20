package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.theherdonline.db.entity.Organisation;

import java.util.List;
@Dao
public interface OrgDao {

    @Query("SELECT * FROM tableOrganisation WHERE id = :id")
    Organisation getOrganisationById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Organisation> products);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrg(Organisation organisation);

    @Query("DELETE FROM tableOrganisation")
    void deleteAll();

}
