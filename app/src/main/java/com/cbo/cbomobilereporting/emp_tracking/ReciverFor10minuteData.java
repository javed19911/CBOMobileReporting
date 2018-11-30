package com.cbo.cbomobilereporting.emp_tracking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import utils.MyConnection;

/**
 * Created by Akshit on 12/4/2015.
 */
public class ReciverFor10minuteData extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context,ServiceForSendingData10Minute.class));

     //   myConnection.msgBox("Service Start For For Sending Data To Server........");
        Log.d("Broadcast is Running 10 minute", "Service Start For For Sending Data To Server........ ");
    }
}
