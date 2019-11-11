package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

public class aChemist extends RecyclerView.Adapter<aChemist.PendingViewHolder> {
    RecyclerStokistListner recycleViewOnItemClickListener = null;
    Boolean ShowParty = false;
    private Context mContext;
    private ArrayList<mChemist> items;

    public aChemist(Context mContext, ArrayList<mChemist> items) {
        this.mContext = mContext;
        this.items = items;
    }

    public aChemist(Context mContext, ArrayList<mChemist> items, Boolean ShowParty) {
        this.mContext = mContext;
        this.items = items;
        this.ShowParty = ShowParty;
    }

    public ArrayList<mChemist> getItems() {
        return this.items;
    }

    public void update(ArrayList<mChemist> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }


    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_view_list, parent, false);
        return new PendingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {

        holder.onBindData(position);
    }

    public void setOnClickListner(RecyclerStokistListner recycleViewOnItemClickListener) {
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }

    public int getItemCount() {
        return this.items.size();
    }


    public class PendingViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvAmount;
        private TextView tvSelection;

        public PendingViewHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.tvTitle);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvSelection = view.findViewById(R.id.tvSelection);

        }

        public void onBindData(int position) {
            tvTitle.setText(items.get(position).getNAME());
            tvAmount.setText(""+items.get(position).getAmount());

            if (items.get(position).getSelectedStokist() != null) {
                tvSelection.setText(items.get(position).getSelectedStokist().getNAME());
            }

            tvSelection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recycleViewOnItemClickListener.onClick(view, position, true, items);
                }
            });
        }
    }

}
