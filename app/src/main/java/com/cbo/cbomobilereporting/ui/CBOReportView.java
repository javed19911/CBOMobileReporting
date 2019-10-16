package com.cbo.cbomobilereporting.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cbo.cbomobilereporting.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import async.RptShowTask;
import services.TaskListener;
import utils.adapterutils.RptAdapter;
import utils.adapterutils.RptModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by Kuldeep.Dwivedi on 2/22/2015.
 */
public class CBOReportView extends AppCompatActivity {


    ListView listView;
    Custom_Variables_And_Method customVariablesAndMethod;
    ArrayList<Map<String,String>>data=new ArrayList<Map<String, String>>();
    SimpleAdapter simpleAdapter;
    String monthId;
    ArrayList<RptModel>rptData;
    RptModel rptModel;
    RptAdapter rptAdapter;
    Button back;
    public  static String lastPaId;



    public void onCreate(Bundle b){
        super.onCreate(b);
      setContentView(R.layout.cboreportview);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        textView.setText("DCR Reports");

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }

        listView=(ListView)findViewById(R.id.rpt_list_dcr);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        rptData=new ArrayList<RptModel>();

        lastPaId=getIntent().getExtras().getString("nameId");
        monthId=getIntent().getExtras().getString("monthId");



        CBOReportView.this.showReportsToUI(CBOReportView.this);
        //listView.setAdapter(simpleAdapter);



    }
    public void showReportsToUI(final AppCompatActivity context){
        final RptShowTask rptShowTask=new RptShowTask(context);
        rptShowTask.setListener(new TaskListener<String>() {
            ProgressDialog commitDialog;
            @Override
            public void onStarted() {
                try {
                    commitDialog = new ProgressDialog(context);
                    commitDialog.setMessage("Please Wait..");
                    commitDialog.setCanceledOnTouchOutside(false);
                    commitDialog.setCancelable(false);
                    commitDialog.show();
                } catch (Exception e) {
                    commitDialog.dismiss();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinished(String result) {
                if ((result == null) || (result.contains("[ERROR]"))) {
                    commitDialog.dismiss();
                    customVariablesAndMethod.msgBox(context,"No Report found");
                } else {
                       try {
                        data.clear();
                          JSONObject jsonObject = new JSONObject(result);
                          JSONArray rows = jsonObject.getJSONArray("Tables");
                           JSONObject jsonObject1 = rows.getJSONObject(0);
                           JSONArray row1 = jsonObject1.getJSONArray("Tables0");



                     for (int i = 0; i < row1.length(); i++) {
                            Map<String,String>datanum=new HashMap<String,String>();
                            JSONObject c = row1.getJSONObject(i);
                            rptModel=new RptModel();
                            String date=c.getString("DCR_DATE");
                            rptModel.setDtae(date);
                            String station=c.getString("STATION");
                            rptModel.setWith(station);
                            String dr=c.getString("MVVVTotal");
                            rptModel.setTtldr(dr);
                            String chm=c.getString("NoOFChemist");
                            rptModel.setTtlchm(chm);
                            String stk=c.getString("NO_STOCKIST");
                            rptModel.setTtlstk(stk);

                         rptModel.setDairyCount(c.getString("DAIRY_COUNT"));

                         rptModel.setPolutaryCount(c.getString("POULTRY_COUT"));

                            String remark=c.getString("REMARK");
                            rptModel.setRemark(remark);
                           String missed_call=c.getString("MISS_DR");
                               rptModel.setTtlMissedCall(missed_call);

                         rptModel.setTtlNonDoctor(c.getString("NLC_TOTAL"));

                         String nonListedDistributor=c.getString("DR_RC");
                         rptModel.setTtlDrRiminder(nonListedDistributor);

                         rptModel.setTtlTenivia(c.getString("DRRX_TOTAL"));
                         rptModel.setBlinkRemark(c.getString("FINAL_SUBMITYN").equalsIgnoreCase("N"));

                         String exp=c.getString("DA_TYPE");
                            rptModel.setTtlexp(exp);
                            rptData.add(rptModel);

                        }



                        rptAdapter=new RptAdapter(context,rptData);
                        listView.setAdapter(rptAdapter);

                        commitDialog.dismiss();
                    }catch (Exception e){
                           commitDialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }
        });
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

            rptShowTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,lastPaId,monthId,"0"," "+ Custom_Variables_And_Method.PA_ID,"0");

        }
        else
        {
            rptShowTask.execute(lastPaId,monthId,"0",""+Custom_Variables_And_Method.PA_ID,"0");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){

            finish();

        }

        return super.onOptionsItemSelected(item);
    }
}
