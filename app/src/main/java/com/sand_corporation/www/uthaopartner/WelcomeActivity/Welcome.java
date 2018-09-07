package com.sand_corporation.www.uthaopartner.WelcomeActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sand_corporation.www.uthaopartner.Animation.TabAnimation;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.HomeActivity.Home;
import com.sand_corporation.www.uthaopartner.IntroSlider.SliderActivity;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BankingInfo.BankingInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ProfilePicTable.ProfilePic;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedDrivingLicenseTable.DrivingLicense;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedFitnessTable.FITNESS;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidBackTable.NIDBACK;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedNidFrontTable.NIDFRONT;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedRegPaperTable.RegPaper;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.ScannedTaxTokenTable.TaxToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import io.fabric.sdk.android.Fabric;

public class Welcome extends AppCompatActivity{
    //View Model
    private WelcomeViewModel mViewModel;


    private final int Request_GROUP_PERMISSION = 10000;
    private ProgressDialog progressDialog;
    private int Number_Of_Document_Uploaded_To_Server = 0;
    private Handler handler;
    private String TAG = "CheckWelcome";
    private String userRegistrationVerificationId;
    private String userSignInVerificationId;
    private Uri imageGalleryPathUri;
    private String mVerificationStage;
    private String strDriverName, strDriverOrBikerMobileNumber,strDriverOrBikerSelectedCity,
            strDriverCountryCode, registrationNumberWithCountryCode, strDriverEmail,
            strDriverPassword, strRegisterBirthDay, strSignInCountryCode, strSignInPhone,
            signInNumberWithCountryCode;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Trace myTrace;
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth mDriverAuth;
//    private FirebaseAuth.AuthStateListener mDriverAuthListener;
    private FirebaseFirestore mFireStoreDriverBasicInfoDB;
    private PhoneAuthProvider.ForceResendingToken mRegisterResendToken;
    private PhoneAuthProvider.ForceResendingToken mSignInResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mRegisterCallbacks;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mSignInCallbacks;
    private AuthCredential credential;
    private SpotsDialog waitingDialogSeverConnection, waitingDialogFaceBookSeverConnection,
            waitingDialogForPhoneVerification, waitingDialogForSignIn, fetchDriverDataFromServer, fetchDriverPPFromServer,
            fetchDriverDocumentsFromServer;
    private final int GPS_PERMISSION_REQUEST = 103;
    private final int REQUEST_SMS_PERMISSIONS = 104;
    private final int PHONE_STATE_PERMISSION = 105;
    private boolean restoreBySavedInStanceStateForCameraProblem = false;
    private boolean restoreBySavedInStanceStateForOtherLayOut = false;
    private static boolean isDriverLoggedInThisDeviceForFirstTime;
    private final String DEFAULT = "N/A";
    //Major Layout
    private RelativeLayout
            welcome_layout,
            chooseFromGalleryOrTakePhoto;
    private LinearLayout
            driverOrBikerRegistrationPhoneVerificationLayout,
            driverOrBikerRegistrationOTPLayOut,
            driverOrBikerRegisterUsingEmailPasswordLayOut,
            previewAndUploadLayout,
            select_city_layout,
            select_vehicle_type_layout,
            scannedDocumentsUploadLayoutForCar,
            addbKashNumber,
            scannedDocumentsUploadLayoutForBike;
    private ScrollView driverOrBikerSignInUsingEmailPasswordLayOut,
            signUpForCarLayout, signUpForBikeLayout;
    private boolean destroyPreviouslyCreatedView = false;
    private boolean destroyPreviouslyCreatedView1 = false;

    //Welcome Layout
    private Button btnDriverLogInWelcome, btnDriverRegistration;

    //Driver Sign in Layout
    private ImageView ic_back_sign9;
    private EditText edtSignInEmail, edtSignInPassword;
    private Button btnSignInUsingEmailPassword, btnBackToAllAccountPage1;

    //Driver Registration Phone Verification Layout
    private ImageView ic_back_sign8;
    private EditText edtRegistrationCountryCode, edtRegistrationPhoneNumber;
    private String strRegistrationCountryCode, strRegistrationPhoneNumber;
    private Button btnVerifyRegistrationPhoneNumber;

    //Driver Registration OTP Layout
    private ImageView ic_back_sign4;
    private EditText edtRegistrationEnterOTP;
    private Button btnVerifyRegistrationOTPNumber;

    //Select Your City Layout
    private ImageView ic_back_sign;
    private Button btnSelectDhaka, btnSelectChittagong, btnSelectSylhet;

    //Select Vehicle Type Layout
    private ImageView ic_back_sign1;
    private Button btnSelectCar, btnSelectBike;

    //Driver Email Password Registration Layout
    private ImageView ic_back_sign6;
    private EditText edtRegisterEmail, edtRegisterPassword;
    private String strRegisterEmail;
    private Button btnRegistrationUsingEmailPassword;

    //Sign up for car layout
    private EditText driverFullNameField, driverNIDField, driverLicenseField, driverHomeAddressField, carRegistrationAlphabet,
            carRegistrationFirstTwoDigit, carRegistrationLastFourDigit, carFitnessCertificateNumber,
            carTaxTokenNumber, referralCode;
    private ImageView ic_back_sign2;
    private CircleImageView driverProfilePic;
    private Uri imageGalleryPathUriForDriverProfilePic;
    private String mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic;
    private TabHost tabHostForCar;
    private Spinner spinnerCarManufacturer, spinnerCarModel, spinnerCarProductionYear,
            spinnerRegistrationAuthority;
    private Button btnGoToUploadDocumentPageForCar;
    private String strdriverFullNameField, strdriverNIDField, strdriverLicenseField,
            strdriverHomeAddressField, strcarRegistrationAlphabet,
            strcarRegistrationFirstTwoDigit, strcarRegistrationLastFourDigit, strDriverCarRegistrationNumber,
            strcarFitnessCertificateNumber, strcarTaxTokenNumber,
            strreferralCode;
    private final int REQUEST_CODE_FOR_GALLERY_ACCESS_FOR_DRIVER_PROFILE_PIC = 106 ;
    private String strManuFacturer, strModel, strProductionYear,
            strRegistrationAuthority;

    //Scanned Document layout For Car
    private LinearLayout scannedDrivingLicenseLayoutForCar, nidFrontSideLayoutForCar, nidBackSideLayoutForCar,
            carFitnessCertificateLayout, carTaxTokenLayout, carRegistrationPaperLayout;
    private ImageView imgDocumentUploadStatusDrivingLicenseForCar, imgDocumentUploadStatusNIDFrontSideForCar,
            imgDocumentUploadStatusNIDBackSideForCar, imgDocumentUploadStatusCarFitness,
            imgDocumentUploadStatusCarTaxToken, imgDocumentUploadStatusCarRegistration;
    private Button btnAddPaymentDetailsForCar;

    //Sign Up For Bike Layout
    private EditText bikerFullNameField, bikerNIDField, bikerLicenseField, bikerHomeAddressField,
            bikeRegistrationAlphabet, bikeRegistrationFirstTwoDigit, bikeRegistrationLastFourDigit,
            bikeReferralCode;
    private ImageView ic_back_sign14;
    private CircleImageView bikerProfilePic;
    private Uri imageGalleryPathUriForBikerProfilePic;
    private String mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic;
    private TabHost tabHostForbike;
    private Spinner spinnerBikeManufacturer, spinnerBikeModel, spinnerBikeProductionYear,
            spinnerBikeRegistrationAuthority;
    private Button btnGoToUploadDocumentPageForBike;
    private String strBikerFullNameField, strBikerNIDField, strBikerLicenseField,
            strBikerHomeAddressField, strBikeRegistrationAlphabet,
            strBikeRegistrationFirstTwoDigit, strBikeRegistrationLastFourDigit,
            strBikeRegistrationNumber, strBikeReferralCode;
    private final int REQUEST_CODE_FOR_GALLERY_ACCESS_FOR_BIKER_PROFILE_PIC = 107 ;


    //Scanned Document layout For Bike
    private LinearLayout scannedDrivingLicenseLayoutForBike, nidFrontSideLayoutForBike,
            nidBackSideLayoutForBike, bikeRegistrationPaperLayout;
    private ImageView imgDocumentUploadStatusDrivingLicenseForBike,
            imgDocumentUploadStatusNIDFrontSideForBike,
            imgDocumentUploadStatusNIDBackSideForBike,
            imgDocumentUploadStatusBikeRegistration;
    private Button btnAddPaymentDetailsForBike;


    //Choose From Gallery or Take New Photo Layout
    private ImageView ic_back_sign12;
    private Button btnTakePhoto, btnUploadFromGallery;
    private String toBeTakenImageName;

    //Preview documents and upload Layout
    private ImageView ic_back_sign3;
//    private SimpleDraweeView imgScannedDocumentShownByFresco;
    private ImageView imgScannedDocumentShownByGlide;
    private Button btnDiscardPhoto, btnUploadPhotoToServer;
    private final int requestCameraPermissionCode = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_CODE_FOR_GALLERY_ACCESS = 102;
    private String mCurrentPhotoPathForTakingImage;
    private String mCurrentPhotoPathForTakingImageFromGallery;
    private String imageTakenUpTOAppUsing;
    private TextView txtScannedPhotoName;

    //Add bKash number
    private ImageView ic_back_sign13;
    private EditText edtBkashNumber;
    private String strBkashNumber;
    private Button btnUploadbKashNumber;

    private void getWhichLayOutIsWrittenInSharedPreferences(){
        String Layout = preferences.getString("Layout",DEFAULT);
        Log.i(TAG,"Layout written in SharedPref: " + Layout);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG,"Welcome: onStart.Called");
        //If google play service is not available in the user device, our app
        //will not work. So in below method we will check if user device has google
        //play service or not. If don't have than we will redirect our user to
        //download page
        isGooglePlayServicesAvailable();
        //Below 'myTrace' code will check in app performance. We can see
        //our every user performance in firebase console.
        myTrace = FirebasePerformance.getInstance().newTrace("test_trace");
        myTrace.start();
        FirebaseApp.initializeApp(this);
        FirebaseCrash.getInstance(FirebaseApp.initializeApp(this));
        FirebaseCrash.log("Welcome onStart() called");
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //First we initialize firebase authentication variable
        //This below variable will check if our user is registered or not
        //only registered user will get access to our server / database
        mDriverAuth = FirebaseAuth.getInstance();

        //Every time, On the start of our app we will check whether our user is new
        //user or existing user. This below code will check that. If user is existing
        //user than we will redirect our user to Home.class (By passing registration
        //page)
        if (mDriverAuth != null) {
            checkWhetherUserIsRegisteredOrNot();
        }
        FirebaseCrash.log("Welcome: onStart() finished");

