package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.SubsetOfBasicInfo.NavigationMenuSubSet;

import java.util.List;

/**
 * Created by HP on 2/17/2018.
 */


@Dao
public interface BasicInfoDao {

    @Query("SELECT * FROM BasicInfo")
    LiveData<List<BasicInfo>> getAllTheBasicInfo();

    @Query("select * from BasicInfo where driverUid = :driverUid")
    LiveData<BasicInfo> loadBasicInfo(String driverUid);

    //Selective Column Query
//    @Query("SELECT full_name, image FROM BasicInfo")
//    LiveData<NavigationMenuSubSet> loadDataForNavigationMenu();

    @Query("SELECT basicInfo.full_name AS full_name, profilePic.Profile_Pic AS Profile_Pic "
            + "FROM basicInfo, profilePic "
            + "WHERE basicInfo.DriverUID = profilePic.DriverUID")
    LiveData<NavigationMenuSubSet> loadDataForNavigationMenu();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertBasicInfo(BasicInfo basicInfo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateBasicInfo(BasicInfo basicInfo);

    @Delete
    void deleteAllDataFromBasicInfo(BasicInfo basicInfo);
}
