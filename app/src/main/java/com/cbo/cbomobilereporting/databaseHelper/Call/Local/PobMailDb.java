package com.cbo.cbomobilereporting.databaseHelper.Call.Local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mChemist;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mStockist;

import java.util.ArrayList;

import utils.model.DropDownModel;

public class PobMailDb extends CBO_DB_Helper {

    public static String PobMailDb_TABLE_NAME = "PobMailDb";
    public static String PobMailDb_Query = "CREATE TABLE "
            + PobMailDb_TABLE_NAME + "(MainId Integer PRIMARY KEY AUTOINCREMENT," +
            "CHEMIST_NAME text,CHEMIST_ID text,STK_ID text,STK_NAME text," +
            "IS_SEND text,ITEM_ID text,QTY text,RATE text,AMOUNT text)";


    private static PobMailDb instance = null;
    private SQLiteDatabase sd;


    private PobMailDb(Context c) {
        super(c);
    }

    public static PobMailDb getInstance(Context context) {
        if (instance == null) {
            instance = new PobMailDb(context);
        }
        return instance;
    }

    private SQLiteDatabase getDatabase() {
        sd = getWritableDatabase();
        return sd;
    }

    @Override
    protected void finalize() throws Throwable {
        if (sd != null) {
            sd.close();
        }
        super.finalize();
    }

    public void insert(mChemist chemist) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CHEMIST_ID", chemist.getID());
        contentValues.put("CHEMIST_NAME", chemist.getNAME());
        contentValues.put("IS_SEND", chemist.getIS_SEND());
        contentValues.put("ITEM_ID", chemist.getItemId());
        contentValues.put("QTY", chemist.getQTY());
        contentValues.put("RATE", chemist.getRate());
        contentValues.put("STK_ID", chemist.getSelectedStokist().getID());
        contentValues.put("STK_NAME", chemist.getSelectedStokist().getNAME());
        contentValues.put("AMOUNT", chemist.getAmount());
        getDatabase().insert(PobMailDb_TABLE_NAME, null, contentValues);
    }


    public ArrayList<mChemist> getChemist() {
        ArrayList<mChemist> chemists = new ArrayList<>();

        Cursor c = null;
        try {
              c = getDatabase().rawQuery("select c.chem_id,c.chem_name," +
                    "dc.allitemid as item_id,dc.sample as qty,dc.rate,"+
                    "IFNull(p.STK_ID,0) as STK_ID,p.STK_NAME ,dc.pob_amt as Amt,"+
                    "CASE WHEN IFNull(p.CHEMIST_ID,0) >0 THEN 1 ELSE 0 END AS Is_Send" +
                    " from chemisttemp as c LEFT OUTER JOIN "+
                      PobMailDb_TABLE_NAME+" as p ON p.CHEMIST_ID=c.chem_id" +
                    " LEFT OUTER JOIN phdcrchem as dc ON dc.chem_id=c.chem_id", null);

            if (c.moveToFirst()) {
                do {
                    mChemist chemist=new mChemist()
                            .setNAME(c.getString(c.getColumnIndex("chem_name")))
                            .setID (c.getInt(c.getColumnIndex("chem_id")))
                            .setRate( c.getString(c.getColumnIndex("rate")))
                            .setIS_SEND(c.getInt(c.getColumnIndex("Is_Send")))
                            .setItemId(c.getString(c.getColumnIndex("item_id")))
                            .setQTY(c.getString(c.getColumnIndex("qty")))
                            .setAmount(c.getString(c.getColumnIndex("Amt")));

                   if (c.getInt(c.getColumnIndex("STK_ID")) != 0) {
                       mStockist stk = new mStockist();
                       stk.setID(c.getInt(c.getColumnIndex("STK_ID")));
                       stk.setNAME(c.getString(c.getColumnIndex("STK_NAME")));
                       chemist.setSelectedStokist(stk);
                   }

                    chemists.add(chemist);
                            // c.getString(c.getColumnIndex("DR_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))));
                } while (c.moveToNext());
            }

        }finally {
            if (c != null){
                c.close();
            }
        }
        return  chemists;
    }
}
