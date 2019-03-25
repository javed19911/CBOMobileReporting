package saleOrder.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.R;

import cbomobilereporting.cbo.com.cboorder.DBHelper.ItemDB;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {

    private Context mContext;
    private mOrder order;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private Boolean keypressed = true;
    private ItemDB itemDB;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item, brand,item_price,brand_price,manufacturer,pack,pescription_yn,rate,amount;
        private AddToCartView add_to_cart;
        private LinearLayout brand_layout;
        private ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            item = (TextView) view.findViewById(R.id.item);
            brand = (TextView) view.findViewById(R.id.brand);
            item_price = (TextView) view.findViewById(R.id.item_price);
            manufacturer = (TextView) view.findViewById(R.id.manufacturer);
            pack = (TextView) view.findViewById(R.id.pack);
            brand_price = (TextView) view.findViewById(R.id.brand_price);
            pescription_yn = (TextView) view.findViewById(R.id.pescription_yn);
            add_to_cart = view.findViewById(R.id.add_to_cart);
            brand_layout = view.findViewById(R.id.brand_layout);
            rate = view.findViewById(R.id.rate);
            amount = view.findViewById(R.id.amount);


            delete = view.findViewById(R.id.delete);



            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recycleViewOnItemClickListener != null) {
                        recycleViewOnItemClickListener.onClick(v,getAdapterPosition(),false);
                    }
                }
            });

            if (!order.getStatus().equalsIgnoreCase("C")) {
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItem item = order.getItems().get(getAdapterPosition());
                        order.getItems().remove(getAdapterPosition());
                        //cart_db_helper.Delete(medicine);
                        notifyItemRemoved(getAdapterPosition());
                        if (recycleViewOnItemClickListener != null)
                            recycleViewOnItemClickListener.onClick(view,getAdapterPosition(),false);
                    }
                });

                add_to_cart.setOnValueChangeListner(new AddToCartView.OnValueChangeListener() {
                    @Override
                    public void OnValueChanged(View view, int value) {
                        mItem item = order.getItems().get(getAdapterPosition());
                        item.setQty("" + value);
                        //cart_db_helper.Insert(medicine);
                        amount.setText(AddToCartView.toCurrency(String.format("%.2f", Double.parseDouble(item.getAmt()))));
                        //rate.setText("X " + MyCustumApplication.toCurrency(String.format("%.2f", medicine.getMRP())));
                        if (recycleViewOnItemClickListener != null)
                            recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
                    }
                });
            }else{
                add_to_cart.setEnable(false);
            }


        }
    }


    public CartAdapter(Context mContext, mOrder order) {
        this.mContext = mContext;
        this.order = order;
        //itemDB = new ItemDB(mContext);

    }



    public void update(mOrder order) {
        this.order = order;
        notifyDataSetChanged();
    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        mItem item = order.getItems().get(position);
//        holder.brand.setText(medicine.getBRAND_NAME());
//        holder.brand_price.setText(MyCustumApplication.toCurrency(String.format("%.2f", medicine.getBRAND_MRP())));
        holder.item.setText(item.getName());
        holder.item_price.setText( AddToCartView.toCurrency(String.format("%.2f", Double.parseDouble(item.getMRP()))));
        //holder.item_price.setText(MyCustumApplication.toCurrency(String.format("%.2f", medicine.getMRP())));
        //holder.manufacturer.setText(medicine.getMFG_NAME());
        holder.pack.setText(item.getPack());
        holder.amount.setText( AddToCartView.toCurrency(String.format("%.2f", Double.parseDouble(item.getAmt()))));
        holder.rate.setText("X " +  AddToCartView.toCurrency(String.format("%.2f", Double.parseDouble(item.getRate()))));

        if (keypressed) {
            holder.add_to_cart.setValue(Integer.parseInt( item.getQty()));
        }

        //if (medicine.getPESCRIPTION_YN() == 0)
            holder.pescription_yn.setVisibility(View.GONE);
//        else
//            holder.pescription_yn.setVisibility(View.VISIBLE);

//        if (medicine.getIs_Generic() == 1)
            holder.brand_layout.setVisibility(View.GONE);
//        else
//            holder.brand_layout.setVisibility(View.VISIBLE);

    }

    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return order.getItems().size();
    }
}
