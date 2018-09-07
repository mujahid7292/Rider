package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable;

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
public class RegPaper {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    private String driverUid;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] REGISTRATION_PAPER;

    public RegPaper() {

    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public byte[] getREGISTRATION_PAPER() {
        return REGISTRATION_PAPER;
    }

    public void setREGISTRATION_PAPER(byte[] REGISTRATION_PAPER) {
        this.REGISTRATION_PAPER = REGISTRATION_PAPER;
    }
}
