package com.cbo.cbomobilereporting.databaseHelper.Sync;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.ExpHeadDB;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.OthExpenseDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.CboProgressDialog;
import com.uenics.javed.CBOLibrary.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import FirebaseDatabase.FirebsaeDB;
import utils_new.AppAlert;

import static utils_new.Custom_Variables_And_Method.FMCG_PREFRENCE;
import static utils_new.Custom_Variables_And_Method.db;

public class SyncAllDataFirebase {

    Context context;
    CBO_DB_Helper db_helper;
    SyncDB syncDB;
    CboProgressDialog cboProgressDialog ;

    OthExpenseDB othExpenseDB;
    ExpHeadDB expHeadDB;

    ArrayList<String> privateKeys = new ArrayList<>();

    public SyncAllDataFirebase(Context context) {
        this.context = context;
        privateKeys.add("LoggedInAsSupport");
        privateKeys.add("GCMToken");
        privateKeys.add("IMEI");
        db_helper = new CBO_DB_Helper(context);
        syncDB = new SyncDB();
        othExpenseDB = new OthExpenseDB(context);
        expHeadDB = new ExpHeadDB(context);



    }

    public void upload(){

        CBOServices.checkConnection(context, new Response() {
            @Override
            public void onSuccess(Bundle bundle) {
                cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nUploading data for Support ");
                cboProgressDialog.show();
                if (MyCustumApplication.getInstance().getUser().getLoggedInAsSupport()){
                    AppAlert.getInstance().getAlert(context,"Logged-In as Support!!!","You are not allowed to upload data to server!!!");
                }else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            uploadfromOutside();
                            cboProgressDialog.dismiss();

                        }
                    }, 3000);

                }

            }

            @Override
            public void onError(String s, String description) {
                AppAlert.getInstance().getAlert(context,s,description);

            }
        });

    }

    public void uploadfromOutside(){
            syncDB.delete(null);
            syncDB.insert(getAllPreferences(), "Preferences");
            syncDB.insert(getAllTableData(), "DB_OLD");
            syncDB.insert(getAllTableData(expHeadDB.getDatabase()), "DB_NEW");
    }

    public Map<String, ?> getAllPreferences(){
        Map<String, ?> allEntries = context.getSharedPreferences(FMCG_PREFRENCE, Context.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
        }
       /* Map<String, Map<String, ?>> pref = new HashMap<>();
        pref.put("Firebase", allEntries);*/
        return allEntries;
    }

    public Map<String, ?> getAllTableData(){
        return getAllTableData(db_helper.getWritableDatabase());
    }


    public Map<String, ?> getAllTableData(SQLiteDatabase db ){
        String qry = "SELECT name FROM sqlite_master WHERE type='table' and name not in ('android_metadata', 'cbo_login')";
        //SQLiteDatabase db = new ExpHeadDB(context).getDatabase();
        //Map<String, Map<String, ?>> Database = new HashMap<>();
        Map<String, Map<String, ?>> Table = new HashMap<>();
        //Database.put("DB_OLD", Table);
        Cursor c = null;
        Cursor cTable = null;
        String tableName;
        try {
            c = db.rawQuery(qry, null);
            if (c.moveToFirst()) {
                do {
                    tableName = c.getString(c.getColumnIndex("name"));
                    Map<String, Map<String, ?>> TableData = new HashMap<>();
                    Table.put(tableName, TableData);
                    Log.d("map values", " Table Start : " + tableName);


                    int rowCount = 0;
                    cTable = db.rawQuery("Select * from " + tableName , null);
                    if (cTable.moveToFirst()) {
                        do {

                            Map<String, String> row = new HashMap<>();
                            Log.d("map values", " -------------------------------------------------------------------");
                            for (int i =0 ; i < cTable.getColumnCount(); i++) {
                                row.put(cTable.getColumnName(i), cTable.getString(i));
                                Log.d("map values", cTable.getColumnName(i) + ": " + cTable.getString(i));
                            }
                            Log.d("map values", " -------------------------------------------------------------------");
                            TableData.put(""+rowCount, row);
                            rowCount++;
                        } while (cTable.moveToNext());
                    }


                    Log.d("map values", " Table End : " + tableName);
                } while (c.moveToNext());
            }
        }finally {
            if (cTable != null) {
                cTable.close();
            }
            if (c != null) {
                c.close();
            }
            db.close();
        }

        return Table;
    }

    public void download(){
        Log.d("map values", "Download started......");
        cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nDownloading data for Support ");
        cboProgressDialog.show();
        syncDB.getDbRef(new FirebsaeDB.ILogin() {
            @Override
            public void onSuccess(DatabaseReference DBRef) {
                Log.d("map values", "Login success...... at ref\n" + DBRef.toString());
                DBRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("map values", dataSnapshot.toString());
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Log.d("map values", "getting datas......");
                            switch (postSnapshot.getKey()){
                                case "Preferences" :
                                    cboProgressDialog.setDescription("Please Wait..\nDownloading data for Support \nDownloading User Settings...");
                                    for (DataSnapshot preferences : postSnapshot.getChildren()) {
                                        if (!privateKeys.contains( preferences.getKey())) {
                                            Log.d("map values", preferences.getKey() + ": " + preferences.getValue());
                                            MyCustumApplication.getInstance().setDataInTo_FMCG_PREFRENCE(preferences.getKey(), preferences.getValue().toString());
                                        }
                                    }
                                    break;
                                case "DB_OLD":
                                    downloaddataToDB(db_helper.getWritableDatabase(),postSnapshot);
                                    break;
                                case "DB_NEW":
                                    downloaddataToDB(othExpenseDB.getDatabase(),postSnapshot);
                            }


                        }
                        cboProgressDialog.setDescription("Please Wait..\nDownloading completed !!!!");
                        DBRef.removeEventListener(this);
                        cboProgressDialog.dismiss();
                        MyCustumApplication.getInstance().Logout((AppCompatActivity) context);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        DBRef.removeEventListener(this);
                        System.out.println(databaseError.getMessage());
                        Log.d("map values", "error :" + databaseError.getMessage());
                        cboProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError(String title, String description) {
                Log.d("map values", "Login error......\n"+description);
            }
        });
    }


    private void downloaddataToDB(SQLiteDatabase db,DataSnapshot postSnapshot){
        try {
            //db = db_helper.getWritableDatabase();
            for (DataSnapshot table : postSnapshot.getChildren()) {

               if( checkForTableExists(db,table.getKey())){
                    String qry = "DELETE FROM " + table.getKey();
                    db.execSQL(qry);

                    Log.d("map values", " Table Start : " + table.getKey());
                    cboProgressDialog.setDescription("Please Wait..\nDownloading data for Support \nDownloading User Data...\n" + table.getKey());


                    for (DataSnapshot row : table.getChildren()) {
                        ContentValues cv = new ContentValues();
                        Log.d("map values", " -------------------------------------------------------------------");
                        //if (db.execSQL("TABLE IF EXISTS  location");)
                        for (DataSnapshot column : row.getChildren()) {
                            Log.d("map values", column.getKey() + ": " + column.getValue());
                            cv.put(column.getKey(), column.getValue().toString());
                        }
                        Log.d("map values", " -------------------------------------------------------------------");
                        db.insert(table.getKey(), null, cv);
                    }

                    Log.d("map values", " Table Ends : " + table.getKey());
                }
            }
        }finally {
            assert db != null;
            db.close();
        }
    }

    private boolean checkForTableExists(SQLiteDatabase db, String table){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }
}
