package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BankingInfo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;

/**
 * Created by HP on 2/27/2018.
 */

//Entity and table in sql are same
@Entity(foreignKeys = @ForeignKey(entity = BasicInfo.class,
        parentColumns = "DriverUID",
        childColumns = "DriverUID"),
        indices = {@Index("DriverUID")})
public class BankingInfo {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    private String driverUid;

    private String BKash_Number;

    public BankingInfo() {

    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public String getBKash_Number() {
        return BKash_Number;
    }

    public void setBKash_Number(String BKash_Number) {
        this.BKash_Number = BKash_Number;
    }
}
