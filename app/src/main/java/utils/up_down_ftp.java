package utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

/**
 * Created by pc24 on 06/12/2016.
 */

public class up_down_ftp {
    public boolean status = false;
    FTPClient mFtpClient;
    String ftp_username="";
    String ftp_hostname="";
    String ftp_password="";
    String web_root_path="";
    String ftp_port="";

    CBO_DB_Helper cbohelp;
    //Shareclass shareclass;
    private AdapterCallback mAdapterCallback;


    public up_down_ftp(){

    }

    private void connnectingwithFTP(String ip, String userName, String pass,final Context context) {

        try {
            mFtpClient = new FTPClient();
            mFtpClient.connect(ip);
            mFtpClient.login(userName, pass);
            status=true;
            Log.e("isFTPConnected", String.valueOf(status));
            mFtpClient.setType(FTPClient.TYPE_BINARY);

            //mFtpClient.changeDirectory("//DEMO/upload/");
            mFtpClient.changeDirectory(web_root_path);


        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FTPException e) {
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
        }
    }


    public boolean downloadSingleFile(final String remoteFilePath,final  File downloadFile,final Context context) {

        final Boolean[] result = {false};
        Runnable runnable = new Runnable() {
            public void run() {
                //shareclass=new Shareclass();
                cbohelp=new CBO_DB_Helper(context);
                connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context);
                File parentDir = downloadFile.getParentFile();
                if (!parentDir.exists())
                    parentDir.mkdir();
                try {

                    mFtpClient.download(remoteFilePath, downloadFile);
                    mFtpClient.disconnect(true);
                    result[0] =true;
                   /* if(shareclass.getValue(context,"Chat","InActive").equals("Active")){
                        Handler handler = new Handler(Looper.getMainLooper());

                        handler.post(new Runnable() {

                            @Override
                            public void run() {
                                try {
                                    mAdapterCallback = ((AdapterCallback) context);
                                    mAdapterCallback. updateAdaptor();
                                } catch (ClassCastException e) {
                                    throw new ClassCastException("Activity must implement AdapterCallback.");
                                }

                            }
                        });
                    }*/
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };
        Thread mythread1 = new Thread(runnable);
        mythread1.start();
        return result[0];
    }

    public static interface AdapterCallback {
        void  updateAdaptor();
    }

    public void uploadFile( final File uploadFile,final Context context) {
        Runnable runnable = new Runnable() {

            public void run() {
              //  shareclass=new Shareclass();
                cbohelp=new CBO_DB_Helper(context);
                getFtpDetail();
                //connnectingwithFTP("220.158.164.114", "CBO_DOMAIN_SERVER", "cbodomain@321");
                connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context);
                try {

                    mFtpClient.upload(uploadFile);
                    mFtpClient.disconnect(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        final Thread mythread1 = new Thread(runnable);

      /*  Runnable checkConection = new Runnable() {

            @Override
            public void run() {
               // boolean[] connection= CboServices.isConnectingToInternet(context);
                if(connection[0] && connection[1]){
                    mythread1.start();
                }else if(!connection[0]){
                    //not connected to internet
                    //CboServices.threadErrorMsg("Please connect to internet");
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(context,"please_connect_to_internet",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    //connection time out
                    //CboServices.threadErrorMsg("Your Conection is too Slow");
                    Handler handler = new Handler(Looper.getMainLooper());

                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            Toast.makeText(context,"Your Conection is too Slow",Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        };*/

        Thread mythread2 = new Thread(mythread1);
        mythread2.start();
    }


    public void getFtpDetail()
    {
        Cursor c=cbohelp.getFTPDATA();
        if(c.moveToFirst())
        {
            do{

                ftp_hostname=c.getString(c.getColumnIndex("ftpip"));
                ftp_username=c.getString(c.getColumnIndex("username"));
                ftp_password=c.getString(c.getColumnIndex("password"));
                ftp_port=c.getString(c.getColumnIndex("port"));
                web_root_path=c.getString(c.getColumnIndex("path"));
            }while(c.moveToNext());
        }
        //port=Integer.parseInt(ftp_port);
        // c.close();
    }
}
