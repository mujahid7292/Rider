package com.sand_corporation.www.uthaopartner.RideHistorySinglePage;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptEditedTable.MoneyReceiptEdited;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;

/**
 * Created by HP on 2/18/2018.
 */

public class RideHistorySinglePageViewModel extends AndroidViewModel {

    private String TAG = "RideHistoryRecyclerViewModel";
    public LiveData<MoneyReceipt> moneyReceiptsForSinglePage;
    public LiveData<MoneyReceiptEdited> moneyReceiptsEditedForSinglePage;
    public LiveData<BasicInfo> mBasicInfo;
    private AppDatabase mDb;

    public RideHistorySinglePageViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
        if (Common.rideIdForAdapter != null){
            moneyReceiptsForSinglePage = mDb.moneyReceiptDao().loadMoneyReceiptByrideId(Common.rideIdForAdapter);
            mBasicInfo = mDb.basicInfoDao().loadBasicInfo(Common.driverUID);
            moneyReceiptsEditedForSinglePage = mDb.moneyReceiptEditedDao()
                    .loadMoneyReceiptEditedByrideId(Common.rideIdForAdapter);
        }

    }

    //Update Money Receipt in database
    public void addMoneyReceiptEdited(final MoneyReceiptEdited moneyReceiptEdited) {
        if (mDb == null){
            Log.i(TAG,"addMoneyReceiptEdited mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new RideHistorySinglePageViewModel.updateMoneyReceiptEditedAsyncTask(mDb).execute(moneyReceiptEdited);
        } else {
            new RideHistorySinglePageViewModel.updateMoneyReceiptEditedAsyncTask(mDb).execute(moneyReceiptEdited);
        }

    }

    private static class updateMoneyReceiptEditedAsyncTask extends AsyncTask<MoneyReceiptEdited, Void, Void> {

        private AppDatabase db;

        updateMoneyReceiptEditedAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(MoneyReceiptEdited... moneyReceiptEditeds) {
            db.moneyReceiptEditedDao().insertMoneyReceiptEdited(moneyReceiptEditeds[0]);
            return null;
        }
    }


    @Override
    protected void onCleared() {
        AppDatabase.destroyInstance();
        mDb = null;
        moneyReceiptsForSinglePage = null;
        Common.rideIdForAdapter = null;
        super.onCleared();
    }
}
