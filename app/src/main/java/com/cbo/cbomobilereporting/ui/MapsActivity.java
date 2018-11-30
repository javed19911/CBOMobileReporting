package com.cbo.cbomobilereporting.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import services.ServiceHandler;
import utils.MyConnection;
import utils_new.Custom_Variables_And_Method;

public class MapsActivity extends Activity implements OnMapReadyCallback {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    String paid ;
    String date ;
    String companyCode ;
    ServiceHandler myService;


    Custom_Variables_And_Method customVariablesAndMethod;

    CBO_DB_Helper myDb;
    Context context;
    ArrayList<String> pa_name = new ArrayList<String>();
    ArrayList<String> pa_latLong = new ArrayList<String>();
    ArrayList<String> pa_add = new ArrayList<String>();
    ArrayList<String> pa_time = new ArrayList<String>();
    String man_Id,div_Id,ak_DateMMDDYY,setSatelite;
    TextView emp_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        context = MapsActivity.this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        myDb = new CBO_DB_Helper(context);
        paid = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"FM_PA_ID");
        date = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"dcr_date_real");
        companyCode = myDb.getCompanyCode();
        Bundle bundle = getIntent().getExtras();
        emp_num = (TextView) findViewById(R.id.number_of_emp);

        man_Id = bundle.getString("userId_man");
        div_Id = bundle.getString("userId_div");
        ak_DateMMDDYY = bundle.getString("ak_DateMMDDYY");
        setSatelite = bundle.getString("akCheckMap");

        myService = new ServiceHandler(context);
        new DoBack().execute();


       // new DoBack().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // setUpMapIfNeeded();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap=map;
        setUpMap();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {

            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map_activity);
            mapFragment.getMapAsync(this);
            //mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map_activity)).getMap();

            if (mMap != null) {
                setUpMap();
            }
        }
    }






    private void setUpMap() {

            if (setSatelite.equalsIgnoreCase("true")){

                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            }else {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                //  mMap.getUiSettings().setZoomGesturesEnabled(true);
            }
                    double lat=0.0,lon=0.0;
                    String loc_cord[]=pa_latLong.get(0).split(",");
                    String cord1=loc_cord[0];
                    String cord2=loc_cord[1];
                    lat= Double.parseDouble(cord1);
                    lon= Double.parseDouble(cord2);

                   CameraPosition cameraPosition = new CameraPosition.Builder().target(
                           new LatLng(lat,lon)).zoom(9).build();



                   mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    for (int i =0; i<pa_add.size();i++) {

                        double lat1=0.0,lon1=0.0;
                        String loc_cord1[]=pa_latLong.get(i).split(",");
                        String cord3=loc_cord1[0];
                        String cord4=loc_cord1[1];
                        lat1= Double.parseDouble(cord3);
                        lon1= Double.parseDouble(cord4);

                        mMap.addMarker(new MarkerOptions().position(new LatLng(lat1,lon1)).title(pa_name.get(i)).snippet("TIme:"+pa_time.get(i)+","+pa_add.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ak2)));

                    }
                    /*CameraPosition cameraPosition = new CameraPosition.Builder().target(
                            new LatLng(22.926909,78.599664)).zoom(12).build();

                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/


    }

    class DoBack extends AsyncTask<String,String,String> {
                ProgressDialog pd = new ProgressDialog(MapsActivity.this);
                @Override
                protected String doInBackground(String... params) {

                   // setUpMapIfNeeded();
                  String result =  myService.getResponseMapDrCallMobile(companyCode,man_Id,ak_DateMMDDYY,div_Id);

                    return result;
                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if ((s.equals(""))||(s.equals(null))||(s.contains("ERROR"))){
                        customVariablesAndMethod.msgBox(context, "Nothing Found");
                    }else {
                         try {
                             JSONObject jsonObject = new JSONObject(s);
                             JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                             for (int i =0; i<jsonArray.length();i++){
                                 JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                 pa_name.add(jsonObject1.getString("PA_NAME"));
                                 pa_add.add(jsonObject1.getString("LOC1"));
                                 pa_latLong.add(jsonObject1.getString("LATLONG"));
                                 pa_time.add(jsonObject1.getString("IN_TIME"));

                             }

                         }catch (JSONException json){

                             customVariablesAndMethod.msgBox(context, "Somthing Wrong");

                         }
                        if (pa_latLong.size() >0){
                             emp_num.setText(""+pa_name.size());
                            setUpMapIfNeeded();
                        }
                        else {
                            customVariablesAndMethod.msgBox(context,"NoBody Found try After Some time .......");

                            Intent i = new Intent(context,ViewPager_2016.class);

                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            i.putExtra("Id", Custom_Variables_And_Method.CURRENTTAB);

                            startActivity(i);

                        }


                    }
                       pd.dismiss();
                }

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pd.setCancelable(false);
                    pd.setTitle("CBO");
                    pd.setMessage("Processing.....");
                    pd.setCanceledOnTouchOutside(false);
                    pd.show();

                }
    }
}
