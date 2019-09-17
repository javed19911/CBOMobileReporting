package Bill.Cart;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.google.android.material.appbar.AppBarLayout;

import Bill.BillReport.mCompany;
import Bill.NewOrder.FBillNeworder;
import Bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.Enum.eItem;
import utils_new.AppAlert;

public class CompanyCartActivity extends CustomActivity implements ICompanyCart{


    TextView title,subTitle;

    private vmCompanyCart viewModel;
    Activity context;
    FCompanyCart fcompanycart;
    FBillNeworder fcompanyneworder;
    AppBarLayout appBarLayout;
    mCompany company;
    Boolean orderChanged = false;
    MenuItem additem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_activity_cart);

        context = this;
        viewModel = ViewModelProviders.of(this).get(vmCompanyCart.class);
       // updateOrder((mOrder) getIntent().getSerializableExtra("order"));
        company = ((mCompany) getIntent().getSerializableExtra("company"));
        fcompanycart = new FCompanyCart();
        Bundle data = new Bundle();
        data.putSerializable("itemType", eItem.PRODUCT);
        fcompanycart.setArguments(data);
        viewModel = ViewModelProviders.of(this).get(vmCompanyCart.class);

        viewModel.setView(context,this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
       // additem = menu.findItem(R.id.add);
        if  (subTitle.getText().toString().equalsIgnoreCase("New Order") && additem != null){
            additem.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
             //   fcompanycart.addAttachment();
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void getReferencesById() {

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        appBarLayout=findViewById(R.id.app_bar) ;
        title  = toolbar.findViewById(R.id.title);
        subTitle = toolbar.findViewById(R.id.subTitle);

        fcompanyneworder = (FBillNeworder) getSupportFragmentManager().findFragmentById(R.id.fcompanyneworder);

        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction();
        transaction.add(R.id.fcart, fcompanycart);
        transaction.addToBackStack(null);
        transaction.commit();





        //onItemEdit(new mItem().setName(""));

    }

    @Override
    public String getActvityttitle() {
        return null;
    }

    @Override
    public void onItemAdded(mBillItem item) {


        orderChanged = true;
        fcompanycart.addItem(item);
    }

    @Override
    public void onItemEdit(mBillItem item) {

    }

    @Override
    public mOrder getOrder() {
        return null;
    }

    @Override
    public void setTitle(String header) {

        title.setText(company.getName());
        subTitle.setText(header);
    }


    @Override
    public void onBackPressed() {
        AppAlert.getInstance()
                .setPositiveTxt("Discard")
                .setNagativeTxt("Cancel")
                .DecisionAlert(context, "Alert !!!",
                        "Are you sure to discard the changes in the order?",
                        new AppAlert.OnClickListener() {
                            @Override
                            public void onPositiveClicked(View item, String result) {
                                finish();
                            }

                            @Override
                            public void onNegativeClicked(View item, String result) {
                                //fcompanycart.orderCommit();
                            }
                        });

    }

}

