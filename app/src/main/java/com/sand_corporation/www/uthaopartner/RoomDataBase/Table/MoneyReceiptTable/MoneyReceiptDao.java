package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.Update;

import com.sand_corporation.www.uthaopartner.RoomDataBase.DateConverter;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.SubsetOfMoneyReceiptColumn.MoneyReceiptRecyclerView;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by HP on 2/16/2018.
 */

@Dao
@TypeConverters(DateConverter.class)
public interface MoneyReceiptDao {

    @Query("select * from MoneyReceipt where rideId = :rideId")
    LiveData<MoneyReceipt> loadMoneyReceiptByrideId(String rideId);

    @Query("SELECT * FROM MoneyReceipt " +
            "WHERE MoneyReceipt.Ride_End_Time > :from " +
            "AND MoneyReceipt.Ride_End_Time < :to "
    )
    LiveData<List<MoneyReceipt>> selectMoneyReceiptFromSpeceficDateToSpeceficeDate(Date from, Date to);

    @Query("SELECT * FROM MoneyReceipt WHERE Ride_End_Time BETWEEN :from AND :to")
    LiveData<List<MoneyReceipt>> loadAllMoneyReceiptBetweenDates(Long from, Long to);

    //Selective Column Query
    @Query("SELECT rideId, Ride_End_Time, PickUp_Location_Address, Destination_Location_Address," +
            "Total_Payment FROM MoneyReceipt")
    LiveData<List<MoneyReceiptRecyclerView>> loadDataForRecyclerView();

    @Insert(onConflict = REPLACE)
    void insertMoneyReceipt(MoneyReceipt moneyReceipt);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMoneyReceipt(MoneyReceipt moneyReceipt);

    @Delete
    void deleteMoneyReceipt(MoneyReceipt moneyReceipt);

}
