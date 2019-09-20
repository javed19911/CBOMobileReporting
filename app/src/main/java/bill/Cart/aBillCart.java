package bill.Cart;

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

import com.cbo.cbomobilereporting.MyCustumApplication;
import com.cbo.cbomobilereporting.R;

import bill.NewOrder.mBillItem;
import bill.mBillOrder;
import cbomobilereporting.cbo.com.cboorder.DBHelper.ItemDB;
import cbomobilereporting.cbo.com.cboorder.Enum.eDeal;
import cbomobilereporting.cbo.com.cboorder.Enum.eTax;
import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import saleOrder.Enum.eItem;
import utils_new.AppAlert;
import utils_new.interfaces.RecycleViewOnItemClickListener;


public class aBillCart extends RecyclerView.Adapter<aBillCart.ProductViewHolder> {

    private Context mContext;
    private mBillOrder order;
    RecycleViewOnItemClickListener recycleViewOnItemClickListener = null;
    private Boolean keypressed = true;

    public aBillCart(Context mContext, mBillOrder order) {
        this.mContext = mContext;
        this.order = order;

    }



    public void update(mBillOrder order) {
        this.order = order;
        notifyDataSetChanged();
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_product_card, parent, false);
        return new ProductViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        mBillItem item = order.getItems().get(position);

        holder.brand.setText(item.getName());
        holder.net_amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
        holder.pack.setText("Pack : "+item.getPACK());
        holder.amount.setText( AddToCartView.toCurrency(String.format("%.2f", item.getAmt())));
        holder.Qty.setText(String.format("%.0f",item.getQty()));
       /*         if (item.getDeal().getType() != eDeal.NA) {
                    holder.rate.setText(String.format("%.0f", item.getQty()) +
                            " X " + AddToCartView.toCurrency(String.format("%.2f", item.getRate())) +
                            " + " + item.getFreeQty());
                }else{
                    holder.rate.setText(String.format("%.0f", item.getQty()) +
                            " X " + AddToCartView.toCurrency(String.format("%.2f", item.getRate())));
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

        holder.LocalTaxName.setText(eTax.SGST.name() +" @ " + item.getGST().getSGST() +"%");*/

       /* holder.CGST_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getCGSTAmt()))));
        holder.SGST_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getSGSTAmt()))));*/
        holder.brand_tot_amt.setText(AddToCartView.toCurrency(String.format("%.2f",(item.getTotAmt()))));

       /* holder.remark.setText(item.getRemark());
        holder.remarkTitle.setText(MyCustumApplication.getInstance().getDataFrom_FMCG_PREFRENCE("SALE_ORDER_REMARK_TITLE","Remark"));
        holder.remarkLayout.setVisibility(item.getRemarkReqd()? View.VISIBLE: View.GONE);*/




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
