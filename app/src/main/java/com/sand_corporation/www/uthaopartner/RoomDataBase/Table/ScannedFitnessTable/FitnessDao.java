package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface FitnessDao {
    @Query("select * from FITNESS where :driverUid = :driverUid")
    LiveData<FITNESS> loadFITNESS(String driverUid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertFITNESS(FITNESS fitness);
}
