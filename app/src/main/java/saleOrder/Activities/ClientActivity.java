package saleOrder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Adaptor.ClientAdapter;
import saleOrder.Model.mParty;
import saleOrder.ViewModel.vmClient;
import saleOrder.Views.iClient;

public class ClientActivity extends AppCompatActivity implements iClient {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private ClientAdapter clientAdapter;
    private vmClient viewModel;
    TextView itemincart;
    AppCompatActivity context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        context = this;
        viewModel = ViewModelProviders.of(this).get(vmClient.class);
        viewModel.setView(context,this);
    }



    @Override
    public void getReferencesById() {
        toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ImageView clearQry = findViewById(R.id.clearQry);
        itemlist_filter = (RecyclerView) findViewById(R.id.itemList);
        clientAdapter = new ClientAdapter(context, viewModel.getParties());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(clientAdapter);

        TextView filterTxt = findViewById(R.id.filterTxt);
        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                clientAdapter.filter(s.toString());

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
        clientAdapter.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                openOrder(clientAdapter.getPartyAt(position));
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
    public void setTile() {
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            //getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_black);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        TextView title = toolbar.findViewById(R.id.title);
        title.setText("Party List");
    }

    @Override
    public void openOrder(mParty party) {
        Intent intent = new Intent(context, MyOrder.class);
        intent.putExtra("party", party);
        intent.putExtra("ShowParty", false);
        startActivity(intent);
    }

    @Override
    public void openNewOrder(mParty party) {
        Intent intent = new Intent(context, CartActivity.class);
        intent.putExtra("order",new mOrder().setPartyId(party.getId()));
        startActivity(intent);
    }

    @Override
    public void onPartyListUpdated(ArrayList<mParty> parties) {
        clientAdapter.update(parties);
    }



}
