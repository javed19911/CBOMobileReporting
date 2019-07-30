package com.cbo.cbomobilereporting.ui_new.dcr_activities.ExpenseNew;

/* public  class ExpenseAdapterNew   {
}*/

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Map;

import utils.adapterutils.Expenses_Adapter;

public class ExpenseAdapterNew extends RecyclerView.Adapter<ExpenseAdapterNew.MyviewHolder>{

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Mexpenses> dataList= new ArrayList<Mexpenses>();
    private Expense_interface expense_interface;

    public interface Expense_interface {
        public void Edit_Expense(String who, String hed, String amt, String rem, final String path, String hed_id);
        public void delete_Expense(String hed_id);
    }

    public ExpenseAdapterNew(Context context, ArrayList<Mexpenses> data) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dataList = data;
       // expense_interface= (Expense_interface) context ;
    }



    @Override
    public ExpenseAdapterNew.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exp_row, parent, false);

        return new ExpenseAdapterNew.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {


       Mexpenses mexpenses = dataList.get(position);
        holder.exp_hed.setText(mexpenses.getExp_head());
        holder.exp_remak.setText(mexpenses.getRemark());
        holder.exp_amt.setText(mexpenses.getDA_amt());


        holder.edit_exp.setVisibility(View.VISIBLE);
        holder.delete_exp.setVisibility(View.VISIBLE);


      /*  holder.exp_hed.setText(dataList.get(position).get("exp_head"));
        holder.exp_amt.setText(dataList.get(position).get("amount"));
        holder.exp_remak.setText(dataList.get(position).get("remark"));*/

        if (!mexpenses.getFilename().equals("")){
            holder.attachment.setVisibility(View.VISIBLE);
        }else{
            holder.attachment.setVisibility(View.GONE);
        }

        if (mexpenses.getExp_head().equalsIgnoreCase("-1")){
            holder.edit_exp.setVisibility(View.GONE);
            holder.delete_exp.setVisibility(View.GONE);
        }else{
            holder.edit_exp.setVisibility(View.VISIBLE);
            holder.delete_exp.setVisibility(View.VISIBLE);
        }

        holder.edit_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                expense_interface.Edit_Expense("1",dataList.get(position).getExp_head(),dataList.get(position).getDA_amt(),
                        dataList.get(position).getRemark(),dataList.get(position).getFilename(),dataList.get(position).getExp_head_id());
            }
        });

        holder.delete_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense_interface.delete_Expense(dataList.get(position).getExp_head_id());
            }
        });

        return ;


    }


    @Override
    public int getItemCount() {
        return  dataList.size();
    }


    public class MyviewHolder extends RecyclerView. ViewHolder{
        TextView exp_hed,exp_amt,exp_remak;
        ImageView edit_exp,delete_exp,attachment;


        public MyviewHolder(View convertView) {
            super(convertView);


            exp_hed=(TextView) convertView.findViewById(R.id.tv_exp_id);
            exp_amt=(TextView)convertView.findViewById(R.id.tv_amt_id);
            exp_remak=(TextView)convertView.findViewById(R.id.tv_rem_id);
            edit_exp=(ImageView) convertView.findViewById(R.id.edit_exp);
            delete_exp=(ImageView)convertView.findViewById(R.id.delete_exp);
            attachment=(ImageView) convertView.findViewById(R.id.attach);




        }
    }
}
