package com.cbo.cbomobilereporting.ui_new.dcr_activities.Customer;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.emp_tracking.MyCustomMethod;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils.CallBuilder;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.WorkWith.WorkWithActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.WorkWith.WorkWithBuilder;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.lead.LeadActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.lead.SamplePOBBuilder;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import locationpkg.Const;
import services.MyAPIService;
import services.Sync_service;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.PobModel;
import utils.adapterutils.SpinnerModel;
import utils.networkUtil.NetworkUtil;
import utils_new.AppAlert;
import utils_new.Chemist_Gift_Dialog;
import utils_new.Chm_Sample_Dialog;
import utils_new.Custom_Variables_And_Method;
import utils_new.Service_Call_From_Multiple_Classes;

public class CustomerCall extends CustomActivity implements iCustomerCall,ExpandableListAdapter.Summary_interface{

    private vmCustomerCall viewModel;
    Activity context;
    Button drname,get_workwithBtn,back;
    ImageView spinner_img_drCall;
    TextView loc,get_workwith;
    RelativeLayout loc_layout;
    Button products, gift,lead,add;


    String sample_name="",sample_pob="",sample_sample="";
    String gift_name="",gift_qty="";
    String lead_names="",lead_ids="";

    String sample_name_previous="",sample_pob_previous="",sample_sample_previous="";
    String gift_name_previous="",gift_qty_previous="";

    String name2 = "", name3 = "", name4 = "";
    double result = 0.0;
    String sample ="0.0",rate ="";
    String name = "", resultList="";

    TableLayout stk,gift_layout,leadView;

    LinearLayout call_layout;
    ExpandableListView summary_layout;
    Button tab_call,tab_summary;
    EditText dr_remark_edit;

    boolean IsRefreshedClicked = true;

    HashMap<String, HashMap<String, ArrayList<String>>> summary_list=new HashMap<>();
    HashMap<String, ArrayList<String>> chemist_list_summary=new HashMap<>();
    ExpandableListAdapter listAdapter;

    CBO_DB_Helper cbo_db_helper;

