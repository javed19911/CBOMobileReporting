package utils.adapterutils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.DistanceCalculator;

import java.util.ArrayList;

import utils_new.Custom_Variables_And_Method;


public class SpinAdapter_new extends ArrayAdapter<String>{
	private Context activity;
    private ArrayList data;
    public Resources res;
    SpinnerModel tempValues=null;
    LayoutInflater inflater;
    Custom_Variables_And_Method customVariablesAndMethod;
    int show;
    public  String latLong = "";
    String DR_COLOR ="";
   // public  String ref_latLong = "";

    public SpinAdapter_new(Context activitySpinner, int textViewResourceId, ArrayList objects,int show)
    {
        super(activitySpinner, textViewResourceId, objects);
        super.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        /********** Take passed values **********/
        activity = activitySpinner;
        data     = objects;
        this.show=show;
        if (MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()){
            this.show=0;
        }
        DR_COLOR = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(getContext(),"DR_COLOR","");


        /***********  Layout inflator to call external xml layout () **********************/
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	  }
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

    	/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
        View row = inflater.inflate(R.layout.spin_row, parent, false);
        
        /***** Get each Model object from Arraylist ********/
        tempValues = null;
        tempValues = (SpinnerModel) data.get(position);
        
        TextView label        = (TextView)row.findViewById(R.id.spin_name);
        TextView sub          = (TextView)row.findViewById(R.id.spin_id);

        TextView distance     = (TextView)row.findViewById(R.id.distance);
        ImageView loc_icon    = (ImageView)row.findViewById(R.id.loc_icon);

        LinearLayout distanceLayout = row.findViewById(R.id.distanceLayout);
        
        
        {
            // Set values for spinner each row
            try {


                latLong = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(activity,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
                label.setText(tempValues.getName());
                sub.setText(tempValues.getId());
                if(!tempValues.getAPP_PENDING_YN().equals("0")){
                    loc_icon.setVisibility(View.GONE);
                    distance.setText("Additional Area Approval pending...");
                    distance.setBackgroundColor(0xffE2571F);
                }else if(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(activity,"GEO_FANCING_KM","0").equals("0") || show==0){
                    loc_icon.setVisibility(View.GONE);
                    distance.setVisibility(View.GONE);
                }else if(tempValues.getLoc().equals("")){
                    loc_icon.setVisibility(View.GONE);
                    distance.setText("Registration pending...");
                    distance.setBackgroundColor(0xffE2571F);
                }else{
                    loc_icon.setVisibility(View.GONE);
                    Double km1,km2=-1.0,km3=-1.0;
                    km1= DistanceCalculator.distance(Double.valueOf(tempValues.getLoc().split(",")[0]), Double.valueOf(tempValues.getLoc().split(",")[1])
                            ,  Double.valueOf(latLong.split(",")[0]), Double.valueOf(latLong.split(",")[1]), "K");

                    if (!tempValues.getLoc2().equals("")) {
                        km2 = DistanceCalculator.distance(Double.valueOf(tempValues.getLoc2().split(",")[0]), Double.valueOf(tempValues.getLoc2().split(",")[1])
                                , Double.valueOf(latLong.split(",")[0]), Double.valueOf(latLong.split(",")[1]), "K");
                    }else{
                        km2=km1;
                    }

                    if (!tempValues.getLoc3().equals("")) {
                        km3 = DistanceCalculator.distance(Double.valueOf(tempValues.getLoc3().split(",")[0]), Double.valueOf(tempValues.getLoc3().split(",")[1])
                                , Double.valueOf(latLong.split(",")[0]), Double.valueOf(latLong.split(",")[1]), "K");
                    }else{
                        km3=km1;
                    }

                    String geo_fancing_km=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(activity,"GEO_FANCING_KM","0");

                    Double km=getShortestDistance(km1,km2,km3);

                    if (km>Double.valueOf(geo_fancing_km)){
                        loc_icon.setVisibility(View.VISIBLE);
                        distance.setText(String.format("%.2f", km) +" Km Away");
                        distance.setBackgroundColor(0xffE2921F);
                    }else{
                        distance.setText("Within Range");
                    }

                }


                if (tempValues.getCOLORYN().equals("0")){
                    label.setTextColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));
                }else{
                    if(DR_COLOR.equals("")) {
                        label.setTextColor(0xFFF9BA22);
                    }else{
                        label.setTextColor(Color.parseColor(DR_COLOR));
                    }
                }



                if (tempValues.getCALLYN().equals("0")){
                    //row.setBackgroundColor(0xFFFFFFFF);
                    if (tempValues.getAPP_PENDING_YN().equals("0")){
                        row.setBackgroundColor(0xFFFFFFFF);
                        if (show == 0){
                            distance.setVisibility(View.GONE);
                        }
                    }else{
                        distance.setText("Additional Area Approval pending...");
                        distance.setBackgroundColor(0xffE2571F);
                        row.setBackgroundColor(0xffE2571F);
                        label.setTextColor(0xFFFFFFFF);
                        distance.setVisibility(View.VISIBLE);
                    }
                }else{
                    distance.setText("Call Done.");
                    distance.setBackgroundColor(0xFFC0C0C0);
                    row.setBackgroundColor(0xFFC0C0C0);
                }


                if (!tempValues.getPANE_TYPE().equals("1")) {
                    //row.setBackgroundResource(R.color.colorPrimaryDark);
                    if (tempValues.getCOLORYN().equals("0")) {
                        label.setTextColor(0xFF187823);
                    } else{
                        if(DR_COLOR.equals("")) {
                            label.setTextColor(0xFFF9BA22);
                        }else{
                            label.setTextColor(Color.parseColor(DR_COLOR));
                        }
                    }
                }/*else{
                    //row.setBackgroundResource(R.color.colorPrimaryDark);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        label.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark,null));
                    }else{
                        label.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                    }
                    //label.setTextColor(0xFF000000);
                }*/

                distanceLayout.setTag(position);

                if (loc_icon.getVisibility() == View.GONE && distance.getVisibility() == View.GONE ) {
                    distanceLayout.setVisibility(View.GONE);
                }else{
                    distanceLayout.setVisibility(View.VISIBLE);
                }

                distanceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (loc_icon.getVisibility() == View.VISIBLE) {
                            SpinnerModel tempValues1 = (SpinnerModel) data.get((Integer) distanceLayout.getTag());
                            //+ "&mode=d"
                            Uri gmmIntentUri = Uri.parse("google.navigation:q=" + tempValues1.getREF_LAT_LONG() );
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            activity.startActivity(mapIntent);

                            /*d for driving
                            w for walking
                            b for bicycling*/
                        }

                    }
                });

            }catch(Exception e){
                e.printStackTrace();
            }

        }   

        return row;
      }

      private Double getShortestDistance(Double km1,Double km2,Double km3){

          if ((km2==-1.0 && km3==-1.0) || ( km3==-1.0 && km1<=km2 ) || ( km2==-1.0 && km1<=km3 ) || (km1<=km2 && km1<=km3) ) {
              tempValues.setREF_LAT_LONG( tempValues.getLoc());
              return km1;
          }if ((km3==-1.0) || (km2<=km3 && km2!=-1.0)){
              tempValues.setREF_LAT_LONG( tempValues.getLoc2());
              return km2;
          }

          tempValues.setREF_LAT_LONG( tempValues.getLoc3());
          return km3;
      }

}
