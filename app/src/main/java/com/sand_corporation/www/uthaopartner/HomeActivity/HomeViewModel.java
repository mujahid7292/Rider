package com.sand_corporation.www.uthaopartner.HomeActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.SubsetOfBasicInfo.NavigationMenuSubSet;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo.AssignedCustomerInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails.AssignedCustomerTripDetails;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.OnTripPersistance.OnTripPersistence;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable.CustomerPickUpLatLng;

import java.util.List;

/**
 * Created by HP on 2/16/2018.
 */

public class HomeViewModel extends AndroidViewModel {

    public final LiveData<NavigationMenuSubSet> navigationMenuSubSetLiveData;
    public LiveData<BasicInfo> mBasicInfo;
    public LiveData<List<CustomerPickUpLatLng>> mCustomerPickUpLatLng;
    public LiveData<List<AssignedCustomerTripDetails>> mAssignedCustomerTripDetails;
    public LiveData<List<OnTripPersistence>> mOnTripPersistence;
    private AppDatabase mDb;
    private static final String TAG = "HomeViewModel";


    public HomeViewModel(@NonNull Application application) {
        super(application);

        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
        navigationMenuSubSetLiveData = mDb.basicInfoDao().loadDataForNavigationMenu();
        mBasicInfo = mDb.basicInfoDao().loadBasicInfo(Common.driverUID);
        mCustomerPickUpLatLng = mDb.customerPickUpLatLngDao().loadCustomerPickUpLatLng();
        mAssignedCustomerTripDetails = mDb.assignedCustomerTripDetailsDao().loadTripDetails();
        mOnTripPersistence = mDb.assignedCustomerInfoDao().retrieveOnTripPersistence();
    }


