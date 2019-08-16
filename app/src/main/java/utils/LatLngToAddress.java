package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.cbo.cbomobilereporting.ui_new.Model.mAddress;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.CboProgressDialog;
import com.uenics.javed.CBOLibrary.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cboios on 18/03/19.
 */

public class LatLngToAddress {

    Context context;
    ILatLngToAddress listener;
    mAddress address ;

    public LatLngToAddress(Context context,String LatLong) {
        this.context = context;
        address = new mAddress().setLAT_LONG(LatLong);
    }

    public interface ILatLngToAddress{
        void onSucess(mAddress address);
        void onError(String message);
    }



    public void execute(ILatLngToAddress listener){
        this.listener = listener;
        CBOServices.checkConnection(context, new Response() {
            @Override
            public void onSuccess(Bundle bundle) {
                new getAddress().execute(address);
            }

            @Override
            public void onError(String s, String s1) {
                listener.onError(s1);
            }
        });

    }


    private class getAddress extends AsyncTask<mAddress, String, mAddress> {
        CboProgressDialog cboProgressDialog;

        @Override
        protected final mAddress doInBackground(mAddress... mAddresses) {
            String latLong = "[ERROR]";

            try {
                latLong = "";


                mAddress address = mAddresses[0];
                //URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=b-141,rama%20park,uttam%20anger,%20New%20Delhi,110059&key=AIzaSyB4UzWR8_mncvSPW_CyA485WryqI6Z4D20");
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng="+ Uri.encode(address.getLAT_LONG())+"&key=AIzaSyB4UzWR8_mncvSPW_CyA485WryqI6Z4D20");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setDoOutput(true);
                /*DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                dStream.writeBytes(params); //Writes out the string to the underlying output stream as a sequence of bytes
                dStream.flush(); // Flushes the data output stream.
                dStream.close(); // Closing the output stream.
*/
                int responseCode = connection.getResponseCode();
                StringBuilder responseOutput = new StringBuilder();
                if (responseCode == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));


                    String line = "";

                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();

                    String result = responseOutput.toString();
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i =0 ; i < jsonArray.length() ; i++){
                        JSONArray address_components = jsonArray.getJSONObject(i).getJSONArray("address_components");
                        String formatted_address = jsonArray.getJSONObject(i).getString("formatted_address");
                        if (address.getFORMATED_ADDRESS().length()< formatted_address.length()) {
                            address.setFORMATED_ADDRESS(formatted_address);
                        }
                        for (int j = 0; j < address_components.length(); j++) {
                            JSONObject component = address_components.getJSONObject(j);
                            JSONArray types = component.getJSONArray("types");
                            for (int k = 0; k < types.length(); k++) {
                                switch (types.getString(k)){

                                    case "locality":
                                        if (address.getCITY().trim().isEmpty()){
                                            address.setCITY(component.getString("long_name"));
                                        }
                                        break;
                                    case "administrative_area_level_2":
                                        if (address.getDISTRICT().trim().isEmpty()){
                                            address.setDISTRICT(component.getString("long_name"));
                                        }
                                        break;
                                    case "administrative_area_level_1":
                                        if (address.getSTATE().trim().isEmpty()){
                                            address.setSTATE(component.getString("long_name"));
                                        }
                                        break;
                                    case "postal_code":
                                        if (address.getZIP().trim().isEmpty()){
                                            address.setZIP(component.getString("long_name"));
                                        }
                                        break;
                                    case "country":
                                        if (address.getCOUNTRY().trim().isEmpty()){
                                            address.setCOUNTRY(component.getString("long_name"));
                                        }
                                        break;
                                }

                            }

                        }

                    }

                    return mAddresses[0];

                } else {
                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));


                    String line = "";

                    System.out.println("output===============" + br);
                    while ((line = br.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    br.close();

                    latLong = "[ERROR]"+responseOutput.toString();

                }
            } catch (Exception e) {
                latLong = "[ERROR]"+e.getMessage() ;
            }

            Handler handler = new Handler(Looper.getMainLooper());
            String finalLatLong = latLong;
            handler.post(new Runnable() {
                public void run() {
                    listener.onError(finalLatLong.replace("[ERROR]",""));

                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cboProgressDialog = new CboProgressDialog(context, "Please Wait\n" +"Scanning Location....");
            cboProgressDialog.show();
        }

        @Override
        protected void onPostExecute(mAddress address) {
            super.onPostExecute(address);
            cboProgressDialog.dismiss();
            if (address != null){
                listener.onSucess(address);
            }

        }
    }


  /*  function parseGeoLocationResults(result) {
    const parsedResult = {}
    const {address_components} = result;

        for (var i = 0; i < address_components.length; i++) {
            for (var b = 0; b < address_components[i].types.length; b++) {
                if (address_components[i].types[b] == "street_number") {
                    //this is the object you are looking for
                    parsedResult.street_number = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "route") {
                    //this is the object you are looking for
                    parsedResult.street_name = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "sublocality_level_1") {
                    //this is the object you are looking for
                    parsedResult.sublocality_level_1 = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "sublocality_level_2") {
                    //this is the object you are looking for
                    parsedResult.sublocality_level_2 = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "sublocality_level_3") {
                    //this is the object you are looking for
                    parsedResult.sublocality_level_3 = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "neighborhood") {
                    //this is the object you are looking for
                    parsedResult.neighborhood = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "locality") {
                    //this is the object you are looking for
                    parsedResult.city = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "administrative_area_level_1") {
                    //this is the object you are looking for
                    parsedResult.state = address_components[i].long_name;
                    break;
                }

                else if (address_components[i].types[b] == "postal_code") {
                    //this is the object you are looking for
                    parsedResult.zip = address_components[i].long_name;
                    break;
                }
                else if (address_components[i].types[b] == "country") {
                    //this is the object you are looking for
                    parsedResult.country = address_components[i].long_name;
                    break;
                }
            }
        }
        return parsedResult;
    }*/
}
