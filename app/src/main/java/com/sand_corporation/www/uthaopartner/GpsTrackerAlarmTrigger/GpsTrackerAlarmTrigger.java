package com.sand_corporation.www.uthaopartner.GpsTrackerAlarmTrigger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by HP on 2/2/2018.
 */

public class GpsTrackerAlarmTrigger extends BroadcastReceiver {

    @Override
    public void onReceive (final Context context, Intent intent) {

        scheduleExactAlarm(context,
                (AlarmManager)context.getSystemService(Context.ALARM_SERVICE),
                (10*1000)); //10 Seconds

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "GpsTrackerWakelock");
        wl.acquire(10 * 1000); //10 Seconds

        Handler handler = new Handler();
        Runnable periodicUpdate = new Runnable() {
            @Override
            public void run() {
                // whatever you want to do
                Log.i("Alarm","Triggered");


            }
        };

        handler.post(periodicUpdate);
        wl.release();
    }

    public static void scheduleExactAlarm(Context context, AlarmManager alarms, int interval) {
        int refresh_interval = interval;
        Intent i=new Intent(context, GpsTrackerAlarmTrigger.class);
        PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarms.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime()+10*1000-SystemClock.elapsedRealtime()%1000, pi);
        }
    }

    public static void cancelAlarm(Context context, AlarmManager alarms) {
        Intent i=new Intent(context, GpsTrackerAlarmTrigger.class);
        PendingIntent pi=PendingIntent.getBroadcast(context, 0, i, 0);
        alarms.cancel(pi);
    }
}
