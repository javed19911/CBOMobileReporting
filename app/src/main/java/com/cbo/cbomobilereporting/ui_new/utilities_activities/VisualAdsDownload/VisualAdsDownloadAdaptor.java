package com.cbo.cbomobilereporting.ui_new.utilities_activities.VisualAdsDownload;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;

import java.util.ArrayList;

import utils_new.Custom_Variables_And_Method;
import utils_new.interfaces.RecycleViewOnItemClickListener;
import utils_new.up_down_ftp;

/**
 * Created by cboios on 31/12/18.
 */

public class VisualAdsDownloadAdaptor extends RecyclerView.Adapter<VisualAdsDownloadAdaptor.MyviewHolder> implements up_down_ftp.AdapterCallback {

    Context context;
    ArrayList<mVisualAds> Rptdata=new ArrayList<>();
    ArrayList<mVisualAds> RptdataCopy=new ArrayList<>();
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    private RecycleViewOnItemClickListener recycleViewOnItemClickListener;
    private int filesdownloaded =0;


    private DownloadListener downloadListener;
    interface DownloadListener{
        void onSucess(int filesDownloaded,int totalFiles);
        void onError(String message);
    }

    public void setListener(DownloadListener downloadListener){
        this.downloadListener = downloadListener;
    }

    public VisualAdsDownloadAdaptor(Context mcontext, ArrayList<mVisualAds> data, RecycleViewOnItemClickListener recycleViewOnItemClickListener) {
        this.context = mcontext;
        this.Rptdata = data;
        RptdataCopy = (ArrayList<mVisualAds>) data.clone();
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance(context);
        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
        Runnable runnable = new Runnable() {
            public void run() {
                startDownload();
            }
        };
        final Thread mythread1 = new Thread(runnable);
        mythread1.start();
    }

