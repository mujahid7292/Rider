package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.SubsetOfMoneyReceiptColumn;

import android.arch.persistence.room.ColumnInfo;

/**
 * Created by HP on 2/16/2018.
 */

public class MoneyReceiptRecyclerView {
    @ColumnInfo(name = "rideId")
    public String rideId;

    @ColumnInfo(name = "Ride_End_Time")
    public long rideEndTime;

    @ColumnInfo(name = "PickUp_Location_Address")
    private String  pickUpLocationAddress;

    @ColumnInfo(name = "Destination_Location_Address")
    private String destinationLocationAddress;

    @ColumnInfo(name = "Total_Payment")
    private double totalPayment;

    public MoneyReceiptRecyclerView() {

    }

    public MoneyReceiptRecyclerView(String rideId, long rideEndTime, String pickUpLocationAddress,
                                    String destinationLocationAddress, double totalPayment) {
        this.rideId = rideId;
        this.rideEndTime = rideEndTime;
        this.pickUpLocationAddress = pickUpLocationAddress;
        this.destinationLocationAddress = destinationLocationAddress;
        this.totalPayment = totalPayment;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public long getRideEndTime() {
        return rideEndTime;
    }

    public void setRideEndTime(long rideEndTime) {
        this.rideEndTime = rideEndTime;
    }

    public String getPickUpLocationAddress() {
        return pickUpLocationAddress;
    }

    public void setPickUpLocationAddress(String pickUpLocationAddress) {
        this.pickUpLocationAddress = pickUpLocationAddress;
    }

    public String getDestinationLocationAddress() {
        return destinationLocationAddress;
    }

    public void setDestinationLocationAddress(String destinationLocationAddress) {
        this.destinationLocationAddress = destinationLocationAddress;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }
}
