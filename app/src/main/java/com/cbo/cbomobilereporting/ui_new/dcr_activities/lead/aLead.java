package com.cbo.cbomobilereporting.ui_new.dcr_activities.lead;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;
import java.util.Random;

import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import utils.adapterutils.PobModel;

/**
 * Created by cboios on 11/03/19.
 */

public class aLead extends RecyclerView.Adapter {
    private Context mContext;
    private ArrayList<PobModel> items;
    private SamplePOBBuilder samplePOBBuilder;
    private ArrayList<PobModel> itemsCopy;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private Boolean updating = false;

    public aLead(Context mContext, ArrayList<PobModel> items, SamplePOBBuilder builder) {
        this.mContext = mContext;
        this.items = items;
        this.samplePOBBuilder = builder;
        itemsCopy = (ArrayList<PobModel>) items.clone();
    }

    public void update(ArrayList<PobModel> items) {
        this.items = items;
        itemsCopy = (ArrayList<PobModel>) items.clone();
        this.notifyDataSetChanged();
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (samplePOBBuilder.getType()){
            case POB:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.pob_card, parent, false);
                return new POBViewHolder(itemView);
            case GIFT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_card, parent, false);
                return new GiftViewHolder(itemView);
            case LEAD:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_with_card, parent, false);
                return new LeadViewHolder(itemView);
        }

        return null;
    }


   /* @Override
    public int getItemViewType(int position) {

        switch (samplePOBBuilder.getType()) {
            case POB:
                return Model.TEXT_TYPE;
            case 1:
                return Model.IMAGE_TYPE;
            case 2:
                return Model.AUDIO_TYPE;
            default:
                return -1;
        }
    } */


    public PobModel getItemAt(int position){
        return itemsCopy.get(position);
    }

    public ArrayList<PobModel> getItems(){
        return items;
    }
    public ArrayList<PobModel> getSelectedItems(){
        return samplePOBBuilder.getItems();
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

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        //updating = true;

        try {


            PobModel item = itemsCopy.get(position);
            Random rnd = new Random();
            final int[] color = {Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))};
            Drawable drawable = null;

            switch (samplePOBBuilder.getType()){
                case POB:

                    ((POBViewHolder) holder).name.setText(item.getName());
                    //holder.description.setText(item.getAREA());
                    ((POBViewHolder) holder).character.setText(item.getName().substring(0,1).toUpperCase());

                    drawable =  ((POBViewHolder) holder).character.getBackground();

                    drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

                    ((POBViewHolder) holder).selected.setChecked(GetOrderItemWhere(item) != null);

                    break;
                case GIFT:

                    ((GiftViewHolder) holder).name.setText(item.getName());
                    //holder.description.setText(item.getAREA());
                    ((GiftViewHolder) holder).character.setText(item.getName().substring(0,1).toUpperCase());

                    drawable =  ((GiftViewHolder) holder).character.getBackground();


                    drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

                    PobModel selecteditem = GetOrderItemWhere(item);
                    if (selecteditem != null) {
                        ((GiftViewHolder) holder).Qty.setText(selecteditem.getScore());
                    }else{
                        ((GiftViewHolder) holder).Qty.setText("");
                    }
                    break;
                case LEAD:

                    ((LeadViewHolder) holder).name.setText(item.getName());
                    //holder.description.setText(item.getAREA());
                    ((LeadViewHolder) holder).character.setText(item.getName().substring(0,1).toUpperCase());

                    drawable =  ((LeadViewHolder) holder).character.getBackground();

                    drawable.setColorFilter(color[0], PorterDuff.Mode.SRC_IN);

                    ((LeadViewHolder) holder).selected.setChecked(GetOrderItemWhere(item) != null);
                    break;
            }




        }catch(Exception e){
            e.printStackTrace();
        }
        //updating = false;
    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener) {
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }

    public int getItemCount() {
        return this.itemsCopy.size();
    }

    public class LeadViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CheckBox selected;
        public CheckBox independent;
        public TextView character;
        public TextView description;
        public LinearLayout card_view;

        public LeadViewHolder(View view) {
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
                        PobModel model = itemsCopy.get(getAdapterPosition());
                        model.setSelected(isChecked);
                        addItem(model);
                    }
                }
            });

        }
    }

    public class POBViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public CheckBox selected;
        public CheckBox independent;
        public TextView character;
        public TextView description;
        public LinearLayout card_view,sampleLayout;
        public EditText saprator;
        public EditText sample;
        public EditText pob;
        public EditText noc;


        public POBViewHolder(View view) {
            super(view);
            this.card_view = view.findViewById(R.id.card_view);
            this.name = view.findViewById(R.id.name);
            this.selected = view.findViewById(R.id.selected);
            this.character = view.findViewById(R.id.character);
            this.description = view.findViewById(R.id.description);
            this.saprator = view.findViewById(R.id.saprator);
            this.independent = view.findViewById(R.id.independent);
            this.sample = view.findViewById(R.id.sample);
            this.pob = view.findViewById(R.id.pob);
            this.noc = view.findViewById(R.id.noc);
            this.sampleLayout = view.findViewById(R.id.sampleLayout);


            selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (!updating) {
                        PobModel model = itemsCopy.get(getAdapterPosition());
                        model.setSelected(isChecked);
                        model.setPob("0");
                        model.setNOC("0");
                        model.setScore("0");
                        if (!isChecked){

                            saprator.setVisibility(View.GONE);
                            sampleLayout.setVisibility(View.GONE);
                        }else{
                            saprator.setVisibility(View.VISIBLE);
                            sampleLayout.setVisibility(View.VISIBLE);
                        }
                        addItem(model);
                    }
                }
            });

            sample.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    PobModel model = itemsCopy.get(getAdapterPosition());
                    model.setScore(s.toString().isEmpty()?"0":s.toString());
                    addItem(model);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }


    public class GiftViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public EditText Qty;
        public TextView character;
        public TextView description;
        public LinearLayout card_view;

        public GiftViewHolder(View view) {
            super(view);
            this.card_view = view.findViewById(R.id.card_view);
            this.name = view.findViewById(R.id.name);
            this.character = view.findViewById(R.id.character);
            this.description = view.findViewById(R.id.description);
            this.Qty = view.findViewById(R.id.qty);


            Qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    PobModel model = itemsCopy.get(getAdapterPosition());
                    model.setScore(s.toString().isEmpty()?"0":s.toString());
                    model.setSelected(!s.toString().isEmpty());
                    addItem(model);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }
    }

    private PobModel GetOrderItemWhere(PobModel item){

        if (samplePOBBuilder.getItems().size() > 0) {

            for (PobModel selectedItem : samplePOBBuilder.getItems()) {
                if (selectedItem.getId().equals(item.getId()) &&
                        selectedItem.getName().equals(item.getName())) {
                    return selectedItem;
                }
            }
        }
        return null;
    }

    public void addItem(PobModel item){
        PobModel selectedItem = GetOrderItemWhere(item);
        if (selectedItem != null){
            samplePOBBuilder.getItems().remove(selectedItem);
        }
        if (item.isSelected()) {
            samplePOBBuilder.getItems().add(item);
        }

    }

}
