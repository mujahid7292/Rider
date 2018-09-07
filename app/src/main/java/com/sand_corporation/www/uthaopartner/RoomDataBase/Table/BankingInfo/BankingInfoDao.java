package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BankingInfo;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface BankingInfoDao {
    @Insert(onConflict = REPLACE)
    long insertBankingInfo(BankingInfo bankingInfo);
}
