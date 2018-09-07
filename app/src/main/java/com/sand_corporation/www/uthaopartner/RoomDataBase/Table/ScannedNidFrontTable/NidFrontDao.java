package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface NidFrontDao {
    @Query("select * from NIDFRONT where :driverUid = :driverUid")
    LiveData<NIDFRONT> loadNIDFRONT(String driverUid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertNIDFRONT(NIDFRONT nidfront);
}
