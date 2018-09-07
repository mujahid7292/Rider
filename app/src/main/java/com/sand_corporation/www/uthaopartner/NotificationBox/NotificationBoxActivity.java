package com.sand_corporation.www.uthaopartner.NotificationBox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.sand_corporation.www.uthaopartner.NotificationBox.CustomerNotificationRecyclerView.Notification;
import com.sand_corporation.www.uthaopartner.NotificationBox.CustomerNotificationRecyclerView.NotificationRecyclerAdapter;
import com.sand_corporation.www.uthaopartner.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationBoxActivity extends AppCompatActivity {

    private RecyclerView notificationsRecyclerView;
    private ArrayList<Notification> arrayList = new ArrayList<>();
    private NotificationRecyclerAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView ic_back_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseCrash.log("NotificationBoxActivity:onCreate.called");
        Crashlytics.log("NotificationBoxActivity:onCreate.called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_box);

        notificationsRecyclerView = findViewById(R.id.notificationsRecyclerView);
        notificationsRecyclerView.setNestedScrollingEnabled(false);
        //This setNestedScrollingEnabled() method will keep scrolling smooth.
        notificationsRecyclerView.setHasFixedSize(true);

        //Initialize layoutManager
        layoutManager = new LinearLayoutManager(NotificationBoxActivity.this);
        //Now we will attach the layOutManager to Recycler View.
        notificationsRecyclerView.setLayoutManager(layoutManager);

        //Now initialize the object from NotificationRecyclerAdapter.class
        adapter = new NotificationRecyclerAdapter(arrayList, NotificationBoxActivity.this);
        notificationsRecyclerView.setAdapter(adapter);

        ic_back_sign = findViewById(R.id.ic_back_sign);
        ic_back_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //Now we will get the Notification from SQLite database
        getNotificationFromSQLite();
    }

    private void getNotificationFromSQLite() {
        FirebaseCrash.log("NotificationBoxActivity:getNotificationFromSQLite.called");
        Crashlytics.log("NotificationBoxActivity:getNotificationFromSQLite.called");
        //At the beginning if there is previous data available in the adapter we need to clear those
        //data.
        arrayList.clear();

    }

    private String getDate(Long ride_end_time) {
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",calendar).toString();
        return date;

    }
}
