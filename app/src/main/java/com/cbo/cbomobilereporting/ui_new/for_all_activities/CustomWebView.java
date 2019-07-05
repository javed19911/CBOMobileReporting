package com.cbo.cbomobilereporting.ui_new.for_all_activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.uenics.javed.CBOLibrary.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;

import services.CboServices;
import utils.adapterutils.SpinnerModel;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

/**
 * Created by Akshit on 08/03/16.
 */
public class CustomWebView extends AppCompatActivity {

    private WebView webView;
    String url;
    Intent intent;
    Context context;
    CBO_DB_Helper cboDbHelper;
    Custom_Variables_And_Method customVariablesAndMethod;
    Bundle bundle;
    private ProgressDialog progressDialog;
    String menu_code="";
    private  static final int MESSAGE_INTERNET=1;
    int count=0;
    String previous_url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tp_pending_webview);

        //  url ="http://www.indianherb.co.in/Forms/DrAddNew.aspx?PAID=144&TYPE=D";//= a.getString("tp_url");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);

        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        setSupportActionBar(toolbar);
        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        cboDbHelper=new CBO_DB_Helper(context);

        if (getSupportActionBar() != null) {
            textView.setText("Add Doctor");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.close_btn);

        }
        bundle = getIntent().getExtras();

        if ((bundle != null) && (bundle.getString("addnew_dr_Url") != null)) {
            url = bundle.getString("addnew_dr_Url");
            //textView.setText(bundle.getString("Title"));
        } else if ((bundle != null) && (bundle.getString("DRSALE_ADDNEW_URL") != null)) {

            textView.setText("Dr. Wise Sales");
            url = bundle.getString("DRSALE_ADDNEW_URL");
        } else if ((bundle != null) && (bundle.getString("TP_ADDNEW_URL") != null)) {
            textView.setText("Add Tour Programme");
            url = bundle.getString("TP_ADDNEW_URL");
        } else if ((bundle != null) && (bundle.getString("CHALLAN_ACK_URL") != null)) {
            textView.setText("Challan Received");
            //textView.setText(bundle.getString("Title"));
            url = bundle.getString("CHALLAN_ACK_URL");
        } else if ((bundle != null) && (bundle.getString("SECONDARY_SALE_URL") != null)) {
            //textView.setText("Secondary Sales");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("SECONDARY_SALE_URL");
        } else if ((bundle != null) && (bundle.getString("TP_APPROVE_URL") != null)) {
            //textView.setText("T.P. Approval");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("TP_APPROVE_URL");
        } else if ((bundle != null) && (bundle.getString("PERSONAL_INFORMATION_URL") != null)) {
            //textView.setText("Personal Info.");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("PERSONAL_INFORMATION_URL");
        } else if ((bundle != null) && (bundle.getString("FORM16_URL") != null)) {
            //textView.setText("Form 16");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("FORM16_URL");
        } else if ((bundle != null) && (bundle.getString("SALARY_SLIP_URL") != null)) {
            //textView.setText("Salary Slip");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("SALARY_SLIP_URL");
        } else if ((bundle != null) && (bundle.getString("CIRCULAR_URL") != null)) {
            //textView.setText("Circular");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("CIRCULAR_URL");
        } else if ((bundle != null) && ((bundle.getString("CHANGE_PASSWORD_URL")) != null)) {
           // textView.setText("Change Password");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("CHANGE_PASSWORD_URL");
        } else if ((bundle != null) && ((bundle.getString("DECLARATION_OF_SAVING_URL")) != null)) {
            //textView.setText("Declaration Of Saving");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("DECLARATION_OF_SAVING_URL");
        } else if ((bundle != null) && ((bundle.getString("ROUTE_MASTER_URL")) != null)) {
            //textView.setText("Route Master");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("ROUTE_MASTER_URL");
        } else if ((bundle != null) && ((bundle.getString("HOLIDAY_URL")) != null)) {
            //textView.setText("Holiday List");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("HOLIDAY_URL");
        }  else if ((bundle != null) && ((bundle.getString("msg_ho")) != null)) {
            //textView.setText("Message form HO");
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("msg_ho");
        }else if ((bundle != null) && ((bundle.getString("A_TP")) != null)) {
            textView.setText(bundle.getString("Title"));
            url = bundle.getString("A_TP");
        }else if ((bundle != null) && ((bundle.getString("A_TP1")) != null)) {
            textView.setText(bundle.getString("Title"));
            menu_code=bundle.getString("Menu_code");
            url = bundle.getString("A_TP1");
        }else {
            textView.setText("Add Chemist");
            url = bundle.getString("addnew_chm_Url");
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        webView = (WebView) findViewById(R.id.webview_tp);

        initWebView(webView);
        //webView.getSettings().setJavaScriptEnabled(true);
//        webView.getSettings().setSupportZoom(true);
//        webView.getSettings().setBuiltInZoomControls(true);
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setInitialScale(1);
//        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new HelloWebViewClient()
        {
            @Override
            public void onPageFinished(WebView view, String url)
            {
                //Calling an init method that tells the website, we're ready
                webView.loadUrl("javascript:m2Init()");

                if (url.toLowerCase().contains("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting&hl=en"));
                    startActivity(i);
                    finish();
                }else if (url.toLowerCase().contains("play.google.com/store/apps/")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                    finish();
                }else if (url.toLowerCase().contains(".pdf")){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(url), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

              /*  if (Build.VERSION.SDK_INT >= 24) {
                    path = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider",path2);
                } else {
                    path = Uri.fromFile(path2);
                }

                // Setting the intent for pdf reader
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);*/

                }else{
                    page11(webView);
                    previous_url=url;
                }

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {

                customVariablesAndMethod.msgBox(context,"error" + error);
            }


        });



        if (url.toLowerCase().contains("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting")) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.cbo.cbomobilereporting&hl=en"));
            startActivity(i);
            finish();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }else if (url.toLowerCase().contains("play.google.com/store/apps/")) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(i);
            finish();
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }else if (textView.getText().toString().equalsIgnoreCase("Salary Slip") && (url.toLowerCase().contains(".pdf"))){
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
        } else if ( (url.toLowerCase().contains(".pdf"))){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(url), "application/pdf");
            startActivity(intent);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        } else  {

            if(!url.toLowerCase().contains("http://") && !url.toLowerCase().contains("emulated/0")){
                url="http://"+url;
            }else if(url.toLowerCase().contains("emulated/0")){
                url="file:///"+url;
            }

            Custom_Variables_And_Method.GLOBAL_LATLON =  customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
            Custom_Variables_And_Method.DCR_DATE_TO_SUBMIT=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"DCR_DATE");
            if(!url.contains("emulated/0") && !url.isEmpty()){
                if ( url.contains("?")) {
                    url = url + "&LAT_LONG=" + Custom_Variables_And_Method.GLOBAL_LATLON ;
                }else{
                    url = url + "?LAT_LONG=" + Custom_Variables_And_Method.GLOBAL_LATLON ;
                }
            }

            //customVariablesAndMethod.getAlert(context,"Url",url);

            String ALLOWED_URI_CHARS = "@#&=*-_.,:!?()/~'%";
            String url1 = Uri.encode(url, ALLOWED_URI_CHARS);
            webView.loadUrl(url1);

        }


    }


    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest  url) {

            //if (Build.VERSION.SDK_INT >= 24) {
                return super.shouldOverrideUrlLoading(view, url);
           /* } else {
               view.loadUrl(url.toString());
                return true;
            }*/

        }

    }


    private final static Object methodInvoke(Object obj, String method, Class<?>[] parameterTypes, Object[] args) {
        try {
            Method m = obj.getClass().getMethod(method, new Class[] { boolean.class });
            m.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initWebView(WebView webView) {

        WebSettings settings = webView.getSettings();

        settings.setJavaScriptEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        // settings.setPluginsEnabled(true);
        methodInvoke(settings, "setPluginsEnabled", new Class[] { boolean.class }, new Object[] { true });
        // settings.setPluginState(PluginState.ON);
        methodInvoke(settings, "setPluginState", new Class[] { WebSettings.PluginState.class }, new Object[] { WebSettings.PluginState.ON });
        // settings.setPluginsEnabled(true);
        methodInvoke(settings, "setPluginsEnabled", new Class[] { boolean.class }, new Object[] { true });
        // settings.setAllowUniversalAccessFromFileURLs(true);
        methodInvoke(settings, "setAllowUniversalAccessFromFileURLs", new Class[] { boolean.class }, new Object[] { true });
        // settings.setAllowFileAccessFromFileURLs(true);
        methodInvoke(settings, "setAllowFileAccessFromFileURLs", new Class[] { boolean.class }, new Object[] { true });

        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearCache(true);

        webView.setWebChromeClient(new MyWebChromeClient());
        // webView.setDownloadListener(downloadListener);
    }

    UploadHandler mUploadHandler;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == Controller.FILE_SELECTED) {
            // Chose a file from the file picker.
            if (mUploadHandler != null) {
                mUploadHandler.onResult(resultCode, intent);
            }
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }

    class MyWebChromeClient extends WebChromeClient {
        public MyWebChromeClient() {

        }

        private String getTitleFromUrl(String url) {
            String title = url;
            try {
                URL urlObj = new URL(url);
                String host = urlObj.getHost();
                if (host != null && !host.isEmpty()) {
                    return urlObj.getProtocol() + "://" + host;
                }
                if (url.startsWith("file:")) {
                    String fileName = urlObj.getFile();
                    if (fileName != null && !fileName.isEmpty()) {
                        return fileName;
                    }
                }
            } catch (Exception e) {
                // ignore
            }

            return title;
        }

        @Override
        public void onCloseWindow(WebView window) {
            super.onCloseWindow(window);
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            switch (consoleMessage.message().toLowerCase()){
                case "window.close();" :
                    finish();
                    break;
                case "sync" :
                    new Service_Call_From_Multiple_Classes().DownloadAll(context, new Response() {
                        @Override
                        public void onSuccess(Bundle bundle) {
                            finish();
                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context,s,s1);
                        }
                    });
                    break;
                default:
                    Log.d("web",consoleMessage.message().toLowerCase());

            }

            return super.onConsoleMessage(consoleMessage);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
            String newTitle = getTitleFromUrl(url);

            new AlertDialog.Builder(CustomWebView.this).setTitle(newTitle).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setCancelable(false).create().show();
            return true;
            // return super.onJsAlert(view, url, message, result);
        }

        @Override
        public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {

            String newTitle = getTitleFromUrl(url);

            new AlertDialog.Builder(CustomWebView.this).setTitle(newTitle).setMessage(message).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    result.confirm();
                }
            }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    result.cancel();
                }
            }).setCancelable(false).create().show();
            return true;

            // return super.onJsConfirm(view, url, message, result);
        }

        // Android 2.x
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, "");
        }

        // Android 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, "", "filesystem");
        }

        // Android 4.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            mUploadHandler = new UploadHandler(new Controller());
            mUploadHandler.openFileChooser(uploadMsg, acceptType, capture);
        }

        // Android 4.4, 4.4.1, 4.4.2
        // openFileChooser function is not called on Android 4.4, 4.4.1, 4.4.2,
        // you may use your own java script interface or other hybrid framework.

        // Android 5.0.1
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {

            String acceptTypes[] = fileChooserParams.getAcceptTypes();

            String acceptType = "";
            for (int i = 0; i < acceptTypes.length; ++ i) {
                if (acceptTypes[i] != null && acceptTypes[i].length() != 0)
                    acceptType += acceptTypes[i] + ";";
            }
            if (acceptType.length() == 0)
                acceptType = "*/*";

            final ValueCallback<Uri[]> finalFilePathCallback = filePathCallback;

            ValueCallback<Uri> vc = new ValueCallback<Uri>() {

                @Override
                public void onReceiveValue(Uri value) {

                    Uri[] result;
                    if (value != null)
                        result = new Uri[]{value};
                    else
                        result = null;

                    finalFilePathCallback.onReceiveValue(result);

                }
            };

            openFileChooser(vc, acceptType, "filesystem");


            return true;
        }
    };

    class Controller {
        final static int FILE_SELECTED = 4;

        Activity getActivity() {
            return CustomWebView.this;
        }
    }

    // copied from android-4.4.3_r1/src/com/android/browser/UploadHandler.java
    //////////////////////////////////////////////////////////////////////

    /*
     * Copyright (C) 2010 The Android Open Source Project
     *
     * Licensed under the Apache License, Version 2.0 (the "License");
     * you may not use this file except in compliance with the License.
     * You may obtain a copy of the License at
     *
     *      http://www.apache.org/licenses/LICENSE-2.0
     *
     * Unless required by applicable law or agreed to in writing, software
     * distributed under the License is distributed on an "AS IS" BASIS,
     * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     * See the License for the specific language governing permissions and
     * limitations under the License.
     */

    // package com.android.browser;
    //
    // import android.app.Activity;
    // import android.content.ActivityNotFoundException;
    // import android.content.Intent;
    // import android.net.Uri;
    // import android.os.Environment;
    // import android.provider.MediaStore;
    // import android.webkit.ValueCallback;
    // import android.widget.Toast;
    //
    // import java.io.File;
    // import java.util.Vector;
    //
    // /**
    // * Handle the file upload callbacks from WebView here
    // */
    // public class UploadHandler {

    class UploadHandler {
        /*
         * The Object used to inform the WebView of the file to upload.
         */
        private ValueCallback<Uri> mUploadMessage;
        private String mCameraFilePath;
        private boolean mHandled;
        private boolean mCaughtActivityNotFoundException;
        private Controller mController;
        public UploadHandler(Controller controller) {
            mController = controller;
        }
        String getFilePath() {
            return mCameraFilePath;
        }
        boolean handled() {
            return mHandled;
        }
        void onResult(int resultCode, Intent intent) {
            if (resultCode == Activity.RESULT_CANCELED && mCaughtActivityNotFoundException) {
                // Couldn't resolve an activity, we are going to try again so skip
                // this result.
                mCaughtActivityNotFoundException = false;
                return;
            }
            Uri result = intent == null || resultCode != Activity.RESULT_OK ? null
                    : intent.getData();
            // As we ask the camera to save the result of the user taking
            // a picture, the camera application does not return anything other
            // than RESULT_OK. So we need to check whether the file we expected
            // was written to disk in the in the case that we
            // did not get an intent returned but did get a RESULT_OK. If it was,
            // we assume that this result has came back from the camera.
            if (result == null && intent == null && resultCode == Activity.RESULT_OK) {
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile);
                    // Broadcast to the media scanner that we have a new photo
                    // so it will be added into the gallery for the user.
                    mController.getActivity().sendBroadcast(
                            new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
                }
            }
            mUploadMessage.onReceiveValue(result);
            mHandled = true;
            mCaughtActivityNotFoundException = false;
        }
        void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            final String imageMimeType = "image/*";
            final String videoMimeType = "video/*";
            final String audioMimeType = "audio/*";
            final String mediaSourceKey = "capture";
            final String mediaSourceValueCamera = "camera";
            final String mediaSourceValueFileSystem = "filesystem";
            final String mediaSourceValueCamcorder = "camcorder";
            final String mediaSourceValueMicrophone = "microphone";
            // According to the spec, media source can be 'filesystem' or 'camera' or 'camcorder'
            // or 'microphone' and the default value should be 'filesystem'.
            String mediaSource = mediaSourceValueFileSystem;
            if (mUploadMessage != null) {
                // Already a file picker operation in progress.
                return;
            }
            mUploadMessage = uploadMsg;
            // Parse the accept type.
            String params[] = acceptType.split(";");
            String mimeType = params[0];
            if (capture.length() > 0) {
                mediaSource = capture;
            }
            if (capture.equals(mediaSourceValueFileSystem)) {
                // To maintain backwards compatibility with the previous implementation
                // of the media capture API, if the value of the 'capture' attribute is
                // "filesystem", we should examine the accept-type for a MIME type that
                // may specify a different capture value.
                for (String p : params) {
                    String[] keyValue = p.split("=");
                    if (keyValue.length == 2) {
                        // Process key=value parameters.
                        if (mediaSourceKey.equals(keyValue[0])) {
                            mediaSource = keyValue[1];
                        }
                    }
                }
            }
            //Ensure it is not still set from a previous upload.
            mCameraFilePath = null;
            if (mimeType.equals(imageMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamera)) {
                    // Specified 'image/*' and requested the camera, so go ahead and launch the
                    // camera directly.
                    startActivity(createCameraIntent());
                    return;
                } else {
                    // Specified just 'image/*', capture=filesystem, or an invalid capture parameter.
                    // In all these cases we show a traditional picker filetered on accept type
                    // so launch an intent for both the Camera and image/* OPENABLE.
                    Intent chooser = createChooserIntent(createCameraIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(imageMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(videoMimeType)) {
                if (mediaSource.equals(mediaSourceValueCamcorder)) {
                    // Specified 'video/*' and requested the camcorder, so go ahead and launch the
                    // camcorder directly.
                    startActivity(createCamcorderIntent());
                    return;
                } else {
                    // Specified just 'video/*', capture=filesystem or an invalid capture parameter.
                    // In all these cases we show an intent for the traditional file picker, filtered
                    // on accept type so launch an intent for both camcorder and video/* OPENABLE.
                    Intent chooser = createChooserIntent(createCamcorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(videoMimeType));
                    startActivity(chooser);
                    return;
                }
            } else if (mimeType.equals(audioMimeType)) {
                if (mediaSource.equals(mediaSourceValueMicrophone)) {
                    // Specified 'audio/*' and requested microphone, so go ahead and launch the sound
                    // recorder.
                    startActivity(createSoundRecorderIntent());
                    return;
                } else {
                    // Specified just 'audio/*',  capture=filesystem of an invalid capture parameter.
                    // In all these cases so go ahead and launch an intent for both the sound
                    // recorder and audio/* OPENABLE.
                    Intent chooser = createChooserIntent(createSoundRecorderIntent());
                    chooser.putExtra(Intent.EXTRA_INTENT, createOpenableIntent(audioMimeType));
                    startActivity(chooser);
                    return;
                }
            }
            // No special handling based on the accept type was necessary, so trigger the default
            // file upload chooser.
            startActivity(createDefaultOpenableIntent());
        }
        private void startActivity(Intent intent) {
            try {
                mController.getActivity().startActivityForResult(intent, Controller.FILE_SELECTED);
            } catch (ActivityNotFoundException e) {
                // No installed app was able to handle the intent that
                // we sent, so fallback to the default file upload control.
                try {
                    mCaughtActivityNotFoundException = true;
                    mController.getActivity().startActivityForResult(createDefaultOpenableIntent(),
                            Controller.FILE_SELECTED);
                } catch (ActivityNotFoundException e2) {
                    // Nothing can return us a file, so file upload is effectively disabled.
                    Toast.makeText(mController.getActivity(), R.string.uploads_disabled,
                            Toast.LENGTH_LONG).show();
                }
            }
        }
        private Intent createDefaultOpenableIntent() {
            // Create and return a chooser with the default OPENABLE
            // actions including the camera, camcorder and sound
            // recorder where available.
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            Intent chooser = createChooserIntent(createCameraIntent(), createCamcorderIntent(),
                    createSoundRecorderIntent());
            chooser.putExtra(Intent.EXTRA_INTENT, i);
            return chooser;
        }
        private Intent createChooserIntent(Intent... intents) {
            Intent chooser = new Intent(Intent.ACTION_CHOOSER);
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
            chooser.putExtra(Intent.EXTRA_TITLE,
                    mController.getActivity().getResources()
                            .getString(R.string.choose_upload));
            return chooser;
        }
        private Intent createOpenableIntent(String type) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            return i;
        }
        private Intent createCameraIntent() {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File externalDataDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM);
            File cameraDataDir = new File(externalDataDir.getAbsolutePath() +
                    File.separator + "browser-photos");
            cameraDataDir.mkdirs();
            mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator +
                    System.currentTimeMillis() + ".jpg";
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
            return cameraIntent;
        }
        private Intent createCamcorderIntent() {
            return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        }
        private Intent createSoundRecorderIntent() {
            return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        }
    }


    @Override
    protected void onDestroy() {
        if (progressDialog != null){
            progressDialog.dismiss();
            progressDialog = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void page11(View view)
    {
        webView.loadUrl("javascript:m2LoadPage(1)");
    }


    /*@Override
    public void onBackPressed() {
        if(menu_code.equals("") || count==2) {
            finish();
        }else{
            //Start of call to service

           *//* HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder",cboDbHelper.getCompanyCode());
            request.put("iPaId",""+Custom_Variables_And_Method.PA_ID);
            //request.put("iDcrId", dcr_id);

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);

            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            count++;
            new CboServices(this,mHandler).customMethodForAllServices(request,"DCREXPDDLALLROUTE_MOBILE",MESSAGE_INTERNET,tables);
*//*
            //End of call to service
            finish();
        }
    }
*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            /*if (webView.canGoBack()) {
                webView.goBack();
            }else {*/
                finish();
         //   }
        }
        return super.onOptionsItemSelected(item);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET:
                    progressDialog.dismiss();
                    if ((null != msg.getData())) {

                        parser1(msg.getData());

                    }
                    break;
                case 99:
                    progressDialog.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
                    progressDialog.dismiss();
                    finish();

            }
        }
    };

    public void parser1(Bundle result) {
        if (result!=null ) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    newlist.add(new SpinnerModel(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID")));

                }
                progressDialog.dismiss();
                finish();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progressDialog.dismiss();

    }

}


