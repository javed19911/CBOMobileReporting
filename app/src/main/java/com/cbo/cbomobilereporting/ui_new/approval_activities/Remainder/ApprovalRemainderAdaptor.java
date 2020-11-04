package com.cbo.cbomobilereporting.ui_new.approval_activities.Remainder;

import android.content.Context;

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

/**
 * Created by cboios on 20/11/18.
 */

public class ApprovalRemainderAdaptor extends RecyclerView.Adapter<ApprovalRemainderAdaptor.MyviewHolder> {


    Context context;
    ArrayList<mApprovalRemainder> Rptdata=new ArrayList<mApprovalRemainder>();
    ArrayList<mApprovalRemainder> RptdataCopy=new ArrayList<mApprovalRemainder>();
    Custom_Variables_And_Method customVariablesAndMethod;
    CBO_DB_Helper cbohelp;
    private RecycleViewOnItemClickListener recycleViewOnItemClickListener;


    public ApprovalRemainderAdaptor(Context mcontext, ArrayList<mApprovalRemainder> data, RecycleViewOnItemClickListener recycleViewOnItemClickListener) {
        this.context = mcontext;
        this.Rptdata = data;
        RptdataCopy = (ArrayList<mApprovalRemainder>) data.clone();
        customVariablesAndMethod= Custom_Variables_And_Method.getInstance(context);
        cbohelp = customVariablesAndMethod.get_cbo_db_instance();
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }



    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.remainder_approval_card, parent, false);

        return new MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        mApprovalRemainder rptmodel = RptdataCopy.get(position);
        holder.particulars.setText(rptmodel.getPARICULARS());
        holder.add_value.setText(rptmodel.getADD_VALUE());
       /* holder.edit_value.setText(rptmodel.getEDIT_VALUE());
        holder.delete_value.setText(rptmodel.getDELETE_VALUE());

        if (rptmodel.getDELETE_VALUE().equalsIgnoreCase("0")){
            holder.lay_del.setVisibility(View.GONE);
        }else{
            holder.lay_del.setVisibility(View.VISIBLE);
        }

        if (rptmodel.getEDIT_VALUE().equalsIgnoreCase("0")){
            holder.lay_edit.setVisibility(View.GONE);
        }else{
            holder.lay_edit.setVisibility(View.VISIBLE);
        }


        if (rptmodel.getADD_VALUE().equalsIgnoreCase("0")){
            holder.lay_add.setVisibility(View.GONE);
        }else{
            holder.lay_add.setVisibility(View.VISIBLE);
        }*/
    }



    @Override
    public int getItemCount() {
        return  RptdataCopy.size();
    }


    public class MyviewHolder extends RecyclerView. ViewHolder{
        TextView particulars;
        TextView edit_value,add_value,delete_value;
        TextView status;
        LinearLayout lay_add,lay_edit,lay_del,container;

        public MyviewHolder(View convertView) {
            super(convertView);
            particulars=(TextView)convertView.findViewById(R.id.particulars);
            add_value=(TextView)convertView.findViewById(R.id.add_value);
          /*  edit_value=(TextView)convertView.findViewById(R.id.edit_value);
            delete_value=(TextView)convertView.findViewById(R.id.delete_value);*/

            lay_add=convertView.findViewById(R.id.lay_add);
           /* lay_edit=convertView.findViewById(R.id.lay_edit);
            lay_del=convertView.findViewById(R.id.lay_del);*/
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


}
