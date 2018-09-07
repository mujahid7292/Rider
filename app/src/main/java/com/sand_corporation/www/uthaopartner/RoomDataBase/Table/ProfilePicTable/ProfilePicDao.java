package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * Created by HP on 2/27/2018.
 */

@Dao
public interface ProfilePicDao {

    @Query("select * from ProfilePic where :driverUID = :driverUID")
    LiveData<ProfilePic> loadProfilePic(String driverUID);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertProfilePic(ProfilePic profilePic);
}
