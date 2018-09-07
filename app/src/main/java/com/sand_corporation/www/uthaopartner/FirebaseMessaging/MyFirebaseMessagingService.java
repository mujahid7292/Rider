package com.sand_corporation.www.uthaopartner.FirebaseMessaging;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.sand_corporation.www.uthaopartner.GlobalVariable.Common;
import com.sand_corporation.www.uthaopartner.HomeActivity.Home;
import com.sand_corporation.www.uthaopartner.R;
import com.sand_corporation.www.uthaopartner.Volley.MySingelTon;

import java.util.Calendar;
import java.util.Locale;

import static android.support.v4.app.NotificationCompat.STREAM_DEFAULT;

/**
 * Created by HP on 1/30/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "CheckUOnMessageReceived";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        initNotificationChannels();


        String notification_type = remoteMessage.getData().get("type");
        String notification_title = remoteMessage.getData().get("title");
        String notification_subtext = remoteMessage.getData().get("subtext");
        String notification_message = remoteMessage.getData().get("message");
        String notification_img_url = remoteMessage.getData().get("img_url");
        String notification_webUrl = remoteMessage.getData().get("webUrl");

        Log.i(TAG,"notification_type: " +notification_type + "\n" +
                "notification_title: " +notification_title + "\n" +
                "notification_subtext: " +notification_subtext + "\n" +
                "notification_message: " +notification_message + "\n" +
                "notification_img_url: " +notification_img_url + "\n" +
                "notification_webUrl: " +notification_webUrl + "\n\n\n");

        if (remoteMessage.getNotification() != null){
            Log.i(TAG,"RemoteMessage body: "
                    + remoteMessage.getNotification().getBody() + "\n" +
                    "RemoteMessage click_action: " + remoteMessage.getNotification().getClickAction() + "\n" +
                    "RemoteMessage Title: " + remoteMessage.getNotification().getTitle() + "\n" +
                    "RemoteMessage getData.isEmpty: " + remoteMessage.getData().isEmpty());
        }



        if (notification_type != null){
            switch (notification_type){
                case NotificationType.NOTIFICATION_TYPE_CUSTOMER_CALL:
                    processCustomerRequest(remoteMessage);
                    break;

                case NotificationType.NOTIFICATION_TYPE_MESSAGE:
                    Log.i(TAG,"sendMessageNotification() called");
                    sendMessageNotification(remoteMessage);
                    break;

                case NotificationType.NOTIFICATION_TYPE_IMAGE_WITH_MESSAGE:
                    handleImageMessage(remoteMessage);
                    break;

                case NotificationType.NOTIFICATION_TYPE_WEB_URL:
                    sendWebUrlNotification(remoteMessage);
                    break;
            }

        }


//        processCustomerRequest(remoteMessage);
    }




    private void processCustomerRequest(RemoteMessage remoteMessage) {
        //As from customer app we will send customer Latlng using Firebase messaging service and
        //we will also send this Latlng in message body. So here we will receive customer
        //Latlng from notification body
        String notification_message = remoteMessage.getData().get("message");
        Common.customerUID = remoteMessage.getData().get("subtext");
        LatLng customer_location = new Gson().fromJson(notification_message,
                LatLng.class);
        Intent intent = new Intent(getBaseContext(), Home.class);
        Bundle bundle = new Bundle();
        bundle.putString("Type","CustomerCall");
        bundle.putDouble("lat",customer_location.latitude);
        bundle.putDouble("lng",customer_location.longitude);
        Log.i("CheckHome","OnMessageReceived: lat: " + customer_location.latitude + "\n" +
        "OnMessageReceived: lng: " + customer_location.longitude);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void sendMessageNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            Long date = getCurrentTimeStamp();
            Log.i("Check","title: " +title + "\n" +
                    "message: " + message);

            Intent intent = new Intent(this,NotificationMsgActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("message",message);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setChannelId("Default_Notification_Channel");
            builder.setSmallIcon(getNotificationIcon(builder));
            //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    STREAM_DEFAULT);
            //Vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            //LED
            builder.setLights(Color.RED, 3000, 3000);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(0,builder.build());


//            CustomerNotificationDbHelper helper = new CustomerNotificationDbHelper(getBaseContext());
//            SQLiteDatabase db = helper.getWritableDatabase();
//            helper.saveNotificationToSQLiteDatabase(getDate(date),title,message,0,db);
//            helper.close();
        }
    }

    private void handleImageMessage(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0){
            final String title = remoteMessage.getData().get("title");
            final String subtext = remoteMessage.getData().get("subtext");
            final String message = remoteMessage.getData().get("message");
            String img_url = remoteMessage.getData().get("img_url");
            String webUrl = remoteMessage.getData().get("webUrl");
            Uri sound_uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            //If we want to display server provided image with notification, than we have to
            //omit notification payload. In that case we have to send only data pay load.
            //Only data payload can display image in system notification's tray.
            Intent intent = null;
            if (remoteMessage.getNotification() != null){
                String click_action = remoteMessage.getNotification().getClickAction();
                intent = new Intent(click_action);
                intent.putExtra("click_action",click_action);
            } else {
                intent = new Intent(this,NotificationMsgActivity.class);
            }

            intent.putExtra("webUrl",webUrl);
            intent.putExtra("title",title);
            intent.putExtra("subtext",title);
            intent.putExtra("message",message);
            intent.putExtra("img_url",img_url);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            Log.i("Token","title: " + title + "\n" +
//                    "message: " + message + "\n" +
//                    "img_url: " + img_url + "\n" +
//                    "webUrl: " + webUrl  + "\n" +
//                    "click_action: " + click_action);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setContentTitle(title);
            builder.setSubText(subtext);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSmallIcon(getNotificationIcon(builder));
//            builder.setSound(sound_uri);
            //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    STREAM_DEFAULT);
            //Vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            //LED
            builder.setLights(Color.RED, 3000, 3000);
            builder.setContentIntent(pendingIntent);

            ImageRequest imageRequest = new ImageRequest(img_url, new Response.Listener<Bitmap>() {
                @Override
                public void onResponse(Bitmap response) {
                    //here we will get the image from the server
                    builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(response));

                    //Now we can display the notification with image
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(0,builder.build());

                }
            }, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            MySingelTon.getmInsatance(this).addToRequestQue(imageRequest);

        }
    }

    private void sendWebUrlNotification(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0){
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            String webUrl = remoteMessage.getData().get("webUrl");

            Intent intent = new Intent(this,NotificationWebViewActivity.class);
            intent.putExtra("title",title);
            intent.putExtra("message",message);
            intent.putExtra("webUrl",webUrl);

            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                    "Default_Notification_Channel");
            builder.setContentTitle(title);
            builder.setContentText(message);
            builder.setAutoCancel(true);
            builder.setSmallIcon(getNotificationIcon(builder));
            //Sound
//            builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, STREAM_DEFAULT);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                    STREAM_DEFAULT);
            //Vibration
            builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
            //LED
            builder.setLights(Color.RED, 3000, 3000);
            builder.setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(0,builder.build());
        }
    }


    //Usefull method

    private Long getCurrentTimeStamp() {
        Long time = System.currentTimeMillis()/1000;
        return time;
    }

    private String getDate(Long ride_end_time) {
        //First we will create an object from Calender.class As we are considering
        //Different time zone. This will give us customers local time zone.
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(ride_end_time * 1000);
        String date = DateFormat.format("dd-MM-yyyy hh:mm",calendar).toString();
        return date;

    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            int color = 0x008000;
            notificationBuilder.setColor(Color.RED);
            notificationBuilder.setColorized(true);
            return R.drawable.ic_launcher;

        }
        return R.drawable.ic_launcher;
    }

    private void initNotificationChannels() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String channelIdOne = "com.sand_corporation.www.uthaopartner.FirebaseMessaging.notification_channel_one";
        CharSequence nameOne = getString(R.string.notification_channel_one_name);
        String descriptionOne = getString(R.string.notification_channel_one_description);

        NotificationChannel channelOne = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importanceOne = NotificationManager.IMPORTANCE_HIGH;
            channelOne = new NotificationChannel(channelIdOne, nameOne, importanceOne);
            channelOne.setDescription(descriptionOne);
            channelOne.enableLights(true);
            channelOne.setLightColor(Color.GREEN);
            channelOne.enableVibration(false);
            mNotificationManager.createNotificationChannel(channelOne);

            String channelIdTwo = "com.sand_corporation.www.uthaopartner.FirebaseMessaging.notification_channel_two";
            CharSequence nameTwo = getString(R.string.notification_channel_two_name);
            String descriptionTwo = getString(R.string.notification_channel_two_description);
            int importanceTwo = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channelTwo = new NotificationChannel(channelIdTwo, nameTwo, importanceTwo);
            // Configure the notification channel.
            channelTwo.setDescription(descriptionTwo);
            channelTwo.enableVibration(false);
            mNotificationManager.createNotificationChannel(channelTwo);
        }

    }
}
