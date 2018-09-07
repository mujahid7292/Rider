package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable;

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
public class NIDFRONT {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    private String driverUid;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] NID_FRONT;

    public NIDFRONT() {

    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public byte[] getNID_FRONT() {
        return NID_FRONT;
    }

    public void setNID_FRONT(byte[] NID_FRONT) {
        this.NID_FRONT = NID_FRONT;
    }
}
