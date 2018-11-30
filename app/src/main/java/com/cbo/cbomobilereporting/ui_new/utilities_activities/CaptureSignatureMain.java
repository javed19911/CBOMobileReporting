package com.cbo.cbomobilereporting.ui_new.utilities_activities;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui.CaptureSignature;

public class CaptureSignatureMain extends AppCompatActivity {
    Button b1;
    ImageView signImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_signature);

        Toolbar toolbar =(Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView =(TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        textView.setText("Capture Signature");
        if (getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }



        b1 = (Button) findViewById(R.id.getSign);
        signImage = (ImageView) findViewById(R.id.imageView1);
        b1.setOnClickListener(onButtonClick);
    }

    Button.OnClickListener onButtonClick = new Button.OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent i = new Intent(CaptureSignatureMain.this, CaptureSignature.class);
            startActivityForResult(i, 0);
            finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == 1) {
            Bitmap b = BitmapFactory.decodeByteArray(
                    data.getByteArrayExtra("byteArray"), 0,
                    data.getByteArrayExtra("byteArray").length);
                   signImage.setImageBitmap(b);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
