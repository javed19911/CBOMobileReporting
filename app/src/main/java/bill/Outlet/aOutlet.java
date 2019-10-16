package bill.Outlet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cbo.cbomobilereporting.R;

import java.util.ArrayList;

import bill.BillReport.BillActivity;
import bill.BillReport.DashboardBill;
import utils_new.UnderlineTextView;


public class aOutlet extends RecyclerView.Adapter<aOutlet.MyViewHolder> {


    private Context context;
    private ArrayList<mOutlet> list;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        UnderlineTextView name;
        TextView OthSale,TotSale,CashSale,bills;
        LinearLayout conatiner,detail_layout,main_layout;
        View rowView;


        public MyViewHolder(View view) {
            super(view);
            this.rowView=view;
            //rcpt_no=(TextView)view.findViewById(R.id.rcpt_no);
            name= view.findViewById(R.id.name);
            CashSale=(TextView) view.findViewById(R.id.CashSale);
            TotSale=(TextView) view.findViewById(R.id.TotSale);

            OthSale=(TextView) view.findViewById(R.id.OthSale);
            conatiner=(LinearLayout) view.findViewById(R.id.container);
            bills = view.findViewById(R.id.bill_count);

        }
    }


    public aOutlet(Context context, ArrayList<mOutlet> list){
        this.context = context;
        this.list =  list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outlet_row_view, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        mOutlet outlet = list.get(position);
        holder.name.setText(outlet.getCOMPANY_NAME());
        holder.TotSale.setText(outlet.getTOTAL_SALE());
        holder.OthSale.setText(outlet.getOTHER_SALE());
        holder.CashSale.setText(outlet.getCASH_SALE());
        holder.bills.setText(outlet.getNO_BILL());


        holder.rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DashboardBill.class);
                intent.putExtra("outlet",outlet);
                intent.putExtra("title",outlet.getCOMPANY_NAME());
                context.startActivity(intent);
            }
        });





    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}
