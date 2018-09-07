package com.sand_corporation.www.uthaopartner.ForeGroundService;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.NotificationTarget;
import com.crashlytics.android.Crashlytics;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.HomeActivity.Home;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.AppDatabase;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.TripPersistance.customerPickUpLatLngTable.CustomerPickUpLatLng;
import com.sand_corporation.www.uthaopartner.SQLiteLatLngDB.DbHelper;

import java.util.HashMap;

import static android.support.v4.app.ActivityCompat.requestPermissions;
import static android.support.v4.app.NotificationCompat.STREAM_DEFAULT;

/**
 * Created by HP on 2/1/2018.
 */

public class ForeGroundService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int ONGOING_NOTIFICATION_ID = 2000;
    private static RemoteViews notificationView;
    private static final String TAG = "CheckUForegroundService";
    private static Notification notification;
    private GoogleMap serviceMap;
    private int doPeriodicWorkInSideAppTimerInSecond = 15;
    private Handler doPeriodicWorkInSideApp;
    private static boolean isDoPeriodicWorkInSideAppHandlerShouldRun = false;
    private static final int CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE_SERVICE = 1001;
    private static final int CHECK_GPS_PERMISSION_REQUEST = 102;
    private static final int OPEN_HOME_PAGE_BY_PENDING_INTENT = 103;
    private static final int IN_APP_NOTIFICATION = 104;


    private static void disConnectDriverFromDatabase(Context context){
        //Now we will send this driver status to firebase database. This activity is closed
        //means driver is unavailable for taking customer. So we will let know our server about
        //this driver status & his userID
        String driverUID = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDriverDataBaseRef = FirebaseDatabase.getInstance()
                .getReference(Common.driverOrBiker + "sAvailAble")
                .child(Common.serviceType);
        GeoFire geoFire = new GeoFire(mDriverDataBaseRef);
        geoFire.removeLocation(driverUID);
        Common.driverStatus = "Offline";
        isDoPeriodicWorkInSideAppHandlerShouldRun = false;
        Toast.makeText(context, "Disconnected from server", Toast.LENGTH_SHORT).show();

    }

    private static void connectDriverToDataBase(final Context context){
        isDoPeriodicWorkInSideAppHandlerShouldRun = true;
        if (Common.mLastLocation != null && Common.serviceType != null){
            String driverUID = FirebaseAuth.getInstance().getUid();
            DatabaseReference mDriverDataBaseRef = FirebaseDatabase.getInstance()
                    .getReference(Common.driverOrBiker + "sAvailAble")
                    .child(Common.serviceType);
            GeoFire geoFire = new GeoFire(mDriverDataBaseRef);
            geoFire.setLocation(driverUID, new GeoLocation(Common.mLastLocation.getLatitude(),
                    Common.mLastLocation.getLongitude()), new GeoFire.CompletionListener() {
                @Override
                public void onComplete(String key, DatabaseError error) {
                    if (error != null) {
                        Log.i(TAG,"There was an error saving the location to GeoFire: " + error);
                    } else {
                        Common.driverStatus = "Online";
                        Toast.makeText(context, "Connected to server", Toast.LENGTH_SHORT).show();
                        Glide.with(context)
                                .asBitmap()
                                .load(R.drawable.ic_driver_online)
                                .into(Common.notificationTarget);
                        Log.i(TAG,"Location saved on server successfully!");
                    }
                }
            });
        } else {
            Log.i(TAG,"Waiting for location or service type");
            Toast.makeText(context, "Waiting for location or service type", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        buildGoogleApiClient();
        if (Common.googleApiClientService.isConnected()){
            Log.i(TAG,"googleApiClientService: CONNECTED.onCreate");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initNotificationChannels();
        Intent notificationIntent = new Intent(this, Home.class);
        notificationIntent.setAction(Home.SERVICE_ACTION);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        notificationView = new RemoteViews(this.getPackageName(),
                R.layout.foreground_service_notification);

        notificationView.setImageViewResource(R.id.swithDriverStatus,
                R.drawable.ic_driver_offline);


        startGPSAndInternetConnectionCheckTest();




//        GpsTrackerAlarmTrigger.scheduleExactAlarm(getBaseContext(),
//                (AlarmManager)getBaseContext().getSystemService(Context.ALARM_SERVICE),
//                (10*1000)); //10 Seconds

        Intent buttonCloseIntent = new Intent(this,
                NotificationCloseButtonHandler.class);
        buttonCloseIntent.putExtra("action", "close");

        PendingIntent buttonClosePendingIntent = pendingIntent.getBroadcast(this,
                0, buttonCloseIntent, 0);
        notificationView.setOnClickPendingIntent(R.id.swithDriverStatus,
                buttonClosePendingIntent);

        Bitmap icon = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        String notification_channel_id = "com.sand_corporation.www.uthaopartner.FirebaseMessaging.foreGroundNotification";
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = new NotificationCompat.Builder(this, notification_channel_id)
                    .setContentTitle(getText(R.string.ongoing_notification_title))
                    .setContentText(getText(R.string.ongoing_notification_message))
                    .setTicker(getText(R.string.ongoing_notification_ticker_text))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContent(notificationView)
                    .setOngoing(true).build();

            if (Common.notificationTarget != null){
                Log.i(TAG,"notificationTarget != null");
            } else {
                Common.notificationTarget = new NotificationTarget(
                        this,
                        R.id.swithDriverStatus,
                        notificationView,
                        notification,
                        ONGOING_NOTIFICATION_ID);
                Log.i(TAG,"notificationTarget == null");
            }
        }





        startForeground(ONGOING_NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    private void initNotificationChannels() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String foreGroundNotification = "com.sand_corporation.www.uthaopartner.FirebaseMessaging.foreGroundNotification";
        CharSequence foreGroundNotificationName = getString(R.string.foreGroundNotificationName);
        String foreGroundNotificationDescription = getString(R.string.foreGroundNotificationDescription);

        NotificationChannel foreGroundNotificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importanceOne = NotificationManager.IMPORTANCE_HIGH;
            foreGroundNotificationChannel = new NotificationChannel(foreGroundNotification,
                    foreGroundNotificationName,
                    importanceOne);
            foreGroundNotificationChannel.setDescription(foreGroundNotificationDescription);
            foreGroundNotificationChannel.enableLights(true);
            foreGroundNotificationChannel.setLightColor(Color.GREEN);
            foreGroundNotificationChannel.enableVibration(false);
            mNotificationManager.createNotificationChannel(foreGroundNotificationChannel);

        }

    }

    private void startGPSAndInternetConnectionCheckTest() {
        if (doPeriodicWorkInSideApp == null){
            Log.i(TAG,"doPeriodicWorkInSideApp == null");
            doPeriodicWorkInSideApp = new Handler();
            doPeriodicWorkInSideApp.postDelayed(periodicUpdate,
                    doPeriodicWorkInSideAppTimerInSecond *1000); //1000 = 1 second
            Log.i(TAG,"ForeGround Service Handler Started");
        } else {
            Log.i(TAG,"doPeriodicWorkInSideApp != null");
        }
    }

    private void endGPSAndInternetConnectionCheckTest() {
        if (doPeriodicWorkInSideApp != null){
            doPeriodicWorkInSideApp.removeCallbacks(periodicUpdate);
            doPeriodicWorkInSideApp = null;
            Log.i(TAG,"ForeGround Service Handler Stopped");
        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected synchronized void buildGoogleApiClient() {
        Common.googleApiClientService = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        Common.googleApiClientService.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //This method will be called when googleApiClient will successfully connected.
        //After googleApiClient is connected, than we can call for location request.
        Common.locationRequestService = new LocationRequest();
        Common.locationRequestService.setInterval(2000); // 2 seconds
        Common.locationRequestService.setFastestInterval(2000); // 2 seconds
        Common.locationRequestService.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        Common.locationSettingsRequestBuilderService= new LocationSettingsRequest.Builder()
                .addLocationRequest(Common.locationRequestService);
        Common.locationListenerService = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Common.mLastLocation = location;
                if (Common.tripStatus.equals("Started")){
                    polyLineOptionForCustomerPickUpPointInitialized = false;
                    initializeGoogleMap();
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                    savePickUpToDestinationLatLngIntoSQLiteDB(location.getLatitude(), location.getLongitude());
                    checkHowMuchDistanceTraveledByDeviceGPS(latLng);
//                    Log.i("tripStatus","Common.tripStatus == Started");

                }else if (!Common.tripStatus.equals("Started")){
//                    Log.i("tripStatus","Common.tripStatus != Started");
                }

                if (Common.tripStatus.equals("OnRoute")){
                    initializeGoogleMap();
                    CustomerPickUpLatLng customerPickUpLatLng = new CustomerPickUpLatLng();
                    customerPickUpLatLng.setLatitude(location.getLatitude());
                    customerPickUpLatLng.setLongitude(location.getLongitude());
                    drawPolyLineFromDriverReqAcceptLocToCustomerPicUpLoc(location);

                    new ForeGroundServiceRepository().addCustomerPickUpLatLng(customerPickUpLatLng,
                            AppDatabase.getInMemoryDatabase(getApplicationContext()));
//                    Log.i("tripStatus","Common.tripStatus == OnRoute");
                    Log.i("tripStatus","OnRoute: LatLng: " + location.getLatitude() + " | "
                    +location.getLongitude());
                } else if (!Common.tripStatus.equals("OnRoute")){
                    Log.i("tripStatus","Common.tripStatus != OnRoute");
                }


                if (Common.mMap != null){
//                    Common.mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//                    Common.mMap.animateCamera(CameraUpdateFactory.zoomTo(18));
                }

                if (Common.gotAssignedCustomerPickupLocation){
                    checkWhetherOurDriverReachedToCustomerPickUpLocation();
                } else {
                    Log.i(TAG,"gotAssignedCustomerPickupLocation: false");
                }
                if (Common.reachedToAssignedCustomerPickUpLocation){
                    checkWhetherOurDriverReachedToCustomerDestinationLocation();
                } else {
                    Log.i(TAG,"reachedToAssignedCustomerPickUpLocation: false");
                }

            }
        };
        Common.locationSettingsRequestBuilderService.setAlwaysShow(true); //this is the key ingredient

        checkIsLocationIsEnabled(Common.locationSettingsRequestBuilderService,
                Common.googleApiClientService,Common.locationRequestService);

        Log.i(TAG,"GPS: Enabled");
    }

    private PolylineOptions polyLineOptionForCustomerPickUpPoint;
    private Polyline polyLineForCustomerPickUpPoint;
    private boolean polyLineOptionForCustomerPickUpPointInitialized = false;
    private void drawPolyLineFromDriverReqAcceptLocToCustomerPicUpLoc(Location location) {
        if (!polyLineOptionForCustomerPickUpPointInitialized){
            polyLineOptionForCustomerPickUpPoint = new PolylineOptions();
            polyLineOptionForCustomerPickUpPoint.color(Color.BLACK);
            polyLineOptionForCustomerPickUpPoint.width(15);
            polyLineOptionForCustomerPickUpPoint.startCap(new SquareCap());
            polyLineOptionForCustomerPickUpPoint.endCap(new SquareCap());
            polyLineOptionForCustomerPickUpPoint.jointType(JointType.ROUND);
            polyLineOptionForCustomerPickUpPointInitialized = true;
        }

        polyLineOptionForCustomerPickUpPoint.add(
                new LatLng(location.getLatitude(), location.getLongitude()));
        if (Common.mMap != null){
            polyLineForCustomerPickUpPoint = Common.mMap.
                    addPolyline(polyLineOptionForCustomerPickUpPoint);
        } else {
            initializeGoogleMap();
        }

    }

    private void savePickUpToDestinationLatLngIntoSQLiteDB(double latitude, double longitude) {
        DbHelper helper = new DbHelper(this);
        SQLiteDatabase db = helper.getWritableDatabase();
        helper.saveLatLngToSQLiteDatabase(latitude,longitude,db);
    }


    private void checkHowMuchDistanceTraveledByDeviceGPS(LatLng currentLatLng) {
        if (Common.previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS == null){
            Common.previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS = currentLatLng;
        } else {
            Double startLatitude = Common.previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS.latitude;
            Double startLongitude = Common.previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS.longitude;

            Double endLatitude = currentLatLng.latitude;
            Double endLongitude = currentLatLng.longitude;

            float results[] = new float[10];

            Location.distanceBetween(startLatitude, startLongitude,
                    endLatitude, endLongitude, results);
            Common.distanceGoneAfterTripStarted = Common.distanceGoneAfterTripStarted + results[0];
            Common.previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS = currentLatLng;
            Toast.makeText(this, "Distance: " + Common.distanceGoneAfterTripStarted + " meter",
                    Toast.LENGTH_SHORT).show();
            Log.i(TAG,"distanceGoneAfterTripStarted: " + Common.distanceGoneAfterTripStarted);
        }

    }

    public void checkIsLocationIsEnabled(LocationSettingsRequest.Builder builder,
                                         final GoogleApiClient googleApiClient,
                                         final LocationRequest locationRequest) {
        FirebaseCrash.log("Home:checkIsLocationIsEnabled.called");
        Crashlytics.log("Home:checkIsLocationIsEnabled.called");
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.
                checkLocationSettings(Common.googleApiClientService, builder.build());
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
                        createInAppNotification("GPS is turned off",
                                "Please switch on your GPS");
//                        try {
//                            status.startResolutionForResult(, CHECK_IS_LOCATION_ENABLED_IN_THIS_DEVICE_SERVICE);
//                        } catch (IntentSender.SendIntentException e) {
//                            e.printStackTrace();
//                        }

                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private void createInAppNotification(String title, String message) {
        //Go to Home Activity
        Intent intent = new Intent(this, Home.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                OPEN_HOME_PAGE_BY_PENDING_INTENT /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext(),
                "Default_Notification_Channel");
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setAutoCancel(true);
        builder.setChannelId("Default_Notification_Channel");
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(pendingIntent);

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
        notificationManager.notify(IN_APP_NOTIFICATION,builder.build());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkGPSConnectionPermission() {
        if (ActivityCompat.checkSelfPermission(getBaseContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getBaseContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions((Activity)getBaseContext(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        CHECK_GPS_PERMISSION_REQUEST);
            }
        }
    }



    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            // below is whatever you want to do
            if (isDoPeriodicWorkInSideAppHandlerShouldRun){
                Log.i(TAG,"GPS CHECKED");
                checkIsLocationIsEnabled(Common.locationSettingsRequestBuilderService,
                        Common.googleApiClientService, Common.locationRequestService);
                if (!isNetworkAvailable()){
                    Log.i(TAG,"No Internet");
                    createInAppNotification("No Internet",
                            "Please switch on your internet connection");
                } else {
                    Log.i(TAG,"There is internet");
                }
            }
            // scheduled another events to be in 10 seconds later
            doPeriodicWorkInSideApp.postDelayed(periodicUpdate,
                    doPeriodicWorkInSideAppTimerInSecond *  1000); //1000 = 1 second
        }
    };



    private void checkWhetherOurDriverReachedToCustomerPickUpLocation() {
        //Now we will get the distance between Our customer & Driver
        //Distance start here
        Location customerLoc = new Location("");
        customerLoc.setLatitude(Common.customerPickUpLocationLatLng.latitude);
        customerLoc.setLongitude(Common.customerPickUpLocationLatLng.longitude);
        Log.i("DriverMapsActivity","checkWhetherOurDriverReachedToCustomerPickUpLocation():\n "
                + "Customer Latitude: " + Common.customerPickUpLocationLatLng.latitude +
                "Customer Longitude: " + Common.customerPickUpLocationLatLng.longitude  );

        Location driverLoc = new Location("");
        if (Common.mLastLocation != null){
            driverLoc.setLatitude(Common.mLastLocation.getLatitude());
            driverLoc.setLongitude(Common.mLastLocation.getLongitude());
        }




        float distanceBetweenCustomerAndDriver = driverLoc.distanceTo(customerLoc);
        Log.i(TAG,"distanceBetweenCustomerAndDriver: " +
                distanceBetweenCustomerAndDriver);


        if (distanceBetweenCustomerAndDriver < 200 && !Common.reachedToAssignedCustomerPickUpLocation){
            //This means our driver reached within 200 meter of our customer location. Now we can
            //enabled the button
            //To pick up customer. As our driver reached to customer, So now he will advance to
            // destination. So
            Common.rideStatus = 2;
            Common.gotAssignedCustomerPickupLocation = false;
            Common.reachedToAssignedCustomerPickUpLocation = true;
            Log.i(TAG,"rideStatus: " + Common.rideStatus);
        }
    }

    private void checkWhetherOurDriverReachedToCustomerDestinationLocation() {
        //Now we will get the distance between Our Uthao car and Our Destination
        //Distance start here
        Location destinationLoc = new Location("");
        destinationLoc.setLatitude(Common.customerDestinationLatLng.latitude);
        destinationLoc.setLongitude(Common.customerDestinationLatLng.longitude);
        Log.i("DriverMapsActivity","checkWhetherOurDriverReachedToCustomerDestinationLocation():\n "
                + "Destination Latitude: " + Common.customerDestinationLatLng.latitude +
                "Destination Longitude: " + Common.customerDestinationLatLng.longitude  );

        Location driverLoc = new Location("");
        if (Common.mLastLocation != null){
            driverLoc.setLatitude(Common.mLastLocation.getLatitude());
            driverLoc.setLongitude(Common.mLastLocation.getLongitude());
        }



        float distanceBetweenUthaoAndDestination = driverLoc.distanceTo(destinationLoc);


        if (distanceBetweenUthaoAndDestination < 200 && !Common.reachedToAssignedCustomerDestinationLocation){
            //This means our driver reached within 200 meter of our customer location. Now we can enabled the button
            //To pick up customer. As our driver reached to customer, So now he will advance to destination. So
            Common.rideStatus = 3;
            //rideStatus == 3,That means our driver has dropped our customer
            //on his destination.
            //We will let our customer know His Ride_Status.
            DatabaseReference mCustomerCurrentRideInfoPnaelRef = FirebaseDatabase.getInstance()
                    .getReference("Users/Customers")
                    .child(Common.customerUID)
                    .child("Current_Ride_Info_Panel");
            HashMap map = new HashMap();
            map.put("Ride_Status",Common.rideStatus);
            mCustomerCurrentRideInfoPnaelRef.updateChildren(map);
            Common.reachedToAssignedCustomerPickUpLocation = false;
            Common.reachedToAssignedCustomerDestinationLocation = true;
        }
    }



    public static class NotificationCloseButtonHandler extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Common.driverStatus.equals("Online")){
                disConnectDriverFromDatabase(context);
                Glide.with(context)
                        .asBitmap()
                        .load(R.drawable.ic_driver_offline)
                        .into(Common.notificationTarget);
            } else if (Common.driverStatus.equals("Offline")){
                connectDriverToDataBase(context);
            } else if (Common.driverStatus.equals("OnTrip")){
                Toast.makeText(context, "Trip is in progress", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initializeGoogleMap(){
        if (Common.mMap == null){
            Home home = new Home();
            home.initializeMap();
        }
    }


}
