package com.sand_corporation.www.uthaopartner.NotificationBox.CustomerNotificationRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sand_corporation.www.uthaopartner.FirebaseMessaging.NotificationMsgActivity;
import com.sand_corporation.www.uthaopartner.R;

import java.util.ArrayList;

/**
 * Created by HP on 12/12/2017.
 */

public class NotificationRecyclerAdapter
        extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {
    //We will put all the single notification in the below array list
    private ArrayList<Notification> arrayList = new ArrayList<>();
    private Context context;

    //Below we are creating a constructor of NotificationRecyclerAdapter.class.
    // Every time, we create an object of this class, this below constructor will automatically
    //called. So every time notification will be saved in the ArrayList.
    public NotificationRecyclerAdapter(ArrayList<Notification> arrayList, Context context){
        this.arrayList = arrayList;
        this.context = context;
    }
    @Override
    public NotificationRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        //Below we will inflate "single_row_notification" layout.
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.single_row_notification,parent,false);
        NotificationRecyclerAdapter.MyViewHolder holder = new NotificationRecyclerAdapter.
                MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NotificationRecyclerAdapter.MyViewHolder holder, int position) {
        Notification notification = arrayList.get(position);
        holder.txtTitleNotificationSingleRow.setText(notification.getTitle());
        holder.txtTimeNotificationSingleRow.setText(notification.getDate());
        holder.txtMessageNotificationSingleRow.setText(notification.getMessage());
        //We could also write like below
        //holder.customerRideId.setText(arrayList.get(position).getRideId());
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView txtTitleNotificationSingleRow, txtTimeNotificationSingleRow,
                txtMessageNotificationSingleRow;


        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //We have initialized each history item's on click listener.
            txtTitleNotificationSingleRow = itemView.findViewById(R.id.txtTitleNotificationSingleRow);
            txtTimeNotificationSingleRow = itemView.findViewById(R.id.txtTimeNotificationSingleRow);
            txtMessageNotificationSingleRow = itemView.findViewById(R.id.txtMessageNotificationSingleRow);

        }

        @Override
        public void onClick(View view) {
            //After click we will move our user to new activity and there we will
            //show detail notification.
            Intent intent = new Intent(view.getContext(), NotificationMsgActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("date",txtTimeNotificationSingleRow.getText().toString());
//            intent.putExtras(bundle);  //be careful here will be putExtra's
            intent.putExtra("date",txtTimeNotificationSingleRow.getText().toString());
            intent.putExtra("title",txtTitleNotificationSingleRow.getText().toString());
            intent.putExtra("message",txtMessageNotificationSingleRow.getText().toString());
            view.getContext().startActivity(intent);  //Without context we will not be able to startActivity.

        }
    }
}
