package saleOrder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Adaptor.Order_List_Adaptor;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.R;
import cbomobilereporting.cbo.com.cboorder.View.iOrder;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Activities.CartActivity;
import saleOrder.Activities.CartAttachment;
import saleOrder.Activities.PartyOverDue;
import saleOrder.Model.mParty;
import saleOrder.ViewModel.vmOrder;
import utils_new.AppAlert;


public class OrderListFragment extends Fragment implements iOrder {

    private RecyclerView itemlist_filter;
//    private OrderAdapter orderAdapter;
//    private List<Order> orderList;

    ListView listView;
    AppCompatActivity context;
    Order_List_Adaptor order_list_adaptor;
    mParty party;

    String filterTxt="",LatLong="";
    String filterType="Generic";
    vmOrder viewModel;
    View fragmentView;
    String AppYN ="";
    Boolean ShowParty = false;
    private static final int OVER_DUE = 0;
    private static final int ATTACHMENT = 10;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case OVER_DUE:
                    viewModel.getOrderDetail(context,(mOrder) data.getSerializableExtra ("order"));
                    break;
                default:

            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.retailer_filter_content,
                container, false);


        context = (AppCompatActivity) getActivity();
        viewModel = ViewModelProviders.of(this).get(vmOrder.class);
        viewModel.setView(context,this);


        return fragmentView;
    }



    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    @Override
    public void getReferencesById() {
        //itemlist_filter = (RecyclerView) fragmentView.findViewById(R.id.itemList);
       /* listView = fragmentView.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               viewModel.getOrderDetail(context,viewModel.getOrders().get(position));
            }
        });*/


        party = ((mParty) getArguments().getSerializable("party"));
        filterType = getArguments().getString("OrderType");
        ShowParty = getArguments().getBoolean("ShowParty");


        itemlist_filter = (RecyclerView) fragmentView.findViewById(R.id.itemList);
        order_list_adaptor = new Order_List_Adaptor(context, viewModel.getOrders(),party.getId().equalsIgnoreCase("0") || ShowParty);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(order_list_adaptor);



        if (filterType.contains("Pending")) {
            filterType = "P";
            AppYN = "N";
//            tabLayout.setBackgroundColor(Color.parseColor ("#329932"));
//            toolbar.setBackgroundColor(Color.parseColor ("#329932"));
        }
        else if (filterType.contains("Not"))
            filterType = "ND";
        else if (filterType.contains("Approved")) {
            filterType = "A";
            AppYN = "Y";
        }
        else if (filterType.contains("Billed"))
            filterType = "C";
        else
            filterType = "ALL";

        viewModel.setStatus(filterType);

        if (filterType.equalsIgnoreCase("P")){
            viewModel.getOrderListAPI(context,"ALL");
        }

        order_list_adaptor.setOnClickListner(new RecycleViewOnItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (view.getId () == com.cbo.cbomobilereporting.R.id.delete) {

                    AppAlert.getInstance().DecisionAlert(context, "Delete !!!",
                            "Are you sure to delete?\nOrder No : "
                                    + viewModel.getOrders().get(position).getDocNo()
                            , new AppAlert.OnClickListener() {
                                @Override
                                public void onPositiveClicked(View item, String result) {
                                    viewModel.DeleteOrder(context,viewModel.getOrders().get(position));
                                }

                                @Override
                                public void onNegativeClicked(View item, String result) {

                                }
                            });

                }else if (view.getId () == com.cbo.cbomobilereporting.R.id.attach) {
                    addAttachment(viewModel.getOrders().get(position));
                   //MyCustumApplication.getInstance().LoadURL("Attachment",viewModel.getOrders().get(position).getAttachment());

                }else{

                    //showPartyOverDue(viewModel.getOrders().get(position));

                    viewModel.getOrderDetail(context,viewModel.getOrders().get(position));
                }

            }
        });
    }

    public void addAttachment(mOrder order) {
        Intent intent = new Intent (context, CartAttachment.class);
        intent.putExtra ("order", order);
        startActivityForResult (intent, ATTACHMENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!getUserVisibleHint()) {
            return;
        }

        viewModel.setStatus(filterType);
    }

    @Override
    public String getPartyID() {
        if (ShowParty){
            party.setId("0");
        }
        return party.getId();
    }

    @Override
    public String getUserID() {
        return MyCustumApplication.getInstance().getUser().getID();
    }

    @Override
    public String getCompanyCode() {
        return MyCustumApplication.getInstance().getUser().getCompanyCode();
    }

    @Override
    public String getActivityTitle() {
        return "Order List new";
    }

    @Override
    public String getAppYN() {
        return "";//AppYN;
    }

    @Override
    public void setTile(String title) {

    }

    @Override
    public void onOrderListChanged(ArrayList<mOrder> orders) {
        //order_list_adaptor=new Order_List_Adaptor(context,orders);
        //listView.setAdapter(order_list_adaptor);
        order_list_adaptor.update(orders);
    }

    @Override
    public void onOrderChanged(mOrder order) {

    }

    @Override
    public void showPartyOverDue(mOrder order) {
        Intent intent = new Intent(context, PartyOverDue.class);
        if (party.getId().equalsIgnoreCase("0")){
            ShowParty = true;
            party.setId(order.getPartyId());
            party.setName(order.getPartyName());
        }
        intent.putExtra("party",party);
        intent.putExtra("order",order);
        startActivityForResult(intent,OVER_DUE);
    }


    @Override
    public void showOderDetail(mOrder order) {
        // show dialog
        if (order.getItems().size()>0) {
            Intent intent = new Intent(context, CartActivity.class);
            if (party.getId().equalsIgnoreCase("0")){
                ShowParty = true;
                party.setId(order.getPartyId());
                party.setName(order.getPartyName());
            }
            intent.putExtra("party",party);
            intent.putExtra("order",order);
            startActivity(intent);
        }else{
            Toast.makeText(context,"No item found for the Order no. "+ order.getDocNo(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void newOrder() {
        Intent intent = new Intent(context, CartActivity.class);
        intent.putExtra("order",new mOrder().setPartyId(getPartyID()));
        startActivity(intent);
    }


}
