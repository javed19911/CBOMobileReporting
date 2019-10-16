package bill.stockEntry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import bill.Cart.aBillCart;
import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.AppAlert;
import utils_new.interfaces.RecycleViewOnItemClickListener;

public class aOpenCart extends RecyclerView.Adapter<aOpenCart.CartItemViewHolder> {

    private Context mContext;
    private mBillOrder order;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private Boolean keypressed = true;

    public aOpenCart(Context mContext, mBillOrder order) {
        this.mContext = mContext;
        this.order = order;

    }



    public void update(mBillOrder order) {
        this.order = order;
        notifyDataSetChanged();
    }

    @Override
    public CartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
       /* itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_bill_card, parent, false);
        return new ProductViewHolder(itemView);*/

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_open_card, parent, false);
        return new CartItemViewHolder(itemView);

    }

   /* @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        mBillItem item = order.getItems().get(position);

        holder.brand.setText(item.getName());
        holder.net_amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
        holder.batch.setText(item.getBATCH_NO());
        holder.pack.setText("( "+item.getPACK() + " )");
        holder.amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
        holder.Qty.setText(String.format("%.0f",item.getQty()));
                if (item.getDeal().getType() != eDeal.NA) {
                    holder.rate.setText(String.format("%.0f", item.getQty()) +
                            " X " + AddToCartView.toCurrency(String.format("%.2f", item.getSALE_RATE())) +
                            " + " + item.getFreeQty());
                }else{
                    holder.rate.setText(String.format("%.0f", item.getQty()) +
                            " X " + AddToCartView.toCurrency(String.format("%.2f", item.getSALE_RATE())));
                }
        holder.discountName.setText("Discount " + item.getDiscountStr());
        holder.discount.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getAmt() - item.getNetAmt()))));

                if (item.getGST().getSGST() == 0){
                    holder.LocalTax.setVisibility(View.GONE);
                    holder.centralTaxName.setText(eTax.IGST.name() +" @" + item.getGST().getCGST() +"%");
                }else{
                    holder.LocalTax.setVisibility(View.VISIBLE);
                    holder.centralTaxName.setText(eTax.CGST.name() +" @" + item.getGST().getCGST() +"%");
                }

        holder.LocalTaxName.setText(eTax.SGST.name() +" @ " + item.getGST().getSGST() +"%");

        holder.CGST_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getCGSTAmt()))));
        holder.SGST_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getSGSTAmt()))));
        holder.brand_tot_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getTotAmt()))));

       *//* holder.remark.setText(item.getRemark());
        holder.remarkTitle.setText(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARK_TITLE","Remark"));
        holder.remarkLayout.setVisibility(item.getRemarkReqd()? View.VISIBLE: View.GONE);*//*




    }*/


    @Override
    public void onBindViewHolder(final CartItemViewHolder holder, int position) {
        mBillItem item = order.getItems().get(position);

        holder.brand.setText(item.getName());
        holder.net_amount.setText( String.format("%.2f", item.getNetAmt()));
        holder.brand_rate.setText( String.format("%.2f", item.getSALE_RATE()));
        holder.batch.setText(item.getBATCH_NO());
        holder.brand_batch.setText(item.getBATCH_NO());
        holder.pack.setText(item.getPACK() );
        holder.brand_pack.setText(item.getPACK() );
        holder.amount.setText( String.format("%.2f", item.getAmt()));
        holder.Qty.setText(String.format("%.0f",item.getQty()));
        holder.MRPDet.setText(String.format("%.2f",item.getMRP_RATE()));

        holder.FreeQty.setText( String.format("%.0f", item.getFreeQty()));
        holder.QtyDet.setText(String.format("%.0f",item.getQty()));


        if (item.getDeal().getType() != eDeal.NA) {
            holder.rate.setText(String.format("%.0f", item.getQty()) +
                    " X " + AddToCartView.toCurrency(String.format("%.2f", item.getSALE_RATE())) +
                    " + " + item.getFreeQty());
        }else{
            holder.rate.setText(String.format("%.0f", item.getQty()) +
                    " X " + AddToCartView.toCurrency(String.format("%.2f", item.getSALE_RATE())));
        }
        //holder.discountName.setText("Discount " + item.getDiscountStr());
        holder.discount.setText(String.format("%.2f",(item.getAmt() - item.getNetAmt())));

        if (item.getGST().getSGST() == 0){
            holder.LocalTax.setVisibility(View.GONE);
            holder.centralTaxName.setText(eTax.IGST.name() +" @" + item.getGST().getCGST() +"%");
        }else{
            holder.LocalTax.setVisibility(View.VISIBLE);
            holder.centralTaxName.setText(eTax.CGST.name() +" @" + item.getGST().getCGST() +"%");
        }

        holder.LocalTaxName.setText(eTax.SGST.name() +" @ " + item.getGST().getSGST() +"%");

        holder.CGST_amt.setText(String.format("%.2f",(item.getCGSTAmt())));
        holder.SGST_amt.setText(String.format("%.2f",(item.getSGSTAmt())));
        holder.brand_tot_amt.setText(String.format("%.2f",(item.getTotAmt())));

       /* holder.remark.setText(item.getRemark());
        holder.remarkTitle.setText(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARK_TITLE","Remark"));
        holder.remarkLayout.setVisibility(item.getRemarkReqd()? View.VISIBLE: View.GONE);*/




    }


    public class CartItemViewHolder extends RecyclerView.ViewHolder {

        private TextView brand,pack,brand_pack,batch,brand_batch,rate,amount,net_amount,brand_rate,brand_tot_amt,SGST_amt,CGST_amt;
        private TextView Qty,discountName,discount,centralTaxName,LocalTaxName,remark,remarkTitle,expand,MRPDet;
        private TextView QtyDet,FreeQty;
        private ImageView delete,edit;
        private LinearLayout extraLayout,centralTax,LocalTax,remarkLayout;

        public CartItemViewHolder(View view) {
            super(view);
            expand = (TextView) view.findViewById(R.id.expand);
            brand = (TextView) view.findViewById(R.id.brand);
            net_amount = (TextView) view.findViewById(R.id.brand_net_amt);
            brand_rate = view.findViewById(R.id.brand_rate);
            brand_batch = (TextView) view.findViewById(R.id.brand_batch);
            batch = (TextView) view.findViewById(R.id.batch);
            brand_pack = (TextView) view.findViewById(R.id.brand_pack);
            pack = (TextView) view.findViewById(R.id.pack);
            Qty = (TextView) view.findViewById(R.id.Qty);
            rate = view.findViewById(R.id.rate);
            amount = view.findViewById(R.id.amount);
            discountName = view.findViewById(R.id.discountName);
            discount = view.findViewById(R.id.discount);
            MRPDet = view.findViewById(R.id.MRPDet);

            QtyDet = view.findViewById(R.id.QtyDet);
            FreeQty = view.findViewById(R.id.FreeQty);

            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);


            centralTax = view.findViewById(R.id.centralTax);
            LocalTax = view.findViewById(R.id.LocalTax);
            centralTaxName = view.findViewById(R.id.centralTaxName);
            LocalTaxName = view.findViewById(R.id.LocalTaxName);
            SGST_amt = view.findViewById(R.id.SGST_amt);
            CGST_amt = view.findViewById(R.id.CGST_amt);

            brand_tot_amt = view.findViewById(R.id.brand_tot_amt);

            extraLayout = view.findViewById(R.id.extraLayout);

            remark = view.findViewById(R.id.remark);
            remarkTitle = view.findViewById(R.id.remarkTitle);
            remarkLayout = view.findViewById(R.id.remarkLayout);



            if (order.getDocId ().equalsIgnoreCase ("0") || order.getStatus().equalsIgnoreCase("E")) {
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //mItem item = order.getItems().get(getAdapterPosition());
                        AppAlert.getInstance().DecisionAlert(mContext,
                                "Delete !!!", "Are you sure to delete "+order.getItems().get(getAdapterPosition()).getName()+" ?",
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
                    if (extraLayout.getVisibility() == View.VISIBLE){
                        //more.setRotation(0);
                        expand.setText("+");
                        extraLayout.setVisibility(View.GONE);
                    }else{
                        //more.setRotation(180);
                        expand.setText("-");
                        extraLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private TextView brand,pack,batch,rate,amount,net_amount,brand_tot_amt,SGST_amt,CGST_amt;
        private TextView Qty,discountName,discount,centralTaxName,LocalTaxName,remark,remarkTitle;
        private ImageView delete,edit;
        private ImageButton more;
        private RelativeLayout sapratorLayout;
        private LinearLayout extraLayout,centralTax,LocalTax,remarkLayout;

        public ProductViewHolder(View view) {
            super(view);
            brand = (TextView) view.findViewById(R.id.brand);
            net_amount = (TextView) view.findViewById(R.id.brand_net_amt);
            batch = (TextView) view.findViewById(R.id.brand_batch);
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


            if (!order.getStatus().equalsIgnoreCase("V")) {
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
