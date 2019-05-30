package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

public class aExpense extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mOthExpense> othExpenses = new ArrayList<>();

    private Expense_interface expense_interface;

    public interface Expense_interface {
        public void Edit_Expense(mOthExpense othExpense);
        public void delete_Expense(mOthExpense othExpense);
    }
    public aExpense(Context context,ArrayList<mOthExpense> othExpenses){
        this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.othExpenses = othExpenses;
        expense_interface= (Expense_interface) context ;

    }

    @Override
    public int getCount() {
        return othExpenses.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.exp_row,null);

            holder.exp_hed=(TextView) convertView.findViewById(R.id.tv_exp_id);
            holder.exp_amt=(TextView) convertView.findViewById(R.id.tv_amt_id);
            holder.exp_remak=(TextView) convertView.findViewById(R.id.tv_rem_id);
            holder.edit_exp=(ImageView) convertView.findViewById(R.id.edit_exp);
            holder.delete_exp=(ImageView) convertView.findViewById(R.id.delete_exp);
            holder.attachment=(ImageView) convertView.findViewById(R.id.attach);

            convertView.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }



        holder.exp_hed.setText(othExpenses.get(position).getExpHead().getName());
        holder.exp_amt.setText(""+othExpenses.get(position).getAmount());
        holder.exp_remak.setText(othExpenses.get(position).getRemark());
        holder.edit_exp.setVisibility(View.VISIBLE);
        holder.delete_exp.setVisibility(View.VISIBLE);

        if (!othExpenses.get(position).getAttachment().equals("")){
            holder.attachment.setVisibility(View.VISIBLE);
        }else{
            holder.attachment.setVisibility(View.GONE);
        }

        holder.edit_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense_interface.Edit_Expense(othExpenses.get(position));
            }
        });

        holder.delete_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expense_interface.delete_Expense(othExpenses.get(position));
            }
        });

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    public class ViewHolder{

        TextView exp_hed,exp_amt,exp_remak;
        ImageView edit_exp,delete_exp,attachment;

    }
}