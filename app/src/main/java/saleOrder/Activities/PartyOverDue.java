package saleOrder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Adaptor.OverDueAdapter;
import saleOrder.Model.mOverDue;
import saleOrder.Model.mParty;
import saleOrder.ViewModel.vmPartyOverDue;
import saleOrder.Views.iPartyOverDue;


public class PartyOverDue extends CustomActivity implements iPartyOverDue {

    vmPartyOverDue viewModel;
    AppCompatActivity context;

    RecyclerView itemlist;
    mParty party;
    OverDueAdapter overDueAdapter;
    Button cancel,ok;
    TextView partyTxt;
    CardView card_view;

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

        card_view = findViewById(R.id.card_view);
        card_view.setVisibility(View.GONE);
        ok = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);
        partyTxt = findViewById(R.id.party);

        itemlist = (RecyclerView) findViewById(R.id.itemList);
        overDueAdapter = new OverDueAdapter(context, viewModel.getOverDues(context));
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
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
        card_view.setVisibility(View.VISIBLE);
        overDueAdapter.update(overDues);
    }
}
