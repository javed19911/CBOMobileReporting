package com.cbo.cbomobilereporting.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

/**
 * Created by Akshit on 4/1/2016.
 */
public class Contact_Us extends AppCompatActivity {



    TextView contact_us_data ;


    private final String contact_us_text ="<body> <h3> CBO Infotech </h3>\n <b>Address:</b>  <p>B-141, Rama Park \n" +
            "Opposite Metro Pillar No.-764, Near Dwarka Mor Metro Station, Uttam Nagar, New Delhi-110059 </p> <b> \n" +
            " \n" +

            " \n <b>Contact Detail : <p>\n</b>\nFor Support    :</b> 9891947306, 9212749563 \n" +
            "<b>\n For Sales        : </b>9891886164  <br>     \n" +
            "<b>\nEmail : </b>paras@cboinfotech.com \n" +
            " \t\t        support@cboinfotech.com</p></body>";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contact_us);
        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hadderText =(TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        contact_us_data =(TextView) findViewById(R.id.contact_us_details);

        if (getSupportActionBar() != null){
            hadderText.setText("Contact Us");

            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
     }

        contact_us_data.setText(Html.fromHtml(contact_us_text));





    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){

            finish();
        }




        return super.onOptionsItemSelected(item);
    }
}
