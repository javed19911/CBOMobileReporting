package saleOrder.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.AttachImage;
import com.cbo.cbomobilereporting.ui_new.CustomActivity;
import com.uenics.javed.CBOLibrary.CBOServices;
import com.uenics.javed.CBOLibrary.CboProgressDialog;
import com.uenics.javed.CBOLibrary.ResponseBuilder;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import CameraGalaryPkg.ChoosePhoto;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.MyOrderAPIService;
import utils.adapterutils.Dcr_Workwith_Model;
import utils.adapterutils.ExpandableListAdapter;
import utils.adapterutils.PobModel;
import utils.adapterutils.SpinnerModel;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.up_down_ftp;

public class CartAttachment extends CustomActivity implements  up_down_ftp.AdapterCallback {


    private final int REQUEST_CAMERA=201;
    mOrder order;
    File OutputFile;
    ImageView attachImg;
    Button cancel,save;
    CboProgressDialog cboProgressDialog = null;
    String filenameTemp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_attachment);
        order = (mOrder) getIntent().getSerializableExtra("order");

        attachImg = findViewById(R.id.attach_img);
        save = findViewById(R.id.save);
        cancel = findViewById(R.id.cancel);

        filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_order_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
        //choosePhoto = new ChoosePhoto(context, REQUEST_CAMERA, ChoosePhoto.ChooseFrom.all);
        Intent intent = new Intent(context, AttachImage.class);
        intent.putExtra("Output_FileName",filenameTemp);
        intent.putExtra("SelectFrom",AttachImage.ChooseFrom.all);
        startActivityForResult(intent,REQUEST_CAMERA);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (OutputFile == null){
                    String filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_order_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
                    //choosePhoto = new ChoosePhoto(context, REQUEST_CAMERA, ChoosePhoto.ChooseFrom.all);
                    Intent intent = new Intent(context, AttachImage.class);
                    intent.putExtra("Output_FileName",filenameTemp);
                    intent.putExtra("SelectFrom",AttachImage.ChooseFrom.all);
                    startActivityForResult(intent,REQUEST_CAMERA);
                }else{
                    cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nuploading Image");
                    cboProgressDialog.show();
                    new up_down_ftp().uploadFile( OutputFile, context);
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA :
                    OutputFile = (File) data.getSerializableExtra("Output");
                    previewCapturedImage(OutputFile.getPath());
                    break;

                default:

            }
        }
    }


    private void previewCapturedImage(String picUri) {
        try {
            // hide video preview
            Glide.with(this).load( picUri).into( attachImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSendResponse(String FileURL) {
        Intent intent = new Intent();
        intent.putExtra("order",order);
        intent.putExtra("FileURL",FileURL);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
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
        OrderFileCommit();
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
        cboProgressDialog.dismiss();
        AppAlert.getInstance().getAlert(context,message,description);
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        cboProgressDialog.dismiss();
        AppAlert.getInstance().getAlert(context,message,description);
    }


    public void OrderFileCommit(){

        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iOrdId", order.getDocId() );
        request.put("sFILE_PATH", filenameTemp );


        ArrayList<Integer> tables=new ArrayList<>();
        tables.add(0);

        new MyOrderAPIService(context).execute(new ResponseBuilder("OrderFile_Commit",request)
                .setTables(tables)
                .setDescription("Please Wait..\nuploading Image")
                .setResponse(new CBOServices.APIResponse() {
                    @Override
                    public void onComplete(Bundle bundle) throws Exception {
                        String table0 = bundle.getString("Tables0");
                        JSONArray jsonArray = new JSONArray(table0);
                        String fileURL = jsonArray.getJSONObject(0).getString("FILE_PATH");
                        if(jsonArray.getJSONObject(0).getString("STATUS").equalsIgnoreCase("1")){
                            //success
                            AppAlert.getInstance().Alert(context, "Success!!!",
                                    "File Uploaded Succesfully.....", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            order.setAttachment(fileURL);
                                            onSendResponse(fileURL);
                                        }
                                    });

                        }else{
                            AppAlert.getInstance().getAlert(context,"Error!!!",
                                    "Failed to upload the file\nPlease try again");
                        }

                    }

                    @Override
                    public void onResponse(Bundle bundle) throws Exception {


                    }

                    @Override
                    public void onError(String s, String s1) {

                    }
                }));


        //End of call to service
    }
}
