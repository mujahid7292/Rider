package com.sand_corporation.www.uthaopartner.HomeActivity;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.IFCMService;
import com.sand_corporation.www.uthaopartner.FareCalculator.FareCalculator;
import com.sand_corporation.www.uthaopartner.FirebaseMessaging.ModelClass.Token;
import com.sand_corporation.www.uthaopartner.ForeGroundService.ForeGroundService;
import com.sand_corporation.www.uthaopartner.GetGoogleDirection.IGoogleAPI;
import com.sand_corporation.www.uthaopartner.GetGoogleRoads.IGoogleRoadAPI;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.LanguageChange.LocalHelper;
import com.sand_corporation.www.uthaopartner.NavigationMenuRecyclerView.NavigationMenuAdapter;
import com.sand_corporation.www.uthaopartner.NavigationMenuRecyclerView.NavigationMenuItem;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.SubsetOfBasicInfo.NavigationMenuSubSet;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerInfo.AssignedCustomerInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.AssignedCustomerTripDetails.AssignedCustomerTripDetails;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.OnTripPersistance.OnTripPersistence;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable.CustomerPickUpLatLng;
import com.sand_corporation.www.uthaopartner.SQLiteLatLngDB.DbContract;
import com.sand_corporation.www.uthaopartner.SQLiteLatLngDB.DbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.v4.app.NotificationCompat.STREAM_DEFAULT;

public class Home extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener,RoutingListener{

    private static final int CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE = 1000;
    private boolean gpsAlertShownToUser = false;
    public static String SERVICE_ACTION = "com.nkdroid.alertdialog.action.main";

    private double drivingDistanceBetweenDriverCurrentLocationAndPickUpLocation = 0.0;
    private int driverETAtoReachPickUpLocation = 0;

    //Number of listeners which we have to remove in onStop() method
    private DatabaseReference checkForNewCustomerInDB;
    private ValueEventListener checkForNewCustomerInDBListener;
    private ValueEventListener assignedCustomerPickUpDestinationListener;
    private DatabaseReference assignedCustomerPickUpDestinationDB;


    //Customer Call Layout
    private LinearLayout customerCallLayout;
    private TextView customerPickUpTime, customerDistanceFromDriver;
    private Button btnDeclineCustomerRequest, btnAcceptCustomerRequest;
    private MediaPlayer mediaPlayer;
    private LatLng pickUpLocationLatlng;
    private LatLng driverLocationLatLng;
    private List<LatLng> polyLineListForDriverDistance;
    private List<LatLng> polyLineListForRoadsApiDistance;
    private List<LatLng> polyLineListForDeviceGPSDistance;
    private PolylineOptions grayPolylineOptions, blackPolylineOptions;
    private Polyline blackPolyline, grayPolyline;
    private IGoogleAPI mService;
    private IGoogleRoadAPI mRoadService;
    private IFCMService ifcmService;
    private double driverDistanceToPicUpLocation;
    private double driverETAtoPickUpLocation;
    private String customerPickUpAddress;

    //customerChatAndInfoBottomSheetLayout Layout
    private BottomSheetBehavior customerChatAndInfoBottomSheetBehavior;
    private LinearLayout customerChatAndInfoBottomSheetLayout;
    private EditText edtCustomerChatWindow;
    private Button btnCancelAcceptedRide, btnCallAssignedCustomer;
    private ImageView ic_up_sign_customer_bottom, sendVoiceMessageToCustomer,
            sendTextMessageToCustomer;
    private CircleImageView assignedCustomerProfilePic;
    private TextView assignedCustomerName, assignedCustomerETA, assignedCustomerRating,
            assignedCustomerTotalLifeTimeTrips, paymentMethodForAssignedCustomer,
            assignedCustomerDistance, assignedCustomerPickUpAddress;
    private String strAssignedCustomerPhoneNumber;

    private Handler doPeriodicWorkInSideApp;

    //Customer Trip Related
    private ArrayList<com.google.android.gms.maps.model.LatLng> pickUpToDestinationLatLng;
    private ArrayList<com.google.android.gms.maps.model.LatLng> sampleTakenFrom_pickUpToDestinationLatLng;
    public static Polyline polyLineFromDeviceLocation;
    public static PolylineOptions polyLineOptionFromDeviceLocation;

    private boolean isDriverConnectedToDatabase = false;

    private static final String TAG = "CheckUHome";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String DEFAULT = "N/A";
    private String roadPathForRequestingAPI;

    //Navigation menu
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private CircleImageView nav_profile_image;
    private TextView nav_profile_name;
    private NavigationMenuAdapter navigationMenuAdapter;
    private RecyclerView navigationMenuRecyclerView;
    private ImageButton navigationDrawerCustomButton;

    //HomeViewModel
    private HomeViewModel mViewModel;
    private String driverName;

    private static final int PERMISSION_REQUEST = 102;
    private Button googleMapsRouting, callRoadApi;
    private TextView txtDistanceGoneByDeviceGPS, txtDistanceGoneByRoadsApi;
    private String CustomerDestinationAddress, CustomerPickUpAddress;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationListener locationListener;
    public LocationSettingsRequest.Builder locationSettingsRequestBuilder;
    private Location mLastLocation;
    private String customerFCMToken;
    private boolean isDriverLoggingOut = false;
    //By this boolean variable we will keep track of driver's logging out of the app.
    private Long rideAcceptTime = 0L;
    private Long rideStartTime = 0L;
    private List<Polyline> polylines;
    private static final int[] COLORS = new int[]{R.color.colorPrimaryDark,
            R.color.colorPrimary, R.color.colorPrimaryLight, R.color.colorAccent,
            R.color.colorBallRelease};

    //Polyline
    private Polyline polyLineFromRoadsApi;
    private PolylineOptions polyLineOptionFromRoadsApi;

