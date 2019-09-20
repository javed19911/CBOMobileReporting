package bill.NewOrder;/*
package com.cbo.cbomobilereporting.ui_new.dcr_activities.Bill.NewOrder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;
import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.CustomDatePicker;

public class aNewItemFilter  extends RecyclerView.Adapter<aNewItemFilter.MyViewHolder>  {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mBillItem> recieptlsit=new ArrayList<>();
    ArrayList<mBillItem> recieptlsitfilter=new ArrayList<mBillItem>();
    private aNewItemFilter_interface bill_interface;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rcpt_no ,rec_id,rec_name,rec_amt,rec_remak,rcpt_date;
        ImageView edit_rec,delete_rec,img_view;
        LinearLayout conatiner;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            //rcpt_no=(TextView)view.findViewById(R.id.rcpt_no);
            rec_name=(TextView) view.findViewById(R.id.rec_name);
            rec_amt=(TextView) view.findViewById(R.id.rec_amt);
            rec_remak=(TextView) view.findViewById(R.id.rec_remak);
            edit_rec=(ImageView) view.findViewById(R.id.rcpt_edit);
            rcpt_date=(TextView) view.findViewById(R.id.rcpt_date);

            delete_rec=(ImageView) view.findViewById(R.id.delete_rec);
            conatiner=(LinearLayout) view.findViewById(R.id.container);




        }
    }


    public aNewItemFilter(Context context, ArrayList<mBillItem> recieptlsit){
        this.context = context;
        this.recieptlsit = recieptlsit;
        this.recieptlsitfilter  = (ArrayList<mBillItem>) recieptlsit.clone();
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.bill_interface=(aNewItemFilter.aNewItemFilter_interface)context;

    }


    @NonNull
    @Override
    public aNewItemFilter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reciept_view, parent, false);

        return new aNewItemFilter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull aNewItemFilter.MyViewHolder holder, int position) {


        mBillItem mBillItem = recieptlsitfilter.get(position);
        holder.rec_remak.setText(mBillItem.getRemark());
        holder.rec_name.setText(mBillItem.getParty().getName());
        holder.rec_amt.setText(AddToCartView.toCurrency(String.format("%.2f",mBillItem.getAmount())));
        holder.rcpt_date.setText(CustomDatePicker.formatDate( mBillItem.getDoc_Date(),CustomDatePicker.ShowFormat));//DATE



        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_interface.OnClick_Recipt(mBillItem);
            }
        });
    }



    @Override
    public int getItemCount() {
        return recieptlsitfilter.size();
    }


    public void filter(String Query){
        recieptlsitfilter.clear();
        if (Query.trim().equals("")){
            recieptlsitfilter = (ArrayList<mBillItem>) recieptlsit.clone();
            notifyDataSetChanged();
            return;
        }

        for (mBillItem item : recieptlsit){
            if (item.getParty().getName().toLowerCase().contains(Query.toLowerCase())){
                recieptlsitfilter.add(item);
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
*/
