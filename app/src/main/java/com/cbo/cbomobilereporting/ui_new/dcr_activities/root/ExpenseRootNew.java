package com.cbo.cbomobilereporting.ui_new.dcr_activities.root;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mDA;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.ExpenseNew.ExpenseAdapterNew;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import services.CboServices;
import services.MyAPIService;
import services.ServiceHandler;
import utils.adapterutils.Expenses_Adapter;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class ExpenseRootNew extends AppCompatActivity   {
     private RecyclerView  rTalist, rDalist;
     private TextView headertext;
     private Button  samplebtn;
     private Toolbar  toolbar;

     private String dcr_id = "",paid = "";
     private  int PA_ID;
     private Context context;
     private Custom_Variables_And_Method custom_variables_and_method;



    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    String picturePath="";
    private File output=null;
    String filename="";
    TableLayout DA_layout;
    private ArrayList<mDA> DA_Types = new ArrayList<>();
    private LinearLayout ta_expenses_layout;

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET=1,MESSAGE_INTERNET_SAVE_EXPENSE=2,MESSAGE_INTERNET_DCR_COMMITEXP=3,MESSAGE_INTERNET_DCR_DELETEEXP=4;


    Spinner exphead;
    EditText daAmt,da_root, distAmt, exhAmt, rem, rem_final;
    Button save, save_exp,add_exp,add_ta_exp;
    ListView mylist,talist;
    RecyclerView rmylist, rtalist;

    RecyclerView mylist2,talist2;
    TextView datype,distAmt1, distanse, textRemark,attach_txt,routeStausTxt;
    Expenses_Adapter sm;
    LinearLayout mainlayout,actual_fare_layout,actual_DA_layout,manual_DA_layout;
    SpinAdapter adapter,adapter1;

    Custom_Variables_And_Method customVariablesAndMethod;

    CBO_DB_Helper cbohelp;
    String  ttl_distance = "", exp_id = "",exp_hed = "",my_Amt="",my_rem="",id="";
    String DistRate = "", datype_val = "";
    String dist_id3 = "";
    ArrayList<String> station, exp_head;
    String chm_ok = "", stk_ok = "", exp_ok = "";
    ArrayList<String> rootdata = new ArrayList<String>();
    ServiceHandler myServiceHandler;
    String value;
    Boolean resultTrue, myval=false;
    ImageView attach_img,attachnew;
    String ROUTE_CLASS = "",ACTUALDA_FAREYN = "",ACTUALFAREYN_MANDATORY="";
    Double ACTUALFARE_MAXAMT = 0D;
    Button btn_DaType;
    ImageView DaType_img;

    ArrayList<Map<String, String>> data = null;
    AlertDialog myalertDialog = null;

    ExpenseAdapterNew expenseAdapterNew;







    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_expense_root);

        PA_ID= Custom_Variables_And_Method.PA_ID;
        dcr_id= Custom_Variables_And_Method.DCR_ID;
        paid = "" + PA_ID;

        context=this;
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }
        toolbar= (Toolbar) findViewById(R.id.toolbar_hadder);
       headertext = (TextView) findViewById(R.id.hadder_text_1);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        headertext.setText("Expense ");








        showExpenseToUi(paid,dcr_id,context);
    }

    private void showExpenseToUi(String paid, String dcr_id, Context context) {

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iPaId",paid);
        request.put("iDcrId", dcr_id);

        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);
        tables.add(1);
        tables.add(2);
        tables.add(3);

        new MyAPIService(context)
                .execute(new ResponseBuilder("DCREXPDDLALLROUTE_MOBILE", request)
                        .setTables(tables)
                        .setDescription("Please Wait..")
                        .setResponse(new CBOServices.APIResponse() {
                            @Override
                            public void onComplete(Bundle message) throws JSONException {


                                parser1(message);

                            }

                            @Override
                            public void onResponse(Bundle response) throws JSONException {

                            }

                            @Override
                            public void onError(String s, String s1) {
                                AppAlert.getInstance().getAlert(context,s, s1);

                            }


                        })
                );

    }

    private void parser1(Bundle message) {

        if (message!=null ){

            try {

                ArrayList<SpinnerModel> newlist = new ArrayList<SpinnerModel>();
                newlist.add(new SpinnerModel("--Select--", "","0"));

                cbohelp.delete_EXP_Head();

                String table0 = message.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                    newlist.add(new SpinnerModel(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("DA_ACTION")));

                    cbohelp.Insert_EXP_Head(jsonObject1.getString("FIELD_NAME"), jsonObject1.getString("ID"),
                            jsonObject1.getString("MANDATORY"), jsonObject1.getString("DA_ACTION"),
                            jsonObject1.getString("EXP_TYPE"), jsonObject1.getString("ATTACHYN"),
                            jsonObject1.getString("MAX_AMT"), jsonObject1.getString("TAMST_VALIDATEYN"));

                }
                if (newlist.size() > 0) {
                    adapter = new SpinAdapter(getApplicationContext(), R.layout.spin_row, newlist);
                    //adapter3.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


                } else {
                    customVariablesAndMethod.msgBox(context,"No ExpHead found...");

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
                JSONObject object = null;
                String table2 = message.getString("Tables2");
                JSONArray jsonArray3 = new JSONArray(table2);
                for (int i = 0; i < jsonArray3.length(); i++) {
                    object = jsonArray3.getJSONObject(i);

                    rootdata.add((object.getString("DA_TYPE")));
                    rootdata.add((object.getString("FARE")));
                    rootdata.add((object.getString("ACTUALFAREYN")));
                    ROUTE_CLASS = object.getString("ROUTE_CLASS");
                    ACTUALDA_FAREYN = object.getString("ACTUALDA_FAREYN");
                    ACTUALFAREYN_MANDATORY  = object.getString("ACTUALFAREYN_MANDATORY");
                    ACTUALFARE_MAXAMT  = object.getDouble("ACTUALFARE_MAXAMT");
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"ACTUALFAREYN",object.getString("ACTUALFAREYN"));



                    String MyDaType = object.getString("DA_TYPE_NEW");
                    String da_val = object.getString("DA_RATE_NEW");
                    /*Float rate = Float.parseFloat(one.getString("FARE_RATE"));
                    Float kms = Float.parseFloat(one.getString("KM"));

                    if (MyDaType.equals("L")) {
                        da_val = one.getString("DA_L_RATE");
                    } else if (MyDaType.equals("EX") || MyDaType.equals("EXS")) {
                        da_val = one.getString("DA_EX_RATE");
                    } else if (MyDaType.equals("NSD") || MyDaType.equals("NS")) {
                        da_val = one.getString("DA_NS_RATE");
                    }
                    String distance_val = "0";
                    if (MyDaType.equals("EX") || MyDaType.equals("NSD")) {
                        distance_val = "" + (kms * rate * 2);

                    } else {
                        distance_val = "" + (kms * rate);
                    }*/

                    datype_val = MyDaType;
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"DA_TYPE",MyDaType);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"da_val",da_val);
                    customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"distance_val",object.getString("TA_AMT_NEW"));
                }


                DA_Types.clear();
                String table3 = message.getString("Tables3");
                JSONArray jsonArray4 = new JSONArray(table3);
                for (int i = 0; i < jsonArray4.length(); i++) {
                    JSONObject object1 = jsonArray4.getJSONObject(i);
                    mDA da = new mDA();
                    da.setCode(object1.getString("FIELD_CODE"));
                    da.setName(object1.getString("FIELD_NAME"));
                    da.setMultipleFactor(object1.getDouble("FARE_MULT_BY"));

                    if (object != null) {
                        da.setTA_Km(object.getDouble("KM_SINGLE_SIDE"));
                        da.setTA_Rate(object.getDouble("FARE_RATE"));
                        switch (da.getCode()) {
                            case "L":
                                da.setDAAmount(object.getDouble("DA_L_RATE"));
                                break;
                            case "EX":
                            case "EXS":
                                da.setDAAmount(object.getDouble("DA_EX_RATE"));
                                break;
                            case "NS":
                            case "NSD":
                                da.setDAAmount(object.getDouble("DA_NS_RATE"));
                                break;
                        }
                    }
                    if (datype_val.equalsIgnoreCase(da.getCode())){
                        btn_DaType.setText(da.getName());
                    }
                    DA_Types.add(da);
                }



                if (object != null){
                    if (object.getString("DA_TYPE_MANUALYN").equalsIgnoreCase("Y")){
                        manual_DA_layout.setVisibility(View.VISIBLE);
                    }else{
                        manual_DA_layout.setVisibility(View.GONE);
                    }
                }



                data=cbohelp.get_Expense();
               // #pchnage
                sm = new Expenses_Adapter(ExpenseRootNew.this, data);
                mylist.setAdapter(sm);

                //#p
                //  init_DA_type(DA_layout);


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
                    datype.setText(rootdata.get(0));
                    distanse.setText(rootdata.get(1));
                    ta_expenses_layout.setVisibility(View.VISIBLE);
                    //distance chngelksdl;sdf;s



                    if(rootdata.get(2).equalsIgnoreCase("y")){
                        distAmt.setText(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"ACTUALFARE",""));
                        actual_fare_layout.setVisibility(View.VISIBLE);
                    }else{
                        actual_fare_layout.setVisibility(View.GONE);
                        distAmt.setText("");
                    }

                } else {
                    customVariablesAndMethod.msgBox(context,"No RootData found");
                }
                progress1.dismiss();
            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this,"Missing field error",getResources().getString(R.string.service_unavilable) +e.toString());
                e.printStackTrace();
            }

        }
        else
        {
            AppAlert.getInstance().getAlert(context,"Server Error","Data not Found");

        }
    }


}
