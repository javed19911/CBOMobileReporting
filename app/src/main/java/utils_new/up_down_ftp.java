package utils_new;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.ViewPager_2016;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.Upload_Photo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;
import services.CboServices;

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
    String ftp_port="21";
    Integer responseCode = 0;

    CBO_DB_Helper cbohelp;
    //Shareclass shareclass;
    private AdapterCallback mAdapterCallback;
    long file_size=0;
    float file_uploaded=0.0f;
    Boolean IsLastFile = true;


    public up_down_ftp(){

    }

    private Boolean connnectingwithFTP(String ip, String userName, String pass,final Context context) {

        try {
            mFtpClient = new FTPClient();
            //mFtpClient.setAutoNoopTimeout(5000);
            mFtpClient.connect(ip,Integer.parseInt(ftp_port));
            mFtpClient.login(userName, pass);
            status=true;
            Log.e("isFTPConnected", String.valueOf(status));
            mFtpClient.setType(FTPClient.TYPE_BINARY);

            //mFtpClient.changeDirectory("//DEMO/upload/");
            mFtpClient.changeDirectory(web_root_path);

            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                    }
                });


            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                    }
                });

            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                    }
                });

            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (FTPException e) {

            try {
                mFtpClient.createDirectory(web_root_path);
                mFtpClient.changeDirectory(web_root_path);
                return true;
            } catch (IOException | FTPException | FTPIllegalReplyException e1) {
                e1.printStackTrace();

                try {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            mAdapterCallback.failed(responseCode,"Folder not found!!!",web_root_path + "   Invalid path \nPlease contact your administrator");// upload_complete("ERROR@"+web_root_path);
                        }
                    });

                } catch (ClassCastException e2) {
                    throw new ClassCastException("Activity must implement AdapterCallback.");
                }

            }

            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                    }
                });

            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        }
        return false;
    }

    public void download_visual_aids(Context context,ArrayList file_list){

        cbohelp=new CBO_DB_Helper(context);
        getFtpDetail();
        mAdapterCallback = ((AdapterCallback) context);
        if(connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context)) {
            try {

               // mAdapterCallback = ((AdapterCallback) context);
                if (file_list.contains("CATALOG")) {
                    download_Directory(context);
                } else {

                    mFtpClient.changeDirectoryUp();
                    mFtpClient.changeDirectory(mFtpClient.currentDirectory() + "/visualaid");
                    String[] files = mFtpClient.listNames();
                    ArrayList<String> file_name = new ArrayList<>();
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" + File.separator);
                    file2.mkdirs();
                    for (int i = 0; i < files.length; i++) {

                        if (files[i].contains(".") && file_list.contains(files[i].substring(0, files[i].lastIndexOf(".")))) {
                            file_name.add(files[i]);
                        }else if(files[i].contains("_") && file_list.contains(files[i].substring(0, files[i].indexOf("_")))){
                            file_name.add(files[i]);
                        }
                    }

                    for (int i = 0; i < file_name.size(); i++) {
                        File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" + File.separator + file_name.get(i));
                        //mFtpClient.download(file_name.get(i),file1,new DownloadListener(context));
                        mFtpClient.download(file_name.get(i), file1);
                    }
                    mFtpClient.disconnect(true);
                }
            } catch (IOException | FTPListParseException | FTPException | FTPDataTransferException | FTPAbortedException | FTPIllegalReplyException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean download_Directory(Context context){
        cbohelp=new CBO_DB_Helper(context);
        getFtpDetail();
        mAdapterCallback = ((AdapterCallback) context);
        if(connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context)) {
            try {
                mFtpClient.changeDirectoryUp();
                mFtpClient.changeDirectory(mFtpClient.currentDirectory() + "/visualaid/Catalog");
                String[] files = mFtpClient.listNames();
                ArrayList<String> file_name = new ArrayList<>();
                ArrayList<String> directory_name = new ArrayList<>();
                for (int i = 0; i < files.length; i++) {
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator);
                    file2.mkdirs();
                    if (files[i].contains(".")) {
                        file_name.add(files[i]);
                    } else {
                        directory_name.add(files[i]);
                    }
                }

                for (int i = 0; i < file_name.size(); i++) {
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator + file_name.get(i));
                    mFtpClient.download(file_name.get(i), file2);
                }

                for (int i = 0; i < directory_name.size(); i++) {
                    mFtpClient.changeDirectory(mFtpClient.currentDirectory() + File.separator + directory_name.get(i));
                    String[] files_child = mFtpClient.listNames();
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator + directory_name.get(i) + File.separator);
                    file2.mkdirs();
                    for (int j = 0; j < files_child.length; j++) {
                        if (files_child[j].contains(".")) {
                            File file3 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator + directory_name.get(i) + File.separator + files_child[j]);
                            mFtpClient.download(files_child[j], file3);
                        }

                        Log.d("File child ", files_child[j]);

                    }
                    mFtpClient.changeDirectoryUp();
                    Log.d("File ", directory_name.get(i));
                }

                mFtpClient.disconnect(true);
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
            } catch (FTPDataTransferException e) {
                e.printStackTrace();
            } catch (FTPListParseException e) {
                e.printStackTrace();
            } catch (FTPAbortedException e) {
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }

    public boolean downloadSingleFile(final String remoteFilePath,final  File downloadFile,final Context context) {

        final Boolean[] result = {false};
        Runnable runnable = new Runnable() {
            public void run() {
                //shareclass=new Shareclass();
                cbohelp=new CBO_DB_Helper(context);
                getFtpDetail();
                if(connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context)) {
                    File parentDir = downloadFile.getParentFile();
                    if (!parentDir.exists())
                        parentDir.mkdir();
                    try {

                        mFtpClient.download(remoteFilePath, downloadFile);
                        mFtpClient.disconnect(true);
                        result[0] = true;
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

            }
        };
        Thread mythread1 = new Thread(runnable);
        mythread1.start();
        return result[0];
    }

    public  interface AdapterCallback {
        void  started(Integer responseCode,String message,String description);
        void  progess(Integer responseCode,Long FileSize,Float value,String description);
        void  complete(Integer responseCode,String message,String description);
        void  aborted(Integer responseCode,String message,String description);
        void  failed(Integer responseCode,String message,String description);
    }

    public void uploadFile( final File[] files,final Context context) {
        IsLastFile = false;
        Runnable runnable = new Runnable() {

            public void run() {
                cbohelp=new CBO_DB_Helper(context);
                getFtpDetail();
                mAdapterCallback = ((AdapterCallback) context);
                //connnectingwithFTP("220.158.164.114", "CBO_DOMAIN_SERVER", "cbodomain@321");
                if(connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context)) {
                    try {


                        // code here to compress images......

                        for (File uploadFile: files) {
                            compressImage(uploadFile);

                            if (uploadFile == files[files.length -1])
                                IsLastFile = true;
                            //============================================
                            file_size = uploadFile.length();
                            mFtpClient.upload(uploadFile, new MyTransferListener(context));
                        }

                        mFtpClient.disconnect(true);
                    } catch (Exception e) {
                        try {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                                }
                            });
                        } catch (ClassCastException e1) {
                            throw new ClassCastException("Activity must implement AdapterCallback.");
                        }
                        e.printStackTrace();
                    }
                }
            }
        };
        final Thread mythread1 = new Thread(runnable);
        mythread1.start();

    }


    public void uploadFile( final File uploadFile,final Context context) {
        IsLastFile = true;
        Runnable runnable = new Runnable() {

            public void run() {
                cbohelp=new CBO_DB_Helper(context);
                getFtpDetail();
                mAdapterCallback = ((AdapterCallback) context);
                //connnectingwithFTP("220.158.164.114", "CBO_DOMAIN_SERVER", "cbodomain@321");
                if(connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,context)) {
                    try {


                        // code here to compress images......

                        compressImage(uploadFile);

                        //============================================

                        file_size = uploadFile.length();
                        mFtpClient.upload(uploadFile, new MyTransferListener(context));
                        mFtpClient.disconnect(true);
                    } catch (Exception e) {
                        try {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                                }
                            });
                        } catch (ClassCastException e1) {
                            throw new ClassCastException("Activity must implement AdapterCallback.");
                        }
                        e.printStackTrace();
                    }
                }
            }
        };
        final Thread mythread1 = new Thread(runnable);
        mythread1.start();

    }


    /*******  Used to file upload and show progress  **********/

    public class MyTransferListener implements FTPDataTransferListener {

        Context context;

        MyTransferListener(Context context){
            this.context=context;
        }

        public void started() {


            // Transfer started
            //Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" Upload Started ...");
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.started(responseCode,"Started!!!","Upload Started!!!");
                    }
                });

            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_file)
                            .setContentTitle("Upload Started ...")
                            .setContentText("Please Do not switch off your Internet")
                            .setAutoCancel(false)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_file);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification

        }

        public void transferred(int length) {

            // Yet other length bytes has been transferred since the last time this
            // method was called
            //Toast.makeText(getBaseContext(), " transferred ..." +MyConnection.user_name+ length, Toast.LENGTH_SHORT).show();
            //System.out.println(" transferred ..." + length);

            //build notification
            file_uploaded=file_uploaded +  length;
            float uploaded=file_uploaded*100/file_size;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    mAdapterCallback.progess(responseCode,file_size,uploaded,""+uploaded+" % completed...");
                }
            });

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_file)
                            .setContentTitle(""+uploaded+" % completed...")
                            .setContentText("Please Do not switch off your Internet ")
                            .setAutoCancel(false)
                            .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_file);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification
        }

        public void completed() {


            // Transfer completed

            //Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
            //System.out.println(" completed ..." );
            try {
                if (IsLastFile){
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            mAdapterCallback.complete(responseCode,"Success!!!","Upload completed....");
                        }
                    });
                }

            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_complete)
                            .setContentTitle("Upload completed")
                            .setContentText("Sucessfully uploaded...")
                            .setAutoCancel(true)
                            .setDefaults(Notification.DEFAULT_ALL);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_complete);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification
        }

        public void aborted() {


            // Transfer aborted
            //Toast.makeText(getBaseContext()," transfer aborted , please try again...", Toast.LENGTH_SHORT).show();
            //System.out.println(" aborted ..." );
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                    }
                });

            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        }

        public void failed() {

            // Transfer failed
            System.out.println(" failed ..." );
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Upload Failed....\nPlease try after sometime");
                    }
                });
            } catch (ClassCastException e) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
            //build notification
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_upload_failed)
                            .setContentTitle("Upload Failed ...")
                            .setContentText("Please try again...")
                            .setAutoCancel(false)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setOngoing(true);

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int color = 0x125688;
                mBuilder.setColor(color);
                mBuilder.setSmallIcon(R.drawable.ic_upload_failed);
            }

            Random random = new Random();
            //int m = random.nextInt(9999 - 1000) + 1000;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build()); //m = ID of notification
        }

    }


