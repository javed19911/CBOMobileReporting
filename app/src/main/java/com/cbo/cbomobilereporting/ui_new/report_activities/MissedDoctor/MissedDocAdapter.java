package com.cbo.cbomobilereporting.ui_new.report_activities.MissedDoctor;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.databaseHelper.CBO_DB_Helper;


import java.util.ArrayList;

import utils_new.Custom_Variables_And_Method;
import utils_new.interfaces.RecycleViewOnItemClickListener;

public class MissedDocAdapter  extends RecyclerView.Adapter<MissedDocAdapter.MyviewHolder>{

    Context context;
    ArrayList<mMissedGrid> Rptdata=new ArrayList<mMissedGrid>();
    ArrayList<mMissedGrid> RptdataCopy=new ArrayList<mMissedGrid>();
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    private RecycleViewOnItemClickListener recycleViewOnItemClickListener;


    public MissedDocAdapter(Context mcontext, ArrayList<mMissedGrid> data, RecycleViewOnItemClickListener recycleViewOnItemClickListener) {
        this.context = mcontext;
        this.Rptdata = data;
        RptdataCopy = (ArrayList<mMissedGrid>) data.clone();
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance(context);
        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }



    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.missed_call_doc_raw, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        mMissedGrid rptmodel = RptdataCopy.get(position);
        holder.name.setText(rptmodel.getName());
        holder.code.setText(rptmodel.getCode());
        holder.status.setText(rptmodel.getStatus());

        holder.name.setTextColor( Color.parseColor( rptmodel.getColor()));
        holder.code.setTextColor( Color.parseColor(rptmodel.getColor()));
        holder.status.setTextColor( Color.parseColor( rptmodel.getColor()));
        holder.container.setBackgroundColor(Color.parseColor(rptmodel.getBG_COLOR()));
    }



    @Override
    public int getItemCount() {
        return  RptdataCopy.size();
    }


    public class MyviewHolder extends RecyclerView. ViewHolder{
        TextView name;
        TextView code;
        TextView status;
        LinearLayout container;

        public MyviewHolder(View convertView) {
            super(convertView);
            name=(TextView)convertView.findViewById(R.id.name);
            code=(TextView)convertView.findViewById(R.id.code);
            status=(TextView)convertView.findViewById(R.id.status);
            container=(LinearLayout)convertView.findViewById(R.id.container);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                    }
                }
            });
        }
    }

    public void filter(String Query){
        RptdataCopy.clear();
        if (Query.trim().equals("")){
            RptdataCopy = (ArrayList<mMissedGrid>) Rptdata.clone();
            notifyDataSetChanged();
            return;
        }

        for (mMissedGrid item : Rptdata){
            if (item.getName().toLowerCase().contains(Query.toLowerCase())){
                RptdataCopy.add(item);
            }
        }
        notifyDataSetChanged();
    }


}
