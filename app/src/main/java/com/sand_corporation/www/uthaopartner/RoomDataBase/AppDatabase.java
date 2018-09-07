package com.sand_corporation.www.uthaopartner.RoomDataBase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BankingInfo.BankingInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BankingInfo.BankingInfoDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfoDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptEditedTable.MoneyReceiptEdited;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptEditedTable.MoneyReceiptEditedDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceiptDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable.ProfilePic;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable.ProfilePicDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable.DrivingLicense;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable.DrivingLicenseDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable.FITNESS;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable.FitnessDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable.NIDBACK;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable.NidBackDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable.NIDFRONT;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable.NidFrontDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable.RegPaper;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable.RegPaperDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable.TaxToken;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable.TaxTokenDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo.AssignedCustomerInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo.AssignedCustomerInfoDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails.AssignedCustomerTripDetails;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails.AssignedCustomerTripDetailsDao;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable.CustomerPickUpLatLng;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable.CustomerPickUpLatLngDao;

/**
 * Created by HP on 2/16/2018.
 */

@Database(entities = {MoneyReceipt.class,
                      MoneyReceiptEdited.class,
                      BasicInfo.class,
                      ProfilePic.class,
                      BankingInfo.class,
                      DrivingLicense.class,
                      NIDFRONT.class,
                      NIDBACK.class,
                      RegPaper.class,
                      FITNESS.class,
                      TaxToken.class,
                      CustomerPickUpLatLng.class,
                      AssignedCustomerTripDetails.class,
                      AssignedCustomerInfo.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase{

    private static AppDatabase INSTANCE;
    public abstract MoneyReceiptDao moneyReceiptDao();
    public abstract MoneyReceiptEditedDao moneyReceiptEditedDao();
    public abstract BasicInfoDao basicInfoDao();
    public abstract ProfilePicDao profilePicDao();
    public abstract BankingInfoDao bankingInfoDao();
    public abstract DrivingLicenseDao drivingLicenseDao();
    public abstract NidFrontDao nidFrontDao();
    public abstract NidBackDao nidBackDao();
    public abstract RegPaperDao regPaperDao();
    public abstract FitnessDao fitnessDao();
    public abstract TaxTokenDao taxTokenDao();
    public abstract CustomerPickUpLatLngDao customerPickUpLatLngDao();
    public abstract AssignedCustomerTripDetailsDao assignedCustomerTripDetailsDao();
    public abstract AssignedCustomerInfoDao assignedCustomerInfoDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "UthaoRoomDB").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
