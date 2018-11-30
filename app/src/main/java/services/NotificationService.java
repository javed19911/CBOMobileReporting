package services;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.util.Locale;
import java.util.Random;

public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("CHECK", "NotificationService");

        // The manufacturer of the product/hardware. 

        String manufactureStr = Build.MANUFACTURER;

        Log.d("CHECK", "manufacture : " + manufactureStr);

        int badgeno = new Random().nextInt(100);

        if (manufactureStr != null) {

            boolean bool2 = manufactureStr.toLowerCase(Locale.US).contains("htc");
            boolean bool3 = manufactureStr.toLowerCase(Locale.US).contains("sony");
            boolean bool4 = manufactureStr.toLowerCase(Locale.US).contains("samsung");

            // Sony Ericssion
            if (bool3) {
                try {
                    intent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME", "com.vardhan.notificationbadgesample.MainActivity");
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE", true);
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", badgeno);
                    intent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", "com.vardhan.notificationbadgesample");

                    sendBroadcast(intent);
                } catch (Exception localException) {
                    Log.e("CHECK", "Sony : " + localException.getLocalizedMessage());
                }
            }

            // HTC
            if (bool2) {
                try {
                    Intent localIntent1 = new Intent("com.htc.launcher.action.UPDATE_SHORTCUT");
                    localIntent1.putExtra("packagename", "com.cbo.cbomobilereporting");
                    localIntent1.putExtra("count", badgeno);
                    sendBroadcast(localIntent1);

                    Intent localIntent2 = new Intent("com.htc.launcher.action.SET_NOTIFICATION");
                    ComponentName localComponentName = new ComponentName(this, "com.cbo.cbomobilereporting.ui.SplashScreen_2016");
                    localIntent2.putExtra("com.htc.launcher.extra.COMPONENT", localComponentName.flattenToShortString());
                    localIntent2.putExtra("com.htc.launcher.extra.COUNT", badgeno);
                    sendBroadcast(localIntent2);
                    Log.e("CHECK", "HTC : " + "Done");
                } catch (Exception localException) {
                    Log.e("CHECK", "HTC : " + localException.getLocalizedMessage());
                }
            }
            if (bool4) {
                // Samsung
                try {
                    ContentResolver localContentResolver = getContentResolver();
                    Uri localUri = Uri.parse("content://com.sec.badge/apps");
                    ContentValues localContentValues = new ContentValues();
                    localContentValues.put("package", "com.vardhan.notificationbadgesample");
                    localContentValues.put("class", "com.vardhan.notificationbadgesample.MainActivity");
                    localContentValues.put("badgecount", Integer.valueOf(badgeno));
                    String str = "package=? AND class=?";
                    String[] arrayOfString = new String[2];
                    arrayOfString[0] = "com.vardhan.notificationbadgesample";
                    arrayOfString[1] = "com.vardhan.notificationbadgesample.MainActivity";

                    int update = localContentResolver.update(localUri, localContentValues, str, arrayOfString);

                    if (update == 0) {
                        localContentResolver.insert(localUri, localContentValues);
                    }

                } catch (IllegalArgumentException localIllegalArgumentException) {
                    Log.e("CHECK", "Samsung1F : " + localIllegalArgumentException.getLocalizedMessage());
                } catch (Exception localException) {
                    Log.e("CHECK", "Samsung : " + localException.getLocalizedMessage());
                }
            }
        }
    }
}
