package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable;

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
public class DrivingLicense {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    private String driverUid;

    //ScannedDrivingLicenseTable
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] Driving_License;

    public DrivingLicense() {

    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public byte[] getDriving_License() {
        return Driving_License;
    }

    public void setDriving_License(byte[] driving_License) {
        Driving_License = driving_License;
    }
}
