package com.sand_corporation.www.uthaopartner.WelcomeActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BankingInfo.BankingInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable.ProfilePic;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable.DrivingLicense;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable.FITNESS;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable.NIDBACK;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable.NIDFRONT;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable.RegPaper;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable.TaxToken;

import java.util.List;

/**
 * Created by HP on 2/19/2018.
 */

public class WelcomeViewModel extends AndroidViewModel {

    private static final String TAG = "WelcomeViewModel";
    public LiveData<List<BasicInfo>> mBasicInfo;
    private AppDatabase mDb;

    public WelcomeViewModel(@NonNull Application application) {
        super(application);
        mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
        mBasicInfo = mDb.basicInfoDao().getAllTheBasicInfo();
    }


    //Update basic info to database
    public void updateBasicInfo(final BasicInfo basicInfo) {
        if (mDb == null){
            Log.i(TAG,"addBasicInfo mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.updateBasicInfoAsyncTask(mDb).execute(basicInfo);
        } else {
            new WelcomeViewModel.updateBasicInfoAsyncTask(mDb).execute(basicInfo);
        }

    }

    private static class updateBasicInfoAsyncTask extends AsyncTask<BasicInfo, Void, Void> {

        private AppDatabase db;

        updateBasicInfoAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(BasicInfo... basicInfos) {
            if(db == null){
                Log.i(TAG,"updateBasicInfo db == null");
            }
            long returnedvalue = db.basicInfoDao().updateBasicInfo(basicInfos[0]);
            Log.i(TAG,"updateBasicInfo: " + returnedvalue);
            return null;
        }
    }

    //Add basic info to database
    public void addBasicInfo(final BasicInfo basicInfo) {
        if (mDb == null){
            Log.i(TAG,"addBasicInfo mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addBasicInfoAsyncTask(mDb).execute(basicInfo);
        } else {
            new WelcomeViewModel.addBasicInfoAsyncTask(mDb).execute(basicInfo);
        }

    }

    private static class addBasicInfoAsyncTask extends AsyncTask<BasicInfo, Void, Void> {

        private AppDatabase db;

        addBasicInfoAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(BasicInfo... basicInfos) {
            if(db == null){
                Log.i(TAG,"BasicInfo db == null");
            }
            long returnedvalue = db.basicInfoDao().insertBasicInfo(basicInfos[0]);
            Log.i(TAG,"insertBasicInfo: " + returnedvalue);
            return null;
        }
    }


    //Add Profile Pic to database
    public void addProfilePic(final ProfilePic profilePic) {
        if (mDb == null){
            Log.i(TAG,"addProfilePic mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addProfilePicAsyncTask(mDb).execute(profilePic);
        } else {
            new WelcomeViewModel.addProfilePicAsyncTask(mDb).execute(profilePic);
        }

    }

    private static class addProfilePicAsyncTask extends AsyncTask<ProfilePic, Void, Void> {

        private AppDatabase db;

        addProfilePicAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(ProfilePic... profilePics) {
            if(db == null){
                Log.i(TAG,"ProfilePic db == null");
            }
            long returnedvalue = db.profilePicDao().insertProfilePic(profilePics[0]);
            Log.i(TAG,"insertProfilePic: " + returnedvalue);
            return null;
        }
    }


    //Add Driving_Licenese to database
    public void addDrivingLicense(final DrivingLicense drivingLicense) {
        if (mDb == null){
            Log.i(TAG,"addDrivingLicense mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addDrivingLicenseAsyncTask(mDb).execute(drivingLicense);
        } else {
            new WelcomeViewModel.addDrivingLicenseAsyncTask(mDb).execute(drivingLicense);
        }

    }

    private static class addDrivingLicenseAsyncTask extends
            AsyncTask<DrivingLicense, Void, Void> {
        private AppDatabase db;

        addDrivingLicenseAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(DrivingLicense... drivingLicenses) {
            if(db == null){
                Log.i(TAG,"DrivingLicense db == null");
            }
            long returnedvalue = db.drivingLicenseDao().insertDrivingLicense(drivingLicenses[0]);
            Log.i(TAG,"insertDrivingLicense: " + returnedvalue);
            return null;
        }
    }


    //Add NID_FRONT to database
    public void addNidFront(final NIDFRONT nidfront) {
        if (mDb == null){
            Log.i(TAG,"addNidFront mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addNidFrontAsyncTask(mDb).execute(nidfront);
        } else {
            new WelcomeViewModel.addNidFrontAsyncTask(mDb).execute(nidfront);
        }

    }

    private static class addNidFrontAsyncTask extends
            AsyncTask<NIDFRONT, Void, Void> {
        private AppDatabase db;

        addNidFrontAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(NIDFRONT... nidfronts) {
            if(db == null){
                Log.i(TAG,"NIDFRONT db == null");
            }
            long returnedvalue = db.nidFrontDao().insertNIDFRONT(nidfronts[0]);
            Log.i(TAG,"insertNIDFRONT: " + returnedvalue);
            return null;
        }
    }


    //Add NID_BACK to database
    public void addNidBack(final NIDBACK nidback) {
        if (mDb == null){
            Log.i(TAG,"addNidBack mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addNidBackAsyncTask(mDb).execute(nidback);
        } else {
            new WelcomeViewModel.addNidBackAsyncTask(mDb).execute(nidback);
        }

    }

    private static class addNidBackAsyncTask extends
            AsyncTask<NIDBACK, Void, Void> {
        private AppDatabase db;

        addNidBackAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(NIDBACK... nidbacks) {
            if(db == null){
                Log.i(TAG,"NIDBACK db == null");
            }
            long returnedvalue = db.nidBackDao().insertNIDBACK(nidbacks[0]);
            Log.i(TAG,"insertNIDBACK: " + returnedvalue);
            return null;
        }
    }


    //Add FITNESS_CERTIFICATE to database
    public void addFitness(final FITNESS fitness) {
        if (mDb == null){
            Log.i(TAG,"addFitness mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addFitnessAsyncTask(mDb).execute(fitness);
        } else {
            new WelcomeViewModel.addFitnessAsyncTask(mDb).execute(fitness);
        }

    }

    private static class addFitnessAsyncTask extends
            AsyncTask<FITNESS, Void, Void> {
        private AppDatabase db;

        addFitnessAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(FITNESS... fitnesses) {
            if(db == null){
                Log.i(TAG,"FITNESS db == null");
            }
            long returnedvalue = db.fitnessDao().insertFITNESS(fitnesses[0]);
            Log.i(TAG,"insertFITNESS: " + returnedvalue);
            return null;
        }
    }


    //Add TAX_TOKEN to database
    public void addTaxToken(final TaxToken taxToken) {
        if (mDb == null){
            Log.i(TAG,"addTaxToken mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addTaxTokenAsyncTask(mDb).execute(taxToken);
        } else {
            new WelcomeViewModel.addTaxTokenAsyncTask(mDb).execute(taxToken);
        }

    }

    private static class addTaxTokenAsyncTask extends
            AsyncTask<TaxToken, Void, Void> {
        private AppDatabase db;

        addTaxTokenAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(TaxToken... taxTokens) {
            if(db == null){
                Log.i(TAG,"TaxToken db == null");
            }
            long returnedvalue = db.taxTokenDao().insertTaxToken(taxTokens[0]);
            Log.i(TAG,"insertTaxToken: " + returnedvalue);
            return null;
        }
    }



    //Add REGISTRATION_PAPER to database
    public void addRegPaper(final RegPaper regPaper) {
        if (mDb == null){
            Log.i(TAG,"addRegPaper mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addRegPaperAsyncTask(mDb).execute(regPaper);
        } else {
            new WelcomeViewModel.addRegPaperAsyncTask(mDb).execute(regPaper);
        }

    }

    private static class addRegPaperAsyncTask extends
            AsyncTask<RegPaper, Void, Void> {
        private AppDatabase db;

        addRegPaperAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(RegPaper... regPapers) {
            if(db == null){
                Log.i(TAG,"RegPaper db == null");
            }
            long returnedvalue = db.regPaperDao().insertRegPaper(regPapers[0]);
            Log.i(TAG,"insertRegPaper: " + returnedvalue);
            return null;
        }
    }


    //Add BankingInfo to database
    public void addBankingInfo(final BankingInfo bankingInfo) {
        if (mDb == null){
            Log.i(TAG,"addBankingInfo mDb == null");
            mDb = AppDatabase.getInMemoryDatabase(this.getApplication());
            new WelcomeViewModel.addBankingInfoAsyncTask(mDb).execute(bankingInfo);
        } else {
            new WelcomeViewModel.addBankingInfoAsyncTask(mDb).execute(bankingInfo);
        }

    }

    private static class addBankingInfoAsyncTask extends
            AsyncTask<BankingInfo, Void, Void> {
        private AppDatabase db;

        addBankingInfoAsyncTask(AppDatabase appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Void doInBackground(BankingInfo... bankingInfos) {
            if(db == null){
                Log.i(TAG,"BankingInfo db == null");
            }
            long returnedvalue = db.bankingInfoDao().insertBankingInfo(bankingInfos[0]);
            Log.i(TAG,"insertBankingInfo: " + returnedvalue);
            return null;
        }
    }





    @Override
    protected void onCleared() {
        AppDatabase.destroyInstance();
        mDb = null;
        mBasicInfo = null;
        super.onCleared();
    }
}
