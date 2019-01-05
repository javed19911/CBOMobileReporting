package com.cbo.cbomobilereporting.ui;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.report_activities.Spo_Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import pl.polidea.view.ZoomView;
import services.ServiceHandler;
import utils.MyConnection;
import utils.adapterutils.SpoRptAdapter;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Akshit Udainiya on 9/14/15.
 */
public class SpoProductWiseStock extends AppCompatActivity {

    Intent myIntent;
    ServiceHandler myService;
    MyConnection myCon;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
   ListView myList;
    ZoomView myZoom;
    String extraFromDate,extraTODate,extraCompanyName;
    CBO_DB_Helper myDataBase;
    SimpleAdapter simpleAdapter;
    ListView listView;
    LinearLayout mainContainer;
    ArrayList<Map<String,String>> dataList ;
    LinkedHashMap<String,ArrayList<String>> data1;
    ArrayList<String> Consignee,Sales_Amount,Sales_Return,Breakage_Expiry,Credit_Note_Other,Net_Sales,Receipt,Outstanding,Stock_Amount,Stock_Qty,Exp_Qty,Exp_Amount;
    String Title="Stock Report",cnftxt="Distributors",rpt_typ="c";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    final boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
       setContentView(R.layout.zoomer_layout);


        Toolbar toolbar =(Toolbar)  findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        Title="SPO Product Wise Stock";

        if(SpoRptAdapter.clickCount==1){
            Title="SPO Headquarter Item Wise Report";
            // cnftxt="Stockist";
            rpt_typ="h";
        }
        if(SpoRptAdapter.clickCount==2){
            Title="SPO Stockist Item Wise Report";
           // cnftxt="Stockist";
            rpt_typ="P";
        }
        textView.setText(Title);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

