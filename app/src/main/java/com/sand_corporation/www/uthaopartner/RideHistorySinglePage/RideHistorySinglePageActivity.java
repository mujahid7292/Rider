package com.sand_corporation.www.uthaopartner.RideHistorySinglePage;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.crashlytics.android.Crashlytics;
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
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.PolyUtil;
import com.sand_corporation.www.uthaopartner.Animation.TabAnimation;
import com.sand_corporation.www.uthaopartner.FareCalculator.FareCalculator;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptEditedTable.MoneyReceiptEdited;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;

public class RideHistorySinglePageActivity extends AppCompatActivity
        implements OnMapReadyCallback, View.OnTouchListener{

    private static final String TAG = "HistorySinglePage";
    private RideHistorySinglePageViewModel mViewModel;
    private String driverUid, customerUID, rideId, driverOrBiker, serviceType;
    private long totalRideTime;

    //Layout views
    private TextView txtDateRideHistorySingle, txtTotalPaymentRideHistorySingle,
            txtVehicleTypeRideHistorySingle, txtPaymentMethodRideHistorySingle,
            txtPickUpLocationRideHistorySingle, txtDestinationLocationRideHistorySingle,
            txtCustomerNameRideHistorySingle, receiptUnitRideHistorySingle,
            receiptFareRideHistorySingle, receiptTotalRideHistorySingle,
            receiptBaseFareRideHistorySingle, receiptBaseFareDistanceRideHistorySingle,
            receiptBaseFarePerCostingRideHistorySingle, receiptBaseFareTotalCostingRideHistorySingle,
            receiptTotalDistanceRideHistorySingle, txtTotalDistanceRideHistorySingle,
            receiptTotalDistancePerUnitCosting, receiptTotalDistanceTotalCosting,
            receiptTotalMinutes, receiptTotalMinutesQuantity, receiptTotalMinutesPerUnitCosting,
            receiptTotalMinutesTotalCosting, receiptTotalFareWithoutDiscount,
            receiptTotalDiscountText, receiptTotalDiscountAmountInThisRide, receiptTotalText,
            receiptTotalFareAfterDiscountAndDemandCharge, receiptTotalDemandCharge,
            receiptTotalDemandChargeInThisRide, txtRideStartTime, txtRideEndTime,
            txtCustomerPickUpTime, txtCustomerDropTime, txtCustomerPickUpDistance,
            txtCustomerDropDistance;
    private ImageView ic_back_sign, imgVehicleTypeRideHistorySingle,
            imgCustomerProfilePicRideHistorySingle;
    private RatingBar rtnCustomerRatingRideHistorySingle;
    private TabHost tabHost;
    private GoogleMap mMap;
    private String encodedPath, editedEncodedPath;
    private double Edited_Ride_Distance, EditedDistanceFair, EditedTotalPayment,
            EditedDiscountAmountInThisRide, EditedDemandCharge, EditedSubTotal,
            EditedDiscount, EditedDemandChargeInPercentage,discountInPercentage,
            discountAmountUptoTaka, demandChargeInPercentage;
    //Polyline
    private Polyline polyLineForRoadsApi;
    private PolylineOptions polyLineOptionForRoadsApi;
    private LatLngBounds bounds;
    private List<com.google.android.gms.maps.model.LatLng> tripRoadLatLngList, editedTripRoadLatLngList;

    @Override
    protected void onResume() {
        super.onResume();

        //Change TabHost title color and underline color
        TabHost tabhost = getTabHost();
        for(int i=0;i<tabhost.getTabWidget().getChildCount();i++)
        {
            //Change Title color
            TextView tv = (TextView) tabhost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
            //Change Title underline color
            View v = tabhost.getTabWidget().getChildAt(i);
            v.setBackgroundResource(R.drawable.tab_widget_bottom_indicator_by_xml);
        }
    }

    private TabHost getTabHost() {
        return tabHost = findViewById(R.id.tabHost);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_single_page);
        Common.rideIdForAdapter = getIntent().getExtras().getString("rideIdForAdapter");
        Log.i(TAG,"rideIdForAdapter: " + Common.rideIdForAdapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapHistorySinglePage);
        mapFragment.getMapAsync(this);
        initializeAllTheViews();
        setUpTabLayOut();

        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this).get(RideHistorySinglePageViewModel.class);
        putObserverInRideHistoryFromRoom();

    }




    private CustomScrollView tripDetailsUpperPartScrollView;
    private View mMapShelterView;
    private GestureDetector mGestureDetector;
    private ArrayList<LatLng> mLatLngForHandsFreeDrawing;
    private List<LatLng> editedPolyLineLatLngByHandsFreeDrawing, freshArrayList,
            temporaryParkingOfArraylist;
    private PolylineOptions mPolylineOptions = null;
    private Polyline mPolyLineForHandsFreeDrawing;
    // flag to differentiate whether user is touching to draw or not
    private boolean mDrawFinished = false;
    private boolean isBlockedScrollView = false;
    private boolean firstEditCompleted = false;
    private Button btnDrawOrClearPolyLine, btnJoinRoadInMiddle;
    private ImageView imgFinalizeTheRoute;
    private boolean selectRoadJoinStart = false;
    private LatLng SelectedRoadJoinStartLatLng;
    private int SelectedRoadJoinStartLatLngIndexNumber;
    private boolean selectRoadJoinEnd = false;
    private LatLng SelectedRoadJoinEndLatLng;
    private int SelectedRoadJoinEndLatLngIndexNumber;
    private boolean startRemovingLatLngFrompolyLineLatLngArrayList = false;
    private int previouslyNumberOfMarkerCreated;
    private SpotsDialog updateEditedPathSpotsDialog;
    private void initializeOnMapHandsFreeDrawing2() {
        freshArrayList = new ArrayList<>();
        editedPolyLineLatLngByHandsFreeDrawing = new ArrayList<>();
        mLatLngForHandsFreeDrawing = new ArrayList<>();
        temporaryParkingOfArraylist = new ArrayList<>();

        tripDetailsUpperPartScrollView = findViewById(R.id.tripDetailsUpperPartScrollView);
        mMapShelterView = (View) findViewById(R.id.drawer_view);
        mGestureDetector = new GestureDetector(this,
                new GestureListener());
        mMapShelterView.setOnTouchListener(this);
        mMapShelterView.setVisibility(View.GONE);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        btnDrawOrClearPolyLine = findViewById(R.id.btnDrawOrClearPolyLine);
        btnDrawOrClearPolyLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state = btnDrawOrClearPolyLine.getText().toString();
                switch (state){
                    case "Draw":
                        mDrawFinished = true;
                        mMapShelterView.setVisibility(View.VISIBLE);
                        mMap.getUiSettings().setZoomGesturesEnabled(false);
                        mMap.getUiSettings().setAllGesturesEnabled(false);
                        mMap.getUiSettings().setScrollGesturesEnabled(false);
                        btnDrawOrClearPolyLine.setText("Hold");
                        break;

                    case "Hold":
                        mDrawFinished = false;
                        mMapShelterView.setVisibility(View.GONE);
                        mMap.getUiSettings().setZoomGesturesEnabled(true);
                        mMap.getUiSettings().setAllGesturesEnabled(true);
                        mMap.getUiSettings().setScrollGesturesEnabled(true);
                        btnDrawOrClearPolyLine.setText("Draw");
                        break;
                }

            }
        });

        imgFinalizeTheRoute = findViewById(R.id.imgFinalizeTheRoute);
        imgFinalizeTheRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditedPathSpotsDialog = new SpotsDialog(RideHistorySinglePageActivity.this,
                        "Updating Edited Route");
                updateEditedPathSpotsDialog.show();
                String editedRoute = PolyUtil.encode(editedPolyLineLatLngByHandsFreeDrawing);
                double editedDistance  = getDistanceFromPolylinePath1(polylineOptionForEditedRoadPath);
                MoneyReceiptEdited moneyReceiptEdited = new MoneyReceiptEdited();
                moneyReceiptEdited.setRideId(Common.rideIdForAdapter);
                moneyReceiptEdited.setEditedEncodedPath(editedRoute);
                moneyReceiptEdited.setEditedRideDistance(editedDistance);
                moneyReceiptEdited.setDemandChargeInPercentage(demandChargeInPercentage);
                moneyReceiptEdited.setDiscountInPercentage(discountInPercentage);
                FareCalculator calculator = new FareCalculator();
                moneyReceiptEdited.setEditedDistanceFair(calculator
                        .getCalculatedDistanceFare(driverOrBiker,serviceType,editedDistance));
                moneyReceiptEdited.setEditedTotalPayment(calculator
                        .getCalculatedTotalFare(driverOrBiker,
                                serviceType,
                                editedDistance,
                                totalRideTime,
                                demandChargeInPercentage));
                mViewModel.addMoneyReceiptEdited(moneyReceiptEdited);
                updateServerOnEditedRoute(editedRoute, editedDistance);
                Log.i(TAG,"EditedRouteDistance: "  + editedDistance);
            }
        });

        btnJoinRoadInMiddle = findViewById(R.id.btnJoinRoadInMiddle);
        btnJoinRoadInMiddle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectRoadJoinStart && !selectRoadJoinEnd){
                    String state = btnJoinRoadInMiddle.getText().toString();
                    switch (state){
                        case "StartPoint":
                            imgFinalizeTheRoute.setVisibility(View.GONE);
                            mMap.clear();
                            if (!firstEditCompleted){
                                //This means first edit on the map
                                for (int i = 0; i < tripRoadLatLngList.size(); i++){
                                    LatLng startPosition;
                                    LatLng endPosition;
                                    if (i == tripRoadLatLngList.size() -1){
                                        startPosition = tripRoadLatLngList.get(i -1);
                                        endPosition = tripRoadLatLngList.get(i);
                                    }else {
                                        startPosition = tripRoadLatLngList.get(i);
                                        endPosition = tripRoadLatLngList.get(i + 1);
                                    }
                                    mMap.addMarker(new MarkerOptions()
                                            .position(tripRoadLatLngList.get(i))
                                            .anchor(0.5f,0.5f)
                                            .rotation(getBearing(startPosition, endPosition))
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.ic_tyre_marker)));
                                }
                                previouslyNumberOfMarkerCreated = tripRoadLatLngList.size();
                                Log.i(TAG,"previouslyNumberOfMarkerCreated: " + previouslyNumberOfMarkerCreated);
                            } else {
                                //This means editing for the second time
                                Log.i(TAG,"temporaryParkingOfArrayList: " + temporaryParkingOfArraylist.size());
                                temporaryParkingOfArraylist.clear();
                                temporaryParkingOfArraylist.addAll(editedPolyLineLatLngByHandsFreeDrawing);
                                editedPolyLineLatLngByHandsFreeDrawing.clear();
                                for (int i = 0; i < temporaryParkingOfArraylist.size(); i++){
                                    LatLng startPosition;
                                    LatLng endPosition;
                                    if (i == temporaryParkingOfArraylist.size() -1){
                                        startPosition = temporaryParkingOfArraylist.get(i -1);
                                        endPosition = temporaryParkingOfArraylist.get(i);
                                    }else {
                                        startPosition = temporaryParkingOfArraylist.get(i);
                                        endPosition = temporaryParkingOfArraylist.get(i + 1);
                                    }
                                    mMap.addMarker(new MarkerOptions()
                                            .position(temporaryParkingOfArraylist.get(i))
                                            .anchor(0.5f,0.5f)
                                            .rotation(getBearing(startPosition, endPosition))
                                            .icon(BitmapDescriptorFactory
                                                    .fromResource(R.drawable.ic_tyre_marker)));
                                }
                            }

                            Toast toast = Toast.makeText(RideHistorySinglePageActivity.this,
                                    "Select Start Point",Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 20);
                            toast.show();
                            selectRoadJoinStart = true;
                            btnJoinRoadInMiddle.setText("EndPoint");
                            break;

                        case "EndPoint":
                            if (!firstEditCompleted){
                                if (!selectRoadJoinStart){
                                    selectRoadJoinEnd = true;
                                    Toast toast1 =  Toast.makeText(RideHistorySinglePageActivity.this,
                                            "Select End Point",Toast.LENGTH_LONG);
                                    toast1.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 20);
                                    toast1.show();
                                    btnJoinRoadInMiddle.setText("Join");
                                } else {
                                    Toast toast1 = Toast.makeText(RideHistorySinglePageActivity.this,
                                            "First Select Start Point",Toast.LENGTH_LONG);
                                    toast1.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 20);
                                    toast1.show();
                                }
                            } else {
                                if (!selectRoadJoinStart){
                                    selectRoadJoinEnd = true;
                                    Toast toast1 =  Toast.makeText(RideHistorySinglePageActivity.this,
                                            "Select End Point",Toast.LENGTH_LONG);
                                    toast1.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 20);
                                    toast1.show();
                                    btnJoinRoadInMiddle.setText("Join");
                                } else {
                                    Toast toast1 = Toast.makeText(RideHistorySinglePageActivity.this,
                                            "First Select Start Point",Toast.LENGTH_LONG);
                                    toast1.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 20);
                                    toast1.show();
                                }

                            }

                            break;

                        case "Join":
                            if (!firstEditCompleted){
                                if (!selectRoadJoinStart && !selectRoadJoinEnd
                                        && mPolylineOptions != null){
                                    Log.i(TAG,"tripRoadLatLngList Size: " + tripRoadLatLngList.size());
                                    for (int i = 0; i < tripRoadLatLngList.size(); i++){
                                        if (i < SelectedRoadJoinStartLatLngIndexNumber +1
                                                || i > SelectedRoadJoinEndLatLngIndexNumber - 1){
                                            freshArrayList.add(tripRoadLatLngList.get(i));
                                        }
                                    }
                                    Log.i(TAG,"freshArrayList Size: " + freshArrayList.size());

                                    //Now add Sorted Arraylist marker

                                    //Now add freshArrayList
                                    for (int i = 0; i<freshArrayList.size();i++){
                                        if (i == SelectedRoadJoinStartLatLngIndexNumber +1){
                                            editedPolyLineLatLngByHandsFreeDrawing.addAll(mLatLngForHandsFreeDrawing);
                                        }else {
                                            editedPolyLineLatLngByHandsFreeDrawing.add(freshArrayList.get(i));
                                        }

                                    }
                                    Log.i(TAG,"editedPolyLineLatLngByHandsFreeDrawing Size: " +
                                            editedPolyLineLatLngByHandsFreeDrawing.size());
                                    mMap.clear();

                                    createHandsFreeEditedPolyLineForThisRide();
                                    btnJoinRoadInMiddle.setText("StartPoint");
                                    firstEditCompleted = true;

                                } else {
                                    Toast toast2 =  Toast.makeText(RideHistorySinglePageActivity.this,
                                            "Please Draw The Edited Road",Toast.LENGTH_LONG);
                                    toast2.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,
                                            0, 20);
                                    toast2.show();
                                }
                            } else {
                                if (!selectRoadJoinStart && !selectRoadJoinEnd
                                        && mPolylineOptions != null){
                                    Log.i(TAG,"temporaryParkingOfArraylist Size: " + temporaryParkingOfArraylist.size());
                                    for (int i = 0; i < temporaryParkingOfArraylist.size(); i++){
                                        if (i < SelectedRoadJoinStartLatLngIndexNumber +1
                                                || i > SelectedRoadJoinEndLatLngIndexNumber - 1){
                                            freshArrayList.add(temporaryParkingOfArraylist.get(i));
                                        }
                                    }
                                    Log.i(TAG,"freshArrayList Size: " + freshArrayList.size());

                                    //Now add Sorted Arraylist marker

                                    //Now add freshArrayList
                                    for (int i = 0; i<freshArrayList.size();i++){
                                        if (i == SelectedRoadJoinStartLatLngIndexNumber +1){
                                            editedPolyLineLatLngByHandsFreeDrawing.addAll(mLatLngForHandsFreeDrawing);
                                        }else {
                                            editedPolyLineLatLngByHandsFreeDrawing.add(freshArrayList.get(i));
                                        }

                                    }
                                    Log.i(TAG,"editedPolyLineLatLngByHandsFreeDrawing Size: " +
                                            editedPolyLineLatLngByHandsFreeDrawing.size());
                                    mMap.clear();

                                    createHandsFreeEditedPolyLineForThisRide();
                                    imgFinalizeTheRoute.setVisibility(View.VISIBLE);
                                    btnJoinRoadInMiddle.setText("StartPoint");

                                } else {
                                    Toast toast2 =  Toast.makeText(RideHistorySinglePageActivity.this,
                                            "Please Draw The Edited Road",Toast.LENGTH_LONG);
                                    toast2.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,
                                            0, 20);
                                    toast2.show();
                                }
                            }

                            break;


                    }


                }
            }
        });



        mMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                int strokeColor = polyline.getColor() ^ 0x0000CC00;
                polyline.setColor(strokeColor);
