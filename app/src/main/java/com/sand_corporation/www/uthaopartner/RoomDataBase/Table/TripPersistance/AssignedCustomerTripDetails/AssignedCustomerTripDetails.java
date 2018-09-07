package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo.AssignedCustomerInfo;

/**
 * Created by HP on 3/6/2018.
 */

//Entity and table in sql are same
@Entity (indices = {
        @Index("tripDetailsRowNumber")},
        foreignKeys = @ForeignKey(entity = AssignedCustomerInfo.class,
                parentColumns = "infoRowNumber",
                childColumns = "tripDetailsRowNumber",
                onDelete = ForeignKey.CASCADE))
public class AssignedCustomerTripDetails {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "tripDetailsRowNumber")
    public int tripDetailsRowNumber;

    @ColumnInfo(name = "CustomerDestinationAddress")
    private String customerDestinationAddress;

    @ColumnInfo(name = "CustomerPickUpAddress")
    private String customerPickUpAddress;

    @ColumnInfo(name = "CustomerDestinationLat")
    private double customerDestinationLat;

    @ColumnInfo(name = "CustomerDestinationLng")
    private double customerDestinationLng;

    @ColumnInfo(name = "CustomerPickUpLat")
    private double customerPickUpLat;

    @ColumnInfo(name = "CustomerPickUpLng")
    private double customerPickUpLng;

    @ColumnInfo(name = "Promo_Code")
    private String promoCode;

    @ColumnInfo(name = "Surcharge_Amount")
    private double surchargeAmount;

    public AssignedCustomerTripDetails() {

    }

    @NonNull
    public int getTripDetailsRowNumber() {
        return tripDetailsRowNumber;
    }

    public void setTripDetailsRowNumber(@NonNull int tripDetailsRowNumber) {
        this.tripDetailsRowNumber = tripDetailsRowNumber;
    }

    public String getCustomerDestinationAddress() {
        return customerDestinationAddress;
    }

    public void setCustomerDestinationAddress(String customerDestinationAddress) {
        this.customerDestinationAddress = customerDestinationAddress;
    }

    public String getCustomerPickUpAddress() {
        return customerPickUpAddress;
    }

    public void setCustomerPickUpAddress(String customerPickUpAddress) {
        this.customerPickUpAddress = customerPickUpAddress;
    }

    public double getCustomerDestinationLat() {
        return customerDestinationLat;
    }

    public void setCustomerDestinationLat(double customerDestinationLat) {
        this.customerDestinationLat = customerDestinationLat;
    }

    public double getCustomerDestinationLng() {
        return customerDestinationLng;
    }

    public void setCustomerDestinationLng(double customerDestinationLng) {
        this.customerDestinationLng = customerDestinationLng;
    }

    public double getCustomerPickUpLat() {
        return customerPickUpLat;
    }

    public void setCustomerPickUpLat(double customerPickUpLat) {
        this.customerPickUpLat = customerPickUpLat;
    }

    public double getCustomerPickUpLng() {
        return customerPickUpLng;
    }

    public void setCustomerPickUpLng(double customerPickUpLng) {
        this.customerPickUpLng = customerPickUpLng;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public double getSurchargeAmount() {
        return surchargeAmount;
    }

    public void setSurchargeAmount(double surchargeAmount) {
        this.surchargeAmount = surchargeAmount;
    }
}
