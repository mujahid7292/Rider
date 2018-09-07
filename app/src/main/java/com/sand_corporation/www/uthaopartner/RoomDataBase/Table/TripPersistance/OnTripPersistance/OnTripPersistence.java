package com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.OnTripPersistance;

import android.arch.persistence.room.Embedded;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo.AssignedCustomerInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails.AssignedCustomerTripDetails;

/**
 * Created by HP on 3/6/2018.
 */

//@Entity
public class OnTripPersistence {

    @Embedded
    AssignedCustomerInfo customerInfo;

    @Embedded
    AssignedCustomerTripDetails tripDetails;

    public AssignedCustomerInfo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(AssignedCustomerInfo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public AssignedCustomerTripDetails getTripDetails() {
        return tripDetails;
    }

    public void setTripDetails(AssignedCustomerTripDetails tripDetails) {
        this.tripDetails = tripDetails;
    }

//    public int getOnTripPersistenceRowNumber() {
//        return OnTripPersistenceRowNumber;
//    }
//
//    public void setOnTripPersistenceRowNumber(int onTripPersistenceRowNumber) {
//        OnTripPersistenceRowNumber = onTripPersistenceRowNumber;
//    }
}
