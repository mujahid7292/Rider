package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface DrivingLicenseDao {
    @Query("select * from DrivingLicense where :driverUid = :driverUid")
    LiveData<DrivingLicense> loadDrivingLicense(String driverUid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertDrivingLicense(DrivingLicense drivingLicense);
}
