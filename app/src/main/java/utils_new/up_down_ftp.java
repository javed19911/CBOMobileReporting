package utils_new;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

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
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

import com.cbo.cbomobilereporting.MyCustumApplication;

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

    String ftp_username_download="";
    String ftp_hostname_download="";
    String ftp_password_download="";
    String ftp_port_download="21";

    CBO_DB_Helper cbohelp;
    //Shareclass shareclass;
    private AdapterCallback mAdapterCallback;
    long file_size=0;
    float file_uploaded=0.0f;
    Boolean IsLastFile = true;


    public up_down_ftp(){

    }

    public up_down_ftp setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    private Boolean connectFTP(String ip, String userName, String pass){
        try {
            if (!mFtpClient.isConnected()) {
                mFtpClient.connect(ip, Integer.parseInt(ftp_port));
                mFtpClient.login(userName, pass);
                //mFtpClient.setPassive(true);
                //mFtpClient.noop();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!",e.getLocalizedMessage());
                    }
                });


            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (FTPIllegalReplyException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!",e.getLocalizedMessage());
                    }
                });


            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        } catch (FTPException e) {
            e.printStackTrace();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!",e.getLocalizedMessage());
                    }
                });


            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }
        }
        return false;
    }

    private FTPClient connnectingwithFTP( String ip, String userName, String pass,final AdapterCallback context) {
        try {
            FTPClient mFtpClient = new FTPClient();
            AdapterCallback mAdapterCallback = context;
            try {


                //mFtpClient.setAutoNoopTimeout(5000);
                mFtpClient.connect(ip, Integer.parseInt(ftp_port));
                mFtpClient.login(userName, pass);
           /* mFtpClient.connect("220.158.164.79",21);
            mFtpClient.login("cbo_user", "cbo@12345#$");*/
                status = true;
                Log.e("isFTPConnected", String.valueOf(status));
                mFtpClient.setType(FTPClient.TYPE_BINARY);

                //mFtpClient.changeDirectory("//DEMO/upload/");
                mFtpClient.changeDirectory(web_root_path);
                //mFtpClient.setPassive(true);
                //mFtpClient.noop();

                return mFtpClient;
            } catch (SocketException e) {
                e.printStackTrace();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mAdapterCallback != null) {
                            mAdapterCallback.failed(responseCode, "Failed!!!", "Upload Failed....\nPlease try after sometime");
                        }
                    }
                });


            } catch (UnknownHostException e) {
                e.printStackTrace();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mAdapterCallback != null) {
                            mAdapterCallback.failed(responseCode, "Failed!!!", "Upload Failed....\nPlease try after sometime");
                        }
                    }
                });


            } catch (IOException e) {
                e.printStackTrace();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mAdapterCallback != null) {
                            mAdapterCallback.failed(responseCode, "Failed!!!", "Upload Failed....\nPlease try after sometime");
                        }
                    }
                });


            } catch (FTPException e) {

                try {
                    mFtpClient.createDirectory(web_root_path);
                    return mFtpClient;
                } catch (IOException | FTPException | FTPIllegalReplyException e1) {
                    e1.printStackTrace();


                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            if (mAdapterCallback != null) {
                                mAdapterCallback.failed(responseCode, "Folder not found!!!", web_root_path + "   Invalid path \nPlease contact your administrator");// upload_complete("ERROR@"+web_root_path);
                            }
                        }
                    });


                }

                e.printStackTrace();
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mAdapterCallback != null) {
                            mAdapterCallback.failed(responseCode, "Failed!!!", "Upload Failed....\nPlease try after sometime");
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();

                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        if (mAdapterCallback != null) {
                            mAdapterCallback.failed(responseCode, "Failed!!!", "Upload Failed....\nPlease try after sometime");
                        }
                    }
                });


            }
        }catch (Throwable e){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    if (mAdapterCallback != null) {
                        mAdapterCallback.failed(responseCode, "Failed!!!", "Upload Failed....\nPlease try after sometime");
                    }
                }
            });
        }
        return null;
    }

    public void download_visual_aids(AdapterCallback context,ArrayList file_list){
        download_visual_aids(context,file_list,"/visualaid");
    }
    public void download_visual_aids(AdapterCallback context,String file,String directory){
        ArrayList<String>download_file=new ArrayList<String>();
        download_file.add(file);
        download_visual_aids(context,download_file,directory);
    }
    public void download_visual_aids(AdapterCallback context,ArrayList file_list,String directory){

        cbohelp=new CBO_DB_Helper(MyCustumApplication.getInstance());

        getFtpDetail();
        ftp_port = ftp_port_download;
        mAdapterCallback = ((AdapterCallback) context);


            try {
                FTPClient mFtpClient = connnectingwithFTP(ftp_hostname_download, ftp_username_download, ftp_password_download,context);
                if(mFtpClient != null) {
                    // mAdapterCallback = ((AdapterCallback) context);
                    if (file_list.contains("CATALOG")) {
                        download_Directory(context);
                    } else {

                        mFtpClient.changeDirectoryUp();
                        mFtpClient.changeDirectory(mFtpClient.currentDirectory() + directory);
                        String storeToDirectory = directory.replace("/visualaid", "");
                        String[] files = mFtpClient.listNames();
                        ArrayList<String> file_name = new ArrayList<>();
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" + storeToDirectory + File.separator);
                        file2.mkdirs();
                        if (storeToDirectory.trim().isEmpty()) {
                            for (int i = 0; i < files.length; i++) {
                                if (files[i].contains(".") && (file_list.contains(files[i].substring(0, files[i].lastIndexOf("."))) || file_list.contains(files[i]))) {
                                    file_name.add(files[i]);
                                } else if (files[i].contains("_") && file_list.contains(files[i].substring(0, files[i].indexOf("_")))) {
                                    file_name.add(files[i]);
                                }
                            }
                        } else {
                            file_name = file_list;
                        }

                        for (int i = 0; i < file_name.size(); i++) {
                            File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" + storeToDirectory + File.separator + file_name.get(i));
                            //mFtpClient.download(file_name.get(i),file1,new DownloadListener(context));
                            mFtpClient.download(file_name.get(i), file1);

                        }
                        mFtpClient.disconnect(true);

                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    mAdapterCallback.complete(responseCode, "Success!!!", "Downloaded Completed...");
                                }
                            });


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapterCallback.failed(responseCode,"Failed!!!","Download Failed....\n"+e.getMessage());
                        }
                    });


            }
        //}

    }


    public void downloadFile(AdapterCallback context,String remoteFileName,String directory) {
         download_visual_aids(context,remoteFileName,directory);
         return;
        /*File file = null;
        int downloadedSize = 0, totalsize;
        float per = 0;
        // set the path where we want to save the file
        File SDCardRoot = Environment.getExternalStorageDirectory();

        mAdapterCallback = ((AdapterCallback) context);

        new File(SDCardRoot + "/cbo").mkdir();
        // create a new file, to save the downloaded file
        String storeToDirectory = directory.replace("/visualaid","");
        file = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" + storeToDirectory+ File.separator+remoteFileName);
        file.mkdirs();
        //file = new File(SDCardRoot, dest_file_path);


        URL myFileUrl = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            myFileUrl = new URL(Custom_Variables_And_Method.WEB_URL + directory + File.separator+remoteFileName);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Log.i("im connected", myFileUrl.toString());
            FileOutputStream fileOutput = new FileOutputStream(file);
            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            totalsize = conn.getContentLength();

            while ((bufferLength = is.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
                //msg_text=name+"is being Downloded...\n"+(int) per+"% compleded";

                Log.d("javed pdf test","Total PDF File size  : "
                        + (totalsize / 1024)
                        + " KB\n\nDownloading PDF " + (int) per
                        + "% complete");
            }
            // close the output stream when complete //
            fileOutput.close();
            try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mAdapterCallback.complete(responseCode,"Success!!!","Downloaded Completed...");
                    }
                });

            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            *//*try {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapterCallback.failed(responseCode,"Failed!!!","Download Failed....\n"+e.getMessage());
                    }
                });

            } catch (ClassCastException e1) {
                throw new ClassCastException("Activity must implement AdapterCallback.");
            }*//*
            download_visual_aids(context,remoteFileName,directory);


        }*/

        //return file;
    }

    public  ArrayList<mFTPFile>  getDirectoryFiles(Context context,String Directory){
        cbohelp=new CBO_DB_Helper(MyCustumApplication.getInstance());
        getFtpDetail();
        ftp_port = ftp_port_download;
        //mAdapterCallback = ((AdapterCallback) context);
        FTPClient mFtpClient = connnectingwithFTP(ftp_hostname_download, ftp_username_download, ftp_password_download,null);
        if(mFtpClient != null) {
            try {
                mFtpClient.changeDirectoryUp();
                String baseDirectory = mFtpClient.currentDirectory();
                mFtpClient.changeDirectory(baseDirectory + "/visualaid"+Directory);
                String[] files = mFtpClient.listNames();
                ArrayList<String> file_name = new ArrayList<>();
                ArrayList<String> directory_name = new ArrayList<>();
                ArrayList<mFTPFile> downloadable_files = new ArrayList<>();

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" +Directory+ File.separator);
                file.mkdirs();

                String currentDirectory = mFtpClient.currentDirectory();

                for (int i = 0; i < files.length; i++) {

                    if (files[i].contains(".")) {
                        file_name.add(files[i]);
                        downloadable_files.add(new mFTPFile( files[i],currentDirectory.replace(baseDirectory,"")));
                    } else {
                        directory_name.add(files[i]);
                    }
                }


                if (!Directory.trim().isEmpty()) {
                    for (int i = 0; i < directory_name.size(); i++) {
                        mFtpClient.changeDirectory(currentDirectory + File.separator + directory_name.get(i));
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product" + Directory + File.separator + directory_name.get(i) + File.separator);
                        file2.mkdirs();
                        String[] files_child = mFtpClient.listNames();


                        for (int j = 0; j < files_child.length; j++) {
                            if (files_child[j].contains(".")) {
                                downloadable_files.add(new mFTPFile(files_child[j], currentDirectory.replace(baseDirectory, "") + File.separator + directory_name.get(i)));
                            }
                        }
                        mFtpClient.changeDirectoryUp();
                        Log.d("File ", directory_name.get(i));

                    }
                }
                mFtpClient.disconnect(true);
                return downloadable_files;

            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPDataTransferException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPListParseException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPAbortedException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            }


        }
        return null;
    }


    public boolean download_Directory(AdapterCallback context){
        cbohelp=new CBO_DB_Helper(MyCustumApplication.getInstance());
        getFtpDetail();
        ftp_port = ftp_port_download;
        mAdapterCallback = ((AdapterCallback) context);
        FTPClient mFtpClient = connnectingwithFTP(ftp_hostname_download, ftp_username_download, ftp_password_download,context);
        if(mFtpClient != null) {
            try {
                mFtpClient.changeDirectoryUp();
                String baseDirectory = mFtpClient.currentDirectory();
                mFtpClient.changeDirectory(baseDirectory + "/visualaid/Catalog");
                String[] files = mFtpClient.listNames();
                ArrayList<String> file_name = new ArrayList<>();
                ArrayList<String> directory_name = new ArrayList<>();
                ArrayList<mFTPFile> downloadable_files = new ArrayList<>();

                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator);
                file.mkdirs();

                String currentDirectory = mFtpClient.currentDirectory();

                for (int i = 0; i < files.length; i++) {

                    if (files[i].contains(".")) {
                        file_name.add(files[i]);
                        downloadable_files.add(new mFTPFile( files[i],currentDirectory.replace(baseDirectory,"")));
                    } else {
                        directory_name.add(files[i]);
                    }
                }

                /*for (int i = 0; i < file_name.size(); i++) {
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator + file_name.get(i));

                    if(connectFTP(ftp_hostname, ftp_username, ftp_password)) {
                        mFtpClient.download(file_name.get(i), file2, new DownloadListener(context));
                    }
                }*/



                for (int i = 0; i < directory_name.size(); i++) {
                    mFtpClient.changeDirectory(currentDirectory + File.separator + directory_name.get(i));
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator + directory_name.get(i) + File.separator);
                    file2.mkdirs();
                    String[] files_child = mFtpClient.listNames();

                    //download_visual_aids(context, new ArrayList<String>(Arrays.asList(files_child)),currentDirectory.replace(baseDirectory,"")+ File.separator + directory_name.get(i));
                   /* for (int j = 0; j < files_child.length; j++) {
                        if (files_child[j].contains(".")) {
                            File file3 = new File(Environment.getExternalStorageDirectory() + File.separator + "cbo/product/Catalog" + File.separator + directory_name.get(i) + File.separator + files_child[j]);
                            //if(connectFTP(ftp_hostname, ftp_username, ftp_password)) {
                            mFtpClient.download(files_child[j], file3);
                            //}
                        }
                        Log.d("File child ", files_child[j]);
                    }*/
                    for (int j = 0; j < files_child.length; j++) {
                        if (files_child[j].contains(".")) {
                            downloadable_files.add(new mFTPFile(files_child[j], currentDirectory.replace(baseDirectory,"")+ File.separator + directory_name.get(i)));
                        }
                    }
                    mFtpClient.changeDirectoryUp();
                    Log.d("File ", directory_name.get(i));

                }

                mFtpClient.disconnect(true);
                //download_visual_aids(context,file_name,currentDirectory.replace(baseDirectory,""));

                for (mFTPFile ftpfile :downloadable_files){
                    download_visual_aids(context,ftpfile.getFileName(),ftpfile.getDirectory());
                }





                //mFtpClient.disconnect(true);
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPIllegalReplyException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPDataTransferException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPListParseException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            } catch (FTPAbortedException e) {
                e.printStackTrace();
                //AppAlert.getInstance().getAlert(context,"Error",e.getMessage());
            }

            return true;
        }
        return false;
    }

    public boolean downloadSingleFile(final String remoteFilePath,final  File downloadFile,final AdapterCallback context) {

        final Boolean[] result = {false};
        Runnable runnable = new Runnable() {
            public void run() {
                //shareclass=new Shareclass();
                cbohelp=new CBO_DB_Helper(MyCustumApplication.getInstance());
                getFtpDetail();
                ftp_port = ftp_port_download;
                FTPClient mFtpClient = connnectingwithFTP(ftp_hostname_download, ftp_username_download, ftp_password_download,context);
                if(mFtpClient != null) {
                    File parentDir = downloadFile.getParentFile();
                    if (!parentDir.exists())
                        parentDir.mkdir();
                    try {

                        mFtpClient.download(remoteFilePath, downloadFile);
                        mFtpClient.disconnect(true);
                        result[0] = true;

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
                cbohelp=new CBO_DB_Helper(MyCustumApplication.getInstance());
                getFtpDetail();
                mAdapterCallback = ((AdapterCallback) context);
                //connnectingwithFTP("220.158.164.114", "CBO_DOMAIN_SERVER", "cbodomain@321");
                FTPClient mFtpClient = connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,mAdapterCallback);
                if(mFtpClient != null) {
                    try {

                        if (files.length == 0){
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {

                                @Override
                                public void run() {
                                    mAdapterCallback.failed(responseCode,"No File!!!","No File Found to be uploaded..");
                                }
                            });

                        }
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
                FTPClient mFtpClient = connnectingwithFTP(ftp_hostname, ftp_username, ftp_password,mAdapterCallback);
                if(mFtpClient != null) {
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

    public class mFTPFile{
        String fileName;
        String Directory;

        public mFTPFile(String fileName, String directory) {
            this.fileName = fileName;
            Directory = directory;
        }

        public String getFileName() {
            return fileName;
        }

        public String getDirectory() {
            return Directory;
        }
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


    /*******  Used to file upload and show progress  **********/

    public class DownloadListener implements FTPDataTransferListener {

        Context context;

        DownloadListener(Context context){
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

                ftp_hostname_download=c.getString(c.getColumnIndex("ftpip_download"));
                ftp_username_download=c.getString(c.getColumnIndex("username_download"));
                ftp_password_download=c.getString(c.getColumnIndex("password_download"));
                ftp_port_download=c.getString(c.getColumnIndex("port_download"));

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
