package com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;

import saleOrder.ViewModel.CBOViewModel;
import utils.adapterutils.SpinnerModel;

/**
 * Created by cboios on 10/03/19.
 */

public class vmCall extends CBOViewModel<iCall> {
    CBO_DB_Helper cbohelp;
    private CallBuilder builder;

    public CallBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(CallBuilder builder) {
        this.builder = builder;
    }


    @Override
    public void onUpdateView(Activity context, iCall view) {
        cbohelp = new CBO_DB_Helper(context);
        view.getReferencesById();
        view.setTile();
    }

    public ArrayList<SpinnerModel> getItems() {
        ArrayList<SpinnerModel> docList = new ArrayList<>();
        try {

            if (getBuilder().getType().equals(CallBuilder.CallType.Doctor)) {
                Cursor c = cbohelp.getDoctorListLocal();
                if (c.moveToFirst()) {
                    do {
                        docList.add(new SpinnerModel(c.getString(c.getColumnIndex("dr_name")) + "-" + c.getString(c.getColumnIndex("DR_AREA")),
                                c.getString(c.getColumnIndex("dr_id")), c.getString(c.getColumnIndex("LAST_VISIT_DATE")),
                                c.getString(c.getColumnIndex("CLASS")), c.getString(c.getColumnIndex("POTENCY_AMT")),
                                c.getString(c.getColumnIndex("ITEM_NAME")), c.getString(c.getColumnIndex("ITEM_POB")),
                                c.getString(c.getColumnIndex("ITEM_SALE")), c.getString(c.getColumnIndex("DR_AREA")),
                                c.getString(c.getColumnIndex("PANE_TYPE")), c.getString(c.getColumnIndex("DR_LAT_LONG")),
                                c.getString(c.getColumnIndex("FREQ")), c.getString(c.getColumnIndex("NO_VISITED")),
                                c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3")),
                                c.getString(c.getColumnIndex("COLORYN")), c.getString(c.getColumnIndex("CALLYN"))
                                , c.getString(c.getColumnIndex("CRM_COUNT")), c.getString(c.getColumnIndex("DRCAPM_GROUP")),
                                c.getString(c.getColumnIndex("APP_PENDING_YN")),c.getString(c.getColumnIndex("DRLAST_PRODUCT"))));
                    } while (c.moveToNext());

                }
            }else if (getBuilder().getType().equals(CallBuilder.CallType.Chemist)){
                Cursor c = cbohelp.getChemistListLocal();

                if (c.moveToFirst()) {
                    do {

                        docList.add(new SpinnerModel(c.getString(c.getColumnIndex("chem_name")), c.getString(c.getColumnIndex("chem_id")), c.getString(c.getColumnIndex("LAST_VISIT_DATE")), c.getString(c.getColumnIndex("DR_LAT_LONG")), c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))));
                    } while (c.moveToNext());
                }
            }else {
                Cursor c = cbohelp.getStockistListLocal();


                if (c.moveToFirst()) {
                    do {

                        docList.add(new SpinnerModel(c.getString(c.getColumnIndex("pa_name")), c.getString(c.getColumnIndex("pa_id")),"", c.getString(c.getColumnIndex("PA_LAT_LONG")), c.getString(c.getColumnIndex("PA_LAT_LONG2")), c.getString(c.getColumnIndex("PA_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))));
                    } while (c.moveToNext());
                }
            }


        } catch (Exception e) {
            Log.e("call List",e.getMessage());
        }
        return docList;
    }
}
