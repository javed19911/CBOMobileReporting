package com.cbo.cbomobilereporting.ui_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.cbo.cbomobilereporting.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import android.net.Uri;

import CameraGalaryPkg.FileUtil;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.CAMERA;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class MyCamera extends AppCompatActivity implements SurfaceHolder.Callback, Camera.PictureCallback {

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
        setContentView(R.layout.activity_my_camera);

        surfaceView = findViewById(R.id.surfaceView);
        captureBtn = findViewById(R.id.capture);
        ResetBtn = findViewById(R.id.reset);
        DoneBtn = findViewById(R.id.done);

        intent = getIntent();

        DoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSendResponse();
            }
        });


        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCamera();
            }
        });

        if (surfaceView != null) {
            boolean result = checkPermission();
            if (result) {
                setupSurfaceHolder();
            }
        }

    }

    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
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
        ActivityCompat.requestPermissions(MyCamera.this, permissions, REQUEST_CODE);
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
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
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
            camera.takePicture(null, null, this);

            ResetBtn.setVisibility(View.VISIBLE);
            DoneBtn.setVisibility(View.VISIBLE);
            captureBtn.setVisibility(View.INVISIBLE);

        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startCamera();
    }

    private void startCamera() {
        ResetBtn.setVisibility(View.INVISIBLE);
        DoneBtn.setVisibility(View.INVISIBLE);
        captureBtn.setVisibility(View.VISIBLE);
        int cameraId = findFrontFacingCamera();
        if (cameraId == -1) {
            camera = Camera.open();
            CameraEyeValue = setPhotoOrientation(this, 0);
        }else{
            camera = Camera.open(cameraId);
            CameraEyeValue = setPhotoOrientation(this, cameraId);
        }
        camera.setDisplayOrientation(CameraEyeValue);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private int findFrontFacingCamera() {

        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                Log.d("AttachImage", "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }


    public int setPhotoOrientation(Activity activity, int cameraId) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        return result;
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        ResetBtn.setVisibility(View.INVISIBLE);
        DoneBtn.setVisibility(View.INVISIBLE);
        captureBtn.setVisibility(View.VISIBLE);

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        // Picture had been taken by camera. So, do appropriate action. For example, save it in file.

        try {
            Uri fileUri = (Uri) intent.getParcelableExtra("output");
            //InputStream inputStream = getContentResolver().openInputStream(fileUri);
            FileOutputStream fos = new FileOutputStream(FileUtil.getRealPathFromURI(this, fileUri));
            Bitmap bm=null;

            // COnverting ByteArray to Bitmap - >Rotate and Convert back to Data
            if (data != null) {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                int screenHeight = getResources().getDisplayMetrics().heightPixels;
                bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);

                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // Notice that width and height are reversed
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
                    int w = scaled.getWidth();
                    int h = scaled.getHeight();
                    // Setting post rotate to 90
                    Matrix mtx = new Matrix();

                    //int CameraEyeValue = setPhotoOrientation(AndroidCameraExample.this, cameraFront==true ? 1:0); // CameraID = 1 : front 0:back
                    if(cameraId != -1) { // As Front camera is Mirrored so Fliping the Orientation
                        if (CameraEyeValue == 270) {
                            mtx.postRotate(90);
                        } else if (CameraEyeValue == 90) {
                            mtx.postRotate(270);
                        }
                    }else{
                        mtx.postRotate(CameraEyeValue); // CameraEyeValue is default to Display Rotation
                    }

                    bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
                }else{// LANDSCAPE MODE
                    //No need to reverse width and height
                    Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
                    bm=scaled;
                }
            }
            // COnverting the Die photo to Bitmap



            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            fos.write(byteArray);
            fos.close();


        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        //write your code here to save bitmap

        /*try {
             Uri fileUri = (Uri) intent.getParcelableExtra("output");
            //InputStream inputStream = getContentResolver().openInputStream(fileUri);
            FileOutputStream fo = new FileOutputStream(FileUtil.getRealPathFromURI(this, fileUri));
            fo.write(data);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void onSendResponse() {
        Intent intent = new Intent();
//        intent.putExtra("customer", customer);
//        intent.putExtra("order", order);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
