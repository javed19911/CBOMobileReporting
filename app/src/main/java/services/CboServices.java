package services;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import utils_new.Custom_Variables_And_Method;
import utils_new.SendMailTask;


/**
 * Created by pc24 on 01/12/2016.
 */

public class CboServices {

    private static final String NAMESPACE = "http://tempuri.org/";
    private static String URL= "http://www.cboservices.com/mobilerpt.asmx";
    private final Handler h1;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;

   /* Boolean ShowGPS_timer=false;
    GPS_Timmer_Dialog dilog;*/
   StringBuilder urlParameters = new StringBuilder();



    public CboServices(Context context, Handler hh) {
        h1 = hh;
        this.context=context;
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        URL= customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"WEBSERVICE_URL",URL);
        if(URL.equals("")){
            URL= "http://www.cboservices.com/mobilerpt.asmx";
        }
        //ShowGPS_timer=false;
        //dilog=new GPS_Timmer_Dialog(context);
    }

   /* public CboServices(Context context, Handler hh,Boolean ShowGPS_timer) {
        h1 = hh;
        this.context=context;
        URL= Constants.getMainUrl(context);
        this.ShowGPS_timer=ShowGPS_timer;
        if (ShowGPS_timer){
            dilog=new GPS_Timmer_Dialog(context);
            dilog.show();
        }
    }*/




    public void customMethodForAllServices(final HashMap<String,String> data, final String methodName, final Integer response_code, final ArrayList<Integer> table) {
        Runnable runnable = new Runnable() {

            public void run() {

                /*if (dilog != null) {
                    while (dilog.isShowing()) ;
                }*/

                //HTTP POST METHOD-------------------------------

                for (int service_try = 0; service_try < 2; service_try++) {
                    try {
                        URL url = new URL(URL + "/" + methodName);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                        connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        connection.setDoOutput(true);

                        urlParameters = new StringBuilder();
                        for (String key : data.keySet()) {
                            String values = data.get(key) == null ? "" : data.get(key);
                            urlParameters.append(key).append("=").append(values.replace("&", "")).append("&");
                        }

                        String ALLOWED_URI_CHARS = "@#&=*-_.,:!?()/~'%";
                        String params = Uri.encode(urlParameters.toString(), ALLOWED_URI_CHARS);
                        //String params= URLEncoder.encode(urlParameters.toString(), "UTF-8");
                        if (params.length() > 1) {
                            params = params.substring(0, params.length() - 1);
                        }
                        int no_of_try = 0;
                        try {
                            for (no_of_try = 1; no_of_try <= 3; no_of_try++) {
                                DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                                dStream.writeBytes(params); //Writes out the string to the underlying output stream as a sequence of bytes
                                dStream.flush(); // Flushes the data output stream.
                                dStream.close(); // Closing the output stream.

                                int responseCode = connection.getResponseCode(); // getting the response code
                           /* final StringBuilder output = new StringBuilder("Request URL " + url);
                            output.append(System.getProperty("line.separatoR") + "Request Parameters " + params);
                            output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);*/
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
                                    result = result.replace("<?xml version=\"1.0\" encoding=\"utf-8\"?><string xmlns=\"http://tempuri.org/\">", "").replace("</string>", "").replace("&amp;", "&").replace("&lt;", "<").replace("&gt;", ">").replace("&quote;", "\"");


                                    threadMsg(result, response_code, table, methodName);
                                } else {
                                    BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));


                                    String line = "";

                                    System.out.println("output===============" + br);
                                    while ((line = br.readLine()) != null) {
                                        responseOutput.append(line);
                                    }
                                    br.close();

                                    //responseOutput.append(connection.getResponseMessage());
                                    threadMsg("[ERROR] " + methodName + " " + responseOutput.toString(), response_code, table, methodName);
                                }
                                // output.append(System.getProperty("line.separator") + "Response " + System.getProperty("");
                                service_try = 1;
                                break;
                            }
                        } catch (SocketTimeoutException e1) {
                            if (no_of_try == 3) {
                                threadMsg("[ERROR] " + "service  method " + methodName + " " + e1.toString(), response_code, table, methodName);
                            } else {
                                try {
                                    if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {
                                        URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                                        url = new URL(URL + "/" + methodName);
                                        connection = (HttpURLConnection) url.openConnection();
                                        connection.setRequestMethod("POST");
                                        connection.setRequestProperty("USER-AGENT", "Mozilla/5.0");
                                        connection.setRequestProperty("ACCEPT-LANGUAGE", "en-US,en;0.5");
                                        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                                        connection.setDoOutput(true);
                                    }

                                } catch (ProtocolException e) {
                                    // PROTOCOL EXCEPTION
                                    threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                                    e.printStackTrace();
                                    no_of_try = 4;

                                } catch (MalformedURLException e) {
                                    //URL CONVERT Exception
                                    e.printStackTrace();
                                    threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                                    no_of_try = 4;
                                } catch (IOException e) {
                                    //OPEN EXCEPTION
                                    e.printStackTrace();
                                    threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                                    no_of_try = 4;
                                }
                            }
                        }
                    } catch (ProtocolException e) {
                        // PROTOCOL EXCEPTION
                        if (service_try == 1) {
                            threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                        } else if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {

                            URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                        }


                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        //URL CONVERT Exception
                        if (service_try == 1) {
                            threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                        } else if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {

                            URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                        }
                        e.printStackTrace();

                    } catch (IOException e) {
                        //OPEN EXCEPTION
                        if (service_try == 1) {
                            threadMsg("[ERROR] " + "service  method " + methodName + " " + e.toString(), response_code, table, methodName);
                        } else if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", "").equals("")) {

                            URL = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "WEBSERVICE_URL_ALTERNATE", URL);
                        }
                        e.printStackTrace();

                    }
                }
            }



        };
        final Thread mythread1 = new Thread(runnable);
        mythread1.start();

        /*Runnable checkConection = new Runnable() {

            @Override
            public void run() {
                boolean[] connection=isConnectingToInternet(context);
                if(connection[0] && connection[1]){
                    mythread1.start();
                }else if(!connection[0]){
                    //not connected to internet
                    threadErrorMsg("Please connect to internet");
                }else{
                    //connection time out
                    threadErrorMsg("Your Conection is too Slow \n Unable to connect to Server \n Please Try again");
                }
            }
        };

        Thread mythread2 = new Thread(checkConection);
        mythread2.start();*/

    }

    private void threadErrorMsg(String errorMsg) {
        Message msgObj = h1.obtainMessage(99);
        Bundle b = new Bundle();
        b.putString("Error",errorMsg);
        msgObj.setData(b);
        h1.sendMessage(msgObj);
    }

    private void threadMsg(final String result,Integer response_code,ArrayList<Integer> tables,final String methodName) {
        if(tables.size()==0){
            /*Message msgObj = h1.obtainMessage(100);
            Bundle b = new Bundle();
            b.putString("Error","Empty Table");
            msgObj.setData(b);
            h1.sendMessage(msgObj);*/
        }else if(result!=null && !result.toUpperCase().contains("[ERROR]")) {


            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("Tables");

                Message msgObj = h1.obtainMessage(response_code);
                Bundle b = new Bundle();
                if (tables.get(0)==-2){
                    tables.set(0,-1);
                }
                if (tables.get(0)==-1){
                    tables.remove(0);
                    for (int j=0;j<jsonArray.length();j++){
                        tables.add(j);
                    }

                }
                for(int i:tables){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables"+i);
                   // Log.d("MYAPP", "objects are: " + jsonArray1.toString());

                    b.putString("Tables"+i,jsonArray1.toString());

                }
                Log.d("MYAPP", "result: " + result);
                msgObj.setData(b);
                h1.sendMessage(msgObj);
            } catch (final JSONException e) {
                Log.d("MYAPP", "error are: " + e.toString());

                threadErrorMsg("Technical Error");

                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        getAlert(context,"Missing table error",e.toString());
                        String subject="";
                        if (e.toString().length()>22  && e.toString().length()>=80){
                            subject=e.toString().substring(22,80);
                        }else if (e.toString().length()>22){
                            subject=e.toString().substring(22);
                        }else{
                            subject=e.toString();
                        }
                        List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
                        /*new SendMailTask().execute("mobileapperror@gmail.com",
                                "Cbo12345",toEmailList , "Missing table error", e.toString());

                        List toEmailList = Arrays.asList("mobileapperror@gmail.com".split("\\s*,\\s*"));*/
                        new SendMailTask().execute("mobilereporting@cboinfotech.com",
                                "mreporting",toEmailList , Custom_Variables_And_Method.COMPANY_CODE+": "+subject,context.getResources().getString(R.string.app_name)+"\n Company Code :"+Custom_Variables_And_Method.COMPANY_CODE+"\n DCR ID :"+Custom_Variables_And_Method.DCR_ID+"\n PA ID : "+Custom_Variables_And_Method.PA_ID+"\n App version : "+Custom_Variables_And_Method.VERSION+"\n methodName : "+methodName+"\n Error Alert :"+ "Missing table error"+"\n"+ e.toString());

                    }
                });
            }


        }else{
            if (tables.get(0)!=-2) {
                Log.d("MYAPP", "error are: " + result);
                threadErrorMsg("Something Went Wrong");
                assert result != null;
                final String title;
                final String message;
                if (result.contains("service  method")) {
                    title = "Internet Error";
                    message = context.getResources().getString(R.string.Internet_error);
                } else {
                    title = "Server Error";
                    message = context.getResources().getString(R.string.try_later);
                }
                Handler handler = new Handler(Looper.getMainLooper());

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        getAlert(context, title, message);
                        String subject = "";
                        if (result.length() > 22 && result.length() >= 80) {
                            subject = result.substring(22, 80);
                        } else if (result.length() > 22) {
                            subject = result.substring(22);
                        } else {
                            subject = result;
                        }
                        List toEmailList = Arrays.asList("mobilereporting@cboinfotech.com".split("\\s*,\\s*"));
                        new SendMailTask().execute("mobilereporting@cboinfotech.com",
                                "mreporting", toEmailList, Custom_Variables_And_Method.COMPANY_CODE + ": " + subject, context.getResources().getString(R.string.app_name) + "\n Company Code :" + Custom_Variables_And_Method.COMPANY_CODE + "\n DCR ID :" + Custom_Variables_And_Method.DCR_ID + "\n PA ID : " + Custom_Variables_And_Method.PA_ID + "\n App version : " + Custom_Variables_And_Method.VERSION + "\n Web Services : " + URL + "\n methodName : " + methodName + "\n Error Alert :" + title + "\n" + "\n urlParameters :" + urlParameters.toString() + "\n" + result);

                  /*  //getAlert(context, "Service error", result);
                    List toEmailList = Arrays.asList("mobileapperror@gmail.com".split("\\s*,\\s*"));
                    new SendMailTask().execute("mobileapperror@gmail.com",
                    "Cbo12345",toEmailList , title, result);*/
                    }
                });
            }
        }
    }

    public void getImage(String image_url,Integer response_code) {

        InputStream in;
        Bitmap myBitmap=null;
        HttpURLConnection connection = null;

        try {
            java.net.URL url = new URL(image_url);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(in);
            connection.disconnect();

            Message msgObj = h1.obtainMessage(response_code);
            Bundle b = new Bundle();

            b.putParcelable("BitImage",myBitmap);

            msgObj.setData(b);
            h1.sendMessage(msgObj);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean[] isConnectingToInternet(Context mContext) {
        final boolean[] result={false,false};
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Network[] networks = connectivityManager.getAllNetworks();
            NetworkInfo networkInfo;
            for (Network mNetwork : networks) {
                networkInfo = connectivityManager.getNetworkInfo(mNetwork);
                if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                    result[0]=true;
                    result[1]=hostAvailable("www.google.com", 80,mContext);
                    return result;
                }
            }
        }else {
            if (connectivityManager != null) {
                //noinspection deprecation
                NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
                if (info != null) {
                    for (NetworkInfo anInfo : info) {
                        if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                            result[0]=true;
                            result[1]=hostAvailable("www.google.com", 80,mContext);
                            return result;
                        }
                    }
                }
            }
        }
//        Toast.makeText(mContext,"please_connect_to_internet",Toast.LENGTH_SHORT).show();
        return result;
    }




        private static boolean hostAvailable(final String host, final int port,final Context mContext) {

                    try {
                        Socket socket = new Socket();
                        socket.connect(new InetSocketAddress(host, port), 2000);
                        socket.close();

                        return true;
                    } catch (IOException e) {
                        // Either we have a timeout or unreachable host or failed DNS lookup

                        //Toast.makeText(mContext, "Your Conection is too Slow", Toast.LENGTH_SHORT).show();

                    }
           return false;
    }

    public static void getAlert(Context context, String title, String messege) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);
        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);
        Alert_message.setText(messege);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+Custom_Variables_And_Method.PA_ID);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);
        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

}
