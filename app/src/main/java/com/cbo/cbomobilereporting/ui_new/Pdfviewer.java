package com.cbo.cbomobilereporting.ui_new;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cbo.cbomobilereporting.R;
import com.github.barteksc.pdfviewer.PDFView;

public class Pdfviewer  extends AppCompatActivity {

    PDFView pdfView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfview);

        pdfView=findViewById(R.id.pdfView);







    }
}



