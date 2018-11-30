package com.cbo.cbomobilereporting.ui_new.transaction_activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.root.ExpenseRoot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import services.ServiceHandler;
import utils.ExceptionHandler;
import utils.MyConnection;
import utils.adapterutils.SpinAdapter;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;
import utils_new.GalleryUtil;
import utils_new.up_down_ftp;

/**
 * Created by Akshit Udainiya on 6/9/15.
 */
public class Complaint extends AppCompatActivity implements up_down_ftp.AdapterCallback{
    Button btSubmit, btBack, btDropdowon;
    EditText compNo, editDate, compText;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    ArrayList<SpinnerModel> RetailerList;
    Date date;
    CBO_DB_Helper myCbohelp;
    SpinnerModel[] titleName;
    ArrayList<SpinnerModel> arrayShort;
    private AlertDialog myalertDialog = null;
    String retailerId;
    String retailerName;
    ServiceHandler myServiceHandler;
    String dateDD, dateMM, cNo;
    ImageView imgDrop,attachnew;
    TextView attach_txt;


    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    String picturePath="";
    private File output=null;
    String filename="";

    String dcr_id = "";
    int PA_ID;

    ProgressDialog progress1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(Complaint.this));
        setContentView(R.layout.complaint_layout);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);
        setSupportActionBar(toolbar);
        textView.setText("Complaint");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        context = Complaint.this;
        myCbohelp = new CBO_DB_Helper(context);
        myServiceHandler = new ServiceHandler(context);

        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();

        btSubmit = (Button) findViewById(R.id.bt_complaion_save);
        btBack = (Button) findViewById(R.id.bt_complaion_back);
        btDropdowon = (Button) findViewById(R.id.bt_complaion_dropdown);
        compNo = (EditText) findViewById(R.id.edt_complain_cno);
        editDate = (EditText) findViewById(R.id.edt_complain_date);
        compText = (EditText) findViewById(R.id.edit_complaion_text);
        imgDrop = (ImageView) findViewById(R.id.spinner_img);
        attachnew = (ImageView) findViewById(R.id.attachnew);
        attach_txt = findViewById(R.id.attach_txt);

        dcr_id = Custom_Variables_And_Method.DCR_ID;
        PA_ID = Custom_Variables_And_Method.PA_ID;

        //  date = new Date();
        //convertedDate = mycon.convetDateddMMyyyy(date);
        // editDate.setText(convertedDate);
        btDropdowon.setText("--Select--");
        imgDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDropDown();
            }
        });
        btDropdowon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickDropDown();
            }

        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        attachnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                //capture_Image();
                PopupMenu menu = new PopupMenu(v.getContext(), v);
                menu.getMenu().add("Camera");
                menu.getMenu().add("Gallery");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getTitle().equals("Camera")){
                            if (ContextCompat.checkSelfPermission(Complaint.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(Complaint.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //takePictureButton.setEnabled(false);
                                ActivityCompat.requestPermissions(Complaint.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, Complaint.this.REQUEST_CAMERA);
                                Toast.makeText(Complaint.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                            }else {

                                capture_Image();
                            }
                        }else{
                            if (ContextCompat.checkSelfPermission(Complaint.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                //takePictureButton.setEnabled(false);
                                ActivityCompat.requestPermissions(Complaint.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, Complaint.this.GALLERY_ACTIVITY_CODE);
                                Toast.makeText(Complaint.this, "Please allow the permission", Toast.LENGTH_LONG).show();

                            }else {
                                open_galary();

                            }
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });

        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btDropdowon.getText().toString().toLowerCase().equals("--select--")) {
                    customVariablesAndMethod.msgBox(context,"Select Retailer Name First..");

                }else if (!attach_txt.getText().toString().equals("* Attach Picture....")){


                    progress1 = new ProgressDialog(context);
                    progress1.setMessage("Please Wait..\nuploading Image");
                    progress1.setCancelable(false);
                    progress1.show();
                    filename = attach_txt.getText().toString();;
                    File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator +  filename);
                    new up_down_ftp().uploadFile(file2,Complaint.this);


                }else {
                    new ComplaintCommit().execute();
                }


            }
        });

        new RetailerData().execute();


    }

    private void open_galary(){
        Intent gallery_Intent = new Intent(Complaint.this, GalleryUtil.class);
        startActivityForResult(gallery_Intent, GALLERY_ACTIVITY_CODE);
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {

            File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                    //return true;
                }
            }
            filename = PA_ID+"_"+dcr_id+"_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
            output = new File(dir, filename);


