package bill.CompanySelecter;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import bill.BillReport.mCompany;
import bill.BillReport.mPay;
import bill.Cart.CompanyCartActivity;
import bill.Cart.mCustomer;
import bill.mBillOrder;
import bill.openingStock.OpeningStockActivity;
import bill.openingStock.mPage;
import bill.stockEntry.OpenScreenActivity;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import utils_new.CustomDatePicker;

public class CompanyActivity extends CustomActivity {

    androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView itemlist_filter;
    private aCompany acustomer;
    private ArrayList<mPay> paymodes = new ArrayList();
    AppCompatActivity context;
    TextView bill_date;
    ImageView bill_date_img;
    private ArrayList<mCompany>Companylist= new ArrayList<mCompany>();
    private OpeningStockActivity.DOC_TYPE doc_type = OpeningStockActivity.DOC_TYPE.BILL;
    mPage page ;



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
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }


        doc_type = (OpeningStockActivity.DOC_TYPE) getIntent().getSerializableExtra("doc_type");
        if (getIntent().getSerializableExtra("page") != null){
            page = (mPage) getIntent().getSerializableExtra("page");
        }else{
            page =  new mPage("New Bill",doc_type.name());
        }


        TextView title = toolbar.findViewById(R.id.title);
        title.setText("Select Company");
        TextView filterTxt = findViewById(R.id.filterTxt);

        bill_date = findViewById(R.id.bill_date);
        bill_date_img = findViewById(R.id.bill_date_img);
        bill_date.setText(CustomDatePicker.currentDate(CustomDatePicker.ShowFormatOld));

        bill_date_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new CustomDatePicker(context, null, new Date()

                    ).Show(CustomDatePicker.getDate(bill_date.getText().toString(),  CustomDatePicker.ShowFormatOld)
                            , new CustomDatePicker.ICustomDatePicker() {
                                @Override
                                public void onDateSet(Date date) {
                                    bill_date.setText(CustomDatePicker.formatDate(date,CustomDatePicker.ShowFormatOld));
                                }
                            });
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        });

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

        paymodes = (ArrayList<mPay>) getIntent().getSerializableExtra("PayModes");
        Companylist= (ArrayList<mCompany>) getIntent().getSerializableExtra("Companies");
        Companylist.remove(Companylist.size()-1);
        acustomer.update(Companylist);

        //bill_date_img.performClick();

        

    }

    private void openCompanyCart(mCompany party) {
        Intent intent = new Intent(context, OpenScreenActivity.class);
        if (OpeningStockActivity.DOC_TYPE.BILL == doc_type) {
            intent = new Intent(context, CompanyCartActivity.class);
        }
        intent.putExtra("page", page);
        intent.putExtra("order", new mBillOrder().setPartyId(party.getId())
                .setPartyName(party.getName())
                .setDocDate(bill_date.getText().toString()));
        intent.putExtra("customer", new mCustomer());
        intent.putExtra("PayModes", paymodes);
        startActivity(intent);
        finish();

    }






}

