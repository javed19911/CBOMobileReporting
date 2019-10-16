package bill.NewOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mTax;

public class BillItemDB  extends DBHelper {
    public String getTableQuery() {
        //ITEM_ID":"1","ITEM_NAME":"Ab-Lol","SGST_PERCENT":"0","CGST_PERCENT":"0"
        return "create table " + this.getTable() + " (main_id integer primary key," +
                " ITEM_ID text,ITEM_NAME text,SGST_PERCENT text,CGST_PERCENT text,GST_TYPE text,STOCK text)";

    }

    public BillItemDB(Context c) {
        super(c);
    }

    public String getTable() {
        return "BILL_ITEM";
    }

    public int getTableVersion() {
        return 2;
    }

    public void onTableCreate(SQLiteDatabase db) {
        db.execSQL(this.getTableQuery());
    }

    public void onTableUpdate(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN STOCK text DEFAULT '0'");
        }
    }

    public void insert(mBillItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ITEM_ID", item.getId());
        contentValues.put("ITEM_NAME", item.getName());
        contentValues.put("GST_TYPE", item.getGST().getType().name());
        contentValues.put("SGST_PERCENT", item.getGST().getSGST());
        contentValues.put("CGST_PERCENT", item.getGST().getCGST());
        contentValues.put("STOCK", item.getStock());

        db.insert(this.getTable(), (String)null, contentValues);
    }

    public ArrayList<mBillItem> items(String filter_text) {
        ArrayList<mBillItem> items = new ArrayList();
        String Qry = "select * from " + this.getTable();
        if (!filter_text.equals("")) {
            Qry = Qry + " where ITEM_NAME LIKE '%" + filter_text + "%'";
        }

        Cursor res = this.getDatabase().rawQuery(Qry, (String[])null);
        res.moveToFirst();
        while(!res.isAfterLast()) {
            mBillItem item = new mBillItem().setId(res.getString(res.getColumnIndex("ITEM_ID")))
                    .setName(res.getString(res.getColumnIndex("ITEM_NAME")))
                    .setStock(res.getDouble(res.getColumnIndex("STOCK")));

            mTax GST = new mTax(eTax.valueOf(res.getString(res.getColumnIndex("GST_TYPE"))));
            GST.setSGST(res.getDouble(res.getColumnIndex("SGST_PERCENT")))
                    .setCGST(res.getDouble(res.getColumnIndex("CGST_PERCENT")));

            item.setGST(GST);
            items.add(item);
            res.moveToNext();
        }

        return items;
    }


}

