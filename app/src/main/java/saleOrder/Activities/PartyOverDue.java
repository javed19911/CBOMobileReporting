package saleOrder.Activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Adaptor.OverDueAdapter;
import saleOrder.Model.mOverDue;
import saleOrder.Model.mParty;
import saleOrder.ViewModel.vmOrder;
import saleOrder.ViewModel.vmPartyOverDue;
import saleOrder.Views.iPartyOverDue;


public class PartyOverDue extends CustomActivity implements iPartyOverDue {

    vmPartyOverDue viewModel;
    Activity context;

    RecyclerView itemlist;
    mParty party;
    OverDueAdapter overDueAdapter;
    Button cancel,ok;
    TextView partyTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_party_over_due);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmPartyOverDue.class);
        viewModel.setView(context,this);

    }


    @Override
    public void onSendResponse() {
        Intent intent = new Intent();
        intent.putExtra("order",(mOrder) getIntent().getSerializableExtra("order"));
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void getReferencesById() {


        party = ((mParty) getIntent().getSerializableExtra("party"));

        ok = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        partyTxt = findViewById(R.id.party);

        itemlist = (RecyclerView) findViewById(R.id.itemList);
        overDueAdapter = new OverDueAdapter(context, viewModel.getOverDues(context));
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        itemlist.setLayoutManager(mLayoutManager1);
        itemlist.setItemAnimator(new DefaultItemAnimator());
        itemlist.setAdapter(overDueAdapter);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendResponse();
            }
        });
    }

    @Override
    public void setTitle(String title) {
        partyTxt.setText("Over Dues : " + party.getName());
    }

    @Override
    public mParty getParty() {
        return party;
    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getUserID() {
        return MyCustumApplication.getInstance().getUser().getID();
    }


    @Override
    public void onOverDueListChanged(ArrayList<mOverDue> overDues) {
        overDueAdapter.update(overDues);
    }
}