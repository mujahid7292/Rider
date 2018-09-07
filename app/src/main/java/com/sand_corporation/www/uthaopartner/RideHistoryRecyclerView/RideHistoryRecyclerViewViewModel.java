package com.sand_corporation.www.uthaopartner.RideHistoryRecyclerView;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.SubsetOfMoneyReceiptColumn.MoneyReceiptRecyclerView;

import java.util.List;

/**
 * Created by HP on 2/16/2018.
 */

public class RideHistoryRecyclerViewViewModel extends AndroidViewModel {

    private String TAG = "RideHistoryRecyclerViewViewModel";
    public final LiveData<List<MoneyReceiptRecyclerView>> moneyReceiptsForRecyclerView;
    public LiveData<BasicInfo> mBasicInfo;
    private AppDatabase mDb;

    public RideHistoryRecyclerViewViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
        moneyReceiptsForRecyclerView = mDb.moneyReceiptDao().loadDataForRecyclerView();
        mBasicInfo = mDb.basicInfoDao().loadBasicInfo(Common.driverUID);
    }


    //Add money receipt to database
    public void addMoneyReceipt(final MoneyReceipt moneyReceipt) {
        if (mDb == null){
            Log.i(TAG,"addMoneyReceipt mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new RideHistoryRecyclerViewViewModel.addMoneyReceiptAsyncTask(mDb).execute(moneyReceipt);
        } else {
            new RideHistoryRecyclerViewViewModel.addMoneyReceiptAsyncTask(mDb).execute(moneyReceipt);
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


    @Override
    protected void onCleared() {
        AppDatabase.destroyInstance();
        mDb = null;
        super.onCleared();
    }
}
