package bill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cbo.cbomobilereporting.MyCustumApplication;

import java.util.ArrayList;

import bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public class Bill_Item extends DBHelper {

    public Bill_Item(Context c) {
        super(c);
    }


    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(Id Integer PRIMARY KEY AUTOINCREMENT," +
                "MAIN_ID Integer,ITEM_ID Integer,NAME text,BATCH_ID text,BATCH_NO text," +
                "MFG_DATE text,EXP_DATE text,PACK text,MRP_RATE text,SALE_RATE text,QTY text,FREE_QTY text," +
                "NET_AMT text,STOCK text,SGST_AMT text,CGST_AMT text,AMT text,TOT_AMT text)";
    }

    @Override
    public String getTable() {
        return "BILL_ITEM";
    }

    @Override
    public int getTableVersion() {
        return 1;
    }

    @Override
    public void onTableCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(getTableQuery());
    }

    @Override
    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {

            default:
        }
    }

    public void insert(String doc_id, mBillItem billItem) {

        new Deal(MyCustumApplication.getInstance()).insert(doc_id,billItem.getDeal());
//        new Discount(MyCustumApplication.getInstance()).insert(doc_id,billItem.getMiscDiscount());
        new Discount(MyCustumApplication.getInstance()).insert(doc_id,billItem.getMangerDiscount());
        new Discount(MyCustumApplication.getInstance()).insert(doc_id,billItem.getManualDiscount());

        ContentValues contentValues = new ContentValues();
        contentValues.put("MAIN_ID", doc_id);
        contentValues.put("ITEM_ID", billItem.getId());
        contentValues.put("NAME", billItem.getName());
        contentValues.put("BATCH_ID", billItem.getBATCH_ID());
        contentValues.put("BATCH_NO", billItem.getBATCH_NO());
        contentValues.put("MFG_DATE", billItem.getMFG_DATE());
        contentValues.put("EXP_DATE", billItem.getEXP_DATE());
        contentValues.put("PACK", billItem.getPACK());
        contentValues.put("MRP_RATE", billItem.getMRP_RATE());
        contentValues.put("SALE_RATE", billItem.getSALE_RATE());
        contentValues.put("QTY", billItem.getQty());
        contentValues.put("FREE_QTY", billItem.getFreeQty());
        contentValues.put("NET_AMT", billItem.getNetAmt());
        contentValues.put("STOCK", billItem.getStock());
        contentValues.put("SGST_AMT", billItem.getSGSTAmt());
        contentValues.put("CGST_AMT", billItem.getCGSTAmt());
        contentValues.put("AMT", billItem.getAmt());
        contentValues.put("TOT_AMT", billItem.getTotAmt());
        getDatabase().insert(getTable(), null, contentValues);

    }


    public ArrayList<mBillItem> get(String doc_id) {
        ArrayList<mBillItem> billItems = new ArrayList<mBillItem>();
        String query = "Select * from " + getTable()+" where MAIN_ID ='"+doc_id+"'";
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {

                    mBillItem bill = new mBillItem();
                    bill.setDeal(new Deal(MyCustumApplication.getInstance()).get(doc_id));
                    bill.setMiscDiscount(new Discount(MyCustumApplication.getInstance()).get(doc_id));
                    bill.setMangerDiscount(new Discount(MyCustumApplication.getInstance()).gets(doc_id));
                    bill.setManualDiscount(new Discount(MyCustumApplication.getInstance()).gets(doc_id));
                    bill.setGST(new Tax(MyCustumApplication.getInstance()).get(doc_id));
                    bill.setId(c.getString(c.getColumnIndex("ITEM_ID")));
                    bill.setName(c.getString(c.getColumnIndex("NAME")));
                    bill.setBATCH_ID(c.getString(c.getColumnIndex("BATCH_ID")));
                    bill.setBATCH_NO(c.getString(c.getColumnIndex("BATCH_NO")));
                    bill.setMFG_DATE(c.getString(c.getColumnIndex("MFG_DATE")));
                    bill.setEXP_DATE(c.getString(c.getColumnIndex("EXP_DATE")));
                    bill.setPACK(c.getString(c.getColumnIndex("PACK")));
                    bill.setMRP_RATE(c.getDouble(c.getColumnIndex("MRP_RATE")));
                    bill.setSALE_RATE(c.getDouble(c.getColumnIndex("SALE_RATE")));
                    bill.setFreeQty(c.getDouble(c.getColumnIndex("FREE_QTY")));
                    bill.setFreeQty(c.getDouble(c.getColumnIndex("NET_AMT")));
                    bill.setStock(c.getDouble(c.getColumnIndex("STOCK")));
                    bill.setAmt(c.getDouble(c.getColumnIndex("AMT")));
                    bill.setQty(c.getDouble(c.getColumnIndex("QTY")));

                    billItems.add(bill);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return billItems;
    }


}

