package com.cbo.cbomobilereporting.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import pl.polidea.view.ZoomView;
import services.ServiceHandler;
import utils.MyConnection;

/**
 * Created by RAM on 9/12/15.
 */
public class SpoStock_Report extends AppCompatActivity {

    ServiceHandler myService;
    CBO_DB_Helper myDb;
    MyConnection myCon;
    Context context;
    ZoomView myZoomView;
    Intent myIntent;
    String getIntentExtra;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
         setContentView(R.layout.zoomer_layout);

        Toolbar toolbar =(Toolbar)  findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        textView.setText("Stock Report");
if (getSupportActionBar() != null){

    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

}


        context = SpoStock_Report.this;
        myDb = new CBO_DB_Helper(context);
        myCon = new MyConnection(context);
        myService = new ServiceHandler(context);
        myIntent = getIntent();
        getIntentExtra = myIntent.getStringExtra("");
   }

    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item !=null){
            finish();
        }

        return super.onOptionsItemSelected(item);

    }
}
