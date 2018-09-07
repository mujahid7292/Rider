package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by HP on 3/6/2018.
 */

@Dao
public interface AssignedCustomerTripDetailsDao {

    @Query("select * from AssignedCustomerTripDetails")
    LiveData<List<AssignedCustomerTripDetails>> loadTripDetails();

    @Query("DELETE FROM AssignedCustomerTripDetails")
    void deleteEntireTableOfAssignedCustomerTripDetails();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAssignedCustomerTripDetails(AssignedCustomerTripDetails tripDetails);

}
