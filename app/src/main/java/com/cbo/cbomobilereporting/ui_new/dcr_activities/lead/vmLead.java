package com.cbo.cbomobilereporting.ui_new.dcr_activities.lead;

import android.app.Activity;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;

import saleOrder.ViewModel.CBOViewModel;
import utils.adapterutils.PobModel;

/**
 * Created by cboios on 10/03/19.
 */

public class vmLead extends CBOViewModel<iLead> {
    CBO_DB_Helper cbohelp;
    private SamplePOBBuilder builder;

    public SamplePOBBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(SamplePOBBuilder builder) {
        this.builder = builder;
    }


    @Override
    public void onUpdateView(AppCompatActivity context, iLead view) {
        cbohelp = new CBO_DB_Helper(context);
        view.getReferencesById();
        view.setTile();
    }

    public ArrayList<PobModel> getItems() {
        ArrayList<PobModel> docList = new ArrayList<>();
        try {

            Cursor c;
            switch (getBuilder().getType()){
                case GIFT:
                    c = cbohelp.getAllProducts("0",getBuilder().getCallType().getValue());
                    break;
                case LEAD:
                    c = cbohelp.getAllLeadProducts("0");
                    break;
                default:
                    c = cbohelp.getAllProducts("0",getBuilder().getCallType().getValue());

            }

            if (c.moveToFirst()) {
                do {
                    docList.add(new PobModel(c.getString(c.getColumnIndex("item_name")),
                            c.getString(c.getColumnIndex("item_id")),
                            c.getString(c.getColumnIndex("stk_rate")),
                            c.getString(c.getColumnIndex("sn")),
                            c.getInt(c.getColumnIndex("STOCK_QTY")),
                            c.getInt(c.getColumnIndex("BALANCE")),
                            c.getInt(c.getColumnIndex("SPL_ID"))));
                } while (c.moveToNext());
            }




        } catch (Exception e) {
        }
        return docList;
    }
}
