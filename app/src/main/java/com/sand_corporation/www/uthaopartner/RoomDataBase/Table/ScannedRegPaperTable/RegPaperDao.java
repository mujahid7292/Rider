package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface RegPaperDao {
    @Query("select * from RegPaper where :driverUid = :driverUid")
    LiveData<RegPaper> loadRegPaper(String driverUid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertRegPaper(RegPaper regPaper);
}
