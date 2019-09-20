package bill.BillReport;

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

public class aBill extends RecyclerView.Adapter<aBill.MyViewHolder>  {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mBill> billlsit=new ArrayList<>();
    ArrayList<mBill> billlsitfilter=new ArrayList<mBill>();
    private Bill_interface bill_interface;





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
                .inflate(R.layout.reciept_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        mBill mbilllist = billlsitfilter.get(position);
        holder.rec_remak.setText(mbilllist.getBILL_PRINT());
   //     holder.rec_name.setText(mbilllist.getParty().getName());
       holder.rec_name.setText(mbilllist.getCOMPANY_NAME());
        holder.rec_amt.setText(AddToCartView.toCurrency(String.format("%.2f",mbilllist.getNET_AMT())));
        holder.rcpt_date.setText(CustomDatePicker.formatDate( mbilllist.getDOC_DATE(),CustomDatePicker.ShowFormat));//DATE

        holder.edit_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bill_interface.Edit_Bill(mbilllist);
            }
        });

        holder.delete_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_interface.Delete_Bill(mbilllist);
            }
        });

        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bill_interface.OnClick_Bill(mbilllist);
            }
        });
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
          /*  if (item.getParty().getName().toLowerCase().contains(Query.toLowerCase())){
                billlsitfilter.add(item);
            }*/
        }
        notifyDataSetChanged();
    }


    public interface Bill_interface {
        void Edit_Bill(mBill mbills);
        void Delete_Bill(mBill mbills);
        void OnClick_Bill(mBill mbills);

    }

}
