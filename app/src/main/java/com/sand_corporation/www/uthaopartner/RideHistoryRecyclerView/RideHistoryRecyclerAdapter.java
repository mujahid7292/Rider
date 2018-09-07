package com.sand_corporation.www.uthaopartner.RideHistoryRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.RideHistorySinglePage.RideHistorySinglePageActivity;
import com.sand_corporation.www.uthaopartner.RoomDataBase.Table.MoneyReceiptTable.SubsetOfMoneyReceiptColumn.MoneyReceiptRecyclerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by HP on 2/16/2018.
 */

public class RideHistoryRecyclerAdapter extends
        RecyclerView.Adapter<RideHistoryRecyclerAdapter.MyViewHolder>{


    //The first thing we need is customer life time ride history. So as we created object of DummyHistory.
    //Than we should put all the history into ArrayList.
    private ArrayList<MoneyReceiptRecyclerView> arrayList = new ArrayList<>();
    //By above code we are putting customer's life time ride history into ArrayList.
    private Context context;

    //Below we are creating a constructor of CustomerDummyRecyclerAdapter.class. Every time, we create
    //an object of this class, this below constructor will automatically called. So every time
    //customers life time ride history will be saved in the ArrayList.
    public RideHistoryRecyclerAdapter(ArrayList<MoneyReceiptRecyclerView> arrayList,
                                      Context context){
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Below we will inflate "item_history" layout.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row_ride_history,parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MoneyReceiptRecyclerView moneyReceiptRecyclerView = arrayList.get(position);
        holder.rideIdForAdapter.setText(moneyReceiptRecyclerView.getRideId());
        holder.rideEndTimeForAdapter.setText(String.format("Date: %s", getDate(moneyReceiptRecyclerView.getRideEndTime())));
        holder.pickUpLocationAddressForAdapter.setText(moneyReceiptRecyclerView.getPickUpLocationAddress());
        holder.destinationLocationAddressForAdapter.setText(moneyReceiptRecyclerView.getDestinationLocationAddress());
        holder.totalPaymentForAdapter.setText(String.format("Cash: %s taka", new DecimalFormat("##.##").format(moneyReceiptRecyclerView.getTotalPayment())));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
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

    public static class MyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private TextView rideIdForAdapter, rideEndTimeForAdapter,
                pickUpLocationAddressForAdapter, destinationLocationAddressForAdapter,
                totalPaymentForAdapter;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //We have initialized each history item's on click listener.
            rideIdForAdapter = itemView.findViewById(R.id.rideIdForAdapter);
            rideEndTimeForAdapter = itemView.findViewById(R.id.rideEndTimeForAdapter);
            pickUpLocationAddressForAdapter = itemView.findViewById(R.id.pickUpLocationAddressForAdapter);
            destinationLocationAddressForAdapter = itemView.findViewById(R.id.destinationLocationAddressForAdapter);
            totalPaymentForAdapter = itemView.findViewById(R.id.totalPaymentForAdapter);

        }

        @Override
        public void onClick(View view) {
            //After click we will move our user to new activity and there we will
            //show detail history of the ride.
            Intent intent = new Intent(view.getContext(), RideHistorySinglePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("rideIdForAdapter", rideIdForAdapter.getText().toString());
            intent.putExtras(bundle);  //be careful here will be putExtra's
            view.getContext().startActivity(intent);  //Without context we will not be able to startActivity.
        }
    }
}
