package com.cbo.cbomobilereporting.databaseHelper.Call.Local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.databaseHelper.Call.mChemistCall;

import java.util.ArrayList;

public class ChemistDb extends CBO_DB_Helper {

    //String CHEMIST_ADD_TABLE = "CREATE TABLE chemisttemp (
    // id integer primary key,chem_id integer,chem_name text,
    // visit_time text,chem_latLong text,chem_address text,
    // updated text,chem_km text,srno text,LOC_EXTRA text)";

    private static ChemistDb ourInstance = null;
    private ChemistDb() {
        super(MyCustumApplication.getInstance());
    }

    public static ChemistDb getInstance() {
        if (ourInstance == null)
        {
            ourInstance = new ChemistDb();
        }
        return ourInstance;
    }


    public ArrayList<mChemistCall> getChemistList(String Status) {
        ArrayList<mChemistCall> mChemistCalls = new ArrayList<>();
        SQLiteDatabase sd = this.getWritableDatabase();
        Cursor c = null;
        try {
            c = sd.rawQuery("select phchemist.chem_id,phchemist.chem_name,phchemist.LAST_VISIT_DATE," +
                    "DR_LAT_LONG,DR_LAT_LONG2,DR_LAT_LONG3,SHOWYN,chemisttemp.chem_latLong," +
                    "chemisttemp.srno, CASE WHEN IFNull(chemisttemp.chem_id,0) >0 THEN 1 ELSE 0 END AS CALLYN" +
                    " from phchemist LEFT OUTER JOIN chemisttemp  ON phchemist.chem_id=chemisttemp.chem_id" +
                    " where SHOWYN = '1' ", null);

           /* if (c.moveToFirst()) {
                do {
                    mChemistCalls.add(new mChemistCall()
                            .setName(c.getString(c.getColumnIndex("chem_name")))
                            .setId(c.getString(c.getColumnIndex("chem_id")))
                            .setLatLong( c.getString(c.getColumnIndex("chem_latLong")))
                            .setSrno(c.getString(c.getColumnIndex("chem_latLong")) c.getString(c.getColumnIndex("DR_LAT_LONG2")), c.getString(c.getColumnIndex("DR_LAT_LONG3")), c.getString(c.getColumnIndex("CALLYN"))));
                } while (c.moveToNext());
            }
*/

        }finally {
            if (c != null){
                c.close();
            }

            sd.close();
        }

        return mChemistCalls;
    }
}
