package Bill.ItemFilter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import Bill.NewOrder.mBillItem;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import saleOrder.Enum.eItem;

public class aCompanyItemFilter extends RecyclerView.Adapter  {

    private Context mContext;
    private ArrayList<mBillItem> items;
    private ArrayList<mBillItem> itemscopy;
    private eItem itemType = eItem.MEDICINE;
    //RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private aNewItemFilter_interface aNewItemFilterInterface;

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
               //     items.get(getAdapterPosition()).setQty(Double.parseDouble( ""+add_to_cart.getValue()));
                    /*if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                    }*/
                }
            });
        }



    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        public TextView item, brand,item_price,brand_price,manufacturer,pack,pescription_yn;
        private AddToCartView add_to_cart;
        private ImageView itemImg;
        View rowView;

        public ProductViewHolder(View view) {
            super(view);
            this.rowView=view;
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
/*
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                    }*//*

                    bill_interface.OnClick_Recipt(mBillItem);
                }
            });



            holder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aNewItemFilterInterface.OnClick_Recipt(mBillItem);
                }
            });*/
        }
    }




    public aCompanyItemFilter(Context mContext, ArrayList<mBillItem> items,eItem itemType) {
        this.mContext = mContext;
        this.items = items;
        this.itemType = itemType;
        this.aNewItemFilterInterface=(aNewItemFilter_interface)mContext;
        itemscopy = (ArrayList<mBillItem>) items.clone();
    }


    public ArrayList<mBillItem> getItems(){
        return items;
    }

    public void update(ArrayList<mBillItem> items) {
        this.items = items;
        itemscopy = (ArrayList<mBillItem>) items.clone();
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (itemType){
            case PRODUCT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comapny_medicine_card, parent, false);
                return new ProductViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.comapny_medicine_card, parent, false);
                return new MedicineViewHolder(itemView);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        mBillItem item = itemscopy.get(position);
        switch (itemType){
            case PRODUCT:

                ((ProductViewHolder) holder).item.setText(item.getName());
               ((ProductViewHolder) holder).item_price.setText(AddToCartView.toCurrency( String.format("%.2f", item.getMRP_RATE())));
                ((ProductViewHolder) holder).manufacturer.setText("");
               ((ProductViewHolder) holder).pack.setText(item.getPACK());
             //   ((aCompanyItemFilter.ProductViewHolder) holder).add_to_cart.setValue(Integer.parseInt(""+String.format("%.0f",item.getQty())));


                break;
            default:

                ((MedicineViewHolder) holder).item.setText(item.getName());
                ((MedicineViewHolder) holder).item_price.setText(AddToCartView.toCurrency( String.format("%.2f", Double.parseDouble(item.getMRP_RATE()))));
                ((MedicineViewHolder) holder).manufacturer.setText("");
                ((MedicineViewHolder) holder).pack.setText(item.getPACK());
                ((MedicineViewHolder) holder).pack.setVisibility(View.GONE);
             //  ((aCompanyItemFilter.MedicineViewHolder) holder).add_to_cart.setValue(Integer.parseInt(""+String.format("%.1f",item.getQty())));



        }
    holder.itemView.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            aNewItemFilterInterface.OnClick_Recipt(item);
            Toast.makeText(mContext, "YOU ARE AWSUME", Toast.LENGTH_SHORT).show();
          }
        });
    }
/*

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }

*/

    @Override
    public int getItemCount() {
        return itemscopy.size();
    }

    public void filter(String Qry){
        int textlength = Qry.length();
        itemscopy.clear();
        for (int i = 0; i < items.size(); i++) {
            if (textlength <= items.get(i).getName().length()) {

                if (items.get(i).getName().toLowerCase().contains(Qry.toLowerCase().trim())) {
                    itemscopy.add(items.get(i));
                }
            }
        }

        notifyDataSetChanged();

    }

    public interface aNewItemFilter_interface {
        //  void Edit_Recipt(mBillItem mBillItem);
        // void Delete_Recipt(mBillItem mBillItem);
        void OnClick_Recipt(mBillItem billItem);

    }
}
