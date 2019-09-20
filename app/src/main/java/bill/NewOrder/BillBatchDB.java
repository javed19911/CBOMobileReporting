package bill.NewOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class BillBatchDB extends DBHelper {
    public String getTableQuery() {
        return "create table " + this.getTable() + " (main_id integer primary key, ITEM_ID text,BATCH_ID text,BATCH_NO text,MFG_DATE text,EXP_DATE text,PACK text,MRP_RATE text,SALE_RATE text)";
    }

    public BillBatchDB(Context c) {
        super(c);
    }

    public String getTable() {
        return "BILL_ITEM_BATCH";
    }

    public int getTableVersion() {
        return 1;
    }

    public void onTableCreate(SQLiteDatabase db) {
        db.execSQL(this.getTableQuery());
    }

    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(mBillBatch item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_ID", item.getITEM_ID());
        contentValues.put("BATCH_ID", item.getBATCH_ID());
        contentValues.put("BATCH_NO", item.getBATCH_NO());
        contentValues.put("MFG_DATE", item.getMFG_DATE());
        contentValues.put("EXP_DATE", item.getEXP_DATE());
        contentValues.put("PACK", item.getPACK());
        contentValues.put("MRP_RATE", item.getMRP_RATE());
        contentValues.put("SALE_RATE", item.getSALE_RATE());

        db.insert(this.getTable(), (String)null, contentValues);
    }


    public ArrayList<mBillBatch> batches(mBillItem item) {
        ArrayList<mBillBatch> batches = new ArrayList();
        String Qry = "select * from " + this.getTable();

        if (item != null) {
            Qry = Qry + " where ITEM_ID ='" + item.getId() + "'";
        }


        Cursor res = this.getDatabase().rawQuery(Qry, (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            mBillBatch batch = new mBillBatch()
                    .setITEM_ID(res.getString(res.getColumnIndex("ITEM_ID")))
                    .setBATCH_ID(res.getString(res.getColumnIndex("BATCH_ID")))
                    .setBATCH_NO(res.getString(res.getColumnIndex("BATCH_NO")))
                    .setMFG_DATE(res.getString(res.getColumnIndex("MFG_DATE")))
                    .setEXP_DATE(res.getString(res.getColumnIndex("EXP_DATE")))
                    .setPACK(res.getString(res.getColumnIndex("PACK")))
                    .setSALE_RATE(res.getString(res.getColumnIndex("SALE_RATE")))
                    .setMRP_RATE(res.getString(res.getColumnIndex("MRP_RATE")));


            batches.add(batch);
            res.moveToNext();
        }

        return batches;
    }
}


