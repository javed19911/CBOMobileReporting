package com.cbo.cbomobilereporting.ui_new.dcr_activities.WorkWith;

import android.app.Activity;
import android.database.Cursor;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;

import saleOrder.ViewModel.CBOViewModel;
import utils.adapterutils.Dcr_Workwith_Model;

/**
 * Created by cboios on 10/03/19.
 */

public class vmWorkWith extends CBOViewModel<iWorkWith> {
    CBO_DB_Helper cbohelp;
    private WorkWithBuilder builder;

    public WorkWithBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(WorkWithBuilder builder) {
        this.builder = builder;
    }


    @Override
    public void onUpdateView(AppCompatActivity context, iWorkWith view) {
        cbohelp = new CBO_DB_Helper(context);
        view.getReferencesById();
        view.setTile();
    }

    public ArrayList<Dcr_Workwith_Model> getItems() {
        ArrayList<Dcr_Workwith_Model> docList = new ArrayList<>();
        try {

            if (getBuilder().getType().equals(WorkWithBuilder.WorkWithType.Doctor)) {
                Cursor c=cbohelp.getDR_Workwith();
                if(c.moveToFirst())
                {
                    do
                    {
                        docList.add(new Dcr_Workwith_Model(c.getString(c.getColumnIndex("workwith")),c.getString(c.getColumnIndex("wwid"))));
                    }	while(c.moveToNext());

                }
            }else {
                Cursor c=cbohelp.get_phdairy_person(getBuilder().getLookForId());
                if(c.moveToFirst())
                {
                    do
                    {
                        docList.add(new Dcr_Workwith_Model(c.getString(c.getColumnIndex("PERSON_NAME")),c.getString(c.getColumnIndex("PERSON_ID"))));
                    }	while(c.moveToNext());

                }
            }



        } catch (Exception e) {
        }
        return docList;
    }
}
