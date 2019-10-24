package com.cbo.cbomobilereporting.ui_new;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cbo.cbomobilereporting.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import CameraGalaryPkg.FileUtil;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class BarcodeReaderActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 100;

    private String[] neededPermissions = new String[]{CAMERA, WRITE_EXTERNAL_STORAGE};

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    int cameraId = -1;
    int CameraEyeValue =0;
    Intent intent;

    private SurfaceView surfaceView;
    private Button captureBtn,ResetBtn,DoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanner);

        surfaceView = findViewById(R.id.surfaceView);
        captureBtn = findViewById(R.id.capture);
        ResetBtn = findViewById(R.id.reset);
        DoneBtn = findViewById(R.id.done);

        intent = getIntent();

        /*DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendResponse();
            }
        });*/


       /* ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCamera();
            }
        });*/

        if (surfaceView != null) {
            boolean result = checkPermission();
            if (result) {
                setupSurfaceHolder();
            }
        }

    }

    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            ArrayList<String> permissionsNotGranted = new ArrayList<>();
            for (String permission : neededPermissions) {
                if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                    permissionsNotGranted.add(permission);
                }
            }
            if (permissionsNotGranted.size() > 0) {
                boolean shouldShowAlert = false;
                for (String permission : permissionsNotGranted) {
                    shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
                }
                if (shouldShowAlert) {
                    showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                } else {
                    requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
                }
                return false;
            }
        }
        return true;
    }

    private void showPermissionAlert(final String[] permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.permission_message);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(BarcodeReaderActivity.this, permissions, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        // Not all permissions granted. Show message to the user.
                        return;
                    }
                }

                // All permissions are granted. So, do the appropriate work now.
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void setupSurfaceHolder() {
        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(this).build();
        final CameraSource cameraSource = new CameraSource.Builder(this,barcodeDetector)
                .setAutoFocusEnabled(true)
                .setRequestedPreviewSize(1600,1024)
                .build();

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(surfaceHolder);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size()>0){
                    onSendResponse(barcodes.valueAt(0));
                }
            }
        });
        setBtnClick();
    }

    private void setBtnClick() {

        if (captureBtn != null) {
            captureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    captureImage();
                }
            });
        }
    }

    public void captureImage() {
        if (camera != null) {
            //camera.takePicture(null, null, this);

            ResetBtn.setVisibility(View.VISIBLE);
            DoneBtn.setVisibility(View.VISIBLE);
            captureBtn.setVisibility(View.INVISIBLE);

        }
    }



    public void onSendResponse(Barcode barcode) {
        Intent intent = new Intent();
        intent.putExtra("barcode", barcode);
        setResult(RESULT_OK, intent);
        finish();
        //Toast.makeText(BarcodeReaderActivity.this,"Barcode Value : "+ barcode.displayValue,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
