package com.cbo.cbomobilereporting.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.Model.mSPO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import pl.polidea.view.ZoomView;
import services.ServiceHandler;
import utils.MyConnection;
import utils.adapterutils.SpoModel;
import utils.adapterutils.SpoRptAdapter;
import utils_new.Custom_Variables_And_Method;


/**
 * Created by Akshit udainiya on 9/10/15.
 */
public class SpoDistributorsWise extends AppCompatActivity {


    Context context;
    Intent intent;
    MyConnection myConnection;
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cboDbHelper;
    ServiceHandler myService;
    String spoIdExtra;
    SpoRptAdapter spoRptAdapter;
    mSPO _mSPO = null;
    ArrayList<SpoModel>dataList;
    ListView listView;
    ZoomView zoomView;
    TextView consigneeText,stk_txt;
    LinearLayout mainContainer;
    String Title="Stock Report",cnftxt="Distributors",rpt_typ="p";

    LinkedHashMap<String,ArrayList<String>> data1;
    ArrayList<String> Consignee,Sales_Amount,Sales_Return,Breakage_Expiry,Credit_Note_Other,Net_Sales,Secondary_Sales,Receipt,Outstanding,Stock_Amount,spo_bill_url;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    boolean customTitleSupported = requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

        setContentView(R.layout.zoomer_layout);


        Toolbar toolbar =(Toolbar)  findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        if(SpoRptAdapter.clickCount==2){
            Title="SPO Stockist Wise Report";
            cnftxt="Stockist";
            rpt_typ="P";
        }else if(SpoRptAdapter.clickCount==3){
            Title="SPO Bill Wise Report";
            cnftxt= "bill";
            rpt_typ="B";
        }
        textView.setText(Title);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

/*
        if (customTitleSupported){
            getWindow().setBackgroundDrawableResource(R.color.titlebackgroundcolor);
            getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.mytitlebar);
        }
        final TextView titleText = (TextView) findViewById(R.id.left_text);

        if (titleText !=null ){
            titleText.setText("SPO Details Distributor Wise");
        }*/

        context = SpoDistributorsWise.this;
        myService = new ServiceHandler(context);
        cboDbHelper = new CBO_DB_Helper(context);
        myConnection = new MyConnection(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        intent = getIntent();
        _mSPO = (mSPO) intent.getSerializableExtra("mSPO");
        spoIdExtra  =intent.getStringExtra("spoId");
        dataList = new ArrayList<SpoModel>();

        View v = ((LayoutInflater) getSystemService(context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.spo_report_details,
                null,false);

        v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));

        zoomView = new ZoomView(this);
        zoomView.addView(v);

        mainContainer = (LinearLayout) findViewById(R.id.main_container_zoom);

        mainContainer.addView(zoomView);


        listView = (ListView) v.findViewById(R.id.spo_cnf_list);
        consigneeText =(TextView) v.findViewById(R.id.spo_cnf_consignee_text);
        stk_txt = (TextView) v.findViewById(R.id.stk);
        stk_txt.setText("");
        if(SpoRptAdapter.clickCount==3) {
            stk_txt = (TextView) v.findViewById(R.id.stk);
            stk_txt.setVisibility(View.GONE);
        }

        Button distributor = (Button) v.findViewById(R.id.spo_cnf_consignee);

        //distributor.setClickable(false);

        consigneeText.setText(cnftxt);

        new HeadQuarterWiseReportBackgroundTask().execute();


        //myConnection.msgBox(spoIdExtra);

