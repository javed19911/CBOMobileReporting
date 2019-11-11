package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.pobmail.IPobStoKist;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.selection.Selection;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class ChemistFragment extends Fragment implements iChemist, IPobStoKist {

    private static final int STOKIST_FILTER = 0;
    AppCompatActivity context;
    aChemist apending;
    vmChemist viewModel;
    View fragmentView;
    String filterType = "Generic";
    String AppYN = "";
    Boolean ShowParty = false;
    private RecyclerView itemlist_filter;
    private mChemist selectStockist;
    private Button btCommit;

    private ArrayList<mChemist> chemestList = new ArrayList<mChemist>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case STOKIST_FILTER:
                    selectStockist = (mChemist) data.getSerializableExtra("chemist");
//                    Toast.makeText(context, selectStockist.getSelectedStokist().getNAME(), Toast.LENGTH_SHORT).show();
//                    viewModel.setData(filterType, selectStockist);
                    if (getActivity() instanceof IPobStoKist) {
                        ((IPobStoKist) getActivity()).updateChemistList(this, filterType, selectStockist);
                    }
                    break;
                default:

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentView = inflater.inflate(R.layout.pending_list_fragment,
                container, false);
        context = (AppCompatActivity) getActivity();
        viewModel = ViewModelProviders.of(this).get(vmChemist.class);
        viewModel.setView(context, this);
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void getReferencesById() {
        filterType = getArguments().getString("OrderType");
        itemlist_filter = (RecyclerView) fragmentView.findViewById(R.id.itemList);
        btCommit = (Button) fragmentView.findViewById(R.id.btCommit);
        apending = new aChemist(context, viewModel.getChemist());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        itemlist_filter.setLayoutManager(mLayoutManager1);
        itemlist_filter.setItemAnimator(new DefaultItemAnimator());
        itemlist_filter.setAdapter(apending);

        if (filterType.contains("Pending")) {
            filterType = "P";
            btCommit.setVisibility(View.VISIBLE);
        } else if (filterType.contains("Completed")) {
            filterType = "C";
            btCommit.setVisibility(View.GONE);
        } else {
            filterType = "All";
            btCommit.setVisibility(View.GONE);
        }
//        if (filterType.equalsIgnoreCase("P")) {
        if (getActivity() instanceof IPobStoKist) {
            ((IPobStoKist) getActivity()).getChemestList(this, filterType);
        }
//        }

        apending.setOnClickListner(new RecyclerStokistListner() {
            @Override
            public void onClick(View view, int position, boolean isLongClick, ArrayList<mChemist> chemests) {
                if (filterType.equalsIgnoreCase("P")) {
                    if (chemests.get(position).getStockists().size() != 0) {
                        Intent intent = new Intent(context, Selection.class);
                        intent.putExtra("DATA", chemests.get(position));
                        startActivityForResult(intent, STOKIST_FILTER);
                    }
                }

            }
        });

        btCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                StringBuilder rateStr = new StringBuilder();
                StringBuilder amountStr = new StringBuilder();
                StringBuilder QuantityStr = new StringBuilder();
                StringBuilder ItemIdStr = new StringBuilder();
                StringBuilder chemestStr = new StringBuilder();
                StringBuilder stokistStr = new StringBuilder();

                for (mChemist chemest : chemestList) {

                    if (chemest.getSelectedStokist() != null) {
                        String SEPERATER = ",";
                        if (chemestStr.toString().equals("")) {
                            SEPERATER = "";
                        }
                        chemestStr.append(SEPERATER).append(chemest.getID());
                        stokistStr.append(SEPERATER).append(chemest.getSelectedStokist().getID());
                        rateStr.append(SEPERATER).append(chemest.getRate());
                        amountStr.append(SEPERATER).append(chemest.getAmount());
                        QuantityStr.append(SEPERATER).append(chemest.getQTY());
                        ItemIdStr.append(SEPERATER).append(chemest.getItemId());
                    }


                }
                viewModel.commitStokist(context, "11184", chemestStr, stokistStr,rateStr,amountStr,QuantityStr,ItemIdStr);
            }
        });
    }

    public void addAttachment(mChemist order) {

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
        return "PobMail";
    }


    @Override
    public void setTile(String title) {

    }

    @Override
    public void oPendingListChange(ArrayList<mChemist> chemests) {
        chemestList = chemests;
        viewModel.setChemest(chemestList);
        apending.update(chemests);
    }

    @Override
    public void getChemestList(ChemistFragment chemistFragment, String filterType) {

    }

    @Override
    public void updateChemistList(ChemistFragment chemistFragment, String filterType, mChemist selectStockist) {

    }
}
