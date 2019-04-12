package com.cbo.cbomobilereporting.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.eExpanse;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mExpHead;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import locationpkg.Const;
import services.CboServices;
import services.ServiceHandler;
import utils.adapterutils.Expenses_Adapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.GPS_Timmer_Dialog;
import utils_new.GalleryUtil;
import utils_new.up_down_ftp;

public class NonWorking_DCR extends AppCompatActivity implements Expenses_Adapter.Expense_interface, up_down_ftp.AdapterCallback {

    TextView textRemark;
    ImageView attach_img;
    String filename = "", DA_typ = "";
    private final int GALLERY_ACTIVITY_CODE = 200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA = 201;
    String picturePath = "";
    private File output = null;
    TableLayout DA_layout;
    EditText distAmt, exhAmt, rem_final;
    TextView distAmt1,attach_txt,routeStausTxt;
    Button add_exp;
    Spinner exphead;
    String exp_hed = "", my_Amt = "", my_rem = "", id = "";

    ArrayList<Map<String, String>> data = null;

    public ProgressDialog progress1;
    private static final int MESSAGE_INTERNET_ROOT = 1, MESSAGE_INTERNET_AREA = 5, MESSAGE_INTERNET_SAVE_EXPENSE = 2,
            MESSAGE_INTERNET_DCR_COMMITEXP = 3, MESSAGE_INTERNET_DCR_DELETEEXP = 4, MESSAGE_INTERNET_DCR_COMMITDCR = 6, GPS_TIMMER = 7;


    Spinner da_spin, dis_spin, exp_spin;
    EditText datype_edit, dis_edit, exp_head_edit, exp_remark, final_remark, loc, rootDatype, route_distance;
    TextView rootDa, rootDistance;
    Button exp_save, dcr_save;
    ListView show_exp;
    LinearLayout loclayout, area_distance_view, areaView;
    HorizontalScrollView rootView, route_distance_view;
    ResultSet rs;
    int PA_ID;
    CBO_DB_Helper cbohelp;
    Expenses_Adapter sm;
    SpinAdapter adapter, adapter1, adapter3;
    String DistRate = "", datype_val = "";
    String paid = "", ttl_distance = "", exp_id = "";
    String dcr_id = "";
    String datype_local = "", datype_ex = "", datype_ns = "";
    String dist_id3 = "";
    ArrayList<String> station, exp_head;
    ArrayList<SpinnerModel> mylist2 = new ArrayList<SpinnerModel>();
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    String routeNeeded;
    ServiceHandler myService;
    CBO_DB_Helper myCboDbHelper;
    ArrayList<String> rootdata = new ArrayList<String>();
    String value, back_allowed = "Y";
    String finalResponse;
    Boolean resultTF;
    ArrayList<SpinnerModel> data1;
    ImageView attachnew;
    LinearLayout actual_fare_layout,actual_DA_layout;
    String ROUTE_CLASS = "",ACTUALDA_FAREYN = "";
    EditText da_root;


