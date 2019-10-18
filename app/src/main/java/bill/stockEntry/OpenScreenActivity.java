package bill.stockEntry;

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

import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import bill.openingStock.OpeningStockActivity;
import bill.openingStock.mPage;
import utils_new.AppAlert;

public class OpenScreenActivity extends CustomActivity implements IOpen {


    TextView title, subTitle;
    AppCompatActivity context;
    FOpenCart fcompanycart;
    FNewOpen fcompanyneworder;
    //AppBarLayout appBarLayout;
//    mCompany company;
    Boolean orderChanged = false;
    MenuItem additem = null;
    private vmOpenView viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        context = this;
        viewModel = ViewModelProviders.of(this).get(vmOpenView.class);
        updateOrder((mBillOrder) getIntent().getSerializableExtra("order"));
//        company = ((mCompany) getIntent().getSerializableExtra("company"));
        fcompanycart = new FOpenCart();
        Bundle data = new Bundle();
        data.putSerializable("page", getIntent().getSerializableExtra("page"));
        data.putSerializable("PayModes", getIntent().getSerializableExtra("PayModes"));
        data.putSerializable("customer", getIntent().getSerializableExtra("customer"));
        fcompanycart.setArguments(data);
        viewModel = ViewModelProviders.of(this).get(vmOpenView.class);

        viewModel.setView(context, this);

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
        return MyCustumApplication.getInstance().getUser().getID();
    }


    @Override
    public void getReferencesById() {

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        //appBarLayout=findViewById(R.id.app_bar) ;
        title = toolbar.findViewById(R.id.title);
        subTitle = toolbar.findViewById(R.id.subTitle);

        fcompanyneworder = (FNewOpen) getSupportFragmentManager().findFragmentById(R.id.fcompanyneworder);

        if (((mPage)getIntent().getSerializableExtra("page")).getCode().equalsIgnoreCase(OpeningStockActivity.DOC_TYPE.OPENING.name())){
            fcompanyneworder.setFreeQtyNA(true);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fcart, fcompanycart);
        transaction.addToBackStack(null);
        transaction.commit();

        if (!viewModel.getOrder().getDocId().equalsIgnoreCase("0")
                && viewModel.getOrder().getStatus().equalsIgnoreCase("V")) {
            fcompanyneworder.HideFragment();
        }


        onItemEdit(new mBillItem().setName(""));

        viewModel.getOrderItem(context, !viewModel.isLoaded()
                && !(!viewModel.getOrder().getDocId().equalsIgnoreCase("0")
                && viewModel.getOrder().getStatus().equalsIgnoreCase("V")));

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
        if (viewModel.getOrder().getItems().size() == 0) {
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
                            "Are you sure to discard the changes?",
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

        } else {
            finish();
        }
    }

}

