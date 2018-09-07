package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by HP on 3/4/2018.
 */

@Dao
public interface CustomerPickUpLatLngDao {

    @Query("select * from CustomerPickUpLatLng")
    LiveData<List<CustomerPickUpLatLng>> loadCustomerPickUpLatLng();

    @Query("DELETE FROM CustomerPickUpLatLng")
    void deleteEntireTableOfCustomerPickUpLatLng();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCustomerPickUpLatLng(CustomerPickUpLatLng customerPickUpLatLng);
}
