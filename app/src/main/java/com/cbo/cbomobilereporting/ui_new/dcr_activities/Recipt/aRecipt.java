package com.cbo.cbomobilereporting.ui_new.dcr_activities.Recipt;

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
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.eExpense;
import com.cbo.cbomobilereporting.ui_new.dcr_activities.Expense.mOthExpense;

import java.text.ParseException;
import java.util.ArrayList;

import cbomobilereporting.cbo.com.cboorder.Utils.AddToCartView;
import utils_new.CustomDatePicker;

public class aRecipt extends RecyclerView.Adapter<aRecipt.MyViewHolder>  {

  Context context;
  LayoutInflater layoutInflater;
  ArrayList<mRecipt> recieptlsit=new ArrayList<>();
  ArrayList<mRecipt> recieptlsitfilter=new ArrayList<mRecipt>();
  private  Recipt_interface recipt_interface;





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


  public aRecipt(Context context, ArrayList<mRecipt> recieptlsit){
    this.context = context;
    this.recieptlsit = recieptlsit;
    this.recieptlsitfilter  = (ArrayList<mRecipt>) recieptlsit.clone();
    layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.recipt_interface=(Recipt_interface)context;

  }


  @NonNull
  @Override
  public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.reciept_view, parent, false);

    return new MyViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


    mRecipt mrecipt = recieptlsitfilter.get(position);
    holder.rec_remak.setText(mrecipt.getRemark());
    holder.rec_name.setText(mrecipt.getParty().getName());
    holder.rec_amt.setText(AddToCartView.toCurrency(String.format("%.2f",mrecipt.getAmount())));
    holder.rcpt_date.setText(CustomDatePicker.formatDate( mrecipt.getDoc_Date(),CustomDatePicker.ShowFormat));//DATE


    holder.edit_rec.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        recipt_interface.Edit_Recipt(mrecipt);
      }
    });

    holder.delete_rec.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        recipt_interface.Delete_Recipt(mrecipt);
      }
    });

    holder.rowView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        recipt_interface.OnClick_Recipt(mrecipt);
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
      recieptlsitfilter = (ArrayList<mRecipt>) recieptlsit.clone();
      notifyDataSetChanged();
      return;
    }

    for (mRecipt item : recieptlsit){
      if (item.getParty().getName().toLowerCase().contains(Query.toLowerCase())){
        recieptlsitfilter.add(item);
      }
    }
    notifyDataSetChanged();
  }


  public interface Recipt_interface {
     void Edit_Recipt(mRecipt mrecipt);
     void Delete_Recipt(mRecipt mrecipt);
     void OnClick_Recipt(mRecipt mrecipt);

  }

}