        //This below code will check whether user opened our app for the first time
        //Than it will show an intro slider page. In intro slider we will give short
        //promo of our product
        String registration_status = preferences.getString("Registration_Status",DEFAULT);
        String sliding_Status = preferences.getString("Sliding_Status",DEFAULT);
        if (registration_status.equals(DEFAULT) && sliding_Status.equals(DEFAULT)){
            FirebaseCrash.log("Welcome: User opened app for the first time");
            Intent intent = new Intent(Welcome.this, SliderActivity.class);
            startActivity(intent);
            finish();
        }


    }

    @Override
    protected void onResume() {
        Log.i(TAG,"Welcome: onResume.Called");
        FirebaseCrash.log("Welcome onResume().called");
        //Register incoming sms auto detector. This broadcast manager will listen
        //for new message in user device.
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("otp"));
        super.onResume();

        String Layout = preferences.getString("Layout",DEFAULT);
        destroyPreviouslyCreatedView1 = preferences
                .getBoolean("destroyPreviouslyCreatedView1",false);
        if (!Layout.equals("previewAndUploadLayout") && !destroyPreviouslyCreatedView1){
            //This means view get created for the first time
            //Change Car TabHost title color and underline color
            //!destroyPreviouslyCreatedView1 = means sign out for car or bike layout
            //view did not get destroyed yet
            TabHost tabHostForCar = getTabHostForCar();
            if (tabHostForCar.getTabWidget() != null){
                for(int i=0;i<tabHostForCar.getTabWidget().getChildCount();i++)
                {
                    //Change Title color
                    TextView tv = (TextView) tabHostForCar.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.parseColor("#ffffff"));
                    //Change Title underline color
                    View v = tabHostForCar.getTabWidget().getChildAt(i);
                    v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
                }
            }


            //Change Bike TabHost title color and underline color
            TabHost tabhostForBike = getTabHostForBike();
            if (tabhostForBike.getTabWidget() != null){
                for(int i=0;i<tabhostForBike.getTabWidget().getChildCount();i++)
                {
                    //Change Title color
                    TextView tv = (TextView) tabhostForBike.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                    tv.setTextColor(Color.parseColor("#ffffff"));
                    //Change Title underline color
                    View v = tabhostForBike.getTabWidget().getChildAt(i);
                    v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
                }
            }

        }

        String beforeDestroyedViewLayOutWas = preferences.getString("Layout",DEFAULT);

        if (beforeDestroyedViewLayOutWas.equals("signUpForCarLayout")){
            //This means view previously get destroyed
            //Change Car TabHost title color and underline color
            TabHost tabHostForCar = getTabHostForCar();
            for(int i=0;i<tabHostForCar.getTabWidget().getChildCount();i++)
            {
                //Change Title color
                TextView tv = (TextView) tabHostForCar.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(Color.parseColor("#ffffff"));
                //Change Title underline color
                View v = tabHostForCar.getTabWidget().getChildAt(i);
                v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
            }

        } else if (beforeDestroyedViewLayOutWas.equals("signUpForBikeLayout")){
            //Change Bike TabHost title color and underline color
            TabHost tabhostForBike = getTabHostForBike();
            for(int i=0;i<tabhostForBike.getTabWidget().getChildCount();i++)
            {
                //Change Title color
                TextView tv = (TextView) tabhostForBike.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
                tv.setTextColor(Color.parseColor("#ffffff"));
                //Change Title underline color
                View v = tabhostForBike.getTabWidget().getChildAt(i);
                v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG,"Welcome: onPause.Called");
        if (mCurrentPhotoPathForTakingImage != null
                && !mCurrentPhotoPathForTakingImage.equals("")){
            editor.putString("mCurrentPhotoPathForTakingImage",
                    mCurrentPhotoPathForTakingImage);
            editor.commit();
            Log.i(TAG,"Camera onPause.called: " + mCurrentPhotoPathForTakingImage);
        }

        if (mCurrentPhotoPathForTakingImageFromGallery != null
                && !mCurrentPhotoPathForTakingImageFromGallery.equals("")){
            editor.putString("mCurrentPhotoPathForTakingImageFromGallery",
                    mCurrentPhotoPathForTakingImageFromGallery);
            editor.commit();
            Log.i(TAG,"Gallery onPause.called: " + mCurrentPhotoPathForTakingImageFromGallery);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG,"Welcome: onStop.Called");
//        mDriverAuth.removeAuthStateListener(mDriverAuthListener);  //VERY IMPORTANT
        FirebaseCrash.log("Welcome: onStop() called");
        myTrace.stop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        if (!destroyPreviouslyCreatedView){
            if (welcome_layout.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }
            if (driverOrBikerSignInUsingEmailPasswordLayOut.getVisibility() == View.VISIBLE){
                driverOrBikerSignInUsingEmailPasswordLayOut.setVisibility(View.GONE);
                welcome_layout.setVisibility(View.VISIBLE);
            }
            if (driverOrBikerRegistrationPhoneVerificationLayout.getVisibility() == View.VISIBLE){
                driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                welcome_layout.setVisibility(View.VISIBLE);
            }
            if (driverOrBikerRegistrationOTPLayOut.getVisibility() == View.VISIBLE){
                driverOrBikerRegistrationOTPLayOut.setVisibility(View.GONE);
                driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
            }
            if (select_city_layout.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }
            if (select_vehicle_type_layout.getVisibility() == View.VISIBLE){
                select_vehicle_type_layout.setVisibility(View.GONE);
                select_city_layout.setVisibility(View.VISIBLE);
            }
            if (driverOrBikerRegisterUsingEmailPasswordLayOut.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }
        }


        if (!destroyPreviouslyCreatedView1){
            if (signUpForCarLayout.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }
            if (signUpForBikeLayout.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }
            if (scannedDocumentsUploadLayoutForCar.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }
            if (scannedDocumentsUploadLayoutForBike.getVisibility() == View.VISIBLE){
                Toast.makeText(this, "Back press will not work",
                        Toast.LENGTH_LONG).show();
            }

            if (chooseFromGalleryOrTakePhoto.getVisibility() == View.VISIBLE){
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                String chosen_vehicle = preferences.getString("Vehicle_Type",DEFAULT);
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Car")){
                    scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                }
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Bike")){
                    scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                }
            }

            if (previewAndUploadLayout.getVisibility() == View.VISIBLE){
                previewAndUploadLayout.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
            }
        }


        if (addbKashNumber.getVisibility() == View.VISIBLE){
            Toast.makeText(this, "Back press will not work",
                    Toast.LENGTH_LONG).show();
        }
    }

    private TabHost getTabHostForCar() {
        return tabHostForCar = findViewById(R.id.tabHostForCar);
    }

    private TabHost getTabHostForBike() {
        return tabHostForbike = findViewById(R.id.tabHostForBike);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG,"Welcome: onCreate.Called");
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());
        Fabric.with(this);
        FirebaseCrash.log("Welcome onCreate.called");
        FirebaseCrash.log("Welcome CalligraphyConfig .build() completed");
        //Below initialize Fire Store
        mFireStoreDriverBasicInfoDB = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_welcome);
        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this).get(WelcomeViewModel.class);

        // Check whether we're recreating a previously destroyed instance
        //View get destroyed when user get out of app for some reason.Exmp:
        //to see if one time password has come to msg inbox.
        preferences = getSharedPreferences("Driver_Basic_Info",
                Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
        editor = preferences.edit();
        if (savedInstanceState != null) {
            //This means our view get destroyed
            restoreBySavedInStanceStateForOtherLayOut = true;
            FirebaseCrash.log("Welcome: Activity Restored");
            Crashlytics.log("Welcome: Activity Restored");
            Log.i(TAG,"Welcome: Activity Restored");
            String cameraString = preferences
                    .getString("mCurrentPhotoPathForTakingImage",DEFAULT);
            if (!cameraString.equals("N/A")){
                restoreBySavedInStanceStateForCameraProblem = true;
                mCurrentPhotoPathForTakingImage = cameraString;
            }

            String galleryString = preferences
                    .getString("mCurrentPhotoPathForTakingImageFromGallery",DEFAULT);
            if (!galleryString.equals("N/A")){
                restoreBySavedInStanceStateForCameraProblem = true;
                mCurrentPhotoPathForTakingImageFromGallery = galleryString;
            }
            setUpTheAppropriateLayoutIfViewGetDestroyed(savedInstanceState);

        } else {
            //else means we are initializing our app for the first time
            //Now we will check whether we have visited slideractivity or not
            String sliding_Status = preferences.getString("Sliding_Status",DEFAULT);
            if (!sliding_Status.equals(DEFAULT)){
                //Tis means we have completed slideractivity
                //Set up all the views here
                initActivityWideVariable();
                setUpAllTheMajorViews();
                setUpAllTheViewsFromWelcomeLayout();
                setUpAllTheViewsFromSignUpForCarLayout();
                setUpAllTheViewsFromSignUpForBikeLayout();
            }

        }
        FirebaseCrash.log("Welcome: onCreate() layout created");
        Crashlytics.log("Welcome: onCreate() layout created");


        putObserverInBasicInfoTable();



    }


    private void putObserverInBasicInfoTable() {
        mViewModel.mBasicInfo.observe(this, new Observer<List<BasicInfo>>() {
            @Override
            public void onChanged(@Nullable List<BasicInfo> basicInfos) {
                if (basicInfos != null){
                    Log.i("WelcomeViewModel",
                            "BasicInfo List Size: " + basicInfos.size());
                    if (basicInfos.size() == 0){
                        isDriverLoggedInThisDeviceForFirstTime = true;
                    } else if (basicInfos.size() == 1){
                        isDriverLoggedInThisDeviceForFirstTime = false;
                    }
                    if (basicInfos.size() != 0){
                        BasicInfo basicInfo = basicInfos.get(0);
                        Log.i("WelcomeViewModel",
                                "BasicInfo Object 0: " + basicInfos.toString());
                    }

                } else if (basicInfos == null){
                    Log.i("WelcomeViewModel",
                            "BasicInfo == null ");
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState, PersistableBundle outPersistentState) {
        if (mCurrentPhotoPathForTakingImage != null
                && !mCurrentPhotoPathForTakingImage.equals("")){
            savedInstanceState.putString("mCurrentPhotoPathForTakingImage",
                    mCurrentPhotoPathForTakingImage);
            savedInstanceState.putString("imageTakenUpTOAppUsing",
                    imageTakenUpTOAppUsing);

            Log.i(TAG,"onSaveInstanceState: " + mCurrentPhotoPathForTakingImage);
        }

        if (mCurrentPhotoPathForTakingImageFromGallery != null
                && !mCurrentPhotoPathForTakingImageFromGallery.equals("")){
            savedInstanceState.putString("mCurrentPhotoPathForTakingImageFromGallery",
                    mCurrentPhotoPathForTakingImageFromGallery);
            savedInstanceState.putString("imageTakenUpTOAppUsing",
                    imageTakenUpTOAppUsing);
            Log.i(TAG,"onSaveInstanceState: " + mCurrentPhotoPathForTakingImageFromGallery);
        }

        //Sign up for car layout
        strdriverFullNameField = driverFullNameField.getText().toString();
        strdriverNIDField = driverNIDField.getText().toString();
        strdriverLicenseField = driverLicenseField.getText().toString();
        strdriverHomeAddressField = driverHomeAddressField.getText().toString();
        strcarRegistrationAlphabet = carRegistrationAlphabet.getText().toString();
        strcarRegistrationFirstTwoDigit = carRegistrationFirstTwoDigit.getText().toString();
        strcarRegistrationLastFourDigit = carRegistrationLastFourDigit.getText().toString();
        strcarFitnessCertificateNumber = carFitnessCertificateNumber.getText().toString();
        strcarTaxTokenNumber = carTaxTokenNumber.getText().toString();
        strreferralCode = referralCode.getText().toString();

        savedInstanceState.putString("strdriverFullNameField", strdriverFullNameField);
        savedInstanceState.putString("strdriverNIDField", strdriverNIDField);
        savedInstanceState.putString("strdriverLicenseField", strdriverLicenseField);
        savedInstanceState.putString("strdriverHomeAddressField", strdriverHomeAddressField);
        savedInstanceState.putString("strcarRegistrationAlphabet", strcarRegistrationAlphabet);
        savedInstanceState.putString("strcarRegistrationFirstTwoDigit", strcarRegistrationFirstTwoDigit);
        savedInstanceState.putString("strcarRegistrationLastFourDigit", strcarRegistrationLastFourDigit);
        savedInstanceState.putString("strcarFitnessCertificateNumber", strcarFitnessCertificateNumber);
        savedInstanceState.putString("strcarTaxTokenNumber", strcarTaxTokenNumber);
        savedInstanceState.putString("strreferralCode", strreferralCode);

        //Choose From Gallery or Take New Photo Layout
        savedInstanceState.putString("toBeTakenImageName", toBeTakenImageName);

        //Driver Email Password Registration Layout
        strRegisterEmail = edtRegisterEmail.getText().toString();
        savedInstanceState.putString("strRegisterEmail", strRegisterEmail);

        //Driver Registration Phone Verification Layout
        strRegistrationCountryCode = edtRegistrationCountryCode.getText().toString();
        strRegistrationPhoneNumber = edtRegistrationPhoneNumber.getText().toString();
        savedInstanceState.putString("strRegistrationCountryCode", strRegistrationCountryCode);
        savedInstanceState.putString("strRegistrationPhoneNumber", strRegistrationPhoneNumber);


        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState, outPersistentState);
    }


    private void initActivityWideVariable() {
        //Ask for android run time permission, by creating object from Permissions.class
        if (!checkHowManyPermissionAllowed(Welcome.this)){
            requestAllPermission(Welcome.this);
        }
        waitingDialogSeverConnection = new SpotsDialog(Welcome.this,
                "Connecting To Server");
        waitingDialogForPhoneVerification = new SpotsDialog(Welcome.this,
                "Auto Verification Is Going On");
        waitingDialogFaceBookSeverConnection = new SpotsDialog(Welcome.this,
                "Verifying User With FaceBook Server");


    }

    private void setUpTheAppropriateLayoutIfViewGetDestroyed(Bundle savedInstanceState) {
        setUpAllTheMajorViews();
        String beforeDestroyedViewLayOutWas = preferences.getString("Layout",DEFAULT);
        if (restoreBySavedInStanceStateForOtherLayOut){
            restoreBySavedInStanceStateForOtherLayOut = false;
            Log.i(TAG,"beforeDestroyedViewLayOutWas: " + beforeDestroyedViewLayOutWas);
            switch (beforeDestroyedViewLayOutWas){
                case "welcome_layout": //Check whether this name changed
                    welcome_layout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromWelcomeLayout();
                    Log.i(TAG,"Layout Restored:welcome_layout");
                    break;
                case "driverOrBikerRegistrationPhoneVerificationLayout": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromDriverRegistrationPhoneVerificationLayout();
                    strRegistrationCountryCode = preferences
                            .getString("strRegistrationCountryCode",DEFAULT);
                    strRegistrationPhoneNumber = preferences
                            .getString("strRegistrationPhoneNumber",DEFAULT);
                    edtRegistrationCountryCode.setText(strRegistrationCountryCode);
                    edtRegistrationPhoneNumber.setText(strRegistrationPhoneNumber);
                    Log.i(TAG,"Layout Restored:driverOrBikerRegistrationPhoneVerificationLayout");
                    break;

                case "driverOrBikerRegistrationOTPLayOut": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    driverOrBikerRegistrationOTPLayOut.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromDriverRegistrationOTPLayout();
                    Log.i(TAG,"Layout Restored:driverOrBikerRegistrationOTPLayOut");
                    break;
                case "select_city_layout": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    select_city_layout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromSelectYourCityLayout();
                    Log.i(TAG,"Layout Restored:select_city_layout");
                    break;
                case "select_vehicle_type_layout": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    select_vehicle_type_layout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromSelectVehicleTypeLayout();
                    Log.i(TAG,"Layout Restored:select_vehicle_type_layout");
                    break;
                case "driverOrBikerSignInUsingEmailPasswordLayOut": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    driverOrBikerSignInUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromDriverSignInLayout();
                    Log.i(TAG,"Layout Restored:driverOrBikerSignInUsingEmailPasswordLayOut");
                    break;
                case "driverOrBikerRegisterUsingEmailPasswordLayOut": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    driverOrBikerRegisterUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromDriverEmailPasswordRegistrationLayout();
                    strRegisterEmail = preferences
                            .getString("strRegisterEmail",DEFAULT);
                    edtRegisterEmail.setText(strRegisterEmail);
                    Log.i(TAG,"Layout Restored:driverOrBikerRegisterUsingEmailPasswordLayOut");
                    break;
                case "signUpForCarLayout": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    signUpForCarLayout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromSignUpForCarLayout();
                    strdriverFullNameField = preferences
                            .getString("strdriverFullNameField",DEFAULT);
                    strdriverNIDField = preferences
                            .getString("strdriverNIDField",DEFAULT);
                    strdriverLicenseField = preferences
                            .getString("strdriverLicenseField",DEFAULT);
                    strdriverHomeAddressField = preferences
                            .getString("strdriverHomeAddressField",DEFAULT);
                    strcarRegistrationAlphabet = preferences
                            .getString("strcarRegistrationAlphabet",DEFAULT);
                    strcarRegistrationFirstTwoDigit = preferences
                            .getString("strcarRegistrationFirstTwoDigit",DEFAULT);
                    strcarRegistrationLastFourDigit = preferences
                            .getString("strcarRegistrationLastFourDigit",DEFAULT);
                    strcarFitnessCertificateNumber = preferences
                            .getString("strcarFitnessCertificateNumber",DEFAULT);
                    strcarTaxTokenNumber = preferences
                            .getString("strcarTaxTokenNumber",DEFAULT);
                    strreferralCode = preferences
                            .getString("strreferralCode",DEFAULT);

                    driverFullNameField.setText(strdriverFullNameField);
                    driverNIDField.setText(strdriverNIDField);
                    driverLicenseField.setText(strdriverLicenseField);
                    driverHomeAddressField.setText(strdriverHomeAddressField);
                    carRegistrationAlphabet.setText(strcarRegistrationAlphabet);
                    carRegistrationFirstTwoDigit.setText(strcarRegistrationFirstTwoDigit);
                    carRegistrationLastFourDigit.setText(strcarRegistrationLastFourDigit);
                    carFitnessCertificateNumber.setText(strcarFitnessCertificateNumber);
                    carTaxTokenNumber.setText(strcarTaxTokenNumber);
                    referralCode.setText(strreferralCode);
                    Log.i(TAG,"Layout Restored:signUpForCarLayout");
                    break;

                case "signUpForBikeLayout": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    signUpForBikeLayout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromSignUpForBikeLayout();
                    strBikerFullNameField = preferences
                            .getString("strBikerFullNameField",DEFAULT);
                    strBikerNIDField = preferences
                            .getString("strBikerNIDField",DEFAULT);
                    strBikerLicenseField = preferences
                            .getString("strBikerLicenseField",DEFAULT);
                    strBikerHomeAddressField = preferences
                            .getString("strBikerHomeAddressField",DEFAULT);
                    strBikeRegistrationAlphabet = preferences
                            .getString("strBikeRegistrationAlphabet",DEFAULT);
                    strBikeRegistrationFirstTwoDigit = preferences
                            .getString("strBikeRegistrationFirstTwoDigit",DEFAULT);
                    strBikeRegistrationLastFourDigit = preferences
                            .getString("strBikeRegistrationLastFourDigit",DEFAULT);
                    strBikeReferralCode = preferences
                            .getString("strBikeReferralCode",DEFAULT);

                    bikerFullNameField.setText(strBikerFullNameField);
                    bikerNIDField.setText(strBikerNIDField);
                    bikerLicenseField.setText(strBikerLicenseField);
                    bikerHomeAddressField.setText(strBikerHomeAddressField);
                    bikeRegistrationAlphabet.setText(strBikeRegistrationAlphabet);
                    bikeRegistrationFirstTwoDigit.setText(strBikeRegistrationFirstTwoDigit);
                    bikeRegistrationLastFourDigit.setText(strBikeRegistrationLastFourDigit);
                    bikeReferralCode.setText(strBikeReferralCode);
                    Log.i(TAG,"Layout Restored:signUpForBikeLayout");
                    break;

                case "scannedDocumentsUploadLayoutForCar": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromScannedDocumentlayoutForCar();
                    putCheckMarkToUploadedDocumentIfViewsGetDestroyed();
                    Log.i(TAG,"Layout Restored:scannedDocumentsUploadLayoutForCar");
                    break;

                case "scannedDocumentsUploadLayoutForBike": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromScannedDocumentlayoutForBike();
                    putCheckMarkToUploadedDocumentIfViewsGetDestroyed();
                    Log.i(TAG,"Layout Restored:scannedDocumentsUploadLayoutForBike");
                    break;

                case "previewAndUploadLayout": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    previewAndUploadLayout.setVisibility(View.VISIBLE);
                    initViewsFromPreviewDocumentsAndUploadLayout();
                    setUpAllTheViewsFromChooseFromGalleryOrTakePhoto();
                    String vehicle_type =  preferences.getString("Vehicle_Type",DEFAULT);
                    if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Car")){
                        setUpAllTheViewsFromScannedDocumentlayoutForCar();
                    } else if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Bike")){
                        setUpAllTheViewsFromScannedDocumentlayoutForBike();
                    }
                    Log.i(TAG,"Layout Restored:previewAndUploadLayout");
                    break;

                case "chooseFromGalleryOrTakePhoto": //Check whether this name changed
                    welcome_layout.setVisibility(View.GONE);
                    chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromChooseFromGalleryOrTakePhoto();
                    Log.i(TAG,"Layout Restored:chooseFromGalleryOrTakePhoto");
                    break;
            }
        }

    }

    private void setUpAllTheMajorViews() {
        welcome_layout = findViewById(R.id.welcome_layout);
        driverOrBikerRegistrationPhoneVerificationLayout = findViewById(R.id.driverRegistrationPhoneVerificationLayout);
        driverOrBikerRegistrationOTPLayOut = findViewById(R.id.driverRegistrationOTPLayOut);
        previewAndUploadLayout = findViewById(R.id.previewAndUploadLayout);
        select_city_layout = findViewById(R.id.select_city_layout);
        select_vehicle_type_layout = findViewById(R.id.select_vehicle_type_layout);
        driverOrBikerSignInUsingEmailPasswordLayOut = findViewById(R.id.driverSignInUsingEmailPasswordLayOut);
        driverOrBikerRegisterUsingEmailPasswordLayOut = findViewById(R.id.driverRegisterUsingEmailPasswordLayOut);
        signUpForCarLayout = findViewById(R.id.signUpForCarLayout);
        scannedDocumentsUploadLayoutForCar = findViewById(R.id.scannedDocumentsUploadLayout);
        signUpForBikeLayout = findViewById(R.id.signUpForBikeLayout);
        scannedDocumentsUploadLayoutForBike = findViewById(R.id.scannedDocumentsUploadLayoutForBike);
        chooseFromGalleryOrTakePhoto = findViewById(R.id.chooseFromGalleryOrTakePhoto);
        addbKashNumber = findViewById(R.id.addbKashNumber);

//        welcome_layout.setVisibility(View.VISIBLE);

    }

    private void setUpAllTheViewsFromWelcomeLayout() {
        btnDriverRegistration = findViewById(R.id.btnDriverRegistration);
        btnDriverLogInWelcome = findViewById(R.id.btnDriverLogInWelcome);

        btnDriverRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnDriverRegistration.onClick");
                if (!checkHowManyPermissionAllowed(Welcome.this)){
                    requestAllPermission(Welcome.this);
                }
                if (checkGPSConnectionPermission() && checkAndRequestSMSPermissions()){
                    welcome_layout.setVisibility(View.GONE);
                    driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromDriverRegistrationPhoneVerificationLayout();
                    Runtime.getRuntime().gc();
                    editor.putString("Layout", "driverOrBikerRegistrationPhoneVerificationLayout");
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    FirebaseCrash.log("Welcome: layOut-driverOrBikerRegistrationPhoneVerificationLayout");

                        //
                } else {
                    Toast.makeText(Welcome.this,"You must provide all the permission" +
                            " before proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDriverLogInWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnDriverLogInWelcome.onClick");
                if (!checkHowManyPermissionAllowed(Welcome.this)){
                    requestAllPermission(Welcome.this);
                }
                if (checkGPSConnectionPermission() && checkAndRequestSMSPermissions()){
                    welcome_layout.setVisibility(View.GONE);
                    driverOrBikerSignInUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                    setUpAllTheViewsFromDriverSignInLayout();
                    Runtime.getRuntime().gc();
                    editor.putString("Layout", "driverOrBikerSignInUsingEmailPasswordLayOut");
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    FirebaseCrash.log("Welcome: layOut-driverOrBikerRegistrationPhoneVerificationLayout");
                    mVerificationStage = "driverOrBikerRegistrationPhoneVerificationLayout";

                } else {
                    Toast.makeText(Welcome.this,"You must provide all the permission" +
                            " before proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void setUpAllTheViewsFromDriverSignInLayout() {
        ic_back_sign9 = findViewById(R.id.ic_back_sign9);
        edtSignInEmail = findViewById(R.id.edtSignInEmail);
        edtSignInPassword = findViewById(R.id.edtSignInPassword);
        btnSignInUsingEmailPassword = findViewById(R.id.btnSignInUsingEmailPassword);
        btnBackToAllAccountPage1 = findViewById(R.id.btnBackToAllAccountPage1);

        ic_back_sign9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcome_layout.setVisibility(View.VISIBLE);
                driverOrBikerSignInUsingEmailPasswordLayOut.setVisibility(View.GONE);
                editor.putString("Layout", "welcome_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });
        waitingDialogForSignIn = new SpotsDialog(this,"Signing In");
        btnSignInUsingEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String driverEmail = edtSignInEmail.getText().toString();
                final String driverPassword = edtSignInPassword.getText().toString();
                //Sign in existing driver with email & password.
                waitingDialogForSignIn.show();
                mDriverAuth.signInWithEmailAndPassword(driverEmail, driverPassword).
                        addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()){
                                    //If task is not successful
                                    Toast.makeText(Welcome.this,"Sign In Error: " + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                    Log.i(TAG,"Sign In Error: "  +
                                            task.getException().getMessage());
                                    waitingDialogForSignIn.dismiss();
                                } else {
                                    waitingDialogForSignIn.dismiss();
                                    Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    if (isDriverLoggedInThisDeviceForFirstTime){
                                        //this means user logged in for the first time
                                        editor.putString("Registration_Status","Completed");
                                        editor.apply();
                                        Log.i("WelcomeViewModel","User Logged in for the first time");
                                        BasicInfo basicInfo = new BasicInfo();
                                        basicInfo.setDriverUid(Common.driverUID );
                                        mViewModel.addBasicInfo(basicInfo);
                                        fetchDriverOrBikerDataFromServer();
                                    } else {
                                        Log.i("WelcomeViewModel","User has previous data");
                                        //We check if our user is driver / biker
                                        getDriverOrBiker();
                                    }

                                }
                            }
                        });

            }
        });

        btnBackToAllAccountPage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcome_layout.setVisibility(View.VISIBLE);
                driverOrBikerSignInUsingEmailPasswordLayOut.setVisibility(View.GONE);
                editor.putString("Layout", "welcome_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });
    }

    private void setUpAllTheViewsFromDriverRegistrationPhoneVerificationLayout() {
        ic_back_sign8 = findViewById(R.id.ic_back_sign8);
        edtRegistrationCountryCode = findViewById(R.id.edtRegistrationCountryCode);
        edtRegistrationPhoneNumber = findViewById(R.id.edtRegistrationPhoneNumber);
        btnVerifyRegistrationPhoneNumber = findViewById(R.id.btnVerifyRegistrationPhoneNumber);

        ic_back_sign8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                welcome_layout.setVisibility(View.VISIBLE);
                driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                editor.putString("Layout", "welcome_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnVerifyRegistrationPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnVerifyRegistrationPhoneNumber.onClick");
                if (checkGPSConnectionPermission() && checkAndRequestSMSPermissions()){
                    setUpAllTheViewsFromDriverRegistrationOTPLayout();
                    startDriverRegistrationProcess();
                } else {
                    Toast.makeText(Welcome.this,"You must provide all the permission" +
                            " before proceed",Toast.LENGTH_LONG).show();
                }
            }
        });

        //This call back is for register using PhoneAuth Verification.
        mRegisterCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                try {
                    if (waitingDialogForPhoneVerification.isShowing()){
                        waitingDialogForPhoneVerification.dismiss();
                    }
                } catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                }
                FirebaseCrash.log("Welcome: NewUser Registration: onVerificationCompleted().called");
                Toast.makeText(Welcome.this, "Verification Complete", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onVerificationCompleted:" + phoneAuthCredential);

                signInWithPhoneAuthCredentialForNewUser(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                Toast.makeText(Welcome.this, "Verification Failed", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...
                    FirebaseCrash.log("Welcome: SMS quota finished");
                    Toast.makeText(Welcome.this, "InValid Phone Number", Toast.LENGTH_SHORT).show();
                }
                try {
                    if (waitingDialogForPhoneVerification.isShowing()){
                        waitingDialogForPhoneVerification.dismiss();
                    }
                } catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                }

                // Show a message and update the UI
                // ...

            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                FirebaseCrash.log("Welcome: Registration onCodeSent.called");
                Log.i(TAG, "onCodeSent:" + verificationId);
                Toast.makeText(Welcome.this, "Verification code has been send on your number", Toast.LENGTH_SHORT).show();
                // Save verification ID and resending token so we can use them later
                userRegistrationVerificationId = verificationId;
                mRegisterResendToken = forceResendingToken;
                //Now change the layout
                //Bring 'enterOTPLayout
                driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                driverOrBikerRegistrationOTPLayOut.setVisibility(View.VISIBLE);
                editor.putString("Layout", "driverOrBikerRegistrationOTPLayOut");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
                FirebaseCrash.log("Welcome: layOut-driverOrBikerRegistrationOTPLayOut");
                mVerificationStage = "driverOrBikerRegistrationOTPLayOut";

            }
        };
    }

    private void setUpAllTheViewsFromDriverRegistrationOTPLayout() {
        ic_back_sign4 = findViewById(R.id.ic_back_sign4);
        edtRegistrationEnterOTP = findViewById(R.id.edtRegistrationEnterOTP);
        btnVerifyRegistrationOTPNumber = findViewById(R.id.btnVerifyRegistrationOTPNumber);

        ic_back_sign4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.VISIBLE);
                driverOrBikerRegistrationOTPLayOut.setVisibility(View.GONE);
                editor.putString("Layout", "driverOrBikerRegistrationPhoneVerificationLayout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnVerifyRegistrationOTPNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome: btnVerifyRegistrationOTPNumber.onClick");
                FirebaseCrash.log("Welcome: OTP entered manually");
                String sentCode = edtRegistrationEnterOTP.getText().toString();
                if (!sentCode.equals("")){
                    FirebaseCrash.log("Welcome: edtRegistrationEnterOTP: " + sentCode);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential
                            (userRegistrationVerificationId, sentCode);
                    // [END verify_with_code]
                    signInWithPhoneAuthCredentialForNewUser(credential);
                } else {
                    Toast.makeText(Welcome.this,"Please enter verification code",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setUpAllTheViewsFromSelectYourCityLayout() {
        ic_back_sign = findViewById(R.id.ic_back_sign);
        btnSelectDhaka = findViewById(R.id.btnSelectDhaka);
        btnSelectChittagong = findViewById(R.id.btnSelectChittagong);
        btnSelectSylhet = findViewById(R.id.btnSelectSylhet);

        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setDriverUid(Common.driverUID);
        mViewModel.addBasicInfo(basicInfo);

        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Welcome.this, "Back press disabled", Toast.LENGTH_SHORT).show();
            }
        });

        btnSelectDhaka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_city_layout.setVisibility(View.GONE);
                select_vehicle_type_layout.setVisibility(View.VISIBLE);
                setUpAllTheViewsFromSelectVehicleTypeLayout();
                editor.putString("Registration_City","Dhaka");
                editor.apply();
                editor.putString("Layout", "select_vehicle_type_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnSelectChittagong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_city_layout.setVisibility(View.GONE);
                select_vehicle_type_layout.setVisibility(View.VISIBLE);
                setUpAllTheViewsFromSelectVehicleTypeLayout();
                editor.putString("Registration_City","Chittagong");
                editor.apply();
                editor.putString("Layout", "select_vehicle_type_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnSelectSylhet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_city_layout.setVisibility(View.GONE);
                select_vehicle_type_layout.setVisibility(View.VISIBLE);
                setUpAllTheViewsFromSelectVehicleTypeLayout();
                editor.putString("Registration_City","Sylhet");
                editor.apply();
                editor.putString("Layout", "select_vehicle_type_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });
    }

    private void setUpAllTheViewsFromSelectVehicleTypeLayout() {
        ic_back_sign1 = findViewById(R.id.ic_back_sign1);
        btnSelectCar = findViewById(R.id.btnSelectCar);
        btnSelectBike = findViewById(R.id.btnSelectBike);

        ic_back_sign1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select_vehicle_type_layout.setVisibility(View.GONE);
                select_city_layout.setVisibility(View.VISIBLE);
                editor.putString("Layout", "select_city_layout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnSelectCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                builder.setMessage("Please confirm again.\nDo you want to sign up for Car?");
                builder.setTitle("SIGN UP FOR CAR");
                builder.setPositiveButton("Car Sign Up",
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        select_vehicle_type_layout.setVisibility(View.GONE);
                        driverOrBikerRegisterUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                        setUpAllTheViewsFromDriverEmailPasswordRegistrationLayout();
                        editor.putString("Vehicle_Type","Car");
                        editor.commit();
                        Runtime.getRuntime().gc();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

            }
        });

        btnSelectBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                builder.setMessage("Please confirm again.\nDo you want to sign up for Bike?");
                builder.setTitle("SIGN UP FOR BIKE");
                builder.setPositiveButton("Bike Sign Up",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                select_vehicle_type_layout.setVisibility(View.GONE);
                                driverOrBikerRegisterUsingEmailPasswordLayOut.setVisibility(View.VISIBLE);
                                setUpAllTheViewsFromDriverEmailPasswordRegistrationLayout();
                                editor.putString("Vehicle_Type","Bike");
                                editor.commit();
                                Runtime.getRuntime().gc();
                            }
                        });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
            }
        });
    }

    private void setUpAllTheViewsFromDriverEmailPasswordRegistrationLayout() {
        ic_back_sign6 = findViewById(R.id.ic_back_sign6);
        edtRegisterEmail = findViewById(R.id.edtRegisterEmail);
        edtRegisterPassword = findViewById(R.id.edtRegisterPassword);
        btnRegistrationUsingEmailPassword = findViewById(R.id.btnRegistrationUsingEmailPassword);

        ic_back_sign6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Welcome.this, "Back Press Will Not Work",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnRegistrationUsingEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attachEmailPasswordSignIn();
            }
        });

    }

    private void setUpAllTheViewsFromSignUpForCarLayout() {
        setUpSpinnerForCar();
        setUpTabLayOutForCar();
        //EditText
        driverFullNameField = findViewById(R.id.driverFullNameField);
        driverNIDField = findViewById(R.id.driverNIDField);
        driverLicenseField = findViewById(R.id.driverLicenseField);
        driverHomeAddressField = findViewById(R.id.driverHomeAddressField);
        carRegistrationAlphabet = findViewById(R.id.carRegistrationAlphabet);
        carRegistrationFirstTwoDigit = findViewById(R.id.carRegistrationFirstTwoDigit);
        carRegistrationLastFourDigit = findViewById(R.id.carRegistrationLastFourDigit);
        carFitnessCertificateNumber = findViewById(R.id.carFitnessCertificateNumber);
        carTaxTokenNumber = findViewById(R.id.carTaxTokenNumber);
        referralCode = findViewById(R.id.referralCode);

        //ImageView
        ic_back_sign2 = findViewById(R.id.ic_back_sign2);
        ic_back_sign2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Welcome.this, "Back Press Will Not Work",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //CircleImageView
        driverProfilePic =findViewById(R.id.driverProfilePic);
        driverProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome:driverProfilePic.onClick");
                Crashlytics.log("Welcome:driverProfilePic.onClick");
                if (!checkHowManyPermissionAllowed(Welcome.this)){
                    requestAllPermission(Welcome.this);
                }
                //We will direct our driver to gallery to choose his profile pic from
                //gallery.
                //As usual we will use Intent to direct our customer to gallery.
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //User will only be allowed to pick up image.
                startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY_ACCESS_FOR_DRIVER_PROFILE_PIC);
                //Instead of 'startActivity' we used 'startActivityForResult'. because 'startActivity'
                //will only open gallery.But we want to do something after opening gallery, so we used
                //'startActivityForResult'.
                //As we have used 'startActivityForResult' than we have to override one method
                //which is 'onActivityResult()' method.
                //In 'onActivityResult()' method, we will get our user image through an intent.
            }
        });

        //Button
        btnGoToUploadDocumentPageForCar = findViewById(R.id.btnGoToUploadDocumentPageForCar);
        btnGoToUploadDocumentPageForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic != null){
                    if (!driverFullNameField.getText().toString().equals("")){
                        uploadDriverAndCarInfo();
                    } else if (driverFullNameField.getText().toString().equals("")){
                        Toast.makeText(Welcome.this, "Please insert your name",
                                Toast.LENGTH_LONG).show();
                    }
                } else if (mCurrentPhotoPathForTakingImageFromGallery == null
                        && !driverFullNameField.getText().toString().equals("")){
                    Toast.makeText(Welcome.this, "Please select photo " +
                            "from device", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void setUpAllTheViewsFromSignUpForBikeLayout() {
        setUpSpinnerForBike();
        setUpTabLayOutForBike();
        //EditText
        bikerFullNameField = findViewById(R.id.bikerFullNameField);
        bikerNIDField = findViewById(R.id.bikerNIDField);
        bikerLicenseField = findViewById(R.id.bikerLicenseField);
        bikerHomeAddressField = findViewById(R.id.bikerHomeAddressField);
        bikeRegistrationAlphabet = findViewById(R.id.bikeRegistrationAlphabet);
        bikeRegistrationFirstTwoDigit = findViewById(R.id.bikeRegistrationFirstTwoDigit);
        bikeRegistrationLastFourDigit = findViewById(R.id.bikeRegistrationLastFourDigit);
        bikeReferralCode = findViewById(R.id.bikeReferralCode);

        //ImageView
        ic_back_sign14 = findViewById(R.id.ic_back_sign14);
        ic_back_sign14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Welcome.this, "Back Press Will Not Work",
                        Toast.LENGTH_SHORT).show();
            }
        });

        //CircleImageView
        bikerProfilePic =findViewById(R.id.bikerProfilePic);
        bikerProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Welcome:bikerProfilePic.onClick");
                Crashlytics.log("Welcome:bikerProfilePic.onClick");
                if (!checkHowManyPermissionAllowed(Welcome.this)){
                    requestAllPermission(Welcome.this);
                }
                //We will direct our driver to gallery to choose his profile pic from
                //gallery.
                //As usual we will use Intent to direct our customer to gallery.
                Intent intent = new Intent(Intent.ACTION_PICK);
                //'Intent.ACTION_PICK' this means this intent will help our customer to pickup something.
                //what customer will pickup, we will decide in the next line
                intent.setType("image/*");
                //User will only be allowed to pick up image.
                startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY_ACCESS_FOR_BIKER_PROFILE_PIC);
                //Instead of 'startActivity' we used 'startActivityForResult'. because 'startActivity'
                //will only open gallery.But we want to do something after opening gallery, so we used
                //'startActivityForResult'.
                //As we have used 'startActivityForResult' than we have to override one method
                //which is 'onActivityResult()' method.
                //In 'onActivityResult()' method, we will get our user image through an intent.
            }
        });

        //Button
        btnGoToUploadDocumentPageForBike = findViewById(R.id.btnGoToUploadDocumentPageForBike);
        btnGoToUploadDocumentPageForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic != null
                        && !bikerFullNameField.getText().toString().equals("")){
                    uploadBikerAndBikeInfo();
                } else if (mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic==null){
                    Toast.makeText(Welcome.this, "Please upload Photo",
                            Toast.LENGTH_LONG).show();
                } else if (bikerFullNameField.getText().toString().equals("")){
                    Toast.makeText(Welcome.this, "Please insert your name",
                            Toast.LENGTH_LONG).show();
                }


            }
        });
    }

    private void setUpAllTheViewsFromScannedDocumentlayoutForCar() {
        //LinearLayout
        scannedDrivingLicenseLayoutForCar = findViewById(R.id.scannedDrivingLicenseLayoutForCar);
        nidFrontSideLayoutForCar = findViewById(R.id.nidFrontSideLayoutForCar);
        nidBackSideLayoutForCar = findViewById(R.id.nidBackSideLayoutForCar);
        carFitnessCertificateLayout = findViewById(R.id.carFitnessCertificateLayout);
        carTaxTokenLayout = findViewById(R.id.carTaxTokenLayout);
        carRegistrationPaperLayout = findViewById(R.id.carRegistrationPaperLayout);

        imgDocumentUploadStatusDrivingLicenseForCar = findViewById
                (R.id.imgDocumentUploadStatusDrivingLicenseForCar);
        imgDocumentUploadStatusNIDFrontSideForCar = findViewById
                (R.id.imgDocumentUploadStatusNIDFrontSideForCar);
        imgDocumentUploadStatusNIDBackSideForCar = findViewById
                (R.id.imgDocumentUploadStatusNIDBackSideForCar);
        imgDocumentUploadStatusCarFitness = findViewById
                (R.id.imgDocumentUploadStatusCarFitness);
        imgDocumentUploadStatusCarTaxToken = findViewById
                (R.id.imgDocumentUploadStatusCarTaxToken);
        imgDocumentUploadStatusCarRegistration = findViewById
                (R.id.imgDocumentUploadStatusCarRegistration);

        btnAddPaymentDetailsForCar = findViewById(R.id.btnAddPaymentDetails);

        scannedDrivingLicenseLayoutForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "Driving_License";
                txtScannedPhotoName.setText("Driving License");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "Driving_License");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        nidFrontSideLayoutForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "NID_FRONT";
                txtScannedPhotoName.setText("NID FRONT");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "NID_FRONT");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        nidBackSideLayoutForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "NID_BACK";
                txtScannedPhotoName.setText("NID BACK");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "NID_BACK");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        carFitnessCertificateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "FITNESS_CERTIFICATE";
                txtScannedPhotoName.setText("FITNESS CERTIFICATE");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "FITNESS_CERTIFICATE");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        carTaxTokenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "TAX_TOKEN";
                txtScannedPhotoName.setText("TAX TOKEN");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "TAX_TOKEN");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        carRegistrationPaperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "REGISTRATION_PAPER";
                txtScannedPhotoName.setText("REGISTRATION PAPER");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "REGISTRATION_PAPER");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnAddPaymentDetailsForCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addbKashNumber.setVisibility(View.VISIBLE);
                setUpAllTheViewsFromAddbKashNumber();
                scannedDocumentsUploadLayoutForCar.setVisibility(View.GONE);
                editor.putString("Layout", "addbKashNumber");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
                destroyPreviouslyCreatedView1();
            }
        });
        initViewsFromPreviewDocumentsAndUploadLayout();
        setUpAllTheViewsFromChooseFromGalleryOrTakePhoto();
    }

    private void setUpAllTheViewsFromScannedDocumentlayoutForBike() {
        //LinearLayout
        scannedDrivingLicenseLayoutForBike = findViewById
                (R.id.scannedDrivingLicenseLayoutForBike);
        nidFrontSideLayoutForBike = findViewById
                (R.id.nidFrontSideLayoutForBike);
        nidBackSideLayoutForBike = findViewById
                (R.id.nidBackSideLayoutForBike);
        bikeRegistrationPaperLayout = findViewById
                (R.id.bikeRegistrationPaperLayout);

        imgDocumentUploadStatusDrivingLicenseForBike = findViewById
                (R.id.imgDocumentUploadStatusDrivingLicenseForBike);
        imgDocumentUploadStatusNIDFrontSideForBike = findViewById
                (R.id.imgDocumentUploadStatusNIDFrontSideForBike);
        imgDocumentUploadStatusNIDBackSideForBike = findViewById
                (R.id.imgDocumentUploadStatusNIDBackSideForBike);
        imgDocumentUploadStatusBikeRegistration = findViewById
                (R.id.imgDocumentUploadStatusBikeRegistration);

        btnAddPaymentDetailsForBike = findViewById(R.id.btnAddPaymentDetailsForBike);

        scannedDrivingLicenseLayoutForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForBike.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "Driving_License";
                txtScannedPhotoName.setText("Driving License");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "Driving_License");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        nidFrontSideLayoutForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForBike.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "NID_FRONT";
                txtScannedPhotoName.setText("NID FRONT");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "NID_FRONT");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        nidBackSideLayoutForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForBike.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "NID_BACK";
                txtScannedPhotoName.setText("NID BACK");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "NID_BACK");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });


        bikeRegistrationPaperLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannedDocumentsUploadLayoutForBike.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                toBeTakenImageName = "REGISTRATION_PAPER";
                txtScannedPhotoName.setText("REGISTRATION PAPER");
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.putString("toBeTakenImageName", "REGISTRATION_PAPER");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnAddPaymentDetailsForBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addbKashNumber.setVisibility(View.VISIBLE);
                scannedDocumentsUploadLayoutForBike.setVisibility(View.GONE);
                editor.putString("Layout", "addbKashNumber");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
                destroyPreviouslyCreatedView1();
            }
        });
        initViewsFromPreviewDocumentsAndUploadLayout();
        setUpAllTheViewsFromChooseFromGalleryOrTakePhoto();

    }

    private void setUpAllTheViewsFromChooseFromGalleryOrTakePhoto() {
        ic_back_sign12 = findViewById(R.id.ic_back_sign12);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnUploadFromGallery = findViewById(R.id.btnUploadFromGallery);

        ic_back_sign12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                String chosen_vehicle = preferences.getString("Vehicle_Type",DEFAULT);
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Car")){
                    scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                    editor.putString("Layout", "scannedDocumentsUploadLayoutForCar");
                }
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Bike")){
                    scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                    editor.putString("Layout", "scannedDocumentsUploadLayoutForBike");
                }
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(toBeTakenImageName);
                editor.putString("Layout", "previewAndUploadLayout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
                Toast.makeText(Welcome.this, toBeTakenImageName, Toast.LENGTH_SHORT).show();
            }
        });

        btnUploadFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("Layout", "previewAndUploadLayout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE_FOR_GALLERY_ACCESS);
            }
        });
    }

    private void initViewsFromPreviewDocumentsAndUploadLayout() {
        previewAndUploadLayout = findViewById(R.id.previewAndUploadLayout);
        imgScannedDocumentShownByGlide = findViewById(R.id.imgScannedDocumentShownByGlide);
//        imgScannedDocumentShownByFresco = findViewById(R.id.imgScannedDocumentShownByFresco);
        ic_back_sign3 = findViewById(R.id.ic_back_sign3);
        txtScannedPhotoName = findViewById(R.id.txtScannedPhotoName);
        btnDiscardPhoto = findViewById(R.id.btnDiscardPhoto);
        btnUploadPhotoToServer = findViewById(R.id.btnUploadPhotoToServer);

        ic_back_sign3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previewAndUploadLayout.setVisibility(View.GONE);
                chooseFromGalleryOrTakePhoto.setVisibility(View.VISIBLE);
                btnUploadPhotoToServer.setEnabled(true);
                editor.putString("Layout", "chooseFromGalleryOrTakePhoto");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnDiscardPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chosen_vehicle = preferences.getString("Vehicle_Type",DEFAULT);
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Car")){
                    scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                    editor.putString("Layout", "scannedDocumentsUploadLayoutForCar");
                }
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Bike")){
                    scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                    editor.putString("Layout", "scannedDocumentsUploadLayoutForBike");
                }
                btnUploadPhotoToServer.setEnabled(true);
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            }
        });

        btnUploadPhotoToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toBeTakenImageName == null || toBeTakenImageName.equals("")){
                    toBeTakenImageName = preferences.getString("toBeTakenImageName",DEFAULT);
                }
                btnUploadPhotoToServer.setEnabled(false);
                if (imageTakenUpTOAppUsing.equals("Camera")){
                    checkDeviceCurrentMemoryStatus();
                    sendTakenImageToServer(toBeTakenImageName,mCurrentPhotoPathForTakingImage);
                } else if (imageTakenUpTOAppUsing.equals("Gallery")){
                    checkDeviceCurrentMemoryStatus();
                    sendTakenImageToServer(toBeTakenImageName,mCurrentPhotoPathForTakingImageFromGallery);
                }

            }
        });
    }

    private void setUpAllTheViewsFromAddbKashNumber() {
        //Add bKash number
        ic_back_sign13 = findViewById(R.id.ic_back_sign13);
        edtBkashNumber = findViewById(R.id.edtBkashNumber);
        btnUploadbKashNumber = findViewById(R.id.btnUploadbKashNumber);

        ic_back_sign13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Welcome.this, "Back press will not work",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnUploadbKashNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnUploadbKashNumber.setEnabled(false);

                HashMap<String, Object> serviceType = new HashMap<>();
                serviceType.put("Service_Type","UnRegistered");
                DatabaseReference mDriverServiceType = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(Common.driverOrBiker + "s")
                        .child(Common.driverUID)
                        .child(Common.driverOrBiker + "_Service_Type");
                mDriverServiceType.setValue(serviceType);


                mFireStoreDriverBasicInfoDB.collection("Users")
                        .document(Common.driverOrBiker + "s").
                        collection(Common.driverUID)
                        .document(Common.driverOrBiker + "_Service_Type")
                        .set(serviceType);

                if (Common.driverUID == null){
                    Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }

                String chosen_vehicle = preferences.getString("Vehicle_Type",DEFAULT);
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Car")){
                    Common.driverOrBiker = "Driver";
                }
                if (!chosen_vehicle.equals(DEFAULT) &&
                        chosen_vehicle.equals("Bike")){
                    Common.driverOrBiker = "Biker";
                }

                strBkashNumber = edtBkashNumber.getText().toString();
                BankingInfo bankingInfo = new BankingInfo();
                bankingInfo.setDriverUid(Common.driverUID);
                bankingInfo.setBKash_Number(strBkashNumber);
                mViewModel.addBankingInfo(bankingInfo);

                editor.putString("Registration_Status","Completed");
                editor.putString("BKash_Number",strBkashNumber);
                editor.commit();

                HashMap<String, Object> banking = new HashMap<>();
                banking.put("BKash_Number",strBkashNumber);

                DatabaseReference mBankingInfoDB = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(Common.driverOrBiker + "s")
                        .child(Common.driverUID)
                        .child("Banking_Info");
                mBankingInfoDB.updateChildren(banking).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(TAG,"Banking Info Update Successful");
                            Log.i(TAG,"bKash Update: Successful");
                            FirebaseCrash.log("Welcome: realTimeDB for bKash_Number update: success");
                            Intent intent = new Intent(Welcome.this, Home.class);
                            startActivity(intent);
                            finish();
                        }else {
                            btnUploadbKashNumber.setEnabled(true);
                            Log.i(TAG,"bKash Update: failed");
                            FirebaseCrash.log("Welcome: realTimeDB for bKash_Number update: failed");
                        }
                    }
                });


            }
        });
    }

    private void destroyPreviouslyCreatedView() {
        destroyPreviouslyCreatedView = true;
        editor.putBoolean("destroyPreviouslyCreatedView",true);
        editor.commit();

        //Welcome layout
        btnDriverLogInWelcome = null;
        btnDriverRegistration = null;
        welcome_layout = null;

        //DriverOrBiker Sign in Layout
        ic_back_sign9 = null;
        edtSignInEmail = null;
        edtSignInPassword = null;
        btnSignInUsingEmailPassword = null;
        btnBackToAllAccountPage1 = null;
        driverOrBikerSignInUsingEmailPasswordLayOut = null;

        //DriverOrBiker Registration Phone Verification Layout
        ic_back_sign8 = null;
        edtRegistrationCountryCode = null;
        edtRegistrationPhoneNumber = null;
        strRegistrationCountryCode = null;
        strRegistrationPhoneNumber = null;
        btnVerifyRegistrationPhoneNumber = null;
        driverOrBikerRegistrationPhoneVerificationLayout = null;

        //DriverOrBiker Registration OTP Layout
        ic_back_sign4 = null;
        edtRegistrationEnterOTP = null;
        btnVerifyRegistrationOTPNumber = null;
        driverOrBikerRegistrationOTPLayOut = null;

        //Select Your City Layout
        ic_back_sign = null;
        btnSelectDhaka = null;
        btnSelectChittagong = null;
        btnSelectSylhet = null;
        select_city_layout = null;

        //Select Vehicle Type Layout
        ic_back_sign1 = null;
        btnSelectCar = null;
        btnSelectBike = null;
        select_vehicle_type_layout = null;


        //Driver Email Password Registration Layout
        ic_back_sign6 = null;
        edtRegisterEmail = null;
        edtRegisterPassword = null;
        strRegisterEmail = null;
        btnRegistrationUsingEmailPassword = null;
        driverOrBikerRegisterUsingEmailPasswordLayOut = null;



        Runtime.getRuntime().gc(); //This will invoke manual Garbage Collection.
        //Before invoking above method we have to sett all the views to null
        //So there will be no reference to those view and garbage collector will
        //be able to collect garbage.
    }

    private void destroyPreviouslyCreatedView1() {

        destroyPreviouslyCreatedView1 = true;
        editor.putBoolean("destroyPreviouslyCreatedView1",true);
        editor.commit();

        //Sign up for car layout only views
        driverFullNameField = null;
        driverNIDField = null;
        driverLicenseField = null;
        driverHomeAddressField = null;
        carRegistrationAlphabet = null;
        carRegistrationFirstTwoDigit = null;
        carRegistrationLastFourDigit = null;
        carFitnessCertificateNumber = null;
        carTaxTokenNumber = null;
        referralCode = null;
        ic_back_sign2 = null;
        if (driverProfilePic != null){
            driverProfilePic.setImageBitmap(null);
        }
        driverProfilePic = null;
        imageGalleryPathUriForDriverProfilePic = null;
        tabHostForCar = null;
        spinnerCarManufacturer = null;
        spinnerCarModel = null;
        spinnerCarProductionYear = null;
        spinnerRegistrationAuthority = null;
        btnGoToUploadDocumentPageForCar = null;

        //Sign up for bike layout only views
        bikerFullNameField = null;
        bikerNIDField = null;
        bikerLicenseField = null;
        bikerHomeAddressField = null;
        bikeRegistrationAlphabet = null;
        bikeRegistrationFirstTwoDigit = null;
        bikeRegistrationLastFourDigit = null;
        bikeReferralCode = null;
        ic_back_sign14 = null;
        if (bikerProfilePic != null){
            bikerProfilePic.setImageBitmap(null);
        }
        bikerProfilePic = null;
        imageGalleryPathUriForBikerProfilePic = null;
        tabHostForbike = null;
        spinnerBikeManufacturer = null;
        spinnerBikeModel = null;
        spinnerBikeProductionYear = null;
        spinnerBikeRegistrationAuthority = null;
        btnGoToUploadDocumentPageForBike = null;

        //Sign up for car layout only variable
        mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic = null;
        strdriverFullNameField = null;
        strdriverNIDField = null;
        strdriverLicenseField = null;
        strdriverHomeAddressField = null;
        strcarRegistrationAlphabet = null;
        strcarRegistrationFirstTwoDigit = null;
        strcarRegistrationLastFourDigit = null;
        strDriverCarRegistrationNumber = null;
        strcarFitnessCertificateNumber = null;
        strcarTaxTokenNumber = null;
        strreferralCode = null;
        strManuFacturer = null;
        strModel = null;
        strProductionYear = null;
        strRegistrationAuthority = null;


        //Sign up for bike layout only variable
        mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic = null;
        strBikerFullNameField = null;
        strBikerNIDField = null;
        strBikerLicenseField = null;
        strBikerHomeAddressField = null;
        strBikeRegistrationAlphabet = null;
        strBikeRegistrationFirstTwoDigit = null;
        strBikeRegistrationLastFourDigit = null;
        strBikeRegistrationNumber = null;
        strBikeReferralCode = null;

        //Scanned Document layout For Car
        scannedDrivingLicenseLayoutForCar = null;
        nidFrontSideLayoutForCar = null;
        nidBackSideLayoutForCar = null;
        carFitnessCertificateLayout = null;
        carTaxTokenLayout = null;
        carRegistrationPaperLayout = null;
        if (imgDocumentUploadStatusDrivingLicenseForCar !=null){
            imgDocumentUploadStatusDrivingLicenseForCar.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusNIDFrontSideForCar !=null){
            imgDocumentUploadStatusNIDFrontSideForCar.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusNIDBackSideForCar !=null){
            imgDocumentUploadStatusNIDBackSideForCar.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusCarFitness !=null){
            imgDocumentUploadStatusCarFitness.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusCarTaxToken !=null){
            imgDocumentUploadStatusCarTaxToken.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusCarRegistration !=null){
            imgDocumentUploadStatusCarRegistration.setImageDrawable(null);
        }
        imgDocumentUploadStatusDrivingLicenseForCar = null;
        imgDocumentUploadStatusNIDFrontSideForCar = null;
        imgDocumentUploadStatusNIDBackSideForCar = null;
        imgDocumentUploadStatusCarFitness = null;
        imgDocumentUploadStatusCarTaxToken = null;
        imgDocumentUploadStatusCarRegistration = null;
        btnAddPaymentDetailsForCar = null;


        //Scanned Document layout For Bike
        scannedDrivingLicenseLayoutForBike = null;
        nidFrontSideLayoutForBike = null;
        nidBackSideLayoutForBike = null;
        bikeRegistrationPaperLayout = null;
        if (imgDocumentUploadStatusDrivingLicenseForBike != null){
            imgDocumentUploadStatusDrivingLicenseForBike.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusNIDFrontSideForBike != null){
            imgDocumentUploadStatusNIDFrontSideForBike.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusNIDBackSideForBike != null){
            imgDocumentUploadStatusNIDBackSideForBike.setImageDrawable(null);
        }
        if (imgDocumentUploadStatusBikeRegistration != null){
            imgDocumentUploadStatusBikeRegistration.setImageDrawable(null);
        }
        imgDocumentUploadStatusDrivingLicenseForBike = null;
        imgDocumentUploadStatusNIDFrontSideForBike = null;
        imgDocumentUploadStatusNIDBackSideForBike = null;
        imgDocumentUploadStatusBikeRegistration = null;
        btnAddPaymentDetailsForBike = null;

        //Choose From Gallery or Take New Photo Layout
        ic_back_sign12 = null;
        btnTakePhoto = null;
        btnUploadFromGallery = null;
        toBeTakenImageName = null;

        //Preview documents and upload Layout
        ic_back_sign3 = null;
        Uri imageUri = null;
        if (imgScannedDocumentShownByGlide != null){
            imgScannedDocumentShownByGlide.setImageURI(imageUri);
        }
        imgScannedDocumentShownByGlide = null;
        btnDiscardPhoto = null;
        btnUploadPhotoToServer = null;
        mCurrentPhotoPathForTakingImage = null;
        mCurrentPhotoPathForTakingImageFromGallery = null;
        imageTakenUpTOAppUsing = null;
        txtScannedPhotoName = null;


        Runtime.getRuntime().gc(); //This will invoke manual Garbage Collection.
        //Before invoking above method we have to set all the views to null
        //So there will be no reference to those view and garbage collector will
        //be able to collect garbage.
    }

    private void uploadDriverOrBikerProfilePIC(
            final String driverOrBiker,
            String driverOrBikerName) {
        //Below we will save our driver / biker profile picture to firebase storage.
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (Common.driverOrBiker == null){
            getDriverOrBiker();
        }
        StorageReference filePath = FirebaseStorage.getInstance().getReference().
                child(Common.driverOrBiker +"_Documents")
                .child(Common.driverUID)
                .child(Common.driverOrBiker +"_Profile_Pic")
                .child(driverOrBikerName);
        //Now we will transfer our 'resultUri' into bitmap
        Bitmap bitmap = null;
        try {
            if (Common.driverOrBiker.equals("Biker")){
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic);
            }
            if (Common.driverOrBiker.equals("Driver")){
                bitmap = BitmapFactory.decodeFile(mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic);
            }

            //As firebase storage has limited free space, so we will first compress our image.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
            //Now we will convert this bitmap image into an array
            final byte[] imgData = baos.toByteArray();
            ProfilePic profilePic = new ProfilePic();
            profilePic.setDriverUid(Common.driverUID);
            profilePic.setProfile_Pic(imgData);
            mViewModel.addProfilePic(profilePic);
            //Now we will upload this array into our firebase storage.
            UploadTask uploadTask = filePath.putBytes(imgData);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //After putting our image to firebase storage. We will create a link of this image
                    //in our firebase database.

                    Uri documentDownloadUri = taskSnapshot.getDownloadUrl();
                    Log.i(TAG,documentDownloadUri.toString());

                    HashMap<String, Object> document_url = new HashMap();
                    document_url.put("Profile_Pic_Url",documentDownloadUri.toString());
                    if (Common.driverUID == null){
                        Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    }

                    DatabaseReference mDriverScannedDocumentUrlDB= FirebaseDatabase.getInstance().
                            getReference("Users")
                            .child(Common.driverOrBiker + "s")
                            .child(Common.driverUID)
                            .child(Common.driverOrBiker + "_Profile_Pic");
                    mDriverScannedDocumentUrlDB.updateChildren(document_url).addOnCompleteListener
                            (new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseCrash.log("Welcome:RealDB:Driver/Biker_Profile_Pic Update: Success");
                                        Crashlytics.log("Welcome:RealDB:Driver/Biker_Profile_Pic Update: Success");
                                        mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic = null;
                                        mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic = null;
                                        imageGalleryPathUriForDriverProfilePic = null;
                                        imageGalleryPathUriForBikerProfilePic = null;
                                        progressDialog.dismiss();
                                        addBasicInfoTableToRoomDB();
                                        Runtime.getRuntime().gc();
                                    }else {
                                        FirebaseCrash.log("Welcome:RealDB:Driver/Biker_Profile_Pic Update: failed");
                                        Crashlytics.log("Home:RealDB:Driver/Biker_Profile_Pic Update: failed");
                                    }
                                }
                            });


                    FirebaseFirestore mFireStoreDriverScannedDocumentUrlDB =FirebaseFirestore.getInstance();
                    mFireStoreDriverScannedDocumentUrlDB.collection("Users")
                            .document(Common.driverOrBiker + "s")
                            .collection(Common.driverUID)
                            .document(Common.driverOrBiker + "_Profile_Pic")
                            .set(document_url)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        FirebaseCrash.log("Welcome:fireStore:Driver/Biker_Profile_Pic Update: Success");
                                        Crashlytics.log("Welcome:fireStore:Driver/Biker_Profile_Pic Update: Success");
                                    }else {
                                        FirebaseCrash.log("Welcome:fireStore:Driver/Biker_Profile_Pic Update: failed");
                                        Crashlytics.log("Welcome:fireStore:Driver/Biker_Profile_Pic Update: failed");
                                    }
                                }
                            });