    private final int CALL_ACTIVITY=0,WORKWITH_ACTIVITY=1, PRODUCT_DILOG=3,
            GIFT_DILOG=2,LEAD_DILOG=4,MESSAGE_INTERNET_SEND_FCM=5,POB_DILOG=6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);
        context = this;
        cbo_db_helper = new CBO_DB_Helper(context);
        viewModel = ViewModelProviders.of(this).get(vmCustomerCall.class);
        viewModel.setView(context,this);
    }

    @Override
    public void getReferencesById() {
        loc_layout = findViewById(R.id.loc_layout);
        loc = findViewById(R.id.loc);
        add = findViewById(R.id.add);
        drname = findViewById(R.id.drname);
        spinner_img_drCall = findViewById(R.id.spinner_img_drCall);
        get_workwith = findViewById(R.id.get_workwith);
        get_workwithBtn = findViewById(R.id.get_workwithBtn);

        dr_remark_edit = findViewById(R.id.dr_remark_edit);

        lead = findViewById(R.id.lead);
        products = findViewById(R.id.product);
        gift = findViewById(R.id.gift);

        stk= (TableLayout) findViewById(R.id.promotion);
        gift_layout = (TableLayout) findViewById(R.id.gift_layout);
        leadView = (TableLayout) findViewById(R.id.leadView);

        call_layout = (LinearLayout) findViewById(R.id.call_layout);
        summary_layout = (ExpandableListView) findViewById(R.id.summary_layout);
        tab_call= (Button) findViewById(R.id.call);
        tab_summary= (Button) findViewById(R.id.summary);

        back = findViewById(R.id.bkfinal_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        drname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewModel.customers();
                openCustomerList(null);
            }
        });
        spinner_img_drCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drname.performClick();
            }
        });

        get_workwithBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //viewModel.customers();
                openWorkWithList(viewModel.getCustomer().getWorkwiths());
            }
        });
        get_workwith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_workwithBtn.performClick();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitLead();
            }
        });

        String GiftCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"GIFT_BTN_CAPTION","");
        if (!GiftCaption.isEmpty())
            gift.setText(GiftCaption);

        gift.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (viewModel.getCustomer().getId().equalsIgnoreCase("0")) {

                    customVariablesAndMethod.msgBox(context, "Please Select " + "" + " First..");
                } else {
                    Bundle b = new Bundle();
                    b.putString("intent_fromRcpaCAll", "dr");
                    b.putString("gift_name", gift_name);
                    b.putString("gift_qty", gift_qty);

                    b.putString("gift_name_previous", gift_name_previous);
                    b.putString("gift_qty_previous", gift_qty_previous);

                    b.putString("ID", viewModel.getCustomer().getId());
                    new Chemist_Gift_Dialog(context, mHandler, b, GIFT_DILOG).Show();
                }
            }
        });

        lead.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                openLead(viewModel.getCustomer().getLeads());
            }
        });

        String ProductCaption = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"SAMPLE_BTN_CAPTION","");
        if (!ProductCaption.isEmpty())
            products.setText(ProductCaption);
        products.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                openPOB(viewModel.getCustomer().getPOBs());
                /*if (viewModel.getCustomer().getId().equalsIgnoreCase("0")) {

                    customVariablesAndMethod.msgBox(context, "Please Select " + "Customer" + " First..");
                } else {
                    Bundle b = new Bundle();
                    b.putString("intent_fromRcpaCAll", "Select Product...");
                    b.putString("sample_name", sample_name);
                    b.putString("sample_pob", sample_pob);
                    b.putString("sample_sample", sample_sample);

                    b.putString("sample_name_previous", sample_name_previous);
                    b.putString("sample_pob_previous", sample_pob_previous);
                    b.putString("sample_sample_previous", sample_sample_previous);

                    new Chm_Sample_Dialog(context, mHandler, b, PRODUCT_DILOG).Show();
                }*/


            }
        });


        chemist_list_summary=cbo_db_helper.getCallDetail("chemisttemp","","1");


        summary_list=new LinkedHashMap<>();
        summary_list.put("Customer",chemist_list_summary);

        final ArrayList<String> header_title=new ArrayList<>();
        //final List<Integer> visible_status=new ArrayList<>();
        for(String main_menu:summary_list.keySet()){
            header_title.add(main_menu);
            //visible_status.add(0);
        }



        listAdapter = new ExpandableListAdapter(summary_layout,this, header_title, summary_list);

        // setting list adapter
        summary_layout.setAdapter(listAdapter);
        summary_layout.setGroupIndicator(null);
        for(int i=0; i < listAdapter.getGroupCount(); i++)
            summary_layout.expandGroup(i);
        //doctor.expandGroup(1);

        summary_layout.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {

                summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition);
                if (summary_list.get(header_title.get(groupPosition)).get("visible_status").get(childPosition).equals("1")){
                    summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"0");
                }else {
                    summary_list.get(header_title.get(groupPosition)).get("visible_status").set(childPosition,"1");
                }
                listAdapter.notifyDataSetChanged();
                return false;
            }
        });


        tab_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCallUI();
            }
        });

        tab_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSummaryUI();
            }
        });

    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance().getUser().getID();
    }

    @Override
    public CallBuilder getCallBuilder() {
        return new CallBuilder()
                .setShowDistance(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(context,"IsBackDate","1").equals("1"))
                .setType(CallBuilder.CallType.Chemist)
                .settitle(getIntent().getStringExtra("title"));
    }




    @Override
    public void setTile() {
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        if (hader_text != null) {
            hader_text.setText(getCallBuilder().getTitle());
        }
    }

    @Override
    public void setLoctionUI() {
        if (Custom_Variables_And_Method.location_required.equals("Y")) {
            loc_layout.setVisibility(View.VISIBLE);
            loc.setText(Custom_Variables_And_Method.GLOBAL_LATLON);
        }else {
            loc_layout.setVisibility(View.GONE);
        }
    }

    @Override
    public void showCallUI() {
        call_layout.setVisibility(View.VISIBLE);
        summary_layout.setVisibility(View.GONE);
        tab_call.setBackgroundResource(R.drawable.tab_selected);
        tab_summary.setBackgroundResource(R.drawable.tab_deselected);
    }

    @Override
    public void showSummaryUI() {
        summary_layout.setVisibility(View.VISIBLE);
        call_layout.setVisibility(View.GONE);
        tab_call.setBackgroundResource(R.drawable.tab_deselected);
        tab_summary.setBackgroundResource(R.drawable.tab_selected);
    }

    @Override
    public void openCustomerList(ArrayList<SpinnerModel> customers) {

        Intent intent = new Intent(context, CallActivity.class);
        intent.putExtra("builder",getCallBuilder());
        startActivityForResult(intent,CALL_ACTIVITY);

    }

    @Override
    public void openWorkWithList(ArrayList<Dcr_Workwith_Model> workWiths) {
        if (viewModel.getCustomer().getId().equalsIgnoreCase("0")) {

            customVariablesAndMethod.msgBox(context, "Please Select " + getCallBuilder().getTitle()  + " First..");
        } else {
            WorkWithBuilder workWithBuilder = new WorkWithBuilder()
                    .setTitle("Select WorkWith...")
                    .setWorkwithModels(workWiths);
            Intent intent = new Intent(context, WorkWithActivity.class);
            intent.putExtra("builder", workWithBuilder);
            startActivityForResult(intent, WORKWITH_ACTIVITY);
        }
    }

    @Override
    public void openRemarkList(String remark) {

    }

    @Override
    public void openProduct(ArrayList<PobModel> products) {

    }

    @Override
    public void openGift(ArrayList<PobModel> gifts) {

    }

    @Override
    public void openLead(ArrayList<PobModel> leads) {
        if (viewModel.getCustomer().getId().equalsIgnoreCase("0")) {

            customVariablesAndMethod.msgBox(context, "Please Select " + getCallBuilder().getTitle() + " First..");
        } else {
            SamplePOBBuilder samplePOBBuilder = new SamplePOBBuilder()
                    .setType(SamplePOBBuilder.ItemType.LEAD)
                    .setTitle("Select Leads...")
                    .setItems(leads);
            Intent intent = new Intent(context, LeadActivity.class);
            intent.putExtra("builder", samplePOBBuilder);
            startActivityForResult(intent, LEAD_DILOG);
        }
    }


    @Override
    public void openPOB(ArrayList<PobModel> pob) {
        if (viewModel.getCustomer().getId().equalsIgnoreCase("0")) {

            customVariablesAndMethod.msgBox(context, "Please Select " + getCallBuilder().getTitle() + " First..");
        } else {
            SamplePOBBuilder samplePOBBuilder = new SamplePOBBuilder()
                    .setType(SamplePOBBuilder.ItemType.POB)
                    .setTitle("Select Products...")
                    .setItems(pob);
            Intent intent = new Intent(context, LeadActivity.class);
            intent.putExtra("builder", samplePOBBuilder);
            startActivityForResult(intent, POB_DILOG);
        }
    }

    @Override
    public void setCustomer(mCustomerCall customerCall) {
        drname.setText(customerCall.getName());
    }

    @Override
    public void setWorkWith(ArrayList<Dcr_Workwith_Model> workWiths) {
        StringBuilder workWith = new StringBuilder();
        for(Dcr_Workwith_Model workwith_model : workWiths){
            workWith.append("\u2022 ").append(workwith_model.getName()).append("\n");
        }
        get_workwith.setText(workWith.toString());
    }

    @Override
    public void setRemark(String remark) {

    }

    @Override
    public void setProduct(ArrayList<PobModel> products) {

    }

    @Override
    public void setGift(ArrayList<PobModel> gifts) {

    }

    @Override
    public void setLeads(ArrayList<PobModel> leads) {
        init_Lead(leadView,leads);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CALL_ACTIVITY:
                    //SpinnerModel model = (SpinnerModel) data.getSerializableExtra("item");
                    viewModel.setCallmodel((SpinnerModel) data.getSerializableExtra("item"));

                    break;
                case WORKWITH_ACTIVITY:
                    ArrayList<Dcr_Workwith_Model> workWiths = (ArrayList<Dcr_Workwith_Model>) data.getSerializableExtra("workWiths");
                    viewModel.setWorkWith(workWiths);
                    break;
                case LEAD_DILOG:
                    ArrayList<PobModel> leads = (ArrayList<PobModel>) data.getSerializableExtra("Items");
                    viewModel.setLeads(leads);
                    break;
                case POB_DILOG:
                    ArrayList<PobModel> pobs = (ArrayList<PobModel>) data.getSerializableExtra("Items");
                    viewModel.setPOBs(pobs);
                    break;

                default:

            }
        }
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle b1;
            switch (msg.what) {
                case PRODUCT_DILOG:
                    b1 = msg.getData();
                    name = b1.getString("val");//id
                    name2 = b1.getString("val2");//score or pob
                    result = b1.getDouble("resultpob");
                    sample = b1.getString("sampleQty");// sample
                    rate = b1.getString("resultRate");
                    DecimalFormat f = new DecimalFormat("#.00");
                    resultList = b1.getString("resultList");
                    String result3 = f.format(result);
                    //pob.setText(result3);

                    sample_name=resultList;
                    sample_pob=name2;
                    sample_sample=sample;


                    String[] sample_name1 = resultList.split(",");
                    String[] sample_qty1 = sample.split(",");
                    String[] sample_pob1 = name2.split(",");
                    init(stk,sample_name1, sample_qty1, sample_pob1);

                    break;

                case LEAD_DILOG:
                    b1 = msg.getData();
                    String name = b1.getString("val");//id
                    String name2 = b1.getString("val2");//score or pob
                    Double result = b1.getDouble("resultpob");
                    String sample = b1.getString("sampleQty");// sample
                    String rate = b1.getString("resultRate");
                    DecimalFormat f1 = new DecimalFormat("#.00");
                    String resultList = b1.getString("resultList");
                    String result4 = f1.format(result);
                    //pob.setText(result3);

                    lead_names=resultList;
                    lead_ids = name;


                    String[] sample_name2 = resultList.split(",");
                    String[] sample_qty2 = sample.split(",");
                    String[] sample_pob2 = name2.split(",");
                    //init(leadView,sample_name2, sample_qty2, sample_pob2);
                    init_Lead(leadView,sample_name2);

                    break;
                case GIFT_DILOG:
                    b1 = msg.getData();
                    name3 = b1.getString("giftid");
                    name4 = b1.getString("giftqan");
                    String[] gift_name1= b1.getString("giftname").split(",");
                    String[] gift_qty1= b1.getString("giftqan").split(",");

                    gift_name=b1.getString("giftname");
                    gift_qty=b1.getString("giftqan");

                    init_gift(gift_layout,gift_name1,gift_qty1);


                    break;
                case MESSAGE_INTERNET_SEND_FCM:
                    if(new NetworkUtil(context).internetConneted(context)){

                        String live_km =customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"live_km");
                        if (live_km.equalsIgnoreCase("Y")||(live_km.equalsIgnoreCase("Y5"))){
                            MyCustomMethod myCustomMethod= new MyCustomMethod(context);
                            myCustomMethod.stopAlarm10Minute();
                            myCustomMethod.startAlarmIn10Minute();
                        }else {
                            startService(new Intent(context, Sync_service.class));
                        }
                    }

                    viewModel.setCustomer(new mCustomerCall());
                    /*Intent intent = new Intent(getApplicationContext(), LoginFake.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);

                    finish();*/
                    break;
                case 99:
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                        //Toast.makeText(getApplicationContext(),msg.getData().getString("Error"),Toast.LENGTH_SHORT).show();
                    }
                    break;


            }
        }
    };


    private void init(TableLayout stk,String[] sample_name, String[] sample_qty, String[] sample_pob) {
        //TableLayout stk= (TableLayout) findViewById(R.id.promotion);
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Product");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Sample ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(context);
        tv2.setPadding(5, 5, 5, 0);
        tv2.setText(" POB ");
        tv2.setTextColor(Color.WHITE);
        tv2.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv2);
        stk.removeAllViews();
        stk.addView(tbrow0);
        for (int i = 0; i < sample_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(sample_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(sample_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(context);
            t3v.setText(sample_pob[i]);
            t3v.setPadding(5, 5, 5, 0);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
            stk.addView(tbrow);
        }




    }


    private void init_Lead(TableLayout stk1, String[] lead_name) {
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Lead");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        stk1.removeAllViews();
        stk1.addView(tbrow0);
        for (int i = 0; i < lead_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(lead_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            stk1.addView(tbrow);
        }
        if(lead_name.length==1 && lead_name[0].equals("0")){
            stk1.removeAllViews();
        }

    }

    private void init_Lead(TableLayout stk1, ArrayList<PobModel> lead_name) {
        lead_names="";
        lead_ids = "";
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Lead");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        stk1.removeAllViews();
        stk1.addView(tbrow0);
        for (int i = 0; i < lead_name.size(); i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(lead_name.get(i).getName());
            lead_names = lead_names + lead_name.get(i).getName() + ",";
            lead_ids = lead_ids + lead_name.get(i).getId() + ",";
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            stk1.addView(tbrow);
        }
        if(lead_name.size()==0 ){
            stk1.removeAllViews();
        }

    }

    private void init_gift(TableLayout stk1, String[] gift_name, String[] gift_qty) {
        //ArrayList<String> sample_name= childText.get("sample_name").get(childPosition).toString();
        TableRow tbrow0 = new TableRow(context);
        tbrow0.setBackgroundColor(0xff125688);
        TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1f);
        TextView tv0 = new TextView(context);
        tv0.setText("Gift");
        tv0.setPadding(5, 5, 5, 0);
        tv0.setTextColor(Color.WHITE);
        tv0.setTypeface(null, Typeface.BOLD);
        tv0.setLayoutParams(params);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(context);
        tv1.setText(" Qty. ");
        tv1.setPadding(5, 5, 5, 0);
        tv1.setTextColor(Color.WHITE);
        tv1.setTypeface(null, Typeface.BOLD);
        tbrow0.addView(tv1);
        stk1.removeAllViews();
        stk1.addView(tbrow0);
        for (int i = 0; i < gift_name.length; i++) {
            TableRow tbrow = new TableRow(context);
            TextView t1v = new TextView(context);
            t1v.setText(gift_name[i]);
            t1v.setPadding(5, 5, 5, 0);
            t1v.setTextColor(Color.BLACK);
            t1v.setLayoutParams(params);
            tbrow.addView(t1v);
            TextView t2v = new TextView(context);
            t2v.setText(gift_qty[i]);
            t2v.setPadding(5, 5, 5, 0);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            stk1.addView(tbrow);
        }
        if(gift_name.length==1 && gift_name[0].equals("0")){
            stk1.removeAllViews();
        }

    }

    @Override
    public void Edit_Call(String Dr_id, String Dr_name) {
//        AppAlert.getInstance()
//                .setPositiveTxt("Edit")
//                .setNagativeTxt("Cancel")
//                .DecisionAlert(context,
//                "Edit!!!", "Do you want to edit " + Dr_name + " ?",
//                new AppAlert.OnClickListener() {
//                    @Override
//                    public void onPositiveClicked(View item, String result) {
//
//                    }
//
//                    @Override
//                    public void onNegativeClicked(View item, String result) {
//
//                    }
//                });
    }

    @Override
    public void delete_Call(String Dr_id, String Dr_name) {
        AppAlert.getInstance()
                .setPositiveTxt("Delete")
                .setNagativeTxt("Cancel")
                .DecisionAlert(context,
                        "Delete!!!", "Do you Really want to delete "+Dr_name+" ?",
                        new AppAlert.OnClickListener() {
                            @Override
                            public void onPositiveClicked(View item, String result) {

                                //Start of call to service

                                HashMap<String,String> request=new HashMap<>();
                                request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
                                request.put("iPaId", MyCustumApplication.getInstance().getUser().getID() );
                                request.put("iDCR_ID",  MyCustumApplication.getInstance().getDCR().getId());
                                request.put("iCHEM_ID", Dr_id);

                                ArrayList<Integer> tables=new ArrayList<>();
                                tables.add(0);

                                new MyAPIService(context).execute(new ResponseBuilder("DCRLEAD_DELETE_COMMIT",request)
                                        .setTables(tables)
                                        .setResponse(new CBOServices.APIResponse() {
                                            @Override
                                            public void onComplete(Bundle bundle) throws Exception {
                                                cbo_db_helper.delete_Chemist_from_local_all(Dr_id);
                                                customVariablesAndMethod.msgBox(context,Dr_name+" sucessfully Deleted.");
                                                finish();
                                            }

                                            @Override
                                            public void onResponse(Bundle bundle) throws Exception {

                                                //parser2(bundle);
                                            }

                                            @Override
                                            public void onError(String s, String s1) {
                                                AppAlert.getInstance().getAlert(context,s,s1);
                                            }
                                        }));


                                //End of call to service



                            }

                            @Override
                            public void onNegativeClicked(View item, String result) {

                            }
                        });
    }

    private void commitLead(){
        if (viewModel.getCustomer().getId().equalsIgnoreCase("0")) {

            customVariablesAndMethod.msgBox(context, "Please Select " + "Customer" + " First..");
        }else if(viewModel.getCustomer().getWorkwiths().size() == 0 &&
                viewModel.getCustomer().getWorkwithreqd()){
            customVariablesAndMethod.msgBox(context, "Please Select Workwith...");
        }else if (dr_remark_edit.getText().toString().trim().equalsIgnoreCase("")){
            customVariablesAndMethod.msgBox(context, "Please enter remark...");
        }else if (viewModel.getCustomer().getLeads().size() == 0 &&
                viewModel.getCustomer().getLeadReqd()){
            customVariablesAndMethod.msgBox(context, "Please Select atleast one lead...");
        }else{

            //Start of call to service

            HashMap<String,String> request=new HashMap<>();
            request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
            request.put("iPaId", MyCustumApplication.getInstance().getUser().getID() );
            request.put("iDCR_ID",  MyCustumApplication.getInstance().getDCR().getId());
            request.put("iCHEM_ID", viewModel.getCustomer().getId());
            request.put("sITEM_ID", lead_ids);
            request.put("sSTATUS",  "");
            request.put("sREAMRK", dr_remark_edit.getText().toString());

            ArrayList<Integer> tables=new ArrayList<>();
            tables.add(0);

            new MyAPIService(context).execute(new ResponseBuilder("DCRLEAD_COMMIT",request)
                    .setTables(tables)
                    .setResponse(new CBOServices.APIResponse() {
                        @Override
                        public void onComplete(Bundle bundle) throws Exception {
                            submitChemist(false);
                        }

                        @Override
                        public void onResponse(Bundle bundle) throws Exception {

                            //parser2(bundle);
                        }

                        @Override
                        public void onError(String s, String s1) {
                            AppAlert.getInstance().getAlert(context,s,s1);
                        }
                    }));


            //End of call to service
        }
    }


    public void  submitChemist(boolean Skip_Verification) {
        //String currentBatterLevel = chm_BatteryLevel.getText().toString();

        String chm_name = viewModel.getCustomer().getName();

        String address = loc.getText().toString();
        String dcrid = MyCustumApplication.getInstance().getDCR().getId();
        String PobAmt = "0.0";
        String AllItemId = "";
        String AllItemQty = "";
        String AllGiftId = "";
        String AllSampleQty = "";
        String AllGiftQty = "";


            for (int i = 0; i < 1; i++) {
                AllItemId = AllItemId + name;
                AllItemQty = AllItemQty + name2;
            }

            if (name3.equals("")) {
                AllGiftId = AllGiftId + "0";
                AllGiftQty = AllGiftQty + "0";
            } else {

                for (int i = 0; i < 1; i++) {
                    AllGiftId = AllGiftId + name3;
                    AllGiftQty = AllGiftQty + name4;
                }
            }




            if (cbo_db_helper.searchChemist(viewModel.getCustomer().getId()).contains(viewModel.getCustomer().getId())) {
                int val = cbo_db_helper.updateChemistInLocal(dcrid, viewModel.getCustomer().getId(), PobAmt, AllItemId, AllItemQty,
                        Custom_Variables_And_Method.GLOBAL_LATLON + "!^" + address, AllGiftId, AllGiftQty,
                        "",sample,dr_remark_edit.getText().toString(),"",rate);
                Log.e("chemist updated", "" + val);
                customVariablesAndMethod.msgBox(context,chm_name + "  successfully updated");


                new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"C",viewModel.getCustomer().getId(),"");

            } else if(!customVariablesAndMethod.checkIfCallLocationValid(context,false,Skip_Verification)) {
                customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                IsRefreshedClicked = false;
                LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                        new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
            }else {
                try {
                    Custom_Variables_And_Method.GLOBAL_LATLON = customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);
                    Location currentBestLocation=customVariablesAndMethod.getObject(context,"currentBestLocation",Location.class);

                    customVariablesAndMethod.SetLastCallLocation(context);

                    String locExtra="";

                    if (currentBestLocation!=null) {
                        locExtra = "Lat_Long " + currentBestLocation.getLatitude() + "," + currentBestLocation.getLongitude() + ", Accuracy " + currentBestLocation.getAccuracy() + ", Time " + currentBestLocation.getTime() + ", Speed " + currentBestLocation.getSpeed() + ", Provider " + currentBestLocation.getProvider();
                    }




                    long val = cbo_db_helper.submitChemistInLocal(dcrid, viewModel.getCustomer().getId(), PobAmt, AllItemId, AllItemQty,
                            viewModel.getCallmodel().getLAT_LONG() + "!^" + address, AllGiftId, AllGiftQty, customVariablesAndMethod.currentTime(context),
                            MyCustumApplication.getInstance().getUser().getBattery(),sample,dr_remark_edit.getText().toString(),"",locExtra,viewModel.getCallmodel().getREF_LAT_LONG(),rate);


                    cbo_db_helper.addChemistInLocal(viewModel.getCustomer().getId(), chm_name,""+customVariablesAndMethod.currentTime(context), viewModel.getCallmodel().getLAT_LONG(), Custom_Variables_And_Method.global_address,"0","0",customVariablesAndMethod.srno(context) ,locExtra);
                    Log.e("chemist submit in local", "" + val);
                    Log.e("chemist details", dcrid + "," + viewModel.getCustomer().getId() + "," + PobAmt + "," + AllItemId + "," + AllItemQty + "," + address + "," + AllGiftId + "," + AllGiftQty);
                    if (val != -1) {

                        /*chm_ok = getmydata().get(0);
                        stk_ok = getmydata().get(1);
                        exp_ok = getmydata().get(2);


                        if (chm_ok.equals("")) {
                            cbohelp.insertfinalTest(chm_id, stk_ok, exp_ok);
                        } else {
                            cbohelp.updatefinalTest(chm_id, stk_ok, exp_ok);
                        }*/

                        customVariablesAndMethod.msgBox(context,"Customer Added Successfully");

                        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(context,"CHEMIST_NOT_VISITED","N");
                        //pob.setText("");
                        Custom_Variables_And_Method.CHEMIST_NOT_VISITED = "Y";

                        new Service_Call_From_Multiple_Classes().SendFCMOnCall(context, mHandler, MESSAGE_INTERNET_SEND_FCM,"C",viewModel.getCustomer().getId(),Custom_Variables_And_Method.GLOBAL_LATLON);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }




    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);

                submitChemist(true);

            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);

        }
    };

}
