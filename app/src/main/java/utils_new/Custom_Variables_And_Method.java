package utils_new;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyLoctionService;
import com.cbo.cbomobilereporting.emp_tracking.DistanceCalculator;
import com.cbo.cbomobilereporting.emp_tracking.GPSTracker;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import locationpkg.LocationTest;
import com.cbo.cbomobilereporting.MyCustumApplication;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import utils.networkUtil.NetworkUtil;

import static android.content.ContentValues.TAG;

/**
 * Created by pc24 on 06/01/2017.
 */
public class Custom_Variables_And_Method implements com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    private static Custom_Variables_And_Method ourInstance = null;
    private static CBO_DB_Helper cbohelp;


    private static Context context;
    public static String ip;
    public static String user;
    public static String pwd;
    public static String db;
    public static int PA_ID=0;
    public static String PA_NAME;
    public static String HEAD_QTR;
    public static String DESIG;
    public static String DCR_ID ="0";
    public static String name;
    public static String work_val;
    public static String FAILED_REASON;
    public static String location_required = "N";
    public static String WORKING_TYPE;
    public static String CHEMIST_NOT_VISITED;
    public static String STOCKIST_NOT_VISITED;
    public static String DR_NAME;
    public static String user_name;
    public static String GLOBAL_LATLON = "0.0,0.0";
    public static String FMCG_PREFRENCE = "FMCG_PREFRENCE";
    public static String work_with_area_id = "";
    public static String ROOT_NEEDED;
    public static String CHEMIST_ID;
    public static String COMPANY_NAME;
    public static String checkVersion = "20190628";
    public static String VERSION = "20190628";
    public static String RPT_DATE;
    public static String EMP_ID;
    public static String DCR_DATE;
    public static String DCR_DATE_TO_SUBMIT = "Y";
    public static String RPT_TIME;
    public static String DR_ID;
    public static String VISUAL_REQUIRED;
    public static String global_address = "";
    public static String WEB_URL;
    public static String pub_area;
    public static String pub_desig_id;
    public static String pub_doctor_spl_code;
    public static String doctor_image_name;
    public static int DOCTOR_SPL_ID;
    public static String COMPANY_CODE;
    public static String lastLocation;
    public static String SELECTED_AREA;
    public static String INTERNET_REQ = "";

    public static String extraFrom = "";
    public static String extraTo = "";
    public static int CURRENTTAB=0;
    public static String BATTERYLEVEL="0";
    public static String SAMPLE_POB_MANDATORY="N";

    public static Boolean GPS_STATE_CHANGED=true;
    public static String GPS_STATE_CHANGED_TIME="0";
    public static Boolean FORCEFULLY_ACCEPT_GPS_LOCATION=false;

    private final int REQUEST_CHECK_SETTINGS = 1000;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;


    public static ArrayList<String> DcrPending_datesList = new ArrayList<String>();

    public static Custom_Variables_And_Method getInstance(Context context1) {
        if (ourInstance == null)
        {
            ourInstance = new Custom_Variables_And_Method();
        }
        context=context1;
        cbohelp = new CBO_DB_Helper(context);
        return ourInstance;
    }

    public static Custom_Variables_And_Method getInstance() {
        if (ourInstance == null)
        {
            ourInstance = new Custom_Variables_And_Method();
        }
        return ourInstance;
    }

    private Custom_Variables_And_Method() {
    }


    public  CBO_DB_Helper get_cbo_db_instance() {
        return cbohelp;
    }

    public void snackBar(String msg, View v) {
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.setAction("RETRY", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        sbView.performClick();

        snackbar.show();
    }


    public void msgBox(Context context,String msg) {
        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        View view = toast.getView();
        view.setBackgroundResource(R.drawable.toastbackground);
        TextView text = (TextView) view.findViewById(android.R.id.message);
        text.setTextColor(0xFFFFFFFF);
        //toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL,0,150);
        toast.show();
    }


    public void getbattrypercentage(Context context) {


        BroadcastReceiver br = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int current_level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (current_level >= 0 && scale > 0) {
                    level = (current_level * 100) / scale;
                }
                BATTERYLEVEL= ""+level;
            }
        };
        IntentFilter batrylevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(br, batrylevelFilter);
    }

    public void getAlert(Context context, String title, String massege) {
        getAlert(context,title,massege,null,null,false);
    }
    public void getAlert(Context context, String title, String massege,Boolean resultVisible) {
        getAlert(context,title,massege,null,null,resultVisible);
    }
    public void getAlert(Context context, String title, String massege, String url) {
        getAlert(context,title,massege,null,url,false);
    }
    public void getAlert(Context context, String title,String[] table_list) {
        getAlert(context,title,null,table_list,null,false);
    }
    public void getAlert(final Context context, final String title, final String massege, String[] table_list, final String url, Boolean reportVisible) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+PA_ID);

        final TextView report= (TextView) dialogLayout.findViewById(R.id.report);
           if (reportVisible) {
               report.setVisibility(View.VISIBLE);
           }

        if (table_list==null ) {
            Alert_message.setText(massege);
            Alert_message_list.setVisibility(View.GONE);
        }else{
            Alert_message.setVisibility(View.GONE);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
            TableRow.LayoutParams params1 = new TableRow.LayoutParams(0, 1, 1f);
            Alert_message_list.removeAllViews();
            for (int i = 0; i < table_list.length; i++) {
                TableRow tbrow = new TableRow(context);
                if ( !table_list[i].contains(":")) {
                    TextView t1v = new TextView(context);
                    t1v.setText(table_list[i].replace("\n",""));
                    t1v.setPadding(15, 10, 15, 10);
                    t1v.setBackgroundColor(0xff5477cf);
                    t1v.setTextColor(Color.WHITE);
                    t1v.setTextSize(16);
                    t1v.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
                    t1v.setLayoutParams(params);
                    tbrow.addView(t1v);
                }else{
                    TextView t1v = new TextView(context);
                    t1v.setText(table_list[i]);
                    t1v.setPadding(15, 5, 15, 0);
                    t1v.setTextColor(Color.BLACK);
                    t1v.setLayoutParams(params);
                    t1v.setTypeface(Typeface.MONOSPACE, Typeface.NORMAL);
                    tbrow.addView(t1v);
                }
               /* TextView t2v = new TextView(context);
                t2v.setText(table_list[i]);
                t2v.setPadding(5, 5, 5, 0);
                t2v.setTextColor(Color.BLACK);
                t2v.setGravity(Gravity.CENTER);
                tbrow.addView(t2v);*/
                TableRow tbrow1 = new TableRow(context);
                TextView t3v = new TextView(context);
                t3v.setPadding(15, 1, 15, 0);
                t3v.setLayoutParams(params1);
                t3v.setBackgroundColor(0xff125688);
                tbrow1.addView(t3v);
                Alert_message_list.addView(tbrow);
                Alert_message_list.addView(tbrow1);
            }
        }


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url!=null && !url.isEmpty()){
                    /*Intent i = new Intent(context, CustomWebView.class);
                    i.putExtra("A_TP", url);
                    i.putExtra("Title", title);
                    context.startActivity(i);*/
                    MyCustumApplication.getInstance().LoadURL(title,url);
                }
                dialog.dismiss();
            }
        });

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Location currentBestLocation=getObject(context,"currentBestLocation",Location.class);
                List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
                *//*new SendMailTask().execute("mobilereporting@cboinfotech.com",
                        "mreporting",toEmailList , Custom_Variables_And_Method.COMPANY_CODE+": Out of Range Error report",context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n massege : "+massege+"\n Error Alert :"+title+"\n"+
                "\nLocation-timestamp : "+currentBestLocation.getTime()+"\nLocation-Lat : "+currentBestLocation.getLatitude()+
                        "\nLocation-long : "+currentBestLocation.getLongitude()+"\n time : " +currentTime(context)+"\nlatlong : "+ getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON));
