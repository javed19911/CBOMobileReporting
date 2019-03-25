package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
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
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 03/12/18.
 */

public class AddressToLatLong {

    Context context;
    String address;
    IAddressToLatLong listener;

    public AddressToLatLong(Context context,String address) {
        this.context = context;
        this.address = address;
    }

    public interface IAddressToLatLong{
        void onSucess(mAddress address);
        void onError(String message);
    }



    public void execute(IAddressToLatLong listener){
        this.listener = listener;
        CBOServices.checkConnection(context, new Response() {
            @Override
            public void onSuccess(Bundle bundle) {
                new getLatLong().execute(address);
            }

            @Override
            public void onError(String s, String s1) {
                listener.onError(s1);
            }
        });

    }


    private class getLatLong extends AsyncTask<String, String, mAddress> {
        CboProgressDialog cboProgressDialog;

        @Override
        protected final mAddress doInBackground(String... params) {
            String latLong = "[ERROR]";

            mAddress maddress = new mAddress();

            try {
                latLong = "";


                //URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=b-141,rama%20park,uttam%20anger,%20New%20Delhi,110059&key=AIzaSyB4UzWR8_mncvSPW_CyA485WryqI6Z4D20");
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+ Uri.encode(params[0])+"&key=AIzaSyB4UzWR8_mncvSPW_CyA485WryqI6Z4D20");
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
                        for (int j = 0; j < address_components.length(); j++) {
                            JSONObject component = address_components.getJSONObject(j);
                            JSONArray types = component.getJSONArray("types");
                            for (int k = 0; k < types.length(); k++) {
                                switch (types.getString(k)){

                                    case "locality":
                                        if (maddress.getCITY().trim().isEmpty()){
                                            maddress.setCITY(component.getString("long_name"));
                                        }
                                        break;
                                    case "administrative_area_level_2":
                                        if (maddress.getDISTRICT().trim().isEmpty()){
                                            maddress.setDISTRICT(component.getString("long_name"));
                                        }
                                        break;
                                    case "administrative_area_level_1":
                                        if (maddress.getSTATE().trim().isEmpty()){
                                            maddress.setSTATE(component.getString("long_name"));
                                        }
                                        break;
                                    case "postal_code":
                                        if (maddress.getZIP().trim().isEmpty()){
                                            maddress.setZIP(component.getString("long_name"));
                                        }
                                        break;
                                    case "country":
                                        if (maddress.getCOUNTRY().trim().isEmpty()){
                                            maddress.setCOUNTRY(component.getString("long_name"));
                                        }
                                        break;
                                }

                            }

                        }

                    }

                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    maddress.setFORMATED_ADDRESS(jsonObject1.getString("formatted_address"));


                    latLong = jsonObject1.getJSONObject("geometry").getJSONObject("location").getString("lat")+
                    ","+jsonObject1.getJSONObject("geometry").getJSONObject("location").getString("lng");

                    maddress.setLAT_LONG(latLong);
                    return maddress;

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
}
