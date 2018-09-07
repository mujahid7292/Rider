package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by HP on 3/6/2018.
 */

//Entity and table in sql are same
@Entity()
public class AssignedCustomerInfo {
    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "infoRowNumber")
    public int infoRowNumber;

    @ColumnInfo(name = "Customer_UID")
    private String customerUID;

    @ColumnInfo(name = "Customer_FCMToken")
    private String customerFCMToken;

    @ColumnInfo(name = "Customer_Name")
    private String customerName;

    @ColumnInfo(name = "Customer_Mobile")
    private String customerMobile;

    @ColumnInfo(name = "Customer_Ratings")
    private String customerRatings;

    @ColumnInfo(name = "Customer_Total_Trips")
    private String customerTotalTrips;

    @ColumnInfo(name = "Customer_FaceBook_PP")
    private String customerFaceBookPP;

    @ColumnInfo(name = "Customer_Profile_Pic_Url")
    private String customerProfilePicUrl;

    public AssignedCustomerInfo() {

    }

    public String getCustomerFCMToken() {
        return customerFCMToken;
    }

    public void setCustomerFCMToken(String customerFCMToken) {
        this.customerFCMToken = customerFCMToken;
    }

    public String getCustomerUID() {
        return customerUID;
    }

    public void setCustomerUID(String customerUID) {
        this.customerUID = customerUID;
    }

    @NonNull
    public int getInfoRowNumber() {
        return infoRowNumber;
    }

    public void setInfoRowNumber(@NonNull int infoRowNumber) {
        this.infoRowNumber = infoRowNumber;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public String getCustomerRatings() {
        return customerRatings;
    }

    public void setCustomerRatings(String customerRatings) {
        this.customerRatings = customerRatings;
    }

    public String getCustomerTotalTrips() {
        return customerTotalTrips;
    }

    public void setCustomerTotalTrips(String customerTotalTrips) {
        this.customerTotalTrips = customerTotalTrips;
    }

    public String getCustomerFaceBookPP() {
        return customerFaceBookPP;
    }

    public void setCustomerFaceBookPP(String customerFaceBookPP) {
        this.customerFaceBookPP = customerFaceBookPP;
    }

    public String getCustomerProfilePicUrl() {
        return customerProfilePicUrl;
    }

    public void setCustomerProfilePicUrl(String customerProfilePicUrl) {
        this.customerProfilePicUrl = customerProfilePicUrl;
    }
}