    private void startDownload(){
        int position =0;
        for (mVisualAds visualAds : Rptdata) {
            if (!visualAds.isDownloadStarted() && !visualAds.isFolderYN()) {
                visualAds.setDownloadStarted(true);
                int finalPosition = position;

              /*  Runnable runnable = new Runnable() {
                    public void run() {*/
                        try {
                            Thread.sleep((finalPosition+1)*100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //new up_down_ftp().setResponseCode(finalPosition).download_visual_aids(VisualAdsDownloadAdaptor.this, visualAds.getFileName(), visualAds.getDirectory());
                        new up_down_ftp().setResponseCode(finalPosition).downloadFile(VisualAdsDownloadAdaptor.this, visualAds.getFileName(), visualAds.getDirectory());
                    }
               /* };
                final Thread mythread1 = new Thread(runnable);
                mythread1.start();*/
                position++;
            //}
        }

    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.visual_ads_download_card, parent, false);

        return new MyviewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        mVisualAds visualAds = RptdataCopy.get(position);
        holder.ItemName.setText(visualAds.getItemName()); //+"("+visualAds.getTrys() + ")"
        //holder.downloading.setProgress((int) visualAds.getProgess());
        if (visualAds.isDownloadCompleted()) {
            holder.container.setVisibility(View.GONE);
            holder.ItemName.setTextColor(context.getResources().getColor(R.color.logo_green));
            holder.downloading.setVisibility(View.GONE);
            holder.downloaded.setVisibility(View.VISIBLE);
            holder.failed.setVisibility(View.GONE);
        }else  if (visualAds.isDownloadFailed()) {
            //if ( visualAds.getTrys()<100 ) {
                //visualAds.setDownloadStarted(false);
            //}
            holder.ItemName.setTextColor(context.getResources().getColor(R.color.red));
            holder.downloading.setVisibility(View.GONE);
            holder.downloaded.setVisibility(View.GONE);
            holder.failed.setVisibility(View.VISIBLE);
        }else{
            holder.ItemName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.downloading.setVisibility(View.VISIBLE);
            holder.downloaded.setVisibility(View.GONE);
            holder.failed.setVisibility(View.GONE);
        }
        if (!visualAds.isDownloadStarted() && !visualAds.isFolderYN()) {
            visualAds.setDownloadStarted(true);

            Runnable runnable = new Runnable() {
                public void run() {
                    //new up_down_ftp().setResponseCode(position).download_visual_aids(VisualAdsDownloadAdaptor.this, visualAds.getFileName(),   visualAds.getDirectory());
                    new up_down_ftp().setResponseCode(position).downloadFile(VisualAdsDownloadAdaptor.this, visualAds.getFileName(), visualAds.getDirectory());
                }
            };
            final Thread mythread1 = new Thread(runnable);
            mythread1.start();
            holder.downloading.setVisibility(View.VISIBLE);
            holder.downloaded.setVisibility(View.GONE);
            holder.failed.setVisibility(View.GONE);
            holder.ItemName.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }


    }



    @Override
    public int getItemCount() {
        return  RptdataCopy.size();
    }

    @Override
    public void started(Integer responseCode, String message, String description) {
        //mVisualAds visualAds = RptdataCopy.get(responseCode);
        //Toast.makeText(context,visualAds.getItemName() + " : " + "Download Started....",Toast.LENGTH_LONG).show();
    }

    @Override
    public void progess(Integer responseCode, Long FileSize, Float value, String description) {

    }

    @Override
    public void complete(Integer responseCode, String message, String description) {
        mVisualAds visualAds = RptdataCopy.get(responseCode);
        //Toast.makeText(context,visualAds.getItemName() + " : " + "Downloaded",Toast.LENGTH_LONG).show();
        visualAds.setDownloadCompleted(true);
        notifyItemChanged(responseCode,visualAds);
        filesdownloaded = filesdownloaded + 1;
        if (downloadListener != null){
            downloadListener.onSucess(filesdownloaded,RptdataCopy.size());
        }
    }

    @Override
    public void aborted(Integer responseCode, String message, String description) {
       // mVisualAds visualAds = RptdataCopy.get(responseCode);
       // visualAds.setDownloadFailed(true);
        //Toast.makeText(context,visualAds.getItemName() + " : " + "Downloaded Aborted...",Toast.LENGTH_LONG).show();
    }

    @Override
    public void failed(Integer responseCode, String message, String description) {
        mVisualAds visualAds = RptdataCopy.get(responseCode);
        visualAds.setTrys(visualAds.getTrys()+1);
        if(visualAds.getTrys() < 200) {
            visualAds.setDownloadStarted(false);
            visualAds.setDownloadFailed(false);
            visualAds.setDownloadCompleted(false);

            if (!visualAds.isDownloadStarted() && !visualAds.isFolderYN()) {
                visualAds.setDownloadStarted(true);

                Runnable runnable = new Runnable() {
                    public void run() {
                        //new up_down_ftp().setResponseCode(responseCode).download_visual_aids(VisualAdsDownloadAdaptor.this, visualAds.getFileName(),  visualAds.getDirectory());
                        new up_down_ftp().setResponseCode(responseCode).downloadFile(VisualAdsDownloadAdaptor.this, visualAds.getFileName(), visualAds.getDirectory());
                    }
                };
                final Thread mythread1 = new Thread(runnable);
                mythread1.start();
            }
            notifyItemChanged(responseCode,visualAds);
        }else {
            visualAds.setDownloadFailed(true);
        }
        //Toast.makeText(context,visualAds.getItemName()+"("+visualAds.getTrys() + ") : " + "Downloaded Failed",Toast.LENGTH_SHORT).show();
    }


    public class MyviewHolder extends RecyclerView. ViewHolder{
        TextView ItemName;
        ProgressBar downloading;
        ImageView downloaded,failed;

        LinearLayout lay_add,lay_edit,lay_del,container;

        public MyviewHolder(View convertView) {
            super(convertView);
            ItemName =(TextView)convertView.findViewById(R.id.particulars);
            downloading=(ProgressBar)convertView.findViewById(R.id.downloading);
            downloaded=(ImageView)convertView.findViewById(R.id.downloaded);
            failed=(ImageView)convertView.findViewById(R.id.failed);

           // lay_add=convertView.findViewById(R.id.lay_add);
           /* lay_edit=convertView.findViewById(R.id.lay_edit);
            lay_del=convertView.findViewById(R.id.lay_del);*/
            container=(LinearLayout)convertView.findViewById(R.id.container);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mVisualAds visualAds = RptdataCopy.get(getAdapterPosition());
                    if (visualAds.isDownloadFailed()) {
                        visualAds.setDownloadStarted(false);
                        visualAds.setDownloadFailed(false);
                        visualAds.setDownloadCompleted(false);
                        visualAds.setTrys(0);
                        if (!visualAds.isDownloadStarted() && !visualAds.isFolderYN()) {
                            visualAds.setDownloadStarted(true);

                            Runnable runnable = new Runnable() {
                                public void run() {
                                    //new up_down_ftp().setResponseCode(getAdapterPosition()).download_visual_aids(VisualAdsDownloadAdaptor.this, visualAds.getFileName(),  visualAds.getDirectory());
                                    new up_down_ftp().setResponseCode(getAdapterPosition()).downloadFile(VisualAdsDownloadAdaptor.this, visualAds.getFileName(), visualAds.getDirectory());
                                }
                            };
                            final Thread mythread1 = new Thread(runnable);
                            mythread1.start();
                        }
                        notifyItemChanged(getAdapterPosition(),visualAds);
                    }

                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);

                    }
                }
            });

        }
    }
}
