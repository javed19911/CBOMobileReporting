package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import saleOrder.Adaptor.ClientAdapter;

public class aExpense extends RecyclerView.Adapter<aExpense.MyViewHolder>  {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mOthExpense> othExpenses = new ArrayList<>();
    eExpense exp_type = eExpense.None;

    private Expense_interface expense_interface;

    public interface Expense_interface {
        public void Edit_Expense(mOthExpense othExpense,eExpense exp_type);
        public void delete_Expense(mOthExpense othExpense,eExpense exp_type);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView exp_hed,exp_amt,exp_remak;
        ImageView edit_exp,delete_exp,attachment;

        public MyViewHolder(View view) {
            super(view);
            exp_hed=(TextView) view.findViewById(R.id.tv_exp_id);
            exp_amt=(TextView) view.findViewById(R.id.tv_amt_id);
            exp_remak=(TextView) view.findViewById(R.id.tv_rem_id);
            edit_exp=(ImageView) view.findViewById(R.id.edit_exp);
            delete_exp=(ImageView) view.findViewById(R.id.delete_exp);
            attachment=(ImageView) view.findViewById(R.id.attach);



            edit_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expense_interface.Edit_Expense(othExpenses.get(getAdapterPosition()),exp_type);
                }
            });

            delete_exp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expense_interface.delete_Expense(othExpenses.get(getAdapterPosition()),exp_type);
                }
            });
        }
    }


    public aExpense(Context context,ArrayList<mOthExpense> othExpenses,eExpense exp_type){
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.othExpenses = othExpenses;
        this.exp_type = exp_type;
        expense_interface= (Expense_interface) context ;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exp_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        mOthExpense item = othExpenses.get(position);
        holder.exp_hed.setText(item.getExpHead().getName());
        holder.exp_amt.setText(""+item.getAmount());
        holder.exp_remak.setText(item.getRemark());
        holder.edit_exp.setVisibility(View.VISIBLE);
        holder.delete_exp.setVisibility(View.VISIBLE);

        if (!othExpenses.get(position).getAttachment().equals("")){
            holder.attachment.setVisibility(View.VISIBLE);
        }else{
            holder.attachment.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return othExpenses.get(position).getId();
    }

    @Override
    public int getItemCount() {
       return othExpenses.size();
    }


}