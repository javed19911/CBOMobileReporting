package com.cbo.cbomobilereporting.ui_new.mail_activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui.MailTo_PPL;
import com.cbo.cbomobilereporting.ui.Mail_CC;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import services.CboServices;
import services.ServiceHandler;
import com.cbo.cbomobilereporting.MyCustumApplication;
import utils_new.Custom_Variables_And_Method;
import utils_new.GalleryUtil;
import utils_new.up_down_ftp;

public class CreateMail1 extends AppCompatActivity implements up_down_ftp.AdapterCallback{
    private final int GALLERY_ACTIVITY_CODE=200;
    private final int RESULT_CROP = 400;
    private final int REQUEST_CAMERA=201;
    private final int CHOOSE_FILE_REQUESTCODE=202;
    String picturePath="";
    private File output=null;
    String filename="",filename_type="";
    int filetype=0;

    TextView tof, ccf, subf;
    EditText toppls, ccppl, subject, message;
    Button toadd, tocc, send, back;
    LinearLayout lay1, lay2, lay3, lay4;
    Custom_Variables_And_Method customVariablesAndMethod;
    Context context;
    String company_name = "", myppl_list = "", name = "", name2 = "";
    String toid = "", ccid = "", result = "", session_id = "";
    String[] toppl;
    int PA_ID, newres;
    int cnt = 0, cnt2 = 0;
    int idarray[], ccarray[];
    ServiceHandler myServiceHandler;
    CBO_DB_Helper myCboDbHelper;
    Date date;
    View line_cc;

    CheckBox add_attachment;
    RadioGroup attach_option;
    ImageView attach_img;
    ArrayList<Map<String, String>> data = null;
    String Msg_Id,mail_type="";
    boolean save_as_draft=true;

    public ProgressDialog progress1;
    private  static final int MESSAGE_INTERNET_MAIL_COMMIT=1;


    public void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.create_mail1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_hadder);
        TextView textView = (TextView) findViewById(R.id.hadder_text_1);

        add_attachment = (CheckBox) findViewById(R.id.add_attachment);
        attach_option = (RadioGroup) findViewById(R.id.attach_option);
        attach_img= (ImageView) findViewById(R.id.attach_img);

        textView.setText("Compose Mail");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }


        tof = (TextView) findViewById(R.id.topplfont);
        ccf = (TextView) findViewById(R.id.ccpplfont);
        line_cc=(View) findViewById(R.id.line_cc);


        toppls = (EditText) findViewById(R.id.idfortoppl);
        ccppl = (EditText) findViewById(R.id.idforccppl);
        subject = (EditText) findViewById(R.id.idforsubject);
        message = (EditText) findViewById(R.id.idformessage);

        toadd = (Button) findViewById(R.id.idtoaddppl);
        tocc = (Button) findViewById(R.id.idtoccppl);
        send = (Button) findViewById(R.id.sendmail2);
        back = (Button) findViewById(R.id.backmail2);

        lay1 = (LinearLayout) findViewById(R.id.rt1);
        lay3 = (LinearLayout) findViewById(R.id.rt2);
