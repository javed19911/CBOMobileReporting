package com.cbo.cbomobilereporting.ui_new.dcr_activities.ExpenseNew;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

public class ExpenseAdapterTa  extends RecyclerView.Adapter<ExpenseAdapterTa.MyviewHolder>  {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<MexpensesTA> talist= new ArrayList<MexpensesTA>();

    Expense_interface expense_interface;

    public interface Expense_interface {
        public void Edit_Expense(String who, String hed, String amt, String rem, final String path, String hed_id);
        public void delete_Expense(String hed_id);
    }

    public ExpenseAdapterTa(Context context, ArrayList<MexpensesTA> data) {
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        talist = data;
        // expense_interface= (Expense_interface) context ;
    }



    @Override
    public ExpenseAdapterTa.MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exp_row, parent, false);

        return new ExpenseAdapterTa.MyviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapterTa.MyviewHolder holder, int position) {


        MexpensesTA mexpensesTA = talist.get(position);
        holder.exp_hed.setText(mexpensesTA.getTa_exp_head());
        holder.exp_remak.setText(mexpensesTA.getTa_remark());
        holder.exp_amt.setText(mexpensesTA.getTa_amt());


        holder.edit_exp.setVisibility(View.VISIBLE);
        holder.delete_exp.setVisibility(View.VISIBLE);


      /*  holder.exp_hed.setText(talist.get(position).get("exp_head"));
        holder.exp_amt.setText(talist.get(position).get("amount"));
        holder.exp_remak.setText(talist.get(position).get("remark"));*/

        if (!mexpensesTA.getTa_filename().equals("")){
            holder.attachment.setVisibility(View.VISIBLE);
        }else{
            holder.attachment.setVisibility(View.GONE);
        }

        if (mexpensesTA.getTa_exp_head().equalsIgnoreCase("-1")){
            holder.edit_exp.setVisibility(View.GONE);
            holder.delete_exp.setVisibility(View.GONE);
        }else{
            holder.edit_exp.setVisibility(View.VISIBLE);
            holder.delete_exp.setVisibility(View.VISIBLE);
        }

        holder.edit_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                expense_interface.Edit_Expense("1",talist.get(position).getTa_exp_head(),talist.get(position).getTa_amt(),
                        talist.get(position).getTa_remark(),talist.get(position).getTa_filename(),talist.get(position).getTa_exp_head_id());
            }
        });

        holder.delete_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    expense_interface.delete_Expense(talist.get(position).getExp_head_id());
            }
        });

        return ;


    }


    @Override
    public int getItemCount() {
        return  talist.size();
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
