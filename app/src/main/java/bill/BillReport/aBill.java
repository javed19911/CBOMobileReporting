package bill.BillReport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.CustomDatePicker;

public class aBill extends RecyclerView.Adapter<aBill.MyViewHolder>  {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mBill> billlsit=new ArrayList<>();
    ArrayList<mBill> billlsitfilter=new ArrayList<mBill>();
    private Bill_interface bill_interface;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bill_no ,party_name,customer_name,bill_amt,bill_date,mobile,gst,amt,net_amt,payMode;
        ImageView edit,delete,billView;
        LinearLayout conatiner,detail_layout,main_layout;
        TextView expand;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            //rcpt_no=(TextView)view.findViewById(R.id.rcpt_no);
            party_name=(TextView) view.findViewById(R.id.party_name);
            bill_amt=(TextView) view.findViewById(R.id.amt);
            bill_no=(TextView) view.findViewById(R.id.doc_no);

            bill_date=(TextView) view.findViewById(R.id.date);
            expand = view.findViewById(R.id.expand);

            customer_name=(TextView) view.findViewById(R.id.cust_name);
            mobile=(TextView) view.findViewById(R.id.mobile);
            gst=(TextView) view.findViewById(R.id.gst);
            amt=(TextView) view.findViewById(R.id.amount);
            net_amt=(TextView) view.findViewById(R.id.netAmt);
            payMode=(TextView) view.findViewById(R.id.payMode);

            delete=(ImageView) view.findViewById(R.id.delete);
            edit=(ImageView) view.findViewById(R.id.edit);
            billView=(ImageView) view.findViewById(R.id.view);

            conatiner=(LinearLayout) view.findViewById(R.id.container);
            detail_layout = view.findViewById(R.id.detail_layout);
            main_layout = view.findViewById(R.id.main_layout);




        }
    }


    public aBill(Context context, ArrayList<mBill> billArrayListt){
        this.context = context;
        this.billlsit =  billArrayListt;
        this.billlsitfilter  = (ArrayList<mBill>) billlsit.clone();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bill_interface=(Bill_interface)context;

    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_row_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            mBill mbilllist = billlsitfilter.get(position);
            holder.bill_no.setText(mbilllist.getDOC_NO());
            //     holder.rec_name.setText(mbilllist.getParty().getName());
            holder.party_name.setText(mbilllist.getCOMPANY_NAME());
            holder.bill_amt.setText(String.format("%.2f", mbilllist.getNET_AMT()));
            holder.bill_date.setText(CustomDatePicker.formatDate(mbilllist.getDOC_DATE(), CustomDatePicker.ShowFormatOld));//DATE


        holder.customer_name.setText(mbilllist.getPartyName());
        holder.mobile.setText(mbilllist.getParty_mobile());
        holder.gst.setText(mbilllist.getTaxAmt());
        holder.amt.setText(mbilllist.getAmt());
        holder.net_amt.setText(""+mbilllist.getNET_AMT());
        holder.payMode.setText(""+mbilllist.getPayMode());

        holder.delete.setVisibility(mbilllist.getDelete()?View.VISIBLE:View.GONE);
        holder.edit.setVisibility(mbilllist.getEdit()?View.VISIBLE:View.GONE);

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    bill_interface.Edit_Bill(mbilllist);
                }
            });

            holder.main_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.expand.getText().toString().equalsIgnoreCase("+")){
                        holder.expand.setText("-");
                        holder.detail_layout.setVisibility(View.VISIBLE);
                    }else{
                        holder.expand.setText("+");
                        holder.detail_layout.setVisibility(View.GONE);
                    }
                }
            });


            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bill_interface.Delete_Bill(mbilllist);
                }
            });

            holder.billView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bill_interface.OnClick_Bill(mbilllist);
                }
            });
           /* holder.rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bill_interface.OnClick_Bill(mbilllist);
                }
            });*/
    }



    @Override
    public int getItemCount() {
        return billlsitfilter.size();
    }


    public void filter(String Query){
        billlsitfilter.clear();
        if (Query.trim().equals("")){
            billlsitfilter = (ArrayList<mBill>) billlsit.clone();
            notifyDataSetChanged();
            return;
        }

        for (mBill item : billlsit){
            if (item.getBILL_PRINT().toLowerCase().contains(Query.toLowerCase())){
                billlsitfilter.add(item);
            }
        }
        notifyDataSetChanged();
    }


    public interface Bill_interface {
        void Edit_Bill(mBill mbills);
        void Delete_Bill(mBill mbills);
        void OnClick_Bill(mBill mbills);

    }

}