    //Add money receipt to database
    public void addMoneyReceipt(final MoneyReceipt moneyReceipt) {
        if (mDb == null){
            Log.i(TAG,"addMoneyReceipt mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new addMoneyReceiptAsyncTask(mDb).execute(moneyReceipt);
        } else {
            new addMoneyReceiptAsyncTask(mDb).execute(moneyReceipt);
        }

    }

    private static class addMoneyReceiptAsyncTask extends AsyncTask<MoneyReceipt, Void, Void> {

        private AppDatabase db;

        addMoneyReceiptAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(MoneyReceipt... moneyReceipts) {
            db.moneyReceiptDao().insertMoneyReceipt(moneyReceipts[0]);
            return null;
        }
    }



    //Retrieve CustomerPickUpLatLng from database
    public void retrieveCustomerPickUpLatLng(final CustomerPickUpLatLng customerPickUpLatLng) {
        if (mDb == null){
            Log.i(TAG,"addMoneyReceipt mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new retrieveCustomerPickUpLatLngAsyncTask(mDb).execute();
        } else {
            new retrieveCustomerPickUpLatLngAsyncTask(mDb).execute();
        }

    }

    private static class retrieveCustomerPickUpLatLngAsyncTask extends
            AsyncTask<CustomerPickUpLatLng, Void, Void> {

        private AppDatabase db;

        retrieveCustomerPickUpLatLngAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(CustomerPickUpLatLng... customerPickUpLatLngs) {
            db.customerPickUpLatLngDao().loadCustomerPickUpLatLng();
            return null;
        }
    }



    //Clear CustomerPickUpLatLng table from database
    public void clearCustomerPickUpLatLng() {
        if (mDb == null){
            Log.i(TAG,"addMoneyReceipt mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new clearCustomerPickUpLatLngAsyncTask(mDb).execute();
        } else {
            new clearCustomerPickUpLatLngAsyncTask(mDb).execute();
        }

    }

    private static class clearCustomerPickUpLatLngAsyncTask extends
            AsyncTask<CustomerPickUpLatLng, Void, Void> {

        private AppDatabase db;

        clearCustomerPickUpLatLngAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(CustomerPickUpLatLng... customerPickUpLatLngs) {
            db.customerPickUpLatLngDao().deleteEntireTableOfCustomerPickUpLatLng();
            return null;
        }
    }



    //Add Assigned Customer Trip Details to database
    public void addAssignedCustomerTripDetails(final AssignedCustomerTripDetails tripDetails) {
        if (mDb == null){
            Log.i(TAG,"addAssignedCustomerTripDetails mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new addAssignedCustomerTripDetailsAsyncTask(mDb).execute(tripDetails);
        } else {
            new addAssignedCustomerTripDetailsAsyncTask(mDb).execute(tripDetails);
        }

    }

    private static class addAssignedCustomerTripDetailsAsyncTask extends
            AsyncTask<AssignedCustomerTripDetails, Void, Void> {

        private AppDatabase db;

        addAssignedCustomerTripDetailsAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(AssignedCustomerTripDetails... tripDetails) {
            db.assignedCustomerTripDetailsDao().insertAssignedCustomerTripDetails(tripDetails[0]);
            return null;
        }
    }

    //Clear AssignedCustomerTripDetails table from database
    public void clearAssignedCustomerTripDetails() {
        if (mDb == null){
            Log.i(TAG,"addMoneyReceipt mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new clearAssignedCustomerTripDetailsAsyncTask(mDb).execute();
        } else {
            new clearAssignedCustomerTripDetailsAsyncTask(mDb).execute();
        }

    }

    private static class clearAssignedCustomerTripDetailsAsyncTask extends
            AsyncTask<AssignedCustomerTripDetails, Void, Void> {

        private AppDatabase db;

        clearAssignedCustomerTripDetailsAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(AssignedCustomerTripDetails... tripDetails) {
            db.assignedCustomerTripDetailsDao().deleteEntireTableOfAssignedCustomerTripDetails();
            return null;
        }
    }

    //Add AssignedCustomerInfo to database
    public void addAssignedCustomerInfo(final AssignedCustomerInfo customerInfo) {
        if (mDb == null){
            Log.i(TAG,"addAssignedCustomerInfo mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new addAssignedCustomerInfoAsyncTask(mDb).execute(customerInfo);
        } else {
            new addAssignedCustomerInfoAsyncTask(mDb).execute(customerInfo);
        }

    }

    private static class addAssignedCustomerInfoAsyncTask extends
            AsyncTask<AssignedCustomerInfo, Void, Void> {

        private AppDatabase db;

        addAssignedCustomerInfoAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(AssignedCustomerInfo... customerInfos) {
            db.assignedCustomerInfoDao().insertAssignedCustomerInfo(customerInfos[0]);
            return null;
        }
    }

    //Clear AssignedCustomerInfo table from database
    public void clearAssignedCustomerInfo() {
        if (mDb == null){
            Log.i(TAG,"clearAssignedCustomerInfo mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new clearAssignedCustomerInfoAsyncTask(mDb).execute();
        } else {
            new clearAssignedCustomerInfoAsyncTask(mDb).execute();
        }

    }

    private static class clearAssignedCustomerInfoAsyncTask extends
            AsyncTask<AssignedCustomerInfo, Void, Void> {

        private AppDatabase db;

        clearAssignedCustomerInfoAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(AssignedCustomerInfo... customerInfos) {
            db.assignedCustomerInfoDao().deleteEntireTableOfAssignedCustomerInfo();
            return null;
        }
    }


    //Clear AssignedCustomerInfo table from database
    public void clearOnTripPersistence() {
        if (mDb == null){
            Log.i(TAG,"clearOnTripPersistence mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new clearOnTripPersistenceAsyncTask(mDb).execute();
        } else {
            new clearOnTripPersistenceAsyncTask(mDb).execute();
        }

    }

    private static class clearOnTripPersistenceAsyncTask extends
            AsyncTask<OnTripPersistence, Void, Void> {

        private AppDatabase db;

        clearOnTripPersistenceAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(OnTripPersistence... tripPersistences) {
//            db.assignedCustomerInfoDao().deleteEntireTableOfOnTripPersistence();
            return null;
        }
    }

    @Override
    protected void onCleared() {
        AppDatabase.destroyInstance();
        mDb = null;
        super.onCleared();
    }



}
