package com.sand_corporation.www.uthaopartner.ForeGroundService;

import android.os.AsyncTask;
import android.util.Log;

import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable.CustomerPickUpLatLng;


/**
 * Created by HP on 3/4/2018.
 */

public class ForeGroundServiceRepository{



    //Add BankingInfo to database
    public void addCustomerPickUpLatLng(final CustomerPickUpLatLng customerPickUpLatLng, AppDatabase mDb) {
        new ForeGroundServiceRepository.addCustomerPickUpLatLngAsyncTask(mDb).execute(customerPickUpLatLng);
    }

    private static class addCustomerPickUpLatLngAsyncTask extends
            AsyncTask<CustomerPickUpLatLng, Void, Void> {
        private AppDatabase db;

        addCustomerPickUpLatLngAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(CustomerPickUpLatLng... customerPickUpLatLngs) {
            if(db == null){
                Log.i("FGServiceRepository","BankingInfo db == null");
            }
            long returnedvalue = db.customerPickUpLatLngDao()
                    .insertCustomerPickUpLatLng(customerPickUpLatLngs[0]);
            Log.i("FGServiceRepository","insertBankingInfo: " + returnedvalue);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
