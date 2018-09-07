package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface TaxTokenDao {
    @Query("select * from TaxToken where :driverUid = :driverUid")
    LiveData<TaxToken> loadTaxToken(String driverUid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTaxToken(TaxToken taxToken);
}
