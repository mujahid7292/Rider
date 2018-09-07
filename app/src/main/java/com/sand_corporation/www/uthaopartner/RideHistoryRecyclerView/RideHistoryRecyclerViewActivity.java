package com.sand_corporation.www.uthaopartner.RideHistoryRecyclerView;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.BasicInfoTable.BasicInfo;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.MoneyReceipt;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.SubsetOfMoneyReceiptColumn.MoneyReceiptRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RideHistoryRecyclerViewActivity extends AppCompatActivity {

    private static final String TAG = "CheckRideHistory";
    private RecyclerView rideHistoryRecyclerView;
    private ArrayList<MoneyReceiptRecyclerView> arrayList = new ArrayList<>();
    private RideHistoryRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView ic_back_sign;
    private RideHistoryRecyclerViewViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_history_recycler_view);

        rideHistoryRecyclerView = findViewById(R.id.rideHistoryRecyclerView);
        rideHistoryRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        rideHistoryRecyclerView.setHasFixedSize(true);

        //Initialize layoutManager
        layoutManager = new LinearLayoutManager(RideHistoryRecyclerViewActivity.this);
        //Now we will attach the layOutManager to Recycler View.
        rideHistoryRecyclerView.setLayoutManager(layoutManager);

        //Here we will put customer's dummy life time ride id to 'arrayList'

        //Now initialize the object from CustomerDummyRecyclerAdapter.class
        adapter = new RideHistoryRecyclerAdapter(arrayList,RideHistoryRecyclerViewActivity.this);
        rideHistoryRecyclerView.setAdapter(adapter);

        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // Get a reference to the ViewModel for this screen.
        mViewModel = ViewModelProviders.of(this).get(RideHistoryRecyclerViewViewModel.class);

        putObserverInRideHistoryFromRoom();
    }


    private void putObserverInRideHistoryFromRoom() {
        mViewModel.moneyReceiptsForRecyclerView.observe(this,
                new Observer<List<MoneyReceiptRecyclerView>>() {
            @Override
            public void onChanged(@Nullable List<MoneyReceiptRecyclerView> moneyReceiptRecyclerViews) {
                //At the beginning if there is previous data available in the adapter we need to clear those
                //data.
                if (moneyReceiptRecyclerViews != null){
                    Log.i(TAG, "moneyReceiptRecyclerViews size: " + moneyReceiptRecyclerViews.size());
                    if (moneyReceiptRecyclerViews.size() == 0){
                        checkWhetherWeHaveRideHistoryInServer();
                    }
                    arrayList.clear();
                    arrayList.addAll(moneyReceiptRecyclerViews);
                    adapter.notifyDataSetChanged();
                    Log.i(TAG, "arrayList size: " + arrayList.size());
                } else {
                    Log.i(TAG, "moneyReceiptRecyclerViews = null");
                }

            }
        });

        mViewModel.mBasicInfo.observe(this, new Observer<BasicInfo>() {
            @Override
            public void onChanged(@Nullable BasicInfo basicInfo) {
                if(basicInfo != null){
//                    Common.driverOrBiker = basicInfo.getDriverOrBiker();
                }
            }
        });

    }

    private void checkWhetherWeHaveRideHistoryInServer() {
        if (Common.driverUID == null){
            Common.driverUID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        DatabaseReference mDriverDrivingHistoryDB = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(Common.driverOrBiker + "s")
                .child(Common.driverUID)
                .child(Common.driverOrBiker + "_Driving_History");
        mDriverDrivingHistoryDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0){
                    Log.i(TAG,"Number of history in server: " + dataSnapshot.getChildrenCount()
                    +"\n dataSnapShot: " + dataSnapshot.toString());

                    for (DataSnapshot data: dataSnapshot.getChildren()){
                        MoneyReceipt moneyReceipt = new MoneyReceipt();
                        Map<String , Object> map = (Map<String, Object>) data.getValue();
                        if (map.get("Customer_Name")!= null){
                            String Customer_Name = map.get("Customer_Name").toString();
                            moneyReceipt.setCustomerName(Customer_Name);
                        }
                        if (map.get("CustomerUID")!= null){
                            String CustomerUID = map.get("CustomerUID").toString();
                            moneyReceipt.setCustomerUid(CustomerUID);
                        }
                        if (map.get("Customer_Profile_Pic")!= null){
                            String Customer_Profile_Pic = map.get("Customer_Profile_Pic").toString();
                            moneyReceipt.setCustomerProfilePic(Customer_Profile_Pic);
                        }
                        if (map.get("Customer_Rating_By_Driver")!= null){
                            String Customer_Rating_By_Driver = map.get("Customer_Rating_By_Driver").toString();
                            moneyReceipt.setCustomerRatingByDriver(Integer.parseInt(Customer_Rating_By_Driver));
                        }
                        if (map.get("Destination_Location_Address")!= null){
                            String Destination_Location_Address =map.get("Destination_Location_Address")
                                    .toString();
                            moneyReceipt.setDestinationLocationAddress(Destination_Location_Address);
                        }
                        if (map.get("Distance")!= null){
                            String Distance = map.get("Distance").toString();
                            moneyReceipt.setCustomerDropDistance(Double.parseDouble(Distance));
                        }
                        if (map.get("Distance_Fair")!= null){
                            String Distance_Fair = map.get("Distance_Fair").toString();
                            moneyReceipt.setDistanceFair(Double.parseDouble(Distance_Fair));
                        }
                        if (map.get("Driver_Rating_By_Customer")!= null){
                            String Driver_Rating_By_Customer = map.get("Driver_Rating_By_Customer").toString();
                            moneyReceipt.setDriverRatingByCustomer(Integer.parseInt(Driver_Rating_By_Customer));
                        }
                        if (map.get("Encoded_Path")!= null){
                            String Encoded_Path = map.get("Encoded_Path").toString();
                            moneyReceipt.setEncodedPath(Encoded_Path);
                        }
                        if (map.get("Payment_Method_Used")!= null){
                            String Payment_Method_Used = map.get("Payment_Method_Used").toString();
                            moneyReceipt.setPaymentMethod(Payment_Method_Used);
                        }
                        if (map.get("PickUp_Location_Address")!= null){
                            String PickUp_Location_Address = map.get("PickUp_Location_Address").toString();
                            moneyReceipt.setPickUpLocationAddress(PickUp_Location_Address);
                        }
                        if (map.get("Ride_End_Time")!= null){
                            long Ride_End_Time = (long) map.get("Ride_End_Time");
                            moneyReceipt.setRideEndTime(Ride_End_Time);
                        }
                        if (map.get("Ride_Start_Time")!= null){
                            long Ride_Start_Time = (long) map.get("Ride_Start_Time");
                            moneyReceipt.setRideStartTime(Ride_Start_Time);
                        }
                        if (map.get("Total_Demand_Charge")!= null){
                            double Total_Demand_Charge = (double) map.get("Total_Demand_Charge");
                            moneyReceipt.setDemandChargeInPercentage(Total_Demand_Charge);
                        }
                        if (map.get("Total_Discount")!= null){
                            double Total_Discount = (double) map.get("Total_Discount");
                            moneyReceipt.setDiscountInPercentage(Total_Discount);
                        }
                        if (map.get("Total_Payment")!= null){
                            String Total_Payment = map.get("Total_Payment").toString();
                            moneyReceipt.setTotalPayment(Double.parseDouble(Total_Payment));
                        }
                        if (map.get("Waiting_Time_Fair")!= null){
                            String Waiting_Time_Fair = map.get("Waiting_Time_Fair").toString();
                            moneyReceipt.setWaitingTimeFair(Double.parseDouble(Waiting_Time_Fair));
                        }
                        if (map.get("rideId")!= null){
                            String rideId = map.get("rideId").toString();
                            moneyReceipt.setRideId(rideId);
                        }
                        if (map.get("Service_Type")!= null){
                            String Service_Type = map.get("Service_Type").toString();
                            moneyReceipt.setServiceType(Service_Type);
                        }
                        mViewModel.addMoneyReceipt(moneyReceipt);

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
