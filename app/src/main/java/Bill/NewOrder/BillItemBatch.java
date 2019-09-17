package Bill.NewOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class BillItemBatch extends DBHelper {
    public String getTableQuery() {
        //ITEM_ID":"1","BATCH_ID":"1","BATCH_NO":"T-13331","MFG_DATE":"01/08/2013","EXP_DATE":"31/07/2015","PACK":"1X 10","MRP_RATE":"77","":"77"
        return "create table " + this.getTable() + " (main_id integer primary key, ITEM_ID text,BATCH_ID text,BATCH_NO text,MFG_DATE text,EXP_DATE text,PACK text,MRP_RATE text,SALE_RATE text)";
    }

    public BillItemBatch(Context c) {
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

    public void insert(mBillItem_Batch item) {
        //ITEM_ID":"1","BATCH_ID":"1","BATCH_NO":"T-13331","MFG_DATE":"01/08/2013","EXP_DATE":"31/07/2015","PACK":"1X 10","MRP_RATE":"77","":"77"
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

    /*public ArrayList<mItem> items(String filter_text) {
        return this.items(filter_text, "");
    }

    public ArrayList<mItem> items(String filter_text, String order_type) {
        ArrayList<mItem> items = new ArrayList();
        String Qry = "select * from " + this.getTable();
        if (!filter_text.equals("")) {
            Qry = Qry + " where ITEM_NAME LIKE '%" + filter_text + "%'";
        }

        if (!order_type.equals("") && !filter_text.equals("")) {
            Qry = Qry + " and KIT_TYPE ='" + order_type + "'";
        } else if (!order_type.equals("")) {
            Qry = Qry + " where KIT_TYPE  ='" + order_type + "'";
        }

        Cursor res = this.getDatabase().rawQuery(Qry, (String[])null);
        res.moveToFirst();

        while(!res.isAfterLast()) {
            mItem item = (new mItem()).setId(res.getString(res.getColumnIndex("ITEM_ID"))).setName(res.getString(res.getColumnIndex("ITEM_NAME"))).setRate(res.getDouble(res.getColumnIndex("RATE"))).setQty(res.getDouble(res.getColumnIndex("QTY"))).setFreeQty(res.getDouble(res.getColumnIndex("FREE_QTY"))).setMRP(res.getString(res.getColumnIndex("MRP_RATE"))).setPack(res.getString(res.getColumnIndex("PACK"))).setType(res.getString(res.getColumnIndex("KIT_TYPE"))).setGropuID(res.getInt(res.getColumnIndex("ITEM_GROUP_ID")));
            mTax GST = new mTax(eTax.valueOf(res.getString(res.getColumnIndex("GST_TYPE"))));
            GST.setCGST(res.getDouble(res.getColumnIndex("TAX_PERCENT1"))).setSGST(res.getDouble(res.getColumnIndex("TAX_PERCENT2")));
            item.setGST(GST);
            mDeal deal = new mDeal();
            deal.setType(eDeal.get(res.getString(res.getColumnIndex("DEAL_TYPE")))).setFreeQty(res.getDouble(res.getColumnIndex("DEAL_QTY"))).setQty(res.getDouble(res.getColumnIndex("DEAL_ON")));
            item.setDeal(deal);
            items.add(item);
            res.moveToNext();
        }

        return items;
    }*/
}