//    public class DownloadListener implements FTPDataTransferListener {
//
//        Context context;
//
//        DownloadListener(Context context){
//            this.context=context;
//        }
//
//        public void started() {
//
//
//            // Transfer started
//            //Toast.makeText(getBaseContext(), " Upload Started ...", Toast.LENGTH_SHORT).show();
//            //System.out.println(" Upload Started ...");
//            try {
//                mAdapterCallback. upload_complete("S");
//            } catch (ClassCastException e) {
//                throw new ClassCastException("Activity must implement AdapterCallback.");
//            }
//
//
//        }
//
//        public void transferred(int length) {
//
//            // Yet other length bytes has been transferred since the last time this
//            // method was called
//            //Toast.makeText(getBaseContext(), " transferred ..." +MyConnection.user_name+ length, Toast.LENGTH_SHORT).show();
//            //System.out.println(" transferred ..." + length);
//
//         /*   //build notification
//            file_uploaded=file_uploaded +  length;
//            float uploaded=file_uploaded*100/file_size;
//            NotificationCompat.Builder mBuilder =
//                    new NotificationCompat.Builder(context)
//                            .setSmallIcon(R.drawable.ic_upload_file)
//                            .setContentTitle(""+uploaded+" % completed...")
//                            .setContentText("Please Do not switch off your Internet ")
//                            .setAutoCancel(false)
//                            .setOngoing(true);
//
//            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                int color = 0x125688;
//                mBuilder.setColor(color);
//                mBuilder.setSmallIcon(R.drawable.ic_upload_file);
//            }
//
//            Random random = new Random();
//            //int m = random.nextInt(9999 - 1000) + 1000;
//            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            notificationManager.notify(0, mBuilder.build()); //m = ID of notification*/
//        }
//
//        public void completed() {
//
//
//            // Transfer completed
//
//            //Toast.makeText(getBaseContext(), " completed ...", Toast.LENGTH_SHORT).show();
//            //System.out.println(" completed ..." );
//           /* try {
//                mAdapterCallback. upload_complete("Y");
//            } catch (ClassCastException e) {
//                throw new ClassCastException("Activity must implement AdapterCallback.");
//            }*/
//
//        }
//
//        public void aborted() {
//
//
//            // Transfer aborted
//            //Toast.makeText(getBaseContext()," transfer aborted , please try again...", Toast.LENGTH_SHORT).show();
//            //System.out.println(" aborted ..." );
//            try {
//                mAdapterCallback. upload_complete("N");
//            } catch (ClassCastException e) {
//                throw new ClassCastException("Activity must implement AdapterCallback.");
//            }
//        }
//
//        public void failed() {
//
//            // Transfer failed
//            System.out.println(" failed ..." );
//            try {
//                mAdapterCallback. upload_complete("N");
//            } catch (ClassCastException e) {
//                throw new ClassCastException("Activity must implement AdapterCallback.");
//            }
//
//        }
//
//    }


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


    public String compressImage(File imageUri) {

        //String filePath = getRealPathFromURI(imageUri);
        String filePath=imageUri.getPath();
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        if (options.outWidth != -1 && options.outHeight != -1) {
            // This is an image file.

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            assert scaledBitmap != null;
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            //String filename = getFilename();
            try {
                out = new FileOutputStream(filePath);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            // This is not an image file.
        }


        return filePath;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

   /* private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }*/

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

}
