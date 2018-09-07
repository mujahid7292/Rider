package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by HP on 3/4/2018.
 */


//Entity and table in sql are same
@Entity()
public class CustomerPickUpLatLng {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int rowNumber;

    @ColumnInfo(name = "Latitude")
    public double latitude;

    @ColumnInfo(name = "Longitude")
    public double longitude;

    public CustomerPickUpLatLng() {

    }

    @NonNull
    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(@NonNull int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