//                Log.i(TAG, "Polyline points @ " + polyline.getPoints());
//                polyLineLatLngArrayList = polyline.getPoints();
//                for (int i = 0; i < polyLineLatLngArrayList.size(); i++){
//                    if (polyLineLatLngArrayList.get(i) == polyLineLatLngArrayList.get(0)){
//                        Log.i(TAG, "First Point LatLng: " +polyLineLatLngArrayList.get(i));
//                    }
//                    mMap.addMarker(new MarkerOptions()
//                    .position(polyLineLatLngArrayList.get(i))
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.pick_up_marker)));
//                }
//                polyline.remove();
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                boolean didUserTouchedPolyLine = PolyUtil.isLocationOnPath(latLng, polyLineForRoadsApi.getPoints(),
                        true, 10);
//                Log.i(TAG,"didUserTouchedPolyLine: " + didUserTouchedPolyLine + "\n" +
//                "Touched LatLng: " + latLng);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng clickedMarkerLatLng = marker.getPosition();
                String markerId = marker.getId();
                markerId = markerId.replace("m","");
                int markerIdInteger = Integer.parseInt(markerId);
                Log.i(TAG,"clickedMarkerLatLng: " + clickedMarkerLatLng + "\n" +
                        "Marker ID: " + markerId + " | " +markerIdInteger);

                if (!firstEditCompleted){
                    if (selectRoadJoinStart){
                        SelectedRoadJoinStartLatLng = tripRoadLatLngList.get(markerIdInteger);
                        SelectedRoadJoinStartLatLngIndexNumber = markerIdInteger;
                        selectRoadJoinStart = false;
                        mMap.addMarker(new MarkerOptions()
                                .position(SelectedRoadJoinStartLatLng)
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.ic_road_edit_start_position)));
                        previouslyNumberOfMarkerCreated = previouslyNumberOfMarkerCreated + 1;
