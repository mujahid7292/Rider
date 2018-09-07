package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptEditedTable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;

/**
 * Created by HP on 2/28/2018.
 */

//Entity and table in sql are same
@Entity(indices = {
        @Index(value = {"rideId"})},
        foreignKeys = @ForeignKey(entity = MoneyReceipt.class,
                parentColumns = "rideId",
                childColumns = "rideId"))
public class MoneyReceiptEdited {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "rideId")
    public String rideId;

    @ColumnInfo(name = "Edited_Ride_Distance")
    private double editedRideDistance;

    @ColumnInfo(name = "Edited_Distance_Fair")
    private double editedDistanceFair;

    @ColumnInfo(name = "Edited_Total_Payment")
    private double EditedTotalPayment;

    @ColumnInfo(name = "Edited_Encoded_Path")
    private String editedEncodedPath;

    @ColumnInfo(name = "Discount_In_Percentage")
    private double discountInPercentage;

    @ColumnInfo(name = "Demand_Charge_Percentage")
    private double demandChargeInPercentage;

    public MoneyReceiptEdited() {

    }

    @NonNull
    public String getRideId() {
        return rideId;
    }

    public void setRideId(@NonNull String rideId) {
        this.rideId = rideId;
    }

    public double getDiscountInPercentage() {
        return discountInPercentage;
    }

    public void setDiscountInPercentage(double discountInPercentage) {
        this.discountInPercentage = discountInPercentage;
    }

    public double getDemandChargeInPercentage() {
        return demandChargeInPercentage;
    }

    public void setDemandChargeInPercentage(double demandChargeInPercentage) {
        this.demandChargeInPercentage = demandChargeInPercentage;
    }

    public String getEditedEncodedPath() {
        return editedEncodedPath;
    }

    public void setEditedEncodedPath(String editedEncodedPath) {
        this.editedEncodedPath = editedEncodedPath;
    }

    public double getEditedTotalPayment() {
        return EditedTotalPayment;
    }

    public void setEditedTotalPayment(double editedTotalPayment) {
        EditedTotalPayment = editedTotalPayment;
    }

    public double getEditedDistanceFair() {
        return editedDistanceFair;
    }

    public void setEditedDistanceFair(double editedDistanceFair) {
        this.editedDistanceFair = editedDistanceFair;
    }

    public double getEditedRideDistance() {
        return editedRideDistance;
    }

    public void setEditedRideDistance(double editedRideDistance) {
        this.editedRideDistance = editedRideDistance;
    }

}
