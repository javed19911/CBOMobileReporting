package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.selection;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mChemist;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mStockist;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import utils_new.AppAlert;

public class Selection extends CustomActivity implements iSelection {

    private vmSelection viewModel;
    private Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private ArrayList<mStockist> stockList = new ArrayList<>();
    private mChemist chemist = new mChemist();
    private aSelection aselection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmSelection.class);
        viewModel.setView(context, this);
        context = this;
    }

    @Override
    public void getReferencesById() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        chemist = (mChemist) getIntent().getSerializableExtra("DATA");
        aselection = new aSelection(context, chemist.getStockists());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(aselection);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }
        TextView title = toolbar.findViewById(R.id.title);
        title.setText("Select Stokist");
        TextView filterTxt = findViewById(R.id.filterTxt);

        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                aselection.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        clearQry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterTxt.setText("");
            }
        });
        aselection.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                if (!chemist.getStockists().get(position).getEMAIL().equalsIgnoreCase("")) {
                    chemist.setSelectedStokist(chemist.getStockists().get(position));
                    onSendResponse(chemist);
                } else {
                    AppAlert.getInstance().getAlert(context,"Alert!!!","Selection Email Not Found");
                }
            }
        });
    }

    @Override
    public String getActivityTitle() {
        return null;
    }

    @Override
    public void setTile(String var1) {

    }

    @Override
    public void onPartyListUpdated(ArrayList<mStockist> stockists) {

    }

    public void onSendResponse(mChemist mchemest) {
        Intent intent = new Intent();
        intent.putExtra("chemist", mchemest);
        setResult(RESULT_OK, intent);
        finish();
    }

}
