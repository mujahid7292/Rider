package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface NidBackDao {
    @Query("select * from NIDBACK where :driverUid = :driverUid")
    LiveData<NIDBACK> loadNIDBACK(String driverUid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNIDBACK(NIDBACK nidback);
}
