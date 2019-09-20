package bill.NewOrder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class BillItemDB  extends DBHelper {
    public String getTableQuery() {
        //ITEM_ID":"1","ITEM_NAME":"Ab-Lol","SGST_PERCENT":"0","CGST_PERCENT":"0"
        return "create table " + this.getTable() + " (main_id integer primary key," +
                " ITEM_ID text,ITEM_NAME text,SGST_PERCENT text,CGST_PERCENT text)";

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
        ArrayList<mBillItem> items = new ArrayList();
        String Qry = "select * from " + this.getTable();
        if (!filter_text.equals("")) {
            Qry = Qry + " where ITEM_NAME LIKE '%" + filter_text + "%'";
        }

        Cursor res = this.getDatabase().rawQuery(Qry, (String[])null);
        res.moveToFirst();
        while(!res.isAfterLast()) {
            mBillItem item = (new mBillItem().setId(res.getString(res.getColumnIndex("ITEM_ID")))
                    .setName(res.getString(res.getColumnIndex("ITEM_NAME")))
                    .setSGST_PERCENT(res.getDouble(res.getColumnIndex("SGST_PERCENT")))
                    .setCGST_PERCENT(res.getDouble(res.getColumnIndex("CGST_PERCENT"))));

            items.add(item);
            res.moveToNext();
        }

        return items;
    }


}

