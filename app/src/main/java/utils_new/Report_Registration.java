package utils_new;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.DistanceCalculator;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.DrCall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import locationpkg.Const;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by pc24 on 29/11/2017.
 */

public class Report_Registration extends DialogFragment{

    public static final int REQUEST_CAMERA=201;
    private File output=null;
    public String filename="",Alertmassege,title;

    int PA_ID;
    Context context;
    Custom_Variables_And_Method customVariablesAndMethod;
    String loc1,loc2,loc3;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        context = getActivity();
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        return ReturnDialog();
    }

    public void setAlertData(String title,  String message){
        this.title=title;
        Alertmassege=message;
    }

    public void setAlertLocation(String loc1,  String loc2,String loc3){
        this.loc1=loc1;
        this.loc3=loc3;
        this.loc2=loc2;
    }

    private AlertDialog ReturnDialog(){


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogLayout = inflater.inflate(R.layout.alert_report_view, null);
        final TextView Alert_title= (TextView) dialogLayout.findViewById(R.id.title);

        final TextView Alert_message= (TextView) dialogLayout.findViewById(R.id.message);
        final TableLayout Alert_message_list= (TableLayout) dialogLayout.findViewById(R.id.table_view);
        final Button Alert_Positive= (Button) dialogLayout.findViewById(R.id.positive);
        Alert_title.setText(title);

        final TextView pa_id_txt= (TextView) dialogLayout.findViewById(R.id.PA_ID);
        pa_id_txt.setText(""+Custom_Variables_And_Method.PA_ID);

        final Button report= (Button) dialogLayout.findViewById(R.id.report);
        //if (reportVisible) {
            //report.setVisibility(View.VISIBLE);
        //}

        Alert_message.setText(Alertmassege);
        Alert_message_list.setVisibility(View.GONE);


        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);


        final AlertDialog dialog = builder1.create();

        dialog.setView(dialogLayout);


        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!customVariablesAndMethod.checkIfCallLocationValid(context,true,false)) {
                    customVariablesAndMethod.msgBox(context,"Verifing Your Location");
                    LocalBroadcastManager.getInstance(context).registerReceiver(mLocationUpdated,
                            new IntentFilter(Const.INTENT_FILTER_LOCATION_UPDATE_AVAILABLE));
                }else if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    captureImage();
                }
                dialog.dismiss();
            }
        });

        Alert_Positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        return dialog;
    }

    private BroadcastReceiver mLocationUpdated = new BroadcastReceiver() {
        @Override
        public void onReceive(Context contex, Intent intent) {
            Location location = intent.getParcelableExtra(Const.LBM_EVENT_LOCATION_UPDATE);

            LocalBroadcastManager.getInstance(context).unregisterReceiver(mLocationUpdated);
            if(getDistancefrom()) {
                Toast.makeText(context, "Location Updated....\nPlease Try Again...", Toast.LENGTH_LONG).show();
            }else if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //takePictureButton.setEnabled(false);
                ActivityCompat.requestPermissions((Activity) context, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA);
                Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

            }else {

                captureImage();
            }

        }
    };


    private Boolean getDistancefrom(){
        Double km1,km2=-1.0,km3=-1.0;
        km1= DistanceCalculator.distance(Double.valueOf(loc1.split(",")[0]), Double.valueOf(loc1.split(",")[1])
                ,  Double.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON).split(",")[0]), Double.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON).split(",")[1]), "K");

        if (!loc2.equals("")) {
            km2 = DistanceCalculator.distance(Double.valueOf(loc2.split(",")[0]), Double.valueOf(loc2.split(",")[1])
                    , Double.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON).split(",")[0]), Double.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON).split(",")[1]), "K");
        }else{
            km2=km1;
        }

        if (!loc3.equals("")) {
            km3 = DistanceCalculator.distance(Double.valueOf(loc3.split(",")[0]), Double.valueOf(loc3.split(",")[1])
                    , Double.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON).split(",")[0]), Double.valueOf(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context, "shareLatLong", Custom_Variables_And_Method.GLOBAL_LATLON).split(",")[1]), "K");
        }else{
            km3=km1;
        }

        String geo_fancing_km=customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(context,"GEO_FANCING_KM","0");

        Double km=getShortestDistance(km1,km2,km3);

        if (km>Double.valueOf(geo_fancing_km)){
            return false;
        }else{
            return true;
        }
    }


    private Double getShortestDistance(Double km1,Double km2,Double km3){
        if ((km2==-1.0 && km3==-1.0) || ( km3==-1.0 && km1<km2 ) || ( km2==-1.0 && km1<km3 ) || (km1<km2 && km1<km3) )
            return km1;
        if ((km3==-1.0) || (km2<km3 && km2!=-1.0))
            return km2;
        return km3;
    }


    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {

            File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
                    //return true;
                }
            }
            filename = PA_ID+"_"+customVariablesAndMethod.DCR_ID+"_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
            output = new File(dir, filename);



            ContentValues values = new ContentValues(1);

            values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
            Uri fileUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            ((Activity) context).startActivityForResult(intent, REQUEST_CAMERA);

        } else {
            Toast.makeText(context, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                captureImage();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }

    }

    public String compressImage(File imageUri) {

        //String filePath = getRealPathFromURI(imageUri);
        String filePath=imageUri.getPath();
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        if (options.outWidth != -1 && options.outHeight != -1) {
            // This is an image file.

            int actualHeight = options.outHeight;
            int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

            float maxHeight = 816.0f;
            float maxWidth = 612.0f;
            float imgRatio = actualWidth / actualHeight;
            float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

            if (actualHeight > maxHeight || actualWidth > maxWidth) {
                if (imgRatio < maxRatio) {
                    imgRatio = maxHeight / actualHeight;
                    actualWidth = (int) (imgRatio * actualWidth);
                    actualHeight = (int) maxHeight;
                } else if (imgRatio > maxRatio) {
                    imgRatio = maxWidth / actualWidth;
                    actualHeight = (int) (imgRatio * actualHeight);
                    actualWidth = (int) maxWidth;
                } else {
                    actualHeight = (int) maxHeight;
                    actualWidth = (int) maxWidth;

                }
            }

//      setting inSampleSize value allows to load a scaled down version of the original image

            options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
            options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inTempStorage = new byte[16 * 1024];

            try {
//          load the bitmap from its path
                bmp = BitmapFactory.decodeFile(filePath, options);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();

            }
            try {
                scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError exception) {
                exception.printStackTrace();
            }

            float ratioX = actualWidth / (float) options.outWidth;
            float ratioY = actualHeight / (float) options.outHeight;
            float middleX = actualWidth / 2.0f;
            float middleY = actualHeight / 2.0f;

            Matrix scaleMatrix = new Matrix();
            scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

            assert scaledBitmap != null;
            Canvas canvas = new Canvas(scaledBitmap);
            canvas.setMatrix(scaleMatrix);
            canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
            ExifInterface exif;
            try {
                exif = new ExifInterface(filePath);

                int orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, 0);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                    Log.d("EXIF", "Exif: " + orientation);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                    Log.d("EXIF", "Exif: " + orientation);
                }
                scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                        scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                        true);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FileOutputStream out = null;
            //String filename = getFilename();
            try {
                out = new FileOutputStream(filePath);

//          write the compressed bitmap at the destination specified by filename.
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else {
            // This is not an image file.
        }


        return filePath;

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height/ (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}
