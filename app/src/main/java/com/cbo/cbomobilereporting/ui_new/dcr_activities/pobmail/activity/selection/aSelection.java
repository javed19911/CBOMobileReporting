package com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.activity.selection;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.pobmail.fragment.pending.mStockist;

import java.util.ArrayList;
import java.util.Random;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;

public class aSelection extends RecyclerView.Adapter<aSelection.MyViewHolder> {

private Context mContext;
private ArrayList<mStockist> stockist;
private ArrayList<mStockist> copyStockist;
        RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

public class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView name, headQtr,character;

    public MyViewHolder(View view) {
        super(view);
        name = (TextView) view.findViewById(R.id.name);
        headQtr = (TextView) view.findViewById(R.id.headqtr);
        character = view.findViewById(R.id.character);


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


    public aSelection(Context mContext, ArrayList<mStockist> stockist) {
        this.mContext = mContext;
        this.stockist = stockist;
        copyStockist = (ArrayList<mStockist>) stockist.clone();
    }


    public ArrayList<mStockist> getParties(){
        return stockist;
    }

    public void update(ArrayList<mStockist> parties) {
        this.stockist = parties;
        copyStockist = (ArrayList<mStockist>) parties.clone();
        notifyDataSetChanged();
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.selection_card, parent, false);

        return new MyViewHolder(itemView);
    }

    public mStockist getPartyAt(int position){
        return copyStockist.get(position);
    }

    public void filter(String Qry){
        int textlength = Qry.length();
        copyStockist.clear();
        for (int i = 0; i < stockist.size(); i++) {
            if (textlength <= stockist.get(i).getNAME().length()) {

                if (stockist.get(i).getNAME().toLowerCase().contains(Qry.toLowerCase().trim())) {
                    copyStockist.add(stockist.get(i));
                }
            }
        }

        notifyDataSetChanged();

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        mStockist item = copyStockist.get(position);
        holder.name.setText(item.getNAME());
        holder.headQtr.setVisibility(View.GONE);

        final Drawable drawable = holder.character.getBackground();
        Random rnd = new Random();
        final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
        drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return copyStockist.size();
    }
}
