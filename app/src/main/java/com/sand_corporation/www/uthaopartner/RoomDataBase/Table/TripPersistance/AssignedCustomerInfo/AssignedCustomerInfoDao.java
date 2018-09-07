package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.OnTripPersistance.OnTripPersistence;

import java.util.List;

/**
 * Created by HP on 3/6/2018.
 */

@Dao
public interface AssignedCustomerInfoDao {

    @Query("select * from AssignedCustomerInfo")
    LiveData<List<AssignedCustomerInfo>> loadAssignedCustomerInfo();

    @Query("DELETE FROM AssignedCustomerInfo")
    void deleteEntireTableOfAssignedCustomerInfo();

//    @Query("DELETE FROM OnTripPersistence")
//    void deleteEntireTableOfOnTripPersistence();

    @Query("SELECT AssignedCustomerInfo.*, AssignedCustomerTripDetails.* " +
            "FROM AssignedCustomerInfo INNER JOIN AssignedCustomerTripDetails " +
            "ON AssignedCustomerInfo.infoRowNumber = AssignedCustomerTripDetails.tripDetailsRowNumber")
    LiveData<List<OnTripPersistence>> retrieveOnTripPersistence();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAssignedCustomerInfo(AssignedCustomerInfo assignedCustomerInfo);
}
