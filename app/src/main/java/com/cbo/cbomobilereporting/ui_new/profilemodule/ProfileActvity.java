package com.cbo.cbomobilereporting.ui_new.profilemodule;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;
import com.cbo.cbomobilereporting.ui_new.AttachImage;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.uenics.javed.CBOLibrary.CboProgressDialog;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import utils_new.Custom_Variables_And_Method;
import utils_new.up_down_ftp;

public class ProfileActvity extends CustomActivity implements  up_down_ftp.AdapterCallback{




    Context context;
    File  filepath=null;
    private final int USER_PROFILE=200;

    private  ProgressDialog progress1;

    int PA_ID;
    String dcr_id = "";

    private File USER_PIC,COMPANY_PIC;
    File OutputFile;
    private ImageView  edit_user_icon, back_icon;
    private CircleImageView user_image;
    CboProgressDialog cboProgressDialog = null;
    TextView emplyoyee_name,emp_desig,emp_hq;
    TextView company_name;
    CBO_DB_Helper cbo_db_helper;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_view_activity);


        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar_hadder);
        TextView hader_text = (TextView) findViewById(R.id.hadder_text_1);
        hader_text.setText("Doctor Call");
        setSupportActionBar(toolbar);


        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);
        }

        cbo_db_helper = new CBO_DB_Helper(ProfileActvity.this);

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.my_statusbar_color));
        }
        user_image=(CircleImageView)findViewById(R.id.user_pic);
        emplyoyee_name = (TextView) findViewById(R.id.emp_name_drawer);
        company_name = (TextView) findViewById(R.id.company_name_drawer);
        emp_desig = (TextView)  findViewById(R.id.emp_desig);
        emp_hq = (TextView) findViewById(R.id.emp_hq);
   //     back_icon=(ImageView)findViewById(R.id.back_icon);

        emplyoyee_name.setText(Custom_Variables_And_Method.PA_NAME);
        company_name.setText(cbo_db_helper.getCOMP_NAME());
        emp_desig.setText(Custom_Variables_And_Method.DESIG);
        emp_hq.setText(Custom_Variables_And_Method.HEAD_QTR);



       if (customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(ProfileActvity.this,"USER_PIC")!=null && !customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(ProfileActvity.this,"USER_PIC").isEmpty()){


           previewCapturedImage(customVariablesAndMethod.getDataFrom_FMCG_PREFRENCE(ProfileActvity.this,"USER_PIC"));
       }
        edit_user_icon= (ImageView) findViewById(R.id.edit_user_icon);
        edit_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProfilePicture();
            }
        });
 back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 finish();
            }
        });

     //   disableEditText(emplyoyee_name,emp_desig,emp_hq);

        if (getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_hadder_2016);

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case USER_PROFILE :
                    OutputFile = (File) data.getSerializableExtra("Output");

                      //  new up_down_ftp().uploadFile( OutputFile, context);
                        try {
                            cboProgressDialog = new CboProgressDialog(ProfileActvity.this, "Please Wait..\nuploading Image");
                            cboProgressDialog.show();
                            new up_down_ftp().uploadFile( OutputFile, ProfileActvity.this);

                          //  previewCapturedImage(OutputFile.getPath());
                        } catch (Exception e) {


                            Toast.makeText(context, "Please try again Server error", Toast.LENGTH_SHORT).show();
                            // ignore
                        }



                    break;

                default:

            }
        }
    }
    private void disableEditText(EditText emplyoyee_name, EditText emp_desig, EditText emp_hq) {

        //for name
        emplyoyee_name.setFocusable(false);
        emplyoyee_name.setEnabled(false);
        emplyoyee_name.setCursorVisible(true);
        emplyoyee_name.setKeyListener(null);
        emplyoyee_name.setBackgroundColor(Color.TRANSPARENT);


        //for desghination
        emp_desig.setFocusable(false);
        emp_desig.setEnabled(false);
        emp_desig.setCursorVisible(true);
        emp_desig.setKeyListener(null);
        emp_desig.setBackgroundColor(Color.TRANSPARENT);


        //for headqtr

        emp_hq.setFocusable(false);
        emp_hq.setEnabled(false);
        emp_hq.setCursorVisible(true);
        emp_hq.setKeyListener(null);
        emp_hq.setBackgroundColor(Color.TRANSPARENT);
    };
    private void addProfilePicture() {

        String filenameTemp ="profile/"+ Custom_Variables_And_Method.PA_ID+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
      //  String filenameTemp ="profile/"+ Custom_Variables_And_Method.PA_ID+".jpg";

                // String filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_order_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";


        Intent intent = new Intent(ProfileActvity.this, AttachImage.class);
        intent.putExtra("Output_FileName",filenameTemp);
        intent.putExtra("SelectFrom", AttachImage.ChooseFrom.all);
        startActivityForResult(intent,USER_PROFILE);
    }

    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview
            Glide.with(this).load( picUri).into( user_image);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.profile_menu,menu);
        return true;




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.share) {

        }else{
            finish();
          // startActivity(new Intent(this, ViewPager_2016.class));

        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        //startActivity(new Intent(this, ViewPager_2016.class));

    }

    @Override
    public void started(Integer responseCode, String message, String description) {

    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {

        cboProgressDialog.dismiss();
        previewCapturedImage(OutputFile.getPath());
        customVariablesAndMethod.setDataInTo_FMCG_PREFRENCE(ProfileActvity.this,"USER_PIC",OutputFile.getPath());
        Toast.makeText(ProfileActvity.this, "your image uploaded succesfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {

    }

    @Override
    public void failed(Integer responseCode, String message, String description) {

    }
}
