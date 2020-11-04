package com.cbo.cbomobilereporting.ui_new.dcr_activities.WorkWith;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Random;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import utils.adapterutils.Dcr_Workwith_Model;

/**
 * Created by cboios on 11/03/19.
 */

public class aWorkWith extends RecyclerView.Adapter<aWorkWith.MyViewHolder> {
    private Context mContext;
    private ArrayList<Dcr_Workwith_Model> items;
    private ArrayList<Dcr_Workwith_Model> selecteditems;
    private ArrayList<Dcr_Workwith_Model> itemsCopy;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private Boolean updating = false;

    Boolean showIndependent = false;

    public aWorkWith(Context mContext, ArrayList<Dcr_Workwith_Model> items,ArrayList<Dcr_Workwith_Model> preitems, Boolean showIndependent) {
        this.mContext = mContext;
        this.items = items;
        this.selecteditems = preitems;
        itemsCopy = (ArrayList<Dcr_Workwith_Model>) items.clone();
        this.showIndependent=showIndependent;
    }

    public void update(ArrayList<Dcr_Workwith_Model> items) {
        this.items = items;
        itemsCopy = (ArrayList<Dcr_Workwith_Model>) items.clone();
        this.notifyDataSetChanged();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_with_card, parent, false);
        return new MyViewHolder(itemView);
    }

    public Dcr_Workwith_Model getItemAt(int position){
        return itemsCopy.get(position);
    }

    public ArrayList<Dcr_Workwith_Model> getItems(){
        return items;
    }
    public ArrayList<Dcr_Workwith_Model> getSelectedItems(){
        return selecteditems;
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

            updating = true;

            Dcr_Workwith_Model item = itemsCopy.get(position);
            holder.name.setText(item.getName());
            //holder.description.setText(item.getAREA());
            holder.character.setText(item.getName().substring(0,1).toUpperCase());

            final Drawable drawable = holder.character.getBackground();
            Random rnd = new Random();
            final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
            drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

            holder.selected.setChecked(GetOrderItemWhere(item) != null);
           /* Dcr_Workwith_Model selectedItem = GetOrderItemWhere(item);
            if (selectedItem!= null) {
                holder.selected.setChecked(selectedItem.isSelected());
            }else {
                holder.selected.setChecked(false);
            }*/



            updating = false;

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
        public CheckBox selected;
        public CheckBox independent;
        public TextView character;
        public TextView description;
        public LinearLayout card_view;

        public MyViewHolder(View view) {
            super(view);
            this.card_view = view.findViewById(R.id.card_view);
            this.name = view.findViewById(R.id.name);
            this.selected = view.findViewById(R.id.selected);
            this.character = view.findViewById(R.id.character);
            this.description = view.findViewById(R.id.description);

            selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!updating) {
                        Dcr_Workwith_Model model = itemsCopy.get(getAdapterPosition());
                        model.setSelected(isChecked);
                        addItem(model);
                    }
                }
            });
            /*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
                    }
                }
            });*/

        }
    }

    private Dcr_Workwith_Model GetOrderItemWhere(Dcr_Workwith_Model item){

        if (selecteditems.size() > 0) {

            for (Dcr_Workwith_Model selectedItem : selecteditems) {
                if (selectedItem.getId().equals(item.getId()) &&
                        selectedItem.getName().equals(item.getName())) {
                    return selectedItem;
                }
            }
        }
        return null;
    }

    public void addItem(Dcr_Workwith_Model item){
        Dcr_Workwith_Model selectedItem = GetOrderItemWhere(item);
        if (selectedItem != null){
            selecteditems.remove(selectedItem);
        }
        if (item.isSelected()) {
            selecteditems.add(item);
        }

    }

}
