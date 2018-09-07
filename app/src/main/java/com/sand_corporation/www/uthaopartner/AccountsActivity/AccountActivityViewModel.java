package com.sand_corporation.www.uthaopartner.AccountsActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable.ProfilePic;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable.DrivingLicense;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable.FITNESS;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable.NIDBACK;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable.NIDFRONT;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable.RegPaper;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable.TaxToken;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by HP on 3/1/2018.
 */

public class AccountActivityViewModel extends AndroidViewModel {

    private static String TAG = "AccountActivityViewModel";
    public LiveData<BasicInfo> mBasicInfo;
    public LiveData<List<MoneyReceipt>> mMoneyReceiptList;
    public LiveData<ProfilePic> mProfilePic;
    public LiveData<DrivingLicense> mDrivingLicense;
    public LiveData<NIDFRONT> mNIDFRONT;
    public LiveData<NIDBACK> mNIDBACK;
    public LiveData<RegPaper> mRegPaper;
    public LiveData<FITNESS> mFITNESS;
    public LiveData<TaxToken> mTaxToken;
    private AppDatabase mDb;

    public AccountActivityViewModel(@NonNull Application application) {
        super(application);
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
        mBasicInfo = mDb.basicInfoDao().loadBasicInfo(Common.driverUID);
        mProfilePic = mDb.profilePicDao().loadProfilePic(Common.driverUID);
        mMoneyReceiptList = mDb.moneyReceiptDao()
                .loadAllMoneyReceiptBetweenDates(numberOfDaysTobeBackInLong(),getTodayDate());

        mDrivingLicense = mDb.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
        mNIDFRONT = mDb.nidFrontDao().loadNIDFRONT(Common.driverUID);
        mNIDBACK = mDb.nidBackDao().loadNIDBACK(Common.driverUID);
        mRegPaper = mDb.regPaperDao().loadRegPaper(Common.driverUID);
        mFITNESS = mDb.fitnessDao().loadFITNESS(Common.driverUID);
        mTaxToken = mDb.taxTokenDao().loadTaxToken(Common.driverUID);
    }


    private Long getFromDate (int numberOfDaysBack){
        Date date = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(numberOfDaysBack));
        Long numberOfDaysBackInLong = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(numberOfDaysBack);
        Log.i(TAG,"FromDate: " + getDate(numberOfDaysBackInLong));
        return numberOfDaysBackInLong;
    }

    private Long getTodayDate(){
        Long time = System.currentTimeMillis();
        Log.i(TAG,"TodayDate: " + getDate(time));
        return time;
    }

    private long numberOfDaysTobeBackInLong(){
        int numberOfDays = 0;
        Long numberOfDaysBackInLong = null;
        //dayOfTheWeek    6     7     1     2     3     4     5
        String[] day = {"Sat","Sun","Mon","Tue","Wed","Thu","Fri"};
        Date today = new Date(System.currentTimeMillis());
        int todayNumber = new DateTime(today).getDayOfWeek();
        switch (todayNumber){
            case 1:
                numberOfDays = 2;
                numberOfDaysBackInLong =
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(2);
                break;

            case 2:
                numberOfDays = 3;
                numberOfDaysBackInLong =
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(3);
                break;

            case 3:
                numberOfDays = 4;
                numberOfDaysBackInLong =
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(4);
                break;

            case 4:
                numberOfDays = 5;
                numberOfDaysBackInLong =
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5);
                break;

            case 5:
                numberOfDays = 6;
                numberOfDaysBackInLong =
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(6);
                break;

            case 6:
                numberOfDays = 0;
                int dayOfTheMonth = new DateTime(today).getDayOfMonth();
                int monthOfTheYear = new DateTime(today).getMonthOfYear();
                long midnightInLong = getMidnight(dayOfTheMonth,monthOfTheYear);
                numberOfDaysBackInLong = midnightInLong;
                break;

            case 7:
                numberOfDays = 1;
                numberOfDaysBackInLong =
                        System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
                break;
        }
        Log.i(TAG,"FromDate: " + getDate(numberOfDaysBackInLong));
        return numberOfDaysBackInLong;
    }

    private String getDate(Long dateInMilli) {
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(dateInMilli);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a",calendar).toString();
        return date;
    }

    private Long getMidnight(int dayOfMonth, int monthOfTheYear){
        Calendar c = new GregorianCalendar();
        c.set(Calendar.MONTH,(monthOfTheYear - 1));
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        c.set(Calendar.HOUR_OF_DAY, 0); //anything 0 - 23
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND,0);
        Long midnight  = c.getTimeInMillis();
        Log.i(TAG,"DayOfMonth: "+ dayOfMonth + " | " + "monthOfTheYear: "
                + monthOfTheYear + " | " +"Midnight: " + getDate(midnight));
        return midnight;
    }




    @Override
    protected void onCleared() {
        AppDatabase.destroyInstance();
        mDb = null;
        mBasicInfo = null;
        mProfilePic = null;
        super.onCleared();
    }
}
