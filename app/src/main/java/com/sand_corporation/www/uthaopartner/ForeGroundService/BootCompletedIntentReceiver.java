package com.sand_corporation.www.uthaopartner.ForeGroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

/**
 * Created by HP on 2/3/2018.
 */

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            createForeGroundServiceUsingOnGoingNotification(context);
        }
    }

    private void createForeGroundServiceUsingOnGoingNotification(Context context){
        Intent startService = new Intent(context, ForeGroundService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(startService);
        } else {
            Intent startIntent = new Intent(context, ForeGroundService.class);
            context.startService(startIntent);
        }
    }
}
