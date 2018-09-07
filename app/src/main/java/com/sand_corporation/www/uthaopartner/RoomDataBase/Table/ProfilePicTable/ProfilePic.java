package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable;

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
public class ProfilePic {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "DriverUID")
    public String driverUid;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] Profile_Pic;

    public ProfilePic() {

    }

    @NonNull
    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(@NonNull String driverUid) {
        this.driverUid = driverUid;
    }

    public byte[] getProfile_Pic() {
        return Profile_Pic;
    }

    public void setProfile_Pic(byte[] profile_Pic) {
        Profile_Pic = profile_Pic;
    }
}
