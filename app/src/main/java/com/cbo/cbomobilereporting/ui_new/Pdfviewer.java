package com.cbo.cbomobilereporting.ui_new;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cbo.cbomobilereporting.R;
import com.github.barteksc.pdfviewer.PDFView;

public class Pdfviewer  extends AppCompatActivity {

    PDFView pdfView;

    public static final String SAMPLE_FILE_For_ALL = "Hep&Support.pdf";
    public static final String SAMPLE_FILE_For_Xiaomi = "Help&Supportxiomi.pdf";
    public static final String SAMPLE_FILE_For_VIVO = "Help&Supportvivo.pdf";
    public static final String SAMPLE_FILE_For_OPPO = "Help&Supportoppo.pdf";

    String deviceName = android.os.Build.MODEL; // returns model name

    String deviceManufacturer = android.os.Build.MANUFACTURER;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            //textView.setText("Dr Prescription");

            textView.setText("Help & Support");

            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }
        pdfView=findViewById(R.id.pdfView);


     //   checkDevice();
        if(getDeviceName().contains("Xiaomi")){
            loadpdf(SAMPLE_FILE_For_Xiaomi);
        }else if(getDeviceName().contains("OPPO")){
            loadpdf(SAMPLE_FILE_For_OPPO);

        }else if(getDeviceName().contains("VIVO")){
            loadpdf(SAMPLE_FILE_For_VIVO);

        }else
        {
         loadpdf(SAMPLE_FILE_For_ALL);
        }

    }
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer)+model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pdfview, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            default:
                finish();
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadpdf(String locsl_file) {
        pdfView.fromAsset(locsl_file)

                .enableSwipe(true) // allows to block changing pages using swipe
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .defaultPage(0)

                .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                .password(null)
                .scrollHandle(null)
                .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                // spacing between pages in dp. To define spacing color, set view background
                .spacing(0)
                .load();
    }
}