//                saveCustomerPicToSqliteDB(imgData);


                }
            });

            progressDialog = new ProgressDialog(Welcome.this, R.style.StyledDialog);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading....");
            setProgressDialogTitle("Profile Pic");
            progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            uploadTask.addOnProgressListener(Welcome.this,
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) /
                                    taskSnapshot.getTotalByteCount();
                            Log.i(TAG, "Upload is " + progress + "% done");
                            int currentprogress = (int) progress;
                            progressDialog.incrementProgressBy(currentprogress);
                        }
                    });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Welcome.this,"Something went wrong. Upload failed....",
                            Toast.LENGTH_LONG).show();

                }
            });

            if (driverOrBiker.equals("Driver")){
                signUpForCarLayout.setVisibility(View.GONE);
                scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                setUpAllTheViewsFromScannedDocumentlayoutForCar();
                editor.putString("Layout","scannedDocumentsUploadLayoutForCar");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();
            } else if (driverOrBiker.equals("Biker")){
                signUpForBikeLayout.setVisibility(View.GONE);
                scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                setUpAllTheViewsFromScannedDocumentlayoutForBike();
                editor.putString("Layout","scannedDocumentsUploadLayoutForBike");
                editor.commit();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error){
            error.printStackTrace();
            Log.i(TAG,"OutOfMemoryError: " + error.getMessage());
            Toast.makeText(this, "Low Memory on RAM", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Select profile pic again", Toast.LENGTH_LONG).show();
        }

    }

    private void uploadDriverAndCarInfo() {
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (Common.driverOrBiker == null){
            getDriverOrBiker();
        }
        strdriverFullNameField = driverFullNameField.getText().toString();
        strdriverNIDField = driverNIDField.getText().toString();
        strdriverLicenseField = driverLicenseField.getText().toString();
        strdriverHomeAddressField = driverHomeAddressField.getText().toString();
        strcarRegistrationAlphabet = carRegistrationAlphabet.getText().toString();
        strcarRegistrationFirstTwoDigit = carRegistrationFirstTwoDigit.getText().toString();
        strcarRegistrationLastFourDigit = carRegistrationLastFourDigit.getText().toString();
        strDriverCarRegistrationNumber = strcarRegistrationAlphabet + "-" +
                strcarRegistrationFirstTwoDigit + "-" +
                strcarRegistrationLastFourDigit;
        strcarFitnessCertificateNumber = carFitnessCertificateNumber.getText().toString();
        strcarTaxTokenNumber = carTaxTokenNumber.getText().toString();
        strreferralCode = referralCode.getText().toString();

        strManuFacturer = preferences.getString("Manufacturer",DEFAULT);
        strModel = preferences.getString("Model",DEFAULT);
        strProductionYear = preferences.getString("Production_Year",DEFAULT);
        strRegistrationAuthority = preferences.getString("Registration_Authority",DEFAULT);

        HashMap <String, Object> map = new HashMap();


        if (strdriverFullNameField.equals("")){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strdriverFullNameField.equals("")){
            map.put("Full_Name", strdriverFullNameField);
            editor.putString("Full_Name",strdriverFullNameField);
        }

        if (strdriverNIDField.equals("")){
            Toast.makeText(this, "Please enter your NID number", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strdriverNIDField.equals("")){
            map.put("NID", strdriverNIDField);
            editor.putString("NID",strdriverNIDField);
        }

        if (strdriverLicenseField.equals("")){
            Toast.makeText(this, "Please enter your License", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strdriverLicenseField.equals("")){
            map.put("License", strdriverLicenseField);
            editor.putString("License",strdriverLicenseField);
        }

        if (!strdriverHomeAddressField.equals("")){
            map.put("Home_Address", strdriverHomeAddressField);
            editor.putString("Home_Address",strdriverHomeAddressField);
        }

        if (strcarRegistrationAlphabet.equals("")){
            Toast.makeText(this, "Please enter your car reg. Alphabet", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strcarRegistrationFirstTwoDigit.equals("")){
            Toast.makeText(this, "Please enter your car reg. first two digit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strcarRegistrationLastFourDigit.equals("")){
            Toast.makeText(this, "Please enter your car reg. last four digit", Toast.LENGTH_SHORT).show();
            return;
        }

        if (strDriverCarRegistrationNumber.equals("")){
            Toast.makeText(this, "Please enter your car reg. number",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!strDriverCarRegistrationNumber.equals("")){
            map.put("Registration_Number", strDriverCarRegistrationNumber);
            editor.putString("Registration_Number",strDriverCarRegistrationNumber);
        }

        if (strcarFitnessCertificateNumber.equals("")){
            Toast.makeText(this, "Please enter fitness",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!strcarFitnessCertificateNumber.equals("")){
            map.put("Fitness", strcarFitnessCertificateNumber);
            editor.putString("Fitness",strcarFitnessCertificateNumber);
        }

        if (strcarTaxTokenNumber.equals("")){
            Toast.makeText(this, "Please enter tax token",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!strcarTaxTokenNumber.equals("")){
            map.put("TaxToken", strcarTaxTokenNumber);
            editor.putString("TaxToken",strcarTaxTokenNumber);
        }

        if (strManuFacturer.equals(DEFAULT)){
            Toast.makeText(this, "Select Car Manufacturer",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strManuFacturer.equals(getResources().getString(R.string.select_car_manufacturer))){
                Toast.makeText(this, "Please enter car manufacturer name",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strManuFacturer.equals(getResources().getString(R.string.select_car_manufacturer))){
                map.put("Manufacturer", strManuFacturer);
                editor.putString("Manufacturer", strManuFacturer);
            }
        }



        if (strModel.equals(DEFAULT)){
            Toast.makeText(this, "Select Car Model",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strModel.equals(getResources().getString(R.string.select_car_model))){
                Toast.makeText(this, "Please enter car model",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strModel.equals(getResources().getString(R.string.select_car_model))){
                map.put("Model", strModel);
                editor.putString("Model", strModel);
            }
        }



        if (strProductionYear.equals(DEFAULT)){
            Toast.makeText(this, "Select Car Production year",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strProductionYear.equals(getResources().getString(R.string.select_vehicle_production_year))){
                Toast.makeText(this, "Please enter car production year",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strProductionYear.equals(getResources().getString(R.string.select_vehicle_production_year))){
                map.put("Production_year", strProductionYear);
                editor.putString("Production_year", strProductionYear);
            }
        }



        if (strRegistrationAuthority.equals(DEFAULT)){
            Toast.makeText(this, "Select Car Registration Authority",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strRegistrationAuthority.equals(getResources().getString(R.string.select_vehicle_registration_authority))){
                Toast.makeText(this, "Please enter car production year",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strRegistrationAuthority.equals(getResources().getString(R.string.select_vehicle_registration_authority))){
                map.put("Registration_Authority", strRegistrationAuthority);
                editor.putString("Registration_Authority", strRegistrationAuthority);
            }
        }

        String vehicle_type = preferences.getString("Vehicle_Type", DEFAULT);
        String Driver_Or_Biker_Mobile_Number = preferences
                .getString("Mobile",DEFAULT);
        String Driver_Or_Biker_Registration_City = preferences
                .getString("Registration_City",DEFAULT);
        if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Car")){
            map.put("Vehicle_Type","Car");
            map.put("Mobile",Driver_Or_Biker_Mobile_Number);
            map.put("Registration_City",Driver_Or_Biker_Registration_City);
            map.put(Common.driverOrBiker + "_Ratings",0);
            map.put(Common.driverOrBiker + "_Total_Trips",0);
            map.put(Common.driverOrBiker + "_Registration_Status","Completed");
        }
        btnGoToUploadDocumentPageForCar.setEnabled(false);
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference mCustomerEmailPutRef = FirebaseDatabase.getInstance()
                .getReference("Users/Drivers")
                .child(Common.driverUID)
                .child("Driver_Basic_Info");
        mCustomerEmailPutRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome: realTimeDB for vehicle_type update: success");
                    uploadDriverOrBikerProfilePIC("Driver",strdriverFullNameField);
                }else {
                    FirebaseCrash.log("Welcome: realTimeDB for vehicle_type update: failed");
                }
            }
        });

        mFireStoreDriverBasicInfoDB
                .collection("Users")
                .document("Drivers").
                collection(Common.driverUID)
                .document("Driver_Basic_Info")
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome: fireStore for car_info update: success");
                }else {
                    FirebaseCrash.log("Welcome: fireStore for car_info update: failed");
                }
            }
        });
        editor.commit();
    }

    private void uploadBikerAndBikeInfo() {
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (Common.driverOrBiker == null){
            getDriverOrBiker();
        }
        strBikerFullNameField = bikerFullNameField.getText().toString();
        strBikerNIDField = bikerNIDField.getText().toString();
        strBikerLicenseField = bikerLicenseField.getText().toString();
        strBikerHomeAddressField = bikerHomeAddressField.getText().toString();
        strBikeRegistrationAlphabet = bikeRegistrationAlphabet.getText().toString();
        strBikeRegistrationFirstTwoDigit = bikeRegistrationFirstTwoDigit.getText().toString();
        strBikeRegistrationLastFourDigit = bikeRegistrationLastFourDigit.getText().toString();
        strBikeRegistrationNumber = strBikeRegistrationAlphabet + "-" +
                strBikeRegistrationFirstTwoDigit + "-" +
                strBikeRegistrationLastFourDigit;
        strBikeReferralCode = bikeReferralCode.getText().toString();

        strManuFacturer = preferences.getString("Manufacturer",DEFAULT);
        strModel = preferences.getString("Model",DEFAULT);
        strProductionYear = preferences.getString("Production_Year",DEFAULT);
        strRegistrationAuthority = preferences.getString("Registration_Authority",DEFAULT);

        HashMap <String, Object> map = new HashMap();

        if (strBikerFullNameField.equals("")){
            Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        } else if (!strBikerFullNameField.equals("")){
            map.put("Full_Name", strBikerFullNameField);
            editor.putString("Full_Name",strBikerFullNameField);
        }

        if (strBikerNIDField.equals("")){
            Toast.makeText(this, "Please enter your NID number",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!strBikerNIDField.equals("")){
            map.put("NID", strBikerNIDField);
            editor.putString("NID",strBikerNIDField);
        }

        if (strBikerLicenseField.equals("")){
            Toast.makeText(this, "Please enter your License",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!strBikerLicenseField.equals("")){
            map.put("License", strBikerLicenseField);
            editor.putString("License",strBikerLicenseField);
        }

        if (!strBikerHomeAddressField.equals("")){
            map.put("Home_Address", strBikerHomeAddressField);
            editor.putString("Home_Address",strBikerHomeAddressField);
        }

        if (strBikeRegistrationAlphabet.equals("")){
            Toast.makeText(this, "Please enter your bike reg. Alphabet",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (strBikeRegistrationFirstTwoDigit.equals("")){
            Toast.makeText(this, "Please enter your bike reg. first two digit",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (strBikeRegistrationLastFourDigit.equals("")){
            Toast.makeText(this, "Please enter your bike reg. last four digit",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (strBikeRegistrationNumber.equals("")){
            Toast.makeText(this, "Please enter your bike reg. number",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!strBikeRegistrationNumber.equals("")){
            map.put("Registration_Number", strBikeRegistrationNumber);
            editor.putString("Registration_Number",strBikeRegistrationNumber);
        }

        if (strManuFacturer.equals(DEFAULT)){
            Toast.makeText(this, "Select Bike Manufacturer",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strManuFacturer.equals(getResources()
                    .getString(R.string.select_bike_manufacturer))){
                Toast.makeText(this, "Please enter bike manufacturer name",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strManuFacturer.equals(getResources()
                    .getString(R.string.select_bike_manufacturer))){
                map.put("Manufacturer", strManuFacturer);
                editor.putString("Manufacturer", strManuFacturer);
            }
        }



        if (strModel.equals(DEFAULT)){
            Toast.makeText(this, "Select Bike Model",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strModel.equals(getResources().getString(R.string.select_bike_model))){
                Toast.makeText(this, "Please enter bike model",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strModel.equals(getResources().getString(R.string.select_bike_model))){
                map.put("Model", strModel);
                editor.putString("Model", strModel);
            }
        }



        if (strProductionYear.equals(DEFAULT)){
            Toast.makeText(this, "Select Bike Production year",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strProductionYear.equals(getResources()
                    .getString(R.string.select_vehicle_production_year))){
                Toast.makeText(this, "Please enter bike production year",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strProductionYear.equals(getResources()
                    .getString(R.string.select_vehicle_production_year))){
                map.put("Production_year", strProductionYear);
                editor.putString("Production_year", strProductionYear);
            }
        }



        if (strRegistrationAuthority.equals(DEFAULT)){
            Toast.makeText(this, "Select Bike Registration Authority",
                    Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (strRegistrationAuthority.equals(getResources()
                    .getString(R.string.select_vehicle_registration_authority))){
                Toast.makeText(this, "Please enter Bike Registration Authority",
                        Toast.LENGTH_SHORT).show();
                return;
            } else if (!strRegistrationAuthority.equals(getResources()
                    .getString(R.string.select_vehicle_registration_authority))){
                map.put("Registration_Authority", strRegistrationAuthority);
                editor.putString("Registration_Authority", strRegistrationAuthority);
            }
        }

        String vehicle_type = preferences.getString("Vehicle_Type", DEFAULT);
        String Driver_Or_Biker_Mobile_Number = preferences
                .getString("Mobile",DEFAULT);
        String Driver_Or_Biker_Registration_City = preferences
                .getString("Registration_City",DEFAULT);
        if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Bike")){
            map.put("Vehicle_Type","Bike");
            map.put("Mobile",Driver_Or_Biker_Mobile_Number);
            map.put("Registration_City",Driver_Or_Biker_Registration_City);
            map.put(Common.driverOrBiker + "_Ratings",0);
            map.put(Common.driverOrBiker + "_Total_Trips",0);
            map.put(Common.driverOrBiker + "_Registration_Status","Completed");
        }
        btnGoToUploadDocumentPageForBike.setEnabled(false);
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference mCustomerEmailPutRef = FirebaseDatabase.getInstance()
                .getReference("Users/Bikers")
                .child(Common.driverUID)
                .child("Biker_Basic_Info");
        mCustomerEmailPutRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome: realTimeDB for vehicle_type update: success");
                    uploadDriverOrBikerProfilePIC("Biker", strBikerFullNameField);
                }else {
                    FirebaseCrash.log("Welcome: realTimeDB for vehicle_type update: failed");
                }
            }
        });

        mFireStoreDriverBasicInfoDB.collection("Users").document("Bikers").
                collection(Common.driverUID).document("Biker_Basic_Info")
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome: fireStore for bike_info update: success");
                }else {
                    FirebaseCrash.log("Welcome: fireStore for bike_info update: failed");
                }
            }
        });

        editor.commit();

    }

    private void setUpSpinnerForCar() {

        spinnerCarManufacturer = findViewById(R.id.spinnerCarManufacturer);
        spinnerCarModel = findViewById(R.id.spinnerCarModel);
        spinnerCarProductionYear = findViewById(R.id.spinnerCarProductionYear);
        spinnerRegistrationAuthority = findViewById(R.id.spinnerRegistrationAuthority);

        spinnerCarManufacturer.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);
        spinnerCarModel.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);
        spinnerCarProductionYear.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);
        spinnerRegistrationAuthority.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);

        spinnerCarManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForCarLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Manufacturer",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Manufacturer: " + selected_item);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForCarLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Model",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Model: " + selected_item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerCarProductionYear.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForCarLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Production_Year",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Production_Year: " + selected_item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerRegistrationAuthority.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForCarLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Registration_Authority",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Registration_Authority: " + selected_item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setUpTabLayOutForCar() {
        tabHostForCar = findViewById(R.id.tabHostForCar);
        tabHostForCar.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHostForCar.newTabSpec(getResources().getString(R.string.car_info));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.car_info));
        tabHostForCar.addTab(spec);

        //Tab 2
        spec = tabHostForCar.newTabSpec(getResources().getString(R.string.documents));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.documents));
        tabHostForCar.addTab(spec);

        //Event
        tabHostForCar.setOnTabChangedListener(new TabAnimation(this, tabHostForCar));
    }

    private void setUpSpinnerForBike() {

        spinnerBikeManufacturer = findViewById(R.id.spinnerBikeManufacturer);
        spinnerBikeModel = findViewById(R.id.spinnerBikeModel);
        spinnerBikeProductionYear = findViewById(R.id.spinnerBikeProductionYear);
        spinnerBikeRegistrationAuthority = findViewById(R.id.spinnerBikeRegistrationAuthority);

        spinnerBikeManufacturer.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);
        spinnerBikeModel.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);
        spinnerBikeProductionYear.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);
        spinnerBikeRegistrationAuthority.getBackground().setColorFilter(getResources()
                .getColor(R.color.uthaoIconColor), PorterDuff.Mode.SRC_ATOP);

        spinnerBikeManufacturer.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForBikeLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Manufacturer",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Manufacturer: " + selected_item);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBikeModel.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForBikeLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Model",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Model: " + selected_item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBikeProductionYear.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForBikeLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Production_Year",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Production_Year: " + selected_item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerBikeRegistrationAuthority.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (signUpForBikeLayout !=null && adapterView.getChildAt(0) != null){
                    ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                    ((TextView) adapterView.getChildAt(0)).setTextSize(16);
                    String selected_item = adapterView.getItemAtPosition(i).toString();
                    editor.putString("Registration_Authority",selected_item);
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    Log.i(TAG,"Registration_Authority: " + selected_item);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setUpTabLayOutForBike() {
        tabHostForbike = findViewById(R.id.tabHostForBike);
        tabHostForbike.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHostForbike.newTabSpec(getResources().getString(R.string.bike_info));
        spec.setContent(R.id.tabbiker1);
        spec.setIndicator(getResources().getString(R.string.bike_info));
        tabHostForbike.addTab(spec);

        //Tab 2
        spec = tabHostForbike.newTabSpec(getResources().getString(R.string.documents));
        spec.setContent(R.id.tabbiker2);
        spec.setIndicator(getResources().getString(R.string.documents));
        tabHostForbike.addTab(spec);

        //Event
        tabHostForbike.setOnTabChangedListener(new TabAnimation(this, tabHostForbike));
    }

    private void signInWithPhoneAuthCredentialForNewUser
            (PhoneAuthCredential phoneAuthCredential) {
        FirebaseCrash.log("Welcome: signInWithPhoneAuthCredentialForNewUser.called");
        //Show waiting animation.
        try {
            if (waitingDialogForPhoneVerification.isShowing()){
                waitingDialogForPhoneVerification.dismiss();
            }
        } catch (IllegalArgumentException ex){
            ex.printStackTrace();
            Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
        }
        waitingDialogForPhoneVerification.show();
        mDriverAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener
                (Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(Welcome.this, "Invalid Verification", Toast.LENGTH_SHORT).show();
                                try {
                                    if (waitingDialogForPhoneVerification.isShowing()){
                                        waitingDialogForPhoneVerification.dismiss();
                                    }
                                } catch (IllegalArgumentException ex){
                                    ex.printStackTrace();
                                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                }
                                FirebaseCrash.log("Welcome: signInWithCredential:failed");
                            }
                        } else {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseCrash.log("Welcome: signInWithCredential:success");
                            //Now we will verify whether our driver / biker is registered user or not.
                            //In some case driver / biker may forget that he has already done registration
                            // in uthoa. So we will check this for saving our database corruption. We will
                            //also check whether driver / biker has customer account activated using same mobile
                            //number or email address. If driver / biker has customer account activated using
                            // same mobile number than we will cancel link account using email an password.
                            if (Common.driverUID == null){
                                Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            }
                            //First we will check in our customer segment whether driver / biker registered as
                            //customer or not
                            DatabaseReference checkInCustomerDB= FirebaseDatabase.getInstance().
                                    getReference("Users")
                                    .child("Customers");
                            checkInCustomerDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(Common.driverUID)){
                                        try {
                                            if (waitingDialogForPhoneVerification.isShowing()){
                                                waitingDialogForPhoneVerification.dismiss();
                                            }
                                        } catch (IllegalArgumentException ex){
                                            ex.printStackTrace();
                                            Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                        }
                                        waitingDialogForPhoneVerification.dismiss();
                                        driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                                        driverOrBikerRegistrationOTPLayOut.setVisibility(View.GONE);
                                        select_city_layout.setVisibility(View.VISIBLE);
                                        setUpAllTheViewsFromSelectYourCityLayout();
                                        FirebaseCrash.log("Welcome: layOut-driverOrBikerRegisterUsingEmailPasswordLayOut");
                                        editor.putString("Layout", "select_city_layout");
                                        editor.commit();
                                        getWhichLayOutIsWrittenInSharedPreferences();
                                        editor.putBoolean("isDriverOrBikerHasCustomerAccount",true);
                                        editor.commit();
                                    } else {
                                        editor.putBoolean("isDriverOrBikerHasCustomerAccount",false);
                                        editor.commit();
                                        //Now we will check whether our driver / biker has previous account
                                        //in our database
                                        //first we will check in driver segment
                                        DatabaseReference checkInDriverDB= FirebaseDatabase.getInstance().
                                                getReference("Users")
                                                .child("Drivers");
                                        checkInDriverDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(Common.driverUID)){
                                                    //Our user has previous driver account
                                                    editor.putBoolean("isDriverOrBikerHasPreviousAccount",true);
                                                    editor.putBoolean("driverHasPreviousAccount",true);
                                                    editor.commit();

                                                    DatabaseReference retrieveDriverBasicInfo = FirebaseDatabase.getInstance()
                                                            .getReference("Users/Drivers")
                                                            .child(Common.driverUID)
                                                            .child("Driver_Basic_Info");
                                                    retrieveDriverBasicInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                                                FirebaseCrash.log("Welcome: existingUser: previous data fetching: success");

                                                                String Full_Name = null;
                                                                Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                                                if (map.get("Full_Name") != null){
                                                                    Full_Name = map.get("Full_Name").toString();
                                                                    editor.putString("Full_Name",Full_Name);
                                                                }
                                                                if (map.get("Email") != null) {
                                                                    String Email = map.get("Email").toString();
                                                                    editor.putString("Email",Email);
                                                                }
                                                                if (map.get("Mobile") != null) {
                                                                    String Mobile = map.get("Mobile").toString();
                                                                    editor.putString("Mobile",Mobile);
                                                                }

                                                                if (map.get("BKash_Number") != null) {
                                                                    String BKash_Number = map.get("BKash_Number").toString();
                                                                    editor.putString("BKash_Number",BKash_Number);
                                                                }

                                                                if (map.get("NID") != null) {
                                                                    String NID = map.get("NID").toString();
                                                                    editor.putString("NID",NID);
                                                                }

                                                                if (map.get("License") != null) {
                                                                    String License = map.get("License").toString();
                                                                    editor.putString("License",License);
                                                                }

                                                                if (map.get("Home_Address") != null) {
                                                                    String Home_Address = map.get("Home_Address").toString();
                                                                    editor.putString("Home_Address",Home_Address);
                                                                }

                                                                if (map.get("Registration_City") != null) {
                                                                    String Registration_City = map.get("Registration_City").toString();
                                                                    editor.putString("Registration_City",Registration_City);
                                                                }

                                                                if (map.get("Vehicle_Type") != null) {
                                                                    String Vehicle_Type = map.get("Vehicle_Type").toString();
                                                                    editor.putString("Vehicle_Type",Vehicle_Type);
                                                                }
                                                                if (map.get("Manufacturer") != null) {
                                                                    String Manufacturer = map.get("Manufacturer").toString();
                                                                    editor.putString("Manufacturer",Manufacturer);
                                                                }
                                                                if (map.get("Model") != null) {
                                                                    String Model = map.get("Model").toString();
                                                                    editor.putString("Model",Model);
                                                                }
                                                                if (map.get("Production_year") != null) {
                                                                    String Production_year = map.get("Production_year").toString();
                                                                    editor.putString("Production_year",Production_year);
                                                                }
                                                                if (map.get("Registration_Authority") != null) {
                                                                    String Registration_Authority = map.get("Registration_Authority").toString();
                                                                    editor.putString("Registration_Authority",Registration_Authority);
                                                                }
                                                                if (map.get("Registration_Number") != null) {
                                                                    String Registration_Number = map.get("Registration_Number").toString();
                                                                    editor.putString("Registration_Number",Registration_Number);
                                                                }
                                                                if (map.get("TaxToken") != null) {
                                                                    String TaxToken = map.get("TaxToken").toString();
                                                                    editor.putString("TaxToken",TaxToken);
                                                                }
                                                                if (map.get("Fitness") != null) {
                                                                    String Fitness = map.get("Fitness").toString();
                                                                    editor.putString("Fitness",Fitness);
                                                                }
                                                                editor.commit();
                                                                addBasicInfoTableToRoomDB();

                                                                try {
                                                                    if (waitingDialogForPhoneVerification.isShowing()){
                                                                        waitingDialogForPhoneVerification.dismiss();
                                                                    }
                                                                } catch (IllegalArgumentException ex){
                                                                    ex.printStackTrace();
                                                                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                                                }
                                                                getDeviceConfiguration("Car");
                                                                Intent intent = new Intent(Welcome.this,Home.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                } else {
                                                    //Our user don't have previous driver account
                                                    editor.putBoolean("isDriverOrBikerHasPreviousAccount",false);
                                                    editor.putBoolean("driverHasPreviousAccount",false);

                                                    //now we will check in biker segment
                                                    DatabaseReference checkInBikerDB= FirebaseDatabase.getInstance().
                                                            getReference("Users")
                                                            .child("Bikers");
                                                    checkInBikerDB.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.hasChild(Common.driverUID)){
                                                                //Our user has previous biker account
                                                                editor.putBoolean("isDriverOrBikerHasPreviousAccount",true);
                                                                editor.putBoolean("bikerHasPreviousAccount",true);
                                                                editor.commit();

                                                                DatabaseReference retrieveDriverBasicInfo = FirebaseDatabase.getInstance()
                                                                        .getReference("Users/Bikers")
                                                                        .child(Common.driverUID)
                                                                        .child("Biker_Basic_Info");
                                                                retrieveDriverBasicInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                                                            FirebaseCrash.log("Welcome: existingUser: previous data fetching: success");
                                                                            String Full_Name = null;
                                                                            Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                                                            if (map.get("Full_Name") != null){
                                                                                Full_Name = map.get("Full_Name").toString();
                                                                                editor.putString("Full_Name",Full_Name);
                                                                            }
                                                                            if (map.get("Email") != null) {
                                                                                String Email = map.get("Email").toString();
                                                                                editor.putString("Email",Email);
                                                                            }
                                                                            if (map.get("Mobile") != null) {
                                                                                String Mobile = map.get("Mobile").toString();
                                                                                editor.putString("Mobile",Mobile);
                                                                            }

                                                                            if (map.get("BKash_Number") != null) {
                                                                                String BKash_Number = map.get("BKash_Number").toString();
                                                                                editor.putString("BKash_Number",BKash_Number);
                                                                            }

                                                                            if (map.get("NID") != null) {
                                                                                String NID = map.get("NID").toString();
                                                                                editor.putString("NID",NID);
                                                                            }

                                                                            if (map.get("License") != null) {
                                                                                String License = map.get("License").toString();
                                                                                editor.putString("License",License);
                                                                            }

                                                                            if (map.get("Home_Address") != null) {
                                                                                String Home_Address = map.get("Home_Address").toString();
                                                                                editor.putString("Home_Address",Home_Address);
                                                                            }

                                                                            if (map.get("Registration_City") != null) {
                                                                                String Registration_City = map.get("Registration_City").toString();
                                                                                editor.putString("Registration_City",Registration_City);
                                                                            }

                                                                            if (map.get("Vehicle_Type") != null) {
                                                                                String Vehicle_Type = map.get("Vehicle_Type").toString();
                                                                                editor.putString("Vehicle_Type",Vehicle_Type);
                                                                            }
                                                                            if (map.get("Manufacturer") != null) {
                                                                                String Manufacturer = map.get("Manufacturer").toString();
                                                                                editor.putString("Manufacturer",Manufacturer);
                                                                            }
                                                                            if (map.get("Model") != null) {
                                                                                String Model = map.get("Model").toString();
                                                                                editor.putString("Model",Model);
                                                                            }
                                                                            if (map.get("Production_year") != null) {
                                                                                String Production_year = map.get("Production_year").toString();
                                                                                editor.putString("Production_year",Production_year);
                                                                            }
                                                                            if (map.get("Registration_Authority") != null) {
                                                                                String Registration_Authority = map.get("Registration_Authority").toString();
                                                                                editor.putString("Registration_Authority",Registration_Authority);
                                                                            }
                                                                            if (map.get("Registration_Number") != null) {
                                                                                String Registration_Number = map.get("Registration_Number").toString();
                                                                                editor.putString("Registration_Number",Registration_Number);
                                                                            }
                                                                            if (map.get("TaxToken") != null) {
                                                                                String TaxToken = map.get("TaxToken").toString();
                                                                                editor.putString("TaxToken",TaxToken);
                                                                            }
                                                                            if (map.get("Fitness") != null) {
                                                                                String Fitness = map.get("Fitness").toString();
                                                                                editor.putString("Fitness",Fitness);
                                                                            }
                                                                            editor.commit();
                                                                            addBasicInfoTableToRoomDB();
                                                                            try {
                                                                                if (waitingDialogForPhoneVerification.isShowing()){
                                                                                    waitingDialogForPhoneVerification.dismiss();
                                                                                }
                                                                            } catch (IllegalArgumentException ex){
                                                                                ex.printStackTrace();
                                                                                Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                                                            }
                                                                            getDeviceConfiguration("Bike");
                                                                            Intent intent = new Intent(Welcome.this,Home.class);
                                                                            startActivity(intent);
                                                                            finish();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });

                                                            } else {
                                                                //Our user don't have previous biker account
                                                                editor.putBoolean("isDriverOrBikerHasPreviousAccount",false);
                                                                editor.putBoolean("bikerHasPreviousAccount",false);

                                                                Log.i(TAG,"User is UnRegistered");
                                                                //Now we will save those basic info to internal storage
                                                                editor.putString("Mobile",registrationNumberWithCountryCode);
                                                                try {
                                                                    if (waitingDialogForPhoneVerification.isShowing()){
                                                                        waitingDialogForPhoneVerification.dismiss();
                                                                    }
                                                                } catch (IllegalArgumentException ex){
                                                                    ex.printStackTrace();
                                                                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                                                }

                                                                driverOrBikerRegistrationPhoneVerificationLayout.setVisibility(View.GONE);
                                                                driverOrBikerRegistrationOTPLayOut.setVisibility(View.GONE);
                                                                select_city_layout.setVisibility(View.VISIBLE);
                                                                setUpAllTheViewsFromSelectYourCityLayout();
                                                                FirebaseCrash.log("Welcome: layOut-driverOrBikerRegisterUsingEmailPasswordLayOut");
                                                                editor.putString("Layout", "select_city_layout");
                                                                editor.commit();
                                                                getWhichLayOutIsWrittenInSharedPreferences();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }

    private void startDriverRegistrationProcess() {

        FirebaseCrash.log("Welcome: startDriverRegistrationProcess.called");
        strDriverOrBikerMobileNumber = edtRegistrationPhoneNumber.getText().toString();
        FirebaseCrash.log("Welcome: edtRegistrationPhoneNumber: " + strDriverOrBikerMobileNumber);
        strDriverCountryCode = edtRegistrationCountryCode.getText().toString();
        FirebaseCrash.log("Welcome: edtRegistrationCountryCode: " + strDriverCountryCode);

        registrationNumberWithCountryCode = strDriverCountryCode + strDriverOrBikerMobileNumber;
        Log.i("Check","registrationNumberWithCountryCode: " + registrationNumberWithCountryCode);

        if (registrationNumberWithCountryCode.equals("")){
            Toast.makeText(Welcome.this,"Please enter your phone number",
                    Toast.LENGTH_LONG).show();
            return;
        }else if (!isPhoneNumberValid(registrationNumberWithCountryCode)){
            Toast.makeText(Welcome.this,"Please insert phone number with Country Code",
                    Toast.LENGTH_LONG).show();
            return;
        }

        editor.putString("Mobile",registrationNumberWithCountryCode);
        editor.commit();
        //Register new user
        //Now we will send customer phone number to firebase database
        waitingDialogForPhoneVerification.show();
        FirebaseCrash.log("Welcome: registrationNumberWithCountryCode: " +registrationNumberWithCountryCode);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                registrationNumberWithCountryCode,
                60,
                TimeUnit.SECONDS,
                Welcome.this,
                mRegisterCallbacks
        );
    }

    private void attachEmailPasswordSignIn() {
        FirebaseCrash.log("Welcome: attachEmailPasswordSignIn.called");
        strDriverEmail = edtRegisterEmail.getText().toString();
        strDriverPassword = edtRegisterPassword.getText().toString();

        if (strDriverPassword.equals("")){
            Toast.makeText(Welcome.this,"Please enter your password",Toast.LENGTH_LONG).show();
            return;
        } else if (strDriverPassword.length() < 6){
            Toast.makeText(this, "Password must be more than 6 character", Toast.LENGTH_SHORT).show();
        }

        if (strDriverEmail.equals("")){
            Toast.makeText(Welcome.this,"Please enter your email",Toast.LENGTH_LONG).show();
            return;
        } else if (!isEmailValid(strDriverEmail)){
            Toast.makeText(Welcome.this,"Please provide valid email address",Toast.LENGTH_LONG).show();
            return;
        }
        final String vehicle_type = preferences.getString("Vehicle_Type",DEFAULT);
        String driverOrBiker = "N/A";
        if (vehicle_type.equals("Car")){
            Common.driverOrBiker = "Driver";
        } else if (vehicle_type.equals("Bike")){
            Common.driverOrBiker = "Biker";
        }

        editor.putString("Email",strDriverEmail);
        editor.commit();

        final String DriverOrBiker_Mobile_Number = preferences
                .getString("Mobile",DEFAULT);
        boolean isDriverOrBikerHasCustomerAccount = preferences
                .getBoolean("isDriverOrBikerHasCustomerAccount",false);
        if (isDriverOrBikerHasCustomerAccount){
            //user also has customer account
            //Now we will create a driver / biker profile for this user

            if (Common.driverUID == null){
                Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
            //Now we will upload driver / biker basic info to server
            DatabaseReference mCustomerEmailPutRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child(Common.driverOrBiker + "_Basic_Info");

            HashMap <String, Object> map = new HashMap();
            map.put("Email", strDriverEmail);
            map.put("Password", strDriverPassword);
            map.put("Mobile", DriverOrBiker_Mobile_Number);


            mCustomerEmailPutRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        FirebaseCrash.log("Welcome: realTimeDB for emailPass update: success");
                        destroyPreviouslyCreatedView();
                    }else {
                        FirebaseCrash.log("Welcome: realTimeDB for emailPass update: failed");
                    }
                }
            });

            mFireStoreDriverBasicInfoDB.collection("Users")
                    .document(Common.driverOrBiker + "s").
                    collection(Common.driverUID)
                    .document(Common.driverOrBiker + "_Basic_Info")
                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        FirebaseCrash.log("Welcome: fireStore for emailPass update: success");
                        Log.i(TAG,"Welcome: fireStore for emailPass update: success");
                    }else {
                        FirebaseCrash.log("Welcome: fireStore for emailPass update: failed");
                        Log.i(TAG,"Welcome: fireStore for emailPass update: failed");
                    }
                }
            });
            //Now we will save those basic info to internal storage
            editor.putString(driverOrBiker + "_Email",strDriverEmail);
            driverOrBikerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
            if (vehicle_type.equals("Car")){
                signUpForCarLayout.setVisibility(View.VISIBLE);
                editor.putString("Layout", "signUpForCarLayout");
            } else if (vehicle_type.equals("Bike")){
                signUpForBikeLayout.setVisibility(View.VISIBLE);
                editor.putString("Layout", "signUpForBikeLayout");
            }
            editor.commit();
            getWhichLayOutIsWrittenInSharedPreferences();
            // ...
        } else {
            //user does not have customer account
            //That means we will add sign in with email and password.
            //Now we will attach EmailAuthProvider with PhoneAuthProvide.
            credential = EmailAuthProvider.getCredential(strDriverEmail, strDriverPassword);
            final String finalDriverOrBiker = driverOrBiker;
            mDriverAuth.getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseCrash.log("Welcome: EmailPassword linked with phoneAuth: success");
                                FirebaseUser user = task.getResult().getUser();
                                user.sendEmailVerification();
                                Log.d(TAG, "linkWithCredential:success\nUser: " + user);
                                Toast.makeText(Welcome.this, "Verification Done", Toast.LENGTH_SHORT).show();

                                if (Common.driverUID == null){
                                    Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                }
                                //Now we will upload driver / biker basic info to server
                                DatabaseReference mCustomerEmailPutRef = FirebaseDatabase.getInstance()
                                        .getReference("Users")
                                        .child(Common.driverOrBiker + "s")
                                        .child(Common.driverUID)
                                        .child(Common.driverOrBiker + "_Basic_Info");
                                HashMap <String, Object> map = new HashMap();
                                map.put("Email", strDriverEmail);
                                map.put("Password", strDriverPassword);
                                map.put("Mobile", DriverOrBiker_Mobile_Number);


                                mCustomerEmailPutRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            FirebaseCrash.log("Welcome: realTimeDB for emailPass update: success");
                                            destroyPreviouslyCreatedView();
                                        }else {
                                            FirebaseCrash.log("Welcome: realTimeDB for emailPass update: failed");
                                        }
                                    }
                                });

                                mFireStoreDriverBasicInfoDB.collection("Users")
                                        .document(Common.driverOrBiker + "s").
                                        collection(Common.driverUID)
                                        .document(Common.driverOrBiker + "_Basic_Info")
                                        .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            FirebaseCrash.log("Welcome: fireStore for emailPass update: success");
                                            Log.i(TAG,"Welcome: fireStore for emailPass update: success");
                                        }else {
                                            FirebaseCrash.log("Welcome: fireStore for emailPass update: failed");
                                            Log.i(TAG,"Welcome: fireStore for emailPass update: failed");
                                        }
                                    }
                                });
                                //Now we will save those basic info to internal storage
                                editor.putString(Common.driverOrBiker + "_Email",strDriverEmail);
                                driverOrBikerRegisterUsingEmailPasswordLayOut.setVisibility(View.GONE);
                                if (vehicle_type.equals("Car")){
                                    signUpForCarLayout.setVisibility(View.VISIBLE);
                                    editor.putString("Layout", "signUpForCarLayout");
                                } else if (vehicle_type.equals("Bike")){
                                    signUpForBikeLayout.setVisibility(View.VISIBLE);
                                    editor.putString("Layout", "signUpForBikeLayout");
                                }
                                editor.commit();
                                getWhichLayOutIsWrittenInSharedPreferences();
                                // ...
                            } else {
                                FirebaseCrash.log("Welcome: EmailPassword linked with phoneAuth: failed");
                                Log.w("Check", "linkWithCredential:failure", task.getException());
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    if (Objects.equals(task.getException().toString(), "com.google.firebase.FirebaseException: User has already been linked to the given provider.")){
                                        Toast.makeText(Welcome.this, "Authentication failed please use different email",
                                                Toast.LENGTH_LONG).show();
                                    }
                                }

                            }
                        }
                    });
        }


    }

    private void putCheckMarkToUploadedDocumentIfViewsGetDestroyed() {
        String vehicle_type =  preferences.getString("Vehicle_Type",DEFAULT);
        String driverOrBiker = "N/A";
        if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Car")){
            Common.driverOrBiker = "Driver";
        } else if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Bike")){
            Common.driverOrBiker = "Biker";
        }

        //Scanned Document layout
        boolean Driving_License = preferences.getBoolean("Driving_License",false);
        boolean NID_FRONT = preferences.getBoolean("NID_FRONT",false);
        boolean NID_BACK = preferences.getBoolean("NID_BACK",false);
        boolean FITNESS_CERTIFICATE = preferences.getBoolean("FITNESS_CERTIFICATE",false);
        boolean TAX_TOKEN = preferences.getBoolean("TAX_TOKEN",false);
        boolean REGISTRATION_PAPER = preferences.getBoolean("REGISTRATION_PAPER",false);
        if (Driving_License && Common.driverOrBiker.equals("Driver")){
            imgDocumentUploadStatusDrivingLicenseForCar.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        } else if (Driving_License && Common.driverOrBiker.equals("Biker")){
            imgDocumentUploadStatusDrivingLicenseForBike.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        }

        if (NID_FRONT && Common.driverOrBiker.equals("Driver")){
            imgDocumentUploadStatusNIDFrontSideForCar.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        } else if (Driving_License && Common.driverOrBiker.equals("Biker")){
            imgDocumentUploadStatusNIDFrontSideForBike.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        }

        if (NID_BACK && Common.driverOrBiker.equals("Driver")){
            imgDocumentUploadStatusNIDBackSideForCar.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        } else if (Driving_License && Common.driverOrBiker.equals("Biker")){
            imgDocumentUploadStatusNIDBackSideForBike.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        }

        if (FITNESS_CERTIFICATE && Common.driverOrBiker.equals("Driver")){
            imgDocumentUploadStatusCarFitness.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        } else if (Driving_License && driverOrBiker.equals("Biker")){

        }

        if (TAX_TOKEN && Common.driverOrBiker.equals("Driver")){
            imgDocumentUploadStatusCarTaxToken.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        } else if (Driving_License && Common.driverOrBiker.equals("Biker")){

        }

        if (REGISTRATION_PAPER && Common.driverOrBiker.equals("Driver")){
            imgDocumentUploadStatusCarRegistration.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        } else if (Driving_License && Common.driverOrBiker.equals("Biker")){
            imgDocumentUploadStatusBikeRegistration.setImageDrawable(getResources()
                    .getDrawable(R.drawable.ic_check_mark));
        }

    }


    private void openCamera( String imageName) {
        dispatchTakePictureIntent(imageName);
    }

    private void dispatchTakePictureIntent(String imageName) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(imageName);
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sand_corporation.www.uthaopartner",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile(String imageName) throws IOException {
        // Create an image file name
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPathForTakingImage = image.getAbsolutePath();
        try {
            Log.i(TAG,"mCurrentPhotoPathForTakingImage: " +mCurrentPhotoPathForTakingImage);
        } catch (RuntimeException e){
            e.printStackTrace();
            Log.i(TAG,"createImageFile.RuntimeException: " + e.getMessage());
        }
        return image;
    }

    private void checkAndRequestCameraPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(Welcome.this,
                Manifest.permission.CAMERA);
        if (permissionCheck == PackageManager.PERMISSION_DENIED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(Welcome.this,
                    Manifest.permission.CAMERA)){
                //If this if statement return true that means user have seen this permission already
                Toast.makeText(this, "Please allow camera permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(Welcome.this,new String[]{Manifest
                        .permission.CAMERA},requestCameraPermissionCode);
            }
        }


    }

    private void sendTakenImageToServer(final String documentName, String documentPath) {
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (Common.driverOrBiker == null){
            getDriverOrBiker();
        }

        //Below we will save our customer's profile picture to firebase storage.
        StorageReference filePath = FirebaseStorage.getInstance().getReference().
                child(Common.driverOrBiker + "_Documents")
                .child(Common.driverUID)
                .child(Common.driverOrBiker + "_Scanned_Documents")
                .child(documentName);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(documentPath);
            //As firebase storage has limited free space, so we will first compress our image.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            //Now we will convert this bitmap image into an array
            byte[] imgData = baos.toByteArray();
            switch (documentName){
                case "Driving_License":
                    DrivingLicense drivingLicense = new DrivingLicense();
                    drivingLicense.setDriverUid(Common.driverUID);
                    drivingLicense.setDriving_License(imgData);
                    mViewModel.addDrivingLicense(drivingLicense);
                    break;
                case "NID_FRONT":
                    NIDFRONT nidfront = new NIDFRONT();
                    nidfront.setDriverUid(Common.driverUID);
                    nidfront.setNID_FRONT(imgData);
                    mViewModel.addNidFront(nidfront);
                    break;
                case "NID_BACK":
                    NIDBACK nidback = new NIDBACK();
                    nidback.setDriverUid(Common.driverUID);
                    nidback.setNID_BACK(imgData);
                    mViewModel.addNidBack(nidback);
                    break;
                case "FITNESS_CERTIFICATE":
                    FITNESS fitness = new FITNESS();
                    fitness.setDriverUid(Common.driverUID);
                    fitness.setFITNESS_CERTIFICATE(imgData);
                    mViewModel.addFitness(fitness);
                    break;
                case "TAX_TOKEN":
                    TaxToken taxToken = new TaxToken();
                    taxToken.setDriverUid(Common.driverUID);
                    taxToken.setTAX_TOKEN(imgData);
                    mViewModel.addTaxToken(taxToken);
                    break;
                case "REGISTRATION_PAPER":
                    RegPaper regPaper = new RegPaper();
                    regPaper.setDriverUid(Common.driverUID);
                    regPaper.setREGISTRATION_PAPER(imgData);
                    mViewModel.addRegPaper(regPaper);
                    break;
            }
            //Now we will upload this array into our firebase storage.
            UploadTask uploadTask = filePath.putBytes(imgData);
            final String finalDriverOrBiker = Common.driverOrBiker;
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    btnUploadPhotoToServer.setEnabled(true);
                    progressDialog.dismiss();
                    previewAndUploadLayout.setVisibility(View.GONE);
                    Uri imageURI = null;
                    imgScannedDocumentShownByGlide.setImageURI(imageURI);
                    Runtime.getRuntime().gc();
                    if (Common.driverOrBiker.equals("Driver")){
                        scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                        editor.putString("Layout", "scannedDocumentsUploadLayoutForCar");
                    } else if (Common.driverOrBiker.equals("Biker")){
                        scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                        editor.putString("Layout", "scannedDocumentsUploadLayoutForBike");
                    }
                    editor.commit();
                    getWhichLayOutIsWrittenInSharedPreferences();
                    //After putting our image to firebase storage. We will create a link of this image
                    //in our firebase database under customer 'customerUID' child
                    Uri documentDownloadUri = taskSnapshot.getDownloadUrl();
//                    Toast.makeText(CustomerSettingsActivity.this,"Download Url: " +
//                    documentDownloadUri.toString(),Toast.LENGTH_LONG).show();
                    Log.i("DownloadUrl", documentDownloadUri.toString());

                    HashMap<String, Object> document_url = new HashMap();
                    document_url.put(documentName, documentDownloadUri.toString());

                    DatabaseReference mDriverScannedDocumentUrlDB = FirebaseDatabase.getInstance().
                            getReference("Users")
                            .child(Common.driverOrBiker + "s")
                            .child(Common.driverUID)
                            .child(Common.driverOrBiker + "_Scanned_Documents");
                    mDriverScannedDocumentUrlDB.updateChildren(document_url).addOnCompleteListener
                            (new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseCrash.log("Welcome:RealDB:ScannedDoc Update: Success");
                                        Crashlytics.log("Welcome:RealDB:ScannedDoc Update: Success");
                                        mCurrentPhotoPathForTakingImage = null;
                                        mCurrentPhotoPathForTakingImageFromGallery = null;
                                        Runtime.getRuntime().gc();
                                    } else {
                                        FirebaseCrash.log("Welcome:RealDB:ScannedDoc Update: failed");
                                        Crashlytics.log("Welcome:RealDB:ScannedDoc Update: failed");
                                    }
                                }
                            });

                    if (checkWhetherUploadingDocumentFireStoreToFirstTime(Common.driverOrBiker)){
                        putCheckMarkOnSuccessfullyUploadedDocument(documentName, Common.driverOrBiker);
                        checkWhetherAllTheDocumentIsUploadedToServer(Common.driverOrBiker);
                        Log.i(TAG,"Uploading Document to Fire store For the first time");
                        FirebaseFirestore mFireStoreDriverScannedDocumentUrlDB = FirebaseFirestore.getInstance();
                        mFireStoreDriverScannedDocumentUrlDB.collection("Users")
                                .document(Common.driverOrBiker + "s")
                                .collection(Common.driverUID)
                                .document(Common.driverOrBiker + "_Scanned_Documents")
                                .set(document_url)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseCrash.log("Welcome:fireStore:ScannedDoc Update: Success");
                                            Crashlytics.log("Welcome:fireStore:ScannedDoc Update: Success");
                                        } else {
                                            FirebaseCrash.log("Welcome:fireStore:ScannedDoc Update: failed");
                                            Crashlytics.log("Welcome:fireStore:ScannedDoc Update: failed");
                                        }
                                    }
                                });
                    } else {
                        Log.i(TAG,"Uploading Document to Fire store For later time");
                        putCheckMarkOnSuccessfullyUploadedDocument(documentName, Common.driverOrBiker);
                        checkWhetherAllTheDocumentIsUploadedToServer(Common.driverOrBiker);
                        FirebaseFirestore mFireStoreDriverScannedDocumentUrlDB = FirebaseFirestore.getInstance();
                        mFireStoreDriverScannedDocumentUrlDB.collection("Users")
                                .document(Common.driverOrBiker + "s")
                                .collection(Common.driverUID)
                                .document(Common.driverOrBiker + "_Scanned_Documents")
                                .update(document_url)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseCrash.log("Welcome:fireStore:ScannedDoc Update: Success");
                                            Crashlytics.log("Welcome:fireStore:ScannedDoc Update: Success");
                                            Runtime.getRuntime().gc();
                                        } else {
                                            FirebaseCrash.log("Welcome:fireStore:ScannedDoc Update: failed");
                                            Crashlytics.log("Welcome:fireStore:ScannedDoc Update: failed");
                                        }
                                    }
                                });
                    }