//                        marker.remove();
                        Log.i(TAG,"SelectedRoadJoinStartLatLng: " + SelectedRoadJoinStartLatLng + "\n"
                                +"SelectedRoadJoinStartLatLngIndexNumber: " + SelectedRoadJoinStartLatLngIndexNumber);
                        Toast toast = Toast.makeText(RideHistorySinglePageActivity.this,
                                "Start Point: Selected",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 10);
                        toast.show();


                    } else if (selectRoadJoinEnd){

                        SelectedRoadJoinEndLatLng = tripRoadLatLngList.get(markerIdInteger);
                        SelectedRoadJoinEndLatLngIndexNumber = markerIdInteger;
                        selectRoadJoinEnd = false;
                        mMap.addMarker(new MarkerOptions()
                                .position(SelectedRoadJoinEndLatLng)
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.ic_road_edit_end_position)));
                        previouslyNumberOfMarkerCreated = previouslyNumberOfMarkerCreated + 1;
//                        marker.remove();
                        Toast toast = Toast.makeText(RideHistorySinglePageActivity.this,
                                "End Point: Selected",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 10);
                        toast.show();
                        Log.i(TAG,"SelectedRoadJoinEndLatLng: " + SelectedRoadJoinEndLatLng + "\n"
                                +"SelectedRoadJoinEndLatLngIndexNumber: " + SelectedRoadJoinEndLatLngIndexNumber);
                    }
                } else {
                    if (selectRoadJoinStart){
                        Log.i(TAG,"previouslyNumberOfMarkerCreated: " + previouslyNumberOfMarkerCreated);

                        int previousMarkerIdInteger = markerIdInteger;
                        markerIdInteger = markerIdInteger - previouslyNumberOfMarkerCreated + 1;
                        Log.i(TAG,"Start markerIdInteger = markerIdInteger - previouslyNumberOfMarkerCreated + 1\n"
                                +markerIdInteger +" = " + previousMarkerIdInteger + " - "
                                + (previouslyNumberOfMarkerCreated + 1) + " + 1");
                        SelectedRoadJoinStartLatLng = temporaryParkingOfArraylist.get(markerIdInteger);
                        SelectedRoadJoinStartLatLngIndexNumber = markerIdInteger;
                        selectRoadJoinStart = false;
                        mMap.addMarker(new MarkerOptions()
                                .position(SelectedRoadJoinStartLatLng)
                                .icon(BitmapDescriptorFactory.
                                        fromResource(R.drawable.ic_road_edit_start_position)));
//                        marker.remove();
                        Log.i(TAG,"SelectedRoadJoinStartLatLng: " + SelectedRoadJoinStartLatLng + "\n"
                                +"SelectedRoadJoinStartLatLngIndexNumber: " + SelectedRoadJoinStartLatLngIndexNumber);
                        Toast toast = Toast.makeText(RideHistorySinglePageActivity.this,
                                "Start Point: Selected",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 10);
                        toast.show();


                    } else if (selectRoadJoinEnd){
                        int previousMarkerIdInteger = markerIdInteger;
                        Log.i(TAG,"previouslyNumberOfMarkerCreated: " + previouslyNumberOfMarkerCreated);
                        markerIdInteger = markerIdInteger - previouslyNumberOfMarkerCreated + 1;
                        Log.i(TAG,"End markerIdInteger = markerIdInteger - previouslyNumberOfMarkerCreated + 1\n"
                                +markerIdInteger +" = " + previousMarkerIdInteger + " - "
                                + (previouslyNumberOfMarkerCreated + 1) + " + 1");
                        SelectedRoadJoinEndLatLng = temporaryParkingOfArraylist.get(markerIdInteger);
                        SelectedRoadJoinEndLatLngIndexNumber = markerIdInteger;
                        selectRoadJoinEnd = false;
                        mMap.addMarker(new MarkerOptions()
                                .position(SelectedRoadJoinEndLatLng)
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.ic_road_edit_end_position)));
//                        marker.remove();
                        Toast toast = Toast.makeText(RideHistorySinglePageActivity.this,
                                "End Point: Selected",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 10);
                        toast.show();
                        Log.i(TAG,"SelectedRoadJoinEndLatLng: " + SelectedRoadJoinEndLatLng + "\n"
                                +"SelectedRoadJoinEndLatLngIndexNumber: " + SelectedRoadJoinEndLatLngIndexNumber);
                        int placeHolder = previouslyNumberOfMarkerCreated;
                        previouslyNumberOfMarkerCreated = previouslyNumberOfMarkerCreated +
                                temporaryParkingOfArraylist.size() + 2;
                        Log.i(TAG,"previouslyNumberOfMarkerCreated = previouslyNumberOfMarkerCreated +" +
                                "                                temporaryParkingOfArraylist.size() + 2\n" +
                                "" + previouslyNumberOfMarkerCreated + " = " +  + placeHolder + " + " +
                                (temporaryParkingOfArraylist.size() + 2));
                    }
                }


                return false;
            }
        });

    }

    private void updateServerOnEditedRoute(String editedRoute, double editedDistance) {
        HashMap<String, Object> map = new HashMap();
        map.put("Edited_Encoded_Path",editedRoute);
        map.put("Edited_Ride_Distance",editedDistance);

        DatabaseReference mCustomerRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users/Customers")
                .child(customerUID)
                .child("Customer_Ride_History")
                .child(driverOrBiker.equals("Driver")? "Car_Ride_History" : "Bike_Ride_History")
                .child(rideId);
        mCustomerRideHistory.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG,"Edited Route Update For Customer: Successful");
            }
        });

        DatabaseReference mDriverRideHistory = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(driverOrBiker + "s")
                .child(driverUid)
                .child(driverOrBiker + "_Driving_History")
                .child(rideId);
        mDriverRideHistory.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG,"Edited Route Update For Driver: Successful");
                updateEditedPathSpotsDialog.dismiss();
            }
        });



    }

    private Polyline polylineForEditedRoadPath;
    private PolylineOptions polylineOptionForEditedRoadPath = null;
    private void createHandsFreeEditedPolyLineForThisRide() {
        polylineOptionForEditedRoadPath = new PolylineOptions();
        polylineOptionForEditedRoadPath.color(Color.BLACK);
        polylineOptionForEditedRoadPath.width(15);
        polylineOptionForEditedRoadPath.startCap(new SquareCap());
        polylineOptionForEditedRoadPath.endCap(new SquareCap());
        polylineOptionForEditedRoadPath.jointType(JointType.ROUND);
        polylineOptionForEditedRoadPath.addAll(editedPolyLineLatLngByHandsFreeDrawing);
        polylineForEditedRoadPath = mMap.addPolyline(polylineOptionForEditedRoadPath);
        freshArrayList.clear();
        mLatLngForHandsFreeDrawing.clear();
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



    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        int X1 = (int) event.getX();
        int Y1 = (int) event.getY();
        Point point = new Point();
        point.x = X1;
        point.y = Y1;
        LatLng firstGeoPoint = mMap.getProjection().fromScreenLocation(
                point);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                if (mDrawFinished) {
                    X1 = (int) event.getX();
                    Y1 = (int) event.getY();
                    point = new Point();
                    point.x = X1;
                    point.y = Y1;
                    LatLng geoPoint = mMap.getProjection()
                            .fromScreenLocation(point);
                    mLatLngForHandsFreeDrawing.add(geoPoint);
                    mPolylineOptions = new PolylineOptions();
                    mPolylineOptions.color(Color.RED);
                    mPolylineOptions.width(15);
                    mPolylineOptions.addAll(mLatLngForHandsFreeDrawing);
                    mPolyLineForHandsFreeDrawing = mMap.addPolyline(mPolylineOptions);
                    mPolyLineForHandsFreeDrawing.setClickable(true);
                    mPolylines.add(mPolyLineForHandsFreeDrawing);
                }
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "Poinnts array size " + mLatLngForHandsFreeDrawing.size());
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }
    
    private void putObserverInRideHistoryFromRoom() {
        mViewModel.moneyReceiptsForSinglePage.observe(this, new Observer<MoneyReceipt>() {
            @Override
            public void onChanged(@Nullable MoneyReceipt moneyReceipt) {
                updateUI(moneyReceipt);
            }
        });

        mViewModel.moneyReceiptsEditedForSinglePage.observe(this, new Observer<MoneyReceiptEdited>() {
            @Override
            public void onChanged(@Nullable MoneyReceiptEdited moneyReceiptEdited) {
                if (moneyReceiptEdited != null){
                    EditedDiscount = moneyReceiptEdited.getDiscountInPercentage();
                    EditedDemandChargeInPercentage = moneyReceiptEdited.getDiscountInPercentage();
                    Edited_Ride_Distance = moneyReceiptEdited.getEditedRideDistance();
                    EditedDistanceFair = moneyReceiptEdited.getEditedDistanceFair();
                    EditedTotalPayment = moneyReceiptEdited.getEditedTotalPayment();
                    EditedDiscountAmountInThisRide = EditedTotalPayment * EditedDiscount;
                    EditedDemandCharge = (EditedTotalPayment - EditedDiscountAmountInThisRide) * EditedDemandChargeInPercentage;
                    EditedSubTotal = EditedTotalPayment - EditedDiscountAmountInThisRide + EditedDemandCharge;
                    editedEncodedPath = moneyReceiptEdited.getEditedEncodedPath();
                    if (editedEncodedPath == null){
                        Log.i(TAG,"editedEncodedPath == null");
                    } else {
                        editedTripRoadLatLngList = PolyUtil.decode(editedEncodedPath);
                    }

                    if (editedTripRoadLatLngList.size()!= 0){
                        txtTotalDistanceRideHistorySingle.setText(String.format("%s k.m",
                                new DecimalFormat("##.##").format(Edited_Ride_Distance)));
                        receiptTotalDistanceTotalCosting.setText(String.format("%s Taka",
                                new DecimalFormat("##.##").format(EditedDistanceFair)));
                        receiptTotalFareWithoutDiscount.setText(String.format("%s Taka",
                                new DecimalFormat("##.##").format(EditedTotalPayment)));
                        receiptTotalDiscountAmountInThisRide.setText(String.format("%s Taka",
                                new DecimalFormat("##.##").format(EditedDiscountAmountInThisRide)));
                        receiptTotalDemandChargeInThisRide.setText(String.format("%s Taka",
                                new DecimalFormat("##.##").format(EditedDemandCharge)));
                        receiptTotalFareAfterDiscountAndDemandCharge.setText(String.format("%s Taka",
                                new DecimalFormat("##.##").format(EditedSubTotal)));
                        txtTotalPaymentRideHistorySingle.setText(String.format("%s Taka",
                                new DecimalFormat("##.##").format(EditedSubTotal)));
                    }
                }
            }
        });
        mViewModel.mBasicInfo.observe(this, new Observer<BasicInfo>() {
            @Override
            public void onChanged(@Nullable BasicInfo basicInfo) {
                if (basicInfo != null){
                    driverUid = basicInfo.getDriverUid();
                    driverOrBiker = basicInfo.getDriverOrBiker();
                    String manufacturer = basicInfo.getVehicle_manufacturer();
                    String model = basicInfo.getVehicle_model();
                    String production_year = basicInfo.getVehicle_production_year();
                    String Vehicle_Model = manufacturer + " " + model + " " +
                            "(" + production_year + ")";
                    txtVehicleTypeRideHistorySingle.setText(Vehicle_Model);
                }
            }
        });
    }

    private void initializeAllTheViews() {
        //TextView
        txtDateRideHistorySingle = findViewById(R.id.txtDateRideHistorySingle);
        txtTotalPaymentRideHistorySingle = findViewById(R.id.txtTotalPaymentRideHistorySingle);
        txtVehicleTypeRideHistorySingle = findViewById(R.id.txtVehicleTypeRideHistorySingle);
        txtPaymentMethodRideHistorySingle = findViewById(R.id.txtPaymentMethodRideHistorySingle);
        txtPickUpLocationRideHistorySingle = findViewById(R.id.txtPickUpLocationRideHistorySingle);
        txtDestinationLocationRideHistorySingle = findViewById(R.id.txtDestinationLocationRideHistorySingle);
        txtCustomerNameRideHistorySingle = findViewById(R.id.txtCustomerNameRideHistorySingle);
        txtRideStartTime = findViewById(R.id.txtRideStartTime);
        txtRideEndTime = findViewById(R.id.txtRideEndTime);
        txtCustomerPickUpTime = findViewById(R.id.txtCustomerPickUpTime);
        txtCustomerDropTime = findViewById(R.id.txtCustomerDropTime);
        txtCustomerPickUpDistance = findViewById(R.id.txtCustomerPickUpDistance);
        txtCustomerDropDistance = findViewById(R.id.txtCustomerDropDistance);

        //Below those font will be changed
        Typeface tf;
        try {
            tf = Typeface.createFromAsset(getAssets(),
                    "fonts/Amandella.ttf");
        } catch (RuntimeException e){
            e.printStackTrace();
            Log.i(TAG,"RuntimeException: " + e.getMessage());
            tf = Typeface.DEFAULT;
        }

        receiptUnitRideHistorySingle = findViewById(R.id.receiptUnitRideHistorySingle);
        receiptUnitRideHistorySingle.setTypeface(tf);
        receiptFareRideHistorySingle = findViewById(R.id.receiptFareRideHistorySingle);
        receiptFareRideHistorySingle.setTypeface(tf);
        receiptTotalRideHistorySingle = findViewById(R.id.receiptTotalRideHistorySingle);
        receiptTotalRideHistorySingle.setTypeface(tf);
        receiptBaseFareRideHistorySingle = findViewById(R.id.receiptBaseFareRideHistorySingle);
        receiptBaseFareRideHistorySingle.setTypeface(tf);
        receiptBaseFareDistanceRideHistorySingle = findViewById(R.id.receiptBaseFareDistanceRideHistorySingle);
        receiptBaseFareDistanceRideHistorySingle.setTypeface(tf);
        receiptBaseFarePerCostingRideHistorySingle = findViewById(R.id.receiptBaseFarePerCostingRideHistorySingle);
        receiptBaseFarePerCostingRideHistorySingle.setTypeface(tf);
        receiptBaseFareTotalCostingRideHistorySingle = findViewById(R.id.receiptBaseFareTotalCostingRideHistorySingle);
        receiptBaseFareTotalCostingRideHistorySingle.setTypeface(tf);
        receiptTotalDistanceRideHistorySingle = findViewById(R.id.receiptTotalDistanceRideHistorySingle);
        receiptTotalDistanceRideHistorySingle.setTypeface(tf);
        txtTotalDistanceRideHistorySingle = findViewById(R.id.txtTotalDistanceRideHistorySingle);
        txtTotalDistanceRideHistorySingle.setTypeface(tf);
        receiptTotalDistancePerUnitCosting = findViewById(R.id.receiptTotalDistancePerUnitCosting);
        receiptTotalDistancePerUnitCosting.setTypeface(tf);
        receiptTotalDistanceTotalCosting = findViewById(R.id.receiptTotalDistanceTotalCosting);
        receiptTotalDistanceTotalCosting.setTypeface(tf);
        receiptTotalMinutes = findViewById(R.id.receiptTotalMinutes);
        receiptTotalMinutes.setTypeface(tf);
        receiptTotalMinutesQuantity = findViewById(R.id.receiptTotalMinutesQuantity);
        receiptTotalMinutesQuantity.setTypeface(tf);
        receiptTotalMinutesPerUnitCosting = findViewById(R.id.receiptTotalMinutesPerUnitCosting);
        receiptTotalMinutesPerUnitCosting.setTypeface(tf);
        receiptTotalMinutesTotalCosting = findViewById(R.id.receiptTotalMinutesTotalCosting);
        receiptTotalMinutesTotalCosting.setTypeface(tf);
        receiptTotalFareWithoutDiscount = findViewById(R.id.receiptTotalFareWithoutDiscount);
        receiptTotalFareWithoutDiscount.setTypeface(tf);
        receiptTotalDiscountText = findViewById(R.id.receiptTotalDiscountText);
        receiptTotalDiscountText.setTypeface(tf);
        receiptTotalDiscountAmountInThisRide = findViewById(R.id.receiptTotalDiscountAmountInThisRide);
        receiptTotalDiscountAmountInThisRide.setTypeface(tf);
        receiptTotalText = findViewById(R.id.receiptTotalText);
        receiptTotalText.setTypeface(tf);
        receiptTotalFareAfterDiscountAndDemandCharge = findViewById(R.id.receiptTotalFareAfterDiscountAndDemandCharge);
        receiptTotalFareAfterDiscountAndDemandCharge.setTypeface(tf);
        receiptTotalDemandCharge = findViewById(R.id.receiptTotalDemandCharge);
        receiptTotalDemandCharge.setTypeface(tf);
        receiptTotalDemandChargeInThisRide = findViewById(R.id.receiptTotalDemandChargeInThisRide);
        receiptTotalDemandChargeInThisRide.setTypeface(tf);



        //ImageView
        ic_back_sign = findViewById(R.id.ic_back_sign);
        imgVehicleTypeRideHistorySingle = findViewById(R.id.imgVehicleTypeRideHistorySingle);
        imgCustomerProfilePicRideHistorySingle = findViewById
                (R.id.imgCustomerProfilePicRideHistorySingle);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Rating Bar
        rtnCustomerRatingRideHistorySingle = findViewById(R.id.rtnCustomerRatingRideHistorySingle);
        //For custom color only using layer drawable to fill the star colors
        LayerDrawable stars = (LayerDrawable) rtnCustomerRatingRideHistorySingle
                .getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#969696"),
                PorterDuff.Mode.SRC_ATOP); // for filled stars
        stars.getDrawable(1).setColorFilter(Color.parseColor("#969696"),
                PorterDuff.Mode.SRC_ATOP); // for half filled stars
        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.colorTransparentWhite),
                PorterDuff.Mode.SRC_ATOP); // for empty stars

        rtnCustomerRatingRideHistorySingle.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.i("Check","New Rating Value: " + v);
            }
        });
    }

    private void setUpTabLayOut() {
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();

        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec(getResources().getString(R.string.money_receipt));
        spec.setContent(R.id.tab1);
        spec.setIndicator(getResources().getString(R.string.money_receipt));
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec(getResources().getString(R.string.complain));
        spec.setContent(R.id.tab2);
        spec.setIndicator(getResources().getString(R.string.complain));
        tabHost.addTab(spec);

        //Event
        tabHost.setOnTabChangedListener(new TabAnimation(this,tabHost));
    }


    private void updateUI(MoneyReceipt moneyReceipt){
        if (moneyReceipt != null){
            rideId = moneyReceipt.getRideId();
            serviceType = moneyReceipt.getServiceType();
            double TotalRideDistance = moneyReceipt.getCustomerDropDistance();
            double customerPickUpDistance = moneyReceipt.getCustomerPickUpDistance();
            double DistanceFair = moneyReceipt.getDistanceFair();
            double WaitingTimeFair = moneyReceipt.getWaitingTimeFair();
            long RideStartTime = moneyReceipt.getRideStartTime();
            long RideEndTime = moneyReceipt.getRideEndTime();
            long RideAcceptTime = moneyReceipt.getRideAcceptTime();
            totalRideTime = getDifferenceBetweenTwoTime1(RideEndTime,RideStartTime);
            double TotalPayment = moneyReceipt.getTotalPayment();
            discountInPercentage = moneyReceipt.getDiscountInPercentage();
            discountAmountUptoTaka = moneyReceipt.getDiscountAmountUptoTaka();
            demandChargeInPercentage = moneyReceipt.getDemandChargeInPercentage();
            double totalDemandCharge = TotalPayment  * demandChargeInPercentage;
            double totalDiscountAmountInThisRide = (TotalPayment + totalDemandCharge)
                    * discountInPercentage;
            if (totalDiscountAmountInThisRide > discountAmountUptoTaka){
                totalDiscountAmountInThisRide = discountAmountUptoTaka;
            }
            double subTotal = TotalPayment + totalDemandCharge - totalDiscountAmountInThisRide ;
            long customerPickUpTime = getDifferenceBetweenTwoTime1(RideStartTime, RideAcceptTime);




            int DriverRatingByCustomer = moneyReceipt.getDriverRatingByCustomer();
            customerUID = moneyReceipt.getCustomerUid();
            String customerName = moneyReceipt.getCustomerName();
            String customerProfilePic = moneyReceipt.getCustomerProfilePic();
            String DestinationLocationAddress = moneyReceipt.getDestinationLocationAddress();
            String PickUpLocationAddress = moneyReceipt.getPickUpLocationAddress();
            String PaymentMethod = moneyReceipt.getPaymentMethod();
            encodedPath = moneyReceipt.getEncodedPath();
            extractPolyLine(encodedPath);

            if (encodedPath == null){
                Log.i(TAG,"encodedPath == null");
            }
            txtTotalDistanceRideHistorySingle.setText(String.format("%s k.m",
                    new DecimalFormat("##.##").format(TotalRideDistance)));
            receiptTotalDistanceTotalCosting.setText(String.format("%s Taka",
                    new DecimalFormat("##.##").format(DistanceFair)));
            receiptTotalFareWithoutDiscount.setText(String.format("%s Taka",
                    new DecimalFormat("##.##").format(TotalPayment)));
            receiptTotalDiscountAmountInThisRide.setText(String.format("%s Taka",
                    new DecimalFormat("##.##").format(totalDiscountAmountInThisRide)));
            receiptTotalDemandChargeInThisRide.setText(String.format("%s Taka",
                    new DecimalFormat("##.##").format(totalDemandCharge)));
            receiptTotalFareAfterDiscountAndDemandCharge.setText(String.format("%s Taka",
                    new DecimalFormat("##.##").format(subTotal)));
            txtTotalPaymentRideHistorySingle.setText(String.format("%s Taka",
                    new DecimalFormat("##.##").format(subTotal)));

            txtDestinationLocationRideHistorySingle.setText(DestinationLocationAddress);
            txtPickUpLocationRideHistorySingle.setText(PickUpLocationAddress);
            txtPaymentMethodRideHistorySingle.setText(PaymentMethod);
            rtnCustomerRatingRideHistorySingle.setRating(DriverRatingByCustomer);
            txtDateRideHistorySingle.setText(getDate(RideEndTime));
            receiptTotalMinutesTotalCosting.setText(String.format("%s Taka", new DecimalFormat("##.##").format(WaitingTimeFair)));
            receiptTotalMinutesQuantity.setText(String.format("%s min",
                    new DecimalFormat("##.##").format(totalRideTime)));
            receiptTotalDiscountText.setText("(-)Discount ");
            receiptTotalDemandCharge.setText(String.format("(+)Surcharge (%s%%)", demandChargeInPercentage * 100));
            txtRideStartTime.setText(getTime(RideStartTime));
            txtRideEndTime.setText(getTime(RideEndTime));
            txtCustomerNameRideHistorySingle.setText(customerName);
            txtCustomerPickUpTime.setText(String.format("%s min",
                    new DecimalFormat("##.##").format(customerPickUpTime)));
            txtCustomerDropTime.setText(String.format("%s min",
                    new DecimalFormat("##.##").format(totalRideTime)));
            txtCustomerPickUpDistance.setText(String.format("%s k.m",
                    new DecimalFormat("##.##").format(customerPickUpDistance)));
            txtCustomerDropDistance.setText(String.format("%s k.m",
                    new DecimalFormat("##.##").format(TotalRideDistance)));
            Glide.with(this).load(customerProfilePic).into(imgCustomerProfilePicRideHistorySingle);
            setUpBaseFair();
        }

    }

    private void setUpBaseFair() {
        if (Common.driverOrBiker.equals("Driver")){
            if (Common.serviceType.equals("Premium")){
                Double premiumCarBaseFair = 80.00;
                Double premiumCarPerKmFair = 19.00;
                Double premiumCarWaitingTimePerMin = 5.00;

                receiptBaseFarePerCostingRideHistorySingle.
                        setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(premiumCarBaseFair)));
                receiptTotalDistancePerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(premiumCarPerKmFair)));
                receiptTotalMinutesPerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(premiumCarWaitingTimePerMin)));

            } else if(Common.serviceType.equals("Economy")){

                Double economyCarBaseFair = 40.00;
                Double economyCarPerKmFair = 17.00;
                Double economyCarWaitingTimePerMin = 4.00;

                receiptBaseFarePerCostingRideHistorySingle.
                        setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(economyCarBaseFair)));
                receiptTotalDistancePerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(economyCarPerKmFair)));
                receiptTotalMinutesPerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(economyCarWaitingTimePerMin)));
            }
        } else if (Common.driverOrBiker.equals("Biker")){
            if (Common.serviceType.equals("Premium")){

                Double premiumBikeBaseFair = 30.00;
                Double premiumBikePerKmFair = 9.00;
                Double premiumBikeWaitingTimePerMin = 2.50;

                receiptBaseFarePerCostingRideHistorySingle.
                        setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(premiumBikeBaseFair)));
                receiptTotalDistancePerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(premiumBikePerKmFair)));
                receiptTotalMinutesPerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(premiumBikeWaitingTimePerMin)));

            } else if(Common.serviceType.equals("Economy")){

                Double economyBikeBaseFair = 25.00;
                Double economyBikePerKmFair = 8.00;
                Double economyBikeWaitingTimePerMin = 2.00;

                receiptBaseFarePerCostingRideHistorySingle.
                        setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(economyBikeBaseFair)));
                receiptTotalDistancePerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(economyBikePerKmFair)));
                receiptTotalMinutesPerUnitCosting
                        .setText(String.format("%s T.K",
                                new DecimalFormat("##.##")
                                        .format(economyBikeWaitingTimePerMin)));
            }
        }
    }

    private void extractPolyLine(String encodedPath){
        tripRoadLatLngList = PolyUtil.decode(encodedPath);

        //Adjusting bounds
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng: tripRoadLatLngList){
            builder.include(latLng);
        }
        bounds = builder.build();
        //Now we will make boundary for our camera
    }



    private void createPolyLine() {
        if (bounds != null && tripRoadLatLngList != null){
            CameraUpdate mCameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,50);
            mMap.animateCamera(mCameraUpdate);

            polyLineOptionForRoadsApi = new PolylineOptions();
            polyLineOptionForRoadsApi.color(Color.BLACK);
            polyLineOptionForRoadsApi.width(15);
            polyLineOptionForRoadsApi.startCap(new SquareCap());
            polyLineOptionForRoadsApi.endCap(new SquareCap());
            polyLineOptionForRoadsApi.jointType(JointType.ROUND);
            polyLineOptionForRoadsApi.addAll(tripRoadLatLngList);
            polyLineForRoadsApi = mMap.addPolyline(polyLineOptionForRoadsApi);
            polyLineForRoadsApi.setClickable(true);
            mPolylines.add(polyLineForRoadsApi);
        } else {
            Log.i(TAG,"bounds == " + bounds==null? "Null":bounds.toString());
            Log.i(TAG,"bounds == " + tripRoadLatLngList==null? "Null":tripRoadLatLngList.toString());
        }

    }

    private PolylineOptions polylineOptionsForEditedRoute;
    private Polyline polylineForEditedRoute;
    private void createPolyLineForEditedRoute() {
        if (editedTripRoadLatLngList != null){
            polylineOptionsForEditedRoute = new PolylineOptions();
            polylineOptionsForEditedRoute.color(Color.RED);
            polylineOptionsForEditedRoute.width(15);
            polylineOptionsForEditedRoute.startCap(new SquareCap());
            polylineOptionsForEditedRoute.endCap(new SquareCap());
            polylineOptionsForEditedRoute.jointType(JointType.ROUND);
            polylineOptionsForEditedRoute.addAll(editedTripRoadLatLngList);
            polylineForEditedRoute = mMap.addPolyline(polylineOptionsForEditedRoute);
            polylineForEditedRoute.setClickable(true);
            mPolylines.add(polylineForEditedRoute);
        }

    }

    private ArrayList<Polyline> mPolylines;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mPolylines = new ArrayList<Polyline>();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createPolyLine();
                createPolyLineForEditedRoute();
            }
        },2000);

        initializeOnMapHandsFreeDrawing2();

    }



    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
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

    private String getTime(Long ride_end_time) {
        FirebaseCrash.log("Home:getTime.called");
        Crashlytics.log("Home:getTime.called");
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time);
        String date = DateFormat.format("hh:mm a",calendar).toString();
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

    private float getBearing(LatLng startPosition, LatLng endPosition) {
        FirebaseCrash.log("Home:getBearing.called");
        Crashlytics.log("Home:getBearing.called");
        //Method for finding bearing between two points.
        //This method will move car right left on the road.
        double lat = Math.abs(startPosition.latitude - endPosition.latitude);
        double lng = Math.abs(startPosition.longitude - endPosition.longitude);

        if (startPosition.latitude < endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude < endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (startPosition.latitude >= endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (startPosition.latitude < endPosition.latitude && startPosition.longitude >= endPosition.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }
}
