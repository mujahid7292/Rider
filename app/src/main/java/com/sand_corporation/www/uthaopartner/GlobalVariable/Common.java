package com.sand_corporation.www.uthaopartner.GlobalVariable;

import android.location.Location;

import com.bumptech.glide.request.target.NotificationTarget;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.IFCMService;
import com.sand_corporation.www.uthaopartner.DeviceToDeviceNotification.RetrofitFCMClient;
import com.sand_corporation.www.uthaopartner.GetGoogleDirection.IGoogleAPI;
import com.sand_corporation.www.uthaopartner.GetGoogleDirection.RetrofitClient;
import com.sand_corporation.www.uthaopartner.GetGoogleRoads.IGoogleRoadAPI;
import com.sand_corporation.www.uthaopartner.GetGoogleRoads.RetrofitRoadClient;

/**
 * Created by HP on 1/30/2018.
 */

public class Common {
    public static String refreshedFCMToken;
    public static String driverOrBiker;
    public static String driverUID;
    public static String serviceType;
    public static String driverStatus = "Offline"; //Offline, Online, OnTrip
    public static String tripStatus = "N/A";  //Started, Ended, OnRoute
    public static int numberOfPolyLineToBeUploadedToServer = 0;
    //N/A = means driver did not have any trip
    //Started = means driver started the trip
    //Ended = means driver ended the trip
    public static String rideIdForAdapter;
    public static Location mLastLocation;
    public static GoogleMap mMap;
    public static SupportMapFragment mapFragment;
    public static GoogleApiClient googleApiClientService;
    public static LocationRequest locationRequestService;
    public static LocationListener locationListenerService;
    public static LocationSettingsRequest.Builder locationSettingsRequestBuilderService;
    public static String fcmURL = "https://fcm.googleapis.com/";
    public static final String baseUrl = "https://maps.googleapis.com";
    public static final String baseRoadUrl = "https://roads.googleapis.com/v1/";
    public static boolean isScreenOn = false;
    public static LatLng previousLatLngForCalculatingCurrentTripDistanceByDeviceGPS = null;
    public static float distanceGoneAfterTripStarted = 0;
    public static LatLng customerPickUpLocationLatLng;
    public static LatLng customerDestinationLatLng;
    public static int rideStatus = 0;
    //By above 'rideStatus' variable we will keep track of our driver ride status
    //Example: whether he picked up the customer, whether he dropped the customer.
    public static boolean gotAssignedCustomerPickupLocation = false;
    public static boolean reachedToAssignedCustomerPickUpLocation = false;
    public static boolean reachedToAssignedCustomerDestinationLocation = false;
    public static String customerUID;
    public static String assignedCustomerName;
    public static String assignedCustomerProfilePic;
    public static NotificationTarget notificationTarget;

    public static IFCMService getFCMService(){
        return RetrofitFCMClient.getClient(fcmURL).create(IFCMService.class);
    }

    public static IGoogleAPI getGoogleApi(){

        return RetrofitClient.getClient(baseUrl).create(IGoogleAPI.class);
    }

    public static IGoogleRoadAPI getGoogleRoadApi(){
        return RetrofitRoadClient.getClient(baseRoadUrl).create(IGoogleRoadAPI.class);
    }
}
