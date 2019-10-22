package bill.NewOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eDiscount;
import cbomobilereporting.cbo.com.cboorder.Model.mDeal;
import cbomobilereporting.cbo.com.cboorder.Model.mDiscount;

public class BillBatchDB extends DBHelper {
    public String getTableQuery() {
        return "create table " + this.getTable() + " (main_id integer primary key, ITEM_ID text,BATCH_ID text," +
                "BATCH_NO text,MFG_DATE text,EXP_DATE text,PACK text,MRP_RATE text,SALE_RATE text,STOCK text," +
                "DEAL_TYPE text,DEAL_ON text,DEAL_QTY text,DIS_PERCENT1 text,DIS_PERCENT2 text,DIS_PERCENT3 text)";
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

        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_ID", item.getITEM_ID());
        contentValues.put("BATCH_ID", item.getBATCH_ID());
        contentValues.put("BATCH_NO", item.getBATCH_NO());
        contentValues.put("MFG_DATE", item.getMFG_DATE());
        contentValues.put("EXP_DATE", item.getEXP_DATE());
        contentValues.put("PACK", item.getPACK());
        contentValues.put("MRP_RATE", item.getMRP_RATE());
        contentValues.put("SALE_RATE", item.getSALE_RATE());


        contentValues.put("STOCK", item.getSTOCK());
        contentValues.put("DEAL_TYPE", item.getDeal().getType().getValue());
        contentValues.put("DEAL_QTY", item.getDeal().getFreeQty());
        contentValues.put("DEAL_ON", item.getDeal().getQty());
        contentValues.put("DIS_PERCENT1", item.getMiscDiscount().get(0).getPercent());
        contentValues.put("DIS_PERCENT3", item.getMiscDiscount().get(1).getPercent());
        contentValues.put("DIS_PERCENT3", item.getMiscDiscount().get(2).getPercent());



        getDatabase().insert(this.getTable(), (String)null, contentValues);
    }




    public void insert(ArrayList<mBillBatch> list) {
        SQLiteDatabase db = getDatabase();
        db.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            for (mBillBatch item : list) {
                contentValues.put("ITEM_ID", item.getITEM_ID());
                contentValues.put("BATCH_ID", item.getBATCH_ID());
                contentValues.put("BATCH_NO", item.getBATCH_NO());
                contentValues.put("MFG_DATE", item.getMFG_DATE());
                contentValues.put("EXP_DATE", item.getEXP_DATE());
                contentValues.put("PACK", item.getPACK());
                contentValues.put("MRP_RATE", item.getMRP_RATE());
                contentValues.put("SALE_RATE", item.getSALE_RATE());


                contentValues.put("STOCK", item.getSTOCK());
                contentValues.put("DEAL_TYPE", item.getDeal().getType().getValue());
                contentValues.put("DEAL_QTY", item.getDeal().getFreeQty());
                contentValues.put("DEAL_ON", item.getDeal().getQty());
                contentValues.put("DIS_PERCENT1", item.getMiscDiscount().get(0).getPercent());
                contentValues.put("DIS_PERCENT3", item.getMiscDiscount().get(1).getPercent());
                contentValues.put("DIS_PERCENT3", item.getMiscDiscount().get(2).getPercent());



                db.insert(this.getTable(), (String)null, contentValues);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<mBillBatch> batches(mBillItem item) {
        return  batches(item,null);
    }

    public ArrayList<mBillBatch> batches(mBillItem item,String batchId) {
        ArrayList<mBillBatch> batches = new ArrayList();
        String Qry = "select * from " + this.getTable();

        if (item != null) {
            Qry = Qry + " where ITEM_ID ='" + item.getId() + "'";
        }

        if (batchId != null){
            if (Qry.contains("where")){
                Qry = Qry + " AND BATCH_ID ='"+batchId+"'";
            }else{
                Qry = Qry + " where BATCH_ID ='"+batchId+"'";
            }
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
                    .setSALE_RATE(res.getDouble(res.getColumnIndex("SALE_RATE")))
                    .setMRP_RATE(res.getDouble(res.getColumnIndex("MRP_RATE")))
                    .setSTOCK(res.getDouble(res.getColumnIndex("STOCK")));

            mDeal deal = new mDeal();
            deal.setType(eDeal.get(res.getString(res.getColumnIndex("DEAL_TYPE"))))
                    .setFreeQty(res.getDouble(res.getColumnIndex("DEAL_QTY")))
                    .setQty(res.getDouble(res.getColumnIndex("DEAL_ON")));
            batch.setDeal(deal);

            batch.getMiscDiscount().clear();

            batch.getMiscDiscount().add(new mDiscount().setType(eDiscount.QI).setPercent(res.getDouble(res.getColumnIndex("DIS_PERCENT1"))));
            batch.getMiscDiscount().add(new mDiscount().setType(eDiscount.VI).setPercent(res.getDouble(res.getColumnIndex("DIS_PERCENT2"))));
            batch.getMiscDiscount().add(new mDiscount().setType(eDiscount.P).setPercent(res.getDouble(res.getColumnIndex("DIS_PERCENT3"))));



            batches.add(batch);
            res.moveToNext();
        }

        return batches;
    }
}


