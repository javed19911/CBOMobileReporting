package Bill.CompanySelecter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.util.ArrayList;

import Bill.BillReport.mCompany;
import Bill.Cart.CompanyCartActivity;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;

public class CompanyActivity extends CustomActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private aCompany acustomer;
    Activity context;
    private mCompany company;
    private ArrayList<mCompany>Companylist= new ArrayList<mCompany>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        context = this;
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        acustomer = new aCompany(context, Companylist);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(acustomer);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        TextView title = toolbar.findViewById(R.id.title);
        title.setText("Select Company");
        TextView filterTxt = findViewById(R.id.filterTxt);

        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                acustomer.filter(s.toString());

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
        acustomer.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                openCompanyCart(acustomer.getPartyAt(position));
            }
        });

        Companylist= (ArrayList<mCompany>) getIntent().getSerializableExtra("Companies");
        Companylist.remove(Companylist.size()-1);
        setupCompanylist(Companylist);
        

    }

    private void openCompanyCart(mCompany partyAt) {
        Intent intent = new Intent(context, CompanyCartActivity.class);
        //intent.putExtra("order",new mOrder().setPartyId(getPartyID()));
        intent.putExtra("company",partyAt);
        startActivity(intent);
    }

    private void setupCompanylist(ArrayList<mCompany> companylist) {
        acustomer.update(companylist);
    }




}

