package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.SubsetOfBasicInfo;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by HP on 2/17/2018.
 */

public class NavigationMenuSubSet {

    public String full_name;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    public byte[] Profile_Pic;

    public NavigationMenuSubSet() {

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public byte[] getProfile_Pic() {
        return Profile_Pic;
    }

    public void setProfile_Pic(byte[] profile_Pic) {
        Profile_Pic = profile_Pic;
    }
}
