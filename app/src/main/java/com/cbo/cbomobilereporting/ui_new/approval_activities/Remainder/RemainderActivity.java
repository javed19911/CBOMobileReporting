package com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.for_all_activities.CustomWebView;
import com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor.MissedDocAdapter;
import com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor.VM_MissedGrid_Mobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils_new.AppAlert;
import utils_new.interfaces.RecycleViewOnItemClickListener;

public class RemainderActivity extends CustomActivity implements IApprovalRemainder{


    android.support.v7.widget.Toolbar toolbar;
    Button backBtn;
    VMApprovalRemainder vmApprovalRemainder;
    RecyclerView listView;
    ApprovalRemainderAdaptor approvalRemainderAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remainder);
        toolbar =  findViewById(R.id.toolbar_hadder);
        TextView hader_text = findViewById(R.id.hadder_text_1);
        backBtn = findViewById(R.id.back);
        listView = findViewById(R.id.listView_rem_approval) ;

        hader_text.setText(getIntent().getStringExtra("Title"));
        setSupportActionBar(toolbar);

        vmApprovalRemainder = ViewModelProviders.of((FragmentActivity) context).get(VMApprovalRemainder.class);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vmApprovalRemainder.setListener(this);


        if (isDiologActivity()){
            try {
                JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("response"));
                JSONArray jsonArray = jsonObject.getJSONArray("Tables");
                Bundle b = new Bundle();

                ArrayList<Integer> tables = new ArrayList<>();
                tables.add(0);
                tables.add(1);
                for(int i:tables){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    JSONArray jsonArray1 = jsonObject1.getJSONArray("Tables"+i);
                    // Log.d("MYAPP", "objects are: " + jsonArray1.toString());

                    b.putString("Tables"+i,jsonArray1.toString());

                }

                vmApprovalRemainder.parser_ApprovalRemainder(b);
            } catch (JSONException e) {
                vmApprovalRemainder.getApprovalRemainders(this);
            }

        }else{
            vmApprovalRemainder.getApprovalRemainders(this);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item != null) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListUpdated(ArrayList<mApprovalRemainder> mApprovalRemainders) {
        approvalRemainderAdaptor =
                new ApprovalRemainderAdaptor(context, mApprovalRemainders, (view, position, isLongClick) -> {
                    Intent i = new Intent(context, CustomWebView.class);
                    i.putExtra("A_TP", mApprovalRemainders.get(position).getADD_URL());
                    i.putExtra("Title",  mApprovalRemainders.get(position).getPARICULARS());
                    startActivity(i);
                });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        listView.setLayoutManager(mLayoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(approvalRemainderAdaptor);
        ViewCompat.setNestedScrollingEnabled(
                listView, false);
    }

    @Override
    public void onError(String Title, String error) {
        AppAlert.getInstance().getAlert(context,Title,error);
    }
}
