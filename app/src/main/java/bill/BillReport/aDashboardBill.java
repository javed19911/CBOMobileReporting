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

import utils_new.CustomDatePicker;

public class aDashboardBill extends RecyclerView.Adapter<aDashboardBill.MyViewHolder>  {

    Context context;
    LayoutInflater layoutInflater;
    ArrayList<mBill> billlsit=new ArrayList<>();
    ArrayList<mBill> billlsitfilter=new ArrayList<mBill>();
    private Bill_interface bill_interface;





    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView bill_no ,bill_date,bill_amt,payMode;
        TextView expand;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            //rcpt_no=(TextView)view.findViewById(R.id.rcpt_no);
            bill_amt=(TextView) view.findViewById(R.id.amt);
            bill_no=(TextView) view.findViewById(R.id.doc_no);

            bill_date=(TextView) view.findViewById(R.id.date);
            payMode=(TextView) view.findViewById(R.id.payMode);






        }
    }


    public aDashboardBill(Context context, ArrayList<mBill> billArrayListt){
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
                .inflate(R.layout.dashboard_bill_row_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


            mBill mbilllist = billlsitfilter.get(position);
            holder.bill_no.setText(mbilllist.getDOC_NO());

            holder.bill_amt.setText(String.format("%.2f", mbilllist.getNET_AMT()));
            holder.bill_date.setText(CustomDatePicker.formatDate(mbilllist.getDOC_DATE(), CustomDatePicker.ShowFormatOld));//DATE


        holder.payMode.setText(""+mbilllist.getPayMode());


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
