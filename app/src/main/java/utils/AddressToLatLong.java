package utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

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
        void onSucess(String LatLong);
        void onError(String message);
    }



    public void execute(IAddressToLatLong listener){
        this.listener = listener;
        new getLatLong().execute(address);
    }


    private class getLatLong extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected final String doInBackground(String... params) {
            String latLong = "[ERROR]";
            try {
                latLong = "";


                //URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=b-141,rama%20park,uttam%20anger,%20New%20Delhi,110059&key=AIzaSyB4UzWR8_mncvSPW_CyA485WryqI6Z4D20");
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address="+ Uri.encode(address)+"&key=AIzaSyB4UzWR8_mncvSPW_CyA485WryqI6Z4D20");
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
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    latLong = jsonObject1.getJSONObject("geometry").getJSONObject("location").getString("lat")+
                    ","+jsonObject1.getJSONObject("geometry").getJSONObject("location").getString("lng");

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
            return latLong;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Processing......." + "\n" + "please wait");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(String latLong) {
            super.onPostExecute(latLong);
            pd.dismiss();
            if (!latLong.contains("[ERROR]")) {

                try {
                    pd.dismiss();
                    listener.onSucess(latLong);

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(e.getMessage());
                }
            }else{
                listener.onError(latLong.replace("[ERROR]",""));
            }
        }
    }
}