//            fileTemp = ImageUtils.getOutputMediaFile();
            ContentValues values = new ContentValues(1);
            //values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
            values.put( MediaStore.Images.ImageColumns.DATA, output.getPath() );
            Uri fileUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//            if (fileTemp != null) {
            // fileUri = Uri.fromFile(output);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_CAMERA);
//            } else {
//                Toast.makeText(this, getString(R.string.error_create_image_file), Toast.LENGTH_LONG).show();
//            }
        } else {
            Toast.makeText(this, getString(R.string.error_no_camera), Toast.LENGTH_LONG).show();
        }
    }

    private void capture_Image(){
        captureImage();
     /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                //return true;
            }
        }
         filename = PA_ID+"_"+dcr_id+"_"+exp_id+".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                filename = PA_ID+"_"+dcr_id+"_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
                Toast.makeText(this, picturePath, Toast.LENGTH_LONG).show();
                /*performCrop(picturePath);*/
                moveFile(picturePath);
            }
        }

        if (requestCode == RESULT_CROP ) {
            if(resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap selectedBitmap = extras.getParcelable("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                assert selectedBitmap != null;
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File dir = new File(Environment.getExternalStorageDirectory(), "CBO");
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
                        //return true;
                    } else {
                        try {
                            File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                            file2.createNewFile();
                            FileOutputStream fo = new FileOutputStream(file2);
                            fo.write(bytes.toByteArray());
                            fo.close();
                            Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                            //attach_img.setImageBitmap(myBitmap);

                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }else {
                    try {
                        File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);

                        file2.createNewFile();
                        FileOutputStream fo = new FileOutputStream(file2);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Bitmap myBitmap = BitmapFactory.decodeFile(file2.getAbsolutePath());
                        //attach_img.setImageBitmap(myBitmap);

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        if (requestCode == REQUEST_CAMERA ) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                /*Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");*/

                File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);
               /* FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file1);
                    Log.i("in save()", "after outputstream");
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
*/

                if (file1.exists()){
                    //Log.d("INSIDE :"," VALUE ===== "+exp_id+"   "+filename);
                    /*performCrop(file1.getPath());*/
                        attach_txt.setText(filename);

                }

            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "image capture cancelled ", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == this.REQUEST_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                capture_Image();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == this.GALLERY_ACTIVITY_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                open_galary();
                //Toast.makeText(this, "Permission granted", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void moveFile( String inputFileUri) {

        InputStream in = null;
        OutputStream out = null;
        try {
            File dir = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO");
            //create output directory if it doesn't exist
            if (!dir.exists()) {
                dir.mkdirs();
            }


            in = new FileInputStream(inputFileUri);
            out = new FileOutputStream(dir+File.separator + filename);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file
            out.flush();
            out.close();
            out = null;


            attach_txt.setText(filename);



        }

        catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        }
        catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            //attach_img.setVisibility(View.VISIBLE);
            //attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void upload_complete(final String IsCompleted) {
        progress1.dismiss();
        if (IsCompleted.equals("S")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                }
            });
        }else if (IsCompleted.equals("Y")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    new ComplaintCommit().execute();

                }
            });
        }else if (IsCompleted.contains("ERROR")) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    //new UploadPhotoInBackGround().execute();
                    String folder=IsCompleted.substring(6);
                    customVariablesAndMethod.getAlert(context,"Folder not found",folder+"   Invalid path \nPlease contact your administrator");
                }
            });
        }else {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {

                @Override
                public void run() {
                    customVariablesAndMethod.msgBox(context,"UPLOAD FAILED \n Please try again");
                }
            });
        }
    }*/

    @Override
    public void started(Integer responseCode, String message, String description) {

    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {
        progress1.dismiss();
        new ComplaintCommit().execute();
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        progress1.dismiss();
        customVariablesAndMethod.getAlert(context,message,description);
    }

   /* private void performCrop(String picUri) {
        try {
            //Start Crop Activity

            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            File f = new File(picUri);
            Uri contentUri = Uri.fromFile(f);

            cropIntent.setDataAndType(contentUri, "image*//*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 280);
            cropIntent.putExtra("outputY", 280);

            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, RESULT_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            // display an error message
            String errorMessage = "your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/

    class ComplaintCommit extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String id;

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if ((s != null) && (!s.toLowerCase().contains("error"))) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                    JSONObject object = jsonArray.getJSONObject(0);
                    id = object.getString("DCRID");
                    int id1 = Integer.parseInt(id);
                    if (id1 > 0) {
                        customVariablesAndMethod.msgBox(context,"Complaint Successfully Submitted..");
                        //Intent i = new Intent(context, TransActions.class);
                       // startActivity(i);
                        finish();
                        pd.dismiss();
                    }
                } catch (JSONException json) {
                    pd.dismiss();
                    customVariablesAndMethod.msgBox(context,"Exception Found...");
                }


            } else {
                pd.dismiss();
                customVariablesAndMethod.msgBox(context,"Nothing Found...");
            }
            pd.dismiss();
        }

        @Override
        protected String doInBackground(String... params) {
            String responseComplaintDoc;
            try{
                responseComplaintDoc = myServiceHandler.getResponse_COMPLAINT_COMMIT(myCbohelp.getCompanyCode(), "" + Custom_Variables_And_Method.PA_ID, "" + Custom_Variables_And_Method.CHEMIST_ID,
                        cNo, dateMM, "" + Custom_Variables_And_Method.CHEMIST_ID, compText.getText().toString(),filename);
            }catch (Exception e){
                return "Error apk "+e;
            }

            return responseComplaintDoc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing....");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }


    class RetailerData extends AsyncTask<ArrayList<SpinnerModel>, String, ArrayList<SpinnerModel>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setTitle("CBO");
            pd.setMessage("Processing...");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }

        @Override
        protected ArrayList<SpinnerModel> doInBackground(ArrayList<SpinnerModel>... params) {
            RetailerList = new ArrayList<SpinnerModel>();
            try {
                Cursor c = myCbohelp.getChemistListLocal();
                RetailerList.clear();
                if (c.moveToFirst()) {
                    do {

                        RetailerList.add(new SpinnerModel(c.getString(c.getColumnIndex("chem_name")), c.getString(c.getColumnIndex("chem_id"))));
                    } while (c.moveToNext());
                }

                String responseComplaintDoc = myServiceHandler.getResponse_Complaint_Docno(myCbohelp.getCompanyCode());

                if ((responseComplaintDoc != null) && (!responseComplaintDoc.toLowerCase().contains("error"))) {

                    JSONObject jsonObject = new JSONObject(responseComplaintDoc);
                    JSONArray jsonArray = jsonObject.getJSONArray("Tables0");
                    JSONObject object = jsonArray.getJSONObject(0);
                    dateDD = object.getString("DATE2");
                    dateMM = object.getString("DATE1");
                    cNo = object.getString("CNO");
                } else {
                    customVariablesAndMethod.msgBox(context,"Nothing Found");
                }
            } catch (Exception e) {
                customVariablesAndMethod.msgBox(context,"Exception Found" + e);
            }


            return RetailerList;
        }

        @Override
        protected void onPostExecute(ArrayList<SpinnerModel> spinnerModels) {
            super.onPostExecute(spinnerModels);
            try {
                titleName = new SpinnerModel[spinnerModels.size()];
                for (int i = 0; i < spinnerModels.size(); i++) {
                    titleName[i] = spinnerModels.get(i);
                    arrayShort = new ArrayList<SpinnerModel>(Arrays.asList(titleName));

                }

            } catch (Exception e) {
                e.printStackTrace();
                pd.dismiss();
            }
            editDate.setText(dateDD);
            compNo.setText(cNo);

            pd.dismiss();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item != null) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void onClickDropDown() {


        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        final EditText editText = new EditText(context);
        final ListView listView = new ListView(context);
        editText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.search, 0, 0, 0);
        arrayShort = new ArrayList<SpinnerModel>(Arrays.asList(titleName));
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(editText);
        linearLayout.addView(listView);
        myDialog.setView(linearLayout);
        SpinAdapter arrayAdapter = new SpinAdapter(context, R.layout.spin_row, arrayShort);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myalertDialog.dismiss();

                retailerId = ((TextView) view.findViewById(R.id.spin_id)).getText().toString();
                retailerName = ((TextView) view.findViewById(R.id.spin_name)).getText().toString();
                Custom_Variables_And_Method.CHEMIST_ID = retailerId;
                btDropdowon.setText(retailerName);
            }
        });
        myalertDialog = myDialog.show();

    }
}