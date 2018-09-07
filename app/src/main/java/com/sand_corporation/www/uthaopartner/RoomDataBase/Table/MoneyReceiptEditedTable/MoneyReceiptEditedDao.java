package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptEditedTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by HP on 2/28/2018.
 */

@Dao
public interface MoneyReceiptEditedDao {

    @Query("select * from MoneyReceiptEdited where rideId = :rideId")
    LiveData<MoneyReceiptEdited> loadMoneyReceiptEditedByrideId(String rideId);

    @Insert(onConflict = REPLACE)
    void insertMoneyReceiptEdited(MoneyReceiptEdited moneyReceiptEdited);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMoneyReceiptEdited(MoneyReceiptEdited moneyReceiptEdited);
}
