package com.cbo.cbomobilereporting.emp_tracking;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class StartAlarmAfterDeviceRestart extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")){

            Intent LocationBroadcast = new Intent(context,LocationBroadcast.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,0,LocationBroadcast,0);

          AlarmManager  alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

            int INTERVAL = 1000*10;

            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),INTERVAL,pendingIntent);
        }


    }
}
