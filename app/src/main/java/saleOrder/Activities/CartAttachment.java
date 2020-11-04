package saleOrder.Activities;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.List;

import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import saleOrder.MyOrderAPIService;
import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;
import utils_new.cboUtils.CBOImageView;
import utils_new.up_down_ftp;

public class CartAttachment extends CustomActivity implements  up_down_ftp.AdapterCallback {


    private final int REQUEST_CAMERA=201;
    mOrder order;
    File OutputFile;
    ImageView attachImg;
    Button cancel,save;
    CboProgressDialog cboProgressDialog = null;
    String filenameTemp = "";
    CBOImageView CboimageView;
    Adapter adapter;
    private List<String>imgarraylist= new ArrayList<>();
    RecyclerView AttachmentList;
    TextView title,subTitle;
    FloatingActionButton fab ;

    MenuItem upload = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_attachment);
        order = (mOrder) getIntent().getSerializableExtra("order");


        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            toolbar.setNavigationOnClickListener(view -> onBackPressed());
        }

        title  = toolbar.findViewById(R.id.title);
        subTitle = toolbar.findViewById(R.id.subTitle);

        fab = findViewById(R.id.fab);

        title.setText("Attachment");
        subTitle.setText("Order No :- " + order.getDocNo());


        AttachmentList = findViewById(R.id.list);
        adapter = new Adapter();
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        AttachmentList.setLayoutManager(mLayoutManager1);
        AttachmentList.setItemAnimator(new DefaultItemAnimator());
        AttachmentList.setAdapter(this.adapter);


        imgarraylist = order.getAttachmentArr();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAttachment();
            }
        });

        if (imgarraylist.size() == 0){
            fab.performClick();
        }else if (imgarraylist.size() >=3){
            fab.hide();;
        }

    }

    private void UpLoadFile(){
        cboProgressDialog = new CboProgressDialog(context, "Please Wait..\nuploading Image");
        cboProgressDialog.show();
        new up_down_ftp().uploadFile( filesToUpload(), context);
    }

    private File[] filesToUpload(){
        List<File> files = new ArrayList();
        for (String file :  imgarraylist) {
            if (!file.toLowerCase().contains("upload/")){
                files.add(new File(file));
            }

        }

        File[] arr = new File[files.size()];
       for (int i=0; i<files.size();i++){
           arr[i] = files.get(i);
       }

        return arr;
    }

    private ArrayList<String> FilesToCommit(){
        ArrayList<String> files = new ArrayList();
        for (String file :  imgarraylist) {
            if (!file.toLowerCase().contains("upload/")){
                files.add(new File(file).getName());
            }else{
                files.add(file);
            }

        }
        return files;
    }

    private void AddAttachment(){
        String filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+Custom_Variables_And_Method.DCR_ID+"_order_"+customVariablesAndMethod.get_currentTimeStamp()+".jpg";
        //choosePhoto = new ChoosePhoto(context, REQUEST_CAMERA, ChoosePhoto.ChooseFrom.all);
        Intent intent = new Intent(context, AttachImage.class);
        intent.putExtra("Output_FileName",filenameTemp);
        intent.putExtra("SelectFrom",AttachImage.ChooseFrom.all);
        startActivityForResult(intent,REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA :
                    OutputFile = (File) data.getSerializableExtra("Output");
                    imgarraylist.add(OutputFile.getPath());
                    adapter.notifyDataSetChanged();
                    if (imgarraylist.size() >=3){
                        fab.hide();;
                    }
                    //previewCapturedImage(OutputFile.getPath());
                    break;

                default:

            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.attach_menu, menu);
        upload = menu.findItem(R.id.upload);
        if  (subTitle.getText().toString().equalsIgnoreCase("New Order") && upload != null){
            upload.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.upload:
                UpLoadFile();
                return true;
            default:
                onBackPressed();
                return super.onOptionsItemSelected(item);
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

        order.setAttachment(FilesToCommit());
        //Start of call to service

        HashMap<String,String> request=new HashMap<>();
        request.put("sCompanyFolder", MyCustumApplication.getInstance().getUser().getCompanyCode());
        request.put("iOrdId", order.getDocId() );
        //request.put("sFILE_PATH", filenameTemp );
        request.put("sFILE_PATH", order.getAttachment() );


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


    public class Adapter extends  RecyclerView.Adapter<Adapter.MyViewHolder> {




        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView pescription;
            public TextView delete;
            public MyViewHolder(View view) {
                super(view);

                //city = (TextView) view.findViewById(R.id.city);
                pescription = view.findViewById(R.id.pescription);
                delete = view.findViewById(R.id.delete);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        imgarraylist.remove(getAdapterPosition());
                        adapter.notifyItemRemoved(getAdapterPosition());
                        if (imgarraylist.size() <3){
                            fab.show();;
                        }
                    }
                });


                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == R.id.delete){
                            imgarraylist.remove(getAdapterPosition());
                            adapter.notifyItemRemoved(getAdapterPosition());
                            if (imgarraylist.size() <3){
                                fab.show();;
                            }
                        }
                        /*else if (recycleViewOnItemClickListener != null) {
                            recycleViewOnItemClickListener.onClick(v,getAdapterPosition(),false);
                        }*/
                    }
                });
            }
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(context)
                    .inflate(R.layout.pescription_card, viewGroup,false);

            return new MyViewHolder(itemView);
        }

        public void onBindViewHolder(MyViewHolder viewHolder, int position) {
            String path="";
            path=imgarraylist.get(position);
            Glide.with(context)
                    .load(path)
                    .error(R.drawable.no_image)
                    .into(viewHolder.pescription);

        }


        public int getItemCount() {
            return imgarraylist.size();
        }
    }
}
