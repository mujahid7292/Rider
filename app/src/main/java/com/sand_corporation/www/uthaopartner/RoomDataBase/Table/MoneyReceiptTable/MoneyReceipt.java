package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;

/**
 * Created by HP on 2/16/2018.
 */
//Entity and table in sql are same
@Entity (indices = {
        @Index("rideId"), @Index(value = {"rideId","Ride_End_Time"}),
        @Index("DriverUID")},
        foreignKeys = @ForeignKey(entity = BasicInfo.class,
                                  parentColumns = "DriverUID",
                                  childColumns = "DriverUID"))
//Above we have created index for rideId & rideEndTime column. As we will
//create query using this two variable, so we are indexing it, so that our
//search result return fast
public class MoneyReceipt {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "rideId")
    public String rideId;

    @ColumnInfo(name = "Ride_End_Time")
    public long rideEndTime;

    @ColumnInfo(name = "Ride_Start_Time")
    private long rideStartTime;

    @ColumnInfo(name = "Ride_Accept_Time")
    private long rideAcceptTime;

    @ColumnInfo(name = "Customer_Drop_Time")
    private long customerDropTime;

    @ColumnInfo(name = "DriverUID")
    private String driverUid;

    @ColumnInfo(name = "Driver_Name")
    private String driverName;

    @ColumnInfo(name = "CustomerUID")
    private String customerUid;

    @ColumnInfo(name = "Customer_Name")
    private String customerName;

    @ColumnInfo(name = "Customer_Profile_Pic")
    private String  customerProfilePic;

    @ColumnInfo(name = "Payment_Method_Used")
    private String paymentMethod;

    @ColumnInfo(name = "PickUp_Location_Address")
    private String pickUpLocationAddress;

    @ColumnInfo(name = "Destination_Location_Address")
    private String destinationLocationAddress;

    @ColumnInfo(name = "Encoded_Path")
    private String encodedPath;

    @ColumnInfo(name = "Customer_Rating_By_Driver")
    private int customerRatingByDriver;

    @ColumnInfo(name = "Driver_Rating_By_Customer")
    private int driverRatingByCustomer;

    @ColumnInfo(name = "Distance_Fair")
    private double distanceFair;

    @ColumnInfo(name = "Waiting_Time_Fair")
    private double waitingTimeFair;

    @ColumnInfo(name = "Total_Payment")
    private double totalPayment;

    @ColumnInfo(name = "Customer_Drop_Distance")
    private double customerDropDistance;

    @ColumnInfo(name = "Customer_PickUp_Distance")
    private double customerPickUpDistance;

    @ColumnInfo(name = "Discount_In_Percentage")
    private double discountInPercentage;

    @ColumnInfo(name = "Discount_Amount_Upto")
    private double discountAmountUptoTaka;

    @ColumnInfo(name = "Demand_Charge_Percentage")
    private double demandChargeInPercentage;

    @ColumnInfo(name = "Service_Type")
    private String serviceType;

    public long getRideAcceptTime() {
        return rideAcceptTime;
    }

    public void setRideAcceptTime(long rideAcceptTime) {
        this.rideAcceptTime = rideAcceptTime;
    }

    public long getCustomerDropTime() {
        return customerDropTime;
    }

    public void setCustomerDropTime(long customerDropTime) {
        this.customerDropTime = customerDropTime;
    }


    public double getCustomerDropDistance() {
        return customerDropDistance;
    }

    public void setCustomerDropDistance(double customerDropDistance) {
        this.customerDropDistance = customerDropDistance;
    }

    public double getCustomerPickUpDistance() {
        return customerPickUpDistance;
    }

    public void setCustomerPickUpDistance(double customerPickUpDistance) {
        this.customerPickUpDistance = customerPickUpDistance;
    }

    public double getDiscountAmountUptoTaka() {
        return discountAmountUptoTaka;
    }

    public void setDiscountAmountUptoTaka(double discountAmountUptoTaka) {
        this.discountAmountUptoTaka = discountAmountUptoTaka;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getCustomerProfilePic() {
        return customerProfilePic;
    }

    public void setCustomerProfilePic(String customerProfilePic) {
        this.customerProfilePic = customerProfilePic;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public MoneyReceipt() {

    }

    @NonNull
    public String getRideId() {
        return rideId;
    }

    public void setRideId(@NonNull String rideId) {
        this.rideId = rideId;
    }

    public String getCustomerUid() {
        return customerUid;
    }

    public void setCustomerUid(String customerUid) {
        this.customerUid = customerUid;
    }

    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(String driverUid) {
        this.driverUid = driverUid;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public int getCustomerRatingByDriver() {
        return customerRatingByDriver;
    }

    public void setCustomerRatingByDriver(int customerRatingByDriver) {
        this.customerRatingByDriver = customerRatingByDriver;
    }

    public int getDriverRatingByCustomer() {
        return driverRatingByCustomer;
    }

    public void setDriverRatingByCustomer(int driverRatingByCustomer) {
        this.driverRatingByCustomer = driverRatingByCustomer;
    }

    public long getRideStartTime() {
        return rideStartTime;
    }

    public void setRideStartTime(long rideStartTime) {
        this.rideStartTime = rideStartTime;
    }

    public long getRideEndTime() {
        return rideEndTime;
    }

    public void setRideEndTime(long rideEndTime) {
        this.rideEndTime = rideEndTime;
    }

    public double getDistanceFair() {
        return distanceFair;
    }

    public void setDistanceFair(double distanceFair) {
        this.distanceFair = distanceFair;
    }

    public double getWaitingTimeFair() {
        return waitingTimeFair;
    }

    public void setWaitingTimeFair(double waitingTimeFair) {
        this.waitingTimeFair = waitingTimeFair;
    }

    public double getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

}