//                saveCustomerPicToSqliteDB(imgData);


                }
            });

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    btnUploadPhotoToServer.setEnabled(true);
                    progressDialog.dismiss();
                    Toast.makeText(Welcome.this, "Something went wrong. Upload failed....",
                            Toast.LENGTH_LONG).show();
                    Log.i(TAG, "Something went wrong. Upload failed....");

                }
            });
            progressDialog = new ProgressDialog(Welcome.this, R.style.StyledDialog);
            progressDialog.setMax(100);
            progressDialog.setMessage("Uploading....");
            setProgressDialogTitle(documentName);
            progressDialog.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.show();
            progressDialog.setCancelable(false);
            uploadTask.addOnProgressListener(Welcome.this,
                    new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) /
                                    taskSnapshot.getTotalByteCount();
                            Log.i(TAG, "Upload is " + progress + "% done");
                            int currentprogress = (int) progress;
                            progressDialog.incrementProgressBy(currentprogress);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError error){
            error.printStackTrace();
            Log.i(TAG, "OutOfMemoryError: " + error.getMessage());
            Toast.makeText(this, "Low Memory in RAM", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "Upload Again", Toast.LENGTH_LONG).show();
        }


    }

    private void checkWhetherAllTheDocumentIsUploadedToServer(String finalDriverOrBiker) {
        boolean Driving_License = preferences.getBoolean("Driving_License",false);
        boolean NID_FRONT = preferences.getBoolean("NID_FRONT",false);
        boolean NID_BACK = preferences.getBoolean("NID_BACK",false);
        boolean FITNESS_CERTIFICATE = preferences.getBoolean("FITNESS_CERTIFICATE",false);
        boolean TAX_TOKEN = preferences.getBoolean("TAX_TOKEN",false);
        boolean REGISTRATION_PAPER = preferences.getBoolean("REGISTRATION_PAPER",false);

        if (finalDriverOrBiker.equals("Driver")){
            if (Driving_License && NID_FRONT && NID_BACK && FITNESS_CERTIFICATE &&
                    TAX_TOKEN && REGISTRATION_PAPER ){
                btnAddPaymentDetailsForCar.setVisibility(View.VISIBLE);
                btnAddPaymentDetailsForCar.setEnabled(true);
            }
        }

        if (finalDriverOrBiker.equals("Biker")){
            if (Driving_License && NID_FRONT && NID_BACK && REGISTRATION_PAPER ){
                btnAddPaymentDetailsForBike.setVisibility(View.VISIBLE);
                btnAddPaymentDetailsForBike.setEnabled(true);
            }
        }



    }

    private boolean checkWhetherUploadingDocumentFireStoreToFirstTime(String driverOrBiker){
        boolean uploading_to_fire_store_first_time = true;

        boolean Driving_License = preferences.getBoolean("Driving_License",false);
        boolean NID_FRONT = preferences.getBoolean("NID_FRONT",false);
        boolean NID_BACK = preferences.getBoolean("NID_BACK",false);
        boolean FITNESS_CERTIFICATE = preferences.getBoolean("FITNESS_CERTIFICATE",false);
        boolean TAX_TOKEN = preferences.getBoolean("TAX_TOKEN",false);
        boolean REGISTRATION_PAPER = preferences.getBoolean("REGISTRATION_PAPER",false);

        if (driverOrBiker.equals("Driver")){
            if (!Driving_License && !NID_FRONT && !NID_BACK && !FITNESS_CERTIFICATE &&
                    !TAX_TOKEN && !REGISTRATION_PAPER ){
                //This means uploading to firestore for the first time
                uploading_to_fire_store_first_time = true;
                return uploading_to_fire_store_first_time;
            } else {
                uploading_to_fire_store_first_time = false;
                return uploading_to_fire_store_first_time;
            }
        } else if (driverOrBiker.equals("Biker")){
            if (!Driving_License && !NID_FRONT && !NID_BACK && !REGISTRATION_PAPER ){
                //This means uploading to firestore for the first time
                uploading_to_fire_store_first_time = true;
                return uploading_to_fire_store_first_time;
            } else {
                uploading_to_fire_store_first_time = false;
                return uploading_to_fire_store_first_time;
            }
        }
        Log.i(TAG,"uploading_to_fire_store_first_time: " + uploading_to_fire_store_first_time);
        Log.i(TAG,"Driving_License: " + Driving_License + "\n" +
                "NID_FRONT: " + NID_FRONT + "\n" +
                "NID_BACK: " + NID_BACK + "\n" +
                "FITNESS_CERTIFICATE: " + FITNESS_CERTIFICATE + "\n" +
                "TAX_TOKEN: " + TAX_TOKEN + "\n" +
                "REGISTRATION_PAPER: " + REGISTRATION_PAPER);
        return uploading_to_fire_store_first_time;
    }

    private void putCheckMarkOnSuccessfullyUploadedDocument(String documentName,
                                                            String driverOrBiker) {
        switch (documentName){
            case "Driving_License":
                if (Common.driverOrBiker.equals("Driver")){
                    if (imgDocumentUploadStatusDrivingLicenseForCar == null){
                        imgDocumentUploadStatusDrivingLicenseForCar =
                                findViewById(R.id.imgDocumentUploadStatusDrivingLicenseForCar);
                        imgDocumentUploadStatusDrivingLicenseForCar.setImageDrawable
                                (getResources().getDrawable(R.drawable.ic_check_mark));
                    } else {
                        imgDocumentUploadStatusDrivingLicenseForCar.setImageDrawable
                                (getResources().getDrawable(R.drawable.ic_check_mark));
                    }

                } else if (Common.driverOrBiker.equals("Biker")){
                    if (imgDocumentUploadStatusDrivingLicenseForBike == null){
                        imgDocumentUploadStatusDrivingLicenseForBike =
                                findViewById(R.id.imgDocumentUploadStatusDrivingLicenseForBike);
                        imgDocumentUploadStatusDrivingLicenseForBike.setImageDrawable
                                (getResources().getDrawable(R.drawable.ic_check_mark));
                    } else {
                        imgDocumentUploadStatusDrivingLicenseForBike.setImageDrawable
                                (getResources().getDrawable(R.drawable.ic_check_mark));
                    }
                }
                Number_Of_Document_Uploaded_To_Server = Number_Of_Document_Uploaded_To_Server + 1;
                editor.putInt("Number_Of_Document_Uploaded_To_Server",Number_Of_Document_Uploaded_To_Server);
                editor.putBoolean("Driving_License",true);
                editor.commit();
                Log.i(TAG,"Number_Of_Document_Uploaded_To_Server: " + Number_Of_Document_Uploaded_To_Server);
                break;

            case "NID_FRONT":
                if (Common.driverOrBiker.equals("Driver")){
                    imgDocumentUploadStatusNIDFrontSideForCar.setImageDrawable
                            (getResources().getDrawable(R.drawable.ic_check_mark));
                } else if (Common.driverOrBiker.equals("Biker")){
                    imgDocumentUploadStatusNIDFrontSideForBike.setImageDrawable
                            (getResources().getDrawable(R.drawable.ic_check_mark));
                }
                Number_Of_Document_Uploaded_To_Server = Number_Of_Document_Uploaded_To_Server + 1;
                editor.putInt("Number_Of_Document_Uploaded_To_Server",Number_Of_Document_Uploaded_To_Server);
                editor.putBoolean("NID_FRONT",true);
                editor.commit();
                Log.i(TAG,"Number_Of_Document_Uploaded_To_Server: " + Number_Of_Document_Uploaded_To_Server);
                break;

            case "NID_BACK":
                if (Common.driverOrBiker.equals("Driver")){
                    imgDocumentUploadStatusNIDBackSideForCar.setImageDrawable
                            (getResources().getDrawable(R.drawable.ic_check_mark));
                } else if (Common.driverOrBiker.equals("Biker")){
                    imgDocumentUploadStatusNIDBackSideForBike.setImageDrawable
                            (getResources().getDrawable(R.drawable.ic_check_mark));
                }
                Number_Of_Document_Uploaded_To_Server = Number_Of_Document_Uploaded_To_Server + 1;
                editor.putInt("Number_Of_Document_Uploaded_To_Server",Number_Of_Document_Uploaded_To_Server);
                editor.putBoolean("NID_BACK",true);
                editor.commit();
                Log.i(TAG,"Number_Of_Document_Uploaded_To_Server: " + Number_Of_Document_Uploaded_To_Server);
                break;

            case "FITNESS_CERTIFICATE":
                imgDocumentUploadStatusCarFitness.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_check_mark));
                Number_Of_Document_Uploaded_To_Server = Number_Of_Document_Uploaded_To_Server + 1;
                editor.putInt("Number_Of_Document_Uploaded_To_Server",Number_Of_Document_Uploaded_To_Server);
                editor.putBoolean("FITNESS_CERTIFICATE",true);
                editor.commit();
                Log.i(TAG,"Number_Of_Document_Uploaded_To_Server: " + Number_Of_Document_Uploaded_To_Server);
                break;

            case "TAX_TOKEN":
                imgDocumentUploadStatusCarTaxToken.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_check_mark));
                Number_Of_Document_Uploaded_To_Server = Number_Of_Document_Uploaded_To_Server + 1;
                editor.putInt("Number_Of_Document_Uploaded_To_Server",Number_Of_Document_Uploaded_To_Server);
                editor.putBoolean("TAX_TOKEN",true);
                editor.commit();
                Log.i(TAG,"Number_Of_Document_Uploaded_To_Server: " + Number_Of_Document_Uploaded_To_Server);
                break;

            case "REGISTRATION_PAPER":
                if (Common.driverOrBiker.equals("Driver")){
                    imgDocumentUploadStatusCarRegistration.setImageDrawable
                            (getResources().getDrawable(R.drawable.ic_check_mark));
                } else if (Common.driverOrBiker.equals("Biker")){
                    imgDocumentUploadStatusBikeRegistration.setImageDrawable
                            (getResources().getDrawable(R.drawable.ic_check_mark));
                }

                Number_Of_Document_Uploaded_To_Server = Number_Of_Document_Uploaded_To_Server + 1;
                editor.putInt("Number_Of_Document_Uploaded_To_Server",Number_Of_Document_Uploaded_To_Server);
                editor.putBoolean("REGISTRATION_PAPER",true);
                editor.commit();
                Log.i(TAG,"Number_Of_Document_Uploaded_To_Server: " + Number_Of_Document_Uploaded_To_Server);
                break;
        }
    }

    private void setProgressDialogTitle(String documentName) {
        switch (documentName) {
            case "Driving_License":
                progressDialog.setTitle("Driving License");
                break;

            case "NID_FRONT":
                progressDialog.setTitle("NID FRONT");
                break;

            case "NID_BACK":
                progressDialog.setTitle("NID BACK");
                break;

            case "CAR_FITNESS_CERTIFICATE":
                progressDialog.setTitle("CAR FITNESS CERTIFICATE");
                break;

            case "CAR_TAX_TOKEN":
                progressDialog.setTitle("CAR TAX TOKEN");
                break;

            case "CAR_REGISTRATION_PAPER":
                progressDialog.setTitle("CAR REGISTRATION PAPER");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "requestCode: " + requestCode + "\n" +
                "resultCode: " + resultCode);
        //This below code is for taking document picture using camera
        if (requestCode == REQUEST_IMAGE_CAPTURE
                && resultCode == RESULT_OK) {
            try{
                data = null;
                //Fresco
                Uri imageUri= Uri.fromFile(new File(mCurrentPhotoPathForTakingImage));// For files on device
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver() , imageUri);
//                imgScannedDocumentShownByGlide.setImageURI(imageUri);
                Glide.with(this).load(imageUri).into(imgScannedDocumentShownByGlide);
            } catch (RuntimeException e){
                e.printStackTrace();
                Log.i(TAG,"RuntimeException: " + e.getMessage());
            } catch (OutOfMemoryError error){
                error.printStackTrace();
                Log.i(TAG,"OutOfMemoryError: onActivityResult" + error.getMessage());
                Toast.makeText(this, "Low Memory in RAM", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Take Picture Again", Toast.LENGTH_LONG).show();
            }

            //Get Dimension Of The Image. Don't call this below method
            //it leaks memory
//            getDimensionOfTheBitMap(mCurrentPhotoPathForTakingImage);
            imageTakenUpTOAppUsing = "Camera";

            chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
            previewAndUploadLayout.setVisibility(View.VISIBLE);
            editor.putString("Layout", "previewAndUploadLayout");
            editor.commit();
            getWhichLayOutIsWrittenInSharedPreferences();
        } else if ((requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED)){
            String vehicle_type =  preferences.getString("Vehicle_Type",DEFAULT);
            if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Car")){
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                previewAndUploadLayout.setVisibility(View.GONE);
                scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                editor.putString("Layout", "scannedDocumentsUploadLayoutForCar");
            } else if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Bike")){
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                previewAndUploadLayout.setVisibility(View.GONE);
                scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                editor.putString("Layout", "scannedDocumentsUploadLayoutForBike");
            }
            editor.commit();
            getWhichLayOutIsWrittenInSharedPreferences();
        }

        //This below code is for uploading document image from gallery
        if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS &&
                resultCode == Activity.RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            imageGalleryPathUri = imageUri;
            Log.i(TAG,"imageUri: " + imageUri);
            FirebaseCrash.log("imageUri: " + imageUri);
            Crashlytics.log("imageUri: " + imageUri);
            try{
                //Fresco
//                imgScannedDocumentShownByGlide.setImageURI(imageUri);
                Glide.with(this).load(imageUri).into(imgScannedDocumentShownByGlide);
            } catch (RuntimeException e){
                Log.i(TAG,"RuntimeException: " + e.getMessage());
            } catch (OutOfMemoryError error){
                error.printStackTrace();
                Log.i(TAG,"OutOfMemoryError: " + error.getMessage());
                Toast.makeText(this, "Low Memory in RAM", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Upload Picture Again", Toast.LENGTH_LONG).show();
            }
//            imgScannedDocumentShownByGlide.setImageURI(imageUri);
            //Now we set this image to image view. Here we only set this image to image view
            //no on the server.
            // Let's read picked image path using content resolver
            String[] filePath = { MediaStore.Images.Media.DATA };
            Log.i(TAG,"Img: " + filePath.toString());
            if (imageUri != null){
                Cursor cursor = getContentResolver().query(imageUri, filePath, null,
                        null, null);
                if (cursor != null && cursor.getColumnCount() > 0) {
                    int numberOfColumn = cursor.getColumnCount();
                    cursor.moveToFirst();
                    String firstColumnName = cursor.getColumnName(0);
                    Log.i(TAG,"Number of Column in Cursor: " + numberOfColumn + "\n" +
                            "Column 0 Name: " + firstColumnName);
                    mCurrentPhotoPathForTakingImageFromGallery
                            = cursor.getString(cursor.getColumnIndex(filePath[0]));
                    cursor.close();
                    // At the end remember to close the cursor or you will end with the RuntimeException!
                }

                if (mCurrentPhotoPathForTakingImageFromGallery != null){
                    Log.i(TAG, mCurrentPhotoPathForTakingImageFromGallery);
                } else {
                    new openAndReadInputStreamInBackgroundThread().execute(imageUri);
                }
                imageTakenUpTOAppUsing = "Gallery";
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                previewAndUploadLayout.setVisibility(View.VISIBLE);
                editor.putString("Layout", "previewAndUploadLayout");
                editor.commit();
                getWhichLayOutIsWrittenInSharedPreferences();

            }

        } else if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS &&
                resultCode == Activity.RESULT_CANCELED){
            String vehicle_type =  preferences.getString("Vehicle_Type",DEFAULT);
            if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Car")){
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                previewAndUploadLayout.setVisibility(View.GONE);
                scannedDocumentsUploadLayoutForCar.setVisibility(View.VISIBLE);
                editor.putString("Layout", "scannedDocumentsUploadLayoutForCar");
            } else if (!vehicle_type.equals(DEFAULT) && vehicle_type.equals("Bike")){
                chooseFromGalleryOrTakePhoto.setVisibility(View.GONE);
                previewAndUploadLayout.setVisibility(View.GONE);
                scannedDocumentsUploadLayoutForBike.setVisibility(View.VISIBLE);
                editor.putString("Layout", "scannedDocumentsUploadLayoutForBike");
            }
            editor.commit();
            getWhichLayOutIsWrittenInSharedPreferences();

        }

        //This below code for uploading driver profile pic from gallery
        if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS_FOR_DRIVER_PROFILE_PIC &&
                resultCode == Activity.RESULT_OK && data != null) {
            //Here we will set driver profile pic from intent data
            //But below we will upload real picture from path
            Uri imageUriForProfilePic = data.getData();
            Log.i(TAG,"imageUri: " + imageUriForProfilePic);
            FirebaseCrash.log("imageUri: " + imageUriForProfilePic);
            Crashlytics.log("imageUri: " + imageUriForProfilePic);
            imageGalleryPathUriForDriverProfilePic = imageUriForProfilePic;
            try {
                Bitmap bitmap = getThumbnail(imageUriForProfilePic,240); //80dp * 3
                driverProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            driverProfilePic.setImageURI(imageUriForProfilePic);
            //Now we set this image to image view. Here we only set this image to image view
            //no on the server.
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Log.i(TAG,"Img: " + filePath.toString());
            if (imageUriForProfilePic != null) {
                Cursor cursor = getContentResolver().query(imageUriForProfilePic, filePath, null,
                        null, null);
                if (cursor != null && cursor.getColumnCount() > 0){
                    int numberOfColumn = cursor.getColumnCount();
                    cursor.moveToFirst();
                    String firstColumnName = cursor.getColumnName(0);
                    Log.i(TAG,"Number of Column in Cursor: " + numberOfColumn + "\n" +
                    "Column 0 Name: " + firstColumnName);
                    mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic = cursor
                            .getString(cursor.getColumnIndex(filePath[0]));
                    // At the end remember to close the cursor or you will end with the RuntimeException!
                    cursor.close();
                }
                if (mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic != null){
                    Log.i(TAG, mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic);
                } else {
                    new openAndReadInputStreamInBackgroundThread().execute(imageUriForProfilePic);
                }

            }
        }



        //This below code for uploading biker profile pic from gallery
        if (requestCode == REQUEST_CODE_FOR_GALLERY_ACCESS_FOR_BIKER_PROFILE_PIC &&
                resultCode == Activity.RESULT_OK && data != null) {
            //Here we will set biker profile pic from intent data
            //But below we will upload real picture from path
            Uri imageUriForProfilePic = data.getData();
            Log.i(TAG,"imageUri: " + imageUriForProfilePic);
            FirebaseCrash.log("imageUri: " + imageUriForProfilePic);
            Crashlytics.log("imageUri: " + imageUriForProfilePic);
            imageGalleryPathUriForBikerProfilePic = imageUriForProfilePic;
            try {
                Bitmap bitmap = getThumbnail(imageUriForProfilePic,240); //80dp * 3
                bikerProfilePic.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
//            driverProfilePic.setImageURI(imageUriForProfilePic);
            //Now we set this image to image view. Here we only set this image to image view
            //no on the server.
            // Let's read picked image path using content resolver
            String[] filePath = {MediaStore.Images.Media.DATA};
            Log.i(TAG,"Img: " + filePath.toString());
            if (imageUriForProfilePic != null) {
                Cursor cursor = getContentResolver().query(imageUriForProfilePic, filePath, null,
                        null, null);
                if (cursor != null && cursor.getColumnCount() > 0){
                    int numberOfColumn = cursor.getColumnCount();
                    cursor.moveToFirst();
                    String firstColumnName = cursor.getColumnName(0);
                    Log.i(TAG,"Number of Column in Cursor: " + numberOfColumn + "\n" +
                            "Column 0 Name: " + firstColumnName);
                    mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic = cursor
                            .getString(cursor.getColumnIndex(filePath[0]));
                    cursor.close();
                    // At the end remember to close the cursor or you will end with the RuntimeException!
                }

                if (mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic != null){
                    Log.i(TAG, mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic);
                } else {
                    new openAndReadInputStreamInBackgroundThread().execute(imageUriForProfilePic);
                }

            }
        }

    }

    private void getDimensionOfTheBitMap(String bitmapsCurrentPath) {
        //This method leaks memory
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(bitmapsCurrentPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;
        if (photoH == 0){
            Toast.makeText(this, "Please take photo again", Toast.LENGTH_LONG).show();
        }
        Log.i(TAG,"Photo Height: " + photoH + "\n" +
                "Photo Width: " + photoW );
    }

    private String getImagePathFromInputStreamUri(Uri uri) {
        InputStream inputStream = null;
        String filePath = null;

        if (uri.getAuthority() != null) {
            try {
                inputStream = getContentResolver().openInputStream(uri); // context needed
                File photoFile = createTemporalFileFrom(inputStream);

                filePath = photoFile.getPath();

            } catch (FileNotFoundException e) {
                // log
            } catch (IOException e) {
                // log
            }finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return filePath;
    }

    private File createTemporalFileFrom(InputStream inputStream) throws IOException {
        File targetFile = null;

        if (inputStream != null) {
            int read;
            byte[] buffer = new byte[8 * 1024];

            targetFile = createTemporalFile();
            OutputStream outputStream = new FileOutputStream(targetFile);

            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            outputStream.flush();

            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private File createTemporalFile() {
        return new File(getExternalCacheDir(), "tempFile.jpg"); // context needed
    }

    private class openAndReadInputStreamInBackgroundThread extends AsyncTask<Uri,Void,String>{

        @Override
        protected String doInBackground(Uri... uris) {
            String imagePath = getImagePathFromInputStreamUri(uris[0]);
            if (imageGalleryPathUriForDriverProfilePic != null){
                mCurrentPhotoPathForTakingImageFromGalleryForDriverProfilePic = imagePath;
            } else if (imageGalleryPathUriForBikerProfilePic != null){
                mCurrentPhotoPathForTakingImageFromGalleryForBikerProfilePic = imagePath;
            } else if (imageGalleryPathUri != null){
                mCurrentPhotoPathForTakingImageFromGallery = imagePath;
            }
            return imagePath;
        }
    }

    public void isGooglePlayServicesAvailable() {
        FirebaseCrash.log("Welcome: isGooglePlayServicesAvailable.called");
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (status != ConnectionResult.SUCCESS) {
            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(Welcome.this, status, 2404).show();
            }
            final String GooglePlayServicesURL = "https://play.google.com/store/apps/details?id=com.google.android.gms";
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("Download Google Play Service")
                    .setMessage("Please download google play service from app store")
                    .setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            openWebPage(GooglePlayServicesURL);
                        }
                    }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            dialog.show();
        }

    }

    public void openWebPage(String url) {
        FirebaseCrash.log("Home:AppVersion:openWebPage.called");
//        Crashlytics.log("Home:AppVersion:openWebPage.called");

        Uri webpage = Uri.parse(url);

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            webpage = Uri.parse("http://" + url);
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private boolean checkGPSConnectionPermission() {
        FirebaseCrash.log("Welcome: checkGPSConnectionPermission.called");
        boolean gpsStatus = false;
        if (ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION}, GPS_PERMISSION_REQUEST);

            }
            //That means we don't have gps permission
            gpsStatus = false;
        } else {
            //That means we have gps permission
            gpsStatus =true;
        }
        return gpsStatus;
    }

    private  boolean checkAndRequestSMSPermissions() {
        FirebaseCrash.log("Welcome: checkAndRequestSMSPermissions.called");
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS);
        int receiveSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS);
        int readSMS = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS);

        List<String> listPermissionsNeeded = new ArrayList<>();
        if (receiveSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.RECEIVE_SMS);
        }
        if (readSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.SEND_SMS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_SMS_PERMISSIONS);
            return false;
        }
        return true;
    }

    private void checkWhetherUserIsRegisteredOrNot() {
        FirebaseCrash.log("Welcome: checkWhetherUserIsRegisteredOrNot.called");
        if (mDriverAuth != null  && mDriverAuth.getCurrentUser() != null){
            if (Common.driverUID == null){
                Common.driverUID = mDriverAuth.getCurrentUser().getUid();
            }
            FirebaseCrash.log("WelcomeActivity: Customer is registered, ID: " + Common.driverUID);
            //Now we will check whether customer is registering for the first time.
            //If first time registration & activity get destroyed than user get
            //access to Home.class without facebook or email sign up.
            //so below we will check whether user done either facebook or email
            //sign up or not
            String registration_status = preferences.getString("Registration_Status",DEFAULT);
            if (!registration_status.equals(DEFAULT)){
//                mDriverAuth.removeAuthStateListener(mDriverAuthListener);
                myTrace.stop();
                Log.i(TAG,"Customer Registration process fully completed");
                Intent intent = new Intent(Welcome.this, Home.class);
                startActivity(intent);
                finish();
            } else {
                Log.i(TAG,"Customer Registration in progress");
            }

        }
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.contains("+");
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private void getDeviceConfiguration(String vehicle_type){
        String _OSVERSION = System.getProperty("os.version");
        String AndroidVersion = android.os.Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        String _DEVICE = android.os.Build.DEVICE;
        String Device_Model = android.os.Build.MODEL;
        String _PRODUCT = android.os.Build.PRODUCT;
        String Device_Brand = android.os.Build.BRAND;
        String _DISPLAY = android.os.Build.DISPLAY;
        String _CPU_ABI = android.os.Build.CPU_ABI;
        String _CPU_ABI2 = android.os.Build.CPU_ABI2;
        String _UNKNOWN = android.os.Build.UNKNOWN;
        String _HARDWARE = android.os.Build.HARDWARE;
        String _ID = android.os.Build.ID;
        String _MANUFACTURER = android.os.Build.MANUFACTURER;
        String _SERIAL = android.os.Build.SERIAL;
        String _USER = android.os.Build.USER;
        String _HOST = android.os.Build.HOST;
        int _NUM_OF_CPU_CORES = getNumCores();
        String TotalRAM = getTotalRAM();

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int device_available_width = metrics.widthPixels;
        int device_available_height = metrics.heightPixels;
        int device_density = metrics.densityDpi;

        Point size = new Point();
        int device_actual_width = 0;
        int device_actual_height = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(size);
            device_actual_width = size.x;
            device_actual_height =size.y;
        }

        Log.i("Check",
                "_OSVERSION: " + _OSVERSION + "\n" +
                        "AndroidVersion: " + AndroidVersion + "\n" +
                        "sdkVersion: " + sdkVersion + "\n" +
                        "_DEVICE: " + _DEVICE + "\n" +
                        "Device_Model: " + Device_Model + "\n" +
                        "_PRODUCT: " + _PRODUCT + "\n" +
                        "Device_Brand: " + Device_Brand + "\n" +
                        "_DISPLAY: " + _DISPLAY + "\n" +
                        "_CPU_ABI: " + _CPU_ABI + "\n" +
                        "_CPU_ABI2: " + _CPU_ABI2 + "\n" +
                        "_UNKNOWN: " + _UNKNOWN + "\n" +
                        "_HARDWARE: " + _HARDWARE + "\n" +
                        "_ID: " + _ID + "\n" +
                        "_MANUFACTURER: " + _MANUFACTURER + "\n" +
                        "_SERIAL: " + _SERIAL + "\n" +
                        "_USER: " + _USER + "\n" +
                        "_HOST: " + _HOST + "\n" +
                        "_NUM_OF_CPU_CORES: " + _NUM_OF_CPU_CORES + "\n" +
                        "TotalRAM: " + TotalRAM + "\n" );
        Log.i("Check","device_available_width: " + device_available_width + "\n" +
                "device_available_height: " + device_available_height + "\n" +
                "device_actual_width: " + device_actual_width + "\n" +
                "device_actual_height: " + device_actual_height);


        HashMap map = new HashMap();
        map.put("AndroidVersion",AndroidVersion);
        map.put("sdkVersion",sdkVersion);
        map.put("Device_Model",Device_Model);
        map.put("Device_Brand",Device_Brand);
        map.put("TotalRAM",TotalRAM);
        map.put("_NUM_OF_CPU_CORES",_NUM_OF_CPU_CORES);
        map.put("device_available_width",device_available_width);
        map.put("device_available_height",device_available_height);
        map.put("device_actual_width",device_actual_width);
        map.put("device_actual_height",device_actual_height);
        map.put("device_density",device_density);

        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        String driverOrBiker = "N/A";
        if (vehicle_type.equals("Car")){
            Common.driverOrBiker = "Driver";
        } else if (vehicle_type.equals("Bike")){
            Common.driverOrBiker = "Biker";
        }
        DatabaseReference mCustomerDeviceRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child("Device_Specification");
        mCustomerDeviceRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome:RealTime:Device_Specification: success");
                    Crashlytics.log("Welcome:RealTime:Device_Specification: success");
                } else {
                    FirebaseCrash.log("Welcome:RealTime:Device_Specification: failed");
                    Crashlytics.log("Welcome:RealTime:Device_Specification: failed");
                }
            }
        });

        HashMap<String, Object> fireMap = new HashMap();
        fireMap.put("AndroidVersion",AndroidVersion);
        fireMap.put("sdkVersion",sdkVersion);
        fireMap.put("Device_Model",Device_Model);
        fireMap.put("Device_Brand",Device_Brand);
        fireMap.put("TotalRAM",TotalRAM);
        fireMap.put("_NUM_OF_CPU_CORES",_NUM_OF_CPU_CORES);
        fireMap.put("device_available_width",device_available_width);
        fireMap.put("device_available_height",device_available_height);
        fireMap.put("device_actual_width",device_actual_width);
        fireMap.put("device_actual_height",device_actual_height);
        fireMap.put("device_density",device_density);

        FirebaseFirestore mFireCustomerDeviceRef = FirebaseFirestore.getInstance();
        mFireCustomerDeviceRef.collection("Users")
                .document(Common.driverOrBiker + "s")
                .collection(Common.driverUID)
                .document("Device_Specification")
                .set(fireMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseCrash.log("Welcome:fireStore:Device_Specification: success");
                    Crashlytics.log("Welcome:fireStore:Device_Specification: success");
                } else {
                    FirebaseCrash.log("Welcome:fireStore:Device_Specification: failed");
                    Crashlytics.log("Welcome:fireStore:Device_Specification: failed");
                }
            }
        });
    }

    private void getThisDeviceResoulution() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        Point size = new Point();
        String real_resolution = "N/A";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.getDefaultDisplay().getRealSize(size);
            real_resolution = size.x + "x" + size.y;
        }

        Log.i("Check","width: " + width + "\n" +
                "height: " + height + "\n" +
                "resolution: " + real_resolution);
    }

    private int getScreenWidth() {
        FirebaseCrash.log("Home:getScreenWidth.called");
        Crashlytics.log("Home:getScreenWidth.called");
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    private int getScreenHeight() {
        FirebaseCrash.log("Home:getScreenHeight.called");
        Crashlytics.log("Home:getScreenHeight.called");
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }


    private int getNumCores() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    public String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }
        return load;
    }

    public Bitmap getThumbnail(Uri uri, int THUMBNAIL_SIZE) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither=true;//optional
        onlyBoundsOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig=Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    //***********************************Out Of Memory*************************************
    //***********************************Solution******************************************
    //*************************************Start*******************************************

    /**
     * Release memory when the UI becomes hidden or when system resources become low.
     * @param level the memory-related event that was raised.
     */
    @Override
    public void onTrimMemory(int level) {
        // Determine which lifecycle or system event was raised.
        switch (level) {

            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:

                /*
                   Release any UI objects that currently hold memory.

                   The user interface has moved to the background.
                */
//                Notice that your app receives the onTrimMemory() callback with
//                TRIM_MEMORY_UI_HIDDEN only when all the UI components of your app
//                process become hidden from the user. This is distinct from the onStop()
//                callback, which is called when an Activity instance becomes hidden,
//                which occurs even when the user moves to another activity in your app.
//                    So although you should implement onStop() to release activity resources
//                such as a network connection or to unregister broadcast receivers, you usually
//                should not release your UI resources until you receive onTrimMemory
//                (TRIM_MEMORY_UI_HIDDEN). This ensures that if the user navigates back
//                from another activity in your app, your UI resources are still available
//                to resume the activity quickly.
                    Log.i(TAG,"TRIM_MEMORY_UI_HIDDEN");
                break;

            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                Log.i(TAG,"TRIM_MEMORY_RUNNING_MODERATE");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                Log.i(TAG,"TRIM_MEMORY_RUNNING_LOW");
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                Log.i(TAG,"TRIM_MEMORY_RUNNING_CRITICAL");
                /*
                   Release any memory that your app doesn't need to run.

                   The device is running low on memory while the app is running.
                   The event raised indicates the severity of the memory-related event.
                   If the event is TRIM_MEMORY_RUNNING_CRITICAL, then the system will
                   begin killing background processes.
                */

                break;

            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                Log.i(TAG,"TRIM_MEMORY_BACKGROUND");
                /*
                   Release as much memory as the process can.

                   The app is on the LRU list and the system is running low on memory.
                   The event raised indicates where the app sits within the LRU list.
                   If the event is TRIM_MEMORY_COMPLETE, the process will be one of
                   the first to be terminated.
                */

                break;

            default:
                /*
                  Release any non-critical data structures.

                  The app received an unrecognized memory level value
                  from the system. Treat this as a generic low-memory message.
                */
                break;
        }
    }


    public void checkDeviceCurrentMemoryStatus() {

        // Before doing something that requires a lot of memory,
        // check to see whether the device is in a low memory state.
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();
        float availableMemory = memoryInfo.availMem;
        float threshold = memoryInfo.threshold;
        float totalMemory = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            totalMemory = memoryInfo.totalMem;
        }
        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in megabytes as LruCache takes an
        // int in its constructor.
        final int VMMemory = (int) (Runtime.getRuntime().maxMemory() / 1048576);
        Log.i(TAG,"availableMemory: " + availableMemory /1048576 + " M.B\n" +
                "threshold: " + threshold /1048576 + " M.B\n" +
                "VMMemory: " + VMMemory + " M.B\n" +
                "totalMemory: " + totalMemory /1073741824 + " G.B");

        if (!memoryInfo.lowMemory) {
            // Do memory intensive work ...
            Log.i(TAG,"Do memory intensive work");
        }
    }

    // Get a MemoryInfo object for the device's current memory status.
    private ActivityManager.MemoryInfo getAvailableMemory() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(
                ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        if (activityManager != null) {
            activityManager.getMemoryInfo(memoryInfo);
        }
        return memoryInfo;
    }

    //*************************************End*********************************************

    //Current Android version data
    public String currentVersion(){
        double release;
        try {
            release = Double.parseDouble(android.os.Build.VERSION.RELEASE);
            Log.i("Check","release: " +release);
        } catch (NumberFormatException e) {
            release = 0; // your default value
        }
        String codeName="Unsupported";//below Jelly bean OR above Oreo
        if(release>=4.1 && release<4.4)codeName="Jelly Bean";
        else if(release<5)codeName="Kit Kat";
        else if(release<6)codeName="Lollipop";
        else if(release<7)codeName="Marshmallow";
        else if(release<8)codeName="Nougat";
        else if(release<9)codeName="Oreo";
        return codeName+" v"+release+", API Level: "+Build.VERSION.SDK_INT;
    }

    //Request all the permission at once.
    private void requestGroupPermission(ArrayList<String> permissions, Activity activity){
        String[]permissionList = new String[permissions.size()];
        permissions.toArray(permissionList);
        Log.i("Check","Permission List Size: " + permissions.size());
        if (permissions.size() >= 1){
            ActivityCompat.requestPermissions(activity,permissionList,
                    Request_GROUP_PERMISSION);
        }

    }


    public void requestAllPermission(Activity activity) {
        //In permissionNeeded Arraylist we will put the permission for which we did not get user
        //approval yet.
        ArrayList<String> permissionNeeded = new ArrayList<>();
        //In permissionsAvailable Arraylist we will put all the permissions which
        // is required by this application
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        permissionsAvailable.add(android.Manifest.permission.READ_PHONE_STATE);
        permissionsAvailable.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsAvailable.add(android.Manifest.permission.READ_SMS);
        permissionsAvailable.add(android.Manifest.permission.RECEIVE_SMS);
        permissionsAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        permissionsAvailable.add(android.Manifest.permission.SEND_SMS);


        //Above we did not added camera permission. We will ask for camera permission
        //to user when we truly need it. Otherwise user will get angry.

        for (String permission : permissionsAvailable){
            if (ContextCompat.checkSelfPermission(activity,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                //We will check each permission in permissionsAvailable Arraylist, whether user already
                //approved the permission or not.
                //Execution will come to this block if user did not approved the permission. So we will
                //add this specefic permission in our permissionNeeded Arraylist.
                permissionNeeded.add(permission);
            }
        }
        requestGroupPermission(permissionNeeded, activity);
        Log.i("Check", "Permission Needed: " + permissionNeeded.toString());
    }

    public boolean checkHowManyPermissionAllowed(Context mContext) {
        boolean allPermissionAllowed = false;
        //In permissionNeeded Arraylist we will put the permission for which we did not get user
        //approval yet.
        ArrayList<String> permissionNeeded = new ArrayList<>();
        //In permissionsAvailable Arraylist we will put all the permissions which is required by this
        //application
        ArrayList<String> permissionsAvailable = new ArrayList<>();
        ArrayList<String> permissionsAllowed = new ArrayList<>();
        permissionsAvailable.add(android.Manifest.permission.READ_PHONE_STATE);
        permissionsAvailable.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        permissionsAvailable.add(android.Manifest.permission.READ_SMS);
        permissionsAvailable.add(android.Manifest.permission.RECEIVE_SMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissionsAvailable.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
//        permissionsAvailable.add(android.Manifest.permission.SEND_SMS);
        permissionsAvailable.add(android.Manifest.permission.INTERNET);
        //Above we did not added camera permission. We will ask for camera permission
        //to user when we truly need it. Otherwise user will get angry.

        for (String permission : permissionsAvailable){
            if (ContextCompat.checkSelfPermission(mContext,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                //We will check each permission in permissionsAvailable Arraylist, whether user already
                //approved the permission or not.
                //Execution will come to this block if user did not approved the permission. So we will
                //add this specefic permission in our permissionNeeded Arraylist.
                permissionNeeded.add(permission);
            } else {
                permissionsAllowed.add(permission);
            }
        }
        if (permissionsAvailable.size() == permissionsAllowed.size()){
            allPermissionAllowed = true;
        } else {
            allPermissionAllowed = false;
        }
        Log.i(TAG, "Permission Needed: " + permissionNeeded.toString() +
                "\nPermission Allowed: " + permissionsAllowed.toString() +
        "\nPermission Allowed Length: " + permissionsAllowed.size());
        return allPermissionAllowed;
    }


    private final int Request_READ_PHONE_STATE = 10001;
    private final int Request_FINE_LOCATION = 10002;
    private final int Request_SEND_SMS = 10003;
    private final int Request_READ_SMS = 10004;
    private final int Request_RECEIVE_SMS = 10005;
    private final int Request_WRITE_EXTERNAL_STORAGE = 10006;
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode){
            case Request_GROUP_PERMISSION:
                String result = "";
                int i = 0;

                for (String perm : permissions){
                    String status = "";
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        switch (i){
                            case 0:
                                AlertDialog.Builder builder = new AlertDialog.Builder(Welcome.this);
                                builder.setMessage("Without Device Phone State Permission This App Will Shut Down");
                                builder.setTitle("Allow Phone State Permission");
                                builder.setPositiveButton("Allow Phone State Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.READ_PHONE_STATE},
                                                Request_READ_PHONE_STATE);
                                    }
                                });
                                builder.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCancelable(false);
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.show();
                                break;

                            case 1:
                                AlertDialog.Builder builder4 = new AlertDialog.Builder(Welcome.this);
                                builder4.setMessage("Without Device Location Permission This App Will Shut Down");
                                builder4.setTitle("Allow Device Location Permission");
                                builder4.setPositiveButton("Allow Device Location Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.ACCESS_FINE_LOCATION},
                                                Request_FINE_LOCATION);
                                    }
                                });
                                builder4.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog4 = builder4.create();
                                alertDialog4.setCancelable(false);
                                alertDialog4.setCanceledOnTouchOutside(false);
                                alertDialog4.show();

                            case 2:
                                AlertDialog.Builder builder6 = new AlertDialog.Builder(Welcome.this);
                                builder6.setMessage("Without Read Sms Permission This App Will Shut Down");
                                builder6.setTitle("Allow Read Sms Permission");
                                builder6.setPositiveButton("Allow Read Sms Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.READ_SMS},
                                                Request_READ_SMS);
                                    }
                                });
                                builder6.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog6 = builder6.create();
                                alertDialog6.setCancelable(false);
                                alertDialog6.setCanceledOnTouchOutside(false);
                                alertDialog6.show();
                                break;

                            case 3:
                                AlertDialog.Builder builder7 = new AlertDialog.Builder(Welcome.this);
                                builder7.setMessage("Without SMS View Permission This App Will Shut Down");
                                builder7.setTitle("Allow SMS View Permission");
                                builder7.setPositiveButton("Allow SMS View Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.RECEIVE_SMS},
                                                Request_RECEIVE_SMS);
                                    }
                                });
                                builder7.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog7 = builder7.create();
                                alertDialog7.setCancelable(false);
                                alertDialog7.setCanceledOnTouchOutside(false);
                                alertDialog7.show();
                                break;

                            case 4:
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Welcome.this);
                                builder1.setMessage("Without Storage Write Permission This App Will Shut Down");
                                builder1.setTitle("Allow Storage Write Permission");
                                builder1.setPositiveButton("Allow Storage Write Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                Request_WRITE_EXTERNAL_STORAGE);
                                    }
                                });
                                builder1.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog1 = builder1.create();
                                alertDialog1.setCancelable(false);
                                alertDialog1.setCanceledOnTouchOutside(false);
                                alertDialog1.show();
                                break;

                            case 5:
                                AlertDialog.Builder builder5 = new AlertDialog.Builder(Welcome.this);
                                builder5.setMessage("Without SMS Send Permission This App Will Shut Down");
                                builder5.setTitle("Allow SMS Send Permission");
                                builder5.setPositiveButton("Allow SMS Send Permission", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ActivityCompat.requestPermissions(Welcome.this, new String[]
                                                        {android.Manifest.permission.SEND_SMS},
                                                Request_SEND_SMS);
                                    }
                                });
                                builder5.setNegativeButton("Shut Down App", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        finish();
                                    }
                                });

                                AlertDialog alertDialog5 = builder5.create();
                                alertDialog5.setCancelable(false);
                                alertDialog5.setCanceledOnTouchOutside(false);
                                alertDialog5.show();
                                break;
                        }
                        status = "Denied";
                    } else {
                        status = "Granted";
                    }
                    result = result + "\n" + perm + " : " + status;

                    i++;
                }

                Log.i("Check", result);

                break;
        }
    }




    private String smsReceivedPhoneNumber, receivedOTP;
    private String smsReceivedsenderNum;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            FirebaseCrash.log("Welcome: BroadcastReceiver.onReceive.called");
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                final String phoneNumber = intent.getStringExtra("phoneNumber");
                final String senderNum = intent.getStringExtra("senderNum");
                //Do whatever you want with the code here
                receivedOTP = message;
                smsReceivedPhoneNumber = phoneNumber;
                smsReceivedsenderNum = senderNum;

                if (edtRegistrationEnterOTP != null){
                    edtRegistrationEnterOTP.setText(receivedOTP);
                }
                FirebaseCrash.log("Received OTP: " + receivedOTP + "\n" +
                        "phoneNumber: " + smsReceivedPhoneNumber + "\n" +
                        "senderNum: " + smsReceivedsenderNum + "\n");
                Log.i("Check", "Received OTP: " + receivedOTP + "\n" +
                        "phoneNumber: " + smsReceivedPhoneNumber + "\n" +
                        "senderNum: " + smsReceivedsenderNum + "\n");
                Toast.makeText(getApplicationContext(), "Received OTP: " + receivedOTP + "\n" +
                                "phoneNumber: " + phoneNumber + "\n" +
                                "senderNum: " + senderNum + "\n"
                        , Toast.LENGTH_LONG).show();
            }
        }
    };

    private void getDriverOrBiker() {
        String chosen_vehicle = preferences.getString("Vehicle_Type",DEFAULT);
        if (!chosen_vehicle.equals(DEFAULT) &&
                chosen_vehicle.equals("Car")){
            Common.driverOrBiker = "Driver";
        }
        if (!chosen_vehicle.equals(DEFAULT) &&
                chosen_vehicle.equals("Bike")){
            Common.driverOrBiker = "Biker";
        }

        if (!chosen_vehicle.equals(DEFAULT)){
            //Now we will check in our
            //database whether user is Driver or Biker. First we will check in
            //Driver list
            DatabaseReference findOutUserStatus = FirebaseDatabase
                    .getInstance()
                    .getReference("Users")
                    .child("Drivers");
            findOutUserStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild(Common.driverUID)){
                        Common.driverOrBiker = "Driver";
                        Log.i(TAG,"driverOrBiker: " + Common.driverOrBiker);
                        Intent intent = new Intent(Welcome.this, Home.class);
                        startActivity(intent);
                        finish();
                    } else {
                        //This mean we did not find this user in Driver database
                        //Now we will find in our Biker Database
                        DatabaseReference findOutUserStatus = FirebaseDatabase
                                .getInstance()
                                .getReference("Users")
                                .child("Bikers");
                        findOutUserStatus.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(Common.driverUID)){
                                    Common.driverOrBiker = "Biker";
                                    Log.i(TAG,"driverOrBiker: " + Common.driverOrBiker);
                                    Intent intent = new Intent(Welcome.this, Home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    //This mean we did not find this user in Biker database
                                    Common.driverOrBiker = "N/A";
                                    Log.i(TAG,"driverOrBiker: " + Common.driverOrBiker);
                                    Toast.makeText(Welcome.this,
                                            "Please contact with Uthao customer care", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Welcome.this, "Something went wrong",
                                        Toast.LENGTH_SHORT).show();
                                Log.i(TAG,"DatabaseError: " + databaseError.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Welcome.this, "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                    Log.i(TAG,"DatabaseError: " + databaseError.getMessage());
                }
            });
        }

    }


    private void fetchDriverOrBikerDataFromServer() {
        //Now we will check whether our driver / biker has previous account
        //in our database
        //first we will check in driver segment
        fetchDriverDataFromServer = new SpotsDialog(this,"Fetching Basic Info");
        fetchDriverDataFromServer.show();
        DatabaseReference checkInDriverDB= FirebaseDatabase.getInstance().
                getReference("Users")
                .child("Drivers");
        checkInDriverDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(Common.driverUID)){
                    //Our user has driver account
                    Common.driverOrBiker = "Driver";
                    DatabaseReference retrieveDriverBasicInfo = FirebaseDatabase.getInstance()
                            .getReference("Users/Drivers")
                            .child(Common.driverUID)
                            .child("Driver_Basic_Info");
                    retrieveDriverBasicInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                String Full_Name = null;
                                Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                if (map.get("Full_Name") != null){
                                    Full_Name = map.get("Full_Name").toString();
                                    editor.putString("Full_Name",Full_Name);
                                }
                                if (map.get("Email") != null) {
                                    String Email = map.get("Email").toString();
                                    editor.putString("Email",Email);
                                }
                                if (map.get("Mobile") != null) {
                                    String Mobile = map.get("Mobile").toString();
                                    editor.putString("Mobile",Mobile);
                                }

                                if (map.get("BKash_Number") != null) {
                                    String BKash_Number = map.get("BKash_Number").toString();
                                    editor.putString("BKash_Number",BKash_Number);
                                }

                                if (map.get("NID") != null) {
                                    String NID = map.get("NID").toString();
                                    editor.putString("NID",NID);
                                }

                                if (map.get("License") != null) {
                                    String License = map.get("License").toString();
                                    editor.putString("License",License);
                                }

                                if (map.get("Home_Address") != null) {
                                    String Home_Address = map.get("Home_Address").toString();
                                    editor.putString("Home_Address",Home_Address);
                                }

                                if (map.get("Registration_City") != null) {
                                    String Registration_City = map.get("Registration_City").toString();
                                    editor.putString("Registration_City",Registration_City);
                                }

                                if (map.get("Vehicle_Type") != null) {
                                    String Vehicle_Type = map.get("Vehicle_Type").toString();
                                    editor.putString("Vehicle_Type",Vehicle_Type);
                                }
                                if (map.get("Manufacturer") != null) {
                                    String Manufacturer = map.get("Manufacturer").toString();
                                    editor.putString("Manufacturer",Manufacturer);
                                }
                                if (map.get("Model") != null) {
                                    String Model = map.get("Model").toString();
                                    editor.putString("Model",Model);
                                }
                                if (map.get("Production_year") != null) {
                                    String Production_year = map.get("Production_year").toString();
                                    editor.putString("Production_year",Production_year);
                                }
                                if (map.get("Registration_Authority") != null) {
                                    String Registration_Authority = map.get("Registration_Authority").toString();
                                    editor.putString("Registration_Authority",Registration_Authority);
                                }
                                if (map.get("Registration_Number") != null) {
                                    String Registration_Number = map.get("Registration_Number").toString();
                                    editor.putString("Registration_Number",Registration_Number);
                                }
                                if (map.get("TaxToken") != null) {
                                    String TaxToken = map.get("TaxToken").toString();
                                    editor.putString("TaxToken",TaxToken);
                                }
                                if (map.get("Fitness") != null) {
                                    String Fitness = map.get("Fitness").toString();
                                    editor.putString("Fitness",Fitness);
                                }
                                editor.commit();
                                addBasicInfoTableToRoomDB();


                                try {
                                    if (fetchDriverDataFromServer.isShowing()){
                                        fetchDriverDataFromServer.dismiss();
                                    }
                                } catch (IllegalArgumentException ex){
                                    ex.printStackTrace();
                                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                }
                                getDeviceConfiguration("Car");
                                fetchDriverOrBikerDocumentAndPPFromServer("Driver",Full_Name);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    //Our user don't have previous driver account
                    //now we will check in biker segment
                    DatabaseReference checkInBikerDB= FirebaseDatabase.getInstance().
                            getReference("Users")
                            .child("Bikers");
                    checkInBikerDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(Common.driverUID)){
                                //Our user has biker account
                                Common.driverOrBiker = "Biker";
                                DatabaseReference retrieveDriverBasicInfo = FirebaseDatabase.getInstance()
                                        .getReference("Users/Bikers")
                                        .child(Common.driverUID)
                                        .child("Biker_Basic_Info");
                                retrieveDriverBasicInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                                            FirebaseCrash.log("Welcome: existingUser: previous data fetching: success");
                                            String Full_Name = null;
                                            Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                                            if (map.get("Full_Name") != null){
                                                Full_Name = map.get("Full_Name").toString();
                                                editor.putString("Full_Name",Full_Name);
                                            }
                                            if (map.get("Email") != null) {
                                                String Email = map.get("Email").toString();
                                                editor.putString("Email",Email);
                                            }
                                            if (map.get("Mobile") != null) {
                                                String Mobile = map.get("Mobile").toString();
                                                editor.putString("Mobile",Mobile);
                                            }

                                            if (map.get("BKash_Number") != null) {
                                                String BKash_Number = map.get("BKash_Number").toString();
                                                editor.putString("BKash_Number",BKash_Number);
                                            }

                                            if (map.get("NID") != null) {
                                                String NID = map.get("NID").toString();
                                                editor.putString("NID",NID);
                                            }

                                            if (map.get("License") != null) {
                                                String License = map.get("License").toString();
                                                editor.putString("License",License);
                                            }

                                            if (map.get("Home_Address") != null) {
                                                String Home_Address = map.get("Home_Address").toString();
                                                editor.putString("Home_Address",Home_Address);
                                            }

                                            if (map.get("Registration_City") != null) {
                                                String Registration_City = map.get("Registration_City").toString();
                                                editor.putString("Registration_City",Registration_City);
                                            }

                                            if (map.get("Vehicle_Type") != null) {
                                                String Vehicle_Type = map.get("Vehicle_Type").toString();
                                                editor.putString("Vehicle_Type",Vehicle_Type);
                                            }
                                            if (map.get("Manufacturer") != null) {
                                                String Manufacturer = map.get("Manufacturer").toString();
                                                editor.putString("Manufacturer",Manufacturer);
                                            }
                                            if (map.get("Model") != null) {
                                                String Model = map.get("Model").toString();
                                                editor.putString("Model",Model);
                                            }
                                            if (map.get("Production_year") != null) {
                                                String Production_year = map.get("Production_year").toString();
                                                editor.putString("Production_year",Production_year);
                                            }
                                            if (map.get("Registration_Authority") != null) {
                                                String Registration_Authority = map.get("Registration_Authority").toString();
                                                editor.putString("Registration_Authority",Registration_Authority);
                                            }
                                            if (map.get("Registration_Number") != null) {
                                                String Registration_Number = map.get("Registration_Number").toString();
                                                editor.putString("Registration_Number",Registration_Number);
                                            }
                                            if (map.get("TaxToken") != null) {
                                                String TaxToken = map.get("TaxToken").toString();
                                                editor.putString("TaxToken",TaxToken);
                                            }
                                            if (map.get("Fitness") != null) {
                                                String Fitness = map.get("Fitness").toString();
                                                editor.putString("Fitness",Fitness);
                                            }
                                            editor.commit();
                                            addBasicInfoTableToRoomDB();
                                            try {
                                                if (fetchDriverDataFromServer.isShowing()){
                                                    fetchDriverDataFromServer.dismiss();
                                                }
                                            } catch (IllegalArgumentException ex){
                                                ex.printStackTrace();
                                                Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                                            }
                                            getDeviceConfiguration("Bike");
                                            fetchDriverOrBikerDocumentAndPPFromServer("Biker",Full_Name );
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchDriverOrBikerDocumentAndPPFromServer(String driverOrBiker,
                                                           final String driverOrBikerName ) {
        fetchDriverPPFromServer = new SpotsDialog(this,"Downloading Profile Picture");
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        if (driverOrBiker.equals("Driver")){
            //First we will retrieve profile pic
            DatabaseReference retrieveDriverPP = FirebaseDatabase.getInstance()
                    .getReference("Users/Drivers")
                    .child(Common.driverUID)
                    .child("Driver_Profile_Pic");
            retrieveDriverPP.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                        if (map.get("Profile_Pic_Url") != null){
                            String profilePicUrl = map.get("Profile_Pic_Url").toString();
                            downLoadDriverOrBikerPPFromFireBaseStorage(driverOrBikerName,
                                    "Driver");

                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else if (driverOrBiker.equals("Biker")){
            //First we will retrieve profile pic
            DatabaseReference retrieveDriverPP = FirebaseDatabase.getInstance()
                    .getReference("Users/Drivers")
                    .child(Common.driverUID)
                    .child("Driver_Profile_Pic");
            retrieveDriverPP.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                        if (map.get("Profile_Pic_Url") != null){
                            String profilePicUrl = map.get("Profile_Pic_Url").toString();

                            downLoadDriverOrBikerPPFromFireBaseStorage(driverOrBikerName,
                                    "Biker");
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void downLoadDriverOrBikerPPFromFireBaseStorage(String driverOrBikerName,
                                                            final String driverOrBiker) {
        Common.driverOrBiker = driverOrBiker;
        StorageReference filePath = FirebaseStorage.getInstance().getReference().
                child(Common.driverOrBiker + "_Documents")
                .child(Common.driverUID)
                .child(Common.driverOrBiker + "_Profile_Pic")
                .child(driverOrBikerName);
        final long ONE_MEGABYTE = 1024 * 1024;
        filePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Log.i(TAG, "PP retrieved from firebase storage succeed");
                try {
                    ProfilePic profilePic = new ProfilePic();
                    profilePic.setDriverUid(Common.driverUID);
                    profilePic.setProfile_Pic(bytes);
                    mViewModel.addProfilePic(profilePic);
                    if (fetchDriverPPFromServer.isShowing()){
                        fetchDriverPPFromServer.dismiss();
                    }
                } catch (IllegalArgumentException ex){
                    ex.printStackTrace();
                    Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                }
                downLoadDriverOrBikerDocumentFromFireBaseStorage(Common.driverOrBiker);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.i(TAG, "PP retrieved from firebase storage failed");

            }
        });

    }

    private void downLoadDriverOrBikerDocumentFromFireBaseStorage(String driverOrBiker) {
        Common.driverOrBiker = driverOrBiker;
        String documentName = null;
        String documentNameArrayForDriver[] = {"Driving_License",
                                               "NID_FRONT",
                                               "NID_BACK",
                                               "REGISTRATION_PAPER",
                                               "TAX_TOKEN",
                                               "FITNESS_CERTIFICATE"};
        String documentNameArrayForBiker[] = {"Driving_License",
                                              "NID_FRONT",
                                              "NID_BACK",
                                              "REGISTRATION_PAPER"};
        if (driverOrBiker.equals("Driver")){
            for (int i = 0; i < documentNameArrayForDriver.length; i++){
                fetchDriverDocumentsFromServer = new SpotsDialog(this,
                        "Downloading " + documentName + " From Server");
                fetchDriverDocumentsFromServer.show();
                documentName = documentNameArrayForDriver[i];
                StorageReference filePath = FirebaseStorage.getInstance().getReference().
                        child(Common.driverOrBiker + "_Documents")
                        .child(Common.driverUID)
                        .child(Common.driverOrBiker +"_Scanned_Documents")
                        .child(documentName);
                final long ONE_MEGABYTE = 1024 * 1024;
                final String finalDocumentName = documentName;
                filePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        switch (finalDocumentName){
                            case "Driving_License":
                                DrivingLicense drivingLicense = new DrivingLicense();
                                drivingLicense.setDriverUid(Common.driverUID);
                                drivingLicense.setDriving_License(bytes);
                                mViewModel.addDrivingLicense(drivingLicense);
                                break;
                            case "NID_FRONT":
                                NIDFRONT nidfront = new NIDFRONT();
                                nidfront.setDriverUid(Common.driverUID);
                                nidfront.setNID_FRONT(bytes);
                                mViewModel.addNidFront(nidfront);
                                break;
                            case "NID_BACK":
                                NIDBACK nidback = new NIDBACK();
                                nidback.setDriverUid(Common.driverUID);
                                nidback.setNID_BACK(bytes);
                                mViewModel.addNidBack(nidback);
                                break;
                            case "FITNESS_CERTIFICATE":
                                FITNESS fitness = new FITNESS();
                                fitness.setDriverUid(Common.driverUID);
                                fitness.setFITNESS_CERTIFICATE(bytes);
                                mViewModel.addFitness(fitness);
                                break;
                            case "TAX_TOKEN":
                                TaxToken taxToken = new TaxToken();
                                taxToken.setDriverUid(Common.driverUID);
                                taxToken.setTAX_TOKEN(bytes);
                                mViewModel.addTaxToken(taxToken);
                                break;
                            case "REGISTRATION_PAPER":
                                RegPaper regPaper = new RegPaper();
                                regPaper.setDriverUid(Common.driverUID);
                                regPaper.setREGISTRATION_PAPER(bytes);
                                mViewModel.addRegPaper(regPaper);
                                break;
                        }
                        try {
                            if (fetchDriverDocumentsFromServer.isShowing()){
                                fetchDriverDocumentsFromServer.dismiss();
                            }
                        } catch (IllegalArgumentException ex){
                            ex.printStackTrace();
                            Log.i(TAG,"IllegalArgumentException: " + ex.getLocalizedMessage());
                        }
                        Log.i(TAG, "Document: " + finalDocumentName + " succeeded");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i(TAG, "Document: " + finalDocumentName + " failed");

                    }
                });
            }

            Intent intent = new Intent(Welcome.this, Home.class);
            startActivity(intent);
            finish();


        } else if (driverOrBiker.equals("Biker")){
            for (int i = 0; i <documentNameArrayForBiker.length; i++){
                documentName = documentNameArrayForBiker[i];
                StorageReference filePath = FirebaseStorage.getInstance().getReference().
                        child(Common.driverOrBiker + "_Documents")
                        .child(Common.driverUID)
                        .child(Common.driverOrBiker +"_Scanned_Documents")
                        .child(documentName);
                final long ONE_MEGABYTE = 1024 * 1024;
                final String finalDocumentName = documentName;
                filePath.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        switch (finalDocumentName){
                            case "Driving_License":
                                DrivingLicense drivingLicense = new DrivingLicense();
                                drivingLicense.setDriverUid(Common.driverUID);
                                drivingLicense.setDriving_License(bytes);
                                mViewModel.addDrivingLicense(drivingLicense);
                                break;
                            case "NID_FRONT":
                                NIDFRONT nidfront = new NIDFRONT();
                                nidfront.setDriverUid(Common.driverUID);
                                nidfront.setNID_FRONT(bytes);
                                mViewModel.addNidFront(nidfront);
                                break;
                            case "NID_BACK":
                                NIDBACK nidback = new NIDBACK();
                                nidback.setDriverUid(Common.driverUID);
                                nidback.setNID_BACK(bytes);
                                mViewModel.addNidBack(nidback);
                                break;
                            case "REGISTRATION_PAPER":
                                RegPaper regPaper = new RegPaper();
                                regPaper.setDriverUid(Common.driverUID);
                                regPaper.setREGISTRATION_PAPER(bytes);
                                mViewModel.addRegPaper(regPaper);
                                break;
                        }
                        Log.i(TAG, "Document: " + finalDocumentName + " succeeded");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.i(TAG, "Document: " + finalDocumentName + " failed");

                    }
                });
            }

            Intent intent = new Intent(Welcome.this, Home.class);
            startActivity(intent);
            finish();

        }



    }

    private void addBasicInfoTableToRoomDB(){
        String driverName = preferences.getString("Full_Name",DEFAULT);
        String driverEmail = preferences.getString("Email",DEFAULT);
        String Driver_Mobile = preferences.getString("Mobile",DEFAULT);
        String NID = preferences.getString("NID",DEFAULT);
        String Vehicle_Type = preferences.getString("Vehicle_Type",DEFAULT);
        String Driver_Registration_City = preferences.getString("Registration_City",DEFAULT);
        String Driver_Home_Address = preferences.getString("Home_Address",DEFAULT);
        String Driver_License = preferences.getString("License",DEFAULT);
        String Driver_Car_Registration = preferences.getString("Registration_Number",DEFAULT);
        String Driver_Car_TaxToken = preferences.getString("TaxToken",DEFAULT);
        String Driver_Car_Fitness = preferences.getString("Fitness",DEFAULT);
        String Car_Registration_Authority = preferences.getString("Registration_Authority",DEFAULT);
        String Car_Production_year = preferences.getString("Production_year",DEFAULT);
        String Car_Model = preferences.getString("Model",DEFAULT);
        String Car_Manufacturer = preferences.getString("Manufacturer",DEFAULT);
        String DriverOrBiker = null;
        if (Vehicle_Type.equals("Car")){
            DriverOrBiker = "Driver";
        } else if (Vehicle_Type.equals("Bike")){
            DriverOrBiker = "Biker";
        }

        BasicInfo basicInfo = new BasicInfo();
        basicInfo.setDriverUid(Common.driverUID);
        basicInfo.setFull_name(driverName);
        basicInfo.setEmail(driverEmail);
        basicInfo.setMobile(Driver_Mobile);
        basicInfo.setNid(NID);
        basicInfo.setDriving_license(Driver_License);
        basicInfo.setHome_address(Driver_Home_Address);
        basicInfo.setRegistration_city(Driver_Registration_City);
        basicInfo.setVehicle_type(Vehicle_Type);
        basicInfo.setVehicle_manufacturer(Car_Manufacturer);
        basicInfo.setVehicle_model(Car_Model);
        basicInfo.setVehicle_production_year(Car_Production_year);
        basicInfo.setVehicle_registration_authority(Car_Registration_Authority);
        basicInfo.setVehicle_registration_number(Driver_Car_Registration);
        basicInfo.setVehicle_tax_token_number(Driver_Car_TaxToken);
        basicInfo.setVehicle_fitness_number(Driver_Car_Fitness);
        basicInfo.setDriverOrBiker(DriverOrBiker);
        mViewModel.updateBasicInfo(basicInfo);
    }


}
