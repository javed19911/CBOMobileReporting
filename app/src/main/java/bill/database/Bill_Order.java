package bill.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import java.util.ArrayList;

import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.DBHelper.DBHelper;

public class Bill_Order extends DBHelper {

    public Bill_Order(Context c) {
        super(c);
    }


    @Override
    public String getTableQuery() {
        return "CREATE TABLE " + getTable() + "(MainId Integer PRIMARY KEY AUTOINCREMENT,PARTY_ID text,PARTY_NAME text," +
                "DOC_ID text,DOC_NO text,DOC_DATE text,AMT text,NET_AMT text,PAY_MODE text," +
                "STATUS text,GR_NO text,TRANSPORT text,GR_DATE text,BILL_NO text,BILL_DATE text,BILL_AMT text," +
                "CGSTAmt text,SGSTAmt text,TOT_AMT text,ROUND_AMT text,BILLED_HO text,APPROVED text,ATTACHMENT text,Remark text)";
    }

    @Override
    public String getTable() {
        return "Bill_Order";
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
        /*switch (oldVersion) {
            case 1:
                db.execSQL("ALTER TABLE " + this.getTable() + " ADD COLUMN SHOW_IN_TA_DA text DEFAULT '0'");

            default:
        }*/
    }

    public void insert(mBillOrder billoredr) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PARTY_ID", billoredr.getPartyId());
        contentValues.put("PARTY_NAME", billoredr.getPartyName());
        contentValues.put("DOC_ID", billoredr.getDocId());
        contentValues.put("DOC_NO", billoredr.getDocNo());
        contentValues.put("DOC_DATE", billoredr.getDocDate());
        contentValues.put("AMT", billoredr.getAmt());
        contentValues.put("NET_AMT", billoredr.getNetAmt());
        contentValues.put("PAY_MODE", billoredr.getPayMode());
        contentValues.put("STATUS", billoredr.getStatus());
        contentValues.put("GR_NO", billoredr.getGrNo());
        contentValues.put("TRANSPORT", billoredr.getTransport());
        contentValues.put("GR_DATE", billoredr.getGrDate());
        contentValues.put("BILL_NO", billoredr.getBillNo());
        contentValues.put("BILL_DATE", billoredr.getBillDate());
        contentValues.put("BILL_AMT", billoredr.getBillAmt());
        contentValues.put("CGSTAmt", billoredr.getCGSTAmt());
        contentValues.put("SGSTAmt", billoredr.getSGSTAmt());
        contentValues.put("TOT_AMT", billoredr.getTotAmt());
        contentValues.put("ROUND_AMT", billoredr.getRouAmt());
        contentValues.put("BILLED_HO", billoredr.getBilledHO());
        contentValues.put("APPROVED", billoredr.getApproved());
        contentValues.put("ATTACHMENT", billoredr.getAttachment());
        contentValues.put("Remark", billoredr.getRemark());
        getDatabase().insert(getTable(), null, contentValues);


    }


    public ArrayList<mBillOrder> get() {
        ArrayList<mBillOrder> billOrders = new ArrayList<mBillOrder>();
        String query = "Select * from " + getTable();
        Cursor c = getDatabase().rawQuery(query, null);
        try {
            if (c.moveToFirst()) {
                do {
                    mBillOrder billorder = new mBillOrder();

                    billorder.setPartyId(c.getString(c.getColumnIndex("PARTY_ID")));
                    billorder.setPartyName(c.getString(c.getColumnIndex("PARTY_NAME")));
                    billorder.setDocId(c.getString(c.getColumnIndex("DOC_ID")));
                    billorder.setDocNo(c.getString(c.getColumnIndex("DOC_NO")));
                    billorder.setDocDate(c.getString(c.getColumnIndex("DOC_DATE")));
                    billorder.setAmt(c.getDouble(c.getColumnIndex("AMT")));
                    billorder.setNetAmt(c.getDouble(c.getColumnIndex("NET_AMT")));
                    billorder.setPayMode(c.getString(c.getColumnIndex("PAY_MODE")));
                    billorder.setStatus(c.getString(c.getColumnIndex("STATUS")));
                    billorder.setGrNo(c.getString(c.getColumnIndex("GR_NO")));
                    billorder.setTransport(c.getString(c.getColumnIndex("TRANSPORT")));
                    billorder.setGrDate(c.getString(c.getColumnIndex("GR_DATE")));
                    billorder.setBillNo(c.getString(c.getColumnIndex("BILL_NO")));
                    billorder.setBillDate(c.getString(c.getColumnIndex("BILL_DATE")));
                    billorder.setBillAmt(c.getString(c.getColumnIndex("BILL_AMT")));
                    billorder.setCGSTAmt(c.getDouble(c.getColumnIndex("CGSTAmt")));
                    billorder.setSGSTAmt(c.getDouble(c.getColumnIndex("SGSTAmt")));

                    billOrders.add(billorder);
                } while (c.moveToNext());
            }
        } finally {
            c.close();
        }
        return billOrders;
    }


}
