package utils.CBOUtils;

import android.content.Context;
import android.content.SharedPreferences;

import utils.AppConstant;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by kuldeep.dwivedi on 11/29/2014.
 */
public class Constants {

    public  static String HIDE_STATUS;
    public static String FINAL_URL;

    public static String getMainUrl(Context context){
        SharedPreferences myPrefrence = context.getSharedPreferences("FMCG_PREFRENCE", Context.MODE_PRIVATE);
        String main_url = myPrefrence.getString("WEBSERVICE_URL", "");
        if (!main_url.equals("")){
            AppConstant.MAIN_URL=main_url;
        }

        return AppConstant.MAIN_URL;
    }


    public static String getSIDE_STATUS(Context context){
        SharedPreferences pref=context.getSharedPreferences(Custom_Variables_And_Method.FMCG_PREFRENCE, context.MODE_PRIVATE);
        HIDE_STATUS=pref.getString("fmcg_value", null);
        return HIDE_STATUS;
    }

    public interface ACTION {
        public static String MAIN_ACTION = "com.truiton.foregroundservice.action.main";
        public static String PREV_ACTION = "com.truiton.foregroundservice.action.prev";
        public static String PLAY_ACTION = "com.truiton.foregroundservice.action.play";
        public static String NEXT_ACTION = "com.truiton.foregroundservice.action.next";
        public static String STARTFOREGROUND_ACTION = "com.truiton.foregroundservice.action.startforeground";
        public static String LIVE_TRACKING_ACTION = "com.truiton.foregroundservice.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.truiton.foregroundservice.action.stopforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}
