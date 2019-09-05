package utils_new.cboUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.AttachImage;
import com.cbo.utils.MultiSelectDetailView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils_new.AppAlert;
import utils_new.Custom_Variables_And_Method;

public class CBOImageView extends MultiSelectDetailView<String,CBOImageView.MyViewHolder>  {




    public interface iCBOImageView {
        public void OnAddClicked();
        public void OnAdded();
        public void OnDeleted(String file);
        public void OnUpdated(ArrayList<String> files);
    }

    public static final int REQUEST_CAMERA=2001;

    private int maxAttachment = 3;
    private iCBOImageView listenter;
    public void setListener(iCBOImageView listener){
        this.listenter = listener;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView pescription;
        public TextView delete;
        public MyViewHolder(View view) {
            super(view);

            //city = (TextView) view.findViewById(R.id.city);
            pescription = view.findViewById(R.id.pescription);
            delete = view.findViewById(R.id.delete);

            delete.setOnClickListener(this);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete){
                try {
                    String filePath = getDataList().get(getAdapterPosition());
                    getDataList().remove(getAdapterPosition());
                    getAdapter().notifyItemRemoved(getAdapterPosition());
                    if (listenter !=null){
                        listenter.OnDeleted(filePath);
                    }
                }catch (Exception e){
                    getAdapter().notifyDataSetChanged();
                }


            }
            else {
                PreviewImage(getContext(),getDataList().get(getAdapterPosition()));
            }
        }
    }



    public File[] filesToUpload(){
        List<File> files = new ArrayList();
        for (String file :  getDataList()) {
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


    public int getMaxAttachment() {
        return maxAttachment;
    }

    public void setMaxAttachment(int maxAttachment) {
        this.maxAttachment = maxAttachment;
    }

    public void addAttachment(Activity context) {
        addAttachment(context,AttachImage.ChooseFrom.all);
    }


    public void addAttachment(Activity context, AttachImage.ChooseFrom chooseFrom) {
        String filenameTemp = Custom_Variables_And_Method.PA_ID+"_"+ Custom_Variables_And_Method.DCR_ID+"_attach_"+Custom_Variables_And_Method.getInstance().get_currentTimeStamp()+".jpg";
        //choosePhoto = new ChoosePhoto(context, REQUEST_CAMERA, ChoosePhoto.ChooseFrom.all);
        Intent intent = new Intent(context, AttachImage.class);
        intent.putExtra("Output_FileName",filenameTemp);
        intent.putExtra("SelectFrom", chooseFrom);
        context.startActivityForResult(intent,REQUEST_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA :
                    File OutputFile = (File) data.getSerializableExtra("Output");
                    getDataList().add(OutputFile.getPath());
                    updateDataList(getDataList());
                    if (listenter !=null){
                        listenter.OnAdded();
                    }
                    break;

                default:

            }
        }
    }

    public void setAttachment(String attacments){
        updateDataList(attacments.isEmpty() ? new ArrayList() : new ArrayList(Arrays.asList(attacments.split("\\|\\^"))));
    }
    public String getAttachmentStr() {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for(String file : getDataList()) {
            if (count != 0) {
                sb.append("|^");
            }
            ++count;
            sb.append(file);
        }
        return sb.toString();
    }

    public String getAttachmentNameStr() {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for(String file : getDataList()) {
            if (count != 0) {
                sb.append("|^");
            }
            ++count;
            sb.append(file.substring(file.lastIndexOf("/")+1));
        }
        return sb.toString();
    }

    private void PreviewImage(Context context,String path){
        //Bitmap b = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // builder.setPositiveButton("OK", null);
        final AlertDialog dialog = builder.create();
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogLayout = inflater.inflate(R.layout.attachment_pop_up, null);
        ImageView attach_img= (ImageView) dialogLayout.findViewById(R.id.attach_img);
        ImageView close= (ImageView) dialogLayout.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        //attach_img.setImageBitmap(b);
        Glide.with(context)
                .load(path)
                .error(R.drawable.no_image)
                .into(attach_img);
        dialog.setView(dialogLayout);
        dialog.show();
    }

    public CBOImageView(Context context) {
        super(context);
        initialize();
    }

    public CBOImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CBOImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CBOImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize();
    }

    private void initialize(){
        setHeaderReqd(false);
        setTitle("Attachment");
    }

    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int position) {
        String path="";
            path=getDataList().get(position);
            Glide.with(getContext())
                    .load(path)
                    .error(R.drawable.no_image)
                    .into(viewHolder.pescription);

    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pescription_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindHeader(MyViewHolder viewHolder) {

    }

    @Override
    public void onClickListener() {
        if (getDataList().size()>=getMaxAttachment()){
            AppAlert.getInstance().getAlert(getContext(),"Alert!!!","Only "+getMaxAttachment()+" attachment is allowed..");
        }else if (listenter != null) {
             listenter.OnAddClicked();
        }
    }

}