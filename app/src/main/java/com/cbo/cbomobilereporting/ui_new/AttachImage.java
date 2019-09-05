package com.cbo.cbomobilereporting.ui_new;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import CameraGalaryPkg.ChoosePhoto;
import CameraGalaryPkg.FileUtil;
import CameraGalaryPkg.PermissionUtil;
import utils_new.AppAlert;

public class AttachImage extends CustomActivity {


    public enum ChooseFrom{
        camera,
        galary,
        all;
    }


    public static final int CHOOSE_PHOTO_INTENT = 101;
    public static final int SELECTED_IMG_CROP = 102;
    public static final int SELECT_PICTURE_CAMERA = 103;
    public static final int currentAndroidDeviceVersion = Build.VERSION.SDK_INT;

    private int ASPECT_X = 1;
    private int ASPECT_Y = 1;
    private int OUT_PUT_X = 300;
    private int OUT_PUT_Y = 300;
    private boolean SCALE = false;

    private Uri cropPictureUrl, selectedImageUri = null, cameraUrl = null;
    private File output;

    ImageView attachImg;
    ChooseFrom choosefrom = ChooseFrom.all;
    String filename="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_image);

        attachImg = findViewById(R.id.attachImg);

        PermissionUtil permissionUtil = new PermissionUtil();

        if (getIntent().getStringExtra("Output_FileName") == null) {
            filename = "CBO_" + customVariablesAndMethod.get_currentTimeStamp() + ".jpg";
        }else{
            filename = getIntent().getStringExtra("Output_FileName");
        }

        if (getIntent().getSerializableExtra("SelectFrom") == null) {
            choosefrom = ChooseFrom.all;
        }else{
            choosefrom = (ChooseFrom) getIntent().getSerializableExtra("SelectFrom");
        }

        if (permissionUtil.checkMarshMellowPermission()) {
            if (permissionUtil.verifyPermissions(context, permissionUtil.getCameraPermissions()) && permissionUtil.verifyPermissions(context, permissionUtil.getGalleryPermissions()))
                showAlertDialog();
            else {
                ActivityCompat.requestPermissions((Activity) context, permissionUtil.getCameraPermissions(), SELECT_PICTURE_CAMERA);
            }
        } else {
            showAlertDialog();
        }
    }


    public void showAlertDialog() {
        final Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }


        output = new File(dir, filename);

        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ContentValues values = new ContentValues(1);
        values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
        cameraUrl = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        //getContentResolver().delete()
        if (cameraUrl == null){
            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,MediaStore.Images.ImageColumns.DATA +"='"+output.getPath()+"'",null);
            cameraUrl = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUrl);
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);



        //Add them to an intent array
        Intent[] intents = new Intent[]{cameraIntent};

        final Intent chooserIntent;
        if (choosefrom == ChooseFrom.camera){
            chooserIntent = Intent.createChooser(cameraIntent, context.getString(R.string.choose_photo_title));
        }else if (choosefrom == ChooseFrom.galary){
            chooserIntent = Intent.createChooser(galleryIntent, context.getString(R.string.choose_photo_title));
        }else{
            //Create a choose from your first intent then pass in the intent array
            chooserIntent = Intent.createChooser(galleryIntent, context.getString(R.string.choose_photo_title));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        }




        startActivityForResult(chooserIntent, CHOOSE_PHOTO_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ChoosePhoto.CHOOSE_PHOTO_INTENT :
                    File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
                    if (!dir.exists()) {
                        if (!dir.mkdirs()) {
                            Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                            //return true;
                        }
                    }
                      if (data != null && data.getData() != null) {
                        handleGalleryResult(data);
                    } else {
                        handleCameraResult(cameraUrl);
                    }

                    break;
                case SELECTED_IMG_CROP :
                    previewCapturedImage();
                    break;

                default:

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.SELECT_PICTURE_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                showAlertDialog();
            }
        }

    }

    private void previewCapturedImage() {
       /* try {
            // hide video preview
            Glide.with(this).load( output.getPath()).into( attachImg);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        onSendResponse();
    }



    public void onSendResponse() {
        Intent intent = new Intent();
        intent.putExtra("Output",output);
        intent.putExtra("Output_FileName",filename);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
    // Change this method(edited)
    public void handleGalleryResult(Intent data) {
        try {

            //cropPictureUrl = Uri.fromFile(File.createTempFile(file,".jpg",new File(Environment.getExternalStorageDirectory(), "CBO")));

            cropPictureUrl = Uri.fromFile(output);
            String realPathFromURI = FileUtil.getRealPathFromURI(context, data.getData());
            assert (realPathFromURI == null ? getImageUrlWithAuthority(context, data.getData()) : realPathFromURI) != null;
            File file1 = new File(realPathFromURI == null ? getImageUrlWithAuthority(context, data.getData()) : realPathFromURI);
            if (file1.exists()) {
                if (currentAndroidDeviceVersion > 23) {
                    cropImage(FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file1), cropPictureUrl);

                } else {
                    cropImage(Uri.fromFile(file1), cropPictureUrl);
                }

            } else {
                cropImage(data.getData(), cropPictureUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp).toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    public void handleCameraResult(Uri cameraPictureUrl) {
        try {

            cropImage(cameraPictureUrl, Uri.fromFile(output));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    private void cropImage(final Uri sourceImage, Uri destinationImage) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            intent.setType("image/*");

            List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
            int size = list.size();
            if (size == 0) {
                //Utils.showToast(mContext, mContext.getString(R.string.error_cant_select_cropping_app));
                selectedImageUri = sourceImage;
                intent.putExtra(MediaStore.EXTRA_OUTPUT, sourceImage);
                ((Activity) context).startActivityForResult(intent, SELECTED_IMG_CROP);
                return;
            } else {
                intent.setDataAndType(sourceImage, "image/*");
               /* intent.putExtra("aspectX", ASPECT_X);
                intent.putExtra("aspectY", ASPECT_Y);*/
                intent.putExtra("outputY", OUT_PUT_Y);
                intent.putExtra("outputX", OUT_PUT_X);
                intent.putExtra("scale", SCALE);

                //intent.putExtra("return-data", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, destinationImage);
                selectedImageUri = destinationImage;
                if (size == 1) {
                    Intent i = new Intent(intent);
                    ResolveInfo res = list.get(0);
                    i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
                    ((Activity) context).startActivityForResult(intent, SELECTED_IMG_CROP);
                } else {
                    Intent i = new Intent(intent);
                    i.putExtra(Intent.EXTRA_INITIAL_INTENTS, list.toArray(new Parcelable[list.size()]));
                    ((Activity) context).startActivityForResult(intent, SELECTED_IMG_CROP);
                }
            }
        }catch (Exception e){
            AppAlert.getInstance().getAlert(context,"Exception",e.getMessage());
        }
    }
}