      /*  if (customTitleSupported){
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.mytitlebar);
            getWindow().setBackgroundDrawableResource(R.color.titlebackgroundcolor);
        }*/
        final TextView titleText = (TextView) findViewById(R.id.left_text);
        context = SpoProductWiseStock.this;
        myIntent = getIntent();
        myService = new ServiceHandler(context);
        myCon = new MyConnection(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        extraFromDate =myIntent.getStringExtra("");
        extraTODate = myIntent.getStringExtra("");
        extraCompanyName = myIntent.getStringExtra("company_name");
        myDataBase = new CBO_DB_Helper(context);

        dataList = new ArrayList<Map<String, String>>();
/*
        if (titleText != null){
            titleText.setText("Spo Product Wise Stock "+extraCompanyName);

        }*/
   View v = ((LayoutInflater) getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spo_product_wise_stock,
                null,false);
        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));

        myZoom = new ZoomView(context);
        myZoom.addView(v);
        mainContainer = (LinearLayout) findViewById(R.id.main_container_zoom);
        mainContainer.addView(myZoom);

        if(SpoRptAdapter.clickCount==0){
            TextView s_amt = (TextView) v.findViewById(R.id.s_amt);
            TextView s_qty = (TextView) v.findViewById(R.id.s_qty);
            s_amt.setVisibility(View.VISIBLE);
            s_qty.setVisibility(View.VISIBLE);
        }

        listView = (ListView) v.findViewById(R.id.spo_product_wise_list);

          new BackgroundData().execute();



    }
    class BackgroundData extends AsyncTask<String,String,String>{

        ProgressDialog pd;


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if ((!s.equals(null)||(!s.contains("ERROR")))){

                try {
                    dataList.clear();

                    Consignee=new ArrayList<String>();
                    Sales_Amount=new ArrayList<String>();
                    Sales_Return=new ArrayList<String>();
                    Breakage_Expiry=new ArrayList<String>();
                    Credit_Note_Other=new ArrayList<String>();
                    Net_Sales=new ArrayList<String>();
                    Receipt=new ArrayList<String>();
                    Outstanding=new ArrayList<String>();
                    Stock_Amount=new ArrayList<String>();
                    Exp_Qty=new ArrayList<String>();
                    Exp_Amount=new ArrayList<String>();
                    Stock_Qty=new ArrayList<String>();

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables0");

                    for (int i =0; i<jsonArray1.length();i++){
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(i);
                        Map<String,String> map = new HashMap<String, String>();
                        map.put("Item Name",jsonObject2.getString("ITEM_NAME"));
                        Consignee.add(jsonObject2.getString("ITEM_NAME"));

                        map.put("PACK",jsonObject2.getString("PACK"));
                        Sales_Amount.add(jsonObject2.getString("PACK"));

                        map.put("Sales Qty",jsonObject2.getString("Sales Qty"));
                        Sales_Return.add(jsonObject2.getString("Sales Qty"));

                        map.put("Sales Amount",jsonObject2.getString("Sales Amount"));
                        Breakage_Expiry.add(jsonObject2.getString("Sales Amount"));

                        map.put("Sales Return Qty",jsonObject2.getString("Sales Return Qty"));
                        Credit_Note_Other.add(jsonObject2.getString("Sales Return Qty"));

                        map.put("Sales Return Amount",jsonObject2.getString("Sales Return Amount"));
                        Net_Sales.add(jsonObject2.getString("Sales Return Amount"));

                        map.put("Claim Qty",jsonObject2.getString("Claim Qty"));
                        Receipt.add(jsonObject2.getString("Claim Qty"));

                        map.put("Claim Amount",jsonObject2.getString("Claim Amount"));
                        Outstanding.add(jsonObject2.getString("Claim Amount"));

                        map.put("Br/Exp Qty",jsonObject2.getString("Br/Exp Qty"));
                        Exp_Qty.add(jsonObject2.getString("Br/Exp Qty"));

                        map.put("Br/Exp Amount",jsonObject2.getString("Br/Exp Amount"));
                        Exp_Amount.add(jsonObject2.getString("Br/Exp Amount"));

                        map.put("Stock Qty",jsonObject2.getString("Stock Qty"));
                        Stock_Qty.add(jsonObject2.getString("Stock Qty"));

                        map.put("Stock Amount",jsonObject2.getString("Stock Amount"));
                        Stock_Amount.add(jsonObject2.getString("Stock Amount"));

                        dataList.add(map);
                    }

                    data1 = new LinkedHashMap<String, ArrayList<String>>();
                    data1.put("Item Name",Consignee);
                    data1.put("PACK",Sales_Amount);
                    data1.put("Sales Qty",Sales_Return);
                    data1.put("Sales Amount",Breakage_Expiry);
                    data1.put("Sales Return Qty",Credit_Note_Other);
                    data1.put("Sales Return Amount",Net_Sales);
                    data1.put("Claim Qty",Receipt);
                    data1.put("Claim Amount",Outstanding);
                    data1.put("Br/Exp Qty",Exp_Qty);
                    data1.put("Br/Exp Amount",Exp_Amount);
                    data1.put("Stock Qty",Stock_Qty);
                    data1.put("Stock Amount",Stock_Amount);


                    if(SpoRptAdapter.clickCount==0){
                        String from[] = {"Item Name","PACK","Sales Qty","Sales Amount","Sales Return Qty","Sales Return Amount","Claim Qty",
                                "Claim Amount","Br/Exp Qty","Br/Exp Amount","Stock Qty","Stock Amount"};
                        int to[] = {R.id.spo_product_wise_stock_item_name,R.id.spo_product_wise_stock_pack,
                                R.id.spo_product_wise_stock_sales_qty,R.id.spo_product_wise_stock_sales_amt,R.id.spo_product_wise_stock_sales_returns_qty,
                                R.id.spo_product_wise_stock_sales_return_amt,R.id.spo_product_wise_stock_claim_qty,R.id.spo_product_wise_stock_claim_amt,
                                R.id.spo_product_wise_stock_br_exp_qty,R.id.spo_product_wise_stock_br_exp_amt,R.id.spo_product_wise_stock_stock_qty,
                                R.id.spo_product_wise_stock_stock_amt};
                        simpleAdapter=   new SimpleAdapter(context,dataList,R.layout.spo_product_wise_stock_view,from,to);
                        listView.setAdapter(simpleAdapter);
                    }else {
                        String from[] = {"Item Name","PACK","Sales Qty","Sales Amount","Sales Return Qty","Sales Return Amount","Claim Qty",
                                "Claim Amount","Br/Exp Qty","Br/Exp Amount"};
                        int to[] = {R.id.spo_product_wise_stock_item_name,R.id.spo_product_wise_stock_pack,
                                R.id.spo_product_wise_stock_sales_qty,R.id.spo_product_wise_stock_sales_amt,R.id.spo_product_wise_stock_sales_returns_qty,
                                R.id.spo_product_wise_stock_sales_return_amt,R.id.spo_product_wise_stock_claim_qty,R.id.spo_product_wise_stock_claim_amt,
                                R.id.spo_product_wise_stock_br_exp_qty,R.id.spo_product_wise_stock_br_exp_amt};
                        simpleAdapter=   new SimpleAdapter(context,dataList,R.layout.spo_product_wise_stock_view1,from,to);
                        listView.setAdapter(simpleAdapter);
                    }


                }
                catch (JSONException json){


                    customVariablesAndMethod.msgBox(context,"Json Data Not Found....");
                }



            }
            else {

                customVariablesAndMethod.msgBox(context,"Error Found...While connecting Server..");
            }
            pd.dismiss();

        }

        @Override
        protected String doInBackground(String... params) {

            String spoProductWise_Result = myService.getResponse_SPOCNFViewGrid(myDataBase.getCompanyCode(),
                    extraCompanyName, Spo_Report.mIdFrom,Spo_Report.mIdTo,""+ Custom_Variables_And_Method.PA_ID,rpt_typ);

            return spoProductWise_Result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing Please Wait...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            myCon.create_xml(data1,Title);
        }else{
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