    @Override
    protected void onStart() {
        Log.i("ActivityLifeCycle","onStart");
        super.onStart();
        createDefaultLanguageForThisApp();
        if (Common.googleApiClientService != null){
            if (Common.googleApiClientService.isConnected()){
                checkIsLocationIsEnabled(
                        Common.locationSettingsRequestBuilderService,
                        Common.googleApiClientService,
                        Common.locationRequestService);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Common.tripStatus = preferences.getString("Trip_Status",DEFAULT);
        if (Common.tripStatus.equals("OnRoute") || Common.tripStatus.equals("Started")){
            Toast.makeText(this, "Back press will not work",
                    Toast.LENGTH_LONG).show();
        }else {
            super.onBackPressed();
        }
    }

    private void createDefaultLanguageForThisApp() {
        FirebaseCrash.log("Home: createDefaultLanguageForThisApp.called");
        Crashlytics.log("Home: createDefaultLanguageForThisApp.called");
        //init paper first
        //We use paper library to write in internal storage
        Paper.init(this);

        //Default language is English
        String language = Paper.book().read("language");
        if (language == null){
            Paper.book().write("language","en");
        }
        updateDefaultLanguage((String)Paper.book().read("language"));
    }


    public void updateDefaultLanguage(String language) {
        FirebaseCrash.log("Home: updateDefaultLanguage.called");
        Crashlytics.log("Home: updateDefaultLanguage.called");
        Context context = LocalHelper.setLocale(Home.this,language);
        Resources resources = context.getResources();
        //Below we will change all the language
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        //Don't call below two log method here. It will create this error
        //Caused by java.lang.IllegalStateException: Must Initialize Fabric before using singleton()
//        FirebaseCrash.log("Home: attachBaseContext.called");
//        Crashlytics.log("Home: attachBaseContext.called");
        //This will help us to change default language
        super.attachBaseContext(LocalHelper.onAttach(newBase,"en"));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isDeviceScreenIsLocked();
        Log.i("CheckUOnMessageReceived","Home: isScreenOn: " + Common.isScreenOn);
        if (Common.isScreenOn){
            startActivityInSleepingDevice();
        }
        setContentView(R.layout.activity_home);
        if (Common.driverUID == null){
            Log.i(TAG,"driverUID: null");
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        putObserverOnHomeViewModel();
        //This below code will keep screen on when user is in the home activity
        //Because driver may see driving direction from this activity
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //This poly line is the route between driver & customer
        polylines = new ArrayList<>();
        polyLineListForDriverDistance = new ArrayList<>();
        polyLineListForRoadsApiDistance = new ArrayList<>();
        polyLineListForDeviceGPSDistance = new ArrayList<>();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Common.mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Common.mapFragment.getMapAsync(this);
        mService = Common.getGoogleApi(); //Initialize mService
        mRoadService = Common.getGoogleRoadApi(); // Initialize mRoadService
        ifcmService = Common.getFCMService(); //Initialize device to device FCM service
        checkGPSConnectionPermission();
        checkForCustomerCall();
        if (!isMyServiceRunning(ForeGroundService.class)){
            createForeGroundServiceUsingOnGoingNotification();
        }

        preferences = getSharedPreferences("Driver_Basic_Info",
                Context.MODE_PRIVATE);
        //Context.MODE_PRIVATE This means MyData will be accessible by only this app
        editor = preferences.edit();



        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View navigationHeaderPart = navigationView.getHeaderView(0);
        nav_profile_image = navigationHeaderPart.findViewById(R.id.nav_profile_image);
        nav_profile_name = navigationHeaderPart.findViewById(R.id.nav_profile_name);
        navigationDrawerCustomButton = findViewById(R.id.navigationDrawerCustomButton);

        navigationDrawerCustomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseCrash.log("Home:navigationDrawerCustomButton.clicked");
                Crashlytics.log("Home:navigationDrawerCustomButton.clicked");
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });




        googleMapsRouting = findViewById(R.id.googleMapsRouting);
        googleMapsRouting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGoogleMapForDirection1();
            }
        });
        callRoadApi = findViewById(R.id.callRoadApi);
        callRoadApi.setText("Pick up customer");
        txtDistanceGoneByDeviceGPS = findViewById(R.id.txtDistanceGoneByDeviceGPS);
        txtDistanceGoneByRoadsApi = findViewById(R.id.txtDistanceGoneByRoadsApi);
        sendFCMTokenToServer();
        populateNavigationMenuToRecyclerView();
        setNavigationProfilePic();

        callRoadApi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String btnText = callRoadApi.getText().toString();
                switch (btnText){
                    case "Pick up customer":
                        if (Common.reachedToAssignedCustomerPickUpLocation){
                            resetDriverMeter();
                            Common.tripStatus = "Started";
                            editor.putString("Trip_Status","Started");
                            editor.apply();
                            callRoadApi.setText("Drop customer");
                            rideStartTime = getCurrentTimeStamp();
                            Common.rideStatus = 2;
                            //rideStatus == 2,That means our driver is on his way to drop our customer
                            //on his destination.
                            //We will let our customer know His Ride_Status.
                            DatabaseReference mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                                    .getReference("Users/Customers")
                                    .child(Common.customerUID)
                                    .child("Current_Ride_Info_Panel");
                            HashMap map = new HashMap();
                            map.put("Ride_Status",Common.rideStatus);
                            mCustomerCurrentRideInfoPnaelRef.updateChildren(map);
                            sendNotificationToCustomer("Our Driver Has Reached");
                        } else {
                            Toast.makeText(Home.this, "Didn't reach pickup location yet", Toast.LENGTH_LONG).show();
                        }

                        break;

                    case "Drop customer":
                        Common.mMap.clear();
                        retrieveLatLngFromSQLiteDB();
                        createPathDataForRoadApi();
                        Common.tripStatus = "Ended";
                        editor.putString("Trip_Status","Ended");
                        editor.apply();
                        callRoadApi.setText("Pick up customer");
                        Common.rideStatus = 3;
                        //rideStatus == 3,That means our driver has dropped our customer
                        //on his destination. Now we will record this ride history in our database
                        DatabaseReference mCustomerCurrentRideInfoPnaelReffinish = FirebaseDatabase.getInstance()
                                .getReference("Users/Customers")
                                .child(Common.customerUID)
                                .child("Current_Ride_Info_Panel");
                        HashMap mapfinish = new HashMap();
                        mapfinish.put("Ride_Status",Common.rideStatus);
                        mCustomerCurrentRideInfoPnaelReffinish.updateChildren(mapfinish);
                        break;
                }

            }
        });

    }

    private void putObserverOnHomeViewModel() {
        mViewModel.mBasicInfo.observe(this, new Observer<BasicInfo>() {
            @Override
            public void onChanged(@Nullable BasicInfo basicInfo) {
                if (basicInfo != null){
                    driverName = basicInfo.getFull_name();
                    Common.driverOrBiker = basicInfo.getDriverOrBiker();
                    getThisDriverOrBikersServiceType();
                }
            }
        });


        mViewModel.mOnTripPersistence.observe(this,
                new Observer<List<OnTripPersistence>>() {
            @Override
            public void onChanged(@Nullable List<OnTripPersistence> onTripPersistences) {
                if (onTripPersistences != null){
                    Log.i(TAG,"onTripPersistence size: "
                            + onTripPersistences.size());
                    for (OnTripPersistence tripPersistence: onTripPersistences){
                        AssignedCustomerInfo customerInfo = tripPersistence.getCustomerInfo();
                        AssignedCustomerTripDetails tripDetails = tripPersistence.getTripDetails();
                        int rowNumberOfCustomerInfo = customerInfo.getInfoRowNumber();
                        if (rowNumberOfCustomerInfo == 0){
                            //After confirming that our customer has info on database than we make
                            //visible 'assignedCustomerInfoPanel'
                            //So before fetching data we have to show loading sign.
                            setUpAllTheViewsFromCustomerChatAndInfoBottomSheet();

                            Common.customerUID = customerInfo.getCustomerUID();
                            customerFCMToken = customerInfo.getCustomerFCMToken();
                            String customerName = customerInfo.getCustomerName();
                            Common.assignedCustomerName = customerName;
                            assignedCustomerName.setText(customerName);

                            String customerMobile = customerInfo.getCustomerMobile();
                            strAssignedCustomerPhoneNumber = customerMobile;

                            String customerRatings = customerInfo.getCustomerRatings();
                            assignedCustomerRating.setText(customerRatings);

                            String customerTotalTrips = customerInfo.getCustomerTotalTrips();
                            assignedCustomerTotalLifeTimeTrips.setText(customerTotalTrips);

                            String customerFaceBookPP = customerInfo.getCustomerFaceBookPP();
                            String firebaseStorageCustomerPP = customerInfo.getCustomerProfilePicUrl();

                            if (customerFaceBookPP != null){
                                Common.assignedCustomerProfilePic = customerFaceBookPP;
                                Glide.with(getBaseContext()).load(customerFaceBookPP).into(assignedCustomerProfilePic);
                            } else if (firebaseStorageCustomerPP != null){
                                Common.assignedCustomerProfilePic = firebaseStorageCustomerPP;
                                Glide.with(getBaseContext()).load(firebaseStorageCustomerPP)
                                        .into(assignedCustomerProfilePic);
                            }
                            Log.i("AssignedCustomerInfo","Name: " + customerName+
                            " | customerUID: " + Common.customerUID + " | customerFCMToken: " + customerFCMToken +
                            "\ncustomerMobile: " + customerMobile + " | customerRatings: " + customerRatings +
                            " | customerTotalTrips: " + customerTotalTrips+ " | customerFaceBookPP: " + customerFaceBookPP
                            +"\nfirebaseStorageCustomerPP: " + firebaseStorageCustomerPP);

                        }





                        int rowNumberOfTripDetails = tripDetails.getTripDetailsRowNumber();
                        if (rowNumberOfTripDetails == 0){
                            CustomerDestinationAddress = tripDetails.getCustomerDestinationAddress();
                            CustomerPickUpAddress = tripDetails.getCustomerPickUpAddress();
                            assignedCustomerPickUpAddress.setText(CustomerPickUpAddress);
                            Double destinationLat = tripDetails.getCustomerDestinationLat();
                            Double destinationLng = tripDetails.getCustomerDestinationLng();
                            Common.customerDestinationLatLng = new LatLng(destinationLat,destinationLng);
                            Double pickUpLat = tripDetails.getCustomerPickUpLat();
                            Double pickUpLng = tripDetails.getCustomerPickUpLng();
                            String promoCode = tripDetails.getPromoCode();
                            double surchargeAmountInPercentage = tripDetails.getSurchargeAmount();
                            Common.customerPickUpLocationLatLng = new LatLng(pickUpLat,pickUpLng);
                            if (Common.mMap != null && pickUpLocationMarker != null){
                                pickUpLocationMarker.remove();
                                pickUpLocationMarker = Common.mMap.addMarker(new MarkerOptions()
                                        .position(Common.customerPickUpLocationLatLng)
                                        .title("Pick Up Location")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker)));

                            }
                            Common.gotAssignedCustomerPickupLocation = true;

                        }
                    }
                    //Here we will check whether tripStatus = Started, Ended or OnRoute
                    Common.tripStatus = preferences.getString("Trip_Status",DEFAULT);
                    if (Common.tripStatus.equals("OnRoute")){
                        //That means our driver is on his way to pick up customer
                        observer = new Observer<List<CustomerPickUpLatLng> >() {
                            @Override
                            public void onChanged(@Nullable List<CustomerPickUpLatLng> customerPickUpLatLng) {
                                if (customerPickUpLatLng != null){
                                    Log.i("PickUpLatLng","customerPickUpLatLng.size(): " + customerPickUpLatLng.size());
                                    if (customerPickUpDistanceLatLng == null){
                                        customerPickUpDistanceLatLng = new ArrayList<>();
                                    } else {
                                        customerPickUpDistanceLatLng.clear();
                                    }

                                    for (CustomerPickUpLatLng singleObject:customerPickUpLatLng){
                                        LatLng latLng = new LatLng(singleObject.getLatitude(),singleObject.getLongitude());
                                        customerPickUpDistanceLatLng.add(latLng);
                                    }
                                    drawPolyLineFromDriverReqAcceptLocToCustomerPicUpLoc();
                                    if (Common.mMap != null && Common.mLastLocation != null){
                                        LatLng latLng = new LatLng(Common.mLastLocation.getLatitude(),
                                                Common.mLastLocation.getLongitude());
                                        Common.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                        Common.mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                                    }
                                }
                                removeObserVer();
                            }
                        };
                        addObserver();

                    } else if (Common.tripStatus.equals("Started")){
                        //That means our driver is on his way to drop up customer
                    }
                }
            }
        });

    }

    private void sendFCMTokenToServer() {
        if (Common.driverOrBiker != null && Common.driverUID != null){
            //FCM_LATEST_TOKEN
            Common.refreshedFCMToken = FirebaseInstanceId.getInstance().getToken();
            Token token = new Token(Common.refreshedFCMToken);
            if (FirebaseAuth.getInstance().getCurrentUser() != null){
                DatabaseReference mDriverRefreshedToken = FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(Common.driverOrBiker + "s")
                        .child(Common.driverUID)
                        .child("FCM_LATEST_TOKEN");
                mDriverRefreshedToken.setValue(token);
            }
        }


    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG,"Foreground Service: Running");
                return true;
            }
        }
        Log.i(TAG,"Foreground Service: Not Running");
        return false;
    }

    //Customer Call Layout
    //Start
    private void checkForCustomerCall() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            String bundleType = bundle.getString("Type");
            if (bundleType != null && bundleType.equals("CustomerCall")){
                Common.driverStatus = "Offline";
                double customerLatitude = bundle.getDouble("lat",-1.0);
                double customerLongitude = bundle.getDouble("lng",-1.0);
                pickUpLocationLatlng = new LatLng(customerLatitude,customerLongitude);
                //Below find out customer distance from driver location
                if (Common.mLastLocation != null){
                    driverLocationLatLng = new LatLng(Common.mLastLocation.getLatitude(),
                            Common.mLastLocation.getLongitude());
                }
                getRouteOfDriverToPickUpLocation(driverLocationLatLng,pickUpLocationLatlng);
                Log.i(TAG,"Home:lat: " + customerLatitude + "\n" +
                "Home:lng: " + customerLongitude);
            }
        }

    }

    private void startActivityInSleepingDevice(){
        Home.this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Home.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        KeyguardManager manager = (KeyguardManager) Home.this.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = manager.newKeyguardLock("abc");
        lock.disableKeyguard();
        Common.isScreenOn = false;
    }

    private void isDeviceScreenIsLocked() {
        PowerManager pm = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE);
        Common.isScreenOn = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            if (pm != null) {
                Common.isScreenOn = pm.isInteractive();
                Log.i(TAG, "isScreenOn: "+ Common.isScreenOn);
            }
        } else {
            assert pm != null;
            Common.isScreenOn = pm.isScreenOn();
            Log.i(TAG, "isScreenOn: "+ Common.isScreenOn);
        }

        if(!Common.isScreenOn)
        {
            PowerManager.WakeLock wl = pm.newWakeLock(
                    PowerManager.FULL_WAKE_LOCK
                            |PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE,
                    "MyLock");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyCpuLock");
            wl_cpu.acquire(10000);

            Common.isScreenOn = true;
        }
    }

    private boolean isCustomerLookingToTheScreen(){
        PowerManager pm = (PowerManager)getBaseContext().getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT_WATCH) {
            if (pm != null) {
                isScreenOn = pm.isInteractive();
                Log.i(TAG, "isScreenOn: "+ isScreenOn);
            }
        } else {
            if (pm != null) {
                isScreenOn = pm.isScreenOn();
                Log.i(TAG, "isScreenOn: "+ isScreenOn);
            }

        }
        return isScreenOn;
    }

    private void createForeGroundServiceUsingOnGoingNotification(){
        Intent startService = new Intent(Home.this, ForeGroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(startService);
        } else {
            Intent startIntent = new Intent(Home.this, ForeGroundService.class);
            startService(startIntent);
        }
    }

    private void stopForeGroundService(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeGroundService();
        } else {
            Intent stopIntent = new Intent(Home.this, ForeGroundService.class);
            stopService(stopIntent);
        }
    }

    private void setUpAllTheViewsFromCustomerCallLayout() {
        customerCallLayout = findViewById(R.id.customerCallLayout);
        customerCallLayout.setVisibility(View.VISIBLE);
        customerPickUpTime = findViewById(R.id.customerPickUpTime);
        customerDistanceFromDriver = findViewById(R.id.customerDistanceFromDriver);
        btnDeclineCustomerRequest = findViewById(R.id.btnDeclineCustomerRequest);
        btnAcceptCustomerRequest = findViewById(R.id.btnAcceptCustomerRequest);
        mediaPlayer = MediaPlayer.create(this,R.raw.ringtone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        //Now we will create timer for auto accepting customer call
        //If our driver don't decline customer call within 15 second
        //than call will be auto accepted
        Handler autoAcceptCustomerCallHandler = new Handler();
        Runnable autoAcceptCustomerCallRunnable = new Runnable() {
            @Override
            public void run() {
                if (!Common.driverStatus.equals("OnTrip") &&
                        !Common.driverStatus.equals("Online")){
                    //!Common.driverStatus.equals("OnTrip")
                    //This means our driver did not accepted customer request

                    //!Common.driverStatus.equals("Online")
                    //This means our driver did not declined customer request
                    acceptCustomerRequest();
                }

            }
        };
        autoAcceptCustomerCallHandler.postDelayed(autoAcceptCustomerCallRunnable, 15 * 1000);

        btnAcceptCustomerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acceptCustomerRequest();
            }
        });

        btnDeclineCustomerRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineCustomerRequest();
            }
        });

    }

    private void destroyAllTheViewsFromCustomerCallLayout() {
        customerCallLayout = null;
        customerPickUpTime = null;
        customerDistanceFromDriver = null;
        btnDeclineCustomerRequest = null;
        btnAcceptCustomerRequest = null;
        mediaPlayer = null;
    }

    private void setUpAllTheViewsFromCustomerChatAndInfoBottomSheet(){
        if (customerChatAndInfoBottomSheetLayout != null){
            Log.i("BottomSheet","customerChatAndInfoBottomSheetLayout != null");
            if (customerChatAndInfoBottomSheetLayout.getVisibility() == View.VISIBLE){
                Log.i("BottomSheet","customerChatAndInfoBottomSheetLayout == VISIBLE");
            }
            if (customerChatAndInfoBottomSheetLayout.getVisibility() == View.GONE){
                Log.i("BottomSheet","customerChatAndInfoBottomSheetLayout == GONE");
            }
        } else if (customerChatAndInfoBottomSheetLayout == null){
            Log.i("BottomSheet","customerChatAndInfoBottomSheetLayout == null");
            //Linear Layout
            customerChatAndInfoBottomSheetLayout = findViewById
                    (R.id.customerChatAndInfoBottomSheetLayout);
            customerChatAndInfoBottomSheetLayout.setVisibility(View.VISIBLE);

            //Circle Image View
            assignedCustomerProfilePic = findViewById(R.id.assignedCustomerProfilePic);

            //EditText
            edtCustomerChatWindow = findViewById(R.id.edtCustomerChatWindow);

            //TextView
            assignedCustomerName = findViewById(R.id.assignedCustomerName);
            assignedCustomerETA = findViewById(R.id.assignedCustomerETA);
            assignedCustomerRating = findViewById(R.id.assignedCustomerRating);
            assignedCustomerTotalLifeTimeTrips = findViewById(R.id.assignedCustomerTotalLifeTimeTrips);
            paymentMethodForAssignedCustomer = findViewById(R.id.paymentMethodForAssignedCustomer);
            assignedCustomerDistance = findViewById(R.id.assignedCustomerDistance);
            assignedCustomerPickUpAddress = findViewById(R.id.assignedCustomerPickUpAddress);

            //ImageView
            ic_up_sign_customer_bottom = findViewById(R.id.ic_up_sign_customer_bottom);
            sendVoiceMessageToCustomer = findViewById(R.id.sendVoiceMessageToCustomer);
            sendTextMessageToCustomer = findViewById(R.id.sendTextMessageToCustomer);

            //Button
            btnCancelAcceptedRide = findViewById(R.id.btnCancelAcceptedRide);
            btnCallAssignedCustomer = findViewById(R.id.btnCallAssignedCustomer);

        }

        ic_up_sign_customer_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int state = customerChatAndInfoBottomSheetBehavior.getState();
                switch (state){
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        ic_up_sign_customer_bottom.setImageDrawable(getResources()
                                .getDrawable(R.drawable.ic_botttom_sheet_down));
                        customerChatAndInfoBottomSheetBehavior
                                .setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;

                    case BottomSheetBehavior.STATE_EXPANDED:
                        ic_up_sign_customer_bottom.setImageDrawable(getResources()
                                .getDrawable(R.drawable.ic_botttom_sheet_up));
                        customerChatAndInfoBottomSheetBehavior
                                .setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                }
            }
        });

        sendVoiceMessageToCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        sendTextMessageToCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnCancelAcceptedRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                declineCustomerRequest();
            }
        });

        btnCallAssignedCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + strAssignedCustomerPhoneNumber));
                startActivity(callIntent);
            }
        });

        if (customerChatAndInfoBottomSheetLayout != null){
            customerChatAndInfoBottomSheetBehavior = BottomSheetBehavior
                    .from(customerChatAndInfoBottomSheetLayout);
            customerChatAndInfoBottomSheetBehavior.setBottomSheetCallback
                    (new BottomSheetBehavior.BottomSheetCallback() {
                        @Override
                        public void onStateChanged(@NonNull View bottomSheet, int newState) {
                            switch (newState){
                                case BottomSheetBehavior.STATE_COLLAPSED:{

                                    Log.d("CheckBottomSheet","collapsed") ;
                                }
                                case BottomSheetBehavior.STATE_SETTLING:{

                                    Log.d("CheckBottomSheet","settling") ;
                                }
                                case BottomSheetBehavior.STATE_EXPANDED:{

                                    Log.d("CheckBottomSheet","expanded") ;
                                }
                                case BottomSheetBehavior.STATE_HIDDEN: {
                                    Log.d("CheckBottomSheet" , "hidden") ;
                                }
                                case BottomSheetBehavior.STATE_DRAGGING: {
                                    Log.d("CheckBottomSheet","dragging") ;
                                }
                            }
                        }

                        @Override
                        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                        }
                    });
        } else {
            Log.i(TAG,"driverChatAndInfoBottomSheet = null");
        }

    }

    private void destroyAllTheViewsFromCustomerChatAndInfoBottomSheetLayout(){

        //Linear Layout
        customerChatAndInfoBottomSheetLayout = null;

        //Circle Image View
        assignedCustomerProfilePic = null;

        //EditText
        edtCustomerChatWindow = null;

        //TextView
        assignedCustomerName = null;
        assignedCustomerETA = null;
        assignedCustomerRating = null;
        assignedCustomerTotalLifeTimeTrips = null;
        paymentMethodForAssignedCustomer = null;
        assignedCustomerDistance = null;

        //ImageView
        ic_up_sign_customer_bottom = null;
        sendVoiceMessageToCustomer = null;
        sendTextMessageToCustomer = null;

        //Button
        btnCancelAcceptedRide = null;
        btnCallAssignedCustomer = null;
    }

    private void acceptCustomerRequest() {
        if (customerCallLayout != null){
            if (customerCallLayout.getVisibility() == View.VISIBLE){
                //This means driver declined customer request without accepting it first.
                customerCallLayout.setVisibility(View.GONE);
                mediaPlayer.stop();
                mediaPlayer.release();
                Common.driverStatus = "OnTrip";
                Glide.with(getApplicationContext())
                        .asBitmap()
                        .load(R.drawable.ic_driver_on_trip)
                        .into(Common.notificationTarget);
                destroyAllTheViewsFromCustomerCallLayout();
            }
        }
        Common.tripStatus = "OnRoute";
        editor.putString("Trip_Status","OnRoute");
        editor.apply();
        rideAcceptTime = getCurrentTimeStamp();
        getAssignedCustomersFCMToken();
        getAssignedCustomerInfo();
        getAssignedCustomerTripDetails();

        //Change customer ride status
        //rideStatus = 1; that means our driver is on his way to pick up customer
        DatabaseReference setRideStatus = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(Common.customerUID)
                .child("Current_Ride_Info_Panel");
        HashMap<String, Object> acceptRequest = new HashMap<>();
        acceptRequest.put("Ride_Status",1);
        setRideStatus.setValue(acceptRequest);
        keepListeningToDatabaseForNewCustomer();
    }

    private void declineCustomerRequest() {
        Common.driverStatus = "Online";
        if (customerCallLayout != null){
            if (customerCallLayout.getVisibility() == View.VISIBLE){
                //This means driver declined customer request without accepting it first.
                customerCallLayout.setVisibility(View.GONE);
                mediaPlayer.stop();
                mediaPlayer.release();
                destroyAllTheViewsFromCustomerCallLayout();

                //Change customer ride status
                //rideStatus = 6; that means temporary driver declined
                DatabaseReference setRideStatus = FirebaseDatabase.getInstance()
                        .getReference("Users/Customers")
                        .child(Common.customerUID)
                        .child("Current_Ride_Info_Panel");
                HashMap<String, Object> declineRequest = new HashMap<>();
                declineRequest.put("Ride_Status",6);
                setRideStatus.setValue(declineRequest);
            }
        }

        if (customerChatAndInfoBottomSheetLayout != null){
            if (customerChatAndInfoBottomSheetLayout.getVisibility() == View.VISIBLE){
                //This means driver declined customer request after accepting it.
                //Check for possible fraud
                mViewModel.clearCustomerPickUpLatLng();
                mViewModel.clearAssignedCustomerInfo();
                mViewModel.clearAssignedCustomerTripDetails();
                customerChatAndInfoBottomSheetLayout.setVisibility(View.GONE);
                destroyAllTheViewsFromCustomerChatAndInfoBottomSheetLayout();
                //Change customer ride status
                //rideStatus = 0; that means temporary driver declined ride
                //after accepting the ride
                DatabaseReference setRideStatus = FirebaseDatabase.getInstance()
                        .getReference("Users/Customers")
                        .child(Common.customerUID)
                        .child("Current_Ride_Info_Panel");
                HashMap<String, Object> declineRequest = new HashMap<>();
                declineRequest.put("Ride_Status",0);
                setRideStatus.setValue(declineRequest);
                Common.assignedCustomerName = null;
                Common.assignedCustomerProfilePic= null;
            }
        }

        if(Common.mMap != null){
            Common.mMap.clear();
        }

        //Remove Customer Request from driver database
        DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child("Customer_Request");
        mDriverDatabaseRef.setValue(true);

        //Change Driver Status
        DatabaseReference mDriverStatusDbRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID);
        Map map = new HashMap();
        map.put(Common.driverOrBiker + "_Status",0);
        mDriverStatusDbRef.updateChildren(map);

        //Now we will reset our driver in driver's available database
        //As customer for the time being removed our driver from driver
        //available database
        DatabaseReference mDriverIsAvailable = FirebaseDatabase.getInstance()
                .getReference(Common.driverOrBiker + "sAvailAble")
                .child(Common.serviceType);
        DatabaseReference mDriverIsWorking=FirebaseDatabase.getInstance()
                .getReference(Common.driverOrBiker + "s_Working")
                .child(Common.serviceType);
        GeoFire geoFireIsAvailable = new GeoFire(mDriverIsAvailable);
        GeoFire geoFireIsWorking = new GeoFire(mDriverIsWorking);

        geoFireIsWorking.removeLocation(Common.driverUID);

        geoFireIsAvailable.setLocation(Common.driverUID, new GeoLocation(Common.mLastLocation.getLatitude(),
                Common.mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    Log.i(TAG,"There was an error saving the location to GeoFire: " + error);
                } else {
                    Log.i(TAG,"Location saved on server successfully!");
                }
            }
        });

    }
    //End

    private void getThisDriverOrBikersServiceType() {
        if (Common.driverOrBiker != null){
            if (Common.driverUID == null){
                Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

            DatabaseReference mDriverCarModelRef = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child(Common.driverOrBiker +"_Service_Type");
            mDriverCarModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i(TAG, dataSnapshot.toString());
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        if (map.get("Service_Type") != null){
                            Common.serviceType = map.get("Service_Type").toString();
                            Log.i(TAG,"serviceType: "+ Common.serviceType);
                        } else {
                            Log.i(TAG,"serviceType: null");
                        }

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void checkGPSConnectionPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},PERMISSION_REQUEST);
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Common.mMap = googleMap;
        Log.i(TAG,"Google Map Is Ready");
        //Enable Map Toolbar:
        Common.mMap.getUiSettings().setMapToolbarEnabled(true);
        if (!Common.driverStatus.equals("OnTrip")){
            checkGPSConnectionPermission();
        }
        //Above permission check is for below code.
        Common.mMap.setMyLocationEnabled(true);

    }

    public void initializeMap() {
        if (Common.mMap == null) {
            if (Common.mapFragment == null){
                Log.i(TAG,"mapFragment == null & getMapAsync: Called");
                Common.mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                if (Common.mapFragment != null){
                    Common.mapFragment.getMapAsync(this);
                }
            } else {
                Common.mapFragment.getMapAsync(this);
                Log.i(TAG,"getMapAsync: Called");
            }

        }
    }

    private void openGoogleMapForDirection(){
        if (Common.gotAssignedCustomerPickupLocation){
            double lat = Common.customerPickUpLocationLatLng.latitude;
            double lng = Common.customerPickUpLocationLatLng.longitude;
            String format = "geo:0,0?q=" + lat + "," + lng + "( Location title)";
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else if (Common.reachedToAssignedCustomerPickUpLocation){
            double lat = Common.customerDestinationLatLng.latitude;
            double lng = Common.customerDestinationLatLng.longitude;
            String format = "geo:0,0?q=" + lat + "," + lng + "( Location title)";
            Uri uri = Uri.parse(format);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }

    private void openGoogleMapForDirection1(){
        driverLocationLatLng = new LatLng(Common.mLastLocation.getLatitude(),
                Common.mLastLocation.getLongitude());
        Uri uri = null;
        if (Common.gotAssignedCustomerPickupLocation){
            uri = Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                    "&saddr="+driverLocationLatLng.latitude+
                    ","+driverLocationLatLng.longitude+"&daddr="+
                    Common.customerPickUpLocationLatLng.latitude+
                    ","+ Common.customerPickUpLocationLatLng.longitude+
                    "&hl=zh&t=m&dirflg=d");
        } else if (Common.reachedToAssignedCustomerPickUpLocation){
            uri = Uri.parse("http://ditu.google.cn/maps?f=d&source=s_d" +
                    "&saddr="+driverLocationLatLng.latitude+
                    ","+driverLocationLatLng.longitude+"&daddr="+
                    Common.customerDestinationLatLng.latitude+
                    ","+Common.customerDestinationLatLng.longitude+
                    "&hl=zh&t=m&dirflg=d");
        }
        if (uri != null){
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK & Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
            startActivityForResult(intent, 1);
        }

    }

    private void sendNotificationToCustomer(String message) {
        DatabaseReference mDriverNotificationToCustomerDB = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child("Notification_To_Customer");
        HashMap<String, Object> notificationTODriver = new HashMap<>();
        notificationTODriver.put("message", message);
        notificationTODriver.put("time", ServerValue.TIMESTAMP);
        mDriverNotificationToCustomerDB.updateChildren(notificationTODriver)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(TAG,"Successfully notified");
                        }
                    }
                });
    }

    private void recordRide() {
        String encodedPath = PolyUtil.encode(polyLineListForRoadsApiDistance);
        double travelDistance = getDistanceFromPolylinePath1(polyLineOptionFromRoadsApi);
        double distanceToCustomerPickUpPoint = 0.00;
        if (polyLineOptionForCustomerPickUpPoint != null){
            distanceToCustomerPickUpPoint =
                    getDistanceFromPolylinePath1(polyLineOptionForCustomerPickUpPoint);
            Log.i(TAG,"distanceToCustomerPickUpPoint: " + distanceToCustomerPickUpPoint);
            customerPickUpDistanceLatLng.clear();
        }



        //Calculate total fair
        long rideEndTime = getCurrentTimeStamp();
        long totalRideTime = getDifferenceBetweenTwoTime1(rideEndTime,rideStartTime);

        Map<TimeUnit, Long> map1 = getDifferenceBetweenTwoTime(rideStartTime, rideEndTime);
        FareCalculator calculator = new FareCalculator();
        double totalFair = calculator.getCalculatedTotalFare(Common.driverOrBiker,
                Common.serviceType,travelDistance, totalRideTime,0.10);
        double totalDistanceFair = calculator.getCalculatedDistanceFare(Common.driverOrBiker,
                Common.serviceType,travelDistance);
        double totalWaitingTimeFair = calculator.getCalculatedWaitingTimeFair(Common.driverOrBiker,
                Common.serviceType, totalRideTime);


        DatabaseReference mCustomerRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(Common.customerUID)
                .child("Customer_Ride_History")
                .child(Common.driverOrBiker.equals("Driver")? "Car_Ride_History" : "Bike_Ride_History");

        DatabaseReference mDriverRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child(Common.driverOrBiker + "_Driving_History");
        String requestUniqueID = mDriverRideHistory.push().getKey();
        //This above will give us an unique id to save this ride.
        //Now we will save this unique id on driver "Driver_Driving_History" database & also
        //"Customer_Ride_History" database.
        mCustomerRideHistory.child(requestUniqueID).setValue(true);

        MoneyReceipt moneyReceipt = new MoneyReceipt();
        moneyReceipt.setRideId(requestUniqueID);
        moneyReceipt.setCustomerUid(Common.customerUID);
        moneyReceipt.setCustomerName(Common.assignedCustomerName);
        moneyReceipt.setCustomerProfilePic(Common.assignedCustomerProfilePic);
        moneyReceipt.setDestinationLocationAddress(CustomerDestinationAddress);
        moneyReceipt.setPickUpLocationAddress(CustomerPickUpAddress);
        moneyReceipt.setRideEndTime(rideEndTime);
        moneyReceipt.setRideStartTime(rideStartTime);
        moneyReceipt.setDistanceFair(totalDistanceFair);
        moneyReceipt.setWaitingTimeFair(totalWaitingTimeFair);
        moneyReceipt.setTotalPayment(totalFair);
        moneyReceipt.setEncodedPath(encodedPath);
        moneyReceipt.setCustomerDropDistance(travelDistance);
        moneyReceipt.setCustomerPickUpDistance(distanceToCustomerPickUpPoint);
        moneyReceipt.setRideAcceptTime(rideAcceptTime);
        moneyReceipt.setPaymentMethod("Cash");
        moneyReceipt.setDiscountInPercentage(0.10);
        moneyReceipt.setDiscountAmountUptoTaka(10.00);
        moneyReceipt.setDemandChargeInPercentage(0.10);
        moneyReceipt.setServiceType(Common.serviceType);
        mViewModel.addMoneyReceipt(moneyReceipt);


        //Now we will save all the required info of this ride on both the
        //database.
        HashMap<String, Object> map = new HashMap();
        map.put("rideId",requestUniqueID);
        map.put("Customer_Profile_Pic",Common.assignedCustomerProfilePic);
        map.put("Total_Discount",0.10);
        map.put("Discount_Amount_Upto",10);
        map.put("Total_Demand_Charge",0.10);
        map.put("CustomerUID",Common.customerUID);
        map.put("Customer_Name",Common.assignedCustomerName);
        map.put(Common.driverOrBiker +"_Name",driverName);
        map.put("DriverUID",Common.driverUID);
        map.put("Driver_Rating_By_Customer", 0);
        map.put("Customer_Rating_By_Driver", 0);
        map.put("Ride_Accept_Time", rideAcceptTime);
        map.put("Ride_End_Time", rideEndTime);
        map.put("Ride_Start_Time", rideStartTime);
        map.put("Distance_Fair",new DecimalFormat("##.##").format(totalDistanceFair));
        map.put("Waiting_Time_Fair",new DecimalFormat("##.##").format(totalWaitingTimeFair));
        map.put("Total_Payment",new DecimalFormat("##.##").format(totalFair));
        map.put("Payment_Method_Used","Cash");
        map.put("PickUp_Location_Address",CustomerPickUpAddress);
        map.put("Destination_Location_Address", CustomerDestinationAddress);
        map.put("Encoded_Path",encodedPath);
        map.put("Distance",new DecimalFormat("##.##").format(travelDistance));
        map.put("Service_Type",Common.serviceType);
        mDriverRideHistory.child(requestUniqueID).updateChildren(map)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i(TAG,"Successfully updated Driver_Driving_History");
                }
            }
        });
        mCustomerRideHistory.child(requestUniqueID).updateChildren(map)
        .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG,"Successfully updated Customer_Ride_History");
            }
        });
    }

    private Long getCurrentTimeStamp() {
        Long time = System.currentTimeMillis();
        return time;
    }

    private String getDate(Long ride_end_time) {
        FirebaseCrash.log("Home:getDate.called");
        Crashlytics.log("Home:getDate.called");
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time);
        String date = DateFormat.format("dd-MM-yyyy hh:mm a",calendar).toString();
        return date;
    }

    private long getDifferenceBetweenTwoTime1(Long rideEndTime,Long rideStartTime){
        long duration = rideEndTime - rideStartTime;
        long totalMinutes = TimeUnit.MILLISECONDS.toMinutes(duration) % 60;
        Log.i(TAG,"RideStartTime: " + rideStartTime + "\n" +
                "RideEndTime: " + rideEndTime + "\n" +
                "RideDuration: " + duration + "\n" +
                "Total Ride Time In Minutes: " + totalMinutes);
        Log.i(TAG,"RideStartTime: " + getDate(rideStartTime) + "\n" +
        "RideEndTime: " + getDate(rideEndTime));
        return totalMinutes;
    }

    public static Map<TimeUnit,Long> getDifferenceBetweenTwoTime(Long rideStartTime,
                                                                 Long rideEndTime) {
        Date date1 = new Date(rideStartTime);
        Date date2 = new Date(rideEndTime);
        long diffInMillies = date2.getTime() - date1.getTime();
        List<TimeUnit> units = new ArrayList<TimeUnit>(EnumSet.allOf(TimeUnit.class));
        Collections.reverse(units);
        Map<TimeUnit,Long> result = new LinkedHashMap<TimeUnit,Long>();
        long milliesRest = diffInMillies;
        for ( TimeUnit unit : units ) {
            long diff = unit.convert(milliesRest,TimeUnit.MILLISECONDS);
            long diffInMilliesForUnit = unit.toMillis(diff);
            milliesRest = milliesRest - diffInMilliesForUnit;
            result.put(unit,diff);
        }
        Log.i(TAG,"Date: " + result.toString());
        return result;
    }


    private void endCurrentRide() {
        if (customerChatAndInfoBottomSheetLayout != null){
            if (customerChatAndInfoBottomSheetLayout.getVisibility() == View.VISIBLE){
                customerChatAndInfoBottomSheetLayout.setVisibility(View.GONE);
                destroyAllTheViewsFromCustomerChatAndInfoBottomSheetLayout();
            }
        }
        mViewModel.clearAssignedCustomerInfo();
        mViewModel.clearCustomerPickUpLatLng();
        mViewModel.clearAssignedCustomerTripDetails();
        mViewModel.clearOnTripPersistence();
        //Now we will remove customer uthao request data from fireBase Database.
        DatabaseReference mCustomerDatabaseRef = FirebaseDatabase.getInstance()
                .getReference("Customer_request");
        GeoFire geoFire = new GeoFire(mCustomerDatabaseRef);
        geoFire.removeLocation(Common.customerUID);
        ////Now we will remove customer ID from 'driverFoundID' child (CustomerRequestID)
        //Now we have to let our driver know that customer has cancel ride. So we
        //will remove customerID from inside this driver's child.
        //So for precaution we will first check if this customer has found some driver.
        //than we remove this customer id from that driver's database
        String driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (driverUID != null){
            DatabaseReference mDriverDatabaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child("Customer_Request");
            mDriverDatabaseRef.setValue(true);
        }
        //So now go back to previous state
        if (pickUpLocationMarker != null){
            pickUpLocationMarker.remove();
        }
        Common.gotAssignedCustomerPickupLocation = false;
        Common.reachedToAssignedCustomerPickUpLocation = false;
        Common.reachedToAssignedCustomerDestinationLocation = false;
        Common.customerPickUpLocationLatLng = null;
        Common.customerDestinationLatLng = null;
        rideStartTime = 0L;
        DatabaseReference mDriverStatusDbRef = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID);
        Map map = new HashMap();
        map.put(Common.driverOrBiker + "_Status",0);
        mDriverStatusDbRef.updateChildren(map);
        Common.customerUID = ""; //Must be = ""
        Common.assignedCustomerName = null;
        Common.assignedCustomerProfilePic= null;

    }

    private void resetDriverMeter(){
        if (polyLineFromRoadsApi != null){
            polyLineFromRoadsApi.remove();
        }
        polyLineOptionFromRoadsApi = null;
        if (polyLineFromDeviceLocation != null){
            polyLineFromDeviceLocation.remove();
        }
        Common.previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS = null;
        Common.distanceGoneAfterTripStarted = 0;
        if (sampleTakenFrom_pickUpToDestinationLatLng != null){
            sampleTakenFrom_pickUpToDestinationLatLng.clear();
        }

        pickUpToDestinationLatLng = new ArrayList<>();
        sampleTakenFrom_pickUpToDestinationLatLng = new ArrayList<>();
    }


    private void keepListeningToDatabaseForNewCustomer() {
//        GeoFire uses the Firebase database for data storage, allowing query results to be updated
//      in real time as they change. GeoFire selectively loads only the data near certain locations,
//      keeping your applications light and responsive, even with extremely large data sets.
        if (!Common.serviceType.equals("")){
            DatabaseReference mDriverIsAvailable = FirebaseDatabase.getInstance()
                    .getReference(Common.driverOrBiker + "sAvailAble")
                    .child(Common.serviceType);
            DatabaseReference mDriverIsWorking=FirebaseDatabase.getInstance()
                    .getReference(Common.driverOrBiker + "s_Working")
                    .child(Common.serviceType);
            final GeoFire geoFireIsAvailable = new GeoFire(mDriverIsAvailable);
            final GeoFire geoFireIsWorking = new GeoFire(mDriverIsWorking);

            final DatabaseReference mDriverStatus = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child(Common.driverOrBiker + "_Status");

            checkForNewCustomerInDB = FirebaseDatabase.getInstance().getReference()
                    .child("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child("Customer_Request")
                    .child("CustomerRequestID");
            //We will look for our customer under our drivers ID in the firebase database. So we will
            //put listener under "CustomerRequestID" child. If suddenly this child has child
            //under it, than this below value event listener will let us know.
            checkForNewCustomerInDBListener = checkForNewCustomerInDB
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Log.i(TAG,"Our Driver or Biker Has Customer");
                        //If code execution enter this line, that means our driver has a customer
                        Common.rideStatus = 1;
                        //rideStatus == 1,That means our driver is on his way to pick up customer.
                        Common.customerUID = dataSnapshot.getValue().toString();
                        //As customer will remove our driver from Driver's available database. so
                        //here we will add our driver to driver working database
                        geoFireIsWorking.setLocation(Common.driverUID,
                                new GeoLocation(Common.mLastLocation.getLatitude(),
                                        Common.mLastLocation.getLongitude()));
                        mDriverStatus.setValue(1);
                        //If driverStatus = 1 that means driver is working.
                        keepListeningToDatabaseWhetherCustomerCancelledRideOrRideFinished();
                        //So now create "Current_Ride_Info_Panel" under customerUID;
                        //We will let our customer know His Ride_Status.
                        DatabaseReference mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                                .getReference("Users/Customers")
                                .child(Common.customerUID)
                                .child("Current_Ride_Info_Panel");
                        HashMap map = new HashMap();
                        map.put("Ride_Status",Common.rideStatus);
                        map.put(Common.driverOrBiker + "_Distance",drivingDistanceBetweenDriverCurrentLocationAndPickUpLocation);
                        map.put(Common.driverOrBiker + "_ETA",driverETAtoReachPickUpLocation);
                        mCustomerCurrentRideInfoPnaelRef.updateChildren(map);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        } else {

        }

    }

    private void keepListeningToDatabaseWhetherCustomerCancelledRideOrRideFinished(){
        if (Common.customerUID != null){

            DatabaseReference mDriverIsAvailable = FirebaseDatabase.getInstance()
                    .getReference(Common.driverOrBiker + "sAvailAble")
                    .child(Common.serviceType);
            DatabaseReference mDriverIsWorking=FirebaseDatabase.getInstance()
                    .getReference(Common.driverOrBiker + "s_Working")
                    .child(Common.serviceType);

            final GeoFire geoFireIsAvailable = new GeoFire(mDriverIsAvailable);
            final GeoFire geoFireIsWorking = new GeoFire(mDriverIsWorking);

            DatabaseReference mDriverStatus = FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child(Common.driverOrBiker + "s")
                    .child(Common.driverUID)
                    .child(Common.driverOrBiker + "_Status");
            mDriverStatus.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        long driverStatus = (long)dataSnapshot.getValue();
                        Log.i(TAG,"driverStatus: " + driverStatus);
                        if (driverStatus == 0){
                            if (customerChatAndInfoBottomSheetLayout != null){
                                if (customerChatAndInfoBottomSheetLayout.getVisibility() == View.VISIBLE){
                                    customerChatAndInfoBottomSheetLayout.setVisibility(View.GONE);
                                    destroyAllTheViewsFromCustomerChatAndInfoBottomSheetLayout();
                                }
                            }
                            geoFireIsWorking.removeLocation(Common.driverUID);
                            geoFireIsAvailable.setLocation(Common.driverUID,
                                    new GeoLocation(Common.mLastLocation.getLatitude(),
                                            Common.mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                                        @Override
                                        public void onComplete(String key, DatabaseError error) {
                                            Common.driverStatus = "Online";
                                            try {
                                                Glide.with(getApplicationContext())
                                                        .asBitmap()
                                                        .load(R.drawable.ic_driver_online)
                                                        .into(Common.notificationTarget);
                                            } catch (AndroidRuntimeException e){
                                                e.printStackTrace();
                                                Log.i(TAG,"AndroidRuntimeException: " + e.getMessage());
                                            }

                                        }
                                    });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void getAssignedCustomersFCMToken() {

        DatabaseReference mCustomerFCMToken = FirebaseDatabase.getInstance().getReference()
                .child("Users/Customers/"+Common.customerUID+"/FCM_LATEST_TOKEN");
        mCustomerFCMToken.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    customerFCMToken = dataSnapshot.getValue().toString();
                    Log.i(TAG,"customerFCMToken: " + customerFCMToken);
                    DatabaseReference mDriverNotification = FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(Common.driverOrBiker + "s")
                            .child(Common.driverUID)
                            .child("Notification_To_Customer");
                    HashMap<String , Object> fcm = new HashMap<>();
                    fcm.put("customerFCMToken",customerFCMToken);
                    mDriverNotification.updateChildren(fcm);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getAssignedCustomerTripDetails() {
        DatabaseReference assignedCustomerPickUpDestinationDB = FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child("Customer_Request");
        assignedCustomerPickUpDestinationListener =
                assignedCustomerPickUpDestinationDB.
                        addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    //If code execution enter this line, that means our driver has a customer
                    //Destination
                    Map<String , Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    AssignedCustomerTripDetails tripDetails = new AssignedCustomerTripDetails();
                    tripDetails.setTripDetailsRowNumber(0);
                    if (map.get("CustomerDestinationAddress") != null){
                        String CustomerDestinationAddress = map.get("CustomerDestinationAddress").toString();
                        tripDetails.setCustomerDestinationAddress(CustomerDestinationAddress);
                    }

                    if (map.get("CustomerPickUpAddress") != null){
                        String CustomerPickUpAddress = map.get("CustomerPickUpAddress").toString();
                        tripDetails.setCustomerPickUpAddress(CustomerPickUpAddress);

                    }

                    Double destinationLat = 0.0;
                    Double destinationLng = 0.0;
                    if (map.get("CustomerDestinationLat") != null){
                        destinationLat = Double.valueOf(map.get("CustomerDestinationLat").toString());
                        tripDetails.setCustomerDestinationLat(destinationLat);
                    }
                    if (map.get("CustomerDestinationLng") != null){
                        destinationLng = Double.valueOf(map.get("CustomerDestinationLng").toString());
                        tripDetails.setCustomerDestinationLng(destinationLng);
                    }

                    Double pickUpLat = 0.0;
                    Double pickUpLng = 0.0;
                    if (map.get("CustomerPickUpLat") != null){
                        pickUpLat = Double.valueOf(map.get("CustomerPickUpLat").toString());
                        tripDetails.setCustomerPickUpLat(pickUpLat);
                    }
                    if (map.get("CustomerPickUpLng") != null){
                        pickUpLng = Double.valueOf(map.get("CustomerPickUpLng").toString());
                        tripDetails.setCustomerPickUpLng(pickUpLng);
                    }

                    if (map.get("Promo_Code") != null){
//                        String appliedDiscountAmount = map.get("Discount_Amount").toString();
                        String promoCode = "U30Up80";
                        tripDetails.setPromoCode(promoCode);
                    }

                    if (map.get("Surcharge_Amount") != null){
                        double surchargeAmountInPercentage = Double.parseDouble(map.get("Surcharge_Amount")
                                .toString());
                        tripDetails.setSurchargeAmount(surchargeAmountInPercentage);
                    }
                    mViewModel.addAssignedCustomerTripDetails(tripDetails);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getAssignedCustomerInfo() {
        DatabaseReference mCustomerDataBaseRef = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(Common.customerUID)
                .child("Customer_Basic_Info");
        mCustomerDataBaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    //After confirming that our customer has info on database than we make
                    //visible 'assignedCustomerInfoPanel'
                    //So before fetching data we have to show loading sign.
                    Map<String, Object> map =(Map<String, Object>) dataSnapshot.getValue();
                    AssignedCustomerInfo customerInfo = new AssignedCustomerInfo();
                    customerInfo.setInfoRowNumber(0);
                    customerInfo.setCustomerUID(Common.customerUID);
                    customerInfo.setCustomerFCMToken(customerFCMToken);
                    if (map.get("Customer_Name") != null){
                        String customerName = map.get("Customer_Name").toString();
                        customerInfo.setCustomerName(customerName);
                    }
                    if (map.get("Customer_Mobile") != null) {
                        String customerMobile = map.get("Customer_Mobile").toString();
                        customerInfo.setCustomerMobile(customerMobile);
                    }

                    if (map.get("Customer_Ratings") != null) {
                        String customerRatings = map.get("Customer_Ratings").toString();
                        customerInfo.setCustomerRatings(customerRatings);
                    }

                    if (map.get("Customer_Total_Trips") != null) {
                        String customerTotalTrips = map.get("Customer_Total_Trips").toString();
                        customerInfo.setCustomerTotalTrips(customerTotalTrips);
                    }

                    //Now we will display customer's profile pic using glide.
                    if (map.get("Customer_FaceBook_PP") != null) {
                        String customerFaceBookPP = map.get("Customer_FaceBook_PP").toString();
                        customerInfo.setCustomerFaceBookPP(customerFaceBookPP);
                    }

                    //Now we will display customer's profile pic using glide.
                    if (map.get("Customer_Profile_Pic_Url") != null) {
                        String firebaseStorageCustomerPP = map.get("Customer_Profile_Pic_Url").toString();
                        customerInfo.setCustomerProfilePicUrl(firebaseStorageCustomerPP);
                    }
                    mViewModel.addAssignedCustomerInfo(customerInfo);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Marker pickUpLocationMarker;

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //This method will be called when googleApiClient will successfully connected.
        //After googleApiClient is connected, than we can call for location request.
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationSettingsRequestBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        locationSettingsRequestBuilder.setAlwaysShow(true); //this is the key ingredient

        checkIsLocationIsEnabled(locationSettingsRequestBuilder,googleApiClient,locationRequest);

        Log.i(TAG,"GPS: Enabled");
    }

    public void checkIsLocationIsEnabled(LocationSettingsRequest.Builder builder,
                                         final GoogleApiClient googleApiClient,
                                         final LocationRequest locationRequest) {
        FirebaseCrash.log("Home:checkIsLocationIsEnabled.called");
        Crashlytics.log("Home:checkIsLocationIsEnabled.called");
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.
                checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        checkGPSConnectionPermission();
                        LocationServices.FusedLocationApi.requestLocationUpdates(
                                Common.googleApiClientService,
                                Common.locationRequestService,
                                Common.locationListenerService);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        try {
                            createInAppNotification("GPS is turned off",
                                    "Please switch on your GPS");
                            status.startResolutionForResult(Home.this, CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setTitle(R.string.network_not_enabled)
                                .setMessage(R.string.go_to_location_settings_manually)
                                .setPositiveButton(R.string.shut_down_app,
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                finish();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        break;
                }
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        //This method will be called when googleApiClient connection will suspend.
        Log.i(TAG,"GPS: Disabled");
        buildGoogleApiClient();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //This method will be called when googleApiClient will fail to connect.
        Log.i(TAG,"GPS: Disabled");
        buildGoogleApiClient();
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        Common.mLastLocation = location;
        DatabaseReference mCurrentLocation = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child("Current_Location");
        HashMap<String, Object> currentLoc = new HashMap<>();
        currentLoc.put("Location",mLastLocation);
        currentLoc.put("Time",ServerValue.TIMESTAMP);
        mCurrentLocation.setValue(currentLoc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i("LastLoc","Last Location Update: Successful");
                        }
                    }
                });
        if (!isDriverConnectedToDatabase && googleApiClient.isConnected()){
//            connectDriverToDataBase();
//            keepListeningToDatabaseForNewCustomer();
            isDriverConnectedToDatabase = true;
        }


        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Common.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        Common.mMap.animateCamera(CameraUpdateFactory.zoomTo(18));


    }

    @Override
    protected void onStop() {
        Log.i("ActivityLifeCycle","onStop");
        if (mediaPlayer != null){
//            mediaPlayer.release();
        }
        Common.mMap = null;
        if (googleApiClient != null){
            if (googleApiClient.isConnected()){
                LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
                googleApiClient.disconnect();
                if (!googleApiClient.isConnected()){
                    Log.i("LOG","Dis-Connected MainActivity;s API");
                }
            }

            if (googleApiClient!= null){
                googleApiClient = null;
            }
            if (locationRequest != null){
                locationRequest = null;
            }
        }




        super.onStop();
        if (!isDriverLoggingOut){
            //If driver is not logging out than we can disconnect driver from this method.
//            disConnectDriverFromDatabase();
            //If driver is logging out than we can disconnect driver from driverLogOut() method.
            //Otherwise app will crash
        }
//        if (checkForNewCustomerInDB != null && checkForNewCustomerInDBListener != null){
//            checkForNewCustomerInDB.removeEventListener(checkForNewCustomerInDBListener);
//        }
//        if (googleApiClient.isConnected()){
//            googleApiClient.disconnect();
//        }
    }

    @Override
    protected void onDestroy() {
        Log.i("ActivityLifeCycle","onDestroy");
        Common.mMap = null;
        Common.mapFragment = null;
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i("ActivityLifeCycle","onPause");
        if (mediaPlayer != null){
//            mediaPlayer.release();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("ActivityLifeCycle","onResume");
        super.onResume();
        if (mediaPlayer != null){
//            mediaPlayer.start();
        }
    }

    public void driverLogOut(View view) {
        isDriverLoggingOut = true;
//        disConnectDriverFromDatabase();
        FirebaseAuth.getInstance().signOut();
//        Intent intent = new Intent(DriverMapsActivity.this, Home.class);
//        startActivity(intent);
        finish();
    }


    //Code about routing between driver & customer starts here
    private void getRouteToCustomerPickUpLocation(LatLng customerpickUpLocationLatLng) {
        if (mLastLocation != null){
            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.DRIVING)
                    .withListener(this)
                    .alternativeRoutes(true)  //Disable alternativeRoutes
                    .waypoints(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),
                            customerpickUpLocationLatLng)
                    .build();
            routing.execute();
        }

    }

    private void getRouteToCustomerDestinationLocation() {
        if (Common.mLastLocation != null){
            if (Common.customerDestinationLatLng.latitude != 0 && Common.customerDestinationLatLng.longitude != 0){
                Routing routing = new Routing.Builder()
                        .travelMode(AbstractRouting.TravelMode.DRIVING)
                        .withListener(this)
                        .alternativeRoutes(true)  //Disable alternativeRoutes
                        .waypoints(new LatLng(Common.mLastLocation.getLatitude(),Common.mLastLocation.getLongitude()),
                                Common.customerDestinationLatLng)
                        .build();
                routing.execute();
            } else {
                Toast.makeText(Home.this,"Routing is not avaiable\nNo Destination Selected",
                        Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if(e != null) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        //First we remove any polylines which we previously have
        if(polylines.size()>0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        //Now we will create new polylines
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i <route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = Common.mMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getBaseContext(),"Route "+ (i+1) +": distance - "+ route.get(i).getDistanceValue()+": duration - "+ route.get(i).getDurationValue(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingCancelled() {

    }

    private void erasePolyLine(){
        for (Polyline line : polylines){
            line.remove();
            //Above we are removing one poly line at a time
        }
//        Now we have to clear polylines array list
        polylines.clear();
    }

    private void getRouteOfDriverToPickUpLocation(LatLng driverLocation,
                                                  LatLng customerPickUpLocation){
        FirebaseCrash.log("Home:getRouteOfDriverToPickUpLocation.called");
        Crashlytics.log("Home:getRouteOfDriverToPickUpLocation.called");
        LatLng startPosition = new LatLng(driverLocation.latitude,driverLocation.longitude);
        LatLng endPosition = new LatLng(customerPickUpLocation.latitude,customerPickUpLocation.longitude);
        String requestApi = null;

        try{
            requestApi = "https://maps.googleapis.com/maps/api/directions/json?" +
                    "mode=driving&"+
                    "transit_routing_preference=less_driving&"+
                    "origin=" + startPosition.latitude +"," +startPosition.longitude+"&"+
                    "destination="+endPosition.latitude+","+endPosition.longitude
                    +"&" + "key="+getResources().getString(R.string.browser_key_1);
            Log.i(TAG,"requestApi:" + requestApi);//Print url for debug
            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                FirebaseCrash.log("Home:getRouteOfDriverToPickUpLocation: response: success");
                                Crashlytics.log("Home:getRouteOfDriverToPickUpLocation: response: success");
                                Log.i(TAG,"Home:getRouteOfDriverToPickUpLocation: response: success");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("routes");

                                //Get distance and travel time
                                //Start Here
                                JSONObject routes = jsonArray.getJSONObject(0);
                                JSONArray legs = routes.getJSONArray("legs");
                                JSONObject steps = legs.getJSONObject(0);
                                JSONObject distance = steps.getJSONObject("distance");
                                JSONObject duration = steps.getJSONObject("duration");
                                Log.i(TAG, "Google Distance: " + distance.toString() +"\n"
                                        +"Google Duration: " + duration.toString());
                                customerPickUpAddress = steps.getString("end_address");
                                driverDistanceToPicUpLocation = Double.parseDouble(distance.getString("text").replaceAll("[^\\.0123456789]","") );
                                driverETAtoPickUpLocation = Double.parseDouble(duration.getString("text").replaceAll("[^\\.0123456789]","") );
                                setUpAllTheViewsFromCustomerCallLayout();
                                customerDistanceFromDriver.setText(String.valueOf(driverDistanceToPicUpLocation) + " km ");
                                customerPickUpTime.setText(String.valueOf(driverETAtoPickUpLocation) + " min ");
                                Log.i(TAG,"driverDistanceToPicUpLocation: " + driverDistanceToPicUpLocation + "\n" +
                                        "driverETAtoPickUpLocation: " + driverETAtoPickUpLocation + "\n" +
                                "customerPickUpAddress: " + customerPickUpAddress);
                                //Get distance and travel time
                                //End Here



                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject route = jsonArray.getJSONObject(i);
                                    JSONObject poly = route.getJSONObject("overview_polyline");
                                    String polyline = poly.getString("points");
                                    polyLineListForDriverDistance = decodePoly(polyline);
                                }
                                //Adjusting bounds
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng latLng: polyLineListForDriverDistance){
                                    builder.include(latLng);
                                }
                                LatLngBounds bounds = builder.build();
                                //Now we will make boundary for our camera
                                CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,10);
                                if (Common.mMap != null){
                                    Common.mMap.animateCamera(mCameraUpdate);
                                }


                                grayPolylineOptions = new PolylineOptions();
                                grayPolylineOptions.color(Color.GRAY);
                                grayPolylineOptions.width(5);
                                grayPolylineOptions.startCap(new SquareCap());
                                grayPolylineOptions.endCap(new SquareCap());
                                grayPolylineOptions.jointType(JointType.ROUND);
                                grayPolylineOptions.addAll(polyLineListForDriverDistance);
                                if (Common.mMap != null){
                                    grayPolyline = Common.mMap.addPolyline(grayPolylineOptions);
                                }



                                blackPolylineOptions = new PolylineOptions();
                                blackPolylineOptions.color(Color.BLACK);
                                blackPolylineOptions.width(5);
                                blackPolylineOptions.startCap(new SquareCap());
                                blackPolylineOptions.endCap(new SquareCap());
                                blackPolylineOptions.jointType(JointType.ROUND);
                                blackPolylineOptions.addAll(polyLineListForDriverDistance);
                                if (Common.mMap != null){
                                    blackPolyline = Common.mMap.addPolyline(blackPolylineOptions);
                                    getDistanceFromPolylinePath1(blackPolylineOptions);
                                }



                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.i(TAG,"Home:getDirection: exception: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(Home.this,"" + t.getMessage(),Toast.LENGTH_LONG).show();
                            FirebaseCrash.log("Home:getDirection: response: failed");
                            Crashlytics.log("Home:getDirection: response: failed");
                            Log.i(TAG,"Home:getDirection: response: failed: " + t.getMessage());
                        }
                    });

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void createPathDataForRoadApi(){
        takeSampleFrom_PickUpToDestinationLatLng();
        printAllTheLatLngObjectFromSampleTakenFrom_pickUpToDestinationLatLng();
        String roadPathForRequestingAPI1 = "";
        String roadPathForRequestingAPI2 = "";
        String roadPathForRequestingAPI3 = "";
        String roadPathForRequestingAPI4 = "";
        String roadPathForRequestingAPI5 = "";
        String roadPathForRequestingAPI6 = "";
        String roadPathForRequestingAPI7 = "";
        String roadPathForRequestingAPI8 = "";
        String roadPathForRequestingAPI9 = "";
        String roadPathForRequestingAPI10 = "";
        int currentIndexNumberOfLatLng = 0;
        for (int i = 0; i < sampleTakenFrom_pickUpToDestinationLatLng.size(); i++){
            currentIndexNumberOfLatLng++;
            Log.i(TAG,"currentIndexNumberOfLatLng: " + currentIndexNumberOfLatLng);
            Log.i(TAG,"Sample Array List Size: " + sampleTakenFrom_pickUpToDestinationLatLng.size());

                              // 0 To 99
            if (currentIndexNumberOfLatLng < 100){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI1.equals("")){
                    roadPathForRequestingAPI1 = roadPathForRequestingAPI1 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI1 =  roadPathForRequestingAPI1 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI1 : " + roadPathForRequestingAPI1);

                if (currentIndexNumberOfLatLng == 99 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI1 = roadPathForRequestingAPI1
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI1: " + roadPathForRequestingAPI1);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 1;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI1);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }


            }
                                         // 97 To 195
            if (96 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 196){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI2.equals("")){
                    roadPathForRequestingAPI2 = roadPathForRequestingAPI2 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI2 =  roadPathForRequestingAPI2 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI2 : " + roadPathForRequestingAPI2);

                if (currentIndexNumberOfLatLng == 195 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI2 = roadPathForRequestingAPI2
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI2: " +
                            roadPathForRequestingAPI2);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 2;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI2);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }

            }
                                           // 193 To 291
            if (192 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 292){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI3.equals("")){
                    roadPathForRequestingAPI3 = roadPathForRequestingAPI3 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI3 =  roadPathForRequestingAPI3 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI3 : " + roadPathForRequestingAPI3);

                if (currentIndexNumberOfLatLng == 291 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI3 = roadPathForRequestingAPI3
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI3: " + roadPathForRequestingAPI3);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 3;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI3);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }
                                            // 289 To 387
            if (288 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 388){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI4.equals("")){
                    roadPathForRequestingAPI4 = roadPathForRequestingAPI4 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI4 =  roadPathForRequestingAPI4 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI4 : " + roadPathForRequestingAPI4);

                if (currentIndexNumberOfLatLng == 387 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI4 = roadPathForRequestingAPI4
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI4: " + roadPathForRequestingAPI4);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 4;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI4);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

                                           // 385 To 483
            if (384 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 484){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI5.equals("")){
                    roadPathForRequestingAPI5 = roadPathForRequestingAPI5 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI5 =  roadPathForRequestingAPI5 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI5 : " + roadPathForRequestingAPI5);

                if (currentIndexNumberOfLatLng == 483 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI5 = roadPathForRequestingAPI5
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI5: " + roadPathForRequestingAPI5);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 5;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI5);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

                                          // 481 To 579
            if (480 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 580){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI6.equals("")){
                    roadPathForRequestingAPI6 = roadPathForRequestingAPI6 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI6 =  roadPathForRequestingAPI6 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI6 : " + roadPathForRequestingAPI6);

                if (currentIndexNumberOfLatLng == 579 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI6 = roadPathForRequestingAPI6
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI6: "
                            + roadPathForRequestingAPI6);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 6;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI6);
                    if (currentIndexNumberOfLatLng ==
                           sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

                                           // 577 To 676
            if (576 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 677){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI7.equals("")){
                    roadPathForRequestingAPI7 = roadPathForRequestingAPI7 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI7 =  roadPathForRequestingAPI7 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI7 : " + roadPathForRequestingAPI7);

                if (currentIndexNumberOfLatLng == 676 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI7 = roadPathForRequestingAPI7
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI7: "
                            + roadPathForRequestingAPI7);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 7;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI7);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

                                         // 674 To 772
            if (673 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 773){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI8.equals("")){
                    roadPathForRequestingAPI8 = roadPathForRequestingAPI8 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI8 =  roadPathForRequestingAPI8 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI8 : " + roadPathForRequestingAPI8);

                if (currentIndexNumberOfLatLng == 772 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI8 = roadPathForRequestingAPI8
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI8: "
                            + roadPathForRequestingAPI8);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 8;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI8);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

                                           // 770 To 868
            if (769 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 869){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI9.equals("")){
                    roadPathForRequestingAPI9 = roadPathForRequestingAPI9 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI9 =  roadPathForRequestingAPI9 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI9 : " + roadPathForRequestingAPI9);

                if (currentIndexNumberOfLatLng == 868 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI9 = roadPathForRequestingAPI9
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI9: "
                            + roadPathForRequestingAPI9);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 9;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI9);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

                                           // 866 To 964
            if (865 < currentIndexNumberOfLatLng && currentIndexNumberOfLatLng < 965){
                //As there will be no this sign '|' for the first LatLng.
                //We will check if we are going to save gpsCoordinate for the
                //first time or not
                if (roadPathForRequestingAPI10.equals("")){
                    roadPathForRequestingAPI10 = roadPathForRequestingAPI10 +
                            sampleTakenFrom_pickUpToDestinationLatLng.get(i) + "";
                } else {
                    roadPathForRequestingAPI10 =  roadPathForRequestingAPI10 +
                            "|" + sampleTakenFrom_pickUpToDestinationLatLng.get(i) ;
                }
                Log.i(TAG,"roadPathForRequestingAPI10 : " + roadPathForRequestingAPI10);

                if (currentIndexNumberOfLatLng == 964 ||
                        currentIndexNumberOfLatLng ==
                                sampleTakenFrom_pickUpToDestinationLatLng.size()){
                    //Now we will clear garbage from our data
                    roadPathForRequestingAPI10 = roadPathForRequestingAPI10
                            .replaceAll("[lat/lng: ()]","");
                    Log.i(TAG,"Cleared roadPathForRequestingAPI10: "
                            + roadPathForRequestingAPI10);
                    //Now we will send our data to server
                    Common.numberOfPolyLineToBeUploadedToServer = 10;
                    getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
                            (roadPathForRequestingAPI10);
                    if (currentIndexNumberOfLatLng ==
                            sampleTakenFrom_pickUpToDestinationLatLng.size()){
                        //If execution enter this line of code that means, we have successfully
                        //drawn our path from pickup location to destination
                        clearLatLngTable();
                        pickUpToDestinationLatLng.clear();
                    }
                }
            }

            if (currentIndexNumberOfLatLng > 980){
                Toast.makeText(this, "Please contact with " +
                        "uthao customer care", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void printAllTheLatLngObjectFromSampleTakenFrom_pickUpToDestinationLatLng() {
        int item = 0;
        for (int i = 0; i < sampleTakenFrom_pickUpToDestinationLatLng.size(); i++){
            item++;
            Log.i(TAG,"Sample Item: " + item + " | LatLng: " + sampleTakenFrom_pickUpToDestinationLatLng.get(i));
        }
    }

    private void takeSampleFrom_PickUpToDestinationLatLng() {
        //Here we will iterate through the every LatLng object which has been
        //saved in our device during trip. So we will go through every object
        //and add distance in the checkTheDistance variable. If distance is more
        //than 100 meter than we will check if the last taken sample and this new samples
        //physical distance is more than 100 meter or not than we will take the sample
        int distanceBetweenTwoSampleInMeter = 100;
        printAllTheLatLngObjectFromPickUpToDestinationLatLngInLogCat();
        float checkTheDistance = 0;
        for(int i = 1; i <= pickUpToDestinationLatLng.size(); i++) {
            //Here int i = 1; must be, App will crash if not
            Log.i(TAG,"pickUpToDestinationLatLng.size(): " +
                    pickUpToDestinationLatLng.size());
            Log.i(TAG,"Iterator number to take sample: " + i);
            Log.i(TAG,"Current LatLng Object: " + pickUpToDestinationLatLng.get(i - 1));
            if (sampleTakenFrom_pickUpToDestinationLatLng.isEmpty()){
                //This means we don't have to check distance. Because we are going
                //to save first data from 'pickUpToDestinationLatLng' array to
                // 'sampleTakenFrom_pickUpToDestinationLatLng' array
                sampleTakenFrom_pickUpToDestinationLatLng
                        .add(pickUpToDestinationLatLng.get(i -1));
                checkTheDistance = 0;
            }
            if (pickUpToDestinationLatLng.size() == (i)){
                //This means we don't have to check distance. Because we are going
                //to save last data from 'pickUpToDestinationLatLng' array to
                // 'sampleTakenFrom_pickUpToDestinationLatLng' array
                sampleTakenFrom_pickUpToDestinationLatLng
                        .add(pickUpToDestinationLatLng.get(i - 1));
                checkTheDistance = 0;
            }else {
                Double startLatitude = pickUpToDestinationLatLng.get(i - 1).latitude;
                Double startLongitude = pickUpToDestinationLatLng.get(i - 1).longitude;

                Double endLatitude = pickUpToDestinationLatLng.get(i).latitude;
                Double endLongitude = pickUpToDestinationLatLng.get(i).longitude;

                float results[] = new float[10];

                Location.distanceBetween(startLatitude, startLongitude,
                        endLatitude, endLongitude, results);
                checkTheDistance = checkTheDistance + results[0];
                Log.i(TAG,"checkTheDistance: " + checkTheDistance );
                if (checkTheDistance > distanceBetweenTwoSampleInMeter){
                    //This means we will take sample from every 100 meter apart
                    if(sampleTakenFrom_pickUpToDestinationLatLng != null){
                        //Now we will check the distance between our last object saved in
                        // 'sampleTakenFrom_pickUpToDestinationLatLng' array and to current
                        //object which is 'Common.pickUpToDestinationLatLng.get(i)'
                        Double startLatitudeLastObject = sampleTakenFrom_pickUpToDestinationLatLng
                                .get(sampleTakenFrom_pickUpToDestinationLatLng.size() - 1).latitude;
                        Double startLongitudeLastObject = sampleTakenFrom_pickUpToDestinationLatLng
                                .get(sampleTakenFrom_pickUpToDestinationLatLng.size() - 1).longitude;

                        Double endLatitudeCurrentObject = pickUpToDestinationLatLng.get(i).latitude;
                        Double endLongitudeCurrentObject = pickUpToDestinationLatLng.get(i).longitude;

                        float resultsOfDifference[] = new float[10];

                        Location.distanceBetween(startLatitudeLastObject, startLongitudeLastObject,
                                endLatitudeCurrentObject, endLongitudeCurrentObject, resultsOfDifference);
                        float distanceBetweenLastObjectSavedAndCurrentObject = resultsOfDifference[0];
                        Log.i(TAG,"distanceBetweenLastObjectSavedAndCurrentObject: " + distanceBetweenLastObjectSavedAndCurrentObject );
                        if (distanceBetweenLastObjectSavedAndCurrentObject > distanceBetweenTwoSampleInMeter){
                            sampleTakenFrom_pickUpToDestinationLatLng
                                    .add(pickUpToDestinationLatLng.get(i));
                            checkTheDistance = 0;
                            distanceBetweenLastObjectSavedAndCurrentObject = 0;
                            Log.i(TAG, "Sample Size: " + sampleTakenFrom_pickUpToDestinationLatLng.size());
                        }


                    }
                }
            }

        }
    }

    private void printAllTheLatLngObjectFromPickUpToDestinationLatLngInLogCat() {
        int item = 0;
        for (int i = 0; i < pickUpToDestinationLatLng.size(); i++){
            item++;
            Log.i(TAG,"Item: " + item + " | LatLng: " + pickUpToDestinationLatLng.get(i));
        }
    }


    private void createPolyLineFromRoadsApi(){
        polyLineOptionFromRoadsApi = new PolylineOptions();
        polyLineOptionFromRoadsApi.color(Color.BLUE);
        polyLineOptionFromRoadsApi.width(5);
        polyLineOptionFromRoadsApi.startCap(new SquareCap());
        polyLineOptionFromRoadsApi.endCap(new SquareCap());
        polyLineOptionFromRoadsApi.jointType(JointType.ROUND);
        polyLineOptionFromRoadsApi.addAll(polyLineListForRoadsApiDistance);
        polyLineFromRoadsApi = Common.mMap.addPolyline(polyLineOptionFromRoadsApi);
        if (pickUpToDestinationLatLng.isEmpty()){
            //If execution enter this line of code that means all the data from roads
            //api has been fetched from the server and road path has been displayed
            //on the map
            getCustomerPickUpDistance();

        }

    }

    private Observer observer;
    private ArrayList<LatLng> customerPickUpDistanceLatLng;
    private void getCustomerPickUpDistance() {
        observer = new Observer<List<CustomerPickUpLatLng> >() {
            @Override
            public void onChanged(@Nullable List<CustomerPickUpLatLng> customerPickUpLatLng) {
                if (customerPickUpLatLng != null){
                    Log.i("PickUpLatLng","customerPickUpLatLng.size(): " + customerPickUpLatLng.size());
                    if (customerPickUpDistanceLatLng == null){
                        customerPickUpDistanceLatLng = new ArrayList<>();
                    } else {
                        customerPickUpDistanceLatLng.clear();
                    }

                    for (CustomerPickUpLatLng singleObject:customerPickUpLatLng){
                        LatLng latLng = new LatLng(singleObject.getLatitude(),singleObject.getLongitude());
                        customerPickUpDistanceLatLng.add(latLng);
                        Log.i("tripStatus","Finish: LatLng: " + singleObject.getLatitude() + " | "
                                +singleObject.getLongitude());
                    }
                    drawPolyLineFromDriverReqAcceptLocToCustomerPicUpLoc();
                    recordRide();
                    endCurrentRide();

                }
                removeObserVer();
            }
        };
        addObserver();
    }

    private void addObserver(){
        mViewModel.mCustomerPickUpLatLng.observe(this, observer);
    }

    private void removeObserVer() {
        mViewModel.mCustomerPickUpLatLng.removeObserver(observer);
    }

    private PolylineOptions polyLineOptionForCustomerPickUpPoint;
    private Polyline polyLineForCustomerPickUpPoint;
    private void drawPolyLineFromDriverReqAcceptLocToCustomerPicUpLoc() {
        if (polyLineOptionForCustomerPickUpPoint == null){
            polyLineOptionForCustomerPickUpPoint = new PolylineOptions();
            polyLineOptionForCustomerPickUpPoint.color(Color.BLACK);
            polyLineOptionForCustomerPickUpPoint.width(15);
            polyLineOptionForCustomerPickUpPoint.startCap(new SquareCap());
            polyLineOptionForCustomerPickUpPoint.endCap(new SquareCap());
            polyLineOptionForCustomerPickUpPoint.jointType(JointType.ROUND);
        }
        if (customerPickUpDistanceLatLng != null){
            polyLineOptionForCustomerPickUpPoint.addAll(customerPickUpDistanceLatLng);
            if (Common.mMap != null){
                polyLineForCustomerPickUpPoint = Common.mMap.addPolyline
                        (polyLineOptionForCustomerPickUpPoint);
            }
        }


    }


    private Double getArraylistComputedRoadDistance(){
        double distance = SphericalUtil.computeLength(pickUpToDestinationLatLng);
        return distance;
    }

    private float getDistanceFromPolylinePath1(PolylineOptions points){
        float totalDistance = 0;

//        Log.i(TAG,"Size of polyline points: " + points.getPoints().size());

        for(int i = 1; i < points.getPoints().size(); i++) {
//            Log.i(TAG,"polyline points: " + points.getPoints().get(i));
            Location currLocation = new Location("this");
            currLocation.setLatitude(points.getPoints().get(i).latitude);
            currLocation.setLongitude(points.getPoints().get(i).longitude);

            Location lastLocation = new Location("this");
            lastLocation.setLatitude(points.getPoints().get(i-1).latitude);
            lastLocation.setLongitude(points.getPoints().get(i-1).longitude);

            totalDistance += (lastLocation.distanceTo(currLocation));
//            Log.i(TAG,"Iteration Distance By Method1: " + totalDistance + " meter");
        }

        Log.i(TAG,"Distance Found From Polyline By Method1: " + totalDistance + " meter");

        return totalDistance / 1000;
    }

    private float getDistanceFromPolyLinePath2(PolylineOptions points){
        float totalDistance = 0;

        for(int i = 1; i < points.getPoints().size(); i++) {
            Double startLatitude = points.getPoints().get(i - 1).latitude;
            Double startLongitude = points.getPoints().get(i - 1).longitude;

            Double endLatitude = points.getPoints().get(i).latitude;
            Double endLongitude = points.getPoints().get(i).longitude;

            float results[] = new float[10];

            Location.distanceBetween(startLatitude, startLongitude,
                    endLatitude, endLongitude, results);
            totalDistance = totalDistance + results[0];
//            Log.i(TAG,"Iteration Distance By Method2: " + totalDistance + " meter");
        }

        Log.i(TAG,"Distance Found From Polyline By Method2: " + totalDistance + " meter");

        return totalDistance;
    }



    private void getRoadDistanceBetweenPickupAndDestinationUsingRoadsApi
            (String roadPathForRequestingAPI){
        String requestApi = null;
        try {
            requestApi = "https://roads.googleapis.com/v1/" +
                    "snapToRoads?" +
                    "path=" + roadPathForRequestingAPI +
                    "&interpolate=true&key=" + getResources().getString(R.string.browser_key_1);
            Log.i(TAG,"requestApi:" + requestApi);//Print url for debug
            mRoadService.getRoad(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            try {
                                Log.i("RoadApi","RoadApiCalled");
                                JSONObject jsonObject = new JSONObject(response.body().toString());
//                                Log.i(TAG,"jsonObject" + jsonObject.toString());
                                JSONArray jsonArray = jsonObject.getJSONArray("snappedPoints");
                                Log.i(TAG,"jsonArray.length()" + jsonArray.length());

                                for (int i = 0; i <jsonArray.length(); i++){
                                    JSONObject pointsObject = jsonArray.getJSONObject(i);
//                                    Log.i(TAG,"points object: " + pointsObject.toString());
                                    JSONObject locationObject = pointsObject.getJSONObject("location");
//                                    Log.i(TAG,"locationObject: " + locationObject.toString());
                                    String Latitude = locationObject.getString("latitude");
                                    String Longitude = locationObject.getString("longitude");
//                                    Log.i(TAG,"Latitude: " + Latitude + " | Longitude: " + Longitude);
                                    LatLng latLng = new LatLng(Double.valueOf(Latitude),
                                            Double.valueOf(Longitude));
                                    polyLineListForRoadsApiDistance.add(latLng);
                                }

                                createPolyLineFromRoadsApi();

                            } catch (JSONException e){
                                e.printStackTrace();
                                Log.i(TAG,"RoadApiResponseJSONException: " + e.getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i(TAG,"RoadApiResponseOnFailure: " + t.getMessage());
                        }
                    });
        } catch (Exception e){
            Log.i(TAG,"RoadApiException: " +e.getMessage());
        }

    }

    private List decodePoly(String encoded) {
        FirebaseCrash.log("Home:decodePoly.called");
        Crashlytics.log("Home:decodePoly.called");
        List poly = new ArrayList();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    private void createInAppNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "Default_Notification_Channel");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setChannelId("Default_Notification_Channel");
        builder.setSmallIcon(R.drawable.ic_notification);
        //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                STREAM_DEFAULT);
        //Vibration
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        //LED
        builder.setLights(Color.RED, 3000, 3000);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(0,builder.build());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE && resultCode == RESULT_OK){
            checkGPSConnectionPermission();
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    Common.googleApiClientService,
                    Common.locationRequestService,
                    Common.locationListenerService);

        } else if (requestCode == CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE && resultCode == RESULT_CANCELED){
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle(R.string.network_not_enabled)
                    .setMessage(R.string.Customer_Denied_Location)
                    .setPositiveButton(R.string.SHUT_DOWN_THIS_APP,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();
                                }
                            });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }
    }

    private void retrieveLatLngFromSQLiteDB(){
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = helper.retrieveLatLngFromQLiteDatabase(db);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                Log.i(TAG,"Size of LatLng in SQLite: " + cursor.getCount());
                int latitude_Column_number = cursor.getColumnIndex(DbContract
                        .TABLE_LATLNG_FIRST_COLUMN_LATITUDE);
                int longitude_Column_number = cursor.getColumnIndex(DbContract
                        .TABLE_LATLNG_SECOND_COLUMN_LONGITUDE);
                for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
                    Double latitude = cursor.getDouble(latitude_Column_number);
                    Double longitude = cursor.getDouble(longitude_Column_number);
                    Log.i(TAG,"Latitude: " + latitude + " | Longitude: " + longitude);
                    LatLng latLng = new LatLng(latitude,longitude);
                    pickUpToDestinationLatLng.add(latLng);
                }
                if (Common.mMap != null){
                    createPolyLineFromDeviceLocation();
                    polyLineFromDeviceLocation.setPoints(pickUpToDestinationLatLng);
                }
                cursor.close();
            }
        }
    }

    public void clearLatLngTable() {
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from "+ DbContract.TABLE_LATLNG);
        db.close();
    }

    private void createPolyLineFromDeviceLocation(){
        polyLineOptionFromDeviceLocation = new PolylineOptions();
        polyLineOptionFromDeviceLocation.color(Color.CYAN);
        polyLineOptionFromDeviceLocation.width(5);
        polyLineOptionFromDeviceLocation.startCap(new SquareCap());
        polyLineOptionFromDeviceLocation.endCap(new SquareCap());
        polyLineOptionFromDeviceLocation.jointType(JointType.ROUND);
        if (Common.mMap != null){
            polyLineFromDeviceLocation = Common.mMap
                    .addPolyline(polyLineOptionFromDeviceLocation);
        } else {
            Log.i(TAG,"Can't create polyline: Common.mMap = null");
        }

    }


    //Navigation menu
    private void populateNavigationMenuToRecyclerView(){
        navigationMenuRecyclerView = (RecyclerView) findViewById(R.id.navigationMenuRecyclerView);
        navigationMenuRecyclerView.setHasFixedSize(true);
        navigationMenuRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        navigationMenuAdapter = new NavigationMenuAdapter(Home.this,getNavigationMenuData());
        navigationMenuRecyclerView.setAdapter(navigationMenuAdapter);
        navigationMenuRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private List<NavigationMenuItem> getNavigationMenuData(){
        List<NavigationMenuItem> data = new ArrayList<>();
        int[] icon = {R.drawable.ic_history,
                R.drawable.ic_birthday,
                R.drawable.ic_notification_bell,
                R.drawable.ic_payment,
                R.drawable.ic_profile_pic,
                R.drawable.ic_settings,
                R.drawable.ic_help,
                R.drawable.ic_language};

        int[]titles = {
                R.string.ride_history,
                R.string.promotions,
                R.string.notification,
                R.string.payment,
                R.string.Account,
                R.string.settings,
                R.string.help,
                R.string.language,};

        for (int i = 0; i < titles.length && i < icon.length; i++){
            NavigationMenuItem currentItem = new NavigationMenuItem();
            currentItem.navigation_icon_id = icon[i];
            currentItem.navigation_title = titles[i];
            data.add(currentItem);
        }
        return data;
    }


    private void setNavigationProfilePic(){
        FirebaseCrash.log("Home:setNavigationProfilePic.called");
        Crashlytics.log("Home:setNavigationProfilePic.called");
        mViewModel.navigationMenuSubSetLiveData.observe(this, new Observer<NavigationMenuSubSet>() {
            @Override
            public void onChanged(@Nullable NavigationMenuSubSet navigationMenuSubSet) {
                if (navigationMenuSubSet != null){
                    nav_profile_name.setText(navigationMenuSubSet.getFull_name());
                    nav_profile_image.setImageBitmap(getImage(navigationMenuSubSet.getProfile_Pic()));
                }
            }
        });


    }

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        FirebaseCrash.log("Home:getBytes.called");
        Crashlytics.log("Home:getBytes.called");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 20, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        FirebaseCrash.log("Home:getImage.called");
        Crashlytics.log("Home:getImage.called");
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }





}
