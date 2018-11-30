package utils.CBOUtils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by Kuldeep.Dwivedi on 3/1/2015.
 */
public class SystemArchitecture {
    public static String COMPLETE_DEVICE_INFO;

    public SystemArchitecture(Context context){
        Context context1 = context;
    }

    public String getDEVICE_ID(Context context){
        TelephonyManager telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID;
        try {
            //DEVICE_ID=telephonyManager.getDeviceId();
            if (telephonyManager.getDeviceId() != null){
                DEVICE_ID = telephonyManager.getDeviceId();
            }else{
                DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }catch (Exception e){
            DEVICE_ID ="0";
        }

        String DEVICE_MODEL = android.os.Build.MODEL;
        String BRAND = android.os.Build.BRAND;

      if (BRAND == null){
            BRAND ="";
        }
        if (DEVICE_MODEL == null){
            DEVICE_MODEL ="MODEL";
        }
        if(DEVICE_ID ==null){
            DEVICE_ID ="0";
        }
        COMPLETE_DEVICE_INFO = DEVICE_ID +"'!'"+ BRAND +""+ DEVICE_MODEL;;
        return COMPLETE_DEVICE_INFO;
    }
   /* public String getManufacturer(Context context){


        if (Manufacturer_Name == null){
            Manufacturer_Name ="Device Name Not Found";
        }
          return Manufacturer_Name;
    }*/

}