//===========================================================================B.L.==========================================================================================
        tof.setText("To: ");
        line_cc.setVisibility(View.GONE);
        lay3.setVisibility(View.GONE);
        context=this;
        progress1 = new ProgressDialog(this);
        myCboDbHelper = new CBO_DB_Helper(context);
        myServiceHandler = new ServiceHandler(context);
        customVariablesAndMethod=Custom_Variables_And_Method.getInstance();
        PA_ID = Custom_Variables_And_Method.PA_ID;
        date=new Date();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        session_id = "" + new Date().getTime();


        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        if (bundle!=null){
            save_as_draft=false;
            Msg_Id=intent.getStringExtra("mail_id");
            mail_type=intent.getStringExtra("mail_type");
            data=myCboDbHelper.get_Mail("X",Msg_Id);


            Msg_Id = data.get(0).get("mail_id");
            message.setText(data.get(0).get("REMARK"));
            subject.setText(data.get(0).get("sub"));
            if (!data.get(0).get("FILE_NAME").equals("")) {
                attach_img.setImageResource(R.drawable.attach);
                final String[] aT1 = {data.get(0).get("FILE_NAME")};
                filename=aT1[0].substring( aT1[0].lastIndexOf("/")+1);
                if(data.get(0).get("category").equals("d")) {
                    File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                    if (file1.exists()){
                        previewCapturedImage(file1.getPath());
                    }
                }else{
                    android.view.ViewGroup.LayoutParams layoutParams = attach_img.getLayoutParams();
                    layoutParams.width = 70;
                    layoutParams.height = 70;
                    attach_img.setLayoutParams(layoutParams);
                }

                attach_img.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!aT1[0].contains("http://") && !data.get(0).get("category").equals("d")){
                            aT1[0] ="http://"+ aT1[0];
                        }else if(data.get(0).get("category").equals("d")){
                            aT1[0]=new File(Environment.getExternalStorageDirectory(), "cbo/"+aT1[0]).toString();
                        }

                        /*Intent i = new Intent(context, CustomWebView.class);
                        i.putExtra("A_TP1", aT1[0]);
                        i.putExtra("Menu_code", "");
                        i.putExtra("Title", "Attachment");
                        context.startActivity(i);*/
                        MyCustumApplication.getInstance().LoadURL("Attachment",aT1[0]);
                    }
                });
            }

            switch (mail_type){
                case "E":
                    name = data.get(0).get("from");
                    toid =data.get(0).get("from_id");
                    toppls.setText(name);
                    myCboDbHelper.delete_Mail(Msg_Id);
                    save_as_draft=true;
                    break;
                case "R":
                    subject.setFocusable(false);
                    toppls.setFocusable(false);
                    name = data.get(0).get("from");
                    toid =data.get(0).get("from_id");
                    toppls.setText(name);
                    toadd.setVisibility(View.GONE);
                    break;
                case "F":
                    subject.setFocusable(false);
                    //message.setFocusable(false);
                    break;
            }

        }

        toppls.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                line_cc.setVisibility(View.VISIBLE);
                lay3.setVisibility(View.VISIBLE);
                toadd.setVisibility(View.VISIBLE);
                tocc.setVisibility(View.GONE);
                return true;
            }
        });
        ccppl.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                tocc.setVisibility(View.VISIBLE);
                toadd.setVisibility(View.GONE);
                return true;
            }
        });

        subject.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (ccppl.getText().toString().equals("")) {
                    line_cc.setVisibility(View.GONE);
                    lay3.setVisibility(View.GONE);
                }

            }
        });

        toadd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), MailTo_PPL.class);
                startActivityForResult(i, 0);
            }
        });

        tocc.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(), Mail_CC.class);
                startActivityForResult(i, 1);
            }
        });

        send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                semd_mail();
            }
        });




        back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                save_as_draft();
                finish();
            }
        });



    }
    //===================================================================onCreate End========================================================================================


    private void semd_mail(){
        if (toppls.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Please Add Atleast one Recepient ");

        } else if (subject.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Enter Subject Please.... ");
        } else if (message.getText().toString().equals("")) {
            customVariablesAndMethod.msgBox(context,"Enter Your Massege Please.... ");
        } else {


            if (!filename.equals("") && filetype==0){
                progress1.setMessage("Please Wait..\nuploading Image");
                progress1.setCancelable(false);
                progress1.show();
                File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                new up_down_ftp().uploadFile(file2,context);

            }else if (!filename.equals("") && filetype==1){
                progress1.setMessage("Please Wait..\nuploading Image");
                progress1.setCancelable(false);
                progress1.show();
                //File file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "CBO" + File.separator + filename);
                File file2 = new File(filename);
                new up_down_ftp().uploadFile(file2,context);
                filename=filename.substring(filename.lastIndexOf("/")+1);
                Toast.makeText(context,filename,Toast.LENGTH_LONG).show();
            }else{
                Mail_commit();
            }



        }
    }

    private void  Mail_commit(){
        //Start of call to service

        HashMap<String, String> request = new HashMap<>();
        request.put("sCompanyFolder", myCboDbHelper.getCompanyCode());
        request.put("sSESSION_ID", "" + session_id);
        request.put("iSEND_PA_ID","" + PA_ID);
        request.put("sREMARK", "" + message.getText().toString());
        request.put("sSUBJECT","" + subject.getText().toString());
        request.put("sFILE_NAME",filename);
        request.put("sToPaId", toid);
        request.put("sCC_ToPaId",ccid);
        request.put("sMAIL_TYPE", mail_type);
        request.put("iMAIL_ID",Msg_Id);

        ArrayList<Integer> tables = new ArrayList<>();
        tables.add(0);  // to get all the tables

        progress1.setMessage("Please Wait.. \n Send mail...");
        progress1.setCancelable(false);
        if (!progress1.isShowing() && progress1!=null && !isFinishing())
        progress1.show();


        new CboServices(this, mHandler).customMethodForAllServices(request, "MAILCOMMIT_ALL_1", MESSAGE_INTERNET_MAIL_COMMIT, tables);

        //End of call to service
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_INTERNET_MAIL_COMMIT:
                    progress1.dismiss();
                    if ((null != msg.getData())) {

                        parser_mail_id(msg.getData());

                    }
                    break;

                case 99:
                    progress1.dismiss();
                    if ((null != msg.getData())) {
                        customVariablesAndMethod.msgBox(context,msg.getData().getString("Error"));
                    }
                    break;
                default:
                    progress1.dismiss();

            }
        }
    };

    private void parser_mail_id(Bundle result) {
        if (result!=null ) {
            try {
                String table0 = result.getString("Tables0");
                JSONArray jsonArray1 = new JSONArray(table0);
                JSONObject jsonObject1 = jsonArray1.getJSONObject(0);
                String mail_id = jsonObject1.getString("DCRID");

                customVariablesAndMethod.msgBox(context,"Massage Send Successfully..");
                Intent i = new Intent(context, Outbox_Mail.class);
                i.putExtra("mail_type","s");
                i.putExtra("mail_id",mail_id);
                startActivity(i);
                finish();

            } catch (JSONException e) {
                Log.d("MYAPP", "objects are: " + e.toString());
                CboServices.getAlert(this, "Missing field error", getResources().getString(R.string.service_unavilable) + e.toString());
                e.printStackTrace();
            }

        }
    }


    private void open_galary(){
        Intent gallery_Intent = new Intent(context, GalleryUtil.class);
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
            filename = PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_"+date.getTime()+ ".jpg";
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
        filename = PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_"+date.getTime()+ ".jpg";
        output = new File(dir, filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }

    public static String getPath(Context context, Uri uri) throws URISyntaxException {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_ACTIVITY_CODE) {
            if(resultCode == Activity.RESULT_OK){
                picturePath = data.getStringExtra("picturePath");
                //perform Crop on the Image Selected from Gallery
                filename = PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_"+date.getTime()+ ".jpg";
                moveFile(picturePath);
            }
        }
        if (requestCode == CHOOSE_FILE_REQUESTCODE) {
            if(resultCode == Activity.RESULT_OK){

                Uri uri = data.getData();
                try {
                    filename = getPath(this, uri);
                    filename_type="f";//file
                    attach_img.setImageResource(R.drawable.attach);
                    android.view.ViewGroup.LayoutParams layoutParams = attach_img.getLayoutParams();
                    layoutParams.width = 70;
                    layoutParams.height = 70;
                    attach_img.setLayoutParams(layoutParams);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(context,filename,Toast.LENGTH_LONG).show();
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
                            attach_img.setImageBitmap(myBitmap);

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
                        attach_img.setImageBitmap(myBitmap);

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
                File file1 = new File(Environment.getExternalStorageDirectory()+File.separator+ "CBO"+File.separator+ filename);

                if (file1.exists()){
                    previewCapturedImage(file1.getPath());
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
        if (requestCode == 0 ) {
            if (resultCode == RESULT_OK) {
                Bundle b1 = data.getExtras();
                name = b1.getString("mailtoname");
                toid = b1.getString("mailto");
                toppls.setText(name);
                ccppl.setText(name2);
            }
        }
        if (requestCode == 1 ) {
            if (resultCode == RESULT_OK) {
                Bundle b1 = data.getExtras();
                name2 = b1.getString("ccname");
                ccid = b1.getString("ccid");
                toppls.setText(name);
                ccppl.setText(name2);
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
        if (requestCode == this.CHOOSE_FILE_REQUESTCODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                AccessingFiles();
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
            if (!dir.exists())
            {
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

            previewCapturedImage(dir+File.separator + filename);

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

            android.view.ViewGroup.LayoutParams layoutParams = attach_img.getLayoutParams();
            layoutParams.width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            layoutParams.height = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
            attach_img.setLayoutParams(layoutParams);

            final Bitmap bitmap = BitmapFactory.decodeFile(picUri,
                    options);
            filename_type="i";//image
            attach_img.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }


    /*private void performCrop(String picUri) {
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
    }
*/

    public void AccessingFiles() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    CHOOSE_FILE_REQUESTCODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onBackPressed() {
        save_as_draft();
        finish();
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mail, menu);
        return true;
    }

    private void save_as_draft(){
        if((!toid.equals("") ||!subject.getText().toString().equals("") || !message.getText().toString().equals("")) && save_as_draft) {
            myCboDbHelper.insert_Mail(0, toid, name, customVariablesAndMethod.currentDate(), customVariablesAndMethod.currentTime(context),
                    "0", "d", "", subject.getText().toString(), message.getText().toString()
                    , filename_type
                    , filename);
            // filename_type="i" for image and f for file;
            customVariablesAndMethod.msgBox(context,"Saved as Draft");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.send:
                semd_mail();
                break;
            case R.id.attach:
                break;
            case R.id.draft:
               save_as_draft();
                finish();
                break;
            case R.id.show_draft:
                Intent i = new Intent(context, Outbox_Mail.class);
                i.putExtra("mail_type","d");
                startActivity(i);
                break;
            case R.id.camera:
                filetype=0;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(CreateMail1.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_CAMERA);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    capture_Image();
                }
                break;
            case R.id.galary:
                filetype=0;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(CreateMail1.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, GALLERY_ACTIVITY_CODE);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {
                    open_galary();

                }
                break;
            case R.id.file:
                filetype=1;
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //takePictureButton.setEnabled(false);
                    ActivityCompat.requestPermissions(CreateMail1.this, new String[] { Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE }, CHOOSE_FILE_REQUESTCODE);
                    Toast.makeText(context, "Please allow the permission", Toast.LENGTH_LONG).show();

                }else {

                    AccessingFiles();
                }
                break;
            default:
                save_as_draft();
                finish();
        }
        return super.onOptionsItemSelected(item);
    }


 /*   @Override
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
                    Mail_commit();
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
        Mail_commit();
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
}


