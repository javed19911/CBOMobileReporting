package saleOrder.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import cbomobilereporting.cbo.com.cboorder.DBHelper.ItemDB;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Model.mItem;
import cbomobilereporting.cbo.com.cboorder.Model.mOrder;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import cbomobilereporting.cbo.com.cboorder.interfaces.RecycleViewOnItemClickListener;
import saleOrder.Enum.eItem;
import utils_new.AppAlert;


public class CartAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private mOrder order;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private Boolean keypressed = true;
    private ItemDB itemDB;
    private eItem itemType = eItem.MEDICINE;

    public CartAdapter(Context mContext, mOrder order,eItem itemType) {
        this.mContext = mContext;
        this.order = order;
        this.itemType = itemType;

    }



    public void update(mOrder order) {
        this.order = order;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        switch (itemType){
            case PRODUCT:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_card, parent, false);
                return new ProductViewHolder(itemView);
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
                return new MedicineViewHolder(itemView);

        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        mItem item = order.getItems().get(position);
        switch (itemType){
            case PRODUCT:


                ((ProductViewHolder) holder).brand.setText(item.getName());
                ((ProductViewHolder) holder).net_amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getNetAmt())));
                ((ProductViewHolder) holder).pack.setText("Pack : "+item.getPack());
                ((ProductViewHolder) holder).amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
                ((ProductViewHolder) holder).Qty.setText(String.format("%.0f",item.getQty()));
                if (item.getDeal().getType() != eDeal.NA) {
                    ((ProductViewHolder) holder).rate.setText(String.format("%.0f", item.getQty()) +
                            " X " + AddToCartView.toCurrency(String.format("%.2f", item.getRate())) +
                            " + " + item.getFreeQty());
                }else{
                    ((ProductViewHolder) holder).rate.setText(String.format("%.0f", item.getQty()) +
                            " X " + AddToCartView.toCurrency(String.format("%.2f", item.getRate())));
                }
                ((ProductViewHolder) holder).discountName.setText("Discount " + item.getDiscountStr());
                ((ProductViewHolder) holder).discount.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getAmt() - item.getNetAmt()))));

                if (item.getGST().getSGST() == 0){
                    ((ProductViewHolder) holder).LocalTax.setVisibility(View.GONE);
                    ((ProductViewHolder) holder).centralTaxName.setText(eTax.IGST.name() +" @" + item.getGST().getCGST() +"%");
                }else{
                    ((ProductViewHolder) holder).LocalTax.setVisibility(View.VISIBLE);
                    ((ProductViewHolder) holder).centralTaxName.setText(eTax.CGST.name() +" @" + item.getGST().getCGST() +"%");
                }

                ((ProductViewHolder) holder).LocalTaxName.setText(eTax.SGST.name() +" @ " + item.getGST().getSGST() +"%");

                ((ProductViewHolder) holder).CGST_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getCGSTAmt()))));
                ((ProductViewHolder) holder).SGST_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getSGSTAmt()))));
                ((ProductViewHolder) holder).brand_tot_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getTotAmt()))));

                ((ProductViewHolder) holder).remark.setText(item.getRemark());
                ((ProductViewHolder) holder).remarkTitle.setText(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARK_TITLE","Remark"));
                ((ProductViewHolder) holder).remarkLayout.setVisibility(item.getRemarkReqd()? View.VISIBLE: View.GONE);




                break;
            default:


                ((MedicineViewHolder) holder).item.setText(item.getName());
                ((MedicineViewHolder) holder).item_price.setText( AddToCartView.toCurrency(String.format("%.2f", Double.parseDouble(item.getMRP()))));
                ((MedicineViewHolder) holder).pack.setText(item.getPack());
                ((MedicineViewHolder) holder).amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
                ((MedicineViewHolder) holder).rate.setText(" X " +  AddToCartView.toCurrency(String.format("%.2f", item.getRate())));

                if (keypressed) {
                    ((MedicineViewHolder) holder).add_to_cart.setValue(Integer.parseInt( "" +String.format("%.1f",item.getQty())));
                }

                ((MedicineViewHolder) holder).pescription_yn.setVisibility(View.GONE);
                ((MedicineViewHolder) holder).brand_layout.setVisibility(View.GONE);


        }

    }


    public class MedicineViewHolder extends RecyclerView.ViewHolder {
        private TextView item, brand,item_price,brand_price,manufacturer,pack,pescription_yn,rate,amount;
        private AddToCartView add_to_cart;
        private LinearLayout brand_layout;
        private ImageView delete;

        public MedicineViewHolder(View view) {
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


            if (!order.getStatus().equalsIgnoreCase("C")) {
                delete.setVisibility(View.VISIBLE);
                add_to_cart.setEnable(true);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mItem item = order.getItems().get(getAdapterPosition());
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
                        item.setQty(Double.parseDouble( "" + value));
                        //cart_db_helper.Insert(medicine);
                        amount.setText(AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
                        //rate.setText("X " + MyCustumApplication.toCurrency(String.format("%.2f", medicine.getMRP())));
                        if (recycleViewOnItemClickListener != null)
                            recycleViewOnItemClickListener.onClick(view, getAdapterPosition(), false);
                    }
                });

            }else{
                delete.setVisibility(View.GONE);
                add_to_cart.setEnable(false);
            }
        }
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView brand,pack,rate,amount,net_amount,brand_tot_amt,SGST_amt,CGST_amt;
        private TextView Qty,discountName,discount,centralTaxName,LocalTaxName,remark,remarkTitle;
        private ImageView delete,edit;
        private ImageButton more;
        private RelativeLayout sapratorLayout;
        private LinearLayout extraLayout,centralTax,LocalTax,remarkLayout;

        public ProductViewHolder(View view) {
            super(view);
            brand = (TextView) view.findViewById(R.id.brand);
            net_amount = (TextView) view.findViewById(R.id.brand_net_amt);
            pack = (TextView) view.findViewById(R.id.brand_pack);
            Qty = (TextView) view.findViewById(R.id.Qty);
            rate = view.findViewById(R.id.rate);
            amount = view.findViewById(R.id.amount);
            discountName = view.findViewById(R.id.discountName);
            discount = view.findViewById(R.id.discount);

            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
            more = view.findViewById(R.id.more);

            centralTax = view.findViewById(R.id.centralTax);
            LocalTax = view.findViewById(R.id.LocalTax);
            centralTaxName = view.findViewById(R.id.centralTaxName);
            LocalTaxName = view.findViewById(R.id.LocalTaxName);
            SGST_amt = view.findViewById(R.id.SGST_amt);
            CGST_amt = view.findViewById(R.id.CGST_amt);

            brand_tot_amt = view.findViewById(R.id.brand_tot_amt);

            sapratorLayout = view.findViewById(R.id.sapratorLayout);
            extraLayout = view.findViewById(R.id.extraLayout);

            remark = view.findViewById(R.id.remark);
            remarkTitle = view.findViewById(R.id.remarkTitle);
            remarkLayout = view.findViewById(R.id.remarkLayout);


            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sapratorLayout.performClick();
                }
            });

            sapratorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (extraLayout.getVisibility() == View.VISIBLE){
                        more.setRotation(0);
                        extraLayout.setVisibility(View.GONE);
                    }else{
                        more.setRotation(180);
                        extraLayout.setVisibility(View.VISIBLE);
                    }
                }
            });


            if (!order.getStatus().equalsIgnoreCase("C")
                    && order.getBilledHO() == 0
                    && !order.getApproved().equalsIgnoreCase("Y")
            ) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mItem item = order.getItems().get(getAdapterPosition());
                        AppAlert.getInstance().DecisionAlert(mContext,
                                "Delete !!!", "Are you to sure to delete ?",
                                new AppAlert.OnClickListener() {
                                    @Override
                                    public void onPositiveClicked(View item, String result) {
                                        order.getItems().remove(getAdapterPosition());
                                        notifyItemRemoved(getAdapterPosition());
                                        if (recycleViewOnItemClickListener != null)
                                            recycleViewOnItemClickListener.onClick(v,getAdapterPosition(),false);
                                    }

                                    @Override
                                    public void onNegativeClicked(View item, String result) {

                                    }
                                });
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recycleViewOnItemClickListener != null)
                            recycleViewOnItemClickListener.onClick(v,getAdapterPosition(),false);
                    }
                });



            }else{
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sapratorLayout.performClick();
                }
            });

        }
    }



    public void setOnClickListner(RecycleViewOnItemClickListener recycleViewOnItemClickListener){
        this.recycleViewOnItemClickListener = recycleViewOnItemClickListener;
    }


    @Override
    public int getItemCount() {
        return order.getItems().size();
    }
}
