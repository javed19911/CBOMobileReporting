package utils_new;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.Date;

import static utils_new.Custom_Variables_And_Method.*;

/**
 * Created by pc24 on 28/11/2017.
 */

public class GpsLocationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
           /* Toast.makeText(context, "in android.location.PROVIDERS_CHANGED",
                    Toast.LENGTH_SHORT).show();*/
            GPS_STATE_CHANGED_TIME=""+new Date().getTime();
            GPS_STATE_CHANGED=true;
        }
    }
}
