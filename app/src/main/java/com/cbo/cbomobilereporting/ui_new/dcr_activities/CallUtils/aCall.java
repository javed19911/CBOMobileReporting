package com.cbo.cbomobilereporting.ui_new.dcr_activities.CallUtils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.emp_tracking.DistanceCalculator;

import java.util.ArrayList;
import java.util.Random;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import utils.adapterutils.SpinnerModel;
import utils_new.Custom_Variables_And_Method;

/**
 * Created by cboios on 11/03/19.
 */

public class aCall extends RecyclerView.Adapter<aCall.MyViewHolder> {
    private Context mContext;
    private ArrayList<SpinnerModel> items;
    private ArrayList<SpinnerModel> itemsCopy;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    Boolean show;
    public  String latLong = "";
    String DR_COLOR ="";

    public aCall(Context mContext, ArrayList<SpinnerModel> items,Boolean show) {
        this.mContext = mContext;
        this.items = items;
        itemsCopy = (ArrayList<SpinnerModel>) items.clone();
        this.show=show;
        DR_COLOR = Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(mContext,"DR_COLOR","");
    }

    public ArrayList<SpinnerModel> getItems() {
        return this.items;
    }

    public void update(ArrayList<SpinnerModel> items) {
        this.items = items;
        itemsCopy = (ArrayList<SpinnerModel>) items.clone();
        this.notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_card, parent, false);
        return new MyViewHolder(itemView);
    }

    public SpinnerModel getItemAt(int position){
        return itemsCopy.get(position);
    }

    public void filter(String Qry){
        int textlength = Qry.length();
        itemsCopy.clear();
        for (int i = 0; i < items.size(); i++) {
            if (textlength <= items.get(i).getName().length()) {

                if (items.get(i).getName().toLowerCase().contains(Qry.toLowerCase().trim())) {
                    itemsCopy.add(items.get(i));
                }
            }
        }

        notifyDataSetChanged();

    }

    public void onBindViewHolder(MyViewHolder holder, int position) {


        try {


            SpinnerModel item = itemsCopy.get(position);
            holder.name.setText(item.getName());
            holder.description.setText(item.getAREA());
            holder.character.setText(item.getName().substring(0,1).toUpperCase());

            final Drawable drawable = holder.character.getBackground();
            Random rnd = new Random();
            final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

            latLong = Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(mContext,"shareLatLong",Custom_Variables_And_Method.GLOBAL_LATLON);

            item.setLAT_LONG(latLong);
            if(Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(mContext,"GEO_FANCING_KM","0").equals("0") || !show){
                holder.distance.setVisibility(View.GONE);
            }else if(!item.getAPP_PENDING_YN().equals("0")){
                holder.distance.setText("Additional Area Approval pending...");
                holder.distance.setBackgroundColor(0xffE2571F);
            }else if(item.getLoc().equals("")){

                holder.distance.setText("Registration pending...");
                holder.distance.setBackgroundColor(0xffE2571F);
            }else{
                Double km1=0D,km2=0D,km3=0D;
                km1= DistanceCalculator.distance(Double.valueOf(item.getLoc().split(",")[0]), Double.valueOf(item.getLoc().split(",")[1])
                        ,  Double.valueOf(latLong.split(",")[0]), Double.valueOf(latLong.split(",")[1]), "K");

                if (!item.getLoc2().equals("")) {
                    km2 = DistanceCalculator.distance(Double.valueOf(item.getLoc2().split(",")[0]), Double.valueOf(item.getLoc2().split(",")[1])
                            , Double.valueOf(latLong.split(",")[0]), Double.valueOf(latLong.split(",")[1]), "K");
                }else{
                    km2=km1;
                }

                if (!item.getLoc3().equals("")) {
                    km3 = DistanceCalculator.distance(Double.valueOf(item.getLoc3().split(",")[0]), Double.valueOf(item.getLoc3().split(",")[1])
                            , Double.valueOf(latLong.split(",")[0]), Double.valueOf(latLong.split(",")[1]), "K");
                }else{
                    km3=km1;
                }

                String geo_fancing_km=Custom_Variables_And_Method.getInstance().getDataFrom_FMCG_PREFRENCE(mContext,"GEO_FANCING_KM","0");

                Double km=getShortestDistance(item,km1,km2,km3);

                if (km>Double.valueOf(geo_fancing_km)){
                    holder.distance.setText(String.format("%.2f", km) +" Km Away");
                    holder.distance.setBackgroundColor(0xffE2921F);
                }else{
                    holder.distance.setText("Within Range");
                    holder.distance.setBackgroundColor(0xff7fbf7f);
                }

            }


            if (item.getCOLORYN().equals("0")){
                holder.name.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            }else{
                if(DR_COLOR.equals("")) {
                    holder.name.setTextColor(0xFFF9BA22);
                }else{
                    holder.name.setTextColor(Color.parseColor(DR_COLOR));
                }
            }



            if (item.getCALLYN().equals("0")){
                //row.setBackgroundColor(0xFFFFFFFF);
                if (item.getAPP_PENDING_YN().equals("0")){
                    holder.card_view.setBackgroundColor(0xFFFFFFFF);
                    if (!show){
                        holder.distance.setVisibility(View.GONE);
                    }
                }else{
                    holder.distance.setText("Additional Area Approval pending...");
                    holder.distance.setBackgroundColor(0xffE2571F);
                    holder.card_view.setBackgroundColor(0xffE2571F);
                    holder.name.setTextColor(0xFFFFFFFF);
                    holder.distance.setVisibility(View.VISIBLE);

                }
                holder.distance.setTextColor(0xFFFFFFFF);
            }else{
                holder.distance.setText("Call Done.");
                holder.distance.setBackgroundColor(0xFFeaeaea);
                holder.distance.setTextColor(0x44000000);
                holder.card_view.setBackgroundColor(0xFFeaeaea);
            }


            if (!item.getPANE_TYPE().equals("1")) {
                //row.setBackgroundResource(R.color.colorPrimaryDark);
                if (item.getCOLORYN().equals("0")) {
                    holder.name.setTextColor(0xFF187823);
                } else{
                    if(DR_COLOR.equals("")) {
                        holder.name.setTextColor(0xFFF9BA22);
                    }else{
                        holder.name.setTextColor(Color.parseColor(DR_COLOR));
                    }
                }
            }/*else{
                    //row.setBackgroundResource(R.color.colorPrimaryDark);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        label.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark,null));
                    }else{
                        label.setTextColor(activity.getResources().getColor(R.color.colorPrimaryDark));
                    }
                    //label.setTextColor(0xFF000000);
                }*/


        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener) {
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }

    public int getItemCount() {
        return this.itemsCopy.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView distance;
        public TextView character;
        public TextView description;
        public LinearLayout card_view;

        public MyViewHolder(View view) {
            super(view);
            this.card_view = view.findViewById(R.id.card_view);
            this.name = view.findViewById(R.id.name);
            this.distance = view.findViewById(R.id.distance);
            this.character = view.findViewById(R.id.character);
            this.description = view.findViewById(R.id.description);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
                    }
                }
            });

        }
    }

    private Double getShortestDistance(SpinnerModel item ,Double km1,Double km2,Double km3){

        if (km1<=km2 && km1<=km3)  {
            item.setREF_LAT_LONG( item.getLoc());
            return km1;
        }if (km2<=km3){
            item.setREF_LAT_LONG( item.getLoc2());
            return km2;
        }

        item.setREF_LAT_LONG( item.getLoc3());
        return km3;
    }
}
