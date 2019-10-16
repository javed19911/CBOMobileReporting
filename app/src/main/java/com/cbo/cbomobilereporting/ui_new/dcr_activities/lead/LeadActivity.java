package com.cbo.cbomobilereporting.ui_new.dcr_activities.lead;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import utils.adapterutils.PobModel;

public class LeadActivity extends AppCompatActivity implements iLead{


    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private aLead leadAdaptor;
    private vmLead viewModel;
    TextView itemincart;
    LinearLayout done;
    AppCompatActivity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmLead.class);
        viewModel.setBuilder((SamplePOBBuilder) getIntent().getSerializableExtra("builder"));
        viewModel.setView(context,this);
    }

    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        itemincart = findViewById(R.id.itemincart);
        done = findViewById(R.id.done);

        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        leadAdaptor = new aLead(this, viewModel.getItems(),viewModel.getBuilder());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(leadAdaptor);

        TextView filterTxt = findViewById(R.id.filterTxt);
        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                leadAdaptor.filter(s.toString());

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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendRespose(leadAdaptor.getSelectedItems());
            }
        });

        /*workWithAdaptor.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean b) {
                onSendRespose(callAdaptor.getItemAt(position));
            }
        });*/
    }

    @Override
    public void setTile() {
        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }



        TextView title = toolbar.findViewById(R.id.title);
        title.setText(viewModel.getBuilder().getTitle());
    }

    @Override
    public void onSendRespose(ArrayList<PobModel> model) {
        Intent intent = new Intent();
        intent.putExtra("Items",model);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}
