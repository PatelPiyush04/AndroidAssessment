package com.theherdonline.db.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import com.theherdonline.db.entity.PaymentCard;

@Dao
public interface PaymentCardDao {


    @Query("SELECT * FROM tablePaymentCard")
    LiveData<List<PaymentCard>> loadAllLive();

    @Query("SELECT * FROM tablePaymentCard")
    List<PaymentCard> loadAll();


    @Query("SELECT * FROM tablePaymentCard WHERE id = :id")
    PaymentCard getUserById(Integer id);



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PaymentCard> paymentCards);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PaymentCard paymentCards);

    /**
     * Delete all users.
     */
    @Query("DELETE FROM tablePaymentCard")
    void deleteAll();

    /**
     * Delete other users.
     */
    @Query("DELETE FROM tablePaymentCard WHERE id != :id")
    void deleteOthers(Integer id);

}