*//*

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ExpenseRoot.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, ExpenseRoot.this.REQUEST_CAMERA);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    capture_Image();
                }*/
               //new SendAttachment((Activity) context).execute("HELLO JAVED",);
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public String getAddressByLatLong(Context context, String myLatLong) {

        double lat = 0.0, lon = 0.0;
        String coder_addres = "";
        String loc_cord[] = myLatLong.split(",");
        if (loc_cord.length==2) {
            String cord1 = loc_cord[0];
            String cord2 = loc_cord[1];
            lat = Double.parseDouble(cord1);
            lon = Double.parseDouble(cord2);


            Geocoder coder = new Geocoder(context, Locale.getDefault());
            try {
                List<Address> address = coder.getFromLocation(lat, lon, 5);
                StringBuilder sb = new StringBuilder();
                for (Address address_new : address) {
                    //Address returnedAddress = address_new; //address.get(0);
                    sb = new StringBuilder();
                    for (int i = 0; i <= address_new.getMaxAddressLineIndex(); i++) {
                        sb.append(address_new.getAddressLine(i)).append(",");
                    }
                    if(coder_addres.length() < sb.toString().length()) {
                        coder_addres = sb.toString();
                    }
                }

            } catch (Exception e) {
                Log.v("javed error", e.toString());
                e.printStackTrace();
            }
        }
        return coder_addres;
    }


 /*   public String getAddress(Context context, String myLatLong) {
        String cAddress = "";
        double lat = 0.0, lon = 0.0;
        String errorMessage ="";
        String loc_cord[] = myLatLong.split(",");
        if (loc_cord.length==2) {
            String cord1 = loc_cord[0];
            String cord2 = loc_cord[1];
            lat = Double.parseDouble(cord1);
            lon = Double.parseDouble(cord2);
        }else{
            return "";
        }

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        // Address found using the Geocoder.
        List<Address> addresses = null;

        try {
            // Using getFromLocation() returns an array of Addresses for the area immediately
            // surrounding the given latitude and longitude. The results are a best guess and are
            // not guaranteed to be accurate.
            addresses = geocoder.getFromLocation(lat, lon,
                    // In this sample, we get just a single address.
                    1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available";
//            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used";
            *//*Log.e(TAG, errorMessage + ". " +
                    "Latitude = " + latLng.latitude +
                    ", Longitude = " + latLng.longitude, illegalArgumentException);*//*
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no_address_found";
                //Log.e(TAG, errorMessage);
            }
        } else {
            Address address = addresses.get(0);
            ArrayList<String> addressFragments = new ArrayList<String>();
            // Fetch the address lines using {@code getAddressLine},
            // join them, and send them to the thread. The {@link android.location.address}
            // class provides other options for fetching address details that you may prefer
            // to use. Here are some examples:
            // getLocality() ("Mountain View", for example)
            // getAdminArea() ("CA", for example)
            // getPostalCode() ("94043", for example)
            // getCountryCode() ("US", for example)
            // getCountryName() ("United States", for example)
            String allAddress = "";
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                addressFragments.add(address.getAddressLine(i));
                allAddress += address.getAddressLine(i) + " ";
            }
            *//*if (address.getAdminArea() != null) {
                state = address.getAdminArea();
            } else {
                state = "";
            }
            if (address.getLocality() != null) {
                city = address.getLocality();
            } else {
                city = "";
            }
            if (address.getPostalCode() != null) {
                postalCode = address.getPostalCode();
            } else {
                postalCode = "";
            }*//*

            //Log.i(TAG, "address_found");
            //driverAddress = TextUtils.join(System.getProperty("line.separator"), addressFragments);
            cAddress = allAddress;
            //Log.e("result", cAddress.toString());
        }
        return cAddress;
    }
*/
    public boolean isBackgroundServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            Log.d("Services","hello");
            Log.d("Services",service.service.getClassName());
            if(MyLoctionService.class.getName().equals(service.service.getClassName())) {
                Log.d("Services","running");
                return true;
            }
        }
        return false;
    }

    ///////////////////////////////////////////setDataInTo_FMCG_PREFRENCE//////////////////////////////////
    public Boolean setDataInTo_FMCG_PREFRENCE(Context context,String key, String value) {


        SharedPreferences.Editor myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE).edit();
        myPrefrence.putString(key, value);

        return myPrefrence.commit();
    }

    ///////////////////////////////////////////
    /////////////////////////////////getDataFrom_FMCG_PREFRENCE/////////////////////////////

    public String getDataFrom_FMCG_PREFRENCE(Context context,String key) {

        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        String value = myPrefrence.getString(key, null);
        if (value == null) {
            value = "";
        }

        return value;
    }

    public Boolean IsGPS_GRPS_ON(Context context){
        int mode=0;
        Boolean GPS_enabled;

        if (getDataFrom_FMCG_PREFRENCE(context,"gps_needed","Y").equals("Y")) {
            mode = Custom_Variables_And_Method.getLocationMode(context);
            GPS_enabled=Custom_Variables_And_Method.checkGpsEnable(context);
        }else{
            GPS_enabled=true;
            mode=3;
        }

        if (!GPS_enabled || mode != 3) {
            // showSettings();
            msgBox(context,"Please Swicth ON your GPS");
            if (mode !=0){
                RequestGPSFromSetting(context);
            }else{
                getGpsSetting(context);
            }

            return false;
        } else if (getDataFrom_FMCG_PREFRENCE(context,"MOBILEDATAYN", "N").equals("Y") && !internetConneted(context)) {
            //msgBox(context,"Not Connected to Internet....");
            Connect_to_Internet_Msg(context);
           return false;
        }
        return true;
    }

    public void Connect_to_Internet_Msg(Context context){
        getAlert(context,"Internet !!!","Not Connected to Internet....\nPlease Switch ON your Mobile Data/WiFi...");
    }

    public String getDataFrom_FMCG_PREFRENCE(Context context,String key,String default_value) {

        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        String value = myPrefrence.getString(key, null);
        if (value == null) {
            value = default_value;
        }

        return value;
    }
