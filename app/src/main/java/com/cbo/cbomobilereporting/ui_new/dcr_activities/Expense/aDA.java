package com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import saleOrder.Adaptor.ClientAdapter;
import utils_new.interfaces.RecycleViewOnItemClickListener;

public class aDA  extends RecyclerView.Adapter<aDA.MyViewHolder>{
    private Context mContext;
    private ArrayList<mDA> DA_Types;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.spin_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(DA_Types.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return DA_Types.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        LinearLayout distanceLayout;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.spin_name);
            distanceLayout =  view.findViewById(R.id.distanceLayout);
            distanceLayout.setVisibility(View.GONE);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
                    }
                }
            });
        }
    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    public aDA(Context mContext, ArrayList<mDA> DA_Types) {
        this.mContext = mContext;
        this.DA_Types = DA_Types;
    }

}
