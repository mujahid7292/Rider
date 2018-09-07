package com.sand_corporation.www.uthaopartner.AccountsActivity;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthaopartner.AccountsActivity.CommunicationBetweenFragment.SelectedDocument;
import com.sand_corporation.www.uthaopartner.AccountsActivity.CommunicationBetweenFragment.SharedViewModel;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable.DrivingLicense;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable.FITNESS;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable.NIDBACK;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable.NIDFRONT;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable.RegPaper;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable.TaxToken;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImageViewFragment extends Fragment {

    private static String TAG = "AccountActivity";
    private AccountActivityViewModel mViewModel;
    private ImageView img;
    private Bitmap bitmap;

    public ImageViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_image_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //This below SharedViewModel for communicating between two fragments
        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this).get(AccountActivityViewModel.class);

        SharedViewModel model = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        model.getSelected().observe(this, new Observer<SelectedDocument>() {
            @Override
            public void onChanged(@Nullable SelectedDocument selectedDocument) {
                if (selectedDocument != null){
                    String selectedDoc = selectedDocument.getSelectedImage();
                    if (selectedDoc.equals("Driving_License")){
                        mViewModel.mDrivingLicense.observe(getActivity(), new Observer<DrivingLicense>() {
                            @Override
                            public void onChanged(@Nullable DrivingLicense drivingLicense) {
                                if (drivingLicense != null){
                                    bitmap = getImage(drivingLicense.getDriving_License());
                                    img = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
                                    Glide.with(getActivity()).load(bitmap).into(img);
                                }
                            }
                        });
                    }
                    if (selectedDoc.equals("Nid_Front")){
                        mViewModel.mNIDFRONT.observe(getActivity(), new Observer<NIDFRONT>() {
                            @Override
                            public void onChanged(@Nullable NIDFRONT nidfront) {
                                if (nidfront != null){
                                    bitmap = getImage(nidfront.getNID_FRONT());
                                    img = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
                                    Glide.with(getActivity()).load(bitmap).into(img);
                                }
                            }
                        });

                    }
                    if (selectedDoc.equals("Nid_Back")){
                        mViewModel.mNIDBACK.observe(getActivity(), new Observer<NIDBACK>() {
                            @Override
                            public void onChanged(@Nullable NIDBACK nidback) {
                                if (nidback != null){
                                    bitmap = getImage(nidback.getNID_BACK());
                                    img = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
                                    Glide.with(getActivity()).load(bitmap).into(img);
                                }
                            }
                        });

                    }
                    if (selectedDoc.equals("Registration_Number")){
                        mViewModel.mRegPaper.observe(getActivity(), new Observer<RegPaper>() {
                            @Override
                            public void onChanged(@Nullable RegPaper regPaper) {
                                if (regPaper != null){
                                    bitmap = getImage(regPaper.getREGISTRATION_PAPER());
                                    img = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
                                    Glide.with(getActivity()).load(bitmap).into(img);
                                }
                            }
                        });
                    }
                    if (selectedDoc.equals("Fitness_Certificate")){
                        mViewModel.mFITNESS.observe(getActivity(), new Observer<FITNESS>() {
                            @Override
                            public void onChanged(@Nullable FITNESS fitness) {
                                if (fitness != null){
                                    bitmap = getImage(fitness.getFITNESS_CERTIFICATE());
                                    img = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
                                    Glide.with(getActivity()).load(bitmap).into(img);
                                }
                            }
                        });
                    }
                    if (selectedDoc.equals("Tax_Token")){
                        mViewModel.mTaxToken.observe(getActivity(), new Observer<TaxToken>() {
                            @Override
                            public void onChanged(@Nullable TaxToken taxToken) {
                                if (taxToken != null){
                                    bitmap = getImage(taxToken.getTAX_TOKEN());
                                    img = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
                                    Glide.with(getActivity()).load(bitmap).into(img);
                                }
                            }
                        });
                    }

                }
            }
        });
    }

    private class loadImageFromDBAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private AppDatabase db;

        loadImageFromDBAsyncTask() {
            if (db == null){
                db = AppDatabase.getInMemoryDatabase(getActivity());
            }

        }


        @Override
        protected Bitmap doInBackground(String... whichImage) {
            if(db == null){
            }
            Bitmap bitmap = null;
            if (whichImage[0].equals("Driving_License")){
//                DrivingLicense drivingLicense = db.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
//                bitmap = getImage(drivingLicense.getDriving_License());
            }
            if (whichImage[0].equals("Nid_Front")){
//                DrivingLicense drivingLicense = db.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
//                bitmap = getImage(drivingLicense.getDriving_License());
            }
            if (whichImage[0].equals("Nid_Back")){
//                DrivingLicense drivingLicense = db.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
//                bitmap = getImage(drivingLicense.getDriving_License());
            }
            if (whichImage[0].equals("Registration_Number")){
//                DrivingLicense drivingLicense = db.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
//                bitmap = getImage(drivingLicense.getDriving_License());
            }
            if (whichImage[0].equals("Fitness_Certificate")){
//                DrivingLicense drivingLicense = db.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
//                bitmap = getImage(drivingLicense.getDriving_License());
            }
            if (whichImage[0].equals("Tax_Token")){
//                DrivingLicense drivingLicense = db.drivingLicenseDao().loadDrivingLicense(Common.driverUID);
//                bitmap = getImage(drivingLicense.getDriving_License());
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ImageView imgScannedDocumentShownByGlide = getActivity().findViewById(R.id.imgScannedDocumentShownByGlide);
            Glide.with(getActivity()).load(bitmap).into(imgScannedDocumentShownByGlide);
        }
    }

    // convert from byte array to bitmap
    public Bitmap getImage(byte[] image) {
        FirebaseCrash.log("Home:getImage.called");
        Crashlytics.log("Home:getImage.called");
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onPause() {
        super.onPause();
        bitmap = null;
        img = null;
        Log.i(TAG,"ImageViewFragment: onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"ImageViewFragment: onResume");
    }

    @Override
    public void onDestroy() {
        bitmap = null;
        img = null;
        super.onDestroy();
        Log.i(TAG,"ImageViewFragment: onDestroy");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,"ImageViewFragment: onStop");
    }

}