////////////////////////////////////////////////////////
    //////////////////////Deleting ShareadPreferences//////////////

    public void deleteComplete_FMCG_PREFRENCE(Context context) {
        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        myPrefrence.edit().clear().apply();

    }

    ////////////////////////////////
    //////////////======Delete Key wise Fmcg Table////////////
    public void deleteFmcg_ByKey(Context context,String key) {

        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        myPrefrence.edit().remove(key).apply();

    }

    public void putObject(Context context, String key, Location location) {
        String who="_"+key;
        /*if (key.equals("currentBestLocation_Validated")){
            who="1";
        }*/
        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        if (location == null) {
            myPrefrence.edit().remove("LOCATION_LAT"+who).apply();
            myPrefrence.edit().remove("LOCATION_LON"+who).apply();
            myPrefrence.edit().remove("LOCATION_PROVIDER"+who).apply();
            myPrefrence.edit().remove("LOCATION_ACCURACY"+who).apply();
            myPrefrence.edit().remove("LOCATION_TIME"+who).apply();
            myPrefrence.edit().remove("LOCATION_SPEED"+who).apply();
        } else {
            myPrefrence.edit().putString("LOCATION_LAT"+who, String.valueOf(location.getLatitude())).apply();
            myPrefrence.edit().putString("LOCATION_LON"+who, String.valueOf(location.getLongitude())).apply();
            myPrefrence.edit().putString("LOCATION_PROVIDER"+who, location.getProvider()).apply();
            myPrefrence.edit().putString("LOCATION_ACCURACY"+who, String.valueOf(location.getAccuracy())).apply();
            myPrefrence.edit().putString("LOCATION_TIME"+who, String.valueOf(location.getTime())).apply();
            myPrefrence.edit().putString("LOCATION_SPEED"+who, String.valueOf(location.getSpeed())).apply();
        }

       /* if (object == null) {
            throw new IllegalArgumentException("object is null");
        }

        if (key.equals("") || key == null) {
            throw new IllegalArgumentException("key is empty or null");
        }
        Gson gson = new Gson();
        String json = gson.toJson(object); // myObject - instance of MyObject

        setDataInTo_FMCG_PREFRENCE(context,key,json);*/

    }


    public Location getObject(Context context, String key, Class<Location> a) {
        String who="_"+key;
        /*if (key.equals("currentBestLocation_Validated")){
            who="1";
        }*/
        SharedPreferences myPrefrence = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE);
        String lat = myPrefrence.getString("LOCATION_LAT"+who, null);
        String lon = myPrefrence.getString("LOCATION_LON"+who, null);
        Location location = null;
        if (lat != null && lon != null) {
            String provider = myPrefrence.getString("LOCATION_PROVIDER"+who, null);
            String time = myPrefrence.getString("LOCATION_TIME"+who, null);
            String speed = myPrefrence.getString("LOCATION_SPEED"+who, null);
            String accuracy = myPrefrence.getString("LOCATION_ACCURACY"+who, null);

            location = new Location(provider);
            location.setLatitude(Double.parseDouble(lat));
            location.setLongitude(Double.parseDouble(lon));
            location.setTime(Long.parseLong(time));
            location.setSpeed(Float.parseFloat(speed));
            location.setAccuracy(Float.parseFloat(accuracy));
        }
        return location;
        /*String gson =  getDataFrom_FMCG_PREFRENCE(context,key,null);
        if (gson == null) {
            return null;
        } else {
            try {

                return new Gson().fromJson(gson, a);
            } catch (Exception e) {
                throw new IllegalArgumentException("Object storaged with key " + key + " is instanceof other class");
            }
        }*/
    }


    //////////////=====////////////

    public Boolean IsCallAllowedToday(Context context) {

        String toDayDate = currentDate();// getDataFrom_FMCG_PREFRENCE(context,"CUR_DATE",currentDate());
        String dcrDateReal = getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real");
        String dcrPlanedDate = getDataFrom_FMCG_PREFRENCE(context,"Dcr_Planed_Date");
        if (dcrDateReal.equals("Y")) {
            toDayDate = "Y";
        }

        if ((dcrDateReal.equals(dcrPlanedDate)) && (!toDayDate.equals(dcrPlanedDate))) {
            return false;
        } else {

            return true;
        }

    }

    public Boolean IsBackDate(Context context) {

        //String toDayDate = currentDate();
        String toDayDate = getDataFrom_FMCG_PREFRENCE(context,"CUR_DATE",currentDate());
        String DCR_DATE = getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");
        if ((DCR_DATE.equals(toDayDate))) {
            return false;
        } else {
            return true;
        }

    }

    ///////////// Getting Current Date ///////////////////
    public String convetDateMMddyyyy(Date date) {

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = timeStampFormat.format(date);
        return todayDate.toString();
    }


    public String currentDate() {

//        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//
//        Date todayDate = new Date();
//        String currentDate = dateFormat.format(todayDate);


        return currentDate("MM/dd/yyyy");
    }

    public String currentDate(String format) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        Date todayDate = new Date();
        String currentDateStr = dateFormat.format(todayDate);


        return currentDateStr;
    }
