package com.cbo.cbomobilereporting.ui_new.dcr_activities.Location;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import utils_new.interfaces.RecycleViewOnItemClickListener;


public class aCentroidLocation extends RecyclerView.Adapter<aCentroidLocation.MyViewHolder> {

    private Context context;
    private ArrayList<mCentroidLocation> locations = new ArrayList<>();
    private RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.centroid_location_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (position!=0) {
            holder.name.setText(locations.get(position-1).getLatLong());
            holder.km.setText(String.format("%.2f", locations.get(position-1).getKm()));
            if (locations.get(position-1).getAddress() != null){
                holder.address.setVisibility(View.VISIBLE);
                holder.address.setText(locations.get(position-1).getAddress().getFORMATED_ADDRESS());
            }else{
                holder.address.setVisibility(View.GONE);
            }
        }else{
            holder.name.setText("Location");
            holder.km.setText("Km(m)");
            holder.address.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return locations.size()+1;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,km,address;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.name);
            km =  view.findViewById(R.id.km);
            address =  view.findViewById(R.id.address);



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

    public aCentroidLocation(Context mContext, ArrayList<mCentroidLocation> locations) {
        this.context = mContext;
        this.locations = locations;
    }
}
