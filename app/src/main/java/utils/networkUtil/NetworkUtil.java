package utils.networkUtil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import utils_new.Custom_Variables_And_Method;


public class NetworkUtil {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    Context context;

    public NetworkUtil(Context c) {
        context = c;
    }

    public static String getDetail(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String val = "";
        if (tm.getNetworkType() <= 128) {
            val = "Slow Connection";
        }

        return val;
    }


    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;

        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = NetworkUtil.getConnectivityStatus(context);
        String status = null;
        if (conn == NetworkUtil.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == NetworkUtil.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {

            status = "Not connected to Internet";
        } else {
            status = "Not connected to Internet";
        }
        return status;
    }


    public Boolean internetConneted(Context context) {

        int connect = NetworkUtil.getConnectivityStatus(context);


        if ((connect == NetworkUtil.TYPE_WIFI) || (connect == NetworkUtil.TYPE_MOBILE)) {

            return true;
        } else {
            return false;
        }
    }

}