      /*  backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backToLast = new Intent(context,Spo_Report.class);
                backToLast.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(backToLast);
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SpoRptAdapter.clickCount= -- SpoRptAdapter.clickCount;
    }

    class HeadQuarterWiseReportBackgroundTask extends AsyncTask<String,String,String>{

        ProgressDialog pd = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd.setTitle("Cbo");
            pd.setMessage("Processing Please Wait.....");
            pd.setCanceledOnTouchOutside(false);
            pd.setCancelable(false);
            pd.show();


        }

        @Override
        protected String doInBackground(String... params) {

            String  resultDistributor = myService.getResponse_SpoCNFGrid(cboDbHelper.getCompanyCode(),
                    ""+ Custom_Variables_And_Method.PA_ID,
                    _mSPO.getFDate(),_mSPO.getTDate(), _mSPO.getType().getValue(),_mSPO.getConsigneeId(),
                    _mSPO.getHqId(),_mSPO.getCurrencyType(),_mSPO.getStkId());
            return resultDistributor;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if ((s.contains("ERROR")||(s.equals(null)))){
                customVariablesAndMethod.msgBox(context,"Nothing Found....");

            }else {
                try {

                    dataList.clear();

                    Consignee=new ArrayList<String>();
                    Sales_Amount=new ArrayList<String>();
                    Sales_Return=new ArrayList<String>();
                    Breakage_Expiry=new ArrayList<String>();
                    Credit_Note_Other=new ArrayList<String>();
                    Net_Sales=new ArrayList<String>();
                    Secondary_Sales=new ArrayList<String>();
                    Receipt=new ArrayList<String>();
                    Outstanding=new ArrayList<String>();
                    Stock_Amount=new ArrayList<String>();
                    spo_bill_url= new ArrayList<String>();

                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");

                    for (int i = 0; i< jsonArray.length(); i++){

                        JSONObject object = jsonArray.getJSONObject(i);

                        SpoModel spoModel = new SpoModel();

                        String id = object.getString("ID");
                        spoModel.setId(id);

                        String con = object.getString("COMPANY_NAME");
                        spoModel.setConsignee(con);
                        Consignee.add(con);

                        String salAmt = object.getString("SALE_AMT");
                        spoModel.setSalAmt(salAmt);
                        Sales_Amount.add(salAmt);

                        String salReturn = object.getString("SALER_AMT");
                        spoModel.setSaleReturn(salReturn);
                        Sales_Return.add(salReturn);

                        String breakExp = object.getString("SALER_BR_AMT");
                        spoModel.setBreageExpiry(breakExp);
                        Breakage_Expiry.add(breakExp);

                        String cridtOther = object.getString("CN_AMT");
                        Credit_Note_Other.add(cridtOther);
                        spoModel.setCreditNotOrther(cridtOther);

                        String netSales = object.getString("NET_SALE_AMT");
                        spoModel.setNetSales(netSales);
                        Net_Sales.add(netSales);

                        String secSales = object.getString("SEC_AMT");
                        spoModel.setSecSales(secSales);
                        Secondary_Sales.add(secSales);

                        String recipict = object.getString("RCPT_AMT");
                        spoModel.setRecipt(recipict);
                        Receipt.add(recipict);

                        String outStanding = object.getString("OUTST_AMT");
                        spoModel.setOutStanding(outStanding);
                        Outstanding.add(outStanding);

                        String stkAmt = object.getString("STOCK_AMT");
                        spoModel.setStockAmt(stkAmt);
                        Stock_Amount.add(stkAmt);

                        String url = object.getString("BILL_URL");
                        spoModel.setSpo_bill_url (url);
                        spo_bill_url.add(url);


                        dataList.add(spoModel);




/*
                        Map<String,String> map = new HashMap<String, String>();

                        map.put("Consignee",object.getString("COMPANY_NAME"));
                        map.put("SALE_AMT",object.getString("SALE_AMT"));
                        map.put("SalesReturn",object.getString("SALER_AMT"));
                        map.put("BreakageExpiry",object.getString("SALER_BR_AMT"));
                        map.put("CreditNoteOther",object.getString("CN_AMT"));
                        map.put("Receipt",object.getString("RCPT_AMT"));
                        map.put("Outstanding",object.getString("OUTST_AMT"));
                        map.put("NetSales",object.getString("NET_SALE_AMT"));
                        map.put("StockAmount",object.getString("STOCK_AMT"));
                        data.add(map);
*/
                    }
                    data1 = new LinkedHashMap<String, ArrayList<String>>();
                    data1.put(cnftxt,Consignee);
                    data1.put("Sales Amount",Sales_Amount);
                    data1.put("Sales Return",Sales_Return);
                    data1.put("Breakage Expiry",Breakage_Expiry);
                    data1.put("Credit Note Other",Credit_Note_Other);
                    data1.put("Net Sales",Net_Sales);
                    data1.put("Secondary Sales",Secondary_Sales);
                    data1.put("Receipt",Receipt);
                    data1.put("Outstanding",Outstanding);


                    //data1.put("Stock Amount",Stock_Amount);

/*
                   String [] from ={"Consignee","SALE_AMT","SalesReturn","BreakageExpiry","CreditNoteOther","NetSales","Receipt"
                           ,"Outstanding","StockAmount"};
                    int[] to ={R.id.spo_cnf_consignee,R.id.spo_cnf_sales_amt,R.id.spo_cnf_sales_return,R.id.spo_cnf_breakage_exp,
                            R.id.spo_cnf_creadit_note_other,R.id.spo_cnf_net_sales,R.id.spo_cnf_receipt,R.id.spo_cnf_out_standing,
                            R.id.spo_cnf_stock_amt
                    };
                    simpleAdapter = new SimpleAdapter(context,data,R.layout.spo_detail_view,from,to);

                    listView.setAdapter(simpleAdapter);
                      pd.dismiss();


*/

                    spoRptAdapter = new SpoRptAdapter(context,dataList,_mSPO);
                    listView.setAdapter(spoRptAdapter);
                    pd.dismiss();


                }catch (JSONException jsonE){
                    pd.dismiss();
                    customVariablesAndMethod.msgBox(context,"Json Error"+jsonE);
                }
            }


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
            myConnection.create_xml(data1,Title);
        }else{
            SpoRptAdapter.clickCount= -- SpoRptAdapter.clickCount;
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
