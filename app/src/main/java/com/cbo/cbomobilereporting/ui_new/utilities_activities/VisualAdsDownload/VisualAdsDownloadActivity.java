package com.cbo.cbomobilereporting.ui_new.utilities_activities.VisualAdsDownload;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder.ApprovalRemainderAdaptor;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;
import com.cbo.cbomobilereporting.ui_new.utilities_activities.VisualAid_Download;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import services.MyAPIService;
import services.ServiceHandler;
import utils.ExceptionHandler;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.interfaces.RecycleViewOnItemClickListener;
import utils_new.up_down_ftp;

public class VisualAdsDownloadActivity extends AppCompatActivity {

    Context context;
    ArrayList<mVisualAds> visualAds = new ArrayList<>();
    VisualAdsDownloadAdaptor visualAdsDownloadAdaptor;
    Custom_Variables_And_Method customVariablesAndMethod;
    RecyclerView listView;
    CBO_DB_Helper cbohelp;
    TextView percent, tname, comp_name, msg;
    ProgressBar pd;
    int PA_ID;
    int count = 0;
    String pa_name = "", msg_text;
    Button StartDownloading;
    LinearLayout DownloadProgess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //new ExceptionHandler(this);
        setContentView(R.layout.activity_visual_ads_download);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            textView.setText("VisualAid Download");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        percent =  findViewById(R.id.per);
        //update=(Button)findViewById(R.id.chk_update);
        pd =  findViewById(R.id.pb);
        tname =  findViewById(R.id.visual_empname);
        comp_name = findViewById(R.id.visual_compname);
        msg = findViewById(R.id.msgbox);
        DownloadProgess = findViewById(R.id.downloadProgress);
        StartDownloading = findViewById(R.id.startdownload);

        PA_ID = Custom_Variables_And_Method.PA_ID;
        pa_name = Custom_Variables_And_Method.PA_NAME;


        context = this;
        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();


        tname.setText("Welcome  " + pa_name);
        comp_name.setText(Custom_Variables_And_Method.COMPANY_NAME);

        listView = findViewById(R.id.file_list);
        context = this;
        cbohelp = new CBO_DB_Helper(context);


        DownloadProgess.setVisibility(View.GONE);
        StartDownloading.setVisibility(View.VISIBLE);

        StartDownloading.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                StrartDownload(0);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void StrartDownload(int who){
        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);
        new MyAPIService(context)
                .execute(new ResponseBuilder("VISUALAID_DOWNLOAD", request)
                        .setMultiTable(false).setTables(tables).setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) {

                                DownloadProgess.setVisibility(View.VISIBLE);
                                StartDownloading.setVisibility(View.GONE);

                                /*String visual_pdf= Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"VISUALAIDPDFYN");
                                String ext=".jpg";
                                if(visual_pdf.equals("Y")){
                                    ext=".pdf";
                                }*/
                                File f = new File(Environment.getExternalStorageDirectory(), "cbo/product");
                                f.deleteOnExit();
                                f.mkdir();
                                visualAdsDownloadAdaptor =
                                        new VisualAdsDownloadAdaptor(context, visualAds, (view, position, isLongClick) -> {
                                            /*Intent i = new Intent(context, CustomWebView.class);
                                            i.putExtra("A_TP", mApprovalRemainders.get(position).getADD_URL());
                                            i.putExtra("Title",  mApprovalRemainders.get(position).getPARICULARS());
                                            startActivity(i);*/
                                        });

                                visualAdsDownloadAdaptor.setListener(new VisualAdsDownloadAdaptor.DownloadListener() {
                                    @Override
                                    public void onSucess(int filesDownloaded,int totalFiles) {
                                        pd.setProgress( (filesDownloaded*100)/totalFiles);

                                        percent.setText(filesDownloaded +"/"+totalFiles+" downloaded");
                                        //msg.setText(msg_text);
                                        if (filesDownloaded == totalFiles){
                                            AppAlert.getInstance().Alert(context, "Download Compleate",
                                                    "Visual Ads Downloaded Sucessfully....", new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View v) {
                                                            finish();
                                                        }
                                                    });
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {

                                    }
                                });
                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                                listView.setLayoutManager(mLayoutManager);
                                listView.setItemAnimator(new DefaultItemAnimator());
                                listView.setAdapter(visualAdsDownloadAdaptor);
                                ViewCompat.setNestedScrollingEnabled(
                                        listView, false);
                            }

                            @Override
                            public void onResponse(Bundle response) {
                                parser_worktype(response);
                            }


                        })
                );
    }

    private void parser_worktype(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                visualAds.clear();
                ArrayList<up_down_ftp.mFTPFile> directoryFiles1 = null;
                while (directoryFiles1 == null) {
                    directoryFiles1 = new up_down_ftp().getDirectoryFiles(context, "");
                }
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject c = jsonArray1.getJSONObject(i);
                    mVisualAds visualAd = new mVisualAds(c.getString("ITEM_NAME"), c.getString("FILE_NAME"), c.getString("ITEM_NAME").equals("CATALOG"));
                    visualAd.setDirectory("/visualaid");

                    if (visualAd.isFolderYN()) {
                        ArrayList<up_down_ftp.mFTPFile> directoryFiles = null;
                        while (directoryFiles == null) {
                            directoryFiles = new up_down_ftp().getDirectoryFiles(context, "/"+visualAd.getItemName());
                        }
                        //if (directoryFiles != null) {
                            for (up_down_ftp.mFTPFile directoryFile : directoryFiles) {
                                mVisualAds DvisualAd = new mVisualAds(directoryFile.getFileName(), directoryFile.getFileName(), false);
                                DvisualAd.setDirectory(directoryFile.getDirectory());
                                visualAds.add(DvisualAd);
                           // }
                        }
                    } else {
                        for (up_down_ftp.mFTPFile directoryFile : directoryFiles1) {
                            if (directoryFile.getFileName().contains(".")
                                    && visualAd.getFileName().equalsIgnoreCase(directoryFile.getFileName().substring(0, directoryFile.getFileName().lastIndexOf(".")))) {
                                visualAd.setFileName(directoryFile.getFileName());
                                visualAds.add(visualAd);

                            }else if (directoryFile.getFileName().contains("_")
                                    && visualAd.getFileName().equalsIgnoreCase(directoryFile.getFileName().substring(0, directoryFile.getFileName().lastIndexOf("_")))) {
                                {
                                    visualAd.setFileName(directoryFile.getFileName());
                                    visualAds.add(visualAd);
                                }
                            }
                        }
                    }
//                    if (visualAds.size()>0)
//                        break;
                }

            } catch (JSONException e) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    public void run() {
                        Log.d("MYAPP", "objects are: " + e.toString());
                        AppAlert.getInstance().getAlert(context, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                        e.printStackTrace();
                    }
                });

            }


        }


    }
}
