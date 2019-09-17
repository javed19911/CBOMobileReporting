package Bill.NewOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public class BillItemDB  extends DBHelper {
    public String getTableQuery() {
        //ITEM_ID":"1","ITEM_NAME":"Ab-Lol","SGST_PERCENT":"0","CGST_PERCENT":"0"
        return "create table " + this.getTable() + " (main_id integer primary key, ITEM_ID text,ITEM_NAME text,SGST_PERCENT text,CGST_PERCENT text)";

    }

    public BillItemDB(Context c) {
        super(c);
    }

    public String getTable() {
        return "BILL_ITEM";
    }

    public int getTableVersion() {
        return 1;
    }

    public void onTableCreate(SQLiteDatabase db) {
        db.execSQL(this.getTableQuery());
    }

    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(mBillItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_ID", item.getId());
        contentValues.put("ITEM_NAME", item.getName());
        contentValues.put("SGST_PERCENT", item.getCGST_PERCENT());
        contentValues.put("CGST_PERCENT", item.getSGST_PERCENT());

        db.insert(this.getTable(), (String)null, contentValues);
    }

    public ArrayList<mBillItem> items(String filter_text) {
        return this.items(filter_text, "");
    }

    public ArrayList<mBillItem> items(String filter_text, String order_type) {
        ArrayList<mBillItem> items = new ArrayList();
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
            mBillItem item = (new mBillItem()).setId(res.getString(res.getColumnIndex("ITEM_ID"))).setName(res.getString(res.getColumnIndex("ITEM_NAME")));
           /* mTax GST = new mTax(eTax.valueOf(res.getString(res.getColumnIndex("GST_TYPE"))));
            GST.setCGST(res.getDouble(res.getColumnIndex("TAX_PERCENT1"))).setSGST(res.getDouble(res.getColumnIndex("TAX_PERCENT2")));
            item.setGST(GST);*/
           /* mDeal deal = new mDeal();
            deal.setType(eDeal.get(res.getString(res.getColumnIndex("DEAL_TYPE")))).setFreeQty(res.getDouble(res.getColumnIndex("DEAL_QTY"))).setQty(res.getDouble(res.getColumnIndex("DEAL_ON")));
            item.setDeal(deal);*/
            items.add(item);
            res.moveToNext();
        }

        return items;
    }
}

