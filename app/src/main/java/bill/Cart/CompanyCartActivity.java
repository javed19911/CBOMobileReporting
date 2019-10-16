package bill.Cart;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;

import bill.NewOrder.FBillNeworder;
import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import utils_new.AppAlert;

public class CompanyCartActivity extends CustomActivity implements ICompanyCart{


    TextView title,subTitle;

    private vmCompanyCart viewModel;
    AppCompatActivity context;
    FCompanyCart fcompanycart;
    FBillNeworder fcompanyneworder;
    //AppBarLayout appBarLayout;
//    mCompany company;
    Boolean orderChanged = false;
    MenuItem additem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_activity_cart);

        context = this;
        viewModel = ViewModelProviders.of(this).get(vmCompanyCart.class);
        updateOrder((mBillOrder) getIntent().getSerializableExtra("order"));
//        company = ((mCompany) getIntent().getSerializableExtra("company"));
        fcompanycart = new FCompanyCart();
        Bundle data = new Bundle();
        data.putSerializable("PayModes", getIntent().getSerializableExtra("PayModes"));
        data.putSerializable("customer", getIntent().getSerializableExtra("customer"));
        fcompanycart.setArguments(data);
        viewModel = ViewModelProviders.of(this).get(vmCompanyCart.class);

        viewModel.setView(context,this);

    }


    /*@Override
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
*/

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getUserId() {
        return MyCustumApplication.getInstance ().getUser ().getID ();
    }


    @Override
    public void getReferencesById() {

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        //appBarLayout=findViewById(R.id.app_bar) ;
        title  = toolbar.findViewById(R.id.title);
        subTitle = toolbar.findViewById(R.id.subTitle);

        fcompanyneworder = (FBillNeworder) getSupportFragmentManager().findFragmentById(R.id.fcompanyneworder);

        FragmentTransaction transaction = getSupportFragmentManager ().beginTransaction();
        transaction.add(R.id.fcart, fcompanycart);
        transaction.addToBackStack(null);
        transaction.commit();

        if (!viewModel.getOrder ().getDocId ().equalsIgnoreCase ("0")
            && viewModel.getOrder ().getStatus ().equalsIgnoreCase ("V")){
            fcompanyneworder.HideFragment();
        }


        onItemEdit(new mBillItem().setName(""));

        viewModel.getOrderItem(context,!viewModel.isLoaded());

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
        //appBarLayout.setExpanded(true);
        fcompanyneworder.setItem(item);
    }

    @Override
    public mBillOrder getOrder() {
        return viewModel.getOrder();
    }

    @Override
    public void updateOrder(mBillOrder order) {
        viewModel.setOrder(order);
    }

    @Override
    public void onItemSynced() {
        if (viewModel.getOrder().getItems().size()==0){
            fcompanyneworder.openItemFilter(viewModel.getOrder());
        }
    }

    @Override
    public void setTitle(String header) {

        title.setText(viewModel.getOrder().getPartyName());
        subTitle.setText(header);
    }


    @Override
    public void onBackPressed() {
        if (orderChanged) {
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

        }else{
            finish();
        }
    }

}

