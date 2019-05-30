package saleOrder.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Adaptor.MedicineAdapter;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Enum.eItem;


public class ItemAdapter extends RecyclerView.Adapter  {

    private Context mContext;
    private ArrayList<mItem> items;
    private eItem itemType = eItem.MEDICINE;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        public TextView item, brand,item_price,brand_price,manufacturer,pack,pescription_yn;
        private AddToCartView add_to_cart;

        public MedicineViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.item);
            add_to_cart = view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.add_to_cart);
            //brand = (TextView) view.findViewById(R.id.brand);
            item_price = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.item_price);
            manufacturer = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.manufacturer);
            pack = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.pack);
            //brand_price = (TextView) view.findViewById(R.id.brand_price);
            pescription_yn = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.pescription_yn);
            pescription_yn.setVisibility(View.GONE);


            add_to_cart.setOnValueChangeListner(new AddToCartView.OnValueChangeListener() {
                @Override
                public void OnValueChanged(View view, int value) {
                    items.get(getAdapterPosition()).setQty(Double.parseDouble( ""+add_to_cart.getValue()));
                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                    }
                }
            });
        }
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView item, brand,item_price,brand_price,manufacturer,pack,pescription_yn;
        private AddToCartView add_to_cart;
        private ImageView itemImg;

        public ProductViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.item);
            add_to_cart = view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.add_to_cart);
            //brand = (TextView) view.findViewById(R.id.brand);
            item_price = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.item_price);
            manufacturer = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.manufacturer);
            pack = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.pack);
            //brand_price = (TextView) view.findViewById(R.id.brand_price);
            pescription_yn = (TextView) view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.pescription_yn);
            itemImg = view.findViewById(cbomobilereporting.cbo.com.cboorder.R.id.itemImg);

            itemImg.setVisibility(View.GONE);
            pescription_yn.setVisibility(View.GONE);
            add_to_cart.setVisibility(View.GONE);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                    }
                }
            });
        }
    }




    public ItemAdapter(Context mContext, ArrayList<mItem> items,eItem itemType) {
        this.mContext = mContext;
        this.items = items;
        this.itemType = itemType;
    }


    public ArrayList<mItem> getItems(){
        return items;
    }

    public void update(ArrayList<mItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (itemType){
            case PRODUCT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card, parent, false);
                return new ProductViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicine_card, parent, false);
                return new MedicineViewHolder(itemView);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        mItem item = items.get(position);
        switch (itemType){
            case PRODUCT:

                ((ProductViewHolder) holder).item.setText(item.getName());
                ((ProductViewHolder) holder).item_price.setText(AddToCartView.toCurrency( String.format("%.2f", item.getRate())));
                ((ProductViewHolder) holder).manufacturer.setText("");
                ((ProductViewHolder) holder).pack.setText(item.getPack());
                ((ProductViewHolder) holder).add_to_cart.setValue(Integer.parseInt(""+String.format("%.0f",item.getQty())));


                break;
            default:

                ((MedicineViewHolder) holder).item.setText(item.getName());
                ((MedicineViewHolder) holder).item_price.setText(AddToCartView.toCurrency( String.format("%.2f", Double.parseDouble(item.getMRP()))));
                ((MedicineViewHolder) holder).manufacturer.setText("");
                ((MedicineViewHolder) holder).pack.setText(item.getPack());
                ((MedicineViewHolder) holder).add_to_cart.setValue(Integer.parseInt(""+String.format("%.1f",item.getQty())));



        }

    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
