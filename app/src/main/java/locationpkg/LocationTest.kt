package locationpkg

import android.app.ActivityManager
import android.location.Location
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.cbo.cbomobilereporting.R

import android.support.v4.content.LocalBroadcastManager
import android.content.*
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cbo.cbomobilereporting.emp_tracking.DistanceCalculator
import com.cbo.cbomobilereporting.MyCustumApplication
import utils_new.AppAlert
import utils_new.Custom_Variables_And_Method
import java.util.*
import kotlin.collections.ArrayList


class LocationTest : AppCompatActivity() {


    private var mPositionContainer: TextView? = null // print location here
    private var context : Context ? = null
    private  var LocArr : ArrayList<Location> = ArrayList()
    private lateinit var customVariableandMethod : Custom_Variables_And_Method
    private  val GPS_Setting_Enabled= 1
    private var NoOfTry  =0
    private var LocationTestSize =10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_test)
        this.setFinishOnTouchOutside(false);
//        val toolbar = findViewById(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
        context = this;
        customVariableandMethod = Custom_Variables_And_Method.getInstance()
        // get text view
        mPositionContainer = findViewById(R.id.positionContainerTxt) as TextView

        var imageView :ImageView = findViewById(R.id.scan_img)
        Glide.with(context as LocationTest)
                .load(Uri.parse("file:///android_asset/wifianimation.gif")).asGif()
                .error(R.drawable.no_image)
                .into(imageView);

        LocationTestSize = intent.getIntExtra("No_Location",10)

    }

    fun getBitmapFromAssets(fileName: String): Bitmap {
        val assetManager = assets

        val istr = assetManager.open(fileName)

        return BitmapFactory.decodeStream(istr)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates();
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates();
    }

    fun startLocationUpdates(){

        var app : ArrayList<String> = getListOfFakeLocationApps(this);
        if(app.size >0 ){
            AppAlert.getInstance().SystemAlert("Alert!!!","UnAuthorized Location Provider found\n" +
                    "Please UnInstall the infacting application\n" +
                   app.toString(),null);
            finish();
            return
        }

        // start location updates
        FusedLocationSingleton.getInstance().startLocationUpdates()
        // register observer for location updates
        LocalBroadcastManager.getInstance(this@LocationTest).registerReceiver(mLocationUpdated,
                IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE))
    }

    fun stopLocationUpdates(){
        // stop location updates
        FusedLocationSingleton.getInstance().stopLocationUpdates()
        // unregister observer
        LocalBroadcastManager.getInstance(this@LocationTest).unregisterReceiver(mLocationUpdated)
    }

    /***********************************************************************************************
     * local broadcast receiver
     **********************************************************************************************/
    /**
     * handle new location
     */
    private val mLocationUpdated = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            try {
                val location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE) as Location
                LocArr.add(location);
                if (LocArr.size >= LocationTestSize){
                    stopLocationUpdates()

                    var km = customVariableandMethod.DistanceBetween(customVariableandMethod.getObject(context, "LastCallLocation", Location::class.java),location )

                    //var IsLastLocationMadeTwoHoursEarlier = customVariableandMethod.IsLocationOlderThan(context, "LastCallLocation", 2 * 60 * 60 * 1000);
                    var time_difference = customVariableandMethod.GetLocationTimeDifference(customVariableandMethod.getObject(context, "LastCallLocation", Location::class.java))
                    if(time_difference > 2 * 60 * 60 * 1000){
                        var estimated_time_taken=km/1.0;             //1km per min allowed
                        var real_time_taken = (time_difference)/60000
                        if (real_time_taken > estimated_time_taken){
                            SetLocation(location);
                        }else{
                            if (NoOfTry == 0){
                                ShowAlert("Please Switch Off your Gps...")
                            }else{
                                ShowAlert("Please Restart your Mobile...")
                            }
                        }

                    }else {
                        SetLocation(location);
                    }

                    //GetValidLocation()
                }
                var msg : String = "Please Wait";
                for (i in 0 .. LocArr.size -1 ){
                    msg = msg + " ..."
                }
                mPositionContainer!!.text= msg  // "Lat: " + location.getLatitude() + " Lon: " + location.getLongitude() + " Size : " + LocArr.size
            } catch (e: Exception) {
                //
            }

        }
    }

    fun isMockLocationOn( location : Location,  context : Context) : Boolean {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return location.isFromMockProvider();
        } else {
            var mockLocation = "0";
            try {
                mockLocation = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION);
            } catch ( e : Exception) {
                e.printStackTrace();
            }
            return !mockLocation.equals("0");
        }
    }



    fun  getListOfFakeLocationApps( context : Context) : ArrayList<String>{
        var runningApps : ArrayList<String> = getRunningApps(context, false);
        for (i in runningApps.size - 1 downTo 0) {
            val app = runningApps.get(i)
            if(!hasAppPermission(context, app, "android.permission.ACCESS_MOCK_LOCATION")){
                runningApps.removeAt(i)
            }
        }
        return runningApps;
    }

    fun getRunningApps( context : Context,  includeSystem : Boolean) : ArrayList<String> {
         var activityManager : ActivityManager =  context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager;

        var runningApps : ArrayList<String> =  ArrayList();

        var runAppsList : List<ActivityManager.RunningAppProcessInfo> = activityManager.getRunningAppProcesses();
        for ( processInfo : ActivityManager.RunningAppProcessInfo in runAppsList) {
            for ( pkg : String in processInfo.pkgList) {
                runningApps.add(pkg);
            }
        }

        try {
            //can throw securityException at api<18 (maybe need "android.permission.GET_TASKS")
            var runningTasks : List<ActivityManager.RunningTaskInfo> = activityManager.getRunningTasks(1000);
            for (taskInfo : ActivityManager.RunningTaskInfo  in runningTasks) {
                runningApps.add(taskInfo.topActivity.getPackageName());
            }
        } catch ( ex : Exception) {
            ex.printStackTrace();
        }

         var runningServices : List<ActivityManager.RunningServiceInfo> = activityManager.getRunningServices(1000);
        for ( serviceInfo : ActivityManager.RunningServiceInfo in runningServices) {
            runningApps.add(serviceInfo.service.getPackageName());
        }

        runningApps = ArrayList(HashSet(runningApps))

        var allowed_apps = customVariableandMethod.getDataFrom_FMCG_PREFRENCE(context, "ALLOWED_APP", "")
        if(!includeSystem){
            for (i in runningApps.size - 1 downTo 0) {
                val app = runningApps.get(i)
                if (isSystemPackage(context, app) || allowed_apps.contains(app)) {
                    runningApps.removeAt(i)
                }
            }
        }
        return runningApps;
    }

    fun isSystemPackage( context : Context,  app : String) :  Boolean{
        var packageManager : PackageManager = context.getPackageManager();
        try {
            var pkgInfo : PackageInfo = packageManager.getPackageInfo(app, 0);
            return (pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch ( e : PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
        return false;
    }

    fun hasAppPermission( context : Context,  app : String,  permission : String) : Boolean{
        var packageManager : PackageManager = context.getPackageManager();
        var packageInfo : PackageInfo;
        try {
            packageInfo = packageManager.getPackageInfo(app, PackageManager.GET_PERMISSIONS);
            if(packageInfo.requestedPermissions!= null){
                for ( requestedPermission  : String in packageInfo.requestedPermissions) {
                    if (requestedPermission.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch ( e : PackageManager.NameNotFoundException) {
            e.printStackTrace();
        }
        return false;
    }

    private fun GetValidLocation(){
        if (LocArr.size >= LocationTestSize){
            //var DistanceArr : ArrayList<Double> = GetDistanceArray(customVariableandMethod.getObject(context,"currentBestLocation_Validated",Location::class.java))
            //var mean : Double = GetDistanceMean(DistanceArr)

            //customVariableandMethod.msgBox(context,"Please wait....");

            var NoOfValidLocation : Int = 0
            var IsValidLocationArr : ArrayList<Double> = ArrayList()
            for (loc : Location in LocArr) {
                IsValidLocationArr.add(IsValidLocation(loc))
                if(IsValidLocationArr[IsValidLocationArr.size - 1] < 1)
                    NoOfValidLocation ++
            }

            //customVariableandMethod.msgBox(context,"Validating ....");

            if (NoOfValidLocation > (IsValidLocationArr.size /2)){
                // find the mean of location and mark it as validated
                //customVariableandMethod.msgBox(context,"Location Validated....");

                val BestLocation : Location = getBestLocationOf(IsValidLocationArr);
                SetLocation(BestLocation);

            }else {
                // unable to find the valid location
                //switch ON/OFF GPS and Internet and try again
                //customVariableandMethod.msgBox(context,"Location InValidated....");
                if (NoOfTry == 0){
                    ShowAlert("Please Switch Off your Gps...")
                }else{
                    ShowAlert("Please Restart your Mobile...")
                }

            }
        }else{
            customVariableandMethod.msgBox(context,"Insaficient Data.....")
        }

    }

    private fun SetLocation (BestLocation : Location){

        val msg : String = "" +BestLocation.latitude + "," + BestLocation.longitude
        customVariableandMethod.putObject(context,"currentBestLocation_Validated",BestLocation)
        customVariableandMethod.setDataInTo_FMCG_PREFRENCE(context, "shareLatLong", msg)
        customVariableandMethod.setDataInTo_FMCG_PREFRENCE(context, "last_location_update_time_in_minites", customVariableandMethod.get_currentTimeStamp())
        Custom_Variables_And_Method.GLOBAL_LATLON = msg

        val intent = Intent(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE)
        intent.putExtra(Const.LBM_EVENT_LOCATION_UPDATE, BestLocation)
        LocalBroadcastManager.getInstance(MyCustumApplication.getInstance()).sendBroadcast(intent)

        finish()
    }

    private fun getBestLocationOf(NumberArr : ArrayList<Double>) : Location{
        var BestLocation : Location = LocArr[0]
        var LeastNumber : Double = NumberArr[0]
        for ( i in 0 ..NumberArr.size-1) {
            if (NumberArr[i] < LeastNumber) {
                LeastNumber = NumberArr[i]
                BestLocation = LocArr[i]
            }
        }
        return BestLocation
    }

    private fun IsValidLocation(Loc : Location) : Double{
        val DistanceArr : ArrayList<Double> = GetDistanceArray(Loc)
        val mean : Double = GetDistanceMean(DistanceArr)
        return mean //if (mean < 1) 1 else 0 /// valid if mean distance is less than 1km
    }

    private fun GetDistanceArray( LastValidatedLocation : Location) : ArrayList<Double> {
        var DistanceArr : ArrayList<Double> = ArrayList()
       // var LastValidatedLocation = customVariableandMethod.getObject(context,"currentBestLocation_Validated",Location::class.java)
        for (loc : Location in LocArr) {
            DistanceArr.add(DistanceCalculator.distance(loc.latitude, loc.longitude, LastValidatedLocation.getLatitude(), LastValidatedLocation.getLongitude(), "K"))
        }
        return DistanceArr
    }

    private fun GetDistanceMean(DistanceArr : ArrayList<Double> ) : Double {
        var mean : Double = 0.0;
        for (dis: Double in DistanceArr) {
            mean += dis
        }
        return mean / DistanceArr.size
    }

    private fun ShowAlert(msg :String){
        var locationManager : LocationManager = (context as LocationTest).getSystemService(Context.LOCATION_SERVICE) as LocationManager;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {
                            if (NoOfTry ==0 ) {
                                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                                startActivityForResult(intent, GPS_Setting_Enabled)
                            }else{
                                var homeIntent =  Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP;
                                startActivity(homeIntent);
                            }
                        }
                    })
                    /*.setNegativeButton("No", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, id: Int) {
                            dialog.cancel()
                        }
                    })*/
            val alert = builder.create()
            alert.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GPS_Setting_Enabled -> {
                LocArr.clear()
                startLocationUpdates()
                NoOfTry = 1

            }
        }

    }

}