    public void onCreate(Bundle b) {
        super.onCreate(b);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        setContentView(R.layout.nonworking_dcr);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        context = this;
        progress1 = new ProgressDialog(this);

        customVariablesAndMethod = Custom_Variables_And_Method.getInstance();
        myCboDbHelper = new CBO_DB_Helper(context);

        loclayout = (LinearLayout) findViewById(R.id.layout2_nonworkingdcr);
        actual_fare_layout = (LinearLayout) findViewById(R.id.actual_fare_layout);
        actual_DA_layout = findViewById(R.id.actual_DA_layout);
        da_root = (EditText) findViewById(R.id.da_root);

        rootView = (HorizontalScrollView) findViewById(R.id.root_view);
        areaView = (LinearLayout) findViewById(R.id.area_view);
        route_distance_view = (HorizontalScrollView) findViewById(R.id.root_distance_view);
        area_distance_view = (LinearLayout) findViewById(R.id.area_distance_view);
        rootDistance = (TextView) findViewById(R.id.meet_da_distance_root);
        rootDa = (TextView) findViewById(R.id.meet_da_type_root);
        rootDatype = (EditText) findViewById(R.id.meet_ex_da_root);
        route_distance = (EditText) findViewById(R.id.meet_ex_dis_root);
        da_spin = (Spinner) findViewById(R.id.da_type_nonworkingdcr);
        dis_spin = (Spinner) findViewById(R.id.da_distance_nonworkingdcr);
        datype_edit = (EditText) findViewById(R.id.ex_da_nonworkingdcr);
        dis_edit = (EditText) findViewById(R.id.ex_dis_nonworkingdcr);
        final_remark = (EditText) findViewById(R.id.final_remark_nonworkingdcr);
        loc = (EditText) findViewById(R.id.loc_nonworkingdcr);
        dcr_save = (Button) findViewById(R.id.save_final_nonworkingdcr);
        show_exp = (ListView) findViewById(R.id.list_exp_nonworkingdcr);
        cbohelp = new CBO_DB_Helper(getApplicationContext());
        PA_ID = Custom_Variables_And_Method.PA_ID;
        dcr_id = Custom_Variables_And_Method.DCR_ID;
        myService = new ServiceHandler(context);

        add_exp = (Button) findViewById(R.id.add_exp);
        DA_layout = (TableLayout) findViewById(R.id.DA_layout);
        distAmt = (EditText) findViewById(R.id.ex_dis_root);
        distAmt1 = (TextView) findViewById(R.id.ex_dis_root1);
        attach_txt = (TextView) findViewById(R.id.attach_txt);
        routeStausTxt = findViewById(R.id.ROUTE_CLASS);

        loc.setText(Custom_Variables_And_Method.global_address);

        if (Custom_Variables_And_Method.location_required.equals("N")) {
            loclayout.setVisibility(View.GONE);
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        routeNeeded = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "root_needed");
        Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            textView.setText("Submit Dcr");
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        Intent intent = getIntent();
        if (intent.getStringExtra("Back_allowed") != null) {
            back_allowed = intent.getStringExtra("Back_allowed");
        }

        //ViewCompat.setNestedScrollingEnabled(show_exp, true);
        if (routeNeeded != null) {
            if (routeNeeded.equals("Y")) {
                rootView.setVisibility(View.VISIBLE);
                areaView.setVisibility(View.GONE);
                route_distance_view.setVisibility(View.VISIBLE);
                area_distance_view.setVisibility(View.GONE);

                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", cbohelp.getCompanyCode());
                request.put("iPaId", "" + PA_ID);
                request.put("iDcrId", dcr_id);

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);
                tables.add(1);
                tables.add(2);

                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();

                new CboServices(this, mHandler).customMethodForAllServices(request, "DCREXPDDLALLROUTE_MOBILE", MESSAGE_INTERNET_ROOT, tables);

                //End of call to service

               /* new RootData().execute();*/
            } else {
                rootView.setVisibility(View.GONE);
                areaView.setVisibility(View.VISIBLE);
                route_distance_view.setVisibility(View.GONE);
                area_distance_view.setVisibility(View.VISIBLE);

                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", cbohelp.getCompanyCode());
                request.put("iPA_ID", "" + PA_ID);

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);
                tables.add(1);
                tables.add(2);
                tables.add(3);

                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();

                new CboServices(this, mHandler).customMethodForAllServices(request, "ExpenseEntryDDL_Mobile", MESSAGE_INTERNET_AREA, tables);

                //End of call to service
            }
        }


        data1 = new ArrayList<SpinnerModel>();

        data1.add(new SpinnerModel("--Select--"));

        exp_head = new ArrayList<String>();

        add_exp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //capture_Image();
                Add_expense("0", "", "", "", "", "");
            }
        });

        //================================================================================================================
        da_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                datype_val = ((TextView) arg1.findViewById(R.id.spin_name)).getText().toString();

                if ((datype_val.equals("Local")) || (datype_val.equals("--Select--")) || (datype_val.equals("DA Not Applicable"))) {
                    area_distance_view.setVisibility(View.GONE);

                } else {
                    area_distance_view.setVisibility(View.VISIBLE);
                }
                if (datype_val.equals("--Select--")) {
                    datype_edit.setText("");
                } else if (datype_val.equals("DA Not Applicable")) {
                    datype_edit.setText("0");
                    dis_edit.setText("0");
                } else if (datype_val.equals("Local")) {

                    datype_edit.setText(datype_local);
                    dis_edit.setText("0");

                } else if (datype_val.equals("Ex-Station Double Side") || datype_val.equals("Ex-Station Single Side")) {

                    datype_edit.setText(datype_ex);


                } else {

                    datype_edit.setText(datype_ns);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }
        });

        station = new ArrayList<String>();

        dis_spin.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {

                dist_id3 = ((TextView) arg1.findViewById(R.id.spin_id)).getText().toString();

                if (datype_val.equals("Ex-Station Double Side") || datype_val.equals("Out-Station Double Side")) {


                    ttl_distance = ((TextView) arg1.findViewById(R.id.spin_name)).getText().toString();
                    if (!ttl_distance.contains("----->")) {
                        dis_edit.setText("");
                    } else {
                        String Distance1[] = ttl_distance.split("----->");

                        String ActDistance = Distance1[2];
                        String Act_dist1[] = ActDistance.split("K");
                        String MyDistance = Act_dist1[0].trim();
                        float fare_rate = Float.parseFloat(DistRate);
                        Float in = Float.parseFloat(MyDistance);
                        float res = in * 2 * fare_rate;
                        String MyData = "" + res;

                        dis_edit.setText(MyData);
                    }
                } else if (datype_val.equals("Ex-Station Single Side") || datype_val.equals("Out-Station Single Side")) {

                    ttl_distance = ((TextView) arg1.findViewById(R.id.spin_name)).getText().toString();
                    if (!ttl_distance.contains("----->")) {
                        dis_edit.setText("");
                    } else {
                        String Distance1[] = ttl_distance.split("----->");

                        String ActDistance = Distance1[2];
                        String Act_dist1[] = ActDistance.split("K");
                        String MyDistance = Act_dist1[0].trim();
                        float fare_rate = Float.parseFloat(DistRate);
                        int in = Integer.parseInt(MyDistance);
                        float res = in * 1 * fare_rate;
                        String MyData = "" + res;

                        dis_edit.setText(MyData);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        //======================================================================================================================

        dcr_save.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                dcrCommit(false);
            }
        });





        attachnew = (ImageView) findViewById(R.id.attachnew);
        attachnew.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                exp_id="-1";
                //capture_Image();
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.getMenu().add("Camera");
                menu.getMenu().add("Gallery");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Camera")){
                            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(NonWorking_DCR.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //takePictureButton.setEnabled(false);
                                ActivityCompat.requestPermissions(NonWorking_DCR.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, NonWorking_DCR.this.REQUEST_CAMERA);
                                Toast.makeText(v.getContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                            }else {

                                capture_Image();
                            }
                        }else{
                            if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //takePictureButton.setEnabled(false);
                                ActivityCompat.requestPermissions(NonWorking_DCR.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, NonWorking_DCR.this.GALLERY_ACTIVITY_CODE);
                                Toast.makeText(v.getContext(), "Please allow the permission", Toast.LENGTH_LONG).show();

                            }else {
                                open_galary();

                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
    }


    public static void justifyListViewHeightBasedOnChildren (ListView listView) {

        ListAdapter adapter = listView.getAdapter();

        if (adapter == null) {
            return;
        }
        ViewGroup vg = listView;
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, vg);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams par = listView.getLayoutParams();
        par.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        listView.setLayoutParams(par);
        listView.requestLayout();
    }

    private void dcrCommit(boolean Skip_Verification){
        ArrayList<Map<String, String>> mandatory_pending_exp_head = null;
        mandatory_pending_exp_head = cbohelp.get_mandatory_pending_exp_head();
        if (loc.getText().toString().equals("")) {
            loc.setText("Unknown Location");
        }
        if (final_remark.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context, "Enter Remark First...");
        }else if (mandatory_pending_exp_head.size() != 0) {

            String pending_list = "";
            for (int i = 0; i < mandatory_pending_exp_head.size(); i++) {
                pending_list += mandatory_pending_exp_head.get(i).get("PA_NAME") + "\n";
            }
            customVariablesAndMethod.getAlert(context, "Expenses Pending", pending_list);

        }else if(cbohelp.get_DA_ACTION_exp_head().size()>0  && actual_DA_layout.getVisibility() == View.VISIBLE
                && !da_root.getText().toString().isEmpty() && !da_root.getText().toString().equals("0")){
            AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                    "Please make DA amount Rs 0.", new OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
        } else if (routeNeeded.equals("Y")) {

            if ( actual_fare_layout.getVisibility()==View.VISIBLE && distAmt.getText().toString().equals("")) {
                customVariablesAndMethod.msgBox(context, "Please Enter the Actual Fare....");
            }
//            else if (mandatory_pending_exp_head.size() != 0) {
//
//                String pending_list = "";
//                for (int i = 0; i < mandatory_pending_exp_head.size(); i++) {
//                    pending_list += mandatory_pending_exp_head.get(i).get("PA_NAME") + "\n";
//                }
//                customVariablesAndMethod.getAlert(context, "Expenses Pending", pending_list);
//
//            }

            else if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"EXP_ATCH_YN","N").equals("Y") &&  actual_fare_layout.getVisibility()==View.VISIBLE && attach_txt.getText().toString().equals("* Attach Picture....")) {
                customVariablesAndMethod.msgBox(context,"Please Attach supporting File for Actual Fare....");
            }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
                customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                        new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
            }else if ( actual_fare_layout.getVisibility()==View.VISIBLE ){

                progress1.setMessage("Please Wait..\nuploading Image");
                progress1.setCancelable(false);
                progress1.show();
                exp_id = "-1";
                my_Amt = distAmt.getText().toString();
                my_rem = "Actual Fare";

                if(!attach_txt.getText().toString().equals("* Attach Picture....")) {
                    filename = attach_txt.getText().toString();
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator +  filename);
                    new up_down_ftp().uploadFile(file2,NonWorking_DCR.this);
                }else{
                    filename = "";
                    other_expense_commit();
                }


            }else {

                expense_commit();

            }
        } else {
            if (datype_val.equals("--Select--")) {
                customVariablesAndMethod.msgBox(context, "Select Your DA Type");
            }else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
                customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                        new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
            } else {

                //Start of call to service

                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", cbohelp.getCompanyCode());
                request.put("iDcrId", dcr_id);
                request.put("sDaType", datype_val);
                request.put("iDistanceId", dist_id3);

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);

                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();

                new CboServices(NonWorking_DCR.this, mHandler).customMethodForAllServices(request, "DCR_COMMITEXP_1", MESSAGE_INTERNET_DCR_COMMITEXP, tables);

                //End of call to service
                //new finalSubmitInBackground().execute(dcr_id, final_remark.getText().toString(), loc.getText().toString());

            }
        }
    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);
            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            //new GPS_Timmer_Dialog(context,mHandler,"Final Submit in Process...",GPS_TIMMER).show();
            dcrCommit(true);
        }
    };

    private void expense_commit(){

        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val",da_root.getText().toString().isEmpty()? "0" : da_root.getText().toString());

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder",cbohelp.getCompanyCode());
        request.put("iDcrId", dcr_id);
        request.put("sDaType", datype_val);
        request.put("iDistanceId", dist_id3);
        request.put("iDA_VALUE", customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(NonWorking_DCR.this,mHandler).customMethodForAllServices(request,"DCR_COMMITEXP_2",MESSAGE_INTERNET_DCR_COMMITEXP,tables);

        //End of call to service
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE",distAmt.getText().toString());
        distAmt.setText("");

        if (actual_fare_layout.getVisibility()!=View.VISIBLE){
            customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFARE","0");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        if (back_allowed.equals("Y")) {
            finish();
        } else {
            customVariablesAndMethod.getAlert(context, "Please Submit", "Please complete your Final Submit");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {

            if (back_allowed.equals("Y")) {
                finish();
            } else {
                customVariablesAndMethod.getAlert(context, "Please Submit", "Please complete your Final Submit");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void open_galary() {
        Intent gallery_Intent = new Intent(context, GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                    //return true;
                }
            }
            filename = PA_ID + "_" + dcr_id + "_" + exp_id + ".jpg";
            output = new File(dir, filename);

//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put(MediaStore.Images.ImageColumns.DATA, output.getPath());
            Uri fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
            // fileUri = Uri.fromFile(output);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }

    private void capture_Image() {
        captureImage();
     /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }
         filename = PA_ID+"_"+dcr_id+"_"+exp_id+".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                filename = PA_ID + "_" + dcr_id + "_" + exp_id + ".jpg";
                Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
                /*performCrop(picturePath);*/
                moveFile(picturePath);
            }
        }

        if (requestCode == RESULT_CROP) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert selectedBitmap != null;
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                        //return true;
                    } else {
                        try {
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                            file2.createNewFile();
                            FileOutputStream fo = new FileOutputStream(file2);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                            attach_img.setImageBitmap(myBitmap);

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    try {
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);

                        file2.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file2);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                        attach_img.setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);

                if (file1.exists()) {
                    /*performCrop(file1.getPath());*/
                    if (exp_id.equals("-1")){
                        attach_txt.setText(filename);
                    }else{
                        previewCapturedImage(file1.getPath());
                    }
                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                capture_Image();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == this.GALLERY_ACTIVITY_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                open_galary();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void moveFile(String inputFileUri) {

        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO");
            //create output directory if it doesn't exist
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFileUri);
            out = new FileOutputStream(dir + File.separator + filename);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;

            if (exp_id.equals("-1")){
                attach_txt.setText(filename);
            }else{
                previewCapturedImage(dir + File.separator + filename);
            }



        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            attach_img.setVisibility(View.VISIBLE);
            attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void Add_expense(String who, String hed, String amt, String rem, final String path, String hed_id) {
        filename = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("ADD", null)
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.add_exp, null);
        exphead = (Spinner) dialogLayout.findViewById(R.id.exp_head_root);
        exhAmt = (EditText) dialogLayout.findViewById(R.id.ex_head_root);
        TextView head = (TextView) dialogLayout.findViewById(R.id.head);
        textRemark = (TextView) dialogLayout.findViewById(R.id.text_remark);
        final TextView ex_head_root_txt = (TextView) dialogLayout.findViewById(R.id.ex_head_root_txt);
        rem_final = (EditText) dialogLayout.findViewById(R.id.exp_remark_root);
        final CheckBox add_attachment = (CheckBox) dialogLayout.findViewById(R.id.add_attachment);
        final RadioGroup attach_option = (RadioGroup) dialogLayout.findViewById(R.id.attach_option);
        attach_img = (ImageView) dialogLayout.findViewById(R.id.attach_img);
        attach_img.setVisibility(View.GONE);
        final String[] DA_ACTION = {"0"};

        String ext = path;
        attach_option.setVisibility(View.GONE);


        final mExpHead[] expHead = {null};
        final Boolean[] keyPressed = {true};

        add_attachment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!exp_id.equals("")) {
                    if (add_attachment.isChecked()) {
                        attach_option.setVisibility(View.VISIBLE);
                    } else {
                        attach_option.setVisibility(View.GONE);
                    }
                } else {
                    customVariablesAndMethod.msgBox(context, "Please select the Expense head");
                    add_attachment.setChecked(false);
                }
            }
        });
        attach_option.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = attach_option.getCheckedRadioButtonId();
                if (id == R.id.attach) {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        ActivityCompat.requestPermissions(NonWorking_DCR.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_ACTIVITY_CODE);
                        Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                    } else {
                        open_galary();

                    }

                } else if (id == R.id.cam) {

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //takePictureButton.setEnabled(false);
                        ActivityCompat.requestPermissions(NonWorking_DCR.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
                        Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                    } else {

                        capture_Image();
                    }

                }

            }

        });

        exphead.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                exp_id = ((TextView) arg1.findViewById(R.id.spin_id)).getText().toString();
                exp_hed = ((TextView) arg1.findViewById(R.id.spin_name)).getText().toString();
                DA_ACTION[0] = ((SpinnerModel)adapter.data.get(arg2)).getPANE_TYPE();
                filename = "";
                attach_img.setImageDrawable(null);
                add_attachment.setChecked(false);


                expHead[0] = cbohelp.getEXP_Head(exp_id);
                Boolean allreadyAdded = false;
                if (who.equals("0")){
                    if (cbohelp.get_ExpenseTypeAdded(expHead[0].getEXP_TYPE_STR()).size() >0
                            && expHead[0].getEXP_TYPE() != eExpanse.None) {
                        allreadyAdded = true;

                    }
                }
                if (!allreadyAdded) {
                    if (exp_id.equals("3119")) {
                        exhAmt.setHint("K.M.");
                        textRemark.setText("K.M. Remark.");
                        ex_head_root_txt.setText("K.M.");
                    } else {
                        exhAmt.setHint("Amt.");
                        textRemark.setText("Exp Remark.");
                        ex_head_root_txt.setText("Amount");

                    }
                }else{
                    AppAlert.getInstance().Alert(context, "Alert!!!",
                            expHead[0].getEXP_TYPE().name() +" allready submitted in another Head",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    exphead.setSelection(0);
                                }
                            });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        exhAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //if (expHead[0] != null) {
                    Double amt = s.toString().trim().isEmpty() ? 0D : Double.parseDouble(s.toString());
                    Double maxAmt = expHead[0].getMAX_AMT();
                    if (maxAmt == 0) {
                        maxAmt = amt;
                    }
                    if (keyPressed[0]) {
                        keyPressed[0] = false;
                        if (amt > maxAmt) {
                            Double finalMaxAmt = maxAmt;
                            AppAlert.getInstance().Alert(context, "Alert!!!",
                                    "You are only allowed to more then " + maxAmt,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            exhAmt.setText("" + finalMaxAmt);
                                        }
                                    });

                        }
                        keyPressed[0] = true;
                    }
               // }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dialog.setView(dialogLayout);
        dialog.setTitle("Add Other Expences");
        dialog.show();

        if (who.equals("0")) {
            adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, cbohelp.get_ExpenseHeadNotAdded());
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
            exphead.setAdapter(adapter);
            head.setVisibility(View.GONE);
        } else {

            head.setVisibility(View.VISIBLE);
            exphead.setVisibility(View.GONE);
            head.setText(hed);
            exp_id = hed_id;
            exp_hed = hed;

            expHead[0] = cbohelp.getEXP_Head(exp_id);


            exhAmt.setText(amt);
            rem_final.setText(rem);
            ext = path.substring(path.lastIndexOf("/") + 1);
            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + ext);

            if (file2.exists() && !ext.equals("")) {
                previewCapturedImage(file2.getPath());
            }
        }


        final String finalExt = ext;
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_Amt = exhAmt.getText().toString();
                my_rem = rem_final.getText().toString();


                //mycon.msgBox(dist_id3);
                if (exp_id.equals("")) {
                    customVariablesAndMethod.msgBox(context, "First Select the Expense Head...");
                } else if (my_Amt.trim().isEmpty()) {
                    customVariablesAndMethod.msgBox(context, "Please Enter the Expense Amt....");
                } else if (Double.valueOf(my_Amt) == 0) {
                    customVariablesAndMethod.msgBox(context, "Expense Amt. can't be zero...");
                } else if (my_rem.trim().isEmpty()) {
                    customVariablesAndMethod.msgBox(context, "Please Enter the Remark....");
                } else if (DA_ACTION[0].equals("1") && actual_DA_layout.getVisibility() == View.VISIBLE
                        && !da_root.getText().toString().equals("0") && !da_root.getText().toString().isEmpty()) {
                    AppAlert.getInstance().Alert(context, "Already Applied for DA...",
                            "Please make DA amount Rs 0.", new OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }else if (expHead[0].getATTACHYN() != 0
                        && filename.equalsIgnoreCase("")
                        && path.equalsIgnoreCase("")) {
                    customVariablesAndMethod.msgBox(context,"Please add an attachment....");
                }else {


                    if (!filename.equals("")) {
                        progress1.setMessage("Please Wait..\nuploading Image");
                        progress1.setCancelable(false);
                        progress1.show();
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                        new up_down_ftp().uploadFile(file2, context);

                    }
                    if (!path.equals("")) {
                        filename = finalExt;
                    }
                    dialog.dismiss();

                    if (filename.equals("")) {
                        other_expense_commit();
                    }


                }
            }
        });
    }

    private void other_expense_commit() {
        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iDcrId", dcr_id);
        request.put("iExpHeadId", exp_id);
        request.put("iAmount", my_Amt);
        request.put("sRemark", my_rem);
        request.put("sFileName", filename);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(NonWorking_DCR.this, mHandler).customMethodForAllServices(request, "DCREXPCOMMITMOBILE_2", MESSAGE_INTERNET_SAVE_EXPENSE, tables);

        //End of call to service
        //exhAmt.setText("");
        //rem_final.setText("");
        //dialog.dismiss();
    }

    @Override
    public void Edit_Expense(String who, String hed, String amt, String rem, String path, String hed_id) {
        Add_expense(who, hed, amt, rem, path, hed_id);
    }

    @Override
    public void delete_Expense(final String hed_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Start of call to service
                exp_id = hed_id;
                HashMap<String, String> request = new HashMap<>();
                request.put("sCompanyFolder", cbohelp.getCompanyCode());
                request.put("iPA_ID", "" + Custom_Variables_And_Method.PA_ID);
                request.put("iDCR_ID", dcr_id);
                request.put("iID", hed_id);

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);

                progress1.setMessage("Please Wait..");
                progress1.setCancelable(false);
                progress1.show();

                new CboServices(NonWorking_DCR.this, mHandler).customMethodForAllServices(request, "DCREXPDELETEMOBILE_1", MESSAGE_INTERNET_DCR_DELETEEXP, tables);

                //End of call to service
            }
        })
                .setCancelable(false)
                .setNegativeButton("Cancel", null);
        final AlertDialog dialog = builder.create();
        dialog.setMessage("Are you sure, you want to delete");
        dialog.show();
    }

    private void init_DA_type(TableLayout stk) {

        stk.removeAllViews();
        TableRow tbrow0 = new TableRow(context);
        //tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("DA. Type");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.BLACK);
        tv0.setTypeface(null, Typeface.NORMAL);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "DA_TYPE"));
        tv1.setGravity(Gravity.RIGHT);
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.BLACK);
        tv1.setTypeface(null, Typeface.NORMAL);
        tbrow0.addView(tv1);
        stk.addView(tbrow0);

        TableRow tbrow1 = new TableRow(context);
        //tbrow1.setBackgroundColor(0xff125688);
        TextView tv10 = new TextView(context);
        tv10.setText("DA. Value");
        tv10.setPadding(5, 5, 5, 0);
        tv10.setTextColor(Color.BLACK);
        tv10.setTypeface(null, Typeface.NORMAL);
        tv10.setLayoutParams(params);
        tbrow1.addView(tv10);
        TextView tv11 = new TextView(context);
        tv11.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val"));
        tv11.setPadding(5, 5, 5, 0);
        tv11.setTextColor(Color.BLACK);
        tv11.setGravity(Gravity.RIGHT);
        tv11.setTypeface(null, Typeface.NORMAL);
        tbrow1.addView(tv11);
        stk.addView(tbrow1);

        Float Dis_val =0f;
        if (!customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "ACTUALFAREYN","").equalsIgnoreCase("Y")) {
            TableRow tbrow2 = new TableRow(context);
            //tbrow2.setBackgroundColor(0xff125688);
            TextView tv21 = new TextView(context);
            tv21.setText("TA. Value");
            tv21.setPadding(5, 5, 5, 0);
            tv21.setTextColor(Color.BLACK);
            tv21.setTypeface(null, Typeface.NORMAL);
            tv21.setLayoutParams(params);
            tbrow2.addView(tv21);
            TextView tv22 = new TextView(context);
            tv22.setText(context.getResources().getString(R.string.rs) + " " + customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val"));
            tv22.setGravity(Gravity.RIGHT);
            tv22.setPadding(5, 5, 5, 0);
            tv22.setTextColor(Color.BLACK);
            tv22.setTypeface(null, Typeface.NORMAL);
            tbrow2.addView(tv22);
            stk.addView(tbrow2);
            Dis_val = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "distance_val","0"));
        }

        TableRow tbrow4 = new TableRow(context);
        //tbrow4.setBackgroundColor(0xff125688);
        TextView tv40 = new TextView(context);
        tv40.setText("Other Value");
        tv40.setPadding(5, 5, 5, 0);
        tv40.setTextColor(Color.BLACK);
        tv40.setTypeface(null, Typeface.NORMAL);
        tv40.setLayoutParams(params);
        tbrow4.addView(tv40);
        TextView tv41 = new TextView(context);


        Float other = 0f;
        //datanum.put("amount", c.getString(c.getColumnIndex("amount")));
        for (int i = 0; i < data.size(); i++) {
            other += Float.parseFloat(data.get(i).get("amount"));
        }

        tv41.setText(context.getResources().getString(R.string.rs) + " " + other);
        tv41.setPadding(5, 5, 5, 0);
        tv41.setTextColor(Color.BLACK);
        tv41.setGravity(Gravity.RIGHT);
        tv41.setTypeface(null, Typeface.NORMAL);
        tbrow4.addView(tv41);
        stk.addView(tbrow4);

        Float net_value = Float.parseFloat(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val"))
                + Dis_val
                + other;


        TableRow tbrow3 = new TableRow(context);
        //tbrow3.setBackgroundColor(0xff125688);
        TextView tv31 = new TextView(context);
        tv31.setText("Total Expenses");
        tv31.setPadding(5, 5, 5, 0);
        tv31.setTextColor(Color.BLACK);
        tv31.setTypeface(null, Typeface.BOLD);
        tv31.setLayoutParams(params);
        tbrow3.addView(tv31);
        TextView tv32 = new TextView(context);

        tv32.setText(context.getResources().getString(R.string.rs) + " " + net_value);

        tv32.setGravity(Gravity.RIGHT);
        tv32.setPadding(5, 5, 5, 0);
        tv32.setTextColor(Color.BLACK);
        tv32.setTypeface(null, Typeface.BOLD);
        tbrow3.addView(tv32);
        stk.addView(tbrow3);

        TableRow tbrow5 = new TableRow(context);
        tbrow5.setPadding(2, 2, 2, 2);
        tbrow5.setBackgroundColor(0xff125688);
        stk.addView(tbrow5);

    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_AREA:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser1(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_SAVE_EXPENSE:
                    if (!exp_id.equals("-1")) {
                        progress1.dismiss();
                    }
                    if ((null != msg.getData())) {

                        parser2(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_DCR_COMMITEXP:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser3(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_DCR_DELETEEXP:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser4(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_ROOT:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser5(msg.getData());

                    }
                    break;
                case MESSAGE_INTERNET_DCR_COMMITDCR:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser6(msg.getData());

                    }
                    break;
                case GPS_TIMMER:
                    FinalSubmit();
                    break;
                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context, msg.getData().getString("Error"));
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser6(Bundle result) {
        if (result != null) {

            try {
                customVariablesAndMethod.SetLastCallLocation(context);

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                String value2 = object.getString("ID");

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "DA_TYPE", "0");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val", "0");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "distance_val", "0");

                customVariablesAndMethod.msgBox(context, " DCR Sucessfully Submitted");
                cbohelp.delete_Expense();
                cbohelp.delete_Nonlisted_calls();
                cbohelp.deleteDcrAppraisal();
                cbohelp.deleteFinalDcr();
                cbohelp.deleteDCRDetails();
                cbohelp.deletedcrFromSqlite();

                Custom_Variables_And_Method.CHEMIST_NOT_VISITED = "";
                Custom_Variables_And_Method.STOCKIST_NOT_VISITED = "";
                Intent i = new Intent(NonWorking_DCR.this, LoginFake.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "Final_submit", "Y");
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "work_type_Selected", "w");

                finish();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    public void parser5(Bundle result) {
        if (result != null) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", "","0"));

                cbohelp.delete_EXP_Head();

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    newlist.add(new SpinnerModel(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"), jsonObject1.getString("DA_ACTION")));

                    cbohelp.Insert_EXP_Head(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("MANDATORY"), jsonObject1.getString("DA_ACTION"),
                            jsonObject1.getString("EXP_TYPE"), jsonObject1.getString("ATTACHYN"),
                            jsonObject1.getString("MAX_AMT"), jsonObject1.getString("TAMST_VALIDATEYN"));
                }
                if (newlist.size() > 0) {
                    adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, newlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

                } else {
                    customVariablesAndMethod.msgBox(context, "No ExpHead found...");

                }

                data = new ArrayList<Map<String, String>>();
                data.clear();
                /*String table1 = result.getString("Tables1");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject object = jsonArray2.getJSONObject(i);
                    Map<String, String> datanum = new HashMap<String, String>();
                    datanum.put("exp_head_id", object.getString("EXP_HEAD_ID"));
                    datanum.put("exp_head", object.getString("EXP_HEAD"));
                    datanum.put("amount", object.getString("AMOUNT"));
                    datanum.put("remark", object.getString("REMARK"));
                    datanum.put("FILE_NAME", object.getString("FILE_NAME"));
                    datanum.put("ID", object.getString("ID"));
                    data.add(datanum);

                }*/


                rootdata.clear();
                String table2 = result.getString("Tables2");
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject object = jsonArray3.getJSONObject(i);

                    rootdata.add((object.getString("DA_TYPE")));
                    rootdata.add((object.getString("FARE")));
                    rootdata.add((object.getString("ACTUALFAREYN")));
                    ROUTE_CLASS = object.getString("ROUTE_CLASS");
                    ACTUALDA_FAREYN = object.getString("ACTUALDA_FAREYN");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFAREYN",object.getString("ACTUALFAREYN"));

                }

                data = cbohelp.get_Expense();
                sm = new Expenses_Adapter(NonWorking_DCR.this, data);
                show_exp.setAdapter(sm);
                justifyListViewHeightBasedOnChildren(show_exp);
                init_DA_type(DA_layout);


                routeStausTxt.setText(ROUTE_CLASS);
                if (ROUTE_CLASS.trim().isEmpty()){
                    routeStausTxt.setVisibility(View.GONE);
                }else{
                    routeStausTxt.setVisibility(View.VISIBLE);
                }

                da_root.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "da_val","0"));
                da_root.setEnabled(cbohelp.get_DA_ACTION_exp_head().size()==0);
                if (ACTUALDA_FAREYN.equalsIgnoreCase("y")){
                    actual_DA_layout.setVisibility(View.VISIBLE);
                }else{
                    actual_DA_layout.setVisibility(View.GONE);

                }

                if (rootdata.size() > 0) {
                    rootDa.setText(rootdata.get(0));
                    rootDistance.setText(rootdata.get(1));
                    if(rootdata.get(2).equalsIgnoreCase("y")){
                        distAmt.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ACTUALFARE",""));
                        actual_fare_layout.setVisibility(View.VISIBLE);
                    }else{
                        actual_fare_layout.setVisibility(View.GONE);
                        distAmt.setText("");
                    }
                } else {
                    customVariablesAndMethod.msgBox(context, "No RootData found");
                }
                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

    private void parser4(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                String value2 = object.getString("DCR_ID");

                cbohelp.delete_Expense_withID(exp_id);
                data = cbohelp.get_Expense();
                sm = new Expenses_Adapter(context, data);
                show_exp.setAdapter(sm);
                justifyListViewHeightBasedOnChildren(show_exp);

                da_root.setEnabled(cbohelp.get_DA_ACTION_exp_head().size()==0);

                init_DA_type(DA_layout);

                customVariablesAndMethod.msgBox(context, " Exp. Deleted Sucessfully");


            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    private void parser3(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                String value2 = object.getString("DCR_ID");

                new GPS_Timmer_Dialog(context, mHandler, "Final Submit in Process...", GPS_TIMMER).show();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    private void FinalSubmit() {
        //Start of call to service

        String ACTUALFARE = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "ACTUALFARE");
        if (ACTUALFARE.equals(""))
            ACTUALFARE = "" + 0;

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", cbohelp.getCompanyCode());
        request.put("iDCRID", dcr_id);
        request.put("iNOCHEMIST", "1");
        request.put("sNOSTOCKIST", "1");
        request.put("sCHEMISTREMARK", "");
        request.put("sSTOCKISTREMARK", "");
        request.put("iPOB", "0.0");
        request.put("iPOBQTY", "0");
        request.put("iACTUALFAREAMT", ACTUALFARE);
        request.put("sDATYPE", "NA");
        request.put("iDISTANCE_ID", "99999");
        request.put("sREMARK", final_remark.getText().toString());
        request.put("sLOC2", "" + Custom_Variables_And_Method.GLOBAL_LATLON + "!^" + loc.getText().toString());
        request.put("iOUTTIME", "99");

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);

        progress1.setMessage("Please Wait..");
        progress1.setCancelable(false);
        progress1.show();

        new CboServices(NonWorking_DCR.this, mHandler).customMethodForAllServices(request, "DCR_COMMITFINAL_1", MESSAGE_INTERNET_DCR_COMMITDCR, tables);

        //End of call to service


    }

    private void parser2(Bundle result) {
        if (result != null) {

            try {

                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);

                JSONObject object = jsonArray1.getJSONObject(0);
                value = object.getString("DCRID");
                id = object.getString("ID");

                cbohelp.insert_Expense(exp_id, exp_hed, my_Amt, my_rem, filename, id, customVariablesAndMethod.currentTime(context));

                da_root.setEnabled(cbohelp.get_DA_ACTION_exp_head().size()==0);
                customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context, "da_val",da_root.getText().toString().isEmpty()? "0" : da_root.getText().toString());


                if (!exp_id.equals("-1")){

                    data = cbohelp.get_Expense();
                    sm = new Expenses_Adapter(NonWorking_DCR.this, data);
                    show_exp.setAdapter(sm);
                    justifyListViewHeightBasedOnChildren(show_exp);

                    init_DA_type(DA_layout);

                    customVariablesAndMethod.msgBox(context, " Exp. Added Sucessfully");
                    exp_id = "";
                    exp_hed = "";
                    my_Amt = "";
                    my_rem = "";
                    filename = "";
                    id = "";
                }else{
                    //FinalSubmit();
                    expense_commit();
                }

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();
    }

    public void parser1(Bundle result) {
        if (result != null) {

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", ""));

                cbohelp.delete_EXP_Head();
                String table0 = result.getString("Tables2");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    newlist.add(new SpinnerModel(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID")));

                    cbohelp.Insert_EXP_Head(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("MANDATORY"), jsonObject1.getString("DA_ACTION"),
                            jsonObject1.getString("EXP_TYPE"), jsonObject1.getString("ATTACHYN"),
                            jsonObject1.getString("MAX_AMT"), jsonObject1.getString("TAMST_VALIDATEYN"));

                }
                actual_fare_layout.setVisibility(View.GONE);
                actual_DA_layout.setVisibility(View.GONE);
                if (newlist.size() > 0) {
                    adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, newlist);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    //exp_spin.setAdapter(adapter);


                } else {
                    customVariablesAndMethod.msgBox(context, "No ExpHead found...");

                }

                String table1 = result.getString("Tables0");
                JSONArray jsonArray2 = new JSONArray(table1);
                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
                    DistRate = jsonObject1.getString("FARE_RATE");
                    datype_local = jsonObject1.getString("DA_L");
                    datype_ex = jsonObject1.getString("DA_EX");
                    datype_ns = jsonObject1.getString("DA_NS");
                }

                mylist2.clear();
                String table2 = result.getString("Tables1");
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    JSONObject jsonObject2 = jsonArray3.getJSONObject(i);
                    mylist2.add(new SpinnerModel(jsonObject2.getString("STATION_NAME"), jsonObject2.getString("DISTANCE_ID")));
                }
                adapter1 = new SpinAdapter(context, R.layout.spin_row2, mylist2);
                adapter1.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                dis_spin.setAdapter(adapter1);

                String table4 = result.getString("Tables3");
                JSONArray jsonArray4 = new JSONArray(table4);
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject jsonObject2 = jsonArray4.getJSONObject(i);
                    data1.add(new SpinnerModel(jsonObject2.getString("PA_NAME")));//,jsonObject2.getString("DISTANCE_ID")));
                }
                adapter3 = new SpinAdapter(getApplicationContext(), R.layout.spin_row, data1);
                adapter3.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                da_spin.setAdapter(adapter3);

                data = cbohelp.get_Expense();
                sm = new Expenses_Adapter(NonWorking_DCR.this, data);
                show_exp.setAdapter(sm);
                justifyListViewHeightBasedOnChildren(show_exp);
                init_DA_type(DA_layout);

                progress1.dismiss();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
        //Log.d("MYAPP", "objects are1: " + result);
        progress1.dismiss();

    }

  /*  @Override
    public void upload_complete(final String IsCompleted) {
        progress1.dismiss();
        if (IsCompleted.equals("S")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                }
            });
        } else if (IsCompleted.equals("Y")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    other_expense_commit();
                }
            });
        } else if (IsCompleted.contains("ERROR")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                    String folder = IsCompleted.substring(6);
                    customVariablesAndMethod.getAlert(context, "Folder not found", folder + "   Invalid path \nPlease contact your administrator");
                }
            });
        } else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    customVariablesAndMethod.msgBox(context, "UPLOAD FAILED \n Please try again");
                }
            });
        }
    }

    */
    @Override
    public void started(Integer responseCode, String message, String description) {

    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {
        progress1.dismiss();
        other_expense_commit();
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }
}