//////////////////////////////////////////

    public String convetDateddMMyyyy(Date date) {

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd/MM/yyyy");

        String todayDate = timeStampFormat.format(date);

        return todayDate.toString();
    }


    ///////////////////////////////Getting Current time ///////////////////

    public String currentTime(Context context) {
      return  currentTime(context,true);
    }
    public String currentTime(Context context,Boolean addServerTimeDifference) {

        String mytime = "";
        Calendar now = Calendar.getInstance();
        Calendar tmp = (Calendar) now.clone();
        if (addServerTimeDifference){
            tmp.add(Calendar.MINUTE, Integer.parseInt( getDataFrom_FMCG_PREFRENCE(context, "DcrPlanTime_server", "0" )));
        }
        Calendar c = tmp;
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        mytime = String.format("%02d.%02d", mHour, mMinute);

        return mytime;

    }

    public String get_currentTimeStamp(){
        return ""+new Date().getTime();
    }


    public String getCurrentBestTime(Context context){
       Location currentBestLocation=getObject(context,"currentBestLocation",Location.class);
        if (currentBestLocation==null) return currentTime(context);

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(currentBestLocation.getTime());
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);
        Float locationTime = Float.valueOf(mHour+"."+mMinute);

       /* SimpleDateFormat formatter = new SimpleDateFormat("hh.mm");
        Float locationTime = Float.valueOf(formatter.format(new Date(currentBestLocation.getTime())));*/

        Float deviceTime= Float.valueOf(currentTime(context));
        if (deviceTime>locationTime)return String.valueOf(locationTime);
        return currentTime(context);
    }

    /////////////splittingRouteData/////////////

    public ArrayList<String> splitRouteData(String route) {

        ArrayList<String> data = new ArrayList<String>();
        String myData1 = route.replace("^", "_");
        String parsingdata[] = myData1.split("_");
        String newResult1 = parsingdata[0];
        String newResult2 = parsingdata[1];
        String newResult3 = parsingdata[2];


        String fno = newResult2.substring(4);
        String vf = newResult3.substring(7);


        data.add(newResult1);
        data.add(fno);
        data.add(vf);


        return data;
    }

    public Boolean IsProductEntryReq(Context context){
        return getDataFrom_FMCG_PREFRENCE(context,"DCRPPNA","N").equalsIgnoreCase("N");
    }

    public String srno(Context context){
        String count= getDataFrom_FMCG_PREFRENCE(context,"srno");
        if(count.equals(""))
            count="0";
        // Log.d("javed SRNO  :",count);
        int a=Integer.parseInt(count)+1;
        setDataInTo_FMCG_PREFRENCE(context,"srno",""+a);
        return ""+a;
    }

    public void UpdateGPS_Location_Forcefully(Context context){
        FORCEFULLY_ACCEPT_GPS_LOCATION=true;
        setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", get_currentTimeStamp());
    }

    public Boolean IsValidLocation(Context context,String lat_long_current,int who) {
        //new DistanceCalculator();

        Location last_location=getObject(context,"currentBestLocation_Validated",Location.class);

        if (last_location==null)
            return true;

        GPSTracker tracker= new GPSTracker(context);

       /* Double km= DistanceCalculator.distance(Double.valueOf(lat_long_current.split(",")[0]), Double.valueOf(lat_long_current.split(",")[1])
                ,  Double.valueOf(getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",GLOBAL_LATLON).split(",")[0]), Double.valueOf(getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",GLOBAL_LATLON).split(",")[1]), "K");
*/
        Double km= DistanceCalculator.distance(Double.valueOf(lat_long_current.split(",")[0]), Double.valueOf(lat_long_current.split(",")[1])
                ,  last_location.getLatitude(), last_location.getLongitude(), "K");

        /* Double km= DistanceCalculator.distance(27.2681261, 95.1035363, 26.4054659, 80.3426673, "K");*/

        String current_time=get_currentTimeStamp();

        if(FORCEFULLY_ACCEPT_GPS_LOCATION){
            FORCEFULLY_ACCEPT_GPS_LOCATION=false;
            setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", current_time);
            km=0.0;
        }
        Double estimated_time_taken=km/3;             //3km per min allowed

        String last_update_time=getDataFrom_FMCG_PREFRENCE(context, "last_location_update_time_in_minites",current_time);

        Double real_time_taken=(Double.valueOf(current_time)-Double.valueOf(last_update_time))/60000;

        if (who>0){
            if (km>30  && who!=3){
                //Location loc= latLongFromInternet(context);
                Location loc=  tracker.getLocation();
                if (loc!=null) {
                    return IsValidLocation(context, loc.getLatitude() + "," + loc.getLongitude(), who + 1);
                }else {
                    return IsValidLocation(context, lat_long_current, who + 1);
                }
            }else {
                if (estimated_time_taken <= real_time_taken) {
                    Custom_Variables_And_Method.GLOBAL_LATLON = lat_long_current;
                    setDataInTo_FMCG_PREFRENCE(context, "shareLatLong", lat_long_current);
                    setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", get_currentTimeStamp());
                    putObject(context,"currentBestLocation_Validated",getObject(context,"currentBestLocation",Location.class));
                }
                //tracker.stopUsingGPS();
                return false;
            }
        }
        if (km>30 || IsLocationTooOld(context,0)){
           // Location loc= latLongFromInternet(context);
            Location loc=  tracker.getLocation();
            if (loc!=null) {
                return IsValidLocation(context, loc.getLatitude() + "," + loc.getLongitude(), who + 1);
            }else {
                return IsValidLocation(context, lat_long_current, who + 1);
            }
        }else {
            tracker.stopUsingGPS();
            return estimated_time_taken <= real_time_taken;
        }
    }

    public Boolean IsLocationTooOld(Context context,final int count){
        Long time_difference= 0L;
        if (getObject(context,"currentBestLocation_Validated",Location.class)!=null) {
            Long location_time = getObject(context, "currentBestLocation_Validated", Location.class).getTime();
            Long current_time = Long.parseLong(get_currentTimeStamp());
            time_difference = current_time - location_time;
        }
        int allowed_time=5*60*1000;  // 5min

        if(!FORCEFULLY_ACCEPT_GPS_LOCATION) {
            return time_difference == 0 || time_difference > allowed_time;
        }else {
            return false;
        }
    }

    public Location latLongFromInternet(Context context) {
        final String[] latLong = {""};

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location location1=null;
        try {
            location1 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            latLong[0] = location1.getLatitude() + "," + location1.getLongitude();
            if (latLong!=null){
                putObject(context,"currentBestLocation",location1);
            }

        } catch (SecurityException e) {
            latLong[0] = "0.0,0.0";
        } catch (NullPointerException n) {
            latLong[0] = "0.0,0.0";
        }
       // if (latLong[0].equalsIgnoreCase("0.0,0.0")) {
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    try {
                        latLong[0] = location.getLatitude() + "," + location.getLongitude();
                        Custom_Variables_And_Method.GLOBAL_LATLON = latLong[0];
                    } catch (Exception e) {


                    }
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } catch (SecurityException s) {


            }
       // }

        return location1;
    }

    public String get_best_latlong(Context context) {

        String lat_long = getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
       /* ArrayList<String> latlong, isValid;
        ArrayList<Double> kilometer;
        latlong = new ArrayList<>();
        isValid = new ArrayList<>();
        kilometer = new ArrayList<>();
        int k = 0;

        for (int i = 0; i < 10; i++) {
            Location location=latLongFromInternet(context);
            String lat_long1 = location.getLatitude()+","+location.getLongitude();
            Double km = DistanceCalculator.distance(Double.valueOf(lat_long1.split(",")[0]), Double.valueOf(lat_long1.split(",")[1])
                    , Double.valueOf(lat_long.split(",")[0]), Double.valueOf(lat_long.split(",")[1]), "K");
            latlong.add(lat_long1);
            kilometer.add(km);

            if (km > 0.5) {
                isValid.add("N");
            } else {
                isValid.add("Y");
                k++;
            }
        }


        if (k < 4) {
            Double valid_km = -1.0;
            for (int i = 0; i < 10; i++) {
                if (isValid.get(i).equals("N")) {
                    if (valid_km < 0) {
                        valid_km = kilometer.get(i);
                        lat_long = latlong.get(i);
                    } else if (valid_km > kilometer.get(i)) {
                        valid_km = kilometer.get(i);
                        lat_long = latlong.get(i);
                    }
                }
            }
        }*/
        return lat_long;
    }


    static public int getLocationMode(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }


        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);


            if (TextUtils.isEmpty(locationProviders)){
                locationMode = Settings.Secure.LOCATION_MODE_OFF;
            }
            else if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
                locationMode = Settings.Secure.LOCATION_MODE_HIGH_ACCURACY;
            }
            else if (locationProviders.contains(LocationManager.GPS_PROVIDER)){
                locationMode = Settings.Secure.LOCATION_MODE_SENSORS_ONLY;
            }
            else if (locationProviders.contains(LocationManager.NETWORK_PROVIDER)){
                locationMode = Settings.Secure.LOCATION_MODE_BATTERY_SAVING;
            }

        }

        return locationMode;
    }

    static private Boolean checkGpsEnableOldMethod(Context context) {

        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Boolean gps;
        try {
            gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            return false;
        }
        return gps;
    }

    static public String checkGpsEnableNewMethod(Context context) {

        try {
            return Settings.System.getString(context.getContentResolver(),
                            Settings.System.LOCATION_PROVIDERS_ALLOWED);
        } catch (Exception e) {

            return "";
        }

    }


    static public Boolean internetConneted(Context context) {

        int connect = NetworkUtil.getConnectivityStatus(context);


        if ((connect == NetworkUtil.TYPE_WIFI) || (connect == NetworkUtil.TYPE_MOBILE)) {

            return true;
        } else {
            return false;
        }
    }

    public Boolean checkIfCallLocationValid(Context context,Boolean CheckForceFully){
        return checkIfCallLocationValid(context, CheckForceFully,false);
    }
    public Boolean checkIfCallLocationValid(Context context,Boolean CheckForceFully,Boolean Skip_Verification){

        if(!getDataFrom_FMCG_PREFRENCE(context,"gps_needed","Y").equals("Y") || Skip_Verification)
            return true;

        int No_Location = 2;
        if(!CheckForceFully) {
            //boolean IsLastLocationMadeTwoHoursEarlier = IsLocationOlderThan(context, "LastCallLocation", 2 * 60 * 60 * 1000);
            //boolean IsLastValidatedLocationFiveMinitesOlder = IsLocationOlderThan(context, "currentBestLocation_Validated", 5 * 60 * 1000);
            if (30 > DistanceBetween(getObject(context,"LastCallLocation",Location.class),getObject(context,"currentBestLocation_Validated",Location.class))) {
                return true;
            }
            No_Location = 2;
        }
        Intent intent = new Intent(context, LocationTest.class);
        intent.putExtra("No_Location",No_Location);
        context.startActivity(intent);

        return false;
    }

    public void SetLastCallLocation(Context context){
        putObject(context,"LastCallLocation", getObject(context,"currentBestLocation_Validated",Location.class));
        setDataInTo_FMCG_PREFRENCE(context,"LastCallTime", get_currentTimeStamp());
    }
    public Boolean IsLocationOlderThan(Context context,String LocationKey,int allowed_time){
        Long time_difference= 0L;
        if (getObject(context,LocationKey,Location.class)!=null) {
            //Long location_time = getObject(context, LocationKey, Location.class).getTime();
            time_difference = GetLocationTimeDifference(getObject(context, LocationKey, Location.class));
            //return IsTimeOlderThan(location_time,allowed_time);
        }
        //int allowed_time=2*60*60*1000;  // 2 hours

        return  time_difference > allowed_time;
    }

    public long GetLocationTimeDifference(Location location){
        Long time_difference= 0L;
        if (location!=null) {
            Long current_time = Long.parseLong(get_currentTimeStamp());
            time_difference = current_time - location.getTime();
        }
        return time_difference ;
    }
    public Boolean IsTimeOlderThan(Long CheckTime,int allowed_time){
        Long time_difference= 0L;
        if (CheckTime!=null) {
            Long current_time = Long.parseLong(get_currentTimeStamp());
            time_difference = current_time - CheckTime;
        }
        return time_difference > allowed_time;
    }

    public Double DistanceBetween(Location PointA,Location PointB){
        Double km = 0.0;
        if (PointA !=null && PointB !=null ) {
            km = DistanceCalculator.distance(PointA.getLatitude(), PointA.getLongitude(), PointB.getLatitude(), PointB.getLongitude(), "K");

        }
        return km;
    }

    static public Boolean checkGpsEnable(Context context) {

        String allowedLocationProviders = checkGpsEnableNewMethod(context);
        Boolean gps = checkGpsEnableOldMethod(context);

        if (gps || ((!allowedLocationProviders.equals("")) && (!allowedLocationProviders.equals("network")))) {

            return true;

        } else {

            return false;

        }
    }


    public boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public Boolean IsGPS_ON() {
        int mode =3;
        Boolean GPS_enabled=false;
        mode = new MyCustomMethod(context).getLocationMode(context);
        GPS_enabled=!new MyCustomMethod(context).checkGpsEnable();


        if (GPS_enabled || mode != 3) {
            return false;
        }
        return true;
    }

    public void RequestGPSFromSetting(Context context){
        AppAlert.getInstance().Alert(context,
                "GPS !!!", "Please Switch ON your GPS with HIGH ACCURACY"
                , new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });

    }

    //////////////////////////////
    public void getGpsSetting(final Context context) {

        /*googleApiClient = null;
        if (googleApiClient == null) {*/
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
        //}

        ////
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************
       /* PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());*/
        Task<LocationSettingsResponse> result =
                LocationServices.getSettingsClient(context).checkLocationSettings( builder.build());

        result.addOnSuccessListener((Activity) context, new OnSuccessListener<LocationSettingsResponse>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //  GPS is already enable, callback GPS status through listener

            }
        }).addOnFailureListener((Activity) context, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText((Activity) context, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });

       /* result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                                     @Override
                                     public void onResult(LocationSettingsResult result) {
                                         final Status status = result.getStatus();
                                         final LocationSettingsStates state = result.getLocationSettingsStates();
                                         switch (status.getStatusCode()) {
                                             case LocationSettingsStatusCodes.SUCCESS:
                                                 // All location settings are satisfied. The client can initialize location
                                                 // requests here.

                                                 break;
                                             case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                                 // Location settings are not satisfied. But could be fixed by showing the user
                                                 // a dialog.
                                                 try {
                                                     // Show the dialog by calling startResolutionForResult(),
                                                     // and check the result in onActivityResult().
                                                     status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                                                 } catch (IntentSender.SendIntentException e) {
                                                     // Ignore the error.
                                                 }
                                                 break;
                                             case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                                 // Location settings are not satisfied. However, we have no way to fix the
                                                 // settings so we won't show the dialog.
                                                 // mycon.msgBox("you are here");
                                                 msgBox(context,"Please Swicth ON your GPS from Settings");
                                                 break;
                                         }
                                     }
                                 }


        );*/


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


}
