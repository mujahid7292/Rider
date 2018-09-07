package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable;

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
public class FITNESS {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    private String driverUid;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] FITNESS_CERTIFICATE;

    public FITNESS() {

    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public byte[] getFITNESS_CERTIFICATE() {
        return FITNESS_CERTIFICATE;
    }

    public void setFITNESS_CERTIFICATE(byte[] FITNESS_CERTIFICATE) {
        this.FITNESS_CERTIFICATE = FITNESS_CERTIFICATE;
    }
